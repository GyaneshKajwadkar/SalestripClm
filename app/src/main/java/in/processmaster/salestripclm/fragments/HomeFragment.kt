package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageDataBase
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.adapter.CallDoctor_Adapter
import `in`.processmaster.salestripclm.adapter.MeetingExpandableHeaderAdapter
import `in`.processmaster.salestripclm.adapter.ScheduleMeetingAdapter
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.DoctorGraphModel
import `in`.processmaster.salestripclm.models.GetScheduleModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_retailer_fill.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import us.zoom.sdk.ZoomSDK
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), OnChartGestureListener {

    var sharePreferance: PreferenceClass?= null
    var chart: BarChart? = null
   // var charthalf: PieChart? = null

    var dataSets = java.util.ArrayList<IBarDataSet>()
    var defaultBarWidth = -1f
    var visitDoc: BarDataSet? = null
    var visitRetail: BarDataSet? = null

    val myCalendar = Calendar.getInstance()
    var selectDate_tv : TextView? =null
    var todaysCall_tv : TextView? =null
 //   var expandable_Rv: RecyclerView?= null
    var dailyDoctorCall_rv: RecyclerView?= null
    var dailyRetailer_rv: RecyclerView?= null
    var parent_ll: LinearLayout?=null
    var progressHomeFrag: ProgressBar?=null
    var noDocCall_tv: TextView?=null
    var doctorList=ArrayList<DailyDocVisitModel.Data.DcrDoctor>()
    var retailerList=ArrayList<DailyDocVisitModel.Data.DcrDoctor>()
    lateinit var toggleButton: MaterialButtonToggleGroup

    @SuppressLint("RestrictedApi", "UseRequireInsteadOfGet")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        if (activity != null && isAdded) {
            parent_ll        = root.findViewById<View>(R.id.parent_ll) as LinearLayout
            progressHomeFrag = root.findViewById<View>(R.id.progressHomeFrag) as ProgressBar
           // expandable_Rv    = root.findViewById<View>(R.id.expandable_Rv) as RecyclerView
            dailyDoctorCall_rv = root.findViewById<View>(R.id.dailyDoctorCall_rv) as RecyclerView
            dailyRetailer_rv = root.findViewById<View>(R.id.dailyRetailer_rv) as RecyclerView
            noDocCall_tv = root.findViewById<View>(R.id.noDocCall_tv) as TextView
            todaysCall_tv = root.findViewById<View>(R.id.todaysCall_tv) as TextView
            toggleButton = root.findViewById<View>(R.id.toggleButton) as MaterialButtonToggleGroup
            dailyDoctorCall_rv?.layoutManager=LinearLayoutManager(activity)
            dailyRetailer_rv?.layoutManager=LinearLayoutManager(activity)
           // expandable_Rv?.layoutManager = LinearLayoutManager(activity)

            sharePreferance = PreferenceClass(activity)

            chart = root.findViewById(R.id.chart1)
            // charthalf = root.findViewById(R.id.charthalf)
            parent_ll?.visibility = View.VISIBLE
            progressHomeFrag?.visibility = View.GONE

            toggleButton.forEach { button ->
                button.setOnClickListener { (button as MaterialButton).isChecked = true }
            }

            toggleButton.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->

                if (isChecked && R.id.doctorCall_btn == checkedId) {
                    if(doctorList.size==0) { noDocCall_tv?.visibility = View.VISIBLE}
                    else  { noDocCall_tv?.visibility = View.INVISIBLE}
                    dailyDoctorCall_rv?.visibility=View.VISIBLE
                    dailyRetailer_rv?.visibility=View.INVISIBLE

                } else if (isChecked && R.id.retailer_btn == checkedId) {
                    if(retailerList.size==0) { noDocCall_tv?.visibility = View.VISIBLE}
                    else  { noDocCall_tv?.visibility = View.INVISIBLE}

                    dailyDoctorCall_rv?.visibility=View.INVISIBLE
                    dailyRetailer_rv?.visibility=View.VISIBLE
                }
            })


       /*     val handler = Handler(Looper.getMainLooper())
            val executorAdapter: ExecutorService = Executors.newSingleThreadExecutor()
            executorAdapter.execute(Runnable {
                //  pieChartInitilize()
                handler.post(Runnable {
                    parent_ll?.visibility = View.VISIBLE
                    progressHomeFrag?.visibility = View.GONE
                })
            })*/



        }


        //Refresh fragment from home page
        (activity as HomePage?)?.setFragmentRefreshListener(object : HomePage.FragmentRefreshListener {
            override fun onRefresh() {
                val responseDocCall=homePageDataBase?.getApiDetail(5)
                val dbListRetailer=homePageDataBase?.getAllSaveSendRetailer("retailerFeedback")
                val eDetailingArray=homePageDataBase?.getAllSaveSend("feedback")
                doctorList.clear()
                retailerList.clear()
                if(!responseDocCall.equals(""))
                {
                    val  docCallModel= Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)

                    activity?.runOnUiThread {
                        val buttonId: Int = toggleButton.getCheckedButtonId()

                        if(docCallModel.dcrDoctorlist?.size==0 && buttonId==R.id.doctorCall_btn) { noDocCall_tv?.visibility = View.VISIBLE}

                        if (eDetailingArray != null) {
                            doctorList.addAll(eDetailingArray)
                        }
                        if (dbListRetailer != null) {
                            retailerList.addAll(dbListRetailer)
                        }

                        val docAdapter= activity?.let { CallDoctor_Adapter(doctorList, it,"doc") }
                        val retailAdapter= activity?.let { CallDoctor_Adapter(retailerList, it,"ret") }
                        dailyDoctorCall_rv?.adapter=docAdapter
                        dailyRetailer_rv?.adapter=retailAdapter

                        docCallModel.dcrDoctorlist?.let { doctorList.addAll(it) }
                        docCallModel.dcrRetailerlist?.let { retailerList.addAll(it) }
                        val totalCall= doctorList.size+ retailerList.size
                        todaysCall_tv?.setText("Today's call- "+totalCall)
                    }
                }
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()
        if(isAdded)
        {
            viewLifecycleOwner.lifecycleScope.launch {
                val default = async { fetchData() }
                default.await()
            }
        }
    }

     fun fetchData() {

        val activity: Activity? = activity
        if (activity == null && !isAdded) {
            return
        }

     //   val responseData=homePageDataBase?.getApiDetail(2)
        val responseGraph=homePageDataBase?.getApiDetail(4)
        val responseDocCall=homePageDataBase?.getApiDetail(5)
        val dbListRetailer=homePageDataBase?.getAllSaveSendRetailer("retailerFeedback")
        val eDetailingArray=homePageDataBase?.getAllSaveSend("feedback")

        doctorList.clear()
        retailerList.clear()

     /*   if(!responseData.equals(""))
        {
           val  getScheduleModel= Gson().fromJson(responseData, GetScheduleModel::class.java)
            var arrayListString : ArrayList<String> = ArrayList()
            arrayListString.add("Today's meetings")
            arrayListString.add("Tommorow meetings")
            arrayListString.add("This week meetings")
            arrayListString.add("Next week meetings")
            val adapter = activity?.let { MeetingExpandableHeaderAdapter(it,arrayListString,getScheduleModel) }
            activity?.runOnUiThread {  expandable_Rv?.adapter = adapter }
        }*/
        if(!responseGraph.equals(""))
        {
            var doctorGraphModel=DoctorGraphModel.Data()
            doctorGraphModel= Gson().fromJson(responseGraph, DoctorGraphModel.Data::class.java)
            activity?.runOnUiThread{ setChart(doctorGraphModel) }
        }

        if(!responseDocCall.equals(""))
        {
            val  docCallModel= Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)

            if (eDetailingArray != null) {
                doctorList.addAll(eDetailingArray)
            }
            if (dbListRetailer != null) {
                retailerList.addAll(dbListRetailer)
            }


            activity?.runOnUiThread {
                val buttonId: Int = toggleButton.getCheckedButtonId()

                docCallModel.dcrDoctorlist?.let { doctorList.addAll(it) }
                docCallModel.dcrRetailerlist?.let { retailerList.addAll(it) }

                val docAdapter= activity?.let { CallDoctor_Adapter(doctorList, it,"doc") }
                val retailAdapter= activity?.let { CallDoctor_Adapter(retailerList, it,"ret") }
                dailyDoctorCall_rv?.adapter=docAdapter
                dailyRetailer_rv?.adapter=retailAdapter

                val totalCall= doctorList.size+ retailerList.size
                todaysCall_tv?.setText("Today's call- "+totalCall)
                if(doctorList?.size==0 && buttonId==R.id.doctorCall_btn) { noDocCall_tv?.visibility = View.VISIBLE}

            }
        }
    }


 /*   fun updateTodaysCall()
    {
        homePageDataBase= DatabaseHandler.getInstance(activity?.applicationContext)
        val responseDocCall=homePageDataBase?.getApiDetail(5)
        val  docCallModel= Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
        val docAdapter= activity?.let { CallDoctor_Adapter(docCallModel.dcrDoctorlist, it, "ret") }
        activity?.runOnUiThread {
            if(docCallModel.dcrDoctorlist?.size==0) { noDocCall_tv?.visibility = View.VISIBLE}
            dailyDoctorCall_rv?.adapter=docAdapter
            todaysCall_tv?.setText("Today's call- "+docCallModel.dcrDoctorlist?.size)
        }
    }*/


    fun createAlert(heading: String, buttonText: String, context: Activity)
    {

        var arrayListDoctor: ArrayList<String> = ArrayList()

     //   var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)

        arrayListDoctor.add("Select Doctor")

        for(item in SplashActivity.staticSyncData?.doctorList!!)
        {
            item.doctorName?.let { arrayListDoctor.add(it) }
        }

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.taskalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val doctor_spinner = dialogView.findViewById<View>(R.id.doctor_spinner) as Spinner
        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        selectDate_tv = dialogView.findViewById<View>(R.id.selectDate_tv) as TextView
        val startTime = dialogView.findViewById<View>(R.id.startTime) as TextView
        val remark_et = dialogView.findViewById<View>(R.id.remark_et) as EditText
        val langAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                arrayListDoctor
        )
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctor_spinner?.setAdapter(langAdapter)

        mainHeading_tv.setText(heading)
        val cancel_btn =
                dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        selectDate_tv?.setOnClickListener({

            var datePicker = DatePickerDialog(
                    context, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
            datePicker.show()
        })

        startTime.setOnClickListener({
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(
                        timePicker: TimePicker?,
                        selectedHour: Int,
                        selectedMinute: Int
                ) {

                    val AM_PM: String
                    AM_PM = if (selectedHour < 12) {
                        "AM"
                    } else {
                        "PM"
                    }

                    try {
                        val sdf = SimpleDateFormat("H:mm")
                        val dateObj =
                                sdf.parse(selectedHour.toString() + ":" + selectedMinute.toString())
                        System.out.println(dateObj)
                        val strDate = SimpleDateFormat("K:mm").format(dateObj)
                        startTime.setText("$strDate $AM_PM")

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                }
            }, hour, minute, false) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()

        })

        exit_btn.setText(buttonText)

        exit_btn.setOnClickListener{

            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {

        override fun onDateSet(
                view: DatePicker?, year: Int, monthOfYear: Int,
                dayOfMonth: Int
        ) {
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateCalendar()
        }
    }

    fun updateCalendar()
    {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        selectDate_tv?.setText(sdf.format(myCalendar.getTime()))
    }

    //============================================Set calendar ==============================================


    //=============================================Chart code================================================
    private fun setChart(doctorGraphModel: DoctorGraphModel.Data) {

        val filteredList: ArrayList<DoctorGraphModel.Data.DcrCount> = ArrayList()
        for(data in doctorGraphModel?.dcrCountList!!)
        { if(data.visitedRetailer!=0 || data.visitedDoctor!=0) filteredList.add(data) }

        val doctorEntries: List<BarEntry> = getDoctorEntries(filteredList)!!
        val retailEntries: List<BarEntry> = getRetailEntries(filteredList)!!

        dataSets = java.util.ArrayList<IBarDataSet>()

        visitDoc = BarDataSet(doctorEntries, "")
        visitDoc?.color = Color.rgb(255, 127, 80)
        visitDoc?.valueTextColor = Color.rgb(55, 70, 73)
        visitDoc?.valueTextSize = 7f
        visitDoc?.setDrawValues(true)
        visitDoc?.valueFormatter = MyFormatter("Doctor")

        visitRetail = BarDataSet(retailEntries, "")
        visitRetail?.color = Color.rgb(154, 205, 50)
        visitRetail?.valueTextColor = Color.rgb(55, 70, 73)
        visitRetail?.valueTextSize = 7f
        visitRetail?.setDrawValues(true)
        visitRetail?.valueFormatter = MyFormatter("Retail")

        dataSets.add(visitDoc!!)
        dataSets.add(visitRetail!!)

        val data = BarData(dataSets)
        chart?.setTouchEnabled(true)
        chart?.setData(data)
        chart?.getAxisLeft()?.setAxisMinimum(0f)
        chart?.getDescription()?.setEnabled(false)
        chart?.getAxisRight()?.setAxisMinimum(0f)
        chart?.setDrawBarShadow(false)
        chart?.setDrawValueAboveBar(true)
        chart?.setMaxVisibleValueCount(6)
        chart?.setPinchZoom(false)
        chart?.setDrawGridBackground(false)

        val l: Legend = chart?.legend!!
        l.isWordWrapEnabled = true
        l.textSize = 14f
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.CIRCLE

        val xAxis: XAxis = chart?.xAxis!!
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
     //   xAxis.labelRotationAngle = -45f
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.axisMaximum = filteredList.size.toFloat()+0.2F!!

        xAxis.textSize=10f
        xAxis.textColor=Color.BLUE


        // chart?.getXAxis()?.setDrawGridLines(false);
        // chart?.getXAxis()?.setDrawLabels(false);
        // chart?.getXAxis()?.setDrawAxisLine(false);

        val monthArray:ArrayList<String> = ArrayList<String>()
        for(data in filteredList!!)
        {
            monthArray.add(data.monthName!!)
        }

        chart?.getXAxis()?.setValueFormatter(IndexAxisValueFormatter(monthArray))
        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis: YAxis = chart?.getAxisLeft()!!
        leftAxis.removeAllLimitLines()
        leftAxis.typeface = Typeface.DEFAULT
        leftAxis.typeface = Typeface.DEFAULT
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.textColor = Color.BLACK
        leftAxis.setDrawGridLines(false)
        leftAxis.valueFormatter = custom
        leftAxis.textSize=12f

        chart?.getAxisRight()?.setEnabled(true)
        chart?.getAxisRight()?.setDrawGridLines(true)
        chart?.getAxisRight()?.setDrawLabels(false)
        chart?.getAxisRight()?.setDrawAxisLine(true)

        setBarWidth(data, filteredList!!)
        chart?.getLegend()?.setEnabled(false)
        chart?.invalidate()
        chart?.setScaleMinima(1.05f, 0f)
        chart?.setHighlightPerTapEnabled(false)
       // chart?.setOnChartGestureListener(this)

        chart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight?) {
                val x = e.x
                val y = e.y
               // viewChartAlert(requireActivity())

            }
            override fun onNothingSelected()
            {

            }
        })
    }


    private fun getRetailEntries(dcrCountList: List<DoctorGraphModel.Data.DcrCount>): List<BarEntry>? {
        var entriesList = java.util.ArrayList<BarEntry>()
       for(i in dcrCountList)
       {
           if(i.visitedRetailer==0){}
           else{entriesList.add(BarEntry(1f, i.visitedRetailer!!.toFloat()))}

       }
        return entriesList.subList(0, entriesList.size)
    }

    private fun getDoctorEntries( dcrCountList: List<DoctorGraphModel.Data.DcrCount>): List<BarEntry>? {
        var entriesList = java.util.ArrayList<BarEntry>()

        for(i in dcrCountList)
        {
            if(i.visitedDoctor==0){}
            else{ entriesList.add(BarEntry(1f, i.visitedDoctor!!.toFloat()))}
        }
        return entriesList.subList(0, entriesList.size)
    }

    private fun setBarWidth(
        barData: BarData,
        dcrCountList: List<DoctorGraphModel.Data.DcrCount>
    ) {
        if (dataSets.size != 0) {
            val barSpace = 0.04f
            val groupSpace = 0.15f
            defaultBarWidth = (1 - groupSpace) / dataSets.size - barSpace
            if (defaultBarWidth >= 0) {
                barData.barWidth = defaultBarWidth
            } else {
                Toast.makeText(
                        activity,
                        "Default Barwdith $defaultBarWidth",
                        Toast.LENGTH_SHORT
                ).show()
            }
        /*    val groupCount = getRetailEntries(dcrCountList)!!.size
            if (groupCount != -1) {
                chart?.getXAxis()?.setAxisMinimum(0f)
                chart?.getXAxis()?.setAxisMaximum(
                        0 + chart?.getBarData()?.getGroupWidth(
                                groupSpace,
                                barSpace
                        )!! * groupCount
                )
                chart?.getXAxis()?.setCenterAxisLabels(true)
            } else {
                Toast.makeText(
                        requireActivity(),
                        "no of bar groups is $groupCount",
                        Toast.LENGTH_SHORT
                ).show()
            }*/
            chart?.groupBars(0f, groupSpace, barSpace) // perform the "explicit" grouping
            chart?.invalidate()
        }
    }

    fun callMeetingAlert(selectedDateCal: String)
    {

        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")


        val dialogBuilder = activity?.let { AlertDialog.Builder(it) }
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dateshedule_alert, null)
        dialogBuilder?.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder?.create()!!
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var selectedDate = dialogView.findViewById(R.id.selectedDate) as TextView
        var scheduledMeeting_rv = dialogView.findViewById(R.id.scheduledMeeting_rv) as RecyclerView

        val responseData=homePageDataBase?.getApiDetail(2)
        if(!responseData.equals(""))
        {
            var getScheduleModel= Gson().fromJson(responseData, GetScheduleModel::class.java)
            val zoomSdk=ZoomSDK.getInstance()
            var adapterRecycler= activity?.let {
                ScheduleMeetingAdapter(
                    it,1,
                    getScheduleModel.getData()?.meetingList as MutableList<GetScheduleModel.Data.Meeting>,zoomSdk)
            }
            scheduledMeeting_rv.layoutManager = LinearLayoutManager(activity)
            scheduledMeeting_rv.adapter = adapterRecycler
        }



        try {
            val date = format.parse(selectedDateCal)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val d = dateFormat.format(date)

            selectedDate.setText("Date- $d")

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        alertDialog.show()
    }

    class MyFormatter(var text: String) : IValueFormatter {
        override fun getFormattedValue(
                value: Float,
                entry: Entry,
                dataSetIndex: Int,
                viewPortHandler: ViewPortHandler
        ): String {
            return text
        }
    }

    class MyAxisValueFormatter : IAxisValueFormatter {
        private val mFormat: DecimalFormat
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mFormat.format(value.toInt())
        }
        init {
            mFormat = DecimalFormat("###,###,###,##0")
        }
    }

    override fun onChartGestureStart(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {}

    override fun onChartGestureEnd(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {}

    override fun onChartLongPressed(me: MotionEvent?) {}

    override fun onChartDoubleTapped(me: MotionEvent?) {}

    override fun onChartSingleTapped(me: MotionEvent?) {}

    override fun onChartFling(
            me1: MotionEvent?,
            me2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
    ) {}

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float)
    {
        var scallingFloat=chart?.scaleX?.toInt()

        if(scallingFloat!!<0)
        {
            scallingFloat=1
        }
        else
        {
            if(scallingFloat<=2)
            {

            }
            else
            {
                scallingFloat=2
            }

        }
        visitDoc?.setValueTextSize(7 * scallingFloat!!.toFloat())
        visitRetail?.setValueTextSize(7 * scallingFloat!!.toFloat())

    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {

    }
    fun viewChartAlert(context: Activity)
    {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.graphalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)

        val ok_btn =
                dialogView.findViewById<View>(R.id.ok_btn) as MaterialButton

        ok_btn.setOnClickListener{
            alertDialog.dismiss()
            chart?.highlightValue(null)
        }

        alertDialog.show()
    }
    //================================================Pie chart ==============================================

   /* fun pieChartInitilize()
    {
        charthalf!!.setBackgroundColor(Color.WHITE)
     //   moveOffScreen()

        charthalf!!.setUsePercentValues(true)
        charthalf!!.description.isEnabled = false

        charthalf!!.setCenterText(generateCenterSpannableText())

        charthalf!!.setDrawHoleEnabled(true)
        charthalf!!.setHoleColor(Color.WHITE)

        charthalf!!.setTransparentCircleColor(Color.WHITE)
        charthalf!!.setTransparentCircleAlpha(110)

        charthalf!!.setHoleRadius(58f)
        charthalf!!.setTransparentCircleRadius(61f)

        charthalf!!.setDrawCenterText(true)

        charthalf!!.setRotationEnabled(false)
        charthalf!!!!.isHighlightPerTapEnabled = true

        charthalf!!.setMaxAngle(180f) // HALF CHART

        charthalf!!.setRotationAngle(180f)
        charthalf!!.setCenterTextOffset(0f, -20f)


     //  charthalf!!.animateY(1400, Easing.EaseInOutQuad)

        val l = charthalf!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 15f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.textSize=15f

        // entry label styling
        charthalf!!.setEntryLabelColor(Color.BLACK)
        charthalf!!.setEntryLabelTextSize(12f)
        charthalf!!.setDrawEntryLabels(false)


    }*/

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("Doctors visit")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 13, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 0, 13, 0)
          return s
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

}