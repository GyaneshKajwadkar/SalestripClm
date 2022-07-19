package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.DownloadAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_downloaded_activtiy.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.apache.commons.io.FileUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class DownloadedActivtiy : BaseActivity() {

    var progressBarAlert: ProgressBar?=null
    var textViewAlert:TextView?=null
    var downloadItem_tv:TextView?=null

    var adapter : DownloadAdapter?=null

    var db = DatabaseHandler.getInstance(this)

   // var arrayList: ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
    var arrayListD: ArrayList<DownloadFileModel> = ArrayList()

    var brandName =""
    var eDetailingId =""

    var adapterVideo =DownloadAdapter()
    var adapterImage =DownloadAdapter()
    var adapterWeb =DownloadAdapter()

   // var arraylistVideo:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
    var arraylistVideoD:ArrayList<DownloadFileModel> = ArrayList()
   // var arraylistImages:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
    var arraylistImagesD:ArrayList<DownloadFileModel> = ArrayList()
   // var arraylistZip:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
    var arraylistZipD:ArrayList<DownloadFileModel> = ArrayList()

    var brandId =0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloaded_activtiy)
        initView()
    }

    fun initView()
    {
        back_imv?.setOnClickListener {
            onBackPressed()
        }

        downloadAll_ll?.setOnClickListener({
            if(arrayListD.size!=0)
            {
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
                downloadAll(0, arrayListD)
            }
        })

        brandName = intent.getStringExtra("brandName").toString()
        brandId = intent.getIntExtra("brandId", 0)
        eDetailingId = intent.getStringExtra("eDetailingId").toString()

        val args = intent.getBundleExtra("BUNDLE");
        arrayListD = args?.getSerializable("ARRAYLIST") as (ArrayList<DownloadFileModel>)

        for ((index, value) in arrayListD?.withIndex())
        {
            if(value.downloadType=="VIDEO")
            {
                arraylistVideoD.add(value)
            }
            else if(value.downloadType=="IMAGE")
            {
                arraylistImagesD.add(value)
            }
            else if(value.downloadType=="ZIP")
            {
                arraylistZipD.add(value)
            }
        }

        downloadAllVideos_ll?.setOnClickListener({
            if(!generalClass.isInternetAvailable())
            {
                alertClass.networkAlert()
                return@setOnClickListener
            }
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            downloadSingleFolders(0,arraylistVideoD)
        })
        downloadAllImages_ll?.setOnClickListener({
            if(!generalClass.isInternetAvailable())
            {
                alertClass.networkAlert()
                return@setOnClickListener
            }
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            downloadSingleFolders(0,arraylistImagesD)
        })
        downloadAllWeb_ll?.setOnClickListener({
            if(!generalClass.isInternetAvailable())
            {
                alertClass.networkAlert()
                return@setOnClickListener
            }
            progressDialog()
            progressBarAlert?.setIndeterminate(true)


            downloadSingleFolders(0,arraylistZipD)
        })

        downloadAll_ll?.setOnClickListener({
            if(!generalClass.isInternetAvailable())
            {
                alertClass.networkAlert()
                return@setOnClickListener
            }
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            var addAddDownloaded:ArrayList<DownloadFileModel> = ArrayList()
            addAddDownloaded.addAll(arraylistVideoD)
            addAddDownloaded.addAll(arraylistImagesD)
            addAddDownloaded.addAll(arraylistZipD)

            downloadSingleFolders(0,addAddDownloaded)
        })

        toolbarText_tv?.setText(brandName)

        adapterVideo= DownloadAdapter(this, "VIDEO", brandId,brandName,arraylistVideoD,eDetailingId)
        video_rv?.layoutManager = GridLayoutManager(this, 5)
        video_rv?.itemAnimator = DefaultItemAnimator()
        video_rv?.adapter = adapterVideo

        adapterImage= DownloadAdapter(this, "IMAGE", brandId,brandName,arraylistImagesD,eDetailingId)
        images_rv?.layoutManager = GridLayoutManager(this, 5)
        images_rv?.itemAnimator = DefaultItemAnimator()
        images_rv?.adapter = adapterImage

        adapterWeb= DownloadAdapter(this, "ZIP", brandId,brandName,arraylistZipD,eDetailingId)
        html_rv?.layoutManager = GridLayoutManager(this, 5)
        html_rv?.itemAnimator = DefaultItemAnimator()
        html_rv?.adapter = adapterWeb


        if(arraylistVideoD.size==0)
        {
            videoView_parent?.visibility=View.GONE
        }
        if(arraylistImagesD.size==0)
        {
            images_parent?.visibility=View.GONE
        }
        if(arraylistZipD.size==0)
        {
            html_parent?.visibility=View.GONE
        }

        /*   val intent = intent
           var eDetailId = intent.extras!!.getString("eDetailId")

           //get downloaded file paths from db
           var extractMainModel= db.getDownloadedSingleData(eDetailId)

           //convert string to array list
           subArray = Gson().fromJson(extractMainModel, object : TypeToken<List<DownloadFileModel?>?>() {}.type)
           adapter = DownloadAdapter(subArray, this)

           //set recycler view
           val mNoOfColumns = Utility.calculateNoOfColumns(this, 180F)
           recyclerView!!.layoutManager = GridLayoutManager(this, mNoOfColumns)
           recyclerView?.itemAnimator = DefaultItemAnimator()
           recyclerView?.adapter = adapter*/
    }

    fun downloadAll(
        index: Int,
        arrayListtype: ArrayList<DownloadFileModel>
    )
    {

        this.runOnUiThread(Runnable {
            downloadItem_tv?.setText("${index + 1} / ${arrayListD.size}")
        })


        var modelObject = arrayListD.get(index)
       // downloadUrl(modelObject.filePath.toString(),index, modelObject.downloadType.toString(),modelObject,arrayListD,"all")

        newDownloadMethod(modelObject,index,modelObject.downloadType,arrayListD,"all")
    }

    fun downloadSingleFolders(index: Int, downloadList : ArrayList<DownloadFileModel>)
    {
        this.runOnUiThread(Runnable {
            downloadItem_tv?.setText("${index + 1} / ${downloadList.size}")
        })

        var modelObject = downloadList.get(index)
       // downloadUrl(modelObject.filePath.toString(),index, modelObject.downloadType.toString(),modelObject,downloadList,"single")
        newDownloadMethod(modelObject,index,modelObject.downloadType,downloadList,"single")
    }


    fun progressDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.downloadallprogress_alert, null)
        dialogBuilder.setCancelable(false)

        dialogBuilder.setView(dialogView)

        alertDialog= dialogBuilder.create()
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        progressBarAlert =
            dialogView.findViewById<View>(R.id.valueProgressBar) as ProgressBar

        textViewAlert = dialogView.findViewById<View>(R.id.progressNumber_tv) as TextView

        downloadItem_tv = dialogView.findViewById<View>(R.id.downloadItem_tv) as TextView

        alertDialog?.show()

    }


    fun downloadUrl(
        urlMain: String,
        position: Int,
        category: String,
        model: DownloadFileModel,
        arrayListtype: ArrayList<DownloadFileModel>,
        downloadType: String
    )
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
                val input: InputStream = BufferedInputStream(url.openStream(), 8192)
                val output: OutputStream

                //create file path
               // var folder = File(this.getExternalFilesDir(null)?.absolutePath + "/$brandName"+"/$category")
                var folder = File(this@DownloadedActivtiy.getFilesDir() , "/$brandName"+"/$category")
                if(category.equals("ZIP"))
                {
                     folder = File(getExternalFilesDir(null)?.absolutePath + "/zipFiles")
                }

                try {
                    if (folder.mkdir()) {
                        println("Directorycreated")
                    } else {
                        println("Directoryisnotcreated")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e("downloadFirstError",e.message.toString())
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

                        this.runOnUiThread(Runnable {
                            progressBarAlert?.setIndeterminate(false)
                            progressBarAlert?.setProgress(((total * 100 / lenghtOfFile).toInt()))
                            textViewAlert?.setText(((total * 100 / lenghtOfFile).toInt()).toString())
                        })

                        output.write(data, 0, count)
                    }
                    output.flush()
                    output.close()

                    if(!category.equals("ZIP"))
                    {
                        var fileModel= DownloadFileModel()
                        fileModel.fileName=extension.replace("/","")
                        fileModel.fileDirectoryPath=folder.absolutePath
                        fileModel.filePath=folder.absolutePath+extension
                     //   fileModel.model=model
                        fileModel.downloadType=category
                        fileModel.fileId= model.fileId
                        fileModel.brandId=brandId
                        fileModel.brandName=brandName


                        /*var downloadedModel=db.getSingleDownloadedData(model.fileId)

                        if(downloadedModel.favFile)
                        {
                            fileModel.favFilePath=downloadedModel.favFilePath
                            fileModel.favFileName=downloadedModel.favFileName
                        }*/


                        val gson = Gson()
                        db.insertOrUpdateEDetailDownload(eDetailingId.toInt(), model.fileId,  gson.toJson(fileModel),category)
                        db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                      //  deleteAndsaveRedownloads(db,model,downloadedModel)

                        if(position==arrayListtype.size-1)
                        {
                            this.runOnUiThread(Runnable {
                                //     progressBarAlert?.setIndeterminate(true)
                                alertDialog?.dismiss()
                                adapterVideo.notifyDataSetChanged()
                                adapterImage.notifyDataSetChanged()
                                adapterWeb.notifyDataSetChanged()
                            })
                        }
                        else
                        {
                            if(downloadType.equals("all"))
                            {
                                downloadAll(position+1,arrayListtype)
                            }
                            else
                            {
                                downloadSingleFolders(position+1,arrayListtype)
                            }

                        }



                    }
                    else
                    {
                        val filePathzip=folder.getAbsolutePath() + extension;

                        //get main path
                        val zipName: String = filePathzip.substring(
                            filePathzip.lastIndexOf(
                                "/"
                            )
                        )
                        //break and get file name

                        this.runOnUiThread(Runnable {
                            progressBarAlert?.setIndeterminate(true)
                        })

                        //unzip file
                        unpackZipmethod(folder.getAbsolutePath(), zipName,position,model,downloadType,arrayListtype)
                    }

                } else {
                    this.runOnUiThread(Runnable {
                        alertDialog?.dismiss()
                    })
                }
                input.close()
            } catch (e: Exception) {
                Log.e("ErrorEnding: ", e.message.toString())
                this.runOnUiThread(Runnable {
                    alertDialog?.dismiss()
                })
            }


        })
    }

    //unzip file path
    fun unpackZipmethod(
        path: String,
        zipname: String,
        position: Int,
        zipmodel: DownloadFileModel,
        downloadType: String,
        arrayListtype: ArrayList<DownloadFileModel>
    ) {
        var htmlPath =""
        var isInput: InputStream? = null
        val zis: ZipInputStream
        var filename: String=" "
        try {

            try {
                isInput = FileInputStream(path + zipname)
            } catch (e: Exception) {
                e.message?.let { Log.e("fileSaving", it) }
            }
            zis = ZipInputStream(BufferedInputStream(isInput))
            var ze: ZipEntry? = null
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze?.name.toString()

                //  create directories if not exists
                val fmd = File("$path/$filename")

                if (ze?.isDirectory == true) {
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
                val fout = FileOutputStream("$path/$filename")
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
            zis.close()
        }
        catch (e: IOException) {
            e.message?.let { Log.e("zipException", it) }
            this.runOnUiThread(Runnable {
                alertDialog?.dismiss()
            })
        }
        finally {
            //if html path not empty then send data to webview activity

            if(!htmlPath.isEmpty())
            {

                //insert file path in database
                this.runOnUiThread(Runnable {

                    var extension=filename.substring(filename.lastIndexOf("/"))

                    var fileModel=DownloadFileModel()
                    fileModel.fileName=extension.replace("/","")
                    fileModel.fileDirectoryPath=path+"/"+filename.substring(0, filename.lastIndexOf('/'))
                    fileModel.filePath=htmlPath
                   // fileModel.model=zipmodel
                    fileModel.downloadType="ZIP"
                    fileModel.fileId=zipmodel.fileId
                    fileModel.brandId=brandId
                    fileModel.brandName=brandName

                    var downloadedModel=db.getSingleDownloadedData(zipmodel.fileId)

                    if(downloadedModel.favFile)
                    {
                        fileModel.favFilePath=downloadedModel.favFilePath
                        fileModel.favFileName=downloadedModel.favFileName

                    }


                    val gson = Gson()
                    db.insertOrUpdateEDetailDownload(eDetailingId.toInt(), zipmodel.fileId,  gson.toJson(fileModel),"ZIP")
                    db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                    deleteAndsaveRedownloads(db,zipmodel,downloadedModel)


                    if(position==arrayListtype.size-1)
                    {
                        this.runOnUiThread(Runnable {
                            //     progressBarAlert?.setIndeterminate(true)
                            alertDialog?.dismiss()
                            adapterVideo.notifyDataSetChanged()
                            adapterImage.notifyDataSetChanged()
                            adapterWeb.notifyDataSetChanged()
                        })
                    }
                    else
                    {
                        if(downloadType.equals("all"))
                        {
                            downloadAll(position+1, arrayListtype)
                        }
                        else
                        {
                            downloadSingleFolders(position+1,arrayListtype)
                        }
                    }

                })
            }
            else{
                alertClass?.commonAlert("Download error","Unable to download this file")
                alertDialog?.dismiss()
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

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
        alertClass = AlertClass(this)

    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }


    inner class MyScrollListener : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
            if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == p1) {
                val currentFocus= (this as Activity).getCurrentFocus()
                if (currentFocus != null) {
                    currentFocus.clearFocus()
                }
            }
        }

        override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {

        }

    }

    fun notifyDataChange(type:String, position: Int)
    {
        if(type=="IMAGE")
        {
            runOnUiThread {
                adapterImage= DownloadAdapter(this, "IMAGE", brandId,brandName,arraylistVideoD,eDetailingId)
                images_rv?.layoutManager = GridLayoutManager(this, 5)
                images_rv?.itemAnimator = DefaultItemAnimator()
                images_rv?.adapter = adapterImage
            }
        }
        else
        {
            runOnUiThread {
                adapterVideo= DownloadAdapter(this, "VIDEO", brandId,brandName,arraylistVideoD,eDetailingId)
                video_rv?.layoutManager = GridLayoutManager(this, 5)
                video_rv?.itemAnimator = DefaultItemAnimator()
                video_rv?.adapter = adapterVideo
            }
        }


    }

    fun newDownloadMethod(model: DownloadFileModel, position: Int, type: String , arrayListtype: ArrayList<DownloadFileModel>, downloadType: String)
    {
        if(generalClass.isInternetAvailable()==false) {
            alertClass.networkAlert()
            return
        }

        val remainingurl: String = model.filePath?.replace("https://salestrip.blob.core.windows.net/", "").toString()

        val downloadService: APIInterface =
            createService(APIInterface::class.java, "https://salestrip.blob.core.windows.net/")

        val coroutineScope= CoroutineScope(Dispatchers.IO+generalClass.coroutineExceptionHandler).launch {

         try {
             val sendEdetailing= async {
                 val responseBody=downloadService.downloadFile(remainingurl).body()
                 responseBody?.let { it1 -> saveFile(it1,type,position,model,model.filePath!!,arrayListtype,downloadType) }
             }
             sendEdetailing.await()
         }
         catch (e:Exception)
         {
             runOnUiThread { alertClass.lowNetworkAlert() }
             alertClass?.commonAlert("Download error","Unable to download this file")
             alertDialog?.dismiss()
         }
        }
        coroutineScope.invokeOnCompletion {
            coroutineScope.cancel()

            if(arrayListtype.size-1==position)
            {
                runOnUiThread { if(type.equals("IMAGE")) {
                    adapterImage.notifyDataSetChanged()
                }
                else if(type.equals("VIDEO"))
                {
                    adapterVideo.notifyDataSetChanged()
                }
                else
                {
                    adapterWeb.notifyDataSetChanged()
                }
                }
            }
        }
    }


    fun saveFile(
        body: ResponseBody,
        type: String,
        position: Int,
        model: DownloadFileModel,
        urlMain: String,
        arrayListtype: ArrayList<DownloadFileModel>,
        downloadType: String
    ):String{
        val extension: String = urlMain.substring(urlMain.lastIndexOf("/"))
        var folder = File(getFilesDir() , "/$brandName"+"/$type")
        if(!type.equals("ZIP"))
        {
            folder = File(getFilesDir() , "/$brandName"+"/$type")
        }
        else
        {
            folder = File(getExternalFilesDir(null)?.absolutePath + "/$brandName/$type")
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
            alertClass?.commonAlert("Download error","Unable to download this file")
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

                    runOnUiThread(Runnable {
                        progressBarAlert?.setIndeterminate(false)
                        progressBarAlert?.setProgress(((total * 100 / lenghtOfFile).toInt()))
                        textViewAlert?.setText(((total * 100 / lenghtOfFile).toInt()).toString())
                    })
                    output.write(buffer, 0, read)
                }

                var fileModel= DownloadFileModel()
                fileModel.fileName=extension.replace("/","")
                fileModel.fileDirectoryPath=folder.absolutePath
                fileModel.filePath=folder.absolutePath+extension
                //  fileModel.model=model
                fileModel.downloadType=type
                fileModel.fileId=model.fileId!!
                fileModel.brandId=brandId
                fileModel.brandName=brandName

                var downloadedModel=db.getSingleDownloadedData(model.fileId!!)

                /*if(downloadedModel.favFile)
                {
                    fileModel.favFilePath=downloadedModel.favFilePath
                    fileModel.favFileName=downloadedModel.favFileName
                }*/

                if(!type.equals("ZIP")){

                    if(position==arrayListtype.size-1)
                    {
                        this.runOnUiThread(Runnable {
                            //     progressBarAlert?.setIndeterminate(true)
                            alertDialog?.dismiss()
                            adapterVideo.notifyDataSetChanged()
                            adapterImage.notifyDataSetChanged()
                            adapterWeb.notifyDataSetChanged()
                        })
                    }
                    else
                    {
                        if(downloadType.equals("all"))
                        {
                            downloadAll(position+1,arrayListtype)
                        }
                        else
                        {
                            downloadSingleFolders(position+1,arrayListtype)
                        }

                    }



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
                    unpackZipmethod(folder.getAbsolutePath(), zipName,position,model,type,arrayListtype)

                }

                runOnUiThread(Runnable {
                    //     progressBarAlert?.setIndeterminate(true)
                  //  alertDialog?.dismiss()
                  //  this.currentFocus?.clearFocus()
                })

                output.flush()
                Log.e("itsSucess","success")
            }
            return folder.getAbsolutePath() + "extension"
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
            alertClass?.commonAlert("Download error","Unable to download this file")
            alertDialog?.dismiss()
        }
        finally {
            input?.close()
        }
        return ""
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