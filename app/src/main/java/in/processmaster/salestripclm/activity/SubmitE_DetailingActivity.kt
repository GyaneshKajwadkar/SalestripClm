package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.PobProductAdapter
import `in`.processmaster.salestripclm.adapter.SelectedPobAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctorIdDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.interfaceCode.PobProductTransfer
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_submit_edetailing.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.bottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_visualads.close_imv
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.noDataCheckAdapter_tv
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.pob_product_bottom_sheet.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


class SubmitE_DetailingActivity : BaseActivity(), IdNameBoll_interface, PobProductTransfer,
    productTransfer {

    var visualSendModel= ArrayList<VisualAdsModel_Send>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetPobProduct: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var sampleArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var stokistArray=ArrayList<IdNameBoll_model>()
    var selectionType=0
    var selectedPurposeID=0
    var dcrId=0
    var pobProductSelectAdapter=PobProductAdapter()
    var selectedPobAdapter=SelectedPobAdapter()
    var passingSchemeList:ArrayList<SyncModel.Data.Scheme> = ArrayList()
    var selectedStockist=IdNameBoll_model()
    var commonSlectionAdapter=CheckboxSpinnerAdapter(ArrayList(),this)
  //  var sendEDetailingArray:ArrayList<Send_EDetailingModel.PobObj.PobDetailList> = ArrayList()

    //pob initilize------
    var mainProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var unSelectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_edetailing)

        alertClass.showProgressAlert("")
        val executor = Executors.newFixedThreadPool(3)
        val handler = Handler(Looper.getMainLooper())

        val runnable = java.lang.Runnable {
            if(!PreferenceClass(this).getPref("dcrId").isEmpty())
            {
                dcrId= PreferenceClass(this).getPref("dcrId").toInt()
            }

            if(intent.getIntExtra("doctorID",0)!=0) doctorIdDisplayVisual= intent.getIntExtra("doctorID",0)!!

            val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)

            var listSample = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Sample"}
            var listStokist = staticSyncData?.retailerList?.filter { s -> s.type == "STOCKIST" }
            var Gift = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Gift"}
            var listGift = Gift!!.filter { s -> s.actualBalanceQty != 0}

            for(stockist in listStokist!!)
            {
                val data =IdNameBoll_model()
                data.id= stockist.retailerId.toString()
                data.city= stockist.cityName.toString()
                data.name= stockist.shopName.toString()
                stokistArray.add(data)
            }

            for(workWith in staticSyncData?.workingWithList!!)
            {
                val data =IdNameBoll_model()
                data.id= workWith.empId.toString()
                data.name= workWith.fullName.toString()
                workWithArray.add(data)
            }

            for(sample in listSample)
            {
                val data =IdNameBoll_model()
                data.id= sample.productId.toString()
                data.name=sample?.productName!!
                data.availableQty=sample?.actualBalanceQty?.toInt()!!
                sampleArray.add(data)
            }

            for(gift in listGift)
            {
                val data =IdNameBoll_model()
                data.id= gift.productId.toString()
                data.name=gift?.productName!!
                data.availableQty=gift?.actualBalanceQty?.toInt()!!
                giftArray.add(data)
            }

            val string = Gson().toJson(staticSyncData)
            val data= Gson().fromJson(string, SyncModel.Data::class.java)
            mainProductList.addAll(data.productList.filter { s -> (s.productType==1) } as ArrayList<SyncModel.Data.Product>)
            unSelectedProductList=ArrayList(mainProductList)

            val getSchemeList=staticSyncData?.schemeList
            val filterByTypeSchemeList= getSchemeList?.filter { data -> (data?.schemeFor=="S" || data?.schemeFor=="H") }

            val getDocDetail: SyncModel.Data.Doctor? = staticSyncData?.doctorList?.find { it.doctorId == doctorIdDisplayVisual }

            getSchemeList?.clear()
            filterByTypeSchemeList?.sortedBy { it.schemeFor }?.let { getSchemeList?.addAll(it) }

            getSchemeList?.let { passingSchemeList.addAll(it)}

            getSchemeList?.forEachIndexed { indexH, hElement ->

                val separated: Array<String>? = hElement.schemeForId?.split(",")?.toTypedArray()
                val event: String? = separated?.find { it ==getDocDetail?.fieldStaffId?.toString() }

                if(hElement.schemeFor.equals("H") && event!="")
                {
                    getSchemeList?.forEachIndexed { indexS, SElement ->

                        val separated: Array<String>? = SElement.schemeForId?.split(",")?.toTypedArray()
                        val event: String? = separated?.find { it ==getDocDetail?.stateId?.toString() }
                        if(SElement.schemeFor.equals("S") && event!="")
                        {
                            if(hElement.productId==SElement.productId) {
                                passingSchemeList.removeAt(indexS)
                            }
                        }
                    }
                }
            }

            val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(this,
                R.layout.spinner_txt, CommonListGetClass().getWorkTypeForSpinner())

            if(intent.hasExtra("doctorName"))
            {
                if(intent.getBooleanExtra("skip",false))
                {
                    dbBase.deleteAllVisualAds()
                    dbBase.deleteAllChildVisual()
                }
                else
                { }

                visualSendModel = dbBase.getAllSubmitVisual()
                runOnUiThread(){
                    if(visualSendModel.size==0)nodata_gif.visibility=View.VISIBLE
                }

            }
            else
            {

            }
            if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
            { getUpdateData()
            }
            getResumeData()
            runOnUiThread {
               /* pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
                pobProduct_rv.adapter= pobProductSelectAdapter

                selectedPobAdapter=SelectedPobAdapter(selectedProductList,this,this)
                selectedPob_rv.adapter= selectedPobAdapter*/

                setPobAdapter()

                visitPurpose_spinner.setAdapter(adapterVisit)


                Handler(Looper.getMainLooper()).postDelayed({
                    alertClass.hideAlert()


                }, 10)

            }
        }
        Thread(runnable).start()


        doctorName_tv.setText(intent.getStringExtra("doctorName"))

        visitPurpose_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position!=0)
                    selectedPurposeID = CommonListGetClass().getWorkTypeForSpinner()[position].workId!!
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        checkRecyclerView_rv.layoutManager=LinearLayoutManager(this)
        workingWithRv.layoutManager=LinearLayoutManager(this)
        sample_rv.layoutManager=LinearLayoutManager(this)
        gift_rv.layoutManager=LinearLayoutManager(this)
        selectedPob_rv.layoutManager=LinearLayoutManager(this)
        pobProduct_rv.layoutManager=LinearLayoutManager(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetPobProduct = BottomSheetBehavior.from(BS_product_pob)

        commonSearch_et?.addTextChangedListener(filterCheckboxWatcher)
        productSearch_et?.addTextChangedListener(filterTextPobWatcher)

        edetailing_rv.layoutManager=LinearLayoutManager(this)
        edetailing_rv.adapter=EdetallingAdapter()

        back_iv.setOnClickListener({onBackPressed()})

        close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        bottomSheetPobProduct.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetPobProduct.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        toggleButton.forEach { button ->
            button.setOnClickListener { (button as MaterialButton).isChecked = true }
        }

        toggleButton.addOnButtonCheckedListener(OnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked && R.id.samples_btn == checkedId) {
                hideAllSelection()
                selectBtn.setText("Select Samples")
                sample_rv.visibility=View.VISIBLE
                selectBtn.visibility=View.VISIBLE
            }
            else if(isChecked && R.id.workingWith_btn == checkedId)
            {
                hideAllSelection()
                selectBtn.setText("Select Working with")
                workingWithRv.visibility=View.VISIBLE
                selectBtn.visibility=View.VISIBLE
            }
            else if(isChecked && R.id.gifts_btn == checkedId)
            {
                hideAllSelection()
                selectBtn.setText("Select Gifts")
                gift_rv.visibility=View.VISIBLE
                selectBtn.visibility=View.VISIBLE
            }
            else if(isChecked && R.id.pob_btn == checkedId)
            {
                hideAllSelection()
                pobParent.visibility=View.VISIBLE
            }
        })

        selectBtn.setOnClickListener({
            if(selectBtn.text.equals("Select Working with")){ openCloseModel(1)}
            if(selectBtn.text.equals("Select Samples")){ openCloseModel(2)}
            if(selectBtn.text.equals("Select Gifts")){   openCloseModel(3)}
        })

        assignStockist.setOnClickListener({
            commonSearch_et.visibility=View.GONE
            openCloseModel(4)})

        submitDetailing_btn.setOnClickListener({

            var firstSample=sampleArray!!.filter { s -> s.isChecked == true }
            var sampleQTy = firstSample!!.filter { s -> s.qty == -1}

            var firstGift=giftArray!!.filter { s -> s.isChecked == true }
            var giftyQTy = firstGift!!.filter { s -> s.qty >= 0}

            if(sampleQTy.size>0 ) {
                alertClass.commonAlert("","Sample quantity not be zero")
                return@setOnClickListener
            }
            if(giftyQTy.size>0) {
                alertClass.commonAlert("","Gift quantity not be zero")
                return@setOnClickListener
            }

            var saveModel=getSaveData(false)

            val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)
            var quantityArray=quantityModel.employeeSampleBalanceList!! as ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>

            for((index, model) in quantityArray!!.withIndex())
            {
                for (data in sampleArray)
                {
                    if(data.id.toInt()==model.productId && data.isChecked) {
                        model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                        quantityArray.set(index, model) }
                }
                for (data in giftArray)
                {
                    if(data.id.toInt()==model.productId && data.isChecked) {
                        model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                        quantityArray.set(index, model) }
                }
            }

            val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }

            saveModel.pobObject = DailyDocVisitModel.Data.DcrDoctor.PobObj()
            for(dataObj in filterSelectecd)
            {
                val pobObje=DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList()
                pobObje.productId=dataObj.notApi.insertedProductId
                pobObje.rate=dataObj.notApi.rate
                pobObje.qty=dataObj.notApi.qty
                pobObje.amount=dataObj.notApi.amount
                pobObje.schemeId=dataObj.notApi.schemeId
                pobObje.totalQty=dataObj.notApi.totalQty
                pobObje.freeQty=dataObj.notApi.freeQty
                saveModel.pobObject?.pobDetailList?.add(pobObje)
            }

            if(filterSelectecd.size!=0)
            {
                saveModel.pobObject?.pobId=0
                saveModel.pobObject?.pobNo=""
                saveModel.pobObject?.pobDate=generalClass.getCurrentDateTimeApiForamt()
                saveModel.pobObject?.partyId=doctorIdDisplayVisual
                saveModel.pobObject?.employeeId= loginModelHomePage.empId

                val jsonObj= JSONObject(staticSyncData?.configurationSetting)
                val checkStockistRequired=jsonObj.getInt("SET014")
                if(checkStockistRequired==1)
                {
                    alertClass.commonAlert("Stockist unselected","Please assign stockist in POB section")
                    return@setOnClickListener
                }
            }

            if(selectedStockist.id!=null && selectedStockist.id!="" && filterSelectecd.size!=0)  saveModel.pobObject?.stockistId=selectedStockist.id.toInt()

            val dcrDetail= Gson().fromJson(sharePreferanceBase?.getPref("dcrObj"),GetDcrToday.Data.DcrData::class.java)
                 saveModel.routeName=dcrDetail.routeName
                 saveModel.routeId=dcrDetail.routeId

            if(!GeneralClass(this).isInternetAvailable())
            {   dbBase.insertOrUpdateSaveAPI(doctorIdDisplayVisual, Gson().toJson(saveModel),"feedback")
                val commonModel=CommonModel.QuantityModel.Data()
                commonModel.employeeSampleBalanceList=quantityArray
                dbBase.addAPIData(Gson().toJson(commonModel),3)

                callRunnableAlert("Data save successfully")
            }
            else{ submitDcr(saveModel,quantityArray) }
        })

        pobProduct_btn.setOnClickListener({ closeBottomSheet() })

        closePob_iv.setOnClickListener({closeBottomSheet()})

        okPob_iv.setOnClickListener({
            generalClass.hideKeyboard(this,it)
            pobProductSelectAdapter.setSelction() })


   /*     executor.execute {
            if(!PreferenceClass(this).getPref("dcrId").isEmpty())
            {
                dcrId= PreferenceClass(this).getPref("dcrId").toInt()
            }

            if(intent.getIntExtra("doctorID",0)!=0) doctorIdDisplayVisual= intent.getIntExtra("doctorID",0)!!

            val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)

            var listSample = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Sample"}
            var listStokist = staticSyncData?.data?.retailerList?.filter { s -> s.type == "STOCKIST" }
            var Gift = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Gift"}
            var listGift = Gift!!.filter { s -> s.actualBalanceQty != 0}

            for(stockist in listStokist!!)
            {
                val data =IdNameBoll_model()
                data.id= stockist.retailerId.toString()
                data.city= stockist.cityName.toString()
                data.name= stockist.shopName.toString()
                stokistArray.add(data)
            }

            for(workWith in staticSyncData?.data?.workingWithList!!)
            {
                val data =IdNameBoll_model()
                data.id= workWith.empId.toString()
                data.name= workWith.fullName.toString()
                workWithArray.add(data)
            }

            for(sample in listSample)
            {
                val data =IdNameBoll_model()
                data.id= sample.productId.toString()
                data.name=sample?.productName!!
                data.availableQty=sample?.actualBalanceQty?.toInt()!!
                sampleArray.add(data)
            }

            for(gift in listGift)
            {
                val data =IdNameBoll_model()
                data.id= gift.productId.toString()
                data.name=gift?.productName!!
                data.availableQty=gift?.actualBalanceQty?.toInt()!!
                giftArray.add(data)
            }

            val string = Gson().toJson(staticSyncData?.data)
            val data= Gson().fromJson(string, SyncModel.Data::class.java)
            mainProductList.addAll(data.productList.filter { s -> (s.productType==1) } as ArrayList<SyncModel.Data.Product>)
            unSelectedProductList=ArrayList(mainProductList)

            val getSchemeList=staticSyncData?.data?.schemeList
            val filterByTypeSchemeList= getSchemeList?.filter { data -> (data?.schemeFor=="S" || data?.schemeFor=="H") }

            val getDocDetail: SyncModel.Data.Doctor? = staticSyncData?.data?.doctorList?.find { it.doctorId == doctorIdDisplayVisual }

            getSchemeList?.clear()
            filterByTypeSchemeList?.sortedBy { it.schemeFor }?.let { getSchemeList?.addAll(it) }

            getSchemeList?.let { passingSchemeList.addAll(it)}

            getSchemeList?.forEachIndexed { indexH, hElement ->

                val separated: Array<String>? = hElement.schemeForId?.split(",")?.toTypedArray()
                val event: String? = separated?.find { it ==getDocDetail?.fieldStaffId?.toString() }

                if(hElement.schemeFor.equals("H") && event!="")
                {
                    getSchemeList?.forEachIndexed { indexS, SElement ->

                        val separated: Array<String>? = SElement.schemeForId?.split(",")?.toTypedArray()
                        val event: String? = separated?.find { it ==getDocDetail?.stateId?.toString() }
                        if(SElement.schemeFor.equals("S") && event!="")
                        {
                            if(hElement.productId==SElement.productId) {
                                passingSchemeList.removeAt(indexS)
                            }
                        }
                    }
                }
            }

            if(intent.hasExtra("doctorName"))
            {
                if(intent.getBooleanExtra("skip",false))
                {
                    dbBase.deleteAllVisualAds()
                    dbBase.deleteAllChildVisual()
                }
                else
                { }

                visualSendModel = dbBase.getAllSubmitVisual()
            }
            else
            {

            }


            handler.post {

                doctorName_tv.setText(intent.getStringExtra("doctorName"))

                val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(this,
                    R.layout.spinner_txt, CommonListGetClass().getWorkTypeForSpinner())
                visitPurpose_spinner.setAdapter(adapterVisit)

                visitPurpose_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if(position!=0)
                            selectedPurposeID = CommonListGetClass().getWorkTypeForSpinner()[position].workId!!
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

                checkRecyclerView_rv.layoutManager=LinearLayoutManager(this)
                workingWithRv.layoutManager=LinearLayoutManager(this)
                sample_rv.layoutManager=LinearLayoutManager(this)
                gift_rv.layoutManager=LinearLayoutManager(this)
                selectedPob_rv.layoutManager=LinearLayoutManager(this)
                pobProduct_rv.layoutManager=LinearLayoutManager(this)
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetPobProduct = BottomSheetBehavior.from(BS_product_pob)

                commonSearch_et?.addTextChangedListener(filterCheckboxWatcher)
                productSearch_et?.addTextChangedListener(filterTextPobWatcher)

                if(visualSendModel.size==0)nodata_gif.visibility=View.VISIBLE

                edetailing_rv.layoutManager=LinearLayoutManager(this)
                edetailing_rv.adapter=EdetallingAdapter()

                pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
                pobProduct_rv.adapter= pobProductSelectAdapter

                selectedPobAdapter=SelectedPobAdapter(selectedProductList,this,this)
                selectedPob_rv.adapter= selectedPobAdapter

                back_iv.setOnClickListener({onBackPressed()})

                close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

                bottomSheetPobProduct.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            bottomSheetPobProduct.setState(BottomSheetBehavior.STATE_EXPANDED)
                        }
                    }
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })

                toggleButton.forEach { button ->
                    button.setOnClickListener { (button as MaterialButton).isChecked = true }
                }

                toggleButton.addOnButtonCheckedListener(OnButtonCheckedListener { group, checkedId, isChecked ->
                    if (isChecked && R.id.samples_btn == checkedId) {
                        hideAllSelection()
                        selectBtn.setText("Select Samples")
                        sample_rv.visibility=View.VISIBLE
                        selectBtn.visibility=View.VISIBLE
                    }
                    else if(isChecked && R.id.workingWith_btn == checkedId)
                    {
                        hideAllSelection()
                        selectBtn.setText("Select Working with")
                        workingWithRv.visibility=View.VISIBLE
                        selectBtn.visibility=View.VISIBLE
                    }
                    else if(isChecked && R.id.gifts_btn == checkedId)
                    {
                        hideAllSelection()
                        selectBtn.setText("Select Gifts")
                        gift_rv.visibility=View.VISIBLE
                        selectBtn.visibility=View.VISIBLE
                    }
                    else if(isChecked && R.id.pob_btn == checkedId)
                    {
                        hideAllSelection()
                        pobParent.visibility=View.VISIBLE
                    }
                })

                selectBtn.setOnClickListener({
                    if(selectBtn.text.equals("Select Working with")){ openCloseModel(1)}
                    if(selectBtn.text.equals("Select Samples")){ openCloseModel(2)}
                    if(selectBtn.text.equals("Select Gifts")){   openCloseModel(3)}
                })

                assignStockist.setOnClickListener({
                    commonSearch_et.visibility=View.GONE
                    openCloseModel(4)})

                submitDetailing_btn.setOnClickListener({

                    var firstSample=sampleArray!!.filter { s -> s.isChecked == true }
                    var sampleQTy = firstSample!!.filter { s -> s.qty == -1}

                    var firstGift=giftArray!!.filter { s -> s.isChecked == true }
                    var giftyQTy = firstGift!!.filter { s -> s.qty >= 0}

                    if(sampleQTy.size>0 ) {
                        alertClass.commonAlert("","Sample quantity not be zero")
                        return@setOnClickListener
                    }
                    if(giftyQTy.size>0) {
                        alertClass.commonAlert("","Gift quantity not be zero")
                        return@setOnClickListener
                    }

                    var saveModel=getSaveData()

                    val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)
                    var quantityArray=quantityModel.employeeSampleBalanceList!! as ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>

                    for((index, model) in quantityArray!!.withIndex())
                    {
                        for (data in sampleArray)
                        {
                            if(data.id.toInt()==model.productId && data.isChecked) {
                                model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                                quantityArray.set(index, model) }
                        }
                        for (data in giftArray)
                        {
                            if(data.id.toInt()==model.productId && data.isChecked) {
                                model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                                quantityArray.set(index, model) }
                        }
                    }

                    val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }

                    saveModel.pobObject = DailyDocVisitModel.Data.DcrDoctor.PobObj()
                    for(dataObj in filterSelectecd)
                    {
                        val pobObje=DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList()
                        pobObje.productId=dataObj.notApi.insertedProductId
                        pobObje.rate=dataObj.notApi.rate
                        pobObje.qty=dataObj.notApi.qty
                        pobObje.amount=dataObj.notApi.amount
                        pobObje.schemeId=dataObj.notApi.schemeId
                        pobObje.totalQty=dataObj.notApi.totalQty
                        pobObje.freeQty=dataObj.notApi.freeQty
                        saveModel.pobObject?.pobDetailList?.add(pobObje)
                    }

                    if(filterSelectecd.size!=0)
                    {
                        saveModel.pobObject?.pobId=0
                        saveModel.pobObject?.pobNo=""
                        saveModel.pobObject?.pobDate=generalClass.getCurrentDateTimeApiForamt()
                        saveModel.pobObject?.partyId=doctorIdDisplayVisual
                        saveModel.pobObject?.employeeId= loginModelHomePage.empId

                        val jsonObj= JSONObject(staticSyncData?.data?.configurationSetting)
                        val checkStockistRequired=jsonObj.getInt("SET014")
                        if(checkStockistRequired==1)
                        {
                            alertClass.commonAlert("Stockist unselected","Please assign stockist in POB section")
                            return@setOnClickListener
                        }

                    }

                    if(selectedStockist.id!=null && selectedStockist.id!="" && filterSelectecd.size!=0)  saveModel.pobObject?.stockistId=selectedStockist.id.toInt()

                    if(!GeneralClass(this).isInternetAvailable())
                    {   dbBase.insertOrUpdateSaveAPI(dcrId, Gson().toJson(saveModel),"feedback")
                        val commonModel=CommonModel.QuantityModel.Data()
                        commonModel.employeeSampleBalanceList=quantityArray
                        dbBase.addAPIData(Gson().toJson(commonModel),3)

                        callRunnableAlert("Data save successfully")
                    }
                    else{ submitDcr(saveModel,quantityArray) }
                })

                pobProduct_btn.setOnClickListener({ closeBottomSheet() })

                closePob_iv.setOnClickListener({closeBottomSheet()})

                okPob_iv.setOnClickListener({ pobProductSelectAdapter.setSelction() })

                if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
                { getUpdateData()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    alertClass.hideAlert()
                }, 500)

            }
        }*/


/*
        Handler(Looper.getMainLooper()).postDelayed({


        }, 500)*/
    }

    //Edit txt text watcher-------------------------------------------
    val filterTextPobWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            pobProductSelectAdapter?.getFilter()?.filter(s.toString())
        }
        override fun afterTextChanged(editable: Editable) {}
    }

    val filterCheckboxWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
         commonSlectionAdapter?.getFilter()?.filter(s.toString())
        }
        override fun afterTextChanged(editable: Editable) {}
    }
    //Edit txt text watcher-------------------------------------------

    fun closeBottomSheet()
    {
        val state =
            if (bottomSheetPobProduct.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetPobProduct.state = state
    }


    fun hideAllSelection()
    {   commonSearch_et.setText("")
        pobParent.visibility=View.GONE
        sample_rv.visibility=View.GONE
        gift_rv.visibility=View.GONE
        workingWithRv.visibility=View.GONE
        selectBtn.visibility=View.GONE
    }

    fun getSaveData(isResume: Boolean):DailyDocVisitModel.Data.DcrDoctor
    {
        var saveModel= DailyDocVisitModel.Data.DcrDoctor()
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]


        saveModel.detailType="DOCTOR"
        saveModel.remark=remark_Et.text.toString()
        saveModel.followUpRemark=remark_Et.text.toString()
        saveModel.addedThrough="W"
        saveModel.visitPurpose=selectedPurposeID
        saveModel.productDetailCount=visualSendModel.size
        saveModel.empId=loginModelHomePage.empId


        if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
        {
            val edetailingEditModel=Gson().fromJson(intent.getStringExtra("apiDataDcr"),DailyDocVisitModel.Data.DcrDoctor::class.java)
            saveModel.mode=2
            saveModel.dcrYear=    edetailingEditModel.dcrYear
            saveModel.dcrMonth=   edetailingEditModel.dcrMonth
            saveModel.callTiming= edetailingEditModel.callTiming
            saveModel.dcrId=      edetailingEditModel.dcrId
            saveModel.doctorId=   edetailingEditModel.doctorId
            saveModel.dcrDate=    edetailingEditModel.dcrDate
        }
        else{
            saveModel.dcrId=dcrId
            saveModel.doctorId=doctorIdDisplayVisual
            saveModel.mode=1
            saveModel.dcrYear=year
            saveModel.dcrMonth=month
            saveModel.callTiming= if (c.get(Calendar.AM_PM)==Calendar.AM) "M" else "E"
            saveModel.dcrDate=generalClass.getCurrentDateTimeApiForamt()
        }

        val workWithTemp=workWithArray!!.filter { s -> s.isChecked == true }
        var workWithStr=""
        for (data in workWithTemp)
        {    if(workWithStr!="") workWithStr=workWithStr.plus(",")
            workWithStr=workWithStr.plus(data.id)
        }
        saveModel.workWith=workWithStr

        val tempGiftList=giftArray!!.filter { s -> s.isChecked == true }
        var giftList=ArrayList<DailyDocVisitModel.Data.DcrDoctor.GiftList>()
        for(data in tempGiftList)
        {
            var giftModel=DailyDocVisitModel.Data.DcrDoctor.GiftList()
            giftModel.dcrId=dcrId
            giftModel.empId=loginModelHomePage.empId
            giftModel.qty=data.qty
            giftModel.productId=data.id.toInt()
            giftModel.type="GIFT"
            giftList.add(giftModel)
        }

        val tempSampleList=sampleArray!!.filter { s -> s.isChecked == true }
        var sampleList=ArrayList<DailyDocVisitModel.Data.DcrDoctor.SampleList>()
        for(data in tempSampleList)
        {
            var sampleModel=DailyDocVisitModel.Data.DcrDoctor.SampleList()
            sampleModel.dcrId=dcrId
            sampleModel.empId=loginModelHomePage.empId
            sampleModel.qty=data.qty
            sampleModel.productId=data.id.toInt()
            sampleList.add(sampleModel)
        }

        var productList=ArrayList<DailyDocVisitModel.Data.DcrDoctor.ProductList>()

        if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
        {
            val edetailingEditModel=Gson().fromJson(intent.getStringExtra("apiDataDcr"),DailyDocVisitModel.Data.DcrDoctor::class.java)

            saveModel.eDetailList=edetailingEditModel.eDetailList
            saveModel.productList=edetailingEditModel.productList
        }
        else
        {
            for((index,data) in visualSendModel.withIndex())
            {
                var productModel=DailyDocVisitModel.Data.DcrDoctor.ProductList()
                productModel.dcrId=dcrId
                productModel.doctorId=data.doctorId?.toInt()
                productModel.productId=data.brandId?.toInt()
                productModel.remark=data.feedback
                productList.add(productModel)

                val oldFormat = "dd-MM-yyyy HH:mm:ss"
                val newFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
                val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

                try {
                    if(!isResume)
                    {
                        val dateStart: Date = inputFormat.parse(data.startDate)
                        val dateEnd: Date = inputFormat.parse(data.endDate)
                        data.startDate=outputFormat.format(dateStart)
                        data.endDate=outputFormat.format(dateEnd)
                    }
                    data.empId= loginModelHomePage.empId?.toString()
                    visualSendModel.set(index,data)
                }
                catch (e: ParseException) { e.printStackTrace() }
            }
            saveModel.eDetailList?.addAll(visualSendModel)
        }


        saveModel.giftList?.addAll(giftList)
        saveModel.sampleList?.addAll(sampleList)
        saveModel.productList?.addAll(productList)


        return saveModel
    }

    fun openCloseModel(type:Int)
    {   selectionType=type

        if(type==1) {
            if (workWithArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
           val passingWorkWith=ArrayList<IdNameBoll_model>()
            passingWorkWith.addAll(workWithArray)
            commonSlectionAdapter=   CheckboxSpinnerAdapter(passingWorkWith, this)
            checkRecyclerView_rv.adapter = commonSlectionAdapter
        }
        if(type==2) {
            if (sampleArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            commonSlectionAdapter=CheckboxSpinnerAdapter(sampleArray, this)
            checkRecyclerView_rv.adapter = commonSlectionAdapter
        }
        if(type==3) {
            if (giftArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            commonSlectionAdapter=CheckboxSpinnerAdapter(giftArray, this)
            checkRecyclerView_rv.adapter = commonSlectionAdapter
        }
        if(type==4)
        {
            if (stokistArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            checkRecyclerView_rv.adapter = CheckboxSpinnerAdapter(stokistArray, this)
        }

        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state
    }


    fun submitDcr(
        saveModel: DailyDocVisitModel.Data.DcrDoctor,
        quantityArray: ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>
    ) {
        Log.e("isgfuiosgfiosgfuisf",Gson().toJson(saveModel))
       // return

        alertClass?.showProgressAlert("")
        var call: Call<DailyDocVisitModel> = HomePage.apiInterface?.submitEdetailingApiAndGet("bearer " + loginModelHomePage.accessToken,saveModel) as Call<DailyDocVisitModel>
        call.enqueue(object : Callback<DailyDocVisitModel?> {
            override fun onResponse(call: Call<DailyDocVisitModel?>?, response: Response<DailyDocVisitModel?>) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if(response.body()?.getErrorObj()?.errorMessage?.isEmpty()==false)
                    {
                        alertClass?.hideAlert()
                        alertClass?.commonAlert("",response.body()?.getErrorObj()?.errorMessage.toString())
                    }
                    else {
                        dbBase?.addAPIData(Gson().toJson(response.body()?.getData()), 5)
                        alertClass?.hideAlert()
                        callRunnableAlert("Doctor Dcr save successfully")

                        val commonModel=CommonModel.QuantityModel.Data()
                        commonModel.employeeSampleBalanceList=quantityArray
                        dbBase.addAPIData(Gson().toJson(commonModel),3)
                        dbBase.deleteApiData(7)

                    } } }

            override fun onFailure(call: Call<DailyDocVisitModel?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass?.hideAlert()
                call.cancel()
            } }) }


    fun callRunnableAlert(message:String)
    {
        if(!intent.getBooleanExtra("skip",false)) dbBase.insertdoctorData(doctorIdDisplayVisual,generalClass.getCurrentDate())
        dbBase.deleteAllVisualAds()
        dbBase.deleteAllChildVisual()

        val r: Runnable = object : Runnable {
            override fun run() {
                if(AlertClass.retunDialog) {
                   if(intent.getBooleanExtra("skip",false))setResult(3)
                    else setResult(2)

                    finish()
                }}}
        alertClass.commonAlertWithRunnable("",message,r)
    }

//----------------------------------Show edetailing inner adapter------------------------------------
    inner class EdetallingAdapter() :
        RecyclerView.Adapter<EdetallingAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var brandName_headerTv: TextView = view.findViewById(R.id.brandName_headerTv)
            var startEndTime_tv: TextView = view.findViewById(R.id.startEndTime_tv)
            var feeback_et: EditText = view.findViewById(R.id.feeback_et)
            var ratingBar: RatingBar = view.findViewById(R.id.ratingBar)

        }
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.edetalling_feedback, parent, false)
            return MyViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int)
        {
            if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
            {
                holder.brandName_headerTv.isEnabled=false
                holder.startEndTime_tv.isEnabled=false
                holder.feeback_et.isEnabled=false
                holder.ratingBar.isEnabled=false
            }

            var objectDetailing=visualSendModel.get(position)
            holder.brandName_headerTv.setText("Name :"+objectDetailing.brandName)

            val hours = objectDetailing.duration / 3600;
            val minutes = (objectDetailing.duration % 3600) / 60;
            val seconds = objectDetailing.duration % 60;
            val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);


            if(intent.getStringExtra("apiDataDcr")?.isEmpty() == false)
            {
                holder.feeback_et.setText(objectDetailing.feedback)
                holder.ratingBar.rating=objectDetailing.rating
                holder.startEndTime_tv.setText("Duration : "+timeString)

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

                val dateFormatterSet = SimpleDateFormat("HH:mm:ss")
                val startDate: Date = sdf.parse(objectDetailing.startDate)
                val endDate: Date = sdf.parse(objectDetailing.endDate)

                if(position==0)
                {
                    detailingMainDateTime_tv.setText(dateFormatterSet.format(startDate)+" - "+dateFormatterSet.format(endDate))
                }
            }
            else
            {
                holder.feeback_et.setText(objectDetailing.feedback)
                holder.ratingBar.rating=objectDetailing.rating
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val dateFormatterSet = SimpleDateFormat("HH:mm:ss")
                val startDate: Date = dateFormatter.parse(objectDetailing.startDate)
                val endDate: Date = dateFormatter.parse(objectDetailing.endDate)

                if(position==0)
                {
                    detailingMainDateTime_tv.setText(dateFormatterSet.format(startDate)+" - "+dateFormatterSet.format(endDate))
                }

                holder.startEndTime_tv.setText("Duration :"+timeString)


              //  val dateFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
              //  val reference: Date = dateFormat.parse("00:00:00")
              //  val date: Date = dateFormat.parse(timeString)
              //  val secondsGet = (date.time - reference.time) / 1000L

               // holder.startEndTime_tv.setText("Duration :"+objectDetailing.duration)
               // objectDetailing.duration=secondsGet.toInt()
               // visualSendModel.set(position,objectDetailing)
            }

            holder.feeback_et.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { visualSendModel.get(position).feedback=p0.toString() }
            })

            holder.ratingBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
                override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                    visualSendModel.get(position).rating=p1
                }
            })
        }
        override fun getItemCount(): Int
        {
            return visualSendModel.size;
        }

    }

    override fun onChangeArray(
        passingArrayList: java.util.ArrayList<IdNameBoll_model>, isUpdate: Boolean,selectionTypeInterface: Int) {

        var localSelection= if(selectionTypeInterface==0)selectionType else selectionTypeInterface

        if(localSelection==1)
        {
          workWithArray= ArrayList<IdNameBoll_model>()
          workWithArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = workWithArray!!.filter { s -> s.isChecked == true }
                workingWithRv.adapter = TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this,0,this,selectionType)
                if(sendingList.size!=0)workingWithRv.visibility=View.VISIBLE
            }
        }
        if(localSelection==2)
        {
            sampleArray= ArrayList<IdNameBoll_model>()
            sampleArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = sampleArray!!.filter { s -> s.isChecked == true }
                sample_rv.adapter =TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                if(sendingList.size!=0)sample_rv.visibility=View.VISIBLE
            }
        }
        if(localSelection==3)
        { giftArray= ArrayList<IdNameBoll_model>()
            giftArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = giftArray!!.filter { s -> s.isChecked == true }
                gift_rv.adapter = TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                if(sendingList.size!=0)gift_rv.visibility=View.VISIBLE
            }
        }
        if(selectionType==4)
        {
            stokistArray= ArrayList<IdNameBoll_model>()
            stokistArray.addAll(passingArrayList)
            stokistArray.forEachIndexed { index, element ->
                if(element.isChecked) {
                    selectedStockist=element
                    stockistName.visibility=View.VISIBLE
                    stockistName.text="Stockist name - "+element.name }
                element.isChecked=false
                stokistArray.set(index,element)
            }
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)  bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    override fun onClickButtonPOB(selectedList: ArrayList<DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList>) {
       // setSelectedPOBRecycler(selectedList)
    }

    override fun onClickButtonProduct(selectedList: ArrayList<SyncModel.Data.Product>) {

        alertClass.showProgressAlert("")
        val runnable = java.lang.Runnable {
          //  selectedProductList.clear()

            for ((index,selected) in selectedList.withIndex())
            {
                if(selected?.notApi?.insertedProductId!=0)
                {
                    selected.notApi.isSaved=true
                    selectedProductList.add(selected)
                    unSelectedProductList.removeAt(index)
                    runOnUiThread {
                        selectedPobAdapter.notifyItemChanged(index)
                      //  pobProductSelectAdapter.notifyItemChanged(index)
                    }
                }
            }
            runOnUiThread {

               // pobProductSelectAdapter.notifyDataSetChanged()
                setPobAdapter()
                //pobProduct_rv.scrollToPosition(0)
                Handler(Looper.getMainLooper()).postDelayed({
                    closeBottomSheet()
                    alertClass.hideAlert()
                }, 1)
            }
        }
         Thread(runnable).start()


     /*   selectedProductList.clear()
        alertClass.showProgressAlert("")

        Handler(Looper.getMainLooper()).postDelayed({

                    val executor: ExecutorService = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())

                    executor.execute {
                        for ((index,selected) in selectedList.withIndex())
                        {
                            if(selected?.notApi?.insertedProductId!=0)
                            {
                                selected.notApi.isSaved=true
                                selectedProductList.add(selected)
                               //selectedPobAdapter.notifyItemChanged(index)
                               //pobProductSelectAdapter.notifyItemChanged(index)
                            }
                        }
                        handler.post {
                            selectedPobAdapter.notifyDataSetChanged()
                            pobProductSelectAdapter.notifyDataSetChanged()
                            pobProduct_rv.scrollToPosition(0)

                            val view = this.currentFocus
                            if (view != null) {
                                val imm: InputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                            calculateTotalProduct()
                            closeBottomSheet()

                            Handler(Looper.getMainLooper()).postDelayed({
                            alertClass.hideAlert()
                            }, 10)
                        }
                    }

        }, 2)*/



/*
        Handler(Looper.getMainLooper()).postDelayed({
            for ((index,selected) in selectedList.withIndex())
            {
                if(selected?.notApi?.insertedProductId!=0)
                {
                    selected.notApi.isSaved=true
                    selectedProductList.add(selected)
                    selectedPobAdapter.notifyItemChanged(index)
                    pobProductSelectAdapter.notifyItemChanged(index)
                }
                if(index==selectedList.size-1)
                {
                    val executor: ExecutorService = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())

                    executor.execute {
                        calculateTotalProduct()
                        handler.post {
                            pobProduct_rv.scrollToPosition(0)
                            val view = this.currentFocus
                            if (view != null) {
                                val imm: InputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                            closeBottomSheet()
                            alertClass.hideAlert()
                        }
                    }
                }
            }
        }, 10)
*/

    }

    fun setPobAdapter()
    {
        runOnUiThread{
            pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
            pobProduct_rv.adapter= pobProductSelectAdapter

            selectedPobAdapter=SelectedPobAdapter(selectedProductList,this,this)
            selectedPob_rv.adapter= selectedPobAdapter

            calculateTotalProduct()
        }
    }

    fun updateSpecificElement(returnModel: SyncModel.Data.Product?, position: Int)
    {
        returnModel?.let { selectedProductList.set(position, it) }
        calculateTotalProduct()
    }

    fun calculateTotalProduct()
    {
        val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }
        var calculation=0.0
        for(data in filterSelectecd)
        { calculation= data.notApi.amount?.plus(calculation)!! }
        totalProductPrice_tv.setText("Grand Total: "+String.format("%.2f", calculation))
    }

    //===============================getResumeData==================================================
    fun getResumeData()
    {
        val runnable = java.lang.Runnable {
            val getDbstr= dbBase?.getApiDetail(7)
            if (getDbstr != "") {
                val edetailingEditModel = Gson().fromJson(getDbstr, DailyDocVisitModel.Data.DcrDoctor::class.java)
               // doctorIdDisplayVisual=edetailingEditModel.doctorId!!
               // visualSendModel.addAll(edetailingEditModel.eDetailList)
                for((index,mainVisual) in visualSendModel.withIndex())
                {
                    for(dbVisual in edetailingEditModel.eDetailList)
                    {
                        Log.e("dsfdsffsdf",dbVisual.brandId.toString())
                        Log.e("fihsfuisd",mainVisual.brandId.toString())
                        if(mainVisual.brandId==dbVisual.brandId)
                        {
                            mainVisual.rating=dbVisual.rating
                            mainVisual.feedback=dbVisual.feedback
                            visualSendModel.set(index,mainVisual)
                        }
                    }
                }


                for(intentProduct in edetailingEditModel.sampleList!!)
                {
                    for( (index,getSample) in sampleArray.withIndex())
                    {
                        if(intentProduct.productId == getSample.id.toInt()){
                            getSample.isChecked=true
                            getSample.qty=intentProduct.qty!!
                            sampleArray.set(index,getSample)
                        }
                    }
                }
                val passingSamples=sampleArray.filter { s -> (s.isChecked) }

                for(intentGift in edetailingEditModel.giftList!!)
                {
                    for( (index,getGift) in giftArray.withIndex())
                    {
                        if(intentGift.productId == getGift.id.toInt()){
                            getGift.isChecked=true
                            getGift.qty=intentGift.qty!!
                            giftArray.set(index,getGift)
                        }
                    }
                }
                val passingGift=giftArray.filter { s -> (s.isChecked) }

                if(edetailingEditModel.workWith!=null && edetailingEditModel.workWith!="")
                {
                    val getWorkingList: List<String> = edetailingEditModel.workWith!!.split(",")
                    for(workingWith in getWorkingList)
                    {
                        for( (index,getWorking) in workWithArray.withIndex())
                        {
                            if(workingWith.toInt() == getWorking.id.toInt()){
                                getWorking.isChecked=true
                                workWithArray.set(index,getWorking)
                                workingWithRv.visibility=View.VISIBLE
                            }
                        }
                    }
                }
                val passingWorking =workWithArray.filter { s -> (s.isChecked) }


                for (pobProducts in edetailingEditModel.pobObject?.pobDetailList!!)
                {
                    for((index,availableProduct) in mainProductList.withIndex())
                    {
                        if(availableProduct.productId==pobProducts.productId)
                        {
                            availableProduct.notApi.amount=pobProducts.amount
                            availableProduct.notApi.rate=pobProducts.rate
                            availableProduct.notApi.qty=pobProducts.qty
                            availableProduct.notApi.totalQty=pobProducts.totalQty
                            availableProduct.notApi.scheme=pobProducts.schemeNameWithQty
                            availableProduct.notApi.schemeId=pobProducts.schemeId
                            availableProduct.notApi.salesQty=pobProducts.schemeSalesQty
                            availableProduct.notApi.salesQtyMain=pobProducts.schemeSalesQty
                            availableProduct.notApi.freeQty=pobProducts.freeQty
                            availableProduct.notApi.freeQtyMain=pobProducts.schemeFreeQty
                            availableProduct.notApi.insertedProductId=pobProducts.productId
                            availableProduct.notApi.isSaved=true

                            unSelectedProductList.removeAt(index)
                            selectedProductList.add(availableProduct)
                        }
                    }
                }

                for (getEdetailing in edetailingEditModel.eDetailList)
                {
                    var eDetailingObj= VisualAdsModel_Send()
                    eDetailingObj.startDate=getEdetailing.startDate
                    eDetailingObj.brandName=getEdetailing.brandName
                    eDetailingObj.feedback=getEdetailing.feedback
                    eDetailingObj.rating=getEdetailing.rating
                    eDetailingObj.duration=getEdetailing.duration
                }

                var spinnerSelect=0
                for((index,visitPurpos) in CommonListGetClass().getWorkTypeForSpinner().withIndex())
                {
                    if(visitPurpos.workId==edetailingEditModel.visitPurpose)
                    {
                        spinnerSelect=index
                    }
                }

                this.runOnUiThread(java.lang.Runnable {
                    if(visualSendModel.size==0)nodata_gif.visibility=View.VISIBLE
                    remark_Et.setText(edetailingEditModel.remark)
                    workingWithRv.adapter = TextWithEditAdapter(passingWorking as ArrayList<IdNameBoll_model>, this, 0, this,selectionType)
                    gift_rv.adapter = TextWithEditAdapter(passingGift as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                    sample_rv.adapter = TextWithEditAdapter(passingSamples as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                    visitPurpose_spinner.setSelection(spinnerSelect)
                    edetailing_rv.adapter=EdetallingAdapter()
                    stokistArray.forEachIndexed { index, element ->
                        if(element.id.toInt() == edetailingEditModel.pobObject?.stockistId) {
                            selectedStockist=element
                            stockistName.visibility=View.VISIBLE
                            stockistName.text="Stockist name - "+element.name
                            element.isChecked=false
                            stokistArray.set(index,element)}
                    }
                    if(visualSendModel.size!=0)nodata_gif.visibility=View.GONE

                    setPobAdapter()
                    calculateTotalProduct()
                })
            }




        }
        Thread(runnable).start()
    }


    //=============================== onUpdate method===============================================
    fun getUpdateData()
    {

        val runnable = java.lang.Runnable {

            val edetailingEditModel=Gson().fromJson(intent.getStringExtra("apiDataDcr"),DailyDocVisitModel.Data.DcrDoctor::class.java)
            doctorIdDisplayVisual=edetailingEditModel.doctorId!!
            visualSendModel.addAll(edetailingEditModel.eDetailList)

            for(intentProduct in edetailingEditModel.sampleList!!)
            {
                for( (index,getSample) in sampleArray.withIndex())
                {
                    if(intentProduct.productId == getSample.id.toInt()){
                        getSample.isChecked=true
                        getSample.qty=intentProduct.qty!!
                        sampleArray.set(index,getSample)
                    }
                }
            }
            val passingSamples=sampleArray.filter { s -> (s.isChecked) }

            for(intentGift in edetailingEditModel.giftList!!)
            {
                for( (index,getGift) in giftArray.withIndex())
                {
                    if(intentGift.productId == getGift.id.toInt()){
                        getGift.isChecked=true
                        getGift.qty=intentGift.qty!!
                        giftArray.set(index,getGift)
                    }
                }
            }
            val passingGift=giftArray.filter { s -> (s.isChecked) }

            if(edetailingEditModel.workWith!=null && edetailingEditModel.workWith!="")
            {
                val getWorkingList: List<String> = edetailingEditModel.workWith!!.split(",")
                for(workingWith in getWorkingList)
                {
                    for( (index,getWorking) in workWithArray.withIndex())
                    {
                        if(workingWith.toInt() == getWorking.id.toInt()){
                            getWorking.isChecked=true
                            workWithArray.set(index,getWorking)
                            workingWithRv.visibility=View.VISIBLE
                        }
                    }
                }
            }
            val passingWorking =workWithArray.filter { s -> (s.isChecked) }


            for (pobProducts in edetailingEditModel.pobObject?.pobDetailList!!)
            {
                for((index,availableProduct) in mainProductList.withIndex())
                {
                    if(availableProduct.productId==pobProducts.productId)
                    {
                        availableProduct.notApi.amount=pobProducts.amount
                        availableProduct.notApi.rate=pobProducts.rate
                        availableProduct.notApi.qty=pobProducts.qty
                        availableProduct.notApi.totalQty=pobProducts.totalQty
                        availableProduct.notApi.scheme=pobProducts.schemeNameWithQty
                        availableProduct.notApi.schemeId=pobProducts.schemeId
                        availableProduct.notApi.salesQty=pobProducts.schemeSalesQty
                        availableProduct.notApi.salesQtyMain=pobProducts.schemeSalesQty
                        availableProduct.notApi.freeQty=pobProducts.freeQty
                        availableProduct.notApi.freeQtyMain=pobProducts.schemeFreeQty
                        availableProduct.notApi.insertedProductId=pobProducts.productId
                        availableProduct.notApi.isSaved=true

                        unSelectedProductList.removeAt(index)
                        selectedProductList.add(availableProduct)
                    }
                }
            }

            for (getEdetailing in edetailingEditModel.eDetailList)
            {
                var eDetailingObj= VisualAdsModel_Send()
                eDetailingObj.startDate=getEdetailing.startDate
                eDetailingObj.brandName=getEdetailing.brandName
                eDetailingObj.feedback=getEdetailing.feedback
                eDetailingObj.rating=getEdetailing.rating
                eDetailingObj.duration=getEdetailing.duration
            }

            var spinnerSelect=0
            for((index,visitPurpos) in CommonListGetClass().getWorkTypeForSpinner().withIndex())
            {
                if(visitPurpos.workId==edetailingEditModel.visitPurpose)
                {
                    spinnerSelect=index
                }
            }

            this.runOnUiThread(java.lang.Runnable {
                if(visualSendModel.size==0)nodata_gif.visibility=View.VISIBLE
                remark_Et.setText(edetailingEditModel.remark)
                doctorName_tv.setText(edetailingEditModel.doctorName)
                workingWithRv.adapter = TextWithEditAdapter(passingWorking as ArrayList<IdNameBoll_model>, this, 0, this,selectionType)
                gift_rv.adapter = TextWithEditAdapter(passingGift as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                sample_rv.adapter = TextWithEditAdapter(passingSamples as ArrayList<IdNameBoll_model>, this, 1, this,selectionType)
                remark_Et.isEnabled=false
                edetailing_rv.adapter=EdetallingAdapter()
                visitPurpose_spinner.setSelection(spinnerSelect)
                stokistArray.forEachIndexed { index, element ->
                    if(element.id.toInt() == edetailingEditModel.pobObject?.stockistId) {
                        selectedStockist=element
                        stockistName.visibility=View.VISIBLE
                        stockistName.text="Stockist name - "+element.name
                        element.isChecked=false
                        stokistArray.set(index,element)}
                }
                if(visualSendModel.size!=0)nodata_gif.visibility=View.GONE

                setPobAdapter()

            })
        }
        Thread(runnable).start()
    }

    override fun onBackPressed() {
        if(intent.getStringExtra("doctorName")?.isEmpty() == false)
        {
            var saveModel=getSaveData(true)

            val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }

            saveModel.pobObject = DailyDocVisitModel.Data.DcrDoctor.PobObj()
            for(dataObj in filterSelectecd)
            {
                val pobObje=DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList()
                pobObje.productId=dataObj.notApi.insertedProductId
                pobObje.rate=dataObj.notApi.rate
                pobObje.qty=dataObj.notApi.qty
                pobObje.amount=dataObj.notApi.amount
                pobObje.schemeId=dataObj.notApi.schemeId
                pobObje.totalQty=dataObj.notApi.totalQty
                pobObje.freeQty=dataObj.notApi.freeQty
                saveModel.pobObject?.pobDetailList?.add(pobObje)
            }

            if(filterSelectecd.size!=0)
            {
                saveModel.pobObject?.pobId=0
                saveModel.pobObject?.pobNo=""
                saveModel.pobObject?.pobDate=generalClass.getCurrentDateTimeApiForamt()
                saveModel.pobObject?.partyId=doctorIdDisplayVisual
                saveModel.pobObject?.employeeId= loginModelHomePage.empId
            }

            if(selectedStockist.id!=null && selectedStockist.id!="" && filterSelectecd.size!=0)  saveModel.pobObject?.stockistId=selectedStockist.id.toInt()

            dbBase?.addAPIData(Gson().toJson(saveModel), 7)
            super.onBackPressed()
        }
        else
        {
            super.onBackPressed()
        }
    }

}