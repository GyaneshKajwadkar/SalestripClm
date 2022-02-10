package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextWithEditAdapter(
    var mainList: ArrayList<IdNameBoll_model>,
    var callback: IdNameBoll_interface,
    var showEdit: Int
) : RecyclerView.Adapter<TextWithEditAdapter.MyViewHolder>() {

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
        var quantity_et=view.findViewById<EditText>(R.id.quantity_et)
    }


    override fun onBindViewHolder(holder:MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.brandName.setText(mainList.get(position).name)
        if(showEdit==0)
            holder.quantity_et.visibility=View.GONE

        holder.quantity_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               if(s.toString()!="")
                mainList.get(position).qty=s.toString().toInt()
                callback.onChangeArray(mainList,false)
            }
        })

    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}