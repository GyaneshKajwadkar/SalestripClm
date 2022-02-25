package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.DownloadAdapter
import `in`.processmaster.salestripclm.models.DownloadEdetail_model
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_downloaded_activtiy.*
import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class DownloadedActivtiy : BaseActivity() {

    var progressBarAlert: ProgressBar?=null
    var textViewAlert:TextView?=null
    var downloadItem_tv:TextView?=null

    var adapter : DownloadAdapter?=null

    var db = DatabaseHandler(this)

    var arrayList: ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()

    var brandName =""
    var eDetailingId =""

    var adapterVideo =DownloadAdapter()
    var adapterImage =DownloadAdapter()
    var adapterWeb =DownloadAdapter()

    var brandId =0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloaded_activtiy)
        initView()
    }

    fun initView()
    {
        back_imv!!.setOnClickListener {
            onBackPressed()
        }

        downloadAll_ll?.setOnClickListener({
            if(arrayList.size!=0)
            {
                progressDialog()
                progressBarAlert?.setIndeterminate(true)
                downloadAll(0, arrayList)
            }
        })

        brandName = intent.getStringExtra("brandName").toString()
        brandId = intent.getIntExtra("brandId", 0)
        eDetailingId = intent.getStringExtra("eDetailingId").toString()

        val args = intent.getBundleExtra("BUNDLE");
        arrayList = args?.getSerializable("ARRAYLIST") as (ArrayList<DownloadEdetail_model.Data.EDetailingImages>)

        var arraylistVideo:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
        var arraylistImages:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()
        var arraylistZip:ArrayList<DownloadEdetail_model.Data.EDetailingImages> = ArrayList()

        for ((index, value) in arrayList?.withIndex()!!)
        {
            if(value.fileType=="VIDEO")
            {
                arraylistVideo.add(value)
            }
            else if(value.fileType=="IMAGE")
            {
                arraylistImages.add(value)
            }
            else if(value.fileType=="ZIP")
            {
                arraylistZip.add(value)
            }
        }

        downloadAllVideos_ll?.setOnClickListener({
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            downloadSingleFolders(0,arraylistVideo)
        })
        downloadAllImages_ll?.setOnClickListener({
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            downloadSingleFolders(0,arraylistImages)
        })
        downloadAllWeb_ll?.setOnClickListener({
            progressDialog()
            progressBarAlert?.setIndeterminate(true)
            downloadSingleFolders(0,arraylistZip)
        })

        toolbarText_tv?.setText(brandName)

        val mNoOfColumns = Utility.calculateNoOfColumns(this, 197F)

        adapterVideo= DownloadAdapter(this, "VIDEO", brandId,brandName,arraylistVideo,eDetailingId)
        video_rv!!.layoutManager = GridLayoutManager(this, mNoOfColumns)
        video_rv?.itemAnimator = DefaultItemAnimator()
        video_rv?.adapter = adapterVideo

        adapterImage= DownloadAdapter(this, "IMAGE", brandId,brandName,arraylistImages,eDetailingId)
        images_rv!!.layoutManager = GridLayoutManager(this, mNoOfColumns)
        images_rv?.itemAnimator = DefaultItemAnimator()
        images_rv?.adapter = adapterImage

        adapterWeb= DownloadAdapter(this, "ZIP", brandId,brandName,arraylistZip,eDetailingId)
        html_rv!!.layoutManager = GridLayoutManager(this, mNoOfColumns)
        html_rv?.itemAnimator = DefaultItemAnimator()
        html_rv?.adapter = adapterWeb

        if(arraylistVideo.size==0)
        {
            videoView_parent?.visibility=View.GONE
        }
        if(arraylistImages.size==0)
        {
            images_parent?.visibility=View.GONE
        }
        if(arraylistZip.size==0)
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

    //for dynamic grid view set no of column according to screen
    object Utility {
        fun calculateNoOfColumns(context: Context, columnWidthDp: Float): Int { // For example columnWidthdp=180
            val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }

    fun downloadAll(
        index: Int,
        arrayListtype: ArrayList<DownloadEdetail_model.Data.EDetailingImages>
    )
    {

        this.runOnUiThread(Runnable {
            downloadItem_tv?.setText("${index + 1} / ${arrayList.size}")
        })


        var modelObject = arrayList.get(index)
        downloadUrl(modelObject.filePath.toString(),index, modelObject.fileType.toString(),modelObject,arrayList,"all")

    }

    fun downloadSingleFolders(index: Int, downloadList : ArrayList<DownloadEdetail_model.Data.EDetailingImages>)
    {
        this.runOnUiThread(Runnable {
            downloadItem_tv?.setText("${index + 1} / ${downloadList.size}")

        })

        var modelObject = downloadList.get(index)
        downloadUrl(modelObject.filePath.toString(),index, modelObject.fileType.toString(),modelObject,downloadList,"single")

    }


    fun progressDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.downloadallprogress_alert, null)
        dialogBuilder.setCancelable(false);

        dialogBuilder.setView(dialogView)

        alertDialog= dialogBuilder.create()
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        progressBarAlert =
            dialogView.findViewById<View>(R.id.valueProgressBar) as ProgressBar

        textViewAlert = dialogView.findViewById<View>(R.id.progressNumber_tv) as TextView

        downloadItem_tv = dialogView.findViewById<View>(R.id.downloadItem_tv) as TextView

        alertDialog!!.show()

    }


    fun downloadUrl(
        urlMain: String,
        position: Int,
        category: String,
        model: DownloadEdetail_model.Data.EDetailingImages,
        arrayListtype: ArrayList<DownloadEdetail_model.Data.EDetailingImages>,
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
                var folder = File(this.getExternalFilesDir(null)?.absolutePath + "/$brandName"+"/$category")

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
                        fileModel.model=model
                        fileModel.downloadType=category
                        fileModel.fileId=model.fileId!!
                        fileModel.brandId=brandId
                        fileModel.brandName=brandName


                        var downloadedModel=db.getSingleDownloadedData(model.fileId!!)

                        if(downloadedModel.isFavFile)
                        {
                            fileModel.favFilePath=downloadedModel.favFilePath
                            fileModel.favFileName=downloadedModel.favFileName
                        }


                        val gson = Gson()
                        db.insertOrUpdateEDetailDownload(eDetailingId!!.toInt(), model.fileId!!,  gson.toJson(fileModel),category)
                        db.insertFilePath(1,  gson.toJson(fileModel), eDetailingId.toString())
                        deleteAndsaveRedownloads(db,model,downloadedModel)

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
        zipmodel: DownloadEdetail_model.Data.EDetailingImages,
        downloadType: String,
        arrayListtype: ArrayList<DownloadEdetail_model.Data.EDetailingImages>
    ) {
        var htmlPath =""
        var isInput: InputStream? = null
        val zis: ZipInputStream
        var filename: String=" "
        try {

            try {
                isInput = FileInputStream(path + zipname)
            } catch (e: Exception) {
                Log.e("fileSaving", e.message!!)
            }
            zis = ZipInputStream(BufferedInputStream(isInput))
            var ze: ZipEntry? = null
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze!!.name

                //  create directories if not exists
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
                val lowercaseName = extractFileName.toLowerCase()

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
            Log.e("zipException", e.message!!)
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
                    fileModel.model=zipmodel
                    fileModel.downloadType="ZIP"
                    fileModel.fileId=zipmodel.fileId!!
                    fileModel.brandId=brandId
                    fileModel.brandName=brandName

                    var downloadedModel=db.getSingleDownloadedData(zipmodel.fileId!!)

                    if(downloadedModel.isFavFile)
                    {
                        fileModel.favFilePath=downloadedModel.favFilePath
                        fileModel.favFileName=downloadedModel.favFileName

                    }


                    val gson = Gson()
                    db.insertOrUpdateEDetailDownload(eDetailingId!!.toInt(), zipmodel.fileId!!,  gson.toJson(fileModel),"ZIP")
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
        }

    }

    fun deleteAndsaveRedownloads(db: DatabaseHandler, videomodel: DownloadEdetail_model.Data.EDetailingImages, downloadedModel: DownloadFileModel)
    {
        if(downloadedModel.isFavFile)
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
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }
}