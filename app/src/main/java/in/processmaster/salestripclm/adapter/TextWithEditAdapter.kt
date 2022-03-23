package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TextWithEditAdapter(
    var mainList: ArrayList<IdNameBoll_model>?,
    var callback: IdNameBoll_interface?,
    var showEdit: Int?,
    val context: SubmitE_DetailingActivity?,
    var selectionType: Int,
) : RecyclerView.Adapter<TextWithEditAdapter.MyViewHolder>(), Filterable {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_edit_view, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var brandName=view.findViewById<TextView>(R.id.brandName)
        var availableQtyTv=view.findViewById<TextView>(R.id.availableQtyTv)
        var quantity_et=view.findViewById<EditText>(R.id.quantity_et)
    }

    override fun onBindViewHolder(holder:MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val model=mainList?.get(position)

        holder.brandName.setText(model?.name)

        if(showEdit==0) {
            holder.quantity_et.visibility = View.GONE
            holder.availableQtyTv.visibility = View.GONE
        }
        if(model?.qty!=-1) holder.quantity_et.setText(model?.qty.toString())

        holder.availableQtyTv.setText(model?.availableQty.toString() +" QTY")

        holder.quantity_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text=holder.quantity_et.text.toString()
                if(text!="") {
                    model?.qty = text.toInt()
                    mainList?.let { callback?.onChangeArray(it, false,selectionType) }
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text=holder.quantity_et.text.toString()
                if(text!="") {
                    if(text.toInt()<= mainList?.get(position)?.availableQty!!)
                    { }
                    else{
                        val length: Int = text.length
                        holder.quantity_et.getText().delete(length - 1, length)
                        Toast.makeText(context,"Quantity is more than available",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return mainList?.size!!
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                mainList = results.values as java.util.ArrayList<IdNameBoll_model>?
                notifyDataSetChanged()
            }
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: java.util.ArrayList<IdNameBoll_model> =
                    java.util.ArrayList()
                constraint = constraint.toString().lowercase()
                for (i in 0 until mainList?.size!!) {
                    val docNames: IdNameBoll_model = mainList?.get(i)!!
                    if (docNames.name?.lowercase()?.startsWith(constraint.toString()) == true) {
                        FilteredArrayNames.add(docNames)
                    }
                }
                results.count = FilteredArrayNames.size
                results.values = FilteredArrayNames
                return results
            }
        }
    }

    fun filter(text: String) {

        for (s in mainList!!) {
            if (s.name.lowercase().contains(text.lowercase())) {
                mainList?.add(s)
            }
        }
        notifyDataSetChanged()
    }


}