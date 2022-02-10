package `in`.processmaster.salestripclm.adapter
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

class CheckboxSpinnerAdapter(var adapterList:ArrayList<IdNameBoll_model>, var callback:IdNameBoll_interface): RecyclerView.Adapter<CheckboxSpinnerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkbox_spinnerview, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkBox=view.findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onBindViewHolder(holder:MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.checkBox.setText(adapterList.get(position).name)
        holder.checkBox.isChecked=adapterList.get(position).isChecked

        holder.checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                Log.e("uisCheckjdhs",p1.toString())
                adapterList.get(position).isChecked=p1
                callback.onChangeArray(adapterList,true)
            }}
        )
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun getItemViewType(position: Int): Int = position

}