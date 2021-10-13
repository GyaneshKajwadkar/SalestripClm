package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.ImageSelectorActivity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class ImageSelectorAdapter constructor() :
        RecyclerView.Adapter<ImageSelectorAdapter.MyViewHolder>()
{
    var allFiles: ArrayList<ImageSelectorActivity.SendImage>? = null
    lateinit var  context: Context

    constructor(allFiles: ArrayList<ImageSelectorActivity.SendImage>?, context: Context) : this()
    {
        this.allFiles=allFiles;
        this.context=context;
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var imageSelectionImg=view.findViewById<ImageView>(R.id.imageSelectionImg)
        var parentImage_rl=view.findViewById<RelativeLayout>(R.id.parentImage_rl)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.imageselector, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val imgFile = File(allFiles?.get(position)?.file?.absolutePath)

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            holder.imageSelectionImg.setImageBitmap(myBitmap)
        }

        holder.imageSelectionImg.setOnClickListener({
            val buttonColor = holder.parentImage_rl.getBackground() as ColorDrawable
            val colorId = buttonColor.color

            if (colorId == ContextCompat.getColor(context, R.color.gray)) {
                allFiles?.get(position)?.isSend = true
                holder.parentImage_rl.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.appColor
                    )
                )
            } else {
                allFiles?.get(position)?.isSend = false
                holder.parentImage_rl.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
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

    fun getAllWorkRows(): ArrayList<ImageSelectorActivity.SendImage>? {
        return allFiles
    }
}