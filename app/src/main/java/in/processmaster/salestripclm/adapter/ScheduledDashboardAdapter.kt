package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.GetScheduleModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScheduledDashboardAdapter(val meetingArrayList: ArrayList<GetScheduleModel.Data.Meeting>) : RecyclerView.Adapter<ScheduledDashboardAdapter.ScheduleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.childexpandableheader, parent, false)
        return ScheduleViewHolder(v)
    }


    override fun getItemCount(): Int {
        return meetingArrayList.size
    }

    class ScheduleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
    {
        fun ScheduleViewHolder(itemView: View?)
        {

        }
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
    }
}