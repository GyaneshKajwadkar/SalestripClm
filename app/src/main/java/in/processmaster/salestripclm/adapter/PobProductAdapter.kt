package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.models.SyncModel
import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PobProductAdapter(
    val productList: ArrayList<SyncModel.Data.Product>?,
    val schemeList: ArrayList<SyncModel.Data.Scheme>?,
    var sendProductInterface: productTransfer?,
    val viewType:Int
): RecyclerView.Adapter<PobProductAdapter.MyViewHolder>(), Filterable {

    var productFilteringList= ArrayList(productList)

    constructor() : this(ArrayList(), ArrayList(),null,0) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pob_product_adapter, parent, false)
        return MyViewHolder(itemView)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var titlePobproduct_tv = view.findViewById<TextView>(R.id.titlePobproduct_tv)
        var uom_tv = view.findViewById<TextView>(R.id.uom_tv)
        var division_tv = view.findViewById<TextView>(R.id.division_tv)
        var scheme_tv = view.findViewById<TextView>(R.id.scheme_tv)
        var qty_et = view.findViewById<EditText>(R.id.qty_et)
        var parentlinearL = view.findViewById<View>(R.id.parentlinearL)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {

        if(viewType==2)
        {
            val model = productFilteringList?.get(position)
            holder.titlePobproduct_tv.text=model?.productName
            holder.uom_tv.visibility=View.GONE
            holder.division_tv.visibility=View.GONE
            holder.qty_et.visibility=View.GONE
            holder.titlePobproduct_tv.setOnClickListener({
                val tempList: ArrayList<SyncModel.Data.Product> = ArrayList()
                tempList.add(model)
                sendProductInterface?.onClickButtonProduct(tempList,100)
            })

        }
        else{
            val model = productFilteringList?.get(position)
            holder.titlePobproduct_tv.text=model?.productName
            holder.uom_tv.text="UOM: "+model?.packingTypeName
            holder.division_tv.text="Divison: "+model?.divisionName
            if (model?.notApi?.qty != null) {
                holder.qty_et.setText(model.notApi?.qty.toString())
            }
            if(model?.notApi?.insertedProductId!=0 && model?.notApi?.isSaved == true)
            {
                holder.parentlinearL.visibility=View.GONE
            }


            schemeList?.forEachIndexed { index, element ->
               if(element.productId==model?.productId)
               {
                   model?.notApi?.schemeId=element.schemeId
                   model?.notApi?.salesQty=element.salesQty
                   holder.scheme_tv.visibility=View.VISIBLE
                   holder.scheme_tv.setText("Scheme: on "+ element.salesQty+" get "+ element.freeQty)
                   model?.notApi?.freeQtyMain=element.freeQty

               }
            }

            holder.qty_et.setTag(position)
            holder.setIsRecyclable(false)

            holder.qty_et.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(5)))

            holder.qty_et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if(!s.toString().isEmpty() && s.toString()!="")
                    {
                        model?.notApi?.rate=model?.price
                        model?.packingTypeName=model?.packingTypeName
                        model?.notApi?.insertedProductId=model?.productId
                        model?.notApi?.salesQtyMain=model?.notApi?.salesQty
                        if( holder.scheme_tv.visibility==View.VISIBLE)
                        {
                            model?.notApi?.scheme=holder.scheme_tv.text.toString()
                            model?.notApi?.schemeId=model?.notApi?.schemeId

                            val getFree=s.toString().toInt()/ model?.notApi?.salesQty!! * model?.notApi?.freeQtyMain!!

                            model?.notApi?.freeQty=getFree
                            val totalQty=getFree+s.toString().toInt()
                            model?.notApi?.totalQty=totalQty
                            model?.notApi?.qty=s.toString().toInt()
                            model?.notApi?.amount=s.toString().toInt()*model.price
                        }
                        else
                        {
                           model?.notApi?.amount=s.toString().toInt()*model?.price!!
                           model?.notApi?.qty=s.toString().toInt()
                           model?.notApi?.totalQty=s.toString().toInt()
                        }



                     /*   if(sendEDetailingArray?.size==0)
                        {
                            val getCurrentObj= Send_EDetailingModel.PobObj.PobDetailList()
                            sendEDetailingArray?.add(getSchemeObject(getCurrentObj,model!!,holder,s))

                        }

                        for((index,getId) in sendEDetailingArray?.withIndex()!!)
                        {
                            if(getId.productId==model?.productId) checkSelected=true
                            if(index==sendEDetailingArray!!.size-1)
                            {
                                if(checkSelected)
                                {
                                    val getCurrentObj: Send_EDetailingModel.PobObj.PobDetailList? = sendEDetailingArray!!.find { it.productId == model?.productId }
                                    val position=sendEDetailingArray?.indexOf(getCurrentObj)
                                    position?.let { sendEDetailingArray?.set(it, getSchemeObject(getCurrentObj,model!!,holder,s)) }
                                }
                                else
                                {
                                    val getCurrentObj= Send_EDetailingModel.PobObj.PobDetailList()
                                    sendEDetailingArray?.add(getSchemeObject(getCurrentObj,model!!,holder,s))
                                }
                            }
                        }*/

                    }
                    else model?.notApi=SyncModel.Data.Product.NotApiData()
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })

            holder.qty_et.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View, hasFocus: Boolean) {
                    if (hasFocus) {
                        v.postDelayed({
                            if (!v.hasFocus()) {
                                v.requestFocus()
                            }
                        }, 300)
                    }
                }
            })
            }


    }

  /*  fun getSchemeObject(
        getCurrentObj: Send_EDetailingModel.PobObj.PobDetailList?,
        model: SyncModel.Data.Product,
        holder: MyViewHolder,
        s: Editable
    ):Send_EDetailingModel.PobObj.PobDetailList
    {
        getCurrentObj?.rate=model.price
        getCurrentObj?.packingTypeName=model.packingTypeName
        getCurrentObj?.productName=model.productName
        getCurrentObj?.productId=model.productId
        getCurrentObj?.salesQtyMain=model.notApi?.salesQty
        if( holder.scheme_tv.visibility==View.VISIBLE)
        {
            getCurrentObj?.scheme=holder.scheme_tv.text.toString()
            getCurrentObj?.schemeId=model.notApi?.schemeId

            val getFree=s.toString().toInt()/ model.notApi?.salesQty!! * model.notApi?.freeQty!!


            getCurrentObj?.freeQty=getFree
            val totalQty=getFree+s.toString().toInt()
            getCurrentObj?.totalQty=totalQty
            getCurrentObj?.qty=s.toString().toInt()
            getCurrentObj?.amount=totalQty*model.price
            getCurrentObj?.freeQtyMain=model.notApi?.freeQty

        }
        else
        {
            getCurrentObj?.amount=s.toString().toInt()*model.price
            getCurrentObj?.qty=s.toString().toInt()
            getCurrentObj?.totalQty=s.toString().toInt()
        }
       return getCurrentObj!!
    }
*/
    override fun getItemCount(): Int {
        return productFilteringList?.size!!
    }


     fun setSelction()
    {
        productFilteringList?.let { sendProductInterface?.onClickButtonProduct(it,1) }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                productFilteringList = results.values as java.util.ArrayList<SyncModel.Data.Product>
                notifyDataSetChanged()
            }
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: java.util.ArrayList<SyncModel.Data.Product> =
                    java.util.ArrayList()
                    constraint = constraint.toString().lowercase()
                    for (i in 0 until productList?.size!!) {
                        val docNames: SyncModel.Data.Product = productList?.get(i)
                        if (docNames.productName?.lowercase()?.contains(constraint.toString()) == true) {
                            FilteredArrayNames.add(docNames)
                        }
                    }
                    results.count = FilteredArrayNames.size
                    results.values = FilteredArrayNames
                    return results
            }
        }
    }

}
