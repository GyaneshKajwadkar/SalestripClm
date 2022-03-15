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
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

class CheckboxSpinnerAdapter(val adapterList:ArrayList<IdNameBoll_model>?, var callback:IdNameBoll_interface?): RecyclerView.Adapter<CheckboxSpinnerAdapter.MyViewHolder>(),
    Filterable {

    var textChangeFiltringList= ArrayList(adapterList)

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
        val model=textChangeFiltringList?.get(position)
        holder.checkBox.setText(model?.name)
        holder.checkBox.isChecked= model?.isChecked == true

        if(model?.city!="")  holder.checkBox.buttonDrawable=null

        holder.checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                textChangeFiltringList?.get(position)?.isChecked=p1
                val index=adapterList?.indexOf(model)
                index?.let { adapterList?.set(it,model) }
                adapterList?.let { callback?.onChangeArray(it,true,0) }
            }}
        )
    }

    override fun getItemCount(): Int {
        return textChangeFiltringList?.size!!
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                textChangeFiltringList = (results.values as java.util.ArrayList<IdNameBoll_model>?)!!
                notifyDataSetChanged()
            }
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: java.util.ArrayList<IdNameBoll_model> =
                    java.util.ArrayList()
                constraint = constraint.toString().lowercase()

                for (i in 0 until adapterList?.size!!) {
                    val docNames: IdNameBoll_model = adapterList?.get(i)!!
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



}