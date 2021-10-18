package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.DownloadedActivtiy
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadEdetail_model
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class Edetailing_Adapter(
        var edetailidList: ArrayList<DevisionModel.Data.EDetailing>?,
        private var sharePreferance: PreferenceClass?,
        var context: Activity,
        var db: DatabaseHandler,
        var progressView_parentRv: RelativeLayout?
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

        if(modeldata?.isSaved==1)
        {
            holder.download_rl?.visibility = View.GONE
            holder.reDownload_rl?.visibility = View.VISIBLE

            val checkDownloadStatus= db.getDownloadStatus(modeldata?.eretailDetailList)

            if(!checkDownloadStatus)
            {
                    holder.reDownload_rl.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)));
                    holder.headerTv.setText("Pending Download")
                    holder.isPending_iv.setImageResource(R.drawable.ic_download)
            }
        }

      holder.header_tv.text = modeldata?.brandName
      holder.bottom_tv.text = "Division: "+modeldata?.divisionName

        holder.download_rl.setOnClickListener({
          //  progressView_parentRv?.visibility=View.VISIBLE
          //  downloadDivision_apiNewCalling(modeldata?.geteDetailId().toString(), modeldata)
            intentCalling(modeldata?.geteDetailId().toString(), modeldata)
        })

        holder.reDownload_rl.setOnClickListener {
          //  progressView_parentRv?.visibility=View.VISIBLE
            intentCalling(modeldata?.geteDetailId().toString(), modeldata)
           // downloadDivision_apiNewCalling(modeldata?.geteDetailId().toString(), modeldata)
        }

            holder.parent_ll.setOnClickListener(
                {
                    if(modeldata?.isSaved==1)
                    {
                     //   progressView_parentRv?.visibility=View.VISIBLE
                        intentCalling(modeldata?.geteDetailId().toString(), modeldata)
                      //  downloadDivision_apiNewCalling(modeldata?.geteDetailId().toString(), modeldata)
                    }
                    else
                    {
                     //   progressView_parentRv?.visibility=View.VISIBLE
                        intentCalling(modeldata?.geteDetailId().toString(), modeldata)
                      //  downloadDivision_apiNewCalling(modeldata?.geteDetailId().toString(), modeldata)
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
                    if (dataNames.brandName.lowercase().startsWith(constraint.toString())) {
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
        var arrayList:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()


        for(iteams in modeldata?.eretailDetailList!!)
        {
            var modelClass=DownloadEdetail_model.Data.EDetailingImages()
            modelClass.fileId=iteams.fileId
            modelClass.fileName=iteams.fileName
            modelClass.fileOrder=iteams.fileOrder
            modelClass.filePath=iteams.filePath
            modelClass.fileSize=iteams.fileSize
            modelClass.fileType=iteams.fileType
            modelClass.seteDetailId(iteams.geteDetailId())
            arrayList.add(modelClass)
        }

        //get eDetailing Image list
        val intent = Intent(context, DownloadedActivtiy::class.java)
        intent.putExtra("brandName", modeldata?.brandName)
        intent.putExtra("brandId", modeldata?.brandId)
        intent.putExtra("eDetailingId", eDetailId)

        val args = Bundle()
        args.putSerializable("ARRAYLIST", arrayList as Serializable?)
        intent.putExtra("BUNDLE",args);

        context.startActivity(intent)
    }



    //call_divisioinApi (Download file)
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
    }

    //check internet connection
    fun isInternetAvailable(context: Context): Boolean
    {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }


}
