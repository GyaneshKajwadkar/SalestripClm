package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.SortingDisplayVisual
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class EdetailingSelectorAdapter(var downloadTypeList: ArrayList<String>, var mainList: ArrayList<DownloadFileModel>, var listner: SortingDisplayVisual,var context: Context?) : RecyclerView.Adapter<EdetailingSelectorAdapter.MyViewHolder>() {

    var sendingList: ArrayList<DownloadFileModel> = ArrayList()
    var buttonViewList: ArrayList<Button> =  ArrayList();


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var selector_btn: Button = view.findViewById(R.id.selector_btn)

        }

        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.edetailing_selector, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: EdetailingSelectorAdapter.MyViewHolder, position: Int) {

            buttonViewList.add(holder.selector_btn)
            holder.selector_btn?.setText(downloadTypeList.get(position))

         holder.selector_btn.setOnClickListener({
             sendingList.clear()
             for ((index, valueDownload) in mainList?.withIndex()!!) {

                 if (valueDownload.downloadType.equals(holder.selector_btn.text)) {
                     sendingList?.add(valueDownload)
                 }
                 if (index == mainList?.size!! - 1) {
                     listner.onClickButton(sendingList)
                 }
             }
             for (currentList in buttonViewList) {
                 currentList.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray));
             }
             holder.selector_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.appColor));

         })

        }

        override fun getItemCount(): Int {
            return downloadTypeList?.size!!
        }



    }