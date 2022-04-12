package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.PobProductAdapter
import `in`.processmaster.salestripclm.adapter.SelectedPobAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.interfaceCode.PobProductTransfer
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.interfaceCode.productTransferIndividual
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.*
import kotlinx.android.synthetic.main.fragment_retailer_fill.view.*
import kotlinx.android.synthetic.main.pob_product_bottom_sheet.view.*
import java.util.ArrayList
import java.util.HashSet

class RetailerFillFragment : Fragment(), IdNameBoll_interface, PobProductTransfer,
    productTransferIndividual
    , productTransfer {


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetPobProduct: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var sampleArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var stokistArray=ArrayList<IdNameBoll_model>()
    var selectionType=0
    var pobProductSelectAdapter= PobProductAdapter()
    var selectedPobAdapter= SelectedPobAdapter()
    var commonSlectionAdapter=CheckboxSpinnerAdapter(ArrayList(),this)
    var mainProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var unSelectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedStockist=IdNameBoll_model()
    lateinit var views:View

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


        return views
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

       /*     selectedPobAdapter=SelectedPobAdapter(selectedProductList,this,requireActivity())
            selectedPob_rv.adapter= selectedPobAdapter*/

            calculateTotalProduct()
        }
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

}