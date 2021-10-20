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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import us.zoom.sdk.*

class ScheduleMeetingAdapter(var context: Context,var viewTypeConst: Int,var meetingList: List<GetScheduleModel.Data.Meeting>?) : RecyclerView.Adapter<ScheduleMeetingAdapter.MyViewHolder>()
{
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
      return meetingList!!.size
    }

    override fun onBindViewHolder(holder: ScheduleMeetingAdapter.MyViewHolder, position: Int)
    {

        if(viewTypeConst==1)
        {
            holder.startmeeting_btn.visibility=View.VISIBLE
            holder.startmeeting_btn.setOnClickListener({
                val zoomSDK = ZoomSDK.getInstance()
                val preMeetingService: PreMeetingService = zoomSDK.getPreMeetingService()
                Log.e("sidfgud",meetingList?.get(position)?.zoomMeetingId?.toString()!!)
                //val item = preMeetingService.getMeetingItemByUniqueId(meetingList?.get(position)?.zoomMeetingId?.toLong()!!)
                onClickBtnStart(meetingList?.get(position)?.zoomMeetingId)
            })
        }

        var doctorName=""
        for((index, doctorList) in meetingList?.get(position)?.doctorList?.withIndex()!!)
        {
            doctorName=doctorName+doctorList.name + if(index== meetingList?.get(position)?.doctorList?.size!! -1)"" else ","
        }

        holder.doctorsName_tv.setText(doctorName)
        holder.doctorsName_tv.setSelected(true)
        holder.subject_tv.setText(meetingList?.get(position)?.topic)
        holder.subject_tv.setSelected(true)
        holder.meetingType_tv.setText("Meeting type: " +if(meetingList?.get(position)?.meetingType.equals("P")) "Physical" else "Online" )
        holder.appointmentDate_tv.setText(meetingList?.get(position)?.strStartTime?.trim())
        holder.appointmentDate_tv.setSelected(true)

    }
    fun onClickBtnStart(item: String?) {
        val zoomSDK = ZoomSDK.getInstance()
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
}