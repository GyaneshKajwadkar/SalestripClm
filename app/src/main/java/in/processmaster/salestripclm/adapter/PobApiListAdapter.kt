package `in`.processmaster.salestripclm.adapter

import IntegerInterface
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.ButtonFilterAdapter
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.interfaceCode.StringInterface
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.CreatePOBModel
import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PobApiListAdapter(val pobList: List<CreatePOBModel.Data.pobObject>?, val intergerInterface :IntegerInterface?,
val activity: Activity?
) : RecyclerView.Adapter<PobApiListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PobApiListAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pob_api_list_view, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name_tv = view.findViewById<TextView>(R.id.name_tv)
        var pobId_tv = view.findViewById<TextView>(R.id.pobId_tv)
        var date_tv = view.findViewById<TextView>(R.id.date_tv)
        var orderValue_tv = view.findViewById<TextView>(R.id.orderValue_tv)
        var view_iv = view.findViewById<ImageView>(R.id.view_iv)
        var edit_iv = view.findViewById<ImageView>(R.id.edit_iv)
        var pobSelection_mb = view.findViewById<MaterialButton>(R.id.pobSelection_mb)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val modelClass= pobList?.get(position)
        holder.name_tv.setText(modelClass?.partyName)
        modelClass?.pobNo?.let { holder.pobId_tv.setText(it) }
        holder.orderValue_tv.setText("Order value: "+ modelClass?.totalPOB.toString())

        if(modelClass?.pobType.equals("DOCTOR")) holder.pobSelection_mb.setBackgroundTintList(
            activity?.let { ContextCompat.getColorStateList(it, R.color.orange) })
        else  holder.pobSelection_mb.setBackgroundTintList(activity?.let {
            ContextCompat.getColorStateList(
                it, R.color.appColor)
        })

        holder.pobSelection_mb.setText(modelClass?.pobType)

        holder.view_iv.setOnClickListener {
            modelClass?.pobId?.let { it1 -> intergerInterface?.passid(it1,1) } //selection type for view=1 and selectrion type for edit=2
        }

        holder.edit_iv.setOnClickListener {
            modelClass?.pobId?.let { it1 -> intergerInterface?.passid(it1,2) } //selection type for view=1 and selectrion type for edit=2
        }

        val filterDate= activity?.let { GeneralClass(it).convertApiDateTime_toDate(modelClass?.pobDate) }
        if(filterDate?.isEmpty()==false) holder.date_tv.setText(filterDate)

    }

    override fun getItemCount(): Int {
        return pobList?.size!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
