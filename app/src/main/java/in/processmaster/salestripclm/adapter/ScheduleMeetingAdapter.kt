package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.GetScheduleModel
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import us.zoom.sdk.*
import java.text.SimpleDateFormat

class ScheduleMeetingAdapter(
    var context: Context,
    var viewTypeConst: Int,
    var meetingList: MutableList<GetScheduleModel.Data.Meeting>,
    var zoomSDK: ZoomSDK
) : RecyclerView.Adapter<ScheduleMeetingAdapter.MyViewHolder>(),
    Filterable
{

    var filteredData =meetingList


    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        var startmeeting_btn=view.findViewById<Button>(R.id.startmeeting_btn)
        var subject_tv=view.findViewById<TextView>(R.id.subject_tv)
        var meetingType_tv=view.findViewById<TextView>(R.id.meetingType_tv)
        var appointmentDate_tv=view.findViewById<TextView>(R.id.appointmentDate_tv)
        var doctorsName_tv=view.findViewById<TextView>(R.id.doctorsName_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleMeetingAdapter.MyViewHolder {

        if(viewTypeConst==1)
       {
           val itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.shedulemeeting_viewsmall, parent, false)
           return MyViewHolder(itemView)
       }
        else
       {
           val itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.shedulemeeting_view, parent, false)
           return MyViewHolder(itemView)
       }
    }

    override fun getItemCount(): Int {
      return filteredData!!.size
    }

    override fun onBindViewHolder(holder: ScheduleMeetingAdapter.MyViewHolder, position: Int)
    {

        if(viewTypeConst==1)
        {
            holder.startmeeting_btn.visibility=View.VISIBLE
            holder.startmeeting_btn.setOnClickListener({
                val zoomSDK = ZoomSDK.getInstance()
               // val preMeetingService: PreMeetingService = zoomSDK.getPreMeetingService()
                Log.e("sidfgud",filteredData?.get(position)?.zoomMeetingId?.toString()!!)
                //val item = preMeetingService.getMeetingItemByUniqueId(meetingList?.get(position)?.zoomMeetingId?.toLong()!!)
                onClickBtnStart(filteredData?.get(position)?.zoomMeetingId)
            })
        }

        var doctorName=""
        for((index, doctorList) in filteredData?.get(position)?.doctorList?.withIndex()!!)
        {
            doctorName=doctorName+doctorList.name + if(index== filteredData?.get(position)?.doctorList?.size!! -1)"" else ","
        }

        holder.doctorsName_tv.setText(doctorName)
        holder.doctorsName_tv.setSelected(true)
        holder.subject_tv.setText(filteredData?.get(position)?.topic)
        holder.subject_tv.setSelected(true)
        holder.meetingType_tv.setText("Meeting type: " +if(filteredData?.get(position)?.meetingType.equals("P")) "Physical" else "Online" )
        holder.appointmentDate_tv.setText(filteredData?.get(position)?.strStartTime?.trim())
        holder.appointmentDate_tv.setSelected(true)

    }
    fun onClickBtnStart(item: String?) {

        if (zoomSDK.isInitialized) {
            val meetingService = zoomSDK.meetingService
            if (meetingService != null) {
                val params = StartMeetingParams()
                params.meetingNo = item.toString() + ""
                meetingService.startMeetingWithParams(
                    context,
                    params,
                    ZoomMeetingUISettingHelper.getStartMeetingOptions()
                )
            }
        }
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = (results.values as ArrayList<GetScheduleModel.Data.Meeting>?)!!
                notifyDataSetChanged()
            }
            override fun performFiltering(constraint: CharSequence): FilterResults? {
                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: ArrayList<GetScheduleModel.Data.Meeting> = ArrayList()

                constraint = constraint.toString().lowercase()
                for (i in 0 until meetingList?.size!!) {
                    val dataNames: GetScheduleModel.Data.Meeting = meetingList?.get(i)!!
                    var apiFormat = SimpleDateFormat("MMMM dd, yyyy")
                    val newDate = apiFormat.parse(dataNames.strStartTime)
                    var spf = SimpleDateFormat("dd/MM/yyyy")
                    var fetchDate = spf.format(newDate)
                    if (fetchDate?.lowercase()?.startsWith(constraint.toString()) == true)
                    {
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