package `in`.processmaster.salestripclm.fragments
import DocManagerModel
import DoctorManagerSelector_Adapter
import SelectorInterface
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.PobProductAdapter
import `in`.processmaster.salestripclm.adapter.SelectedPobAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.interfaceCode.*
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
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
import kotlinx.android.synthetic.main.lastrcpa_layout.*
import kotlinx.android.synthetic.main.pob_product_bottom_sheet.view.*
import java.util.HashSet


class RetailerFillFragment : Fragment(), IdNameBoll_interface, PobProductTransfer,
    productTransferIndividual
    , productTransfer, EditInterface,SelectorInterface,StringInterface {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetPobProduct: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var sampleArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var stokistArray=ArrayList<IdNameBoll_model>()
    var selectionType=0
    var pobProductSelectAdapter= PobProductAdapter()
    lateinit var selectedPobAdapter: SelectedPobAdapter
    var commonSlectionAdapter=CheckboxSpinnerAdapter(ArrayList(),this)
    var mainProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var unSelectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()

    var selectedStockist=IdNameBoll_model()
    lateinit var views:View
    var passingSchemeList:ArrayList<SyncModel.Data.Scheme> = ArrayList()
    var arrayListSelectorDoctor: ArrayList<DocManagerModel> = ArrayList()

    //RCPA section
    lateinit var alertDialog: AlertDialog
    lateinit var ownBrand_et: EditText //rcpa detail own brand
    lateinit var adapter1: AddedRcpa_Adapter
    lateinit var adapter2: AddedRcpa_Adapter
    lateinit var adapter3: AddedRcpa_Adapter
    var saveRcpaDetailList1:ArrayList<CommonModel.SaveRcpaDetail> = ArrayList()
    var saveRcpaDetailList2:ArrayList<CommonModel.SaveRcpaDetail> = ArrayList()
    var saveRcpaDetailList3:ArrayList<CommonModel.SaveRcpaDetail> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_retailer_fill, container, false)

        bottomSheetBehavior = BottomSheetBehavior.from(views.bottomSheet)
        bottomSheetPobProduct = BottomSheetBehavior.from(views.BS_product_pob)
        //checkRecyclerView_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.workingWithRv.layoutManager= LinearLayoutManager(requireActivity())
        views.sample_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.gift_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.selectedPob_rv.layoutManager= LinearLayoutManager(requireActivity())


        views.rcpaDetailOne_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.rcpaDetailTwo_rv.layoutManager= LinearLayoutManager(requireActivity())
        views.rcpaDetailThree_rv.layoutManager= LinearLayoutManager(requireActivity())
        adapter1=AddedRcpa_Adapter(1,saveRcpaDetailList1)
        adapter2=AddedRcpa_Adapter(2,saveRcpaDetailList2)
        adapter3=AddedRcpa_Adapter(3,saveRcpaDetailList3)
        views.rcpaDetailOne_rv.adapter=adapter1
        views.rcpaDetailTwo_rv.adapter=adapter2
        views.rcpaDetailThree_rv.adapter=adapter3
        // pobProduct_rv.layoutManager= LinearLayoutManager(requireActivity())

        val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(requireActivity(),
            R.layout.spinner_txt, CommonListGetClass().getWorkTypeForSpinner())

        views.visitPurpose_spinner.setAdapter(adapterVisit)

        views.toggleButton.forEach { button ->
            button.setOnClickListener { (button as MaterialButton).isChecked = true }
        }

        views.close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        views.assignStockist.setOnClickListener({
            commonSearch_et.visibility=View.GONE
            openCloseModel(4)})

        views.toggleButton.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked && R.id.rcpa_btn == checkedId) {
                hideAllSelection()
                views.selectBtn.setText("Select Samples")
                views.rcpaNestedScroll.visibility = View.VISIBLE
                views.selectBtn.visibility = View.GONE
            } else if (isChecked && R.id.workingWith_btn == checkedId) {
                hideAllSelection()
                views?.selectBtn?.setText("Select Working with")
                views?.workingWithRv?.visibility = View.VISIBLE
                views?.selectBtn?.visibility = View.VISIBLE
            } else if (isChecked && R.id.gifts_btn == checkedId) {
                hideAllSelection()
                views?.selectBtn.setText("Select Gifts")
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

        views.pobProduct_btn.setOnClickListener({
            //    closeBottomSheet()
            callPobSelectAlert()
        })

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

        views.doctorOne_et.setOnClickListener { selectDoctorManager_alert(10)}
        views.doctorTwo_et.setOnClickListener { selectDoctorManager_alert(20)}
        views.doctorThree_et.setOnClickListener { selectDoctorManager_alert(30)}
        views.addBrandOne_btn.setAlpha(0.5f)
        views.addBrandOne_btn.setClickable(false)
        views.addBrandTwo_btn.setAlpha(0.5f)
        views.addBrandTwo_btn.setClickable(false)
        views.addBrandThree_btn.setAlpha(0.5f)
        views.addBrandThree_btn.setClickable(false)

        views.addBrandOne_btn.setOnClickListener {
            AddRCPA_alert(1)
        }
        views.addBrandTwo_btn.setOnClickListener {
            AddRCPA_alert(2)
        }
        views.addBrandThree_btn.setOnClickListener {
            AddRCPA_alert(3)
        }

        return views
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
                    if (workWithArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
                }
                commonSlectionAdapter=   CheckboxSpinnerAdapter(workWithArray, this)
            }
            if(type==2) {
                requireActivity().runOnUiThread {
                    if (sampleArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
                }
                commonSlectionAdapter= CheckboxSpinnerAdapter(sampleArray,this)
            }
            if(type==3) {
                requireActivity().runOnUiThread {
                    if (giftArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
                }
                commonSlectionAdapter= CheckboxSpinnerAdapter(giftArray,this)
            }
            if(type==4)
            {
                requireActivity().runOnUiThread {
                    if (stokistArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
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
        views.pobParent.visibility=View.GONE
        views.rcpaNestedScroll.visibility=View.GONE
        views.gift_rv.visibility=View.GONE
        views.workingWithRv.visibility=View.GONE
        views.selectBtn.visibility=View.GONE
    }


    override fun onClickButtonProduct(productModel: SyncModel.Data.Product, positon: Int) {


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

    override fun onChangeArray(
        passingArrayList: java.util.ArrayList<IdNameBoll_model>, isUpdate: Boolean,selectionTypeInterface: Int) {

        var localSelection= if(selectionTypeInterface==0)selectionType else selectionTypeInterface

        if(localSelection==1)
        {
            workWithArray= ArrayList<IdNameBoll_model>()
            workWithArray.addAll(passingArrayList)

                var sendingList = workWithArray?.filter { s -> s.isChecked == true }

                views.workingWithRv.adapter = TextWithEditAdapter(
                    sendingList as ArrayList<IdNameBoll_model>,
                    this,
                    0,
                    requireActivity(),
                    selectionType,
                    false
                )
                if(sendingList.size!=0)views.workingWithRv.visibility=View.VISIBLE
        }
        if(localSelection==2)
        {
            sampleArray= ArrayList<IdNameBoll_model>()
            sampleArray.addAll(passingArrayList)

                var sendingList = sampleArray?.filter { s -> s.isChecked == true }
                views.sample_rv.adapter = TextWithEditAdapter(
                    sendingList as ArrayList<IdNameBoll_model>,
                    this,
                    1,
                    requireActivity(),
                    selectionType,
                    false
                )
              //  if(sendingList.size!=0)views.sample_rv.visibility=View.VISIBLE

        }
        if(localSelection==3)
        { giftArray= ArrayList<IdNameBoll_model>()
            giftArray.addAll(passingArrayList)

                var sendingList = giftArray?.filter { s -> s.isChecked == true }
                views.gift_rv.adapter = TextWithEditAdapter(
                    sendingList as ArrayList<IdNameBoll_model>,
                    this,
                    1,
                    requireActivity(),
                    selectionType,
                    false
                )
                if(sendingList.size!=0)views.gift_rv.visibility=View.VISIBLE

        }
        if(selectionType==4)
        {
            stokistArray= ArrayList<IdNameBoll_model>()
            stokistArray.addAll(passingArrayList)
            stokistArray.forEachIndexed { index, element ->
                if(element.isChecked) {
                    selectedStockist=element
                    views.stockistName.visibility=View.VISIBLE
                    views.stockistName.text="Stockist name - "+element.name }
                element.isChecked=false
                stokistArray.set(index,element)
            }
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)  bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

    fun AddRCPA_alert(type:Int,obj: CommonModel.SaveRcpaDetail= CommonModel.SaveRcpaDetail(),position:Int=0)
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

        if(!obj.ownBrand.isEmpty())
        {
            ownBrand_et.setText(obj.ownBrand)
            rxunit_et.setText(obj.rxUnit)
            competitor1_et.setText(obj.competitor1)
            competitor2_et.setText(obj.competitor2)
            competitor3_et.setText(obj.competitor3)
            competitor4_et.setText(obj.competitor4)
            cp1unit_et.setText(obj.cp1Rxunit)
            cp2unit_et.setText(obj.cp2Rxunit)
            cp3unit_et.setText(obj.cp3Rxunit)
            cp4unit_et.setText(obj.cp4Rxunit)
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

            if(!obj.ownBrand.isEmpty())
            {
                obj.ownBrand=ownBrand_et.text.toString()
                obj.rxUnit=rxunit_et.text.toString()
                obj.competitor1=competitor1_et.text.toString()
                obj.competitor2=competitor2_et.text.toString()

                obj.competitor3=competitor3_et.text.toString()
                obj.competitor4=competitor4_et.text.toString()
                obj.cp1Rxunit=cp1unit_et.text.toString()
                obj.cp2Rxunit=cp2unit_et.text.toString()
                obj.cp3Rxunit=cp3unit_et.text.toString()
                obj.cp4Rxunit=cp4unit_et.text.toString()

                when(type)
                {
                    1-> {   saveRcpaDetailList1.set(position,obj)
                        adapter1.notifyItemChanged(position)}
                    2-> {  saveRcpaDetailList2.add(position,obj)
                        adapter2.notifyItemChanged(position)}
                    3-> {   saveRcpaDetailList3.add(position,obj)
                        adapter3.notifyItemChanged(position)}
                }
            }
            else{
                val objRcpaDetail= CommonModel.SaveRcpaDetail()
                objRcpaDetail.ownBrand=ownBrand_et.text.toString()
                objRcpaDetail.rxUnit=rxunit_et.text.toString()
                objRcpaDetail.competitor1=competitor1_et.text.toString()
                objRcpaDetail.competitor2=competitor2_et.text.toString()
                objRcpaDetail.competitor3=competitor3_et.text.toString()
                objRcpaDetail.competitor4=competitor4_et.text.toString()
                objRcpaDetail.cp1Rxunit=cp1unit_et.text.toString()
                objRcpaDetail.cp2Rxunit=cp2unit_et.text.toString()
                objRcpaDetail.cp3Rxunit=cp3unit_et.text.toString()
                objRcpaDetail.cp4Rxunit=cp4unit_et.text.toString()
                when(type)
                {
                    1-> {   saveRcpaDetailList1.add(objRcpaDetail)
                        adapter1.notifyItemInserted(saveRcpaDetailList1.size)}
                    2-> {  saveRcpaDetailList2.add(objRcpaDetail)
                        adapter2.notifyItemInserted(saveRcpaDetailList2.size)}
                    3-> {   saveRcpaDetailList3.add(objRcpaDetail)
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
                    views.doctorOne_et.setText(item.name)
                    views.addBrandOne_btn.setAlpha(1f)
                    views.addBrandOne_btn.setClickable(true)}
                if(selectionType==20){ views.doctorTwo_et.setText(item.name)
                    views.addBrandTwo_btn.setAlpha(1f)
                    views.addBrandTwo_btn.setClickable(true)}
                if(selectionType==30){ views.doctorThree_et.setText(item.name)
                    views.addBrandThree_btn.setAlpha(1f)
                    views.addBrandThree_btn.setClickable(true)}
            }
        }
    }

    override fun onClickString(passingInterface: String?) {

    }

    inner class AddedRcpa_Adapter(
        val type: Int,
        val rcpaList: ArrayList<CommonModel.SaveRcpaDetail>
    ) : RecyclerView.Adapter<AddedRcpa_Adapter.ViewHolders>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            var itemView= LayoutInflater.from(parent.context).inflate(R.layout.added_rcpa_view, parent, false)
            return ViewHolders(itemView);
        }

        override fun onBindViewHolder(holder:ViewHolders, position: Int) {
            val modeldata = rcpaList?.get(position)
            holder.productName_tv.setText(modeldata.ownBrand)
            holder.rxUnit_tv.setText("Rx unit: "+modeldata.rxUnit)
            holder.cp1rx_tv.setText(modeldata.cp1Rxunit)
            holder.competitor1Tv.setText(modeldata.competitor1)
            holder.cp2rx_tv.setText(modeldata.cp2Rxunit)
            holder.competitor2Tv.setText(modeldata.competitor2)

            if(!modeldata.competitor3.isEmpty()) holder.cp3Parent.visibility=View.VISIBLE
            if(!modeldata.competitor4.isEmpty()) holder.cp4Parent.visibility=View.VISIBLE

            holder.competitor3Tv.setText(modeldata.competitor3)
            holder.competitor4Tv.setText(modeldata.competitor4)
            holder.cp3rx_tv.setText(modeldata.cp3Rxunit)
            holder.cp4rx_tv.setText(modeldata.cp4Rxunit)

            holder.editClick_iv.setOnClickListener {
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


}