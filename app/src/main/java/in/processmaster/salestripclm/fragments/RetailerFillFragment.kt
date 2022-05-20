package `in`.processmaster.salestripclm.fragments
import DocManagerModel
import DoctorManagerSelector_Adapter
import SelectorInterface
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.PobProductAdapter
import `in`.processmaster.salestripclm.adapter.SelectedPobAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.NewCallFragment.Companion.retailerObj
import `in`.processmaster.salestripclm.interfaceCode.*
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.GPSTracker
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RetailerFillFragment(val stringInter: StringInterface) : Fragment(), IdNameBoll_interface, PobProductTransfer,
    productTransferIndividual
    , productTransfer, EditInterface,SelectorInterface,StringInterface {


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var stokistArray=ArrayList<IdNameBoll_model>()
    var selectionType=0
    var pobProductSelectAdapter= PobProductAdapter()
    lateinit var selectedPobAdapter: SelectedPobAdapter
    var commonSlectionAdapter=CheckboxSpinnerAdapter(ArrayList(),this)
    var mainProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var unSelectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedPurposeID=0
    var dcrId=0

    var selectedStockist=IdNameBoll_model()
    lateinit var views:View
    var passingSchemeList:ArrayList<SyncModel.Data.Scheme> = ArrayList()
    var arrayListSelectorDoctor: ArrayList<DocManagerModel> = ArrayList()
    var checkIsDcrSave=false

    //RCPA section
    lateinit var alertDialog: AlertDialog
    lateinit var ownBrand_et: EditText //rcpa detail own brand
    lateinit var adapter1: AddedRcpa_Adapter
    lateinit var adapter2: AddedRcpa_Adapter
    lateinit var adapter3: AddedRcpa_Adapter
    var saveRcpaDetailList1:ArrayList<DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail> = ArrayList()
    var saveRcpaDetailList2:ArrayList<DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail> = ArrayList()
    var saveRcpaDetailList3:ArrayList<DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail> = ArrayList()
    lateinit var alertClass: AlertClass
    lateinit var generalClass: GeneralClass
    lateinit var dbBase: DatabaseHandler
    lateinit var sharePreferanceBase: PreferenceClass
    lateinit  var doctorRcpa1 :DocManagerModel
    lateinit  var doctorRcpa2 :DocManagerModel
    lateinit  var doctorRcpa3 :DocManagerModel
    var brandId=0
    //IsCLMUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_retailer_fill, container, false)

        bottomSheetBehavior = BottomSheetBehavior.from(views.bottomSheet)
        views.workingWithRv.layoutManager= LinearLayoutManager(requireActivity())
        views.gift_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.selectedPob_rv.layoutManager= LinearLayoutManager(requireActivity())

        views.rcpaDetailOne_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.rcpaDetailTwo_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.rcpaDetailThree_rv.layoutManager= LinearLayoutManager(requireActivity())

        val coroutine=GlobalScope.launch(Dispatchers.Default) {

            val task= async {
                alertClass= AlertClass(requireActivity())
                dbBase= DatabaseHandler(requireActivity())
                generalClass= GeneralClass(requireActivity())
                sharePreferanceBase= PreferenceClass(requireActivity())

                adapter1=AddedRcpa_Adapter(1,saveRcpaDetailList1)
                adapter2=AddedRcpa_Adapter(2,saveRcpaDetailList2)
                adapter3=AddedRcpa_Adapter(3,saveRcpaDetailList3)

                val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(requireActivity(),
                    R.layout.spinner_txt, CommonListGetClass().getWorkTypeForSpinner())
                requireActivity().runOnUiThread {  views.visitPurpose_spinner.setAdapter(adapterVisit) }

                val quantityModel= Gson().fromJson(DatabaseHandler(requireActivity()).getApiDetail(3), CommonModel.QuantityModel.Data::class.java)
                var Gift = quantityModel.employeeSampleBalanceList?.filter { s -> s.productType == "Gift"}
                var listGift = Gift?.filter { s -> s.actualBalanceQty != 0}
                var listStokist = SplashActivity.staticSyncData?.retailerList?.filter { s -> s.type == "STOCKIST" }

                for(workWith in SplashActivity.staticSyncData?.workingWithList!!)
                {
                    val data =IdNameBoll_model()
                    data.id= workWith.empId.toString()
                    data.name= workWith.fullName.toString()
                    workWithArray.add(data)
                }

                if (listGift != null) {
                    for(gift in listGift) {
                        val data =IdNameBoll_model()
                        data.id= gift.productId.toString()
                        data.name=gift?.productName!!
                        data.availableQty=gift?.actualBalanceQty?.toInt()!!
                        giftArray.add(data)
                    }
                }

                if (listStokist != null) {
                    for(stockist in listStokist) {
                        val data =IdNameBoll_model()
                        data.id= stockist.retailerId.toString()
                        data.city= stockist.cityName.toString()
                        data.name= stockist.shopName.toString()
                        stokistArray.add(data)
                    }
                }

                val string = Gson().toJson(SplashActivity.staticSyncData)
                val data= Gson().fromJson(string, SyncModel.Data::class.java)
                mainProductList.addAll(data.productList.filter { s -> (s.productType==1) } as ArrayList<SyncModel.Data.Product>)
                unSelectedProductList=ArrayList(mainProductList)

                val getSchemeList= SplashActivity.staticSyncData?.schemeList
                val filterByTypeSchemeList= getSchemeList?.filter { data -> (data?.schemeFor=="S" || data?.schemeFor=="H") }
                filterByTypeSchemeList?.sortedBy { it.schemeFor }?.let { getSchemeList?.addAll(it) }
                val getDocDetail: SyncModel.Data.Doctor? = SplashActivity.staticSyncData?.doctorList?.find { it.doctorId == PresentEDetailingFrag.doctorIdDisplayVisual }

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

                for(item in SplashActivity.staticSyncData?.doctorList!!)
                {
                    val selectorModel = DocManagerModel()
                    selectorModel.name= item.doctorName.toString()
                    selectorModel.routeName= item.routeName.toString()
                    selectorModel.specialityName= item.specialityName.toString()
                    selectorModel.id= item.doctorId!!
                    selectorModel.mailId= item.emailId.toString()
                    arrayListSelectorDoctor.add(selectorModel)
                }

                if(!sharePreferanceBase.getPref("dcrId").isEmpty())
                {
                    dcrId= sharePreferanceBase.getPref("dcrId").toInt()
                }
                if(arguments?.getString("retailerData")?.isEmpty() == false)
                {getUpdateData() }

            }
            task.await()


        }
        coroutine.invokeOnCompletion {
            requireActivity().runOnUiThread {
                views.rcpaDetailOne_rv.adapter=adapter1
                views.rcpaDetailTwo_rv.adapter=adapter2
                views.rcpaDetailThree_rv.adapter=adapter3
            }
        }

        views.visitPurpose_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position!=0)
                    selectedPurposeID = CommonListGetClass().getWorkTypeForSpinner()[position].workId!!
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        views.toggleButton.forEach { button ->
            button.setOnClickListener { (button as MaterialButton).isChecked = true }
        }

        views.close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        views.assignStockist.setOnClickListener({
            commonSearch_et.visibility=View.INVISIBLE
            openCloseModel(4)})

        views.toggleButton.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked && R.id.rcpa_btn == checkedId) {
                hideAllSelection()
                views.rcpaNestedScroll.visibility = View.VISIBLE
                views.selectBtn.visibility = View.INVISIBLE
            } else if (isChecked && R.id.workingWith_btn == checkedId) {
                hideAllSelection()
                views?.selectBtn?.setText("Select Working with")
                views?.selectHeader_tv?.setText("Select Working with")
                views?.workingWithRv?.visibility = View.VISIBLE
                views?.selectBtn?.visibility = View.VISIBLE
            } else if (isChecked && R.id.gifts_btn == checkedId) {
                hideAllSelection()
                views?.selectBtn.setText("Select Gifts")
                views?.selectHeader_tv.setText("Select Gifts")
                views?.gift_rv.visibility = View.VISIBLE
                views?.selectBtn.visibility = View.VISIBLE
            } else if (isChecked && R.id.pob_btn == checkedId) {
                hideAllSelection()
                views.pobParent.visibility = View.VISIBLE
            }
        })

        views.selectBtn.setOnClickListener({
            if(selectBtn.text.equals("Select Working with")){ openCloseModel(1)}
            if(selectBtn.text.equals("Select Samples")){ openCloseModel(2)}
            if(selectBtn.text.equals("Select Gifts")){   openCloseModel(3)}
        })

        views.pobProduct_btn.setOnClickListener({
            //    closeBottomSheet()
            callPobSelectAlert()
        })

        views.doctorOne_et.setOnClickListener { selectDoctorManager_alert(10)}
        views.doctorTwo_et.setOnClickListener {
            if(saveRcpaDetailList1.size==0)
            {
                generalClass.showSnackbar(it ,"Doctor 1 brand detail is empty")
                return@setOnClickListener
            }
            selectDoctorManager_alert(20)}

        views.doctorThree_et.setOnClickListener {
            if(saveRcpaDetailList1.size==0) {
                generalClass.showSnackbar(it ,"Doctor 1 brand detail is empty")
                return@setOnClickListener
            }
            if(saveRcpaDetailList2.size==0){
                generalClass.showSnackbar(it ,"Doctor 2 brand detail is empty")
                return@setOnClickListener
            }
            selectDoctorManager_alert(30)}

        views.addBrandOne_btn.setAlpha(0.5f)
        views.addBrandOne_btn.isEnabled=false
        views.addBrandTwo_btn.setAlpha(0.5f)
        views.addBrandTwo_btn.isEnabled=false
        views.addBrandThree_btn.setAlpha(0.5f)
        views.addBrandThree_btn.isEnabled=false

        views.addBrandOne_btn.setOnClickListener {

           if(::alertDialog.isInitialized  && alertDialog.isShowing) { alertDialog.dismiss() }

            AddRCPA_alert(1)
        }

        views.addBrandTwo_btn.setOnClickListener {
            if(saveRcpaDetailList1.size==0)
            {
                generalClass.showSnackbar(it,"First RCPA detail not fill")
                return@setOnClickListener
            }
            if(::alertDialog.isInitialized && alertDialog.isShowing) { alertDialog.dismiss() }

            AddRCPA_alert(2)
        }

        views.addBrandThree_btn.setOnClickListener {
            if(saveRcpaDetailList1.size==0)
            {
                generalClass.showSnackbar(it,"First RCPA detail not fill")
                return@setOnClickListener
            }
            if(saveRcpaDetailList2.size==0)
            {
                generalClass.showSnackbar(it,"Second RCPA detail not fill")
                return@setOnClickListener
            }
            if(::alertDialog.isInitialized  && alertDialog.isShowing) { alertDialog.dismiss() }

            AddRCPA_alert(3)
        }

        views.submit_btn.setOnClickListener({

            var firstGift=giftArray?.filter { s -> s.isChecked == true }
            var giftyQTy = firstGift?.filter { s -> s.qty >= 0}

            if(giftyQTy.size>0) {
                alertClass.commonAlert("","Gift quantity not be zero")
                return@setOnClickListener
            }
            if(staticSyncData?.settingDCR?.isRCPAMandatoryForChemistReport == true && staticSyncData?.settingDCR?.minChemistRCPA==0)
            {
                if(::doctorRcpa1.isInitialized==false)
                {
                    alertClass.commonAlert("","Doctor 1 RCPA required")
                    return@setOnClickListener
                }
                if(saveRcpaDetailList1.size==0)
                    {
                        alertClass.commonAlert("","Please add brands in RCPA section doctor 1")
                        return@setOnClickListener
                    }

            }

            var saveModel=getSaveData(false)
            val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)
            var quantityArray=quantityModel.employeeSampleBalanceList as java.util.ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>

            var rcpaarray: ArrayList<DailyDocVisitModel.Data.DcrDoctor.Rcpavo> = ArrayList()

            if(::doctorRcpa1.isInitialized==true) {
                saveModel.isRCPAFilled=true
                val model = DailyDocVisitModel.Data.DcrDoctor.Rcpavo()
                model.docId=doctorRcpa1.id
                model.docName=doctorRcpa1.name
                model.mode = 1
                model.dcrId = dcrId
                model.empId= loginModelHomePage.empId
                model.rCPADate=generalClass.getCurrentDateTimeApiForamt()
                model.strRCPADate=generalClass.getCurrentDateTimeApiForamt()


                if(arguments?.getString("retailerData")?.isEmpty() == false) {
                    val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)
                    model.retailerId=edetailingEditModel.retailerId
                }
                else
                {  model.retailerId=retailerObj.retailerId  }
                model.rCPADetailList=saveRcpaDetailList1
                rcpaarray.add(model)
            }

            if(::doctorRcpa2.isInitialized==true) {
                if(::doctorRcpa1.isInitialized==true)
                {
                    if(doctorRcpa2.id==doctorRcpa1.id){
                        alertClass.commonAlert("","Doctor 1 and Doctor 2 should not be the same")
                        return@setOnClickListener
                    }
                }


                val model = DailyDocVisitModel.Data.DcrDoctor.Rcpavo()
                model.docId=doctorRcpa2.id
                model.docName=doctorRcpa2.name
                model.mode = 1
                model.dcrId = dcrId
                model.empId= loginModelHomePage.empId
                model.rCPADate=generalClass.getCurrentDateTimeApiForamt()
                model.strRCPADate=generalClass.getCurrentDateTimeApiForamt()

                if(arguments?.getString("retailerData")?.isEmpty() == false) {
                    val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)
                    model.retailerId=edetailingEditModel.retailerId
                }
                else
                {  model.retailerId=retailerObj.retailerId  }

                model.rCPADetailList=saveRcpaDetailList2
                rcpaarray.add(model)
            }

            if(::doctorRcpa3.isInitialized==true) {
                if(::doctorRcpa1.isInitialized==true)
                {
                    if(doctorRcpa3.id==doctorRcpa1.id){
                        alertClass.commonAlert("","Doctor 1 and Doctor 3 should not be the same")
                        return@setOnClickListener
                    }
                }
                if(::doctorRcpa2.isInitialized==true)
                {
                    if(doctorRcpa3.id==doctorRcpa2.id){
                        alertClass.commonAlert("","Doctor 2 and Doctor 3 should not be the same")
                        return@setOnClickListener
                    }
                }
                val model = DailyDocVisitModel.Data.DcrDoctor.Rcpavo()
                model.docId=doctorRcpa3.id
                model.docName=doctorRcpa3.name
                model.mode = 1
                model.dcrId = dcrId
                model.empId= loginModelHomePage.empId
                model.rCPADate=generalClass.getCurrentDateTimeApiForamt()
                model.strRCPADate=generalClass.getCurrentDateTimeApiForamt()

                if(arguments?.getString("retailerData")?.isEmpty() == false) {
                    val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)
                    model.retailerId=edetailingEditModel.retailerId
                }
                else
                {  model.retailerId=retailerObj.retailerId  }

                model.rCPADetailList=saveRcpaDetailList3
                rcpaarray.add(model)
            }

            saveModel.RCPAList=ArrayList()
            saveModel.RCPAList?.addAll(rcpaarray)

            for((index, model) in quantityArray?.withIndex())
            {
                for (data in giftArray)
                {
                    if(data.id.toInt()==model.productId && data.isChecked) {
                        model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                        quantityArray.set(index, model) }
                }
            }
            val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }

            if(filterSelectecd.size!=0){
                if(arguments?.getString("retailerData")?.isEmpty() == false)
                {

                    val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)
                    saveModel.pobObject=edetailingEditModel.pobObject
                    saveModel.pobObject?.pobDetailList?.clear()
                }
                else
                {
                    saveModel.pobObject = DailyDocVisitModel.Data.DcrDoctor.PobObj()
                    saveModel.pobObject?.pobDate=generalClass.getCurrentDateTimeApiForamt()
                    saveModel.pobObject?.partyId= PresentEDetailingFrag.doctorIdDisplayVisual
                    saveModel.pobObject?.employeeId= loginModelHomePage.empId
                }

                saveModel.pobObject?.pobDetailList=ArrayList()
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
                    pobObje.pobId=dataObj.notApi.pobId
                    pobObje.pobNo=dataObj.notApi.pobNo
                    saveModel.pobObject?.pobDetailList?.add(pobObje)
                }

                if(filterSelectecd.size!=0)
                {
                    //  saveModel.pobObject?.pobId=0
                    // saveModel.pobObject?.pobNo=""
                    val jsonObj= JSONObject(SplashActivity.staticSyncData?.configurationSetting)
                    val checkStockistRequired=jsonObj.getInt("SET014")
                    if(checkStockistRequired==1)
                    {
                        alertClass.commonAlert("Stockist unselected","Please assign stockist in POB section")
                        return@setOnClickListener
                    }
                }

                if(selectedStockist.id!=null && selectedStockist.id!="" && filterSelectecd.size!=0)  saveModel.pobObject?.stockistId=selectedStockist.id.toInt()

            }

            val dcrDetail= Gson().fromJson(sharePreferanceBase?.getPref("dcrObj"), GetDcrToday.Data.DcrData::class.java)
            if(dcrDetail?.routeId!=null)
            {
                saveModel.routeName=dcrDetail.routeName
                saveModel.routeId=dcrDetail.routeId.toString()
            }

            if(!GeneralClass(requireActivity()).isInternetAvailable())
            {
                saveModel.isOffline=true
                retailerObj.retailerId?.let { it1 ->
                    dbBase.insertOrUpdateSaveAPI(
                        it1,
                        Gson().toJson(saveModel),"retailerFeedback")
                }
               val commonModel=CommonModel.QuantityModel.Data()
               commonModel.employeeSampleBalanceList=quantityArray
               dbBase.addAPIData(Gson().toJson(commonModel),3)
                alertClass.commonAlert("","Data save successfully")

                if(arguments?.getString("retailerData")?.isEmpty() == false) {

                    (requireActivity() as HomePage).backToHome()
                }
                else{
                    requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameRetailer_view, RetailerFillFragment(stringInter))
                        .commit()
                    stringInter.onClickString("")
                }



            }
            else{
                saveModel.isOffline=false
                submitDcr(saveModel,quantityArray) }

        })



        return views
    }

    fun submitDcr(
        saveModel: DailyDocVisitModel.Data.DcrDoctor,
        quantityArray: java.util.ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>
    ) {
        val arrayModel:ArrayList<DailyDocVisitModel.Data.DcrDoctor> = ArrayList()
        arrayModel.add(saveModel)
        Log.e("isgfuiosgfiosgfuisf",Gson().toJson(arrayModel))
      //  return

        alertClass?.showProgressAlert("")
        var call: Call<DailyDocVisitModel> = HomePage.apiInterface?.submitRetailer("bearer " + HomePage.loginModelHomePage.accessToken,arrayModel) as Call<DailyDocVisitModel>
        call.enqueue(object : Callback<DailyDocVisitModel?> {
            override fun onResponse(call: Call<DailyDocVisitModel?>?, response: Response<DailyDocVisitModel?>) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if(response.body()?.getErrorObj()?.errorMessage?.isEmpty()==false)
                    {
                        alertClass?.hideAlert()
                        alertClass?.commonAlert("",response.body()?.getErrorObj()?.errorMessage.toString())
                    }
                    else {

                        val responseDocCall=dbBase.getApiDetail(5)
                        var  docCallModel= Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)

                        if(responseDocCall.isEmpty()){
                            docCallModel= DailyDocVisitModel.Data()
                        }


                        docCallModel.dcrRetailerlist=response.body()?.getData()?.retailerStockistDCRList

                        //  dbBase?.addAPIData(Gson().toJson(response.body()?.getData()), 5)
                        dbBase?.addAPIData(Gson().toJson(docCallModel), 5)

                    //    dbBase?.addAPIData(Gson().toJson(response.body()?.getData()), 5)
                        alertClass?.hideAlert()
                        alertClass?.commonAlert("", response.body()?.getData()?.message.toString())

                        val commonModel=CommonModel.QuantityModel.Data()
                        commonModel.employeeSampleBalanceList=quantityArray
                        dbBase.addAPIData(Gson().toJson(commonModel),3)

                        if(arguments?.getString("retailerData")?.isEmpty() == false) {
                            (requireActivity() as HomePage).backToHome()
                        }
                        else
                        {
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameRetailer_view, RetailerFillFragment(stringInter))
                                .commit()

                            stringInter.onClickString("")
                        }


                       // dbBase.deleteApiData(7)

                    } }
            else {
                alertClass?.hideAlert()
                }
            }

            override fun onFailure(call: Call<DailyDocVisitModel?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass?.hideAlert()
                call.cancel()
            } }) }

    fun getSaveData(isResume: Boolean): DailyDocVisitModel.Data.DcrDoctor
    {
        var saveModel= DailyDocVisitModel.Data.DcrDoctor()
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]+1

        saveModel.detailType="RETAILER"
        saveModel.remark=remark_Et.text.toString()
        saveModel.addedThrough="C"
        saveModel.visitPurpose=selectedPurposeID
        saveModel.empId= HomePage.loginModelHomePage.empId

        val getGpsTracker= GPSTracker(requireActivity())
        saveModel.latitude=getGpsTracker.latitude
        saveModel.longitude=getGpsTracker.longitude


        saveModel.partyLatitude= NewCallFragment.retailerObj.latitude
        saveModel.partyLongitude=NewCallFragment.retailerObj.longitude

        val startPoint = Location("locationA")
        startPoint.latitude=getGpsTracker.latitude
        startPoint.longitude=getGpsTracker.longitude
        val endPoint = Location("locationB")
        endPoint.latitude =  NewCallFragment.retailerObj.latitude
        endPoint.longitude = NewCallFragment.retailerObj.longitude
        if(NewCallFragment.retailerObj.latitude!=0.00 && NewCallFragment.retailerObj.longitude!=0.00) {
            val distance = startPoint.distanceTo(endPoint).toInt()
            saveModel.partyDistance=distance
        }
        if(arguments?.getString("retailerData")?.isEmpty() == false)
        {
            val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)
            saveModel.mode=1
            saveModel.callTiming=  edetailingEditModel.callTiming
            saveModel.dcrId=       edetailingEditModel.dcrId
            saveModel.doctorId=    edetailingEditModel.doctorId
            saveModel.dcrDate=     generalClass.getCurrentDateTimeApiForamt()
            saveModel.retailerId = edetailingEditModel.retailerId
        }
        else {
            saveModel.dcrId = dcrId
            saveModel.retailerId = NewCallFragment.retailerObj.retailerId
            saveModel.mode = 1
            saveModel.callTiming = if (c.get(Calendar.AM_PM) == Calendar.AM) "M" else "E"
            saveModel.dcrDate=generalClass.getCurrentDateTimeApiForamt()
        }
        saveModel.dcrYear=year
        saveModel.dcrMonth=month

        val workWithTemp=workWithArray?.filter { s -> s.isChecked == true }
        var workWithStr=""
        for (data in workWithTemp)
        {    if(workWithStr!="") workWithStr=workWithStr.plus(",")
            workWithStr=workWithStr.plus(data.id)
        }
        saveModel.workWith=workWithStr

        val tempGiftList=giftArray?.filter { s -> s.isChecked == true }
        var giftList= java.util.ArrayList<DailyDocVisitModel.Data.DcrDoctor.GiftList>()
        for(data in tempGiftList)
        {
            var giftModel=DailyDocVisitModel.Data.DcrDoctor.GiftList()
            giftModel.dcrId=dcrId
            giftModel.empId= HomePage.loginModelHomePage.empId
            giftModel.qty=data.qty
            giftModel.productId=data.id.toInt()
            giftModel.type="GIFT"
            giftList.add(giftModel)
        }
        saveModel.giftList= ArrayList()
        saveModel.giftList?.addAll(giftList)

        return saveModel
    }

    fun callPobSelectAlert()
    {
        val dialogBuilder = AlertDialog.Builder(requireActivity(), R.style.my_dialog)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.pobcreatealert, null)

        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        // alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 25)
        alertDialog.getWindow()?.setBackgroundDrawable(inset)

        val wmlp: WindowManager.LayoutParams? = alertDialog.getWindow()?.getAttributes()

        wmlp?.gravity = Gravity.TOP or Gravity.RIGHT


        val closePob_iv = dialogView.findViewById<View>(R.id.closePob_iv) as ImageView
        val okPob_iv = dialogView.findViewById<View>(R.id.okPob_iv) as TextView
        val pobProduct_rv = dialogView.findViewById<View>(R.id.pobProduct_rv) as RecyclerView
        pobProduct_rv.layoutManager=LinearLayoutManager(requireActivity())

        val productSearch_et = dialogView.findViewById<View>(R.id.productSearch_et) as EditText
        productSearch_et?.addTextChangedListener(filterTextPobWatcher)

        pobProduct_rv.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent?): Boolean {
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isAcceptingText()) { imm?.hideSoftInputFromWindow(v.windowToken, 0) }
                return false
            }
        })

        val pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this,1)
        pobProduct_rv.adapter= pobProductSelectAdapter

        productSearch_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pobProductSelectAdapter?.getFilter()?.filter(s.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        okPob_iv.setOnClickListener{
            requireActivity().runOnUiThread{
                GeneralClass(requireActivity()).hideKeyboard(requireActivity(),it)
            }
            pobProductSelectAdapter.setSelction()
            alertDialog.dismiss()
        }

        closePob_iv.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    val filterTextPobWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            pobProductSelectAdapter?.getFilter()?.filter(s.toString())
        }
        override fun afterTextChanged(editable: Editable) {}
    }

    fun openCloseModel(type: Int)
    {   selectionType=type

        val runnable= Runnable {
            if(type==1) {
                requireActivity().runOnUiThread {
                    if (workWithArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.INVISIBLE
                }
                commonSlectionAdapter=   CheckboxSpinnerAdapter(workWithArray, this)
            }

            if(type==3) {
                requireActivity().runOnUiThread {
                    if (giftArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.INVISIBLE
                }
                commonSlectionAdapter= CheckboxSpinnerAdapter(giftArray,this)
            }
            if(type==4)
            {
                requireActivity().runOnUiThread {
                    if (stokistArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.INVISIBLE
                }
                commonSlectionAdapter = CheckboxSpinnerAdapter(stokistArray, this)

            }

            requireActivity().runOnUiThread {
                views.checkRecyclerView_rv.adapter = commonSlectionAdapter

                val state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                        BottomSheetBehavior.STATE_COLLAPSED
                    else
                        BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.state = state
            }

        }
        Thread(runnable).start()
    }

    fun hideAllSelection()
    {   views.commonSearch_et.setText("")
        views.pobParent.visibility=View.INVISIBLE
        views.rcpaNestedScroll.visibility=View.INVISIBLE
        views.gift_rv.visibility=View.INVISIBLE
        views.workingWithRv.visibility=View.INVISIBLE
        views.selectBtn.visibility=View.INVISIBLE
    }


    override fun onClickButtonProduct(productModel: SyncModel.Data.Product, positon: Int) {

        GlobalScope.launch(Dispatchers.Default) {
            for ((index,data) in SplashActivity.staticSyncData?.productList?.withIndex()!!)
            {

                if(productModel?.productId==data.productId)
                {
                    productModel.notApi=SyncModel.Data.Product.NotApiData()
                    unSelectedProductList?.add(index, productModel)
                    selectedProductList.removeAt(positon)

                    requireActivity().runOnUiThread{
                        setPobAdapter()
                        //  alertClass?.hideAlert()
                    }
                }

            }
        }
    }

    override fun onChangeArray(
        passingArrayList: java.util.ArrayList<IdNameBoll_model>, isUpdate: Boolean,selectionTypeInterface: Int) {

        GlobalScope.launch(Dispatchers.Default) {
            var localSelection= if(selectionTypeInterface==0)selectionType else selectionTypeInterface
            if(localSelection==1)
            {
                workWithArray= ArrayList<IdNameBoll_model>()
                workWithArray.addAll(passingArrayList)

                var sendingList = workWithArray?.filter { s -> s.isChecked == true }

                requireActivity().runOnUiThread {
                    views.workingWithRv.adapter = TextWithEditAdapter(
                        sendingList as ArrayList<IdNameBoll_model>,
                        this@RetailerFillFragment,
                        0,
                        requireActivity(),
                        selectionType,
                        false
                    )
                    if(sendingList.size!=0)views.workingWithRv.visibility=View.VISIBLE
                }

            }

            if(localSelection==3)
            { giftArray= ArrayList<IdNameBoll_model>()
                giftArray.addAll(passingArrayList)

                var sendingList = giftArray?.filter { s -> s.isChecked == true }

                requireActivity().runOnUiThread {
                    views.gift_rv.adapter = TextWithEditAdapter(
                        sendingList as ArrayList<IdNameBoll_model>,
                        this@RetailerFillFragment,
                        1,
                        requireActivity(),
                        selectionType,
                        false
                    )
                    if(sendingList.size!=0)views.gift_rv.visibility=View.VISIBLE
                }


            }
            if(selectionType==4)
            {
                stokistArray= ArrayList<IdNameBoll_model>()
                stokistArray.addAll(passingArrayList)
                stokistArray.forEachIndexed { index, element ->
                    if(element.isChecked) {
                        selectedStockist=element
                        requireActivity().runOnUiThread {
                            views.stockistName.visibility=View.VISIBLE
                            views.stockistName.text="Stockist name - "+element.name }
                        }

                    element.isChecked=false
                    stokistArray.set(index,element)
                }
                requireActivity().runOnUiThread {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)  bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                }
            }
        }
    }

    override fun onClickButtonPOB(selectedList: ArrayList<DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList>) {
        // setSelectedPOBRecycler(selectedList)
    }


    fun setPobAdapter()
    {
        requireActivity().runOnUiThread{
            /*   pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
               pobProduct_rv.adapter= pobProductSelectAdapter*/

            selectedPobAdapter=SelectedPobAdapter(selectedProductList,this,this,requireActivity(),false)
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

    override fun onClickButtonProduct(selectedList: ArrayList<SyncModel.Data.Product>, type: Int) {

        if(type==100)
        {
            val selectedProduct=selectedList.get(0)
            ownBrand_et.setText(selectedProduct.productName)
            brandId= selectedProduct.productId!!
        }
        if(type==1)
        {
           // alertClass.showProgressAlert("")
            val runnable = java.lang.Runnable {
                //  selectedProductList.clear()

                for ((index,selected) in selectedList.withIndex())
                {
                    if(selected?.notApi?.insertedProductId!=0)
                    {

                        selected.notApi.isSaved=true
                        selectedProductList.add(selected)

                        val hashSet = HashSet<SyncModel.Data.Product>()
                        hashSet.addAll(selectedProductList)
                        selectedProductList.clear()
                        selectedProductList.addAll(hashSet)

                        unSelectedProductList.removeAt(index)
                        requireActivity().runOnUiThread {
                            //   selectedPobAdapter.notifyItemChanged(index)
                            //  pobProductSelectAdapter.notifyItemChanged(index)
                        }
                    }
                }
                requireActivity().runOnUiThread {

                    // pobProductSelectAdapter.notifyDataSetChanged()
                    setPobAdapter()
                    //pobProduct_rv.scrollToPosition(0)
                    Handler(Looper.getMainLooper()).postDelayed({
                        //   closeBottomSheet()
                      //  alertClass.hideAlert()
                    }, 1)
                }
            }
            Thread(runnable).start()
        }
        else
        {

        }

    }


    override fun onClickEdit(productModel: SyncModel.Data.Product, positon: Int) {
        productModel?.let { selectedProductList.set(positon, it) }
        calculateTotalProduct()
    }

    fun selectDoctorManager_alert(selectionType:Int)
    {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.selectionalert, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val list_rv= dialogView.findViewById<View>(R.id.list_rv) as RecyclerView
        val search_et= dialogView.findViewById<View>(R.id.search_et) as EditText
        val ok_btn= dialogView.findViewById<View>(R.id.ok_btn) as Button
        val headerTv= dialogView.findViewById<View>(R.id.headerTv) as TextView
        headerTv.setText("Select Doctor")

        val layoutManager = LinearLayoutManager(requireActivity())
        list_rv.layoutManager=layoutManager
        var adapterView : DoctorManagerSelector_Adapter? = null

        adapterView= DoctorManagerSelector_Adapter(arrayListSelectorDoctor,this,selectionType)

        list_rv.adapter = adapterView

        search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            )
            {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            )
            {
                adapterView?.getFilter()?.filter(s.toString())
            }
        })
        ok_btn.visibility=View.GONE
        ok_btn.setOnClickListener({
            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    fun AddRCPA_alert(type:Int,obj: DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail= DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail(),position:Int=0)
    {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setCancelable(false)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.add_rcpa_detail_alert, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val selectProduct= dialogView.findViewById<View>(R.id.selectProduct) as RecyclerView
        val closeIv= dialogView.findViewById<View>(R.id.closeIv) as ImageView
        ownBrand_et= dialogView.findViewById<View>(R.id.ownBrand_et) as EditText
        val productSearch_et= dialogView.findViewById<View>(R.id.productSearch_et) as EditText
        val rxunit_et= dialogView.findViewById<View>(R.id.rxunit_et) as EditText
        val competitor1_et= dialogView.findViewById<View>(R.id.competitor1_et) as EditText
        val cp1unit_et= dialogView.findViewById<View>(R.id.cp1unit_et) as EditText
        val competitor2_et= dialogView.findViewById<View>(R.id.competitor2_et) as EditText
        val competitor3_et= dialogView.findViewById<View>(R.id.competitor3_et) as EditText
        val competitor4_et= dialogView.findViewById<View>(R.id.competitor4_et) as EditText
        val cp2unit_et= dialogView.findViewById<View>(R.id.cp2unit_et) as EditText
        val cp3unit_et= dialogView.findViewById<View>(R.id.cp3unit_et) as EditText
        val cp4unit_et= dialogView.findViewById<View>(R.id.cp4unit_et) as EditText
        val addBtn= dialogView.findViewById<View>(R.id.addBtn) as Button
        val cancelBtn= dialogView.findViewById<View>(R.id.cancelBtn) as Button
        val parentScrollView= dialogView.findViewById<View>(R.id.parentScrollView) as LinearLayout
        val productFilter_paretn= dialogView.findViewById<View>(R.id.productFilter_paretn) as LinearLayout

        if(obj.brandName?.isEmpty()==false)
        {
            ownBrand_et.setText(obj.brandName)
            rxunit_et.setText(obj.brandUnits.toString())
            competitor1_et.setText(obj.cp1)
            competitor2_et.setText(obj.cp2)
            competitor3_et.setText(obj.cp3)
            competitor4_et.setText(obj.cp4)
            cp1unit_et.setText(obj.cPRx1.toString())
            cp2unit_et.setText(obj.cPRx2.toString())
            cp3unit_et.setText(obj.cPRx3.toString())
            cp4unit_et.setText(obj.cPRx4.toString())
        }


        val layoutManager = LinearLayoutManager(requireActivity())
        selectProduct.layoutManager=layoutManager
        val pobProductSelectAdapter=PobProductAdapter(mainProductList, passingSchemeList,this,2)

        productSearch_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pobProductSelectAdapter?.getFilter()?.filter(s.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        ownBrand_et.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                productFilter_paretn.visibility=View.GONE
                selectProduct.visibility=View.GONE
                parentScrollView.visibility=View.VISIBLE
            }

        })

        selectProduct.adapter = pobProductSelectAdapter
        ownBrand_et.setOnClickListener({
            productFilter_paretn.visibility=View.VISIBLE
            selectProduct.visibility=View.VISIBLE
            parentScrollView.visibility=View.GONE
        })

        closeIv.setOnClickListener({
            productFilter_paretn.visibility=View.GONE
            selectProduct.visibility=View.GONE
            parentScrollView.visibility=View.VISIBLE
        })

        cancelBtn.setOnClickListener({
            alertDialog.dismiss()
        })

        addBtn.setOnClickListener({
            if(ownBrand_et.text.toString().isEmpty())
            {
                ownBrand_et.setError("Required");ownBrand_et.requestFocus();return@setOnClickListener
            }

            if(rxunit_et.text.toString().isEmpty() || rxunit_et.text.toString().toInt()==0)
            {
                rxunit_et.setError("Required");rxunit_et.requestFocus();return@setOnClickListener
            }
            if(competitor1_et.text.toString().isEmpty())
            {
                competitor1_et.setError("Required");competitor1_et.requestFocus();return@setOnClickListener
            }
            if(cp1unit_et.text.toString().isEmpty()|| cp1unit_et.text.toString().toInt()==0)
            {
                cp1unit_et.setError("Required");cp1unit_et.requestFocus();return@setOnClickListener
            }
            if(competitor2_et.text.toString().isEmpty())
            {
                competitor2_et.setError("Required");competitor2_et.requestFocus();return@setOnClickListener
            }
            if(cp2unit_et.text.toString().isEmpty()|| cp2unit_et.text.toString().toInt()==0)
            {
                cp2unit_et.setError("Required");cp2unit_et.requestFocus();return@setOnClickListener
            }
            Log.e("dsffsfsfsdfgegdfv",obj.brandId.toString())
            if(obj.brandId!=null && obj.brandId != 0)
            {
                obj.brandName=ownBrand_et.text.toString()
                obj.brandUnits=rxunit_et.text.toString().toInt()
                obj.cp1=competitor1_et.text.toString()
                obj.cp2=competitor2_et.text.toString()
                obj.brandId=brandId
                obj.dCRId=dcrId


                obj.cp3=competitor3_et.text.toString()
                obj.cp4=competitor4_et.text.toString()

                if(cp1unit_et.text.toString().isEmpty()==false)  obj.cPRx1=cp1unit_et.text.toString().toInt()
                if(!cp2unit_et.text.toString().isEmpty()==false) obj.cPRx2=cp2unit_et.text.toString().toInt()
                if(!cp3unit_et.text.toString().isEmpty()==false) obj.cPRx3=cp3unit_et.text.toString().toInt()
                if(!cp4unit_et.text.toString().isEmpty()==false) obj.cPRx4=cp4unit_et.text.toString().toInt()

                when(type)
                {
                    1-> {
                        obj.rCPANo=1
                        saveRcpaDetailList1.set(position,obj)
                        adapter1.notifyItemChanged(position)}
                    2-> {
                        obj.rCPANo=2
                        saveRcpaDetailList2.set(position,obj)
                        adapter2.notifyItemChanged(position)}
                    3-> {
                        obj.rCPANo=3
                        saveRcpaDetailList3.set(position,obj)
                        adapter3.notifyItemChanged(position)}
                }
            }
            else{
                val objRcpaDetail= DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail()
                objRcpaDetail.brandName=ownBrand_et.text.toString()
                objRcpaDetail.brandUnits=rxunit_et.text.toString().toInt()
                objRcpaDetail.cp1=competitor1_et.text.toString()
                objRcpaDetail.cp2=competitor2_et.text.toString()
                objRcpaDetail.cp3=competitor3_et.text.toString()
                objRcpaDetail.cp4=competitor4_et.text.toString()
                objRcpaDetail.brandId=brandId
                objRcpaDetail.dCRId=dcrId

                if(!cp1unit_et.text.toString().isEmpty()) objRcpaDetail.cPRx1=cp1unit_et.text.toString().toInt()
                if(!cp2unit_et.text.toString().isEmpty()) objRcpaDetail.cPRx2=cp2unit_et.text.toString().toInt()
                if(!cp3unit_et.text.toString().isEmpty()) objRcpaDetail.cPRx3=cp3unit_et.text.toString().toInt()
                if(!cp4unit_et.text.toString().isEmpty()) objRcpaDetail.cPRx4=cp4unit_et.text.toString().toInt()

                when(type)
                {
                    1-> {
                        objRcpaDetail.rCPANo=1
                        saveRcpaDetailList1.add(objRcpaDetail)
                        adapter1.notifyItemInserted(saveRcpaDetailList1.size)}
                    2-> {
                        objRcpaDetail.rCPANo=2
                        saveRcpaDetailList2.add(objRcpaDetail)
                        adapter2.notifyItemInserted(saveRcpaDetailList2.size)}
                    3-> {
                        objRcpaDetail.rCPANo=3
                        saveRcpaDetailList3.add(objRcpaDetail)
                        adapter3.notifyItemInserted(saveRcpaDetailList3.size)}
                }
            }


            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    override fun selectorArray(docMangerList: ArrayList<DocManagerModel>, selectionType: Int) {
        for(item in docMangerList)
        {
            if(item.checked)
            {      if(::alertDialog.isInitialized && alertDialog!=null )
                    {
                        alertDialog.dismiss()
                    }
                if(selectionType==10){
                    doctorRcpa1=item
                    views.doctorOne_et.setText(item.name)
                    views.addBrandOne_btn.setAlpha(1f)
                    views.addBrandOne_btn.isEnabled=true}
                if(selectionType==20){ views.doctorTwo_et.setText(item.name)
                    doctorRcpa2=item
                    views.addBrandTwo_btn.setAlpha(1f)
                    views.addBrandTwo_btn.isEnabled=true}
                if(selectionType==30){ views.doctorThree_et.setText(item.name)
                    doctorRcpa3=item
                    views.addBrandThree_btn.setAlpha(1f)
                    views.addBrandThree_btn.isEnabled=true}
            }
        }
    }

    override fun onClickString(passingInterface: String?) { }

    inner class AddedRcpa_Adapter(
        val type: Int,
        val rcpaList: ArrayList<DailyDocVisitModel.Data.DcrDoctor.Rcpavo.RCPADetail>
    ) : RecyclerView.Adapter<AddedRcpa_Adapter.ViewHolders>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            var itemView= LayoutInflater.from(parent.context).inflate(R.layout.added_rcpa_view, parent, false)
            return ViewHolders(itemView);
        }

        override fun onBindViewHolder(holder:ViewHolders, position: Int) {
            val modeldata = rcpaList?.get(position)
            holder.productName_tv.setText(modeldata?.brandName)
            holder.rxUnit_tv.setText("Rx unit: "+modeldata?.brandUnits)
            holder.cp1rx_tv.setText(modeldata?.cPRx1.toString())
            holder.competitor1Tv.setText(modeldata?.cp1)
            holder.cp2rx_tv.setText(modeldata?.cPRx2.toString())
            holder.competitor2Tv.setText(modeldata?.cp2)

            if(modeldata?.cp3?.isEmpty() == false) holder.cp3Parent.visibility=View.VISIBLE
            if(modeldata?.cp4?.isEmpty() == false) holder.cp4Parent.visibility=View.VISIBLE

            holder.competitor3Tv.setText(modeldata?.cp3)
            holder.competitor4Tv.setText(modeldata?.cp4)
            if(modeldata?.cPRx3!=null) holder.cp3rx_tv.setText(modeldata?.cPRx3.toString())
            if(modeldata?.cPRx4!=null) holder.cp4rx_tv.setText(modeldata?.cPRx4.toString())


            holder.editClick_iv.setOnClickListener {
                if(::alertDialog.isInitialized  && alertDialog.isShowing) { alertDialog.dismiss() }
                AddRCPA_alert(type,modeldata,position)
            }

            holder.deleteClick_iv.setOnClickListener {
                when(type)
                {
                    1->{
                        saveRcpaDetailList1.removeAt(position)
                        adapter1.notifyItemRemoved(position)
                    }
                    2->{
                        saveRcpaDetailList2.removeAt(position)
                        adapter2.notifyItemRemoved(position)
                    }
                    3->{
                        saveRcpaDetailList3.removeAt(position)
                        adapter3.notifyItemRemoved(position)
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return rcpaList!!.size
        }

        inner class ViewHolders(view: View): RecyclerView.ViewHolder(view){
            var productName_tv=view.findViewById<TextView>(R.id.productName_tv)
            var rxUnit_tv=view.findViewById<TextView>(R.id.rxUnit_tv)
            var competitor4Tv=view.findViewById<TextView>(R.id.competitor4Tv)
            var competitor3Tv=view.findViewById<TextView>(R.id.competitor3Tv)
            var competitor2Tv=view.findViewById<TextView>(R.id.competitor2Tv)
            var competitor1Tv=view.findViewById<TextView>(R.id.competitor1Tv)
            var cp1rx_tv=view.findViewById<TextView>(R.id.cp1rx_tv)
            var cp2rx_tv=view.findViewById<TextView>(R.id.cp2rx_tv)
            var cp3rx_tv=view.findViewById<TextView>(R.id.cp3rx_tv)
            var cp4rx_tv=view.findViewById<TextView>(R.id.cp4rx_tv)
            var cp3Parent=view.findViewById<LinearLayout>(R.id.cp3Parent)
            var cp4Parent=view.findViewById<LinearLayout>(R.id.cp4Parent)

            var editClick_iv=view.findViewById<ImageView>(R.id.editClick_iv)
            var deleteClick_iv=view.findViewById<ImageView>(R.id.deleteClick_iv)
        }


    }
    fun getUpdateData()
    {
        val runnable = java.lang.Runnable {

            val edetailingEditModel=Gson().fromJson(arguments?.getString("retailerData"),DailyDocVisitModel.Data.DcrDoctor::class.java)

            arguments?.getString("retailerData")?.let { Log.e("dfosjdfposdjfoisd", it) }

            if(edetailingEditModel.dataSaveType?.lowercase().equals("s"))
            {
                checkIsDcrSave=true
                selectBtn.visibility=View.GONE
                pobProduct_btn.visibility=View.GONE
                assignStockist.visibility=View.GONE
                visitPurpose_spinner.isEnabled=false
                views.submit_btn.visibility=View.INVISIBLE
            }

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
                val getWorkingList: List<String>? = edetailingEditModel.workWith?.split(",")
                if (getWorkingList != null) {
                    for(workingWith in getWorkingList) {
                        for( (index,getWorking) in workWithArray.withIndex()) {
                            if(workingWith.toInt() == getWorking.id.toInt()){
                                getWorking.isChecked=true
                                workWithArray.set(index,getWorking)
                                requireActivity().runOnUiThread {
                                    workingWithRv.visibility=View.VISIBLE
                                }

                            }
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
                        availableProduct.notApi.pobId= pobProducts?.pobId!!
                        availableProduct.notApi.pobNo= pobProducts?.pobNo.toString()

                        unSelectedProductList.removeAt(index)
                        selectedProductList.add(availableProduct)
                    }
                }
            }

            var spinnerSelect=0
            for((index,visitPurpos) in CommonListGetClass().getWorkTypeForSpinner().withIndex())
            {
                if(visitPurpos.workId==edetailingEditModel.visitPurpose)
                {
                    spinnerSelect=index
                }
            }


            requireActivity().runOnUiThread(java.lang.Runnable {
                remark_Et.setText(edetailingEditModel.remark)
                workingWithRv.adapter = TextWithEditAdapter(
                    passingWorking as ArrayList<IdNameBoll_model>,
                    this@RetailerFillFragment,
                    0,
                    requireActivity(),
                    selectionType,
                    checkIsDcrSave
                )
                gift_rv.adapter = TextWithEditAdapter(
                    passingGift as ArrayList<IdNameBoll_model>,
                    this@RetailerFillFragment,
                    1,
                    requireActivity(),
                    selectionType,
                    checkIsDcrSave
                )

                remark_Et.isEnabled=false
                visitPurpose_spinner.setSelection(spinnerSelect)
                stokistArray.forEachIndexed { index, element ->
                    if(element.id.toInt() == edetailingEditModel.pobObject?.stockistId) {
                        selectedStockist=element
                        stockistName.visibility=View.VISIBLE
                        stockistName.text="Stockist name - "+element.name
                        element.isChecked=false
                        stokistArray.set(index,element)}
                }
                setPobAdapter()

                val size: Int = edetailingEditModel.RCPAList?.size!!
                if(size>=1)
                {
                    edetailingEditModel.RCPAList?.get(0)?.rCPADetailList?.let {
                        saveRcpaDetailList1.addAll(
                            it
                        )
                    }
                    adapter1.notifyDataSetChanged()
                    doctorRcpa1=DocManagerModel()
                    doctorRcpa1.name= edetailingEditModel?.RCPAList?.get(0)?.docName.toString()
                    doctorRcpa1.id= edetailingEditModel?.RCPAList?.get(0)?.docId!!
                    views.doctorOne_et.setText(doctorRcpa1.name)
                    views.addBrandOne_btn.setAlpha(1f)
                    views.addBrandOne_btn.isEnabled=true
                }
                if(size>=2)
                {
                    edetailingEditModel.RCPAList?.get(1)?.rCPADetailList?.let {
                        saveRcpaDetailList2.addAll(
                            it
                        )
                    }
                    doctorRcpa2=DocManagerModel()
                    adapter2.notifyDataSetChanged()
                    doctorRcpa2.name= edetailingEditModel?.RCPAList?.get(1)?.docName.toString()
                    doctorRcpa2.id= edetailingEditModel?.RCPAList?.get(1)?.docId!!
                    views.doctorTwo_et.setText(doctorRcpa2.name)
                    views.addBrandTwo_btn.setAlpha(1f)
                    views.addBrandTwo_btn.isEnabled=true
                }
                if(size>=3)
                {
                    edetailingEditModel.RCPAList?.get(2)?.rCPADetailList?.let {
                        saveRcpaDetailList3.addAll(
                            it
                        )
                    }
                    doctorRcpa3=DocManagerModel()
                    adapter3.notifyDataSetChanged()
                    doctorRcpa3.name= edetailingEditModel?.RCPAList?.get(2)?.docName.toString()
                    doctorRcpa3.id= edetailingEditModel?.RCPAList?.get(2)?.docId!!
                    views.doctorThree_et.setText(doctorRcpa3.name)
                    views.addBrandThree_btn.setAlpha(1f)
                    views.addBrandThree_btn.isEnabled=true
                }
            })
        }
        Thread(runnable).start()
    }
    fun  addDoctorInStep(size: Int, edetailingEditModel: DailyDocVisitModel.Data.DcrDoctor)
    {
        Log.e("sdfsdfsdfdsgfsdf",size.toString())
        if(size==1)
        {
            edetailingEditModel.RCPAList?.get(0)?.rCPADetailList?.let {
                saveRcpaDetailList1.addAll(
                    it
                )
            }
            adapter1.notifyDataSetChanged()
            doctorRcpa1=DocManagerModel()
            doctorRcpa1.name= edetailingEditModel?.RCPAList?.get(0)?.docName.toString()
            doctorRcpa1.id= edetailingEditModel?.RCPAList?.get(0)?.docId!!
            views.doctorOne_et.setText(doctorRcpa1.name)
            views.addBrandOne_btn.setAlpha(1f)
            views.addBrandOne_btn.isEnabled=true
        }
        else if(size==2)
        {
            edetailingEditModel.RCPAList?.get(1)?.rCPADetailList?.let {
                saveRcpaDetailList2.addAll(
                    it
                )
            }
            doctorRcpa2=DocManagerModel()
            adapter2.notifyDataSetChanged()
            doctorRcpa2.name= edetailingEditModel?.RCPAList?.get(1)?.docName.toString()
            doctorRcpa2.id= edetailingEditModel?.RCPAList?.get(1)?.docId!!
            views.doctorTwo_et.setText(doctorRcpa2.name)
            views.addBrandTwo_btn.setAlpha(1f)
            views.addBrandTwo_btn.isEnabled=true
        }
        else if(size==3)
        {
            edetailingEditModel.RCPAList?.get(2)?.rCPADetailList?.let {
                saveRcpaDetailList3.addAll(
                    it
                )
            }
            doctorRcpa3=DocManagerModel()
            adapter3.notifyDataSetChanged()
            doctorRcpa3.name= edetailingEditModel?.RCPAList?.get(2)?.docName.toString()
            doctorRcpa3.id= edetailingEditModel?.RCPAList?.get(2)?.docId!!
            views.doctorThree_et.setText(doctorRcpa3.name)
            views.addBrandThree_btn.setAlpha(1f)
            views.addBrandThree_btn.isEnabled=true
        }
    }
}