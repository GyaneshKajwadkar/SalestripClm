package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.SyncModel
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.util.ArrayList

class CallDoctor_Adapter(
    val doctorList: List<DailyDocVisitModel.Data.DcrDoctor>?,
    val requireActivity: Activity
) : RecyclerView.Adapter<CallDoctor_Adapter.ViewHolders>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.calldoctors_adapter, parent, false)
        return ViewHolders(itemView);
    }

    override fun onBindViewHolder(holder:ViewHolders, position: Int) {
        val modeldata = doctorList?.get(position)
        holder.doctorName_tv.setText(modeldata?.doctorName)
        holder.doctorPlace_tv.setText( modeldata?.routeName)
        holder.speciality_tv.setText( modeldata?.specialityName)
        holder.reportedTime_tv.setText( modeldata?.strReportedTime)
        holder.workWithName_tv.setText(if(modeldata?.workWithName?.isEmpty()==false) modeldata?.workWithName else "-")
        holder.visitPurpose_tv.setText(if(modeldata?.visitPurposeName?.isEmpty()==false) modeldata?.visitPurposeName else "-" )
        holder.meetingAt_tv.setText( modeldata?.callMediumTypeName)
        holder.callTime_tv.setText( modeldata?.callTimingName)

        if(modeldata?.dataSaveType?.lowercase().equals("s"))
        {
            holder.editDoctorCall_mb.setText("View")
        }

        holder.editDoctorCall_mb.setOnClickListener({

            val intent = Intent(requireActivity, SubmitE_DetailingActivity::class.java)
            intent.putExtra("apiDataDcr", Gson().toJson(modeldata))
            requireActivity.startActivity(intent)
        })

    }

    override fun getItemCount(): Int {
        return doctorList!!.size
    }

    class ViewHolders(view: View): RecyclerView.ViewHolder(view){
    var doctorName_tv=view.findViewById<TextView>(R.id.doctorName_tv)
    var doctorPlace_tv=view.findViewById<TextView>(R.id.doctorPlace_tv)
    var speciality_tv=view.findViewById<TextView>(R.id.speciality_tv)
    var reportedTime_tv=view.findViewById<TextView>(R.id.reportedTime_tv)
    var workWithName_tv=view.findViewById<TextView>(R.id.workWithName_tv)
    var visitPurpose_tv=view.findViewById<TextView>(R.id.visitPurpose_tv)
    var meetingAt_tv=view.findViewById<TextView>(R.id.meetingAt_tv)
    var editDoctorCall_mb=view.findViewById<TextView>(R.id.editDoctorCall_mb)
    var callTime_tv=view.findViewById<TextView>(R.id.callTime_tv)
    }


}