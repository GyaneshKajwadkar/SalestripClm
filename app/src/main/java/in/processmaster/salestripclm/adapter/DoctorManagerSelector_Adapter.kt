
import `in`.processmaster.salestripclm.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class DoctorManagerSelector_Adapter(
    var arrayListSelector: ArrayList<DocManagerModel>,
    var listner: SelectorInterface,
    var selectionType: Int
) : RecyclerView.Adapter<DoctorManagerSelector_Adapter.ViewHolders>(),
    Filterable
{

    var filteredData =arrayListSelector

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorManagerSelector_Adapter.ViewHolders
    {
        var itemView : View
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectorview, parent, false)
        return ViewHolders(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolders, @SuppressLint("RecyclerView") position: Int)
    {
        val modeldata = filteredData?.get(position)
        //set text of layout
        holder.headerDoctor_tv.setText(modeldata?.getName())
        holder.route_tv.setText("Route: " + modeldata?.getRoute())
        holder.speciality_tv.setText("Speciality: " + modeldata?.getSpeciality())
        holder.select_checkBox.isChecked= modeldata.getChecked()!!


        holder.checkParent_ll.setOnClickListener({
            for ((iMain, itemMain) in arrayListSelector.withIndex())
            {
                if(itemMain.getId()== modeldata.getId())
                    {
                        var getChecked=modeldata.getChecked()
                        modeldata.setChecked(!getChecked!!)
                        arrayListSelector.set(iMain,modeldata)
                        holder.select_checkBox.isChecked=!getChecked!!
                        listner.selectorArray(arrayListSelector,selectionType)
                    }
            }
        })

       // holder.select_checkBox.isChecked= modeldata.getChecked()!!

    /*    holder.select_checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean)
            {
                for ((iMain, itemMain) in arrayListSelector.withIndex())
                {
                    for ((iChild, itemChild) in filteredData.withIndex()) {
                        if(itemMain.getId()== itemChild.getId())
                        {
                            Log.e("theNameIs",itemChild.getName().toString())
                            modeldata.setChecked(isChecked)
                            arrayListSelector.set(iMain,modeldata)
                        }
                    }
                }
                modeldata.setChecked(isChecked)
                filteredData.set(position,modeldata)
            }
        }
        )*/
    }

    override fun getItemCount(): Int
    {
        return filteredData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolders(view: View): RecyclerView.ViewHolder(view)
    {
        var headerDoctor_tv: TextView = view.findViewById(R.id.headerDoctor_tv)
        var route_tv: TextView = view.findViewById(R.id.route_tv)
        var speciality_tv: TextView = view.findViewById(R.id.speciality_tv)
        var checkParent_ll: LinearLayout = view.findViewById(R.id.checkParent_ll)
        var select_checkBox: CheckBox = view.findViewById(R.id.select_checkBox)
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            // publis result when text editing complete
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = (results.values as ArrayList<DocManagerModel>?)!!
                notifyDataSetChanged()
            }
            //filter list from parent list = doctor list
            // and add filter data to = filteredData list
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: ArrayList<DocManagerModel> = ArrayList()

                // perform your search here using the searchConstraint String.
                constraint = constraint.toString().toLowerCase()
                for (i in 0 until arrayListSelector?.size!!) {
                    val dataNames: DocManagerModel = arrayListSelector?.get(i)!!
                    if (dataNames.getName()?.toLowerCase()?.startsWith(constraint.toString()) == true) {
                        FilteredArrayNames.add(dataNames)
                    }
                }
                results.count = FilteredArrayNames.size
                results.values = FilteredArrayNames
                return results
            }
        }
    }
}