package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
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
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.gson.Gson
import com.zipow.videobox.confapp.ConfMgr.getApplicationContext
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.view.*
import kotlinx.android.synthetic.main.pob_product_bottom_sheet.view.*
import java.util.ArrayList
import java.util.HashSet

class RetailerFillFragment : Fragment(), IdNameBoll_interface, PobProductTransfer,
    productTransferIndividual
    , productTransfer, EditInterface {

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

            if (isChecked && R.id.samples_btn == checkedId) {
                hideAllSelection()
                views.selectBtn.setText("Select Samples")
                views.sample_rv.visibility = View.VISIBLE
                views.selectBtn.visibility = View.VISIBLE
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



        val pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
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
        views.sample_rv.visibility=View.GONE
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
                if(sendingList.size!=0)views.sample_rv.visibility=View.VISIBLE

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

}