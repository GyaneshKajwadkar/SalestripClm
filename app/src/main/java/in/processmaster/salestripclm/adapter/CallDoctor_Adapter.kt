package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.fragments.EdetailingFragment
import `in`.processmaster.salestripclm.models.SyncModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CallDoctor_Adapter(
    val doctorList: ArrayList<SyncModel.Data.Doctor>,
    val activity: Context?
) : RecyclerView.Adapter<CallDoctor_Adapter.ViewHolders>(), Filterable
{
    var filteredData: ArrayList<SyncModel.Data.Doctor>? = doctorList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.calldoctors_adapter, parent, false)
        return ViewHolders(itemView);
    }

    override fun onBindViewHolder(holder:ViewHolders, position: Int) {
        val modeldata = filteredData?.get(position)
        holder.doctorName_tv.setText(modeldata?.doctorName)
        holder.doctorPlace_tv.setText("Route: " + modeldata?.routeName)
        holder.speciality_tv.setText("Speciality: " + modeldata?.specialityName)

        holder.doctorDetail_cv.setOnClickListener({
         // val fragment = DoctorDetailFragment()
         // val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
         // transaction.replace(R.id.container, fragment,"detailDoctor")
         // transaction.addToBackStack(null)
         // transaction.commit()
        })
    }

    override fun getItemCount(): Int {
        return filteredData!!.size
    }

    class ViewHolders(view: View): RecyclerView.ViewHolder(view){
    var doctorName_tv=view.findViewById<TextView>(R.id.doctorName_tv)
    var doctorPlace_tv=view.findViewById<TextView>(R.id.doctorPlace_tv)
    var speciality_tv=view.findViewById<TextView>(R.id.speciality_tv)
    var doctorDetail_cv=view.findViewById<CardView>(R.id.doctorDetail_cv)
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = results.values as ArrayList<SyncModel.Data.Doctor>?
                notifyDataSetChanged()
            }
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()
                constraint = constraint.toString().lowercase()

                for (i in 0 until doctorList?.size!!) {
                    val dataNames: SyncModel.Data.Doctor = doctorList?.get(i)!!
                    if (dataNames.doctorName.lowercase().startsWith(constraint.toString())) {
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