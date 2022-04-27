package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.PhotoSlideShowActivity
import `in`.processmaster.salestripclm.activity.VideoPlayerActivity
import `in`.processmaster.salestripclm.activity.WebViewActivity
import `in`.processmaster.salestripclm.fragments.ShowDownloadedFragment
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
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OtherBrandSelectionAdapter(
    val context: Context,
    var list: ArrayList<DownloadFileModel>,
    val clickType: String,
    val end_btn: Button,
    val doctorId: Int,
    val eDetailingIdConst: Int
): RecyclerView.Adapter<OtherBrandSelectionAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageThumb_iv: ImageView = view.findViewById(R.id.pics_iv)
        var parent_llImage: LinearLayout = view.findViewById(R.id.parent_llVideo)
        var imageTitle: TextView = view.findViewById(R.id.imageTitle)
        var webview: WebView = view.findViewById(R.id.horizontal_wv)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherBrandSelectionAdapter.MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.downloaedimage_view, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val model=list.get(position)
        model.eDetailingId=eDetailingIdConst

        holder.imageTitle.setText(list.get(position).fileName)
        holder.imageTitle.setSelected(true)

        var oneTime=true
        holder.webview.setOnTouchListener(View.OnTouchListener { v, event ->

            if (oneTime) {

                val intent = Intent(context, WebViewActivity::class.java)

                //get profile data from share prefrance
                var sharePreferance = PreferenceClass(context)
                var profileData = sharePreferance?.getPref("profileData")
                var loginModel = Gson().fromJson(profileData, LoginModel::class.java)

                intent.putExtra("empId", loginModel.empId)
                intent.putExtra("brandId", model?.brandId)
                intent.putExtra("currentDateTime", ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime)
                intent.putExtra("webArray", list)
                intent.putExtra("position", position)
                intent.putExtra("doctorId", doctorId)
                intent.putExtra("model", model)
                context.startActivity(intent)
                var db= DatabaseHandler(context)
                db.insertStartTimeSlide(
                    ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime,doctorId,model?.brandId,model.brandName,0, ShowDownloadedFragment.currentTime)
                end_btn.performClick()
                oneTime = false

                Handler(Looper.getMainLooper()).postDelayed({
                    oneTime = true
                }, 500)

            }
            true
        })


        holder.parent_llImage.setOnClickListener({

            holder.parent_llImage.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
            var sharePreferance = PreferenceClass(context)
            var profileData =sharePreferance?.getPref("profileData")
            var  loginModel= Gson().fromJson(profileData, LoginModel::class.java)
            var intent = Intent(context, VideoPlayerActivity::class.java)

             if(clickType.equals("ZIP")) {
                 intent = Intent(context, WebViewActivity::class.java)
                 intent.putExtra("webArray", list)
             }
             else if(clickType.equals("VIDEO")) {
                 intent = Intent(context, VideoPlayerActivity::class.java)
                 intent.putExtra("videoArray", list)
             }
             else {
                 intent = Intent(context, PhotoSlideShowActivity::class.java)
                 intent.putExtra("imageArray", list)
             }

            intent.putExtra("empId", loginModel.empId)
            intent.putExtra("brandId", model?.brandId)
            intent.putExtra("currentDateTime", ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime)

            intent.putExtra("position", position)
            intent.putExtra("doctorId", doctorId)
            intent.putExtra("model", model)
            Log.e("fgdsfdsfhdsfdsfds",model.eDetailingId.toString())
            Log.e("uytutytfhfhgf",model.brandId.toString())
            context.startActivity(intent)
            val db= DatabaseHandler(context)
            db.insertStartTimeSlide(
                ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime,doctorId,model?.brandId,model.brandName,0, ShowDownloadedFragment.currentTime)

            end_btn.performClick()

        })

        if(clickType.equals("ZIP"))
        {
            holder.webview.visibility=View.VISIBLE
            holder.imageThumb_iv.visibility=View.GONE

            val file = File(list.get(position).filePath)
            holder.webview.settings.javaScriptEnabled = true
            holder.webview.settings.setDomStorageEnabled(true)
            holder.webview.settings.setDatabaseEnabled(true)
            holder.webview.settings.setLoadWithOverviewMode(true)
            holder.webview.getSettings().setAllowContentAccess(true)
            holder.webview.getSettings().setAllowFileAccess(true)
            holder.webview.getSettings().setUseWideViewPort(true)
            holder.webview.setInitialScale(1)
            holder.webview.loadUrl("file:///$file")
        }
        else{
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOptions = RequestOptions()
            requestOptions.isMemoryCacheable
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(Uri.fromFile(File(model.fileDirectoryPath,model.fileName)))
                .placeholder(circularProgressDrawable)
                .into(holder.imageThumb_iv)
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }
}