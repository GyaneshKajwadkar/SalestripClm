package `in`.processmaster.salestripclm.activity
import DocManagerModel
import DoctorManagerSelector_Adapter
import IntegerInterface
import SelectedDocManList_adapter
import SelectorInterface
import `in`.processmaster.salestripclm.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.ScheduleMeetingAdapter
import `in`.processmaster.salestripclm.fragments.ScheduleNewMetting_frag
import `in`.processmaster.salestripclm.fragments.ScheduledMetting_frag
import `in`.processmaster.salestripclm.inbuild_mail.GmailSender
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.sdksampleapp.startjoinmeeting.UserLoginCallback
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_set_schedule_.*
import kotlinx.android.synthetic.main.progress_view.*
import us.zoom.sdk.*
import us.zoom.sdk.PreMeetingService.ScheduleOrEditMeetingError
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.GridLayoutManager
import okhttp3.MediaType
import org.json.JSONArray
import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import okhttp3.RequestBody





class SetSchedule_Activity : BaseActivity() ,SelectorInterface,IntegerInterface, PreMeetingServiceListener,/*TabLayout.OnTabSelectedListener,*/ UserLoginCallback.ZoomDemoAuthenticationListener

{
    val myCalendar = Calendar.getInstance()
    var dialogCalendar: DatePickerDialog?=null
    private var mPreMeetingService: PreMeetingService? = null
    private var mAccoutnService: AccountService? = null
    var mCountry: MobileRTCDialinCountry? = null
    var connectivityChangeReceiver= ConnectivityChangeReceiver()
    var arrayListSelectorDoctor: ArrayList<DocManagerModel> = ArrayList()
    var arrayListSelectorTeams: ArrayList<DocManagerModel> = ArrayList()
    var selectedAdapeter :SelectedDocManList_adapter? = null
    var selectedAdapeterTeams :SelectedDocManList_adapter? = null
    var constructorList: ArrayList<DocManagerModel> = ArrayList()
    var constructorListTeam: ArrayList<DocManagerModel> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_schedule_)

        getTeamsApi()

        Handler(Looper.getMainLooper()).postDelayed({
            init()
        }, 10)

    //    UserLoginCallback.getInstance().addListener(this)
    //    loginFirst()

    }

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    fun init()
    {
     //   selected.text = getDateString(eventsCalendar.getCurrentSelectedDate()?.timeInMillis)

        if (ZoomSDK.getInstance().isInitialized && ZoomSDK.getInstance().isLoggedIn) {
            mAccoutnService = ZoomSDK.getInstance().getAccountService()
            mPreMeetingService = ZoomSDK.getInstance().preMeetingService
            mCountry = ZoomSDK.getInstance().accountService.availableDialInCountry
            if (mAccoutnService == null || mPreMeetingService == null) {
                finish()
            }
        }

        selectedoctor_rv.setLayoutManager(GridLayoutManager(this, 5))
        recyclerView_teams.setLayoutManager(GridLayoutManager(this, 5))

      //  selectedoctor_rv.setNestedScrollingEnabled(false);

     //   val selectedAdapeter=SelectedDocManList_adapter(arrayListSelector,this)
     //   selectedoctor_rv.adapter=selectedAdapeter




        //   val today = Calendar.getInstance()
     //   val end = Calendar.getInstance()
     //   end.add(Calendar.YEAR, 2)

     /*   var typeface: Typeface? = ResourcesCompat.getFont(this, R.font.opensans_regular)
        var typeface2: Typeface? = ResourcesCompat.getFont(this, R.font.opensans_semibold)

        eventsCalendar.setSelectionMode(eventsCalendar.MULTIPLE_SELECTION)
                .setToday(today)
                .setMonthRange(today, end)
                .setWeekStartDay(Calendar.SUNDAY, false)
                .setIsBoldTextOnSelectionEnabled(true)
                .setDatesTypeface(typeface!!)
                .setMonthTitleTypeface(typeface2!!)
                .setWeekHeaderTypeface(typeface2!!)
                .setCallback(this)

                .build()

        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 2)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 3)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 4)
        eventsCalendar.addEvent(c)
        c.add(Calendar.DAY_OF_MONTH, 7)
        eventsCalendar.addEvent(c)
        c.add(Calendar.MONTH, 1)
        c[Calendar.DAY_OF_MONTH] = 1
        eventsCalendar.addEvent(c)

        selected.setOnClickListener {
            val dates = eventsCalendar.getDatesFromSelectedRange()
            Log.e("SELECTED SIZE", dates.size.toString())
        }
*/
        //  selected.typeface = FontsManager.getTypeface(FontsManager.OPENSANS_SEMIBOLD, this)

     //   val dc = Calendar.getInstance()
     //   dc.add(Calendar.DAY_OF_MONTH, 2)


        val newMetting= ScheduleNewMetting_frag()
        val scheduledMeeting = ScheduledMetting_frag()

        val adapter = Adapter(supportFragmentManager)
        adapter.addFragment(newMetting, "Schedule Meeting")
        adapter.addFragment(scheduledMeeting, "Scheduled Meetings")

        viewpager_schedule.setAdapter(adapter)
        result_tabs_schedule!!.setupWithViewPager(viewpager_schedule)

        scheduleBack_iv.setOnClickListener({
            onBackPressed()
        })


        var adapterRecycler= ScheduleMeetingAdapter(0)
        recyclerNewSchedule!!.layoutManager = LinearLayoutManager(this)
      //  recyclerNewSchedule?.itemAnimator = DefaultItemAnimator()
        recyclerNewSchedule?.adapter = adapterRecycler



        var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {


            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateCalendar()
            }
        }


        selectDate_tv.setOnClickListener({
            selectDateHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))

            HideKeyboard(currentFocus ?: View(this))


          var datePicker= DatePickerDialog(this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH])
            datePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
            datePicker.show()
        })

        startTime.setOnClickListener({
            HideKeyboard(currentFocus ?: View(this))
            startTimeheader_id.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))
            stopTimeHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int) {

                    val AM_PM: String
                    AM_PM = if (selectedHour < 12)
                    {
                        "AM"
                    }
                    else
                    {
                        "PM"
                    }

                    try {
                        val sdf = SimpleDateFormat("H:mm")
                        val dateObj = sdf.parse(selectedHour.toString()+":"+selectedMinute.toString())
                        System.out.println(dateObj)
                        val strDate= SimpleDateFormat("K:mm").format(dateObj)
                        startTime.setText("$strDate $AM_PM" )

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                }
            }, hour, minute, false) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        })

        stopTime.setOnClickListener({
            stopTimeHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))
            startTimeheader_id.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))

            HideKeyboard(currentFocus ?: View(this))

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int) {

                    val AM_PM: String
                    AM_PM = if (selectedHour < 12)
                    {
                        "AM"
                    }
                    else
                    {
                        "PM"
                    }

                    try {
                        val sdf = SimpleDateFormat("H:mm")
                        val dateObj = sdf.parse(selectedHour.toString()+":"+selectedMinute.toString())
                        System.out.println(dateObj)
                       val strDate= SimpleDateFormat("K:mm").format(dateObj)
                        stopTime.setText("$strDate $AM_PM" )

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }



                }
            }, hour, minute, false) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        })

        val db=DatabaseHandler(this)

        var arrayListDoctor: ArrayList<String> = ArrayList()

        var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)


        arrayListDoctor.add("Select Doctor")

        for(item in model.data.doctorList)
        {
            arrayListDoctor.add(item.doctorName)

        }

        val langAdapter = ArrayAdapter(this , android.R.layout.simple_spinner_item,arrayListDoctor)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctor_spinner?.setAdapter(langAdapter)

        radio_meeting.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            HideKeyboard(currentFocus ?: View(this@SetSchedule_Activity))

            /*   when (checkedId) {

                   R.id.radio0 -> {
                   }
                   R.id.radio1 -> {
                   }
                   R.id.radio2 -> {
                   }
               }*/
        })

       // val db=DatabaseHandler(this)


       // var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)

        for(item in model.data.doctorList)
        {
            val selectorModel = DocManagerModel()
            selectorModel.setName(item.doctorName)
            selectorModel.setRoute(item.routeName)
            selectorModel.setSpeciality(item.specialityName)
            selectorModel.setId(item.doctorId)
            selectorModel.setMailId(item.emailId)
            arrayListSelectorDoctor.add(selectorModel)
        }

        selectManagers_cv.setOnClickListener({
            selectManagers_cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.appColor))
            selectDoctorManager_alert(2)
        })

        selectDoctors_cv.setOnClickListener({
            selectDoctors_cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.appColor))
            selectDoctorManager_alert(1)
        })

        submit_newSchedule.setOnClickListener({
            HideKeyboard(currentFocus ?: View(this@SetSchedule_Activity))

            if(constructorList.size==0)
            {
                selectDoctors_cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.zm_red))
                return@setOnClickListener
            }

            if(subject_et.text.toString().isEmpty())
            {
                subject_et.requestFocus()
                subject_et.setError("Required")
                return@setOnClickListener
            }
            if(selectDate_tv.text.equals("Select Date"))
            {
                selectDateHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                showSnackbar(parentSetSchedule,"Please select date")
                return@setOnClickListener
            }

            if(startTime.text.equals("Start time"))
            {
                startTimeheader_id.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))

                showSnackbar(parentSetSchedule,"Please select start time")
                return@setOnClickListener
            }

            if(stopTime.text.equals("End time"))
            {
                stopTimeHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))

                showSnackbar(parentSetSchedule,"Please select end time")

                return@setOnClickListener
            }

            if( !compareTimes() )
            {
                stopTimeHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                startTimeheader_id.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                return@setOnClickListener
            }

            if(remark_et.text.toString().isEmpty())
            {
                remark_et.requestFocus()
                remark_et.setError("Required")
                return@setOnClickListener
            }

            setSheduleApi()
         /*   if (mPreMeetingService == null)
            {
                return@setOnClickListener
            }*/



       /*     val meetingItem = mPreMeetingService!!.createScheduleMeetingItem()

            meetingItem.meetingTopic = subject_et.text.toString()
            meetingItem.startTime =getTimeDate().time
            meetingItem.durationInMinutes = getDurationInMinutes()
            meetingItem.canJoinBeforeHost = true
            meetingItem.password = "zoom"
            meetingItem.isHostVideoOff = false
            meetingItem.isAttendeeVideoOff = true

            meetingItem.availableDialinCountry = mCountry

            meetingItem.isEnableMeetingToPublic = false
            meetingItem.isEnableLanguageInterpretation =false
            meetingItem.isEnableWaitingRoom = false
            meetingItem.isUsePmiAsMeetingID = false
            var mTimeZoneId = TimeZone.getDefault().id
            meetingItem.audioType = MeetingItem.AudioType.AUDIO_TYPE_VOIP

            meetingItem.timeZoneId = mTimeZoneId

            if (mPreMeetingService != null) {
               mPreMeetingService!!.addListener(this)
                val error = mPreMeetingService!!.scheduleMeeting(meetingItem)
                if (error == ScheduleOrEditMeetingError.SUCCESS) {
                    progressMessage_tv?.setText("Scheduling Meeting")
                    enableProgress(progressView_parentRv!!)
                }
                else {
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "User not login.", Toast.LENGTH_LONG).show()
               // finish()
            }*/

        })

        doctor_spinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean
            {
                HideKeyboard(currentFocus ?: View(this@SetSchedule_Activity))

                return false
            }
        })

        doctor_spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.selectedItem

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateCalendar()
    {
        val myFormat = "dd/MM/YYYY" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        selectDate_tv.setText(sdf.format(myCalendar.getTime()))
    }


    /*  override fun onTabReselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    //    viewPager.setCurrentItem(tab.getPosition());
    }*/

    @SuppressLint("WrongConstant")
    internal class Adapter(manager: FragmentManager?) : FragmentPagerAdapter(
        manager!!,
        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        private val mFragmentList: MutableList<Fragment> = java.util.ArrayList()
        private val mFragmentTitleList: MutableList<String> = java.util.ArrayList()
        override fun getItem(position: Int): Fragment
        {
            return mFragmentList[position]
        }

        override fun getCount(): Int
        {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String)
        {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence?
        {
            return mFragmentTitleList[position]
        }
    }

    private fun getDurationInMinutes(): Int {
        val calTo = Calendar.getInstance()
        val calFrom = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        calTo.setTime(sdf.parse(startTime.text.toString()));// all done
        calFrom.setTime(sdf.parse(stopTime.text.toString()));// all done


        return (((calTo.getTimeInMillis() - calFrom.getTimeInMillis()) / 60000).toInt())
    }


    private fun getTimeDate(): Date {

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH)
        cal.time = sdf.parse(selectDate_tv.text.toString()+" "+startTime.text.toString()) // all done
        return cal.getTime()
    }


    override fun onZoomSDKLoginResult(result: Long) {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong()) {
            UserLoginCallback.getInstance().removeListener(this)
        }

        else {
            Log.e("errorMessageghfh", result.toString())
            Toast.makeText(this, "Login failed result code = $result", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onZoomIdentityExpired() {

    }

    override fun onZoomSDKLogoutResult(result: Long) {

    }

    override fun onZoomAuthIdentityExpired() {

    }

    override fun onUpdateMeeting(p0: Int, p1: Long) {

    }

    override fun onGetInviteEmailContent(p0: Int, p1: Long, p2: String?) {
     Log.e("fg8uisdf",p2.toString())
    }

    override fun onScheduleMeeting(result: Int, meetingNumber: Long) {
        if (result == PreMeetingError.PreMeetingError_Success) {

            runOnUiThread {
                disableProgress(progressView_parentRv!!)
            }


            val zoomSDK = ZoomSDK.getInstance()
            val preMeetingService = zoomSDK.preMeetingService
            if (preMeetingService != null) {

                val item = preMeetingService.getMeetingItemByUniqueId(meetingNumber)
                        if (item != null) {
                            sendMessage(item.invitationEmailContentWithTime)
                        }
            }

        }
        else {
            Toast.makeText(
                this,
                "Schedule failed result code =$result",
                Toast.LENGTH_LONG
            ).show()
            runOnUiThread {
                disableProgress(progressView_parentRv!!)
            }
        }
    }
    private fun sendMessage(invitationEmailContentWithTime: String) {

        runOnUiThread {
            progressMessage_tv?.setText("Sending request")
            enableProgress(progressView_parentRv!!)
        }

        progressMessage_tv?.setText("Sending request")
        enableProgress(progressView_parentRv!!)

        val sender = Thread(Runnable {
            try {

                val sender = GmailSender("gyaneshk13@gmail.com", "13electronic003")
                sender.sendMail(
                        "Sheduled meeting invitation from pms",
                        invitationEmailContentWithTime,
                        "gyaneshk13@gmail.com",
                        //"anilpratapjain@gmail.com"
                        "kajwadkar13@gmail.com"

                )
                runOnUiThread {
                    disableProgress(progressView_parentRv!!)
                    commonAlert(this,"Request send successfully","")

                    subject_et.setText("")
                    remark_et.setText("")
                    selectDate_tv.setText("Select Date")
                    startTime.setText("Start time")
                    stopTime.setText("End time")
                    first.isChecked=true

                }



            } catch (e: Exception) {

                runOnUiThread {
                    disableProgress(progressView_parentRv!!)
                }
                Log.e("mylog", "Error: " + e.message)
            }


        })
        sender.start()
    }


    override fun onListMeeting(result: Int, meetingList: List<Long>?) {

    }

    override fun onDeleteMeeting(p0: Int) {
    }

    fun compareTimes() :Boolean
    {
        var dateCheck=false
        val sdf = SimpleDateFormat("hh:mm")
        val startTime = sdf.parse(startTime.text.toString())
        val endTime = sdf.parse(stopTime.text.toString())

        val dateDelta = endTime.compareTo(startTime)
        when (dateDelta) {
            0 ->
            {
                showSnackbar(parentSetSchedule,"Meeting start time and end time are equal")
                dateCheck=false
                //startTime and endTime not **Equal**
                return false
            }
            1 ->
            {
                dateCheck=true
                //endTime is **Greater** then startTime
                return true
            }
            -1 -> {

                dateCheck=false
                //startTime is **Greater** then endTime
                showSnackbar(parentSetSchedule,"Meeting start time greater then end time")
                return false
            }
        }

        return dateCheck
    }

    fun selectDoctorManager_alert(selectionType: Int)
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.selectionalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val list_rv= dialogView.findViewById<View>(R.id.list_rv) as RecyclerView
        val search_et= dialogView.findViewById<View>(R.id.search_et) as EditText
        val ok_btn= dialogView.findViewById<View>(R.id.ok_btn) as Button


        val layoutManager = LinearLayoutManager(this)
        list_rv.layoutManager=layoutManager
        var adapterView : DoctorManagerSelector_Adapter? = null

        if(selectionType==1)
        {
             adapterView= DoctorManagerSelector_Adapter(arrayListSelectorDoctor,this,selectionType)
        }
        else
        {
             adapterView= DoctorManagerSelector_Adapter(arrayListSelectorTeams,this,selectionType)
        }
        list_rv.adapter = adapterView

        search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                adapterView?.getFilter()?.filter(s.toString());

            }
        })

        ok_btn.setOnClickListener({
            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    override fun selectorArray(docMangerList: ArrayList<DocManagerModel>, selectionType: Int)
    {
        if(selectionType==1)
        {
            arrayListSelectorDoctor=docMangerList;
        }
        else
        {
            arrayListSelectorTeams=docMangerList;
        }

        callSelectedAdapter(selectionType)

    }

    fun callSelectedAdapter(selectionType: Int)
    {
        if(selectionType==1)
        {
            constructorList= ArrayList()
            for (item in arrayListSelectorDoctor) {
                if(item.getChecked()!!)
                {
                    constructorList.add(item)
                }
            }

            selectedAdapeter=SelectedDocManList_adapter(constructorList,this,selectionType)
            selectedoctor_rv.adapter=selectedAdapeter
            selectedAdapeter?.notifyDataSetChanged()

        }
        else
        {
            constructorListTeam= ArrayList()
            for (item in arrayListSelectorTeams) {
                if(item.getChecked()!!)
                {
                    constructorListTeam.add(item)
                }
            }

            selectedAdapeterTeams=SelectedDocManList_adapter(constructorListTeam,this,selectionType)
            recyclerView_teams.adapter=selectedAdapeterTeams
            selectedAdapeterTeams?.notifyDataSetChanged()
        }

    }

    override fun passid(id: Int, selectionType: Int)
    {
        if(selectionType==1)
        {
            for ((iMain, itemMain) in arrayListSelectorDoctor.withIndex())
            {
                if(itemMain.getId()== id)
                {
                    val modeldata=arrayListSelectorDoctor.get(iMain)
                    modeldata.setChecked(false)
                    arrayListSelectorDoctor.set(iMain,modeldata)
                    selectedAdapeter?.notifyDataSetChanged()
                }
            }
        }
        else
        {
            for ((iMain, itemMain) in arrayListSelectorTeams.withIndex())
            {
                if(itemMain.getId()== id)
                {
                    val modeldata=arrayListSelectorTeams.get(iMain)
                    modeldata.setChecked(false)
                    arrayListSelectorTeams.set(iMain,modeldata)
                    selectedAdapeterTeams?.notifyDataSetChanged()
                }
            }
        }

        callSelectedAdapter(selectionType)
    }


    private fun getTeamsApi()
    {
        progressView_parentRv?.visibility=View.VISIBLE

        var call: Call<TeamsModel> = getSecondaryApiInterface().getTeamsMember(
            "bearer " + loginModelBase?.accessToken,
            loginModelBase.empId.toString()
        ) as Call<TeamsModel>
        call.enqueue(object : Callback<TeamsModel?> {
            override fun onResponse(
                call: Call<TeamsModel?>?,
                response: Response<TeamsModel?>
            ) {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getTeamslist=response.body()

                    for(singleItem in getTeamslist?.data?.employeeList!!)
                    {
                    var selectorModel =DocManagerModel()
                        selectorModel.setName(singleItem.firstName+" "+singleItem.lastName)
                        selectorModel.setRoute(singleItem.headQuaterName)
                        selectorModel.setSpeciality(singleItem.divisionName)
                        selectorModel.setId(singleItem.empId)
                        selectorModel.setMailId(singleItem.emailId)
                        arrayListSelectorTeams.add(selectorModel)
                    }
                }
                else
                {
                    Toast.makeText(this@SetSchedule_Activity, "Server error ", Toast.LENGTH_SHORT).show()
                }
                progressView_parentRv?.visibility=View.GONE
            }

            override fun onFailure(call: Call<TeamsModel?>, t: Throwable?) {
                call.cancel()
                progressView_parentRv?.visibility=View.GONE
            }
        })
    }

    private fun setSheduleApi()
    {
        progressMessage_tv?.setText("Scheduling Meeting")
        enableProgress(progressView_parentRv!!)

        val originalFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date: Date = originalFormat.parse(selectDate_tv.text.toString())
        val formattedDate: String = targetFormat.format(date) // 20120821

        var spf = SimpleDateFormat("hh:mm aaa")
        val startTimeStr = spf.parse(startTime.text.toString())
        val endTimeStr = spf.parse(stopTime.text.toString())
        spf= SimpleDateFormat("hh:mm:ss aaa")


        val paramObject = JSONObject()
        paramObject.put("MeetingId", "0")
        paramObject.put("MeetingDate",formattedDate )
        paramObject.put("topic",subject_et.text.toString())
        paramObject.put("StartTime",formattedDate+" "+spf.format(startTimeStr))
        paramObject.put("EndTime", formattedDate+" "+spf.format(endTimeStr))
        paramObject.put("MeetingType", if(radio_meeting.getCheckedRadioButtonId()==0) "O" else "P")
        paramObject.put("EmpId", loginModelBase.empId)
        paramObject.put("Mode", "1")
        paramObject.put("Description", remark_et.text.toString())

        val doctorList = JSONArray()
        for( item in arrayListSelectorDoctor)
        {
            if(item.getChecked()!!)
            {
                val arrayObject = JSONObject()
                arrayObject.put("MeetingId","1")
                arrayObject.put("MemberId",item.getId())
                arrayObject.put("MemberType","doc")
                arrayObject.put("EmailId",item.getMailId())
                arrayObject.put("name",item.getName())
                doctorList.put(arrayObject)
            }

        }
        paramObject.put("DoctorList", doctorList)
        val teamList = JSONArray()
        for( item in arrayListSelectorTeams)
        {
            if(item.getChecked()!!)
            {
                val arrayObject = JSONObject()
                arrayObject.put("MeetingId","1")
                arrayObject.put("MemberId",item.getId())
                arrayObject.put("MemberType","doc")
                arrayObject.put("EmailId",item.getMailId())
                arrayObject.put("name",item.getName())
                teamList.put(arrayObject)
            } }
        paramObject.put("EmployeeList", teamList)

        val bodyRequest: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), paramObject.toString())

        var call: Call<GenerateOTPModel> = getSecondaryApiInterface().setScheduleMeetingApi(
            "bearer " + loginModelBase?.accessToken,
            bodyRequest
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(
                call: Call<GenerateOTPModel?>?,
                response: Response<GenerateOTPModel?>
            ) {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getResponse=response.body()
                    if(!getResponse?.errorObj?.errorMessage.equals(""))
                    {
                        Toast.makeText(this@SetSchedule_Activity, "Server error ", Toast.LENGTH_SHORT).show()

                    }
                    else{
                        disableProgress(progressView_parentRv!!)
                        commonAlert(this@SetSchedule_Activity,getResponse?.data?.message.toString(),"")

                        subject_et.setText("")
                        remark_et.setText("")
                        selectDate_tv.setText("Select Date")
                        startTime.setText("Start time")
                        stopTime.setText("End time")
                        first.isChecked=true
                        constructorList= ArrayList()
                        constructorListTeam= ArrayList()


                    /*    selectedAdapeter=SelectedDocManList_adapter(constructorList,this@SetSchedule_Activity,1)
                        selectedoctor_rv.adapter=selectedAdapeter
                        selectedAdapeter?.notifyDataSetChanged()
                        selectedAdapeterTeams=SelectedDocManList_adapter(constructorListTeam,this@SetSchedule_Activity,1)
                        recyclerView_teams.adapter=selectedAdapeterTeams
                        selectedAdapeterTeams?.notifyDataSetChanged()*/

                    }
                }
                else
                {
                    Toast.makeText(this@SetSchedule_Activity, "Server error ", Toast.LENGTH_SHORT).show()
                }
                disableProgress(progressView_parentRv!!)

            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                call.cancel()
                disableProgress(progressView_parentRv!!)

            }
        })
    }

}