package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.common_classes.CheckDcrClass
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CallDoctor_Adapter(
    val doctorList: List<DailyDocVisitModel.Data.DcrDoctor>?,
    val requireActivity: Activity?,
    val type: String?
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

        if(modeldata?.saveInDb==true)
        {
            holder.needsync.visibility=View.VISIBLE

            holder.editDoctorCall_mb.visibility=View.GONE

            if(modeldata.callTiming.equals("E"))  holder.callTime_tv.setText("Evening")
                else   holder.callTime_tv.setText( "Morning")

            holder.speciality_tv.setText( "-")
            holder.workWithName_tv.setText("-")
            holder.visitPurpose_tv.setText("-")
            try {
                val sdf =   SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                val dateFormatterSet = SimpleDateFormat("dd/MM/yyyy hh:mm a")
                val startDate: Date = sdf.parse(modeldata?.reportedTime)
                holder.reportedTime_tv.setText( dateFormatterSet.format(startDate))
            }
            catch (e: Exception)
            { }
        }
        else {
            holder.needsync.visibility = View.GONE
            holder.editDoctorCall_mb.visibility = View.VISIBLE

            holder.callTime_tv.setText( modeldata?.callTimingName)
            holder.speciality_tv.setText( modeldata?.specialityName)
            holder.reportedTime_tv.setText( modeldata?.strReportedTime)
            holder.workWithName_tv.setText(if(modeldata?.workWithName?.isEmpty()==false) modeldata?.workWithName else "-")
            holder.visitPurpose_tv.setText(if(modeldata?.visitPurposeName?.isEmpty()==false) modeldata?.visitPurposeName else "-" )

        }
        if(type.equals("ret")){
            holder.doctorName_tv.setText(modeldata?.shopName)
            holder.parentSpeciality.visibility=View.GONE
        }
        else {
            holder.speciality_tv.setText(modeldata?.specialityName)
        }


        if(modeldata?.dataSaveType?.lowercase().equals("s"))
        {
            holder.editDoctorCall_mb.setText("View")
        }

        holder.editDoctorCall_mb.setOnClickListener({
            if(type.equals("doc"))
            {
                if(CheckDcrClass(requireActivity,"doctorAdapter",Gson().toJson(modeldata)).checkDCR_UsingSP())
                {
                    val intent = Intent(requireActivity, SubmitE_DetailingActivity::class.java)
                    intent.putExtra("apiDataDcr", Gson().toJson(modeldata))
                    requireActivity?.startActivity(intent)
                }
            }
            else{
                requireActivity as HomePage
                requireActivity.selectRetailerForEdit(Gson().toJson(doctorList?.get(position)))
            }

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
    var editDoctorCall_mb=view.findViewById<TextView>(R.id.editDoctorCall_mb)
    var callTime_tv=view.findViewById<TextView>(R.id.callTime_tv)
    var specilityHeader=view.findViewById<TextView>(R.id.specilityHeader)
    var needsync=view.findViewById<TextView>(R.id.needsync)
    var parentSpeciality=view.findViewById<LinearLayout>(R.id.parentSpeciality)
    }


}