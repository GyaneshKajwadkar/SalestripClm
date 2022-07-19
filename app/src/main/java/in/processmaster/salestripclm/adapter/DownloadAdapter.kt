package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.DownloadedActivtiy
import `in`.processmaster.salestripclm.activity.PhotoSlideShowActivity
import `in`.processmaster.salestripclm.activity.VideoPlayerActivity
import `in`.processmaster.salestripclm.activity.WebViewActivity
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.apache.commons.io.FileUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.lang.Runnable
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class DownloadAdapter constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    var context: DownloadedActivtiy? = null
    var downloadedType=""
    var eDetailingId=""
    var brandName=""
    var parentId=0
    var arraylistVideo: ArrayList<DownloadFileModel> = ArrayList()

    constructor(context: DownloadedActivtiy, downloadedType: String, parentId: Int, brandName: String, arraylistVideo: ArrayList<DownloadFileModel>, eDetailingId: String) : this()
    {
        this.context=context
        this.downloadedType=downloadedType
        this.parentId=parentId
        this.brandName= brandName
        this.arraylistVideo= arraylistVideo
        this.eDetailingId= eDetailingId
    }

    var progressBarAlert: ProgressBar?=null
    var textViewAlert:TextView?=null
    var alertDialog: AlertDialog?=null



    class ViewHoldersVideo(view: View): RecyclerView.ViewHolder(view)
    {
        var videoThumb_iv: ImageView =view.findViewById(R.id.videoThumb_iv)
        var videoTitle:TextView=view.findViewById(R.id.videoTitle)
        var download_rl:RelativeLayout=view.findViewById(R.id.download_rl)
        var reDownload_rl:RelativeLayout=view.findViewById(R.id.reDownload_rl)
        var fav_iv:ImageView=view.findViewById(R.id.fav_iv)
    }

    class ViewHoldersImage(view: View): RecyclerView.ViewHolder(view)
    {
        var pics_iv: ImageView =view.findViewById(R.id.pics_iv)
        var imageTitle:TextView=view.findViewById(R.id.imageTitle)
        var download_rl:RelativeLayout=view.findViewById(R.id.download_rl)
        var reDownload_rl:RelativeLayout=view.findViewById(R.id.reDownload_rl)
        var fav_iv:ImageView=view.findViewById(R.id.fav_iv)
    }

    class ViewHoldersWeb(view: View): RecyclerView.ViewHolder(view)
    {
        var temppic_iv: ImageView =view.findViewById(R.id.temppic_iv)
        var html_wv: WebView =view.findViewById(R.id.html_wv)
        var webViewTitle:TextView=view.findViewById(R.id.webViewTitle)
        var download_rl:RelativeLayout=view.findViewById(R.id.download_rl)
        var reDownload_rl:RelativeLayout=view.findViewById(R.id.reDownload_rl)
        var fav_iv:ImageView=view.findViewById(R.id.fav_iv)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        var itemView :View
        if(downloadedType.equals("VIDEO"))
        {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.downloadvideo_view, parent, false)
            return ViewHoldersVideo(itemView)
        }
        else if(downloadedType.equals("IMAGE"))
        {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.downloadimage_view, parent, false)
            return ViewHoldersImage(itemView)
        }
        else
        {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.downloadhtml_view, parent, false)
            return ViewHoldersWeb(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        var db = DatabaseHandler.getInstance(context!!)
        if(downloadedType.equals("VIDEO"))
        {
            val videoView: ViewHoldersVideo = holder as ViewHoldersVideo
            var videomodel=arraylistVideo.get(position)

            videoView.videoTitle.setText(videomodel.fileName)
            videoView.videoTitle.setSelected(true)

            var dbdata=db.getSingleDownloadedData(videomodel.fileId!!)
            if(dbdata.fileName!=null)
            {
                if(getAllfileList(dbdata.fileDirectoryPath,dbdata.fileName))
                {
                        val circularProgressDrawable = CircularProgressDrawable(context!!)
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()

                        videoView.reDownload_rl.visibility=View.VISIBLE
                        //    videoView.fav_iv.visibility=View.VISIBLE
                        videoView.download_rl.visibility=View.GONE

                        val requestOptions = RequestOptions()
                        requestOptions.isMemoryCacheable
                        Glide.with(context!!).setDefaultRequestOptions(requestOptions)
                            .load(Uri.fromFile(File(dbdata.fileDirectoryPath,dbdata.fileName)))
                            .skipMemoryCache( true )
                            .thumbnail(0.1f)
                            .placeholder(circularProgressDrawable)
                            .into(videoView.videoThumb_iv)

                       /* if(dbdata.favFile!!)
                        {
                            videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red));
                            videoView.fav_iv.setTag("set")
                        }
                        else
                        {
                            videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray));
                            videoView.fav_iv.setTag("not")
                        }*/




                    videoView.videoThumb_iv.setOnClickListener({
                        if(videoView.reDownload_rl.visibility==View.VISIBLE)
                        {
                            val intent = Intent(context, VideoPlayerActivity::class.java)
                            intent.putExtra("singleSelection",dbdata.filePath)
                            context!!.startActivity(intent)
                        }
                    })
                }
            }

           /* videoView.fav_iv.setOnClickListener({

                if(videoView.fav_iv.getTag().equals("set"))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray))
                    videoView.fav_iv.setTag("not")
                    db.updateFavouriteItem(videomodel.fileId.toString(),0)

                    var downloadedModel=db.getSingleDownloadedData(videomodel.fileId!!)
                    deleteAvailableFiles(downloadedModel.favFilePath)
                }
                else
                {
                    favAlertVideo(videoView,videomodel,db)
                }

                checkIsItemIsFav(db)
            })*/



            videoView.download_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }

                progressDialog()
                progressBarAlert?.setIndeterminate(true)

                newDownloadMethod(videomodel,position,"VIDEO")
            //  downloadUrl(videomodel.filePath!!,position,"VIDEO",videomodel)
            })

            videoView.reDownload_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
                newDownloadMethod(videomodel,position,"VIDEO")
             //   downloadUrl(videomodel.filePath!!,position,"VIDEO",videomodel)

            })
        }

        else if(downloadedType.equals("IMAGE"))
        {
            val videoView: ViewHoldersImage = holder as ViewHoldersImage
            var imagemodel=arraylistVideo.get(position)

            videoView.imageTitle.setText(imagemodel.fileName)
            videoView.imageTitle.setSelected(true);

            var dbdata=db.getSingleDownloadedData(imagemodel.fileId!!)
            if(dbdata.fileName!=null)
            {
                if(getAllfileList(dbdata.fileDirectoryPath,dbdata.fileName))
                {
                    videoView.reDownload_rl.visibility=View.VISIBLE
                   // videoView.fav_iv.visibility=View.VISIBLE
                    videoView.download_rl.visibility=View.GONE

                    val circularProgressDrawable = CircularProgressDrawable(context!!)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    val requestOptions = RequestOptions()
                    requestOptions.isMemoryCacheable
                    Glide.with(context!!).setDefaultRequestOptions(requestOptions)
                        .load(Uri.fromFile(File(dbdata.fileDirectoryPath,dbdata.fileName)))
                        .placeholder(circularProgressDrawable)
                        .skipMemoryCache( true )
                        .thumbnail(0.1f)
                        .into(videoView.pics_iv)

                    videoView.pics_iv.setOnClickListener({
                        if(videoView.reDownload_rl.visibility==View.VISIBLE)
                        {
                            val intent = Intent(context, PhotoSlideShowActivity::class.java)
                            intent.putExtra("singleSelection",dbdata.filePath)
                            intent.putExtra("model",dbdata)
                            context!!.startActivity(intent)
                        }
                    })

                    if(dbdata.favFile!!)
                    {
                        videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red))
                        videoView.fav_iv.setTag("set")
                    }
                    else
                    {
                        videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray))
                        videoView.fav_iv.setTag("not")
                    }
                }
            }

            videoView.download_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
                newDownloadMethod(imagemodel,position,"IMAGE")

                //  downloadUrl(imagemodel.filePath!!, position, "IMAGE", imagemodel)

            })

            videoView.reDownload_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
                newDownloadMethod(imagemodel,position,"IMAGE")
              //  downloadUrl(imagemodel.filePath!!, position, "IMAGE", imagemodel)

            })

            videoView.fav_iv.setOnClickListener({

                if(videoView.fav_iv.getTag().equals("set"))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray))
                    videoView.fav_iv.setTag("not")
                    db.updateFavouriteItem(imagemodel.fileId.toString(),0)

                    var downloadedModel=db.getSingleDownloadedData(imagemodel.fileId!!)
                    deleteAvailableFiles(downloadedModel.favFilePath)
                }
                else
                {
                    favAlertImage(videoView,imagemodel,db)

                }
             //   checkIsItemIsFav(db)
            })

        }
        else
        {
            val videoView: ViewHoldersWeb = holder as ViewHoldersWeb
            var zipmodel=arraylistVideo.get(position)


            videoView.webViewTitle.setText(zipmodel.fileName)
            videoView.webViewTitle.setSelected(true);

            var dbdata=db.getSingleDownloadedData(zipmodel.fileId!!)
            if(dbdata.fileName!=null)
            {
                if(getAllfileList(dbdata.fileDirectoryPath,dbdata.fileName))
                {
                    videoView.reDownload_rl.visibility=View.VISIBLE
                  //  videoView.fav_iv.visibility=View.VISIBLE
                    videoView.html_wv.visibility=View.VISIBLE
                    videoView.download_rl.visibility=View.GONE
                    videoView.temppic_iv.visibility=View.GONE

                    var filePath=File(dbdata.filePath)

                    videoView.html_wv.settings.javaScriptEnabled = true
                    videoView.html_wv.settings.setDomStorageEnabled(true)
                    videoView.html_wv.settings.setDatabaseEnabled(true)
                    videoView.html_wv.settings.setLoadWithOverviewMode(true)
                    videoView.html_wv.getSettings().setAllowContentAccess(true)
                    videoView.html_wv.getSettings().setAllowFileAccess(true)
                    videoView.html_wv.getSettings().setUseWideViewPort(true);
                    videoView.html_wv.setInitialScale(1);

                    videoView.html_wv.loadUrl("file:///$filePath")

                    /*   videoView.html_wv.webViewClient = object : WebViewClient() {
                           override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                               view.loadUrl(url)
                               return true
                           }
                           override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                               //Your code to do
                               Log.e("webViewError", error.toString())
                           }

                       }*/


                    var oneTime=true
                    holder.html_wv.setOnTouchListener(View.OnTouchListener { v, event ->

                        if(oneTime)
                        {
                            val intent = Intent(context, WebViewActivity::class.java)
                            intent.putExtra("singleSelection",filePath.absoluteFile.toString())
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            context!!.startActivity(intent)
                            oneTime =false

                            Handler(Looper.getMainLooper()).postDelayed({
                                oneTime = true
                            }, 500)
                        }

                        true
                    })

                    if(dbdata.favFile!!)
                    {
                        videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red));
                        videoView.fav_iv.setTag("set")
                    }
                    else
                    {
                        videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray));
                        videoView.fav_iv.setTag("not")
                    }

                }

            }


            videoView.download_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
               // downloadHtmlZip(zipmodel.filePath!!, position,zipmodel)
                newDownloadMethod(zipmodel,position,"ZIP")



            })

            videoView.reDownload_rl.setOnClickListener({
                if(!GeneralClass(context!!).isInternetAvailable())
                {
                    AlertClass(context!!).networkAlert()
                    return@setOnClickListener
                }
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
               // downloadHtmlZip(zipmodel.filePath!!, position,zipmodel)
                newDownloadMethod(zipmodel,position,"ZIP")

            })

            videoView.fav_iv.setOnClickListener({

                if(videoView.fav_iv.getTag().equals("set"))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.gray))
                    videoView.fav_iv.setTag("not")
                    db.updateFavouriteItem(zipmodel.fileId.toString(),0)

                    var downloadedModel=db.getSingleDownloadedData(zipmodel.fileId!!)
                    deleteAvailableFiles(downloadedModel.favFilePath)
                }
                else
                {
                    favAlertHTML(videoView,zipmodel,db)
                }
              //  checkIsItemIsFav(db)
            })
        }

    }

    override fun getItemCount(): Int {
        return arraylistVideo.size
    }

    fun newDownloadMethod(model: DownloadFileModel, position: Int, type: String)
    {
        if(GeneralClass(context as Activity).isInternetAvailable()==false) {
            AlertClass(context as Activity).networkAlert()
            return
        }

        val remainingurl: String = model.filePath?.replace("https://salestrip.blob.core.windows.net/", "").toString()

        val downloadService: APIInterface =
            createService(APIInterface::class.java, "https://salestrip.blob.core.windows.net/")

        val coroutineScope= CoroutineScope(Dispatchers.IO+GeneralClass(context!!).coroutineExceptionHandler).launch {
            try {
                val sendEdetailing= async {
                    val responseBody=downloadService.downloadFile(remainingurl).body()
                    responseBody?.let { it1 -> saveFile(it1,type,position,model,model.filePath!!) }
                }
                sendEdetailing.await()
            }
            catch (e:Exception)
            {
                AlertClass(context!!).lowNetworkAlert()
            }


        }
        coroutineScope.invokeOnCompletion {
             coroutineScope.cancel()
             context?.runOnUiThread { notifyDataSetChanged() }
        }
    }


    fun progressDialog()
    {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = context!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.percentprogress_alert, null)
        dialogBuilder.setCancelable(false);

        dialogBuilder.setView(dialogView)

        alertDialog= dialogBuilder.create()
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        progressBarAlert =
            dialogView.findViewById<View>(R.id.valueProgressBar) as ProgressBar

        textViewAlert =
            dialogView.findViewById<View>(R.id.progressNumber_tv) as TextView

        alertDialog!!.show()

    }

    fun getAllfileList(pathdirectoryIs:String,fileName:String) :Boolean
    {
        val yourDir = File(pathdirectoryIs)
        if(yourDir.listFiles()==null) { return false }

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

/*    fun downloadUrl(
        urlMain: String,
        position: Int,
        category: String,
        model: DownloadFileModel)
    {
        val extension: String = urlMain.substring(urlMain.lastIndexOf("/"))
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        Log.e("fhsiohfuisdfhidsfidf",urlMain)

        executor.execute(Runnable {
            var count: Int
            try {
               // val temp: String = URLEncoder.encode(urlMain, "UTF-8")

                val url = URL(urlMain)
                //establish connection
                val conection = url.openConnection()
                conection.connect()
                val lenghtOfFile = conection.contentLength
                val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                val output: OutputStream

                //create file path
              //  var folder = File(context!!.getExternalFilesDir(null)?.absolutePath + "/$brandName"+"/$category")
                var folder = File(context?.getFilesDir() , "/$brandName"+"/$category")

                try {
                    if (folder.mkdir()) {
                        println("Directorycreated")
                    } else {
                        println("Directoryisnotcreated")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e("firstCatch",e.message.toString())
                }

                var success = true
                if (!folder.exists()) {
                    success = folder.mkdirs()
                }
                if (success) {
                    // Output stream to write file
                    output = FileOutputStream(folder.getAbsolutePath() + extension)
                    val data = ByteArray(1024)
                    var total: Long = 0
                    while (input.read(data).also { count = it } != -1) {

                        total += count.toLong()

                        context!!.runOnUiThread(Runnable {
                            progressBarAlert?.setIndeterminate(false)
                            progressBarAlert?.setProgress(((total * 100 / lenghtOfFile).toInt()))
                            textViewAlert?.setText(((total * 100 / lenghtOfFile).toInt()).toString())
                        })

                        output.write(data, 0, count)
                    }
                    output.flush()
                    output.close()

                    var db = DatabaseHandler.getInstance(context!!)

                    var fileModel= DownloadFileModel()
                    fileModel.fileName=extension.replace("/","")
                    fileModel.fileDirectoryPath=folder.absolutePath
                    fileModel.filePath=folder.absolutePath+extension
                   // fileModel.model=model
                    fileModel.downloadType=category
                    fileModel.fileId=model.fileId!!
                    fileModel.brandId=parentId
                    fileModel.brandName=brandName

                    var downloadedModel=db.getSingleDownloadedData(model.fileId!!)

                    if(downloadedModel.favFile)
                    {
                        fileModel.favFilePath=downloadedModel.favFilePath
                        fileModel.favFileName=downloadedModel.favFileName
                    }

                    val gson = Gson()
                    db.insertOrUpdateEDetailDownload(eDetailingId!!.toInt(), model.fileId!!,  gson.toJson(fileModel),category)
                    db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                    deleteAndsaveRedownloads(db,model,downloadedModel)


                    context!!.runOnUiThread(Runnable {
                        //     progressBarAlert?.setIndeterminate(true)
                        alertDialog?.dismiss()
                        notifyItemChanged(position)
                    })
                } else {
                    Log.e("notSuccessfull","hsidhfidsh")
                    alertDialog?.dismiss()
                }
                input.close()
            } catch (e: Exception) {
                Log.e("Error:single ",Log.getStackTraceString(e)    )
                alertDialog?.dismiss()
            }})
    }*/

    fun saveFile(
        body: ResponseBody,
        type: String,
        position: Int,
        model: DownloadFileModel,
        urlMain: String
    ):String{
        val extension: String = urlMain.substring(urlMain.lastIndexOf("/"))
        var folder = File(context?.getFilesDir() , "/$brandName"+"/$type")
        if(!type.equals("ZIP"))
        {
             folder = File(context?.getFilesDir() , "/$brandName"+"/$type")
        }
        else
        {
             folder = File(context?.getExternalFilesDir(null)?.absolutePath + "/$brandName/$type")
        }

       //  val folder =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            folder.mkdirs();
            if (folder.mkdir()) {
                println("Directorycreated")
            } else {
                println("Directoryisnotcreated")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("firstCatch",e.message.toString())
            context?.alertClass?.commonAlert("Download error","Unable to download this file")
            alertDialog?.dismiss()

        }

        var success = true
        if (!folder.exists()) {
            success = folder.mkdirs()
        }
        if (body==null && success)
            return ""
        var input: InputStream? = null
        try {
            input = body?.byteStream()
            val lenghtOfFile =body?.contentLength()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(folder.getAbsolutePath() + extension)
            var total: Long = 0
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    total += read.toLong()

                    context!!.runOnUiThread(Runnable {
                        progressBarAlert?.setIndeterminate(false)
                        progressBarAlert?.setProgress(((total * 100 / lenghtOfFile).toInt()))
                        textViewAlert?.setText(((total * 100 / lenghtOfFile).toInt()).toString())
                    })
                    output.write(buffer, 0, read)
                }
                var db = DatabaseHandler.getInstance(context!!)

                var fileModel= DownloadFileModel()
                fileModel.fileName=extension.replace("/","")
                fileModel.fileDirectoryPath=folder.absolutePath
                fileModel.filePath=folder.absolutePath+extension
              //  fileModel.model=model
                fileModel.downloadType=type
                fileModel.fileId=model.fileId!!
                fileModel.brandId=parentId
                fileModel.brandName=brandName

                var downloadedModel=db.getSingleDownloadedData(model.fileId!!)

                if(downloadedModel.favFile)
                {
                    fileModel.favFilePath=downloadedModel.favFilePath
                    fileModel.favFileName=downloadedModel.favFileName
                }

                if(!type.equals("ZIP")){
                    val gson = Gson()
                    db.insertOrUpdateEDetailDownload(eDetailingId!!.toInt(), model.fileId!!,  gson.toJson(fileModel),type)
                    db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                    deleteAndsaveRedownloads(db,model,downloadedModel)
                }
                else{
                    val filePathzip=folder.getAbsolutePath() + extension;
                    //get main path
                    val zipName: String = filePathzip.substring(
                        filePathzip.lastIndexOf(
                            "/"
                        )
                    )
                    unpackZipmethod(folder.getAbsolutePath(), zipName,position,model)

                }

                context?.runOnUiThread(Runnable {
                    //     progressBarAlert?.setIndeterminate(true)
                    alertDialog?.dismiss()
                    (context as Activity).currentFocus?.clearFocus()
                })

                output.flush()
                Log.e("itsSucess","success")
            }
            return folder.getAbsolutePath() + "extension"
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
            context?.alertClass?.commonAlert("Download error","Unable to download this file")
            alertDialog?.dismiss()
        }
        finally {
            input?.close()
        }
        return ""
    }

    //download files
    fun downloadHtmlZip(
        urlMain: String,
        position: Int,
        zipmodel: DownloadFileModel)
    {

        val extension: String = urlMain.substring(urlMain.lastIndexOf("/"))
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute(Runnable {
            var count: Int
            try {
                val url = URL(urlMain)
                //establish connection
                val conection = url.openConnection()
                conection.connect()
                val lenghtOfFile = conection.contentLength
             //   val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                val output: OutputStream

                //create file path
                var folder = File(context?.getExternalFilesDir(null)?.absolutePath + "/zipFiles")
              //  folder = File(context?.getFilesDir()?.getAbsolutePath() + File.separator +"zipFiles")
                if (!folder.exists())
                    folder.mkdirs()
                try {
                    if (folder.mkdir()) {
                        println("Directorycreated")
                    } else {
                        println("Directoryisnotcreated")
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("gdfgdfgdre",e.message.toString())
                    e.printStackTrace()
                }

                var success = true
                if (!folder.exists()) {
                    success = folder.mkdirs()
                }
                if (success) {

                    // Output stream to write file
                    output = FileOutputStream(folder.getAbsolutePath() + extension)
                    val data = ByteArray(1024)
                    var total: Long = 0
                    while (input.read(data).also { count = it } != -1) {

                        total += count.toLong()

                        context!!.runOnUiThread(Runnable {
                            progressBarAlert?.setIndeterminate(false)
                            progressBarAlert?.setProgress(((total * 100 / lenghtOfFile).toInt()))
                            textViewAlert?.setText(((total * 100 / lenghtOfFile).toInt()).toString())
                        })

                        output.write(data, 0, count)
                    }
                    output.flush()
                    output.close()


                    val filePathzip=folder.getAbsolutePath() + extension;
                    //get main path
                    val zipName: String = filePathzip.substring(
                        filePathzip.lastIndexOf(
                            "/"
                        )
                    )
                    //break and get file name

                    context!!.runOnUiThread(Runnable {
                        progressBarAlert?.setIndeterminate(true)
                        alertDialog?.dismiss()
                    })

                    //unzip file
                    unpackZipmethod(folder.getAbsolutePath(), zipName,position,zipmodel)

                } else {
                    alertDialog?.dismiss()
                    Log.e("notSuccessfull","hisduhdf")
                }
                input.close()
            } catch (e: Exception) {
                Log.e("Error: ", e.message!!)
                alertDialog?.dismiss()
            }


        })
    }


    //unzip file path
    fun unpackZipmethod(path: String, zipname: String, position: Int, zipmodel: DownloadFileModel) {
        var htmlPath =""
        var isInput: InputStream? = null
        val zis: ZipInputStream
        var filename: String=" "
        try {

            try
            {
                isInput = FileInputStream(path + zipname)
            }
            catch (e: Exception)
            {
                Log.e("fileSaving", e.message!!)
            }
            zis = ZipInputStream(BufferedInputStream(isInput))
            var ze: ZipEntry? = null
            val buffer = ByteArray(1024)
            var count: Int

            var files: ArrayList<String> = ArrayList()

            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze!!.name

                //  create directories if not exists
              //  val fmd = File(context?.getFilesDir()?.getAbsolutePath() + File.separator+filename)
                val fmd = File("$path/$filename")
                if (ze!!.isDirectory) {

                    fmd.mkdirs()
                    continue
                }
                else
                {
                    fmd.getParentFile().mkdirs()
                }

                val extractFileName = fmd.absolutePath
                val lowercaseName = extractFileName.lowercase()

                //retrive html path
                if (lowercaseName.endsWith(".html"))
                {
                    htmlPath=lowercaseName
                }
                val fout = FileOutputStream(extractFileName)
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
            zis.close()
        }
        catch (e: IOException)
        {
            Log.e("zipException", e.message!!)
            alertDialog?.dismiss()
        }
        finally
        {
            //if html path not empty then send data to webview activity
            if(!htmlPath.isEmpty())
            {
                //insert file path in database
                val gson = Gson()
                context!!.runOnUiThread(Runnable {

                    var extension=filename.substring(filename.lastIndexOf("/"))

                    alertDialog?.dismiss()
                    var fileModel=DownloadFileModel()
                    fileModel.fileName=extension.replace("/","")
                    fileModel.fileDirectoryPath=path+"/"+filename.substring(0, filename.lastIndexOf('/'))
                    fileModel.filePath=htmlPath
                    //fileModel.model=zipmodel
                    fileModel.fileId=zipmodel.fileId!!
                    fileModel.downloadType="ZIP"
                    fileModel.brandId=parentId
                    fileModel.brandName=brandName

                    var db = DatabaseHandler.getInstance(context?.applicationContext)

                    var downloadedModel=db.getSingleDownloadedData(zipmodel.fileId!!)

                    if(downloadedModel.favFile)
                    {
                        fileModel.favFilePath=downloadedModel.favFilePath
                        fileModel.favFileName=downloadedModel.favFileName

                    }
                    val gson = Gson()
                    db.insertOrUpdateEDetailDownload(eDetailingId!!.toInt(), zipmodel.fileId!!,  gson.toJson(fileModel),"ZIP")
                    db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                    deleteAndsaveRedownloads(db, zipmodel, downloadedModel)

                    notifyItemChanged(position)

                })

                var folder = File(context?.getExternalFilesDir(null)?.absolutePath + "/zipFiles")

                val file = File(folder, zipname)
                val deleted = file.delete()
            }
            else{
                context?.alertClass?.commonAlert("Download error","Unable to download this file")
                alertDialog?.dismiss()
            }
        }

    }

    /*//Check is brand have fav items
    private fun checkIsItemIsFav(db: DatabaseHandler)
    {
        var eDeatilingList = db.getAllDownloadedData(eDetailingId.toInt())

        var isaddedtoFav=false
        for ((index, valuerand) in eDeatilingList?.withIndex()!!)
        {
            if(valuerand.favFile)
            {
                isaddedtoFav=true
            }
            if(index==eDeatilingList.size-1)
            {
                if(isaddedtoFav)
                {
                    db.updateFavourite(eDetailingId,1)
                }
                else
                {
                    db.updateFavourite(eDetailingId,0)
                }
            }
        }

    }*/


    //alert dialog for video views
    fun favAlertVideo(videoView: ViewHoldersVideo, videomodel: DownloadFileModel, db: DatabaseHandler)
    {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = context!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fav_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val favName_et = dialogView.findViewById<View>(R.id.favName_et) as EditText
        val save_btn = dialogView.findViewById<View>(R.id.save_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        save_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if(favName_et.text.toString().isEmpty())
                {
                    favName_et.requestFocus()
                    favName_et.setError("Required")
                    return
                }


                if(saveRenamedFiles(db,favName_et.text.toString(),videomodel))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red))
                    videoView.fav_iv.setTag("set")
                }
                alertDialog.dismiss()
                view?.let { context?.hideKeyboard(it) }

            }

        })

        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                view?.let { context?.hideKeyboard(it) }
                alertDialog.dismiss()
            }
        })
        alertDialog.show()
    }

    //alert dialog for image views
    fun favAlertImage(videoView: ViewHoldersImage, videomodel: DownloadFileModel, db: DatabaseHandler)
    {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = context!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fav_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val favName_et = dialogView.findViewById<View>(R.id.favName_et) as EditText
        val save_btn = dialogView.findViewById<View>(R.id.save_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        save_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if(favName_et.text.toString().isEmpty())
                {
                    favName_et.requestFocus()
                    favName_et.setError("Required")
                    return
                }

                if(saveRenamedFiles(db,favName_et.text.toString(),videomodel))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red))
                    videoView.fav_iv.setTag("set")
                }
                view?.let { context?.hideKeyboard(it) }

                alertDialog.dismiss()
            }

        })


        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                view?.let { context?.hideKeyboard(it) }

                alertDialog.dismiss()
            }

        })

        alertDialog.show()
    }

    //alert dialog for heml pages
    fun favAlertHTML(videoView: ViewHoldersWeb, videomodel: DownloadFileModel, db: DatabaseHandler)
    {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflater = context!!.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fav_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val favName_et = dialogView.findViewById<View>(R.id.favName_et) as EditText
        val save_btn = dialogView.findViewById<View>(R.id.save_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton


        save_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                if(favName_et.text.toString().isEmpty())
                {
                    favName_et.requestFocus()
                    favName_et.setError("Required")
                    return
                }

                if(saveRenamedFiles(db,favName_et.text.toString(),videomodel))
                {
                    videoView.fav_iv.setColorFilter(ContextCompat.getColor(context!!, R.color.zm_red))
                    videoView.fav_iv.setTag("set")
                }

                view?.let { context?.hideKeyboard(it) }

                alertDialog.dismiss()
            }

        })


        cancel_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                view?.let { context?.hideKeyboard(it) }

                alertDialog.dismiss()
            }

        })

        alertDialog.show()
    }


    //save and rename files method
    fun saveRenamedFiles(db: DatabaseHandler, filename: String, videomodel: DownloadFileModel):Boolean
    {

        var folder = File(context!!.getExternalFilesDir(null)?.absolutePath + "/$brandName"+"/favrouteFolder")

        try {
            if (folder.mkdir()) {
            }

        } catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }

        if (!folder.exists()) {
            folder.mkdirs()
        }

        var downloadedModel=db.getSingleDownloadedData(videomodel.fileId!!)

        val sourcePath = downloadedModel.filePath

        val extension= downloadedModel.filePath.substring(downloadedModel.filePath.lastIndexOf(".")); // Extension with dot .jpg, .png
        val directoryPath: String = File(downloadedModel.filePath).getParent()

        val source = File(sourcePath)

        val destinationPath =  directoryPath+"/"+filename+extension
        val destination = File(destinationPath)
        try
        {
            FileUtils.copyFile(source, destination)
            downloadedModel.favFilePath=destination.absoluteFile.toString()
            downloadedModel.favFileName=filename
            db.updateFavouriteItem(videomodel.fileId.toString(),1)

            val gson = Gson()
            db.updateFavouriteItemWithModel(videomodel.fileId.toString(),1, gson.toJson(downloadedModel))
            Toast.makeText(context!!,"Add to Favorite sucessfully",Toast.LENGTH_LONG).show()
            return true
        }
        catch (e: IOException)
        {
            e.printStackTrace()
            Log.e("destinationPathError",e.message.toString())
            Toast.makeText(context!!,"System error",Toast.LENGTH_LONG).show()
            return false
        }
    }


    //deleted already added files from internal strorge
    fun deleteAvailableFiles(path: String)
    {
        val fdelete = File(path)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + path)
            } else {
                System.out.println("file not Deleted :" + path)
            }
        }
    }

    fun deleteAndsaveRedownloads(db: DatabaseHandler, videomodel: DownloadFileModel, downloadedModel: DownloadFileModel)
    {
        if(downloadedModel.favFile)
        {
            val sourcePath = downloadedModel.filePath
            val source = File(sourcePath)

            val fdelete = File(downloadedModel.favFilePath)
            if (fdelete.exists()) {
                if (fdelete.delete())
                {
                    val destinationPath =  downloadedModel.favFilePath
                    val destination = File(destinationPath)
                    try
                    {
                        FileUtils.copyFile(source, destination)
                        Log.e("createAgain","againagain")
                    }
                    catch (e: IOException)
                    {
                        e.printStackTrace()
                        Log.e("destinationPathError",e.message.toString())
                    }
                }
                else
                {

                }
            }
        }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun <T> createService(serviceClass: Class<T>?, baseUrl: String?): T {

        val client = OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        return retrofit.create(serviceClass)
    }


}
