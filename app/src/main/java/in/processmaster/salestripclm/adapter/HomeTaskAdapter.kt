package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.fragments.ToadyTaskFragment
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeTaskAdapter(var context: FragmentActivity,var tasktype: Int) : RecyclerView.Adapter<HomeTaskAdapter.ViewHolders>() {

    val myCalendar = Calendar.getInstance()
    var selectDate_tv : TextView? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskAdapter.ViewHolders
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskview, parent, false)
        return ViewHolders(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int)
    {

        if(tasktype==2)
        {
            holder.viewLine.setBackgroundColor(Color.RED)
        }

        holder.cancel_btn.setOnClickListener(
            {
                ToadyTaskFragment().deleteAlert("delete task?","Delete task","Delete task!",context)
            }
        )

        holder.modify_btn.setOnClickListener(
            {
                createAlert("Modify task!")
            }
        )

        holder.complete_btn.setOnClickListener(
            {
                ToadyTaskFragment().deleteAlert("complete task?","Submit","Complete task!",context)

            }
        )

    }

    override fun getItemCount(): Int
    {
        return 10
    }

    class ViewHolders(view: View): RecyclerView.ViewHolder(view)
    {
        var cancel_btn=view.findViewById<Button>(R.id.cancel_btn)
        var modify_btn=view.findViewById<Button>(R.id.modify_btn)
        var complete_btn=view.findViewById<Button>(R.id.complete_btn)
        var viewLine=view.findViewById<View>(R.id.viewLine)
    }

    fun createAlert(heading :String)
    {

        val db= DatabaseHandler(context)

        var arrayListDoctor: ArrayList<String> = ArrayList()

        var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)


        arrayListDoctor.add("Select Doctor")

        for(item in model.data.doctorList)
        {
            arrayListDoctor.add(item.doctorName)

        }

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.taskalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val doctor_spinner = dialogView.findViewById<View>(R.id.doctor_spinner) as Spinner
        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
         selectDate_tv = dialogView.findViewById<View>(R.id.selectDate_tv) as TextView
        val startTime = dialogView.findViewById<View>(R.id.startTime) as TextView
        val remark_et = dialogView.findViewById<View>(R.id.remark_et) as EditText
        val langAdapter = ArrayAdapter(context , android.R.layout.simple_spinner_item,arrayListDoctor)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doctor_spinner?.setAdapter(langAdapter)

        mainHeading_tv.setText(heading)
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        selectDate_tv?.setOnClickListener({

            var datePicker= DatePickerDialog(context, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH])
            datePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
            datePicker.show()
        })

        startTime.setOnClickListener({
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
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


        
        exit_btn.setOnClickListener{

            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {


        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
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



}