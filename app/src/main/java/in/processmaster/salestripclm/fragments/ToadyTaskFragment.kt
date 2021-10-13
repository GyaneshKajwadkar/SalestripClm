package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.HomeTaskAdapter
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ToadyTaskFragment : Fragment() {

    var recyclerView_todayTask : RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_toady_task, container, false)
        recyclerView_todayTask=view.findViewById(R.id.recyclerView_todayTask)

        val bundle = this.arguments
        if (bundle != null)
        {
            val myInt = bundle.getInt("key", 0)

            val adapter=HomeTaskAdapter(requireActivity(),myInt)
            recyclerView_todayTask?.setLayoutManager(LinearLayoutManager(requireActivity()));
            recyclerView_todayTask?.adapter=adapter
        }

        return view
    }


    override fun onResume() {
        super.onResume()

    }


    fun deleteAlert(subtitle: String, buttonText: String, mainHeading: String, context: Activity)
    {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val message_tv = dialogView.findViewById<View>(R.id.message_tv) as TextView
        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
                dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setText(buttonText)
        message_tv.setText("Are you sure you want to $subtitle")
        mainHeading_tv.setText(mainHeading)

        exit_btn.setOnClickListener{
            alertDialog.dismiss()

        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}