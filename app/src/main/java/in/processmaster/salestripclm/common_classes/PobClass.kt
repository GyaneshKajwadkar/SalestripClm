package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.CreatePobActivity
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.adapter.ButtonFilterAdapter
import `in`.processmaster.salestripclm.adapter.PobProductAdapter
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import `in`.processmaster.salestripclm.models.SyncModel
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import java.util.HashSet

class PobCommonClass(val activity : Activity?)
{

    fun getStockist(): ArrayList<IdNameBoll_model>
    {   var stokistArray=ArrayList<IdNameBoll_model>()
        var listStokist = SplashActivity.staticSyncData?.retailerList?.filter { s -> s.type == "STOCKIST" }
        if(listStokist?.size==0) return stokistArray
        if (listStokist != null) {
            for(stockist in listStokist) {
                val data =IdNameBoll_model()
                data.id= stockist.retailerId.toString()
                data.city= stockist.cityName.toString()
                data.name= stockist.shopName.toString()
                stokistArray.add(data)
            }
        }
        return stokistArray
    }

    fun getProductList() : ArrayList<SyncModel.Data.Product>
    {  var productList:ArrayList<SyncModel.Data.Product> = ArrayList()
        productList.addAll(SplashActivity.staticSyncData?.productList?.filter { s -> (s.productType==1) } as ArrayList<SyncModel.Data.Product>)
       removeAllSelectionPob(productList)
        return  productList
    }

    fun getSchemeList() : ArrayList<SyncModel.Data.Scheme>
    {
        var schemeList:ArrayList<SyncModel.Data.Scheme> = ArrayList()
        val getSchemeList= SplashActivity.staticSyncData?.schemeList
        val filterByTypeSchemeList= getSchemeList?.filter { data -> (data?.schemeFor=="S" || data?.schemeFor=="H") }

        val getDocDetail: SyncModel.Data.Doctor? = SplashActivity.staticSyncData?.doctorList?.find { it.doctorId == PresentEDetailingFrag.doctorIdDisplayVisual }

        getSchemeList?.clear()
        filterByTypeSchemeList?.sortedBy { it.schemeFor }?.let { getSchemeList?.addAll(it) }

        getSchemeList?.let { schemeList.addAll(it)}

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
                            schemeList.removeAt(indexS)
                        }
                    }
                }
            }
        }
        return schemeList;
    }

    fun callPobSelectAlert(
        filterTextPobWatcher: TextWatcher,
        mainProductList: ArrayList<SyncModel.Data.Product>,
        unSelectedProductList: ArrayList<SyncModel.Data.Product>,
        passingSchemeList: ArrayList<SyncModel.Data.Scheme>,
        context: productTransfer
    )
    {
        val dialogBuilder = activity?.let { AlertDialog.Builder(it, R.style.my_dialog) }
        val inflater = activity?.layoutInflater
        val dialogView: View? = inflater?.inflate(R.layout.pobcreatealert, null)

        dialogBuilder?.setView(dialogView)

        val alertDialog: AlertDialog? = dialogBuilder?.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 25)
        alertDialog?.getWindow()?.setBackgroundDrawable(inset)

        val wmlp: WindowManager.LayoutParams? = alertDialog?.getWindow()?.getAttributes()

        wmlp?.gravity = Gravity.TOP or Gravity.RIGHT


        val closePob_iv = dialogView?.findViewById<View>(R.id.closePob_iv) as ImageView
        val okPob_iv = dialogView?.findViewById<View>(R.id.okPob_iv) as TextView
        val filterRv = dialogView?.findViewById<View>(R.id.filterRv) as RecyclerView
        val pobProduct_rv = dialogView?.findViewById<View>(R.id.pobProduct_rv) as RecyclerView
        pobProduct_rv.layoutManager= LinearLayoutManager(activity)
        filterRv.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false))

        val productSearch_et = dialogView.findViewById<View>(R.id.productSearch_et) as EditText
        productSearch_et?.addTextChangedListener(filterTextPobWatcher)

        var categoryList:ArrayList<String> =ArrayList<String>()

        for(categoryName in mainProductList)
        {
            categoryName.categoryName?.let { categoryList.add(it) }
        }

        val uniqueValues: HashSet<String> = HashSet(categoryList)
        val categoryListFiltered :ArrayList<CommonModel.FilterModel> =ArrayList<CommonModel.FilterModel>()

        for(categoryName in uniqueValues)
        {
            val filterModel= CommonModel.FilterModel()
            filterModel.categoryName= categoryName.toString()
            categoryListFiltered.add(filterModel)
        }

        val pobProductSelectAdapter=
            PobProductAdapter(unSelectedProductList, passingSchemeList,context,1,productSearch_et)
        pobProduct_rv.adapter= pobProductSelectAdapter

        val filterAdapter= ButtonFilterAdapter(categoryListFiltered, pobProductSelectAdapter)
        filterRv.adapter=filterAdapter

        productSearch_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pobProductSelectAdapter?.getFilter()?.filter(s.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        okPob_iv.setOnClickListener{

                activity?.let { it1 -> GeneralClass(it1).hideKeyboard(activity,it) }

            pobProductSelectAdapter.setSelction()
            alertDialog?.dismiss()
        }

        closePob_iv.setOnClickListener{

            alertDialog?.dismiss()
        }
        alertDialog?.show()

    }


    fun calculateTotalProduct(selectedProductList:ArrayList<SyncModel.Data.Product>,totalProductPrice_tv:TextView)
    {
        val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }
        var calculation=0.0
        for(data in filterSelectecd)
        { calculation= data.notApi.amount?.plus(calculation)!! }
        totalProductPrice_tv.setText("Grand Total: "+String.format("%.2f", calculation))
    }

    fun removeAllSelectionPob(productList:ArrayList<SyncModel.Data.Product>)
    {
        for((index,unSelected) in productList.withIndex())
        {
            if(unSelected.notApi.isSaved) {
                unSelected.notApi.isSaved = false
                unSelected.notApi.qty=null
                unSelected.notApi.totalQty=null
                productList.set(index,unSelected)
            }

        }
    }


}