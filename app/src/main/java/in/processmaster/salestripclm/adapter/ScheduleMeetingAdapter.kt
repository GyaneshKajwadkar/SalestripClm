package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ScheduleMeetingAdapter(var viewType: Int) : RecyclerView.Adapter<ScheduleMeetingAdapter.MyViewHolder>()
{
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        var startmeeting_btn=view.findViewById<Button>(R.id.startmeeting_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleMeetingAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.shedulemeeting_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
      return 6
    }

    override fun onBindViewHolder(holder: ScheduleMeetingAdapter.MyViewHolder, position: Int)
    {
        if(viewType==1)
        {
            holder.startmeeting_btn.visibility=View.VISIBLE
        }
    }

}