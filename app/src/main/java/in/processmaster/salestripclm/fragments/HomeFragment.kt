package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.adapter.*
import `in`.processmaster.salestripclm.models.GetScheduleModel
import `in`.processmaster.salestripclm.models.SyncModel
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
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import us.zoom.sdk.ZoomSDK
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnChartGestureListener {

    var sharePreferance: PreferenceClass?= null
    var chart: BarChart? = null
    var charthalf: PieChart? = null
    var createTask_fab: FloatingActionButton? = null

    var xAxisValues: List<String> = java.util.ArrayList(
            Arrays.asList(
                    "Jan",
                    "Feb",
                    "Mar",
                    "April",
                    "May",
                    "June",
                    "July",
                    "Aug",
                    "Sept",
                    "Oct",
                    "Nov",
                    "Dec"
            )
    )
    var myText = arrayOf("Brand", "Brand", "Brand", "Brand", "Brand", "Brand")
    protected val parties = arrayOf(
            "Online meeting", "Physical meeting", "Pending visit"
    )
    var dataSets = java.util.ArrayList<IBarDataSet>()
    var defaultBarWidth = -1f
    var set1: BarDataSet? = null
    var set2: BarDataSet? = null
    var set3: BarDataSet? = null
    var set4: BarDataSet? = null
    var set5: BarDataSet? = null
    val myCalendar = Calendar.getInstance()
    var selectDate_tv : TextView? =null
    var expandable_Rv: RecyclerView?= null

    var viewPager: ViewPager?=null
    var tabs: TabLayout? = null
    var db= DatabaseHandler(context)
    var parent_ll: LinearLayout?=null
    var progressHomeFrag: ProgressBar?=null

    @SuppressLint("RestrictedApi", "UseRequireInsteadOfGet")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_home, container, false)

        viewPager = root.findViewById<View>(R.id.viewpager) as ViewPager
        tabs = root.findViewById<View>(R.id.result_tabs) as TabLayout
        createTask_fab   = root.findViewById<View>(R.id.createTask_fab) as FloatingActionButton
        parent_ll        = root.findViewById<View>(R.id.parent_ll) as LinearLayout
        progressHomeFrag = root.findViewById<View>(R.id.progressHomeFrag) as ProgressBar
        expandable_Rv    = root.findViewById<View>(R.id.expandable_Rv) as RecyclerView

        sharePreferance = PreferenceClass(activity)
        db= DatabaseHandler(requireActivity())

        createTask_fab?.setOnClickListener({
            createAlert("Create task!", "Create", requireActivity())
        })

        chart = root.findViewById(R.id.chart1)
        charthalf = root.findViewById(R.id.charthalf)

        val executorBarChart: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        val executorCalendar: ExecutorService = Executors.newSingleThreadExecutor()
        val executorAdapter: ExecutorService = Executors.newSingleThreadExecutor()
        val executorPieChart: ExecutorService = Executors.newSingleThreadExecutor()

        executorBarChart.execute(Runnable {
            //background metoud
            handler.post(Runnable {
                //UI Thread work here
            })
        })

        executorCalendar.execute(Runnable {
            //  setCalendar(root)
            handler.post(Runnable {
                //UI Thread work here
            })
        })



        executorAdapter.execute(Runnable {
            setChart(2)
            pieChartInitilize()
            handler.post(Runnable {
                //UI Thread work here
                calladapter()
                parent_ll?.visibility = View.VISIBLE
                progressHomeFrag?.visibility = View.GONE
            })
        })


        expandable_Rv?.layoutManager = LinearLayoutManager(requireActivity())


        return root
    }

    override fun onResume() {
        super.onResume()
        val responseData=db.getApiDetail(2)
        var getScheduleModel=GetScheduleModel()
        if(!responseData.equals(""))
        {
            getScheduleModel= Gson().fromJson(responseData, GetScheduleModel::class.java)
        }

        var arrayListString : ArrayList<String> = ArrayList()
        arrayListString.add("Today's meetings")
        arrayListString.add("Tommorow meetings")
        arrayListString.add("This week meetings")
        arrayListString.add("Next week meetings")
        val adapter = MeetingExpandableHeaderAdapter(requireActivity(),arrayListString,getScheduleModel)
        expandable_Rv?.adapter = adapter
    }


    //=========================================Viewpagers=================================================

    fun calladapter()
    {
        setupViewPager(viewPager!!)
        tabs!!.setupWithViewPager(viewPager)
    }
    // Add Fragments to Tabs
    private fun setupViewPager(
            viewPager: ViewPager //,
    )
    {

        if (!isAdded()) return;

        var adapter = ViewPagerAdapter(getChildFragmentManager())
        viewPager.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    class ViewPagerAdapter(fm: FragmentManager?) :
            FragmentPagerAdapter(
                    fm!!,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null
            val bundle = Bundle()

            if (position == 0)
            {
                bundle.putInt("key", 1)
                fragment = ToadyTaskFragment()
                fragment!!.arguments = bundle
            }
            else if (position == 1)
            {
                bundle.putInt("key", 2)
                fragment = ToadyTaskFragment()
                fragment!!.arguments = bundle
            }
            return fragment!!
        }


        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            if (position == 0)
            {
                title = "Today task"
            }
            else if (position == 1)
            {
                title = "Pending task"
            }
            return title
        }
    }

    fun createAlert(heading: String, buttonText: String, context: Activity)
    {


        var arrayListDoctor: ArrayList<String> = ArrayList()

     //   var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)


        arrayListDoctor.add("Select Doctor")

        for(item in SplashActivity.staticSyncData?.data?.doctorList!!)
        {
            arrayListDoctor.add(item.doctorName)

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
    private fun setChart(size: Int) {
        val incomeEntries: List<BarEntry> = getIncomeEntries(size)!!
        val expenseEntries: List<BarEntry> = getExpenseEntries(size)!!
        val expenseEntries2: List<BarEntry> = getIncomeEntries1(size)!!
        val expenseEntries3: List<BarEntry> = getIncomeEntries2(size)!!
        val expenseEntries4: List<BarEntry> = getIncomeEntries3(size)!!
        dataSets = java.util.ArrayList<IBarDataSet>()

        set1 = BarDataSet(incomeEntries, "Income")
        set1?.color = Color.rgb(255, 127, 80)
        set1?.valueTextColor = Color.rgb(55, 70, 73)
        set1?.valueTextSize = 7f
        set1?.setDrawValues(true)
        set1?.valueFormatter = MyFormatter(myText)

        set2 = BarDataSet(expenseEntries, "Expense")
        set2?.color = Color.rgb(154, 205, 50)
        set2?.valueTextColor = Color.rgb(55, 70, 73)
        set2?.valueTextSize = 7f
        set2?.setDrawValues(true)
        set2?.valueFormatter = MyFormatter(myText)


        set3 = BarDataSet(expenseEntries2, "Income")
        set3?.color = Color.rgb(0, 128, 128)
        set3?.valueTextColor = Color.rgb(55, 70, 73)
        set3?.valueTextSize = 7f
        set3?.setDrawValues(true)
        set3?.valueFormatter = MyFormatter(myText)


        set4 = BarDataSet(expenseEntries3, "Income")
        set4?.color = Color.rgb(106, 90, 205)
        set4?.valueTextColor = Color.rgb(55, 70, 73)
        set4?.valueTextSize = 7f
        set4?.setDrawValues(true)
        set4?.valueFormatter = MyFormatter(myText)


        set5 = BarDataSet(expenseEntries4, "Income")
        set5?.color = Color.rgb(72, 209, 204)
        set5?.valueTextColor = Color.rgb(55, 70, 73)
        set5?.valueTextSize = 7f
        set5?.setDrawValues(true)
        set5?.valueFormatter = MyFormatter(myText)

        dataSets.add(set1!!)
        dataSets.add(set2!!)
        dataSets.add(set3!!)
        dataSets.add(set4!!)
        dataSets.add(set5!!)


        val data = BarData(dataSets)
        chart?.setTouchEnabled(true);
        chart?.setData(data)
        chart?.getAxisLeft()?.setAxisMinimum(0f)
        chart?.getDescription()?.setEnabled(false)
        chart?.getAxisRight()?.setAxisMinimum(0f)
        chart?.setDrawBarShadow(false)
        chart?.setDrawValueAboveBar(true)
        chart?.setMaxVisibleValueCount(10)
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
        xAxis.axisMaximum = getExpenseEntries(size)?.size?.toFloat()!!
        xAxis.textSize=12f
        xAxis.granularity = 1f

        // chart?.getXAxis()?.setDrawGridLines(false);
        // chart?.getXAxis()?.setDrawLabels(false);
        // chart?.getXAxis()?.setDrawAxisLine(false);

        chart?.getXAxis()?.setValueFormatter(IndexAxisValueFormatter(xAxisValues))
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

        setBarWidth(data, size)
        chart?.getLegend()?.setEnabled(false)
        chart?.invalidate()
        chart?.setScaleMinima(1.05f, 0f)
        chart?.setOnChartGestureListener(this)

        chart!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight?) {
                val x = e.x
                val y = e.y
                viewChartAlert(requireActivity())

            }
            override fun onNothingSelected()
            {

            }
        })
    }


    private fun getExpenseEntries(size: Int): List<BarEntry>? {
        var expenseEntries = java.util.ArrayList<BarEntry>()
        expenseEntries.add(BarEntry(1f, 5f))
        expenseEntries.add(BarEntry(2f, 9f))
        expenseEntries.add(BarEntry(3f, 1f))
        return expenseEntries.subList(0, size)
    }

    private fun getIncomeEntries(size: Int): List<BarEntry>? {
        var incomeEntries = java.util.ArrayList<BarEntry>()
        incomeEntries.add(BarEntry(1f, 11f))
        incomeEntries.add(BarEntry(2f, 13f))
        incomeEntries.add(BarEntry(3f, 9f))
        return incomeEntries.subList(0, size)
    }

    private fun getIncomeEntries1(size: Int): List<BarEntry>? {
        var incomeEntries = java.util.ArrayList<BarEntry>()
        incomeEntries.add(BarEntry(1f, 16f))
        incomeEntries.add(BarEntry(2f, 17f))
        incomeEntries.add(BarEntry(3f, 1f))
        return incomeEntries.subList(0, size)
    }

    private fun getIncomeEntries2(size: Int): List<BarEntry>? {
        var incomeEntries = java.util.ArrayList<BarEntry>()
        incomeEntries.add(BarEntry(1f, 19f))
        incomeEntries.add(BarEntry(2f, 6f))
        incomeEntries.add(BarEntry(3f, 2f))
        return incomeEntries.subList(0, size)
    }

    private fun getIncomeEntries3(size: Int): List<BarEntry>? {
        var incomeEntries = java.util.ArrayList<BarEntry>()
        incomeEntries.add(BarEntry(1f, 30f))
        incomeEntries.add(BarEntry(2f, 3f))
        incomeEntries.add(BarEntry(3f, 19f))
        return incomeEntries.subList(0, size)
    }

    private fun setBarWidth(barData: BarData, size: Int) {
        if (dataSets.size != 0) {
            val barSpace = 0.04f
            val groupSpace = 0.15f
            defaultBarWidth = (1 - groupSpace) / dataSets.size - barSpace
            if (defaultBarWidth >= 0) {
                barData.barWidth = defaultBarWidth
            } else {
                Toast.makeText(
                        requireActivity(),
                        "Default Barwdith $defaultBarWidth",
                        Toast.LENGTH_SHORT
                ).show()
            }
            val groupCount = getExpenseEntries(size)!!.size
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
            }
            chart?.groupBars(0f, groupSpace, barSpace) // perform the "explicit" grouping
            chart?.invalidate()
        }
    }

    fun callMeetingAlert(selectedDateCal: String)
    {

        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")


        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dateshedule_alert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var selectedDate = dialogView.findViewById(R.id.selectedDate) as TextView
        var scheduledMeeting_rv = dialogView.findViewById(R.id.scheduledMeeting_rv) as RecyclerView

        val responseData=db.getApiDetail(2)
        if(!responseData.equals(""))
        {
            var getScheduleModel= Gson().fromJson(responseData, GetScheduleModel::class.java)
            val zoomSdk=ZoomSDK.getInstance()
            var adapterRecycler= ScheduleMeetingAdapter(requireActivity(),1,
                getScheduleModel.getData()?.meetingList as MutableList<GetScheduleModel.Data.Meeting>,zoomSdk)
            scheduledMeeting_rv.layoutManager = LinearLayoutManager(requireActivity())
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

    class MyFormatter(var text: Array<String>) : IValueFormatter {
        override fun getFormattedValue(
                value: Float,
                entry: Entry,
                dataSetIndex: Int,
                viewPortHandler: ViewPortHandler
        ): String {
            return text[entry.x.toInt()]
        }
    }

    class MyAxisValueFormatter : IAxisValueFormatter {
        private val mFormat: DecimalFormat
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mFormat.format(value.toDouble()) + " min"
        }
        init {
            mFormat = DecimalFormat("###,###,###,##0.0")
        }
    }

    override fun onChartGestureStart(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
    )
    {

    }

    override fun onChartGestureEnd(
            me: MotionEvent?,
            lastPerformedGesture: ChartTouchListener.ChartGesture?
    )
    {

    }

    override fun onChartLongPressed(me: MotionEvent?)
    {

    }

    override fun onChartDoubleTapped(me: MotionEvent?)
    {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartFling(
            me1: MotionEvent?,
            me2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
    ) {
    }

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
        set1?.setValueTextSize(7 * scallingFloat!!.toFloat())
        set2?.setValueTextSize(7 * scallingFloat!!.toFloat())
        set3?.setValueTextSize(7 * scallingFloat!!.toFloat())
        set4?.setValueTextSize(7 * scallingFloat!!.toFloat())
        set5?.setValueTextSize(7 * scallingFloat!!.toFloat())
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

    fun pieChartInitilize()
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


    }

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("Doctors visit")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 13, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 0, 13, 0)
        //   s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //   s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        //    s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        //  s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s
    }

    private fun moveOffScreen()
    {
        val displayMetrics = DisplayMetrics()
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val offset = (height * 0.65).toInt() /* percent to move */
        val rlParams = charthalf!!.layoutParams as LinearLayout.LayoutParams
        rlParams.setMargins(0, 0, 0, -offset)
        charthalf!!.layoutParams = rlParams
    }


    class PercentFormatter : IValueFormatter, IAxisValueFormatter {
        protected var mFormat: DecimalFormat

        constructor() {
            mFormat = DecimalFormat("###,###,##0.0")
        }

        /**
         * Allow a custom decimalformat
         *
         * @param format
         */
        constructor(format: DecimalFormat) {
            mFormat = format
        }

        // IValueFormatter
        override fun getFormattedValue(
                value: Float,
                entry: Entry,
                dataSetIndex: Int,
                viewPortHandler: ViewPortHandler
        ): String {
            return mFormat.format(value.toDouble()) + " %"
        }

        // IAxisValueFormatter
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mFormat.format(value.toDouble()) + " %"
        }

        val decimalDigits: Int
            get() = 1
    }


}