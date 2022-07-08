package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.DownloadedActivtiy
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.*


class Edetailing_Adapter(
    var edetailidList: ArrayList<DevisionModel.Data.EDetailing>?,
    private var sharePreferance: PreferenceClass?,
    var context: Activity?,
    var db: DatabaseHandler
) : RecyclerView.Adapter<Edetailing_Adapter.MyViewHolder>(), Filterable
{

    var filteredData: ArrayList<DevisionModel.Data.EDetailing>? = edetailidList
    var downloadList: ArrayList<DownloadFileModel>? = ArrayList()
    var alertDialog: AlertDialog?=null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var header_tv: TextView = view.findViewById(R.id.header_tv)
        var bottom_tv: TextView = view.findViewById(R.id.bottom_tv)
        var download_rl: RelativeLayout = view.findViewById(R.id.download_rl)
        var reDownload_rl: RelativeLayout = view.findViewById(R.id.reDownload_rl)
        var parent_ll: LinearLayout = view.findViewById(R.id.parent_ll)
        var valueProgressBar: ProgressBar = view.findViewById(R.id.valueProgressBar)
        var headerTv: TextView = view.findViewById(R.id.headerTv)
        var isPending_iv: ImageView = view.findViewById(R.id.isPending_iv)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.edetailing_view, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val modeldata = filteredData?.get(position)
        Log.e("fdgdgdgdgdgdf","dfsdfsdfsdfgegdf")
        if(modeldata?.isSaved==1)
        {
            holder.download_rl?.visibility = View.GONE
            holder.reDownload_rl?.visibility = View.VISIBLE

           val runnable= Runnable {
               val checkDownloadStatus= db.getDownloadStatus(modeldata?.eretailDetailList!!)

               context?.runOnUiThread {
                   if(!checkDownloadStatus)
                   {
                       holder.reDownload_rl.setBackgroundTintList(context?.let {
                           ContextCompat.getColor(
                               it, R.color.orange)
                       }?.let { ColorStateList.valueOf(it) });
                       holder.headerTv.setText("Pending Download")
                       holder.isPending_iv.setImageResource(R.drawable.ic_download)
                   }
               }
           }
            Thread(runnable).start()
        }

      holder.header_tv.text = modeldata?.brandName
      holder.bottom_tv.text = "Division: "+modeldata?.divisionName

        holder.download_rl.setOnClickListener({
            intentCalling(modeldata?.geteDetailId().toString(), modeldata)
        })

        holder.reDownload_rl.setOnClickListener {
            intentCalling(modeldata?.geteDetailId().toString(), modeldata)
        }

            holder.parent_ll.setOnClickListener(
                {
                    if(modeldata?.isSaved==1)
                    {
                        intentCalling(modeldata?.geteDetailId().toString(), modeldata)
                    }
                    else
                    {
                        intentCalling(modeldata?.geteDetailId().toString(), modeldata)
                    }
                }
            )
    }
    override fun getItemCount(): Int {
        return filteredData?.size!!
    }

    //filterUsingEdit
    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = results.values as ArrayList<DevisionModel.Data.EDetailing>?
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults? {

                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()

                // perform your search here using the searchConstraint String.
                constraint = constraint.toString().lowercase()
                for (i in 0 until edetailidList?.size!!) {
                    val dataNames: DevisionModel.Data.EDetailing = edetailidList?.get(i)!!
                    if (dataNames.brandName?.lowercase()?.startsWith(constraint.toString())!!) {
                        FilteredArrayNames.add(dataNames)
                    }
                }
                results.count = FilteredArrayNames.size
                results.values = FilteredArrayNames
                return results
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun intentCalling(
        eDetailId: String,
        modeldata: DevisionModel.Data.EDetailing?
    )
    {
        var arrayListDownloadFileModel:ArrayList<DownloadFileModel> = ArrayList()


        for(iteams in modeldata?.eretailDetailList!!)
        {
            var fileModel=DownloadFileModel()
            fileModel.fileId=iteams.fileId
            fileModel.fileName=iteams.fileName
            fileModel.filePath=iteams.filePath

            fileModel.downloadType=iteams.fileType
            fileModel.eDetailingId=iteams.geteDetailId()

           /* var modelClass=DownloadEdetail_model.Data.EDetailingImages()
            modelClass.fileId=iteams.fileId
            modelClass.fileName=iteams.fileName
            modelClass.fileOrder=iteams.fileOrder
            modelClass.filePath=iteams.filePath
            modelClass.fileSize=iteams.fileSize
            modelClass.fileType=iteams.fileType
            modelClass.eDetailId=iteams.geteDetailId()
            arrayList.add(modelClass)*/
            arrayListDownloadFileModel.add(fileModel)
        }

        //get eDetailing Image list
        val intent = Intent(context, DownloadedActivtiy::class.java)
        intent.putExtra("brandName", modeldata?.brandName)
        intent.putExtra("brandId", modeldata?.brandId)
        intent.putExtra("eDetailingId", eDetailId)

        val args = Bundle()
        args.putSerializable("ARRAYLIST", arrayListDownloadFileModel as Serializable?)
        intent.putExtra("BUNDLE",args);

        context?.startActivity(intent)
    }



 /*   //call_divisioinApi (Download file)
    private fun downloadDivision_apiNewCalling(
            eDetailId: String,
            modeldata: DevisionModel.Data.EDetailing?
    )
    {

        //get profile data from sharePreferance
        var profileData =sharePreferance?.getPref("profileData")
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)

        //   enableProgress(progressBar)
        var  apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(
                APIInterface::class.java
        )

        var call: Call<DownloadEdetail_model> = apiInterface?.downloadUrl(
                "bearer " + loginModel?.accessToken,
                eDetailId
        ) as Call<DownloadEdetail_model>
        call.enqueue(object : Callback<DownloadEdetail_model?> {
            override fun onResponse(
                    call: Call<DownloadEdetail_model?>?,
                    response: Response<DownloadEdetail_model?>
            ) {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    val arrayList = response.body()?.getData()?.geteDetailingImagesList()

                    //get eDetailing Image list
                    val intent = Intent(context, DownloadedActivtiy::class.java)
                    intent.putExtra("brandName", modeldata?.brandName)
                    intent.putExtra("brandId", modeldata?.brandId)
                    intent.putExtra("eDetailingId", eDetailId)

                    val args = Bundle()
                    args.putSerializable("ARRAYLIST", arrayList as Serializable?)
                    intent.putExtra("BUNDLE",args)

                    context.startActivity(intent)

                } else {
                    // alertDialog?.dismiss()
                    Toast.makeText(context, "Unable to download", Toast.LENGTH_SHORT).show()

                }
                progressView_parentRv?.visibility=View.GONE
            }

            override fun onFailure(call: Call<DownloadEdetail_model?>, t: Throwable?) {
                call.cancel()
                progressView_parentRv?.visibility=View.GONE
            }
        })
    }*/



}
