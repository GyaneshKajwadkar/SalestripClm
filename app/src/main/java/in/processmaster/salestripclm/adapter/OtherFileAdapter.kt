package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.models.DevisionModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class OtherFileAdapter(
    public var downloadList: ArrayList<DevisionModel.Data.EDetailing>?,
    var context: Context,
    var mCallback: ItemClickDisplayVisual?,
    var parentmodel: Int) :
        RecyclerView.Adapter<OtherFileAdapter.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var filePathName_tv: TextView = view.findViewById(R.id.filePathName_tv)
        var parentLayout: RelativeLayout = view.findViewById(R.id.parentLayout)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.otherbrandlayout, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val modeldata = downloadList?.get(position)
        holder.filePathName_tv.setText(modeldata?.brandName)


        if(parentmodel==modeldata?.geteDetailId())
        {
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
            holder.filePathName_tv.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        else
        {
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.filePathName_tv.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray));
        }


        holder.parentLayout.setOnClickListener()
        {
            mCallback?.onClickDisplayVisual(modeldata?.geteDetailId()!!,modeldata?.brandId!!,0)
        }

    }
    override fun getItemCount(): Int {
        return downloadList?.size!!
    }

}