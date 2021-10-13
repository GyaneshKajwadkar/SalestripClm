package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.StoreVisualInterface
import `in`.processmaster.salestripclm.activity.WebViewActivity
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import kotlin.collections.ArrayList


class HorizontalWebViewAdapter(
        var list: ArrayList<DownloadFileModel>,
        var context: Context,
        var stringInterface: StoreVisualInterface,
        var brandId: Int,
        var constPosition: Int
) : RecyclerView.Adapter<HorizontalWebViewAdapter.MyViewHolder>() {

   var relativeViewList: ArrayList<RelativeLayout> =  ArrayList();
    private var gs: GestureDetector? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var webview: WebView = view.findViewById(R.id.horizontal_wv)
        var webParent_rv: RelativeLayout = view.findViewById(R.id.webParent_rv)
        var brandName: TextView = view.findViewById(R.id.brandName)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_webview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HorizontalWebViewAdapter.MyViewHolder, position: Int) {

        var zipModel= list.get(position)
        holder.brandName.setText(list.get(position).fileName)
        holder.brandName.setSelected(true)

        val file = File(list.get(position).filePath)
        relativeViewList.add(holder.webParent_rv)

       holder.webview.settings.javaScriptEnabled = true
       holder.webview.settings.setDomStorageEnabled(true)
       holder.webview.settings.setDatabaseEnabled(true)
       holder.webview.settings.setLoadWithOverviewMode(true)
       holder.webview.getSettings().setAllowContentAccess(true)
       holder.webview.getSettings().setAllowFileAccess(true)
       holder.webview.getSettings().setUseWideViewPort(true);
       holder.webview.setInitialScale(1);

        if(constPosition==position)
        {
            holder.webParent_rv.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
        }


        holder.webview.setOnTouchListener(OnTouchListener { v, event ->
            for (currentList in relativeViewList) {
                currentList.setBackgroundColor(Color.TRANSPARENT);
            }
            holder.webParent_rv.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));

            var filePath=File(zipModel.filePath)
            WebViewActivity.modelweb=zipModel

            stringInterface.onClickString(filePath)



            true
        })


        holder.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                //Your code to do
                Log.e("webViewError", error.toString())
            }

        }
       holder.webview.loadUrl("file:///$file")

    }

    override fun getItemCount(): Int {
        return list?.size!!
    }



}