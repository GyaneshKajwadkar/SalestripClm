package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_all_data_sharing.*
import java.io.File

class AllDataSharingActivity : BaseActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_data_sharing)
        initView()
    }
    fun initView()
    {
        dbBase= DatabaseHandler(this)

        dataSelectorback_iv.setOnClickListener({ onBackPressed() })

        var downloadedList = dbBase?.getAllDataUsingType("IMAGE")

        dataSelector_rv.layoutManager= GridLayoutManager(this, 6)

        var adapter=SelectorAdapter(downloadedList)
        dataSelector_rv.adapter=adapter

        share_ll.setOnClickListener({
            var intent = Intent()
            intent.putExtra("editTextValue", "value_here")
            setResult(RESULT_OK, intent)
            finish()
        })

    }

    inner class SelectorAdapter(var allFiles: java.util.ArrayList<DownloadFileModel>?): RecyclerView.Adapter<SelectorAdapter.MyViewHolder>()
    {
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        {
            var imageSelectionImg=view.findViewById<ImageView>(R.id.imageSelectionImg)
            var parentImage_rl=view.findViewById<RelativeLayout>(R.id.parentImage_rl)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.selectorlayout, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            val imgFile = File(allFiles?.get(position)?.filePath)

            if (imgFile.exists())
            {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.imageSelectionImg.setImageBitmap(myBitmap)
            }

            holder.imageSelectionImg.setOnClickListener({
                val buttonColor = holder.parentImage_rl.getBackground() as ColorDrawable
                val colorId = buttonColor.color

                if (colorId == ContextCompat.getColor(this@AllDataSharingActivity, R.color.gray)) {
                    holder.parentImage_rl.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AllDataSharingActivity,
                            R.color.appColor
                        )) }
                else {
                    holder.parentImage_rl.setBackgroundColor(
                        ContextCompat.getColor(
                            this@AllDataSharingActivity,
                            R.color.gray
                        )
                    )
                }
            })
        }

        override fun getItemCount(): Int
        {
            return allFiles?.size!!
        }
    }

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }
}




