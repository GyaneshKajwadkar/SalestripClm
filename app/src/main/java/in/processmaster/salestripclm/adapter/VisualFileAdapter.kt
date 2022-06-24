package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctor_et
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class VisualFileAdapter(
    var downloadList: ArrayList<DownloadFileModel>?,
    var context: Context?,
    var selectionType: Int,
    var mCallback: ItemClickDisplayVisual?
) :
        RecyclerView.Adapter<VisualFileAdapter.MyViewHolder>()
{
    //selectionType= 1 for normal 2 for fav

    var selectedPosition = -1

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var division_tv: TextView = view.findViewById(R.id.division_tv)
        var filePathName_tv: TextView = view.findViewById(R.id.filePathName_tv)
        var favBrand_tv: LinearLayout = view.findViewById(R.id.favBrand_tv)
        var parentCard: CardView = view.findViewById(R.id.parentCard)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.visualdownloadlist_view, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val modeldata = downloadList?.get(position)
        if(modeldata?.isFav==true) {
            holder.filePathName_tv.setText(modeldata?.brandName)
            holder.favBrand_tv.visibility=View.VISIBLE
        }
        else holder.filePathName_tv.setText(modeldata?.brandName)


        holder.division_tv.setText("Division: " + modeldata?.downloadType)

        if(selectedPosition==position)
        {
            holder.parentCard.setCardBackgroundColor(Color.parseColor("#0E8FD1"));
            holder.filePathName_tv.setTextColor(Color.parseColor("#ffffff"));
            holder.division_tv.setTextColor(Color.parseColor("#ffffff"));
        }

        else
        {
            holder.parentCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.filePathName_tv.setTextColor(Color.parseColor("#055E99"));
            holder.division_tv.setTextColor(Color.BLACK);
        }



        holder.parentCard.setOnClickListener()
        {
            //if doctor not selected
            if(doctor_et?.text.toString()=="")
            {
             Toast.makeText(context, "Select Doctor first", Toast.LENGTH_SHORT).show()
            }

            else
            {
                selectedPosition=position;
                notifyDataSetChanged();

                mCallback?.onClickDisplayVisual(modeldata?.eDetailingId!!,modeldata?.brandId,selectionType)

            }
        }

    }
    override fun getItemCount(): Int {
        return downloadList?.size!!
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
