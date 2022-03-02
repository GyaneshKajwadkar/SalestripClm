package `in`.processmaster.salestripclm.adapter
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SetSchedule_Activity
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.AlertClass.Companion.retunDialog
import `in`.processmaster.salestripclm.models.GetScheduleModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import us.zoom.sdk.*
import java.io.Serializable
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
        var buttonParent_ll=view.findViewById<LinearLayout>(R.id.buttonParent_ll)
        var cancelMeeting_btn=view.findViewById<Button>(R.id.cancelMeeting_btn)
        var updateMeeting_btn=view.findViewById<Button>(R.id.updateMeeting_btn)
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

    override fun onBindViewHolder(holder: ScheduleMeetingAdapter.MyViewHolder, @SuppressLint("RecyclerView") position: Int)
    {

        if(viewTypeConst==1)
        {
            holder.buttonParent_ll.visibility=View.VISIBLE

            holder.cancelMeeting_btn.setOnClickListener({

                val r: Runnable = object : Runnable {
                    override fun run() {
                        if(retunDialog)
                            initilizeDelete(position)
                    }
                }
                AlertClass(context).twoButtonAlert("cancel dialog","cancel meeting",
                    1,"cancel meeting","Cancel Meeting?",r)

            })

            holder.updateMeeting_btn.setOnClickListener({
                val r: Runnable = object : Runnable {
                    override fun run() {
                        if(retunDialog)
                        {
                            val args = Bundle()
                            args.putSerializable("ARRAYOBJECT", filteredData.get(position) as Serializable?)
                            val intent = Intent(context, SetSchedule_Activity::class.java)
                            intent.putExtra("fillData", args)
                            context.startActivity(intent)
                        }
                    }
                }
                AlertClass(context).twoButtonAlert("No","Yes",
                    1,"update meeting","Update Meeting?",r)
            })


            holder.startmeeting_btn.setOnClickListener({

//                val uri: Uri =
//                    Uri.parse(filteredData?.get(position).meetingLink) // missing 'http://' will cause crashed
//
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                context.startActivity(intent)
//                return@setOnClickListener



                val zoomSDK = ZoomSDK.getInstance()
                // val preMeetingService: PreMeetingService = zoomSDK.getPreMeetingService()
                //val item = preMeetingService.getMeetingItemByUniqueId(meetingList?.get(position)?.zoomMeetingId?.toLong()!!)
                val c: Date = Calendar.getInstance().getTime()
                val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                var apiFormat = SimpleDateFormat("MMMM dd, yyyy")
                var apiFormatTime = SimpleDateFormat("MMMM dd, yyyy hh:mm a")
                val newDate = apiFormat.parse(filteredData?.get(position)?.strStartTime)
                val apiTime = apiFormatTime.parse(filteredData?.get(position)?.strStartTime)
                var fetchDate = df.format(newDate)
                val formattedDate = df.format(c)
                if(fetchDate.equals(formattedDate))
                {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.HOUR, 1)
                    val oneHourBack = cal.time

                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(oneHourBack)
                    val apiTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(apiTime)
                    Log.e("sdnuiofsdguf",currentTime.toString())
                    Log.e("sdnuiapiTime",apiTime.toString())
                    val currentTimeInt : Int =currentTime.replace(":","").toInt()
                    val apiTimeInt : Int =apiTime.replace(":","").toInt()
                  //  launchZoomUrl(filteredData?.get(position))
                    onClickBtnStart(filteredData?.get(position)?.zoomMeetingId)
                    return@setOnClickListener
                    if(apiTimeInt<=currentTimeInt)
                    {
                        onClickBtnStart(filteredData?.get(position)?.zoomMeetingId)
                    }
                    else
                    {
                        AlertClass(context).commonAlert("Meeting Alert","Meeting not start before the scheduled time")
                    }

                }
                else{
                    AlertClass(context).commonAlert("Meeting Alert","Meeting not start before the scheduled time")
                }

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

    private fun launchZoomUrl(get: GetScheduleModel.Data.Meeting) {
        val str="zoomus://zoom.us/start?browser=chrome&confno="+get.zoomMeetingId+"&zc=0&stype=100&uid=7JxmLMQRRYyEHrJTsduDYQ&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxMG9iRzFqU1JUYTI3R29DSG8tVlp3IiwiZXhwIjoxODAwMDAwMDAwMH0.KHz1LEHUa6EP3i0ClDlN10G2Ew_1r9OknbBnrCaAcMI&uname=kajwadkar13@gmail.com"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
        else{
            Toast.makeText(context,"zoom not installed",Toast.LENGTH_SHORT).show()
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

    fun initilizeDelete(position: Int)
    {
        AlertClass(context).showProgressAlert("Deleting data...")
        val coroutine=CoroutineScope(Dispatchers.IO).launch {
            val deleteSchedule= async {
                setSheduleApi(filteredData?.get(position))
            }
            val deleteComplete= deleteSchedule.await()
            if(deleteComplete!=null)
            {
                val getAllSchedule= async{
                    getSheduleMeetingAPI(position)
                }
                getAllSchedule.await()
            }
        }
        coroutine.invokeOnCompletion {
            AlertClass(context).hideAlert()
        }

    }

    suspend fun setSheduleApi(data: GetScheduleModel.Data.Meeting)
    {
        val paramObject = JSONObject()
        paramObject.put("MeetingId", data.meetingId)
        paramObject.put("MeetingDate",data.meetingDate )
        paramObject.put("topic",data.topic)
        paramObject.put("Start_Time",data.startTime?.replace("Z", ""))
        paramObject.put("End_Time", data.endTime?.replace("Z", ""))
        paramObject.put("MeetingType", data.meetingType)
        paramObject.put("EmpId", data.empId)
        paramObject.put("Mode", "3")
        paramObject.put("isCancel", true)

        val doctorList = JSONArray()

        paramObject.put("DoctorList", doctorList)
        val teamList = JSONArray()

        paramObject.put("EmployeeList", teamList)

        val bodyRequest: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), paramObject.toString())


        var profileData =PreferenceClass(context)?.getPref("profileData")

        val response = APIClientKot().getUsersService(2, PreferenceClass(context)?.getPref("secondaryUrl")!!
        ).setScheduleMeetingApi("bearer " + Gson().fromJson(profileData, LoginModel::class.java)?.accessToken,bodyRequest)
        withContext(Dispatchers.Main) {
            if (response!!.isSuccessful)
            {
                Log.e("meetingAPiIS",response.body().toString())
            }
            else
            {
                Log.e("responseERROR", response.errorBody().toString())
            }
        }
    }

    suspend fun getSheduleMeetingAPI(position: Int)
    {
        var profileData =PreferenceClass(context)?.getPref("profileData")
        val profile=Gson().fromJson(profileData, LoginModel::class.java)

        val response = APIClientKot().getUsersService(2, PreferenceClass(context)?.getPref("secondaryUrl")!!
        ).getScheduledMeetingCoo("bearer " + profile?.accessToken,profile.empId.toString())
        withContext(Dispatchers.Main) {
            Log.e("getScheduleAPI",response.toString())
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val gson = Gson()
                    var model = response.body()
                    Log.e("abcdef",model.toString())

                    DatabaseHandler(context)?.addAPIData(gson.toJson(model),2)


                    filteredData.removeAt(position)

                    notifyDataSetChanged()
                }
                else
                {
                    Log.e("elseGetScheduled", response.code().toString())
                }
            }
            else
            {   Log.e("scheduleERROR", response.errorBody().toString())
            }
        }

    }

}