package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.GetScheduleModel
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import us.zoom.sdk.ZoomSDK
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MeetingExpandableHeaderAdapter(
    var context: Context,
    var arrayListString: ArrayList<String>,
    var getScheduleModel: GetScheduleModel
) : RecyclerView.Adapter<MeetingExpandableHeaderAdapter.ViewHolders>() {

    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.feed_heading, parent, false)
        return ViewHolders(v)
    }

    override fun onBindViewHolder(holder: ViewHolders, @SuppressLint("RecyclerView") position: Int) {

        holder.headingTxt.setText(arrayListString.get(position))

        if (currentPosition == position)
        {
            //creating an animation
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)

            //toggling visibility
            holder.showmeeting_rv.visibility = View.VISIBLE

            //adding sliding effect
            holder.showmeeting_rv.startAnimation(slideDown)
            holder.arrowImage.animate().rotation(180f).start()
        }
        else
        {
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slideup)

            //adding sliding effect
            holder.showmeeting_rv.startAnimation(slideDown)
            holder.arrowImage.animate().rotation(0f).start()

            //toggling visibility
            holder.showmeeting_rv.visibility = View.GONE
        }
        holder.toggleView.setOnClickListener {
            if (currentPosition == position) {
                currentPosition = -1
            } else {
                currentPosition = position
            }

            //reloding the list
            notifyDataSetChanged()
        }

        var meetingArrayList=ArrayList<GetScheduleModel.Data.Meeting>()

        if(getScheduleModel!=null)
        {
            for(meeting in getScheduleModel?.getData()?.meetingList!!)
            {
                var apiFormat = SimpleDateFormat("MMMM dd, yyyy")
                val newDate = apiFormat.parse(meeting.strStartTime)
                var spf = SimpleDateFormat("dd/MM/yyyy")
                var fetchDate = spf.format(newDate)

                when(position)
                {
                    0 ->
                    {
                        var currentdate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
                        if(currentdate.equals(fetchDate))
                        {
                            meetingArrayList.add(meeting)
                        }

                    }
                    1 ->
                    {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                        val tomorrow = calendar.time
                        val tomorrowAsString = spf.format(tomorrow)
                        if(tomorrowAsString.equals(fetchDate))
                        {
                            meetingArrayList.add(meeting)
                        }
                    }

                    else ->
                    {
                        val c = Calendar.getInstance()
                        c.firstDayOfWeek = Calendar.MONDAY
                        c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                        c[Calendar.HOUR_OF_DAY] = 0
                        c[Calendar.MINUTE] = 0
                        c[Calendar.SECOND] = 0
                        c[Calendar.MILLISECOND] = 0
                        val monday = c.time
                        val nextMonday = Date(monday.time + 7 * 24 * 60 * 60 * 1000)

                        var dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
                        val startDate = dateFormat.parse(monday.toString())
                        val lastDate =  dateFormat.parse(nextMonday.toString())
                        val startingDate = spf.format(startDate)
                        val endDate =  spf.format(lastDate)
                        val finalStartingDate=spf.parse(startingDate)
                        val finalEndDate=spf.parse(endDate)
                        val apiDate= spf.parse(fetchDate)

                        val isThisWeek = apiDate.after(finalStartingDate) && apiDate.before(finalEndDate)
                        if(isThisWeek && position==2)
                        {
                            meetingArrayList.add(meeting)
                        }
                        else if(!isThisWeek && position==3)
                        {
                            meetingArrayList.add(meeting)
                        }
                    }
                }
            }
        }

        if(meetingArrayList.size==0)
        {
            holder.noDataFound_tv.visibility=View.VISIBLE
        }
        else
        {
            holder.showmeeting_rv.layoutManager = LinearLayoutManager(context)
            val zoomSdk= ZoomSDK.getInstance()

            val adapter = ScheduleMeetingAdapter(context,3,meetingArrayList,zoomSdk)
            holder.showmeeting_rv.adapter = adapter
        }
    }

    override fun getItemCount(): Int {
        return arrayListString.size
    }

    class ViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var toggleView: LinearLayout
        var showmeeting_rv: RecyclerView
        var arrowImage: ImageView
        var headingTxt: TextView
        var noDataFound_tv: TextView

        init {
            toggleView = itemView.findViewById(R.id.toggleView)
            showmeeting_rv = itemView.findViewById(R.id.showmeeting_rv)
            arrowImage = itemView.findViewById(R.id.toggleIcon)
            headingTxt = itemView.findViewById(R.id.headingTxt)
            noDataFound_tv = itemView.findViewById(R.id.noDataFound_tv)
        }
    }

}
