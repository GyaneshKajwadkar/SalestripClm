package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.PhotoSlideShowActivity
import `in`.processmaster.salestripclm.activity.VideoPlayerActivity
import `in`.processmaster.salestripclm.activity.WebViewActivity
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DownloadedFolderAdapter(
    var sendType: String?,
    var downloadedType: String,
    var arraylist: ArrayList<DownloadFileModel>,
    var context: Context,
    var doctorIdDisplayVisual: Int,
    var brandID: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


    var filteredData: ArrayList<DownloadFileModel>? = arraylist


    class ViewHoldersVideo(view: View): RecyclerView.ViewHolder(view) {

        var videoThumb_iv:ImageView=view.findViewById(R.id.videoThumb_iv)
        var videoTitle:TextView=view.findViewById(R.id.videoTitle)
    }

    class ViewHoldersImage(view: View): RecyclerView.ViewHolder(view) {
        var pics_iv:ImageView=view.findViewById(R.id.pics_iv)
        var imageTitle:TextView=view.findViewById(R.id.imageTitle)
    }
    class ViewHoldersWeb(view: View): RecyclerView.ViewHolder(view)
    {

        var html_wv:WebView=view.findViewById(R.id.html_wv)
        var webViewTitle:TextView=view.findViewById(R.id.webViewTitle)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

      var itemView :View
       if(downloadedType.equals("VIDEO"))
       {
           itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.downloaedvideo_view, parent, false)
           return ViewHoldersVideo(itemView)
       }
        else if(downloadedType.equals("IMAGE"))
       {
           itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.downloaedimage_view, parent, false)
           return ViewHoldersImage(itemView)
       }
       else
       {
           itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.downloaedhtml_view, parent, false)
           return ViewHoldersWeb(itemView)
       }

    }


    override fun getItemCount(): Int
    {
         return filteredData?.size!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(downloadedType.equals("VIDEO"))
        {
            val videoView: ViewHoldersVideo = holder as ViewHoldersVideo
            var videomodel= filteredData!!.get(position)


            if(getAllfileList(videomodel.fileDirectoryPath, videomodel.fileName))
            {
                if(!videomodel.favFileName.isEmpty())
                {
                    videoView.videoTitle.setText(videomodel.favFileName)
                }
                else
                {
                    videoView.videoTitle.setText(videomodel.fileName)
                }


                videoView.videoTitle.setSelected(true);

                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                val requestOptions = RequestOptions()
                requestOptions.isMemoryCacheable
                Glide.with(context).setDefaultRequestOptions(requestOptions)
                        .load(Uri.fromFile(File(videomodel.fileDirectoryPath, videomodel.fileName)))
                        .placeholder(circularProgressDrawable)
                        .into(videoView.videoThumb_iv)


                holder.videoThumb_iv.setOnClickListener(View.OnClickListener { v ->

                    val intent = Intent(context, VideoPlayerActivity::class.java)

                        //get profile data from share prefrance
                        var sharePreferance = PreferenceClass(context)
                        var profileData =sharePreferance?.getPref("profileData")
                        var  loginModel= Gson().fromJson(profileData, LoginModel::class.java)

                        //get current date and time
                        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            Date()
                        )

                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        intent.putExtra("empId", loginModel.empId)
                        intent.putExtra("brandId", videomodel?.brandId)
                        intent.putExtra("doctorId", doctorIdDisplayVisual)
                        intent.putExtra("currentDateTime", currentDate + " " + currentTime)

                        intent.putExtra("videoArray", arraylist)
                        for ((index, value) in arraylist?.withIndex()!!) {
                        if(value==videomodel)
                        {
                            intent.putExtra("position", index)

                        }
                         }
                        intent.putExtra("model", videomodel)
                        context.startActivity(intent)

                        val db= DatabaseHandler(context)
                        db.insertStartTimeSlide(currentDate + " " + currentTime,doctorIdDisplayVisual,brandID)

                })



            }

        }

        else if(downloadedType.equals("IMAGE"))
        {
            val videoView: ViewHoldersImage = holder as ViewHoldersImage
            var imagemodel= filteredData!!.get(position)


            if(getAllfileList(imagemodel.fileDirectoryPath, imagemodel.fileName))
            {

                if(!imagemodel.favFileName.isEmpty())
                {
                    videoView.imageTitle.setText(imagemodel.favFileName)
                }
                else
                {
                    videoView.imageTitle.setText(imagemodel.fileName)
                }

                videoView.imageTitle.setSelected(true);

                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                val requestOptions = RequestOptions()
                requestOptions.isMemoryCacheable
                Glide.with(context).setDefaultRequestOptions(requestOptions)
                        .load(Uri.fromFile(File(imagemodel.fileDirectoryPath, imagemodel.fileName)))
                        .placeholder(circularProgressDrawable)
                        .into(videoView.pics_iv)

                videoView.pics_iv.setOnClickListener({
                    val intent = Intent(context, PhotoSlideShowActivity::class.java)

                    //get profile data from share prefrance
                    var sharePreferance = PreferenceClass(context)
                    var profileData =sharePreferance?.getPref("profileData")
                    var  loginModel= Gson().fromJson(profileData, LoginModel::class.java)

                    //get current date and time
                    val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            Date()
                    )

                    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    intent.putExtra("empId", loginModel.empId)
                    intent.putExtra("brandId", imagemodel?.brandId)
                    intent.putExtra("doctorId", doctorIdDisplayVisual)
                    intent.putExtra("currentDateTime", currentDate + " " + currentTime)
                    intent.putExtra("imageArray", arraylist)

                    for ((index, value) in arraylist?.withIndex()!!)
                    {
                    if(value==imagemodel)
                    {
                        intent.putExtra("position", index)
                    }
                    }

                    intent.putExtra("model", imagemodel)
                    context.startActivity(intent)
                    val db= DatabaseHandler(context)

                    db.insertStartTimeSlide(currentDate + " " + currentTime,doctorIdDisplayVisual,imagemodel!!.brandId)

                })
            }
        }
        else
        {
            val videoView: ViewHoldersWeb = holder as ViewHoldersWeb
            var zipmodel=filteredData?.get(position)


            if(getAllfileList(zipmodel?.fileDirectoryPath!!, zipmodel.fileName))
            {

                if(!zipmodel.favFileName.isEmpty())
                {
                    videoView.webViewTitle.setText(zipmodel.favFileName)
                }
                else
                {
                    videoView.webViewTitle.setText(zipmodel.fileName)
                }

                videoView.webViewTitle.setSelected(true)

                var filePath=File(zipmodel.filePath)

                videoView.html_wv.settings.javaScriptEnabled = true
                videoView.html_wv.settings.setDomStorageEnabled(true)
                videoView.html_wv.settings.setDatabaseEnabled(true)
                videoView.html_wv.settings.setLoadWithOverviewMode(true)
                videoView.html_wv.getSettings().setAllowContentAccess(true)
                videoView.html_wv.getSettings().setAllowFileAccess(true)
                videoView.html_wv.getSettings().setUseWideViewPort(true);
                videoView.html_wv.setInitialScale(1);

                videoView.html_wv.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        view.loadUrl(url)
                        return true
                    }
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                        //Your code to do
                        Log.e("webViewError", error.toString())
                    }

                }

                videoView.html_wv.loadUrl("file:///$filePath")


                var oneTime=true
                holder.html_wv.setOnTouchListener(OnTouchListener { v, event ->

                    if(oneTime)
                    {

                        val intent = Intent(context, WebViewActivity::class.java)

                        //get profile data from share prefrance
                        var sharePreferance = PreferenceClass(context)
                        var profileData =sharePreferance?.getPref("profileData")
                        var  loginModel= Gson().fromJson(profileData, LoginModel::class.java)

                        //get current date and time
                        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                                Date()
                        )

                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        intent.putExtra("empId", loginModel.empId)
                        intent.putExtra("brandId", zipmodel?.brandId)
                        intent.putExtra("doctorId", doctorIdDisplayVisual)
                        intent.putExtra("currentDateTime", currentDate + " " + currentTime)
                        intent.putExtra("webArray", arraylist)
                        for ((index, value) in arraylist?.withIndex()!!)
                        {
                            if(value==zipmodel)
                            {
                                intent.putExtra("position", index)

                            }
                        }
                        intent.putExtra("model", zipmodel)
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        context.startActivity(intent)

                        val db= DatabaseHandler(context)
                        db.insertStartTimeSlide(currentDate + " " + currentTime,doctorIdDisplayVisual,zipmodel?.brandId)

                        oneTime = false

                        Handler(Looper.getMainLooper()).postDelayed({
                            oneTime = true
                        }, 500)

                    }
                          true
                })


            }
        }
    }

    fun getAllfileList(pathdirectoryIs: String, fileName: String) :Boolean
    {
        val yourDir = File(pathdirectoryIs)
        for (f in yourDir.listFiles()) {
            val name = f.name
            Log.i("filenames", name)
            if(name.trim().equals(fileName.trim()))
            {
                return true
            }
        }
        return false
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredData = results.values as ArrayList<DownloadFileModel>?
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults? {

                var constraint = constraint
                val results = FilterResults()
                val FilteredArrayNames: ArrayList<DownloadFileModel> = ArrayList()

                // perform your search here using the searchConstraint String.
                constraint = constraint.toString().toLowerCase()
                for (i in 0 until arraylist?.size!!) {
                    val dataNames: DownloadFileModel = arraylist?.get(i)!!
                    if (dataNames.favFileName.toLowerCase().startsWith(constraint.toString())) {
                        FilteredArrayNames.add(dataNames)
                    }
                }
                results.count = FilteredArrayNames.size
                results.values = FilteredArrayNames
                return results
            }
        }
    }



}