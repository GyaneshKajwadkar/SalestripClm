package `in`.processmaster.salestripclm.utils

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.net.URLDecoder
import java.nio.channels.FileChannel
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class DownloadManagerClass(
    val context: HomePage,
    val dataBase: DatabaseHandler,
    val getAlleDetailListDb: java.util.ArrayList<DevisionModel.Data.EDetailing>
) {

    var allProductList: ArrayList<DownloadFileModel> = ArrayList()
    var getNumber=0
    lateinit var downloadManager :DownloadManager
    lateinit var selectedObj :DownloadFileModel
    lateinit var downloadNumber_tv : TextView
    var downloadId: Long =0

    companion object {
        var cancelAutoDownload= false
    }

    fun startDownloading()
    {
        if( cancelAutoDownload==true) return

        if(getNumber==0)
        {
            context.registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            for(brand in getAlleDetailListDb)
            {
                for(iteams in brand.eretailDetailList){
                    if(iteams.fileType.isEmpty()) continue

                    var modelClass=DownloadFileModel()
                    modelClass.eDetailingId=iteams.geteDetailId()
                    modelClass.fileId=iteams.fileId
                    modelClass.fileName=iteams.fileName
                    modelClass.filePath=iteams.filePath
                    modelClass.downloadType=iteams.fileType
                   // modelClass.model=iteams
                    modelClass.brandId=brand.brandId
                    modelClass.brandName=brand.brandName
                    allProductList.add(modelClass)
                }
            }
        }
        if(allProductList.size!=0 && getNumber<allProductList.size)
        {
            selectedObj= allProductList.get(getNumber)
            if(context.stopDownload==true)return
            downloadFile(selectedObj.filePath,selectedObj.fileName,selectedObj.downloadType)
        }
    }

    private fun downloadFile(url: String, name: String, fileType: String){
        val setMime = if(fileType=="IMAGE")"image/*" else if(fileType=="VIDEO")"video/*" else "zip"
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(name)
            .setDescription("desc")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setMimeType(setMime)
            .setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,name)
            downloadManager= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
    }

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                CheckDwnloadStatus(downloadId)
            }
        }
    }

    @SuppressLint("Range")
    private fun CheckDwnloadStatus(id: Long) {

        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor
                .getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status: Int = cursor.getInt(columnIndex)
            val columnReason: Int = cursor
                .getColumnIndex(DownloadManager.COLUMN_REASON)
            val reason: Int = cursor.getInt(columnReason)
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    val downloadFilePath: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)).replace("file://", "")
                    val downloadTitle: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val decoded: String = URLDecoder.decode(downloadFilePath, "UTF-8")
                    writeFileOnInternalStorage(decoded,downloadTitle,cursor)
                }
                else ->{
                    getNumber++
                    startDownloading()
                }
            }
        }
    }

    private fun writeFileOnInternalStorage(inputFile: String, title: String?, cursor: Cursor) {
        val coroutineScope= CoroutineScope(Dispatchers.IO).launch {
            val cutFile= async {

                var folder=File("")
                if(selectedObj.downloadType!="ZIP") folder = File(context?.getFilesDir() , "/"+selectedObj.brandName+"/"+selectedObj.downloadType)
                else folder = File(context?.getExternalFilesDir(null)?.absolutePath , "/"+selectedObj.brandName+"/"+selectedObj.downloadType)

                var iny: FileChannel? = null
                var out: FileChannel ? = null
                try {

                    folder.mkdirs()
                    if (folder.mkdir()) { println("Directorycreated") }
                    else { println("Directoryisnotcreated") }

                    iny = FileInputStream(inputFile).getChannel()
                    out = FileOutputStream(folder.absoluteFile.toString()+"/"+title).getChannel()
                    out.transferFrom(iny, 0, iny.size());
                    out.close()
                    out = null

                    if(selectedObj.downloadType=="ZIP")
                    {
                      unpackZipmethod(inputFile,folder,title)
                    }
                    else
                    {
                        selectedObj.fileName= title.toString()
                        selectedObj.fileDirectoryPath=folder.absolutePath
                        selectedObj.filePath=folder.absolutePath+"/"+title
                        val gson = Gson()
                        dataBase.insertOrUpdateEDetailDownload(selectedObj.eDetailingId?.toInt(), selectedObj.fileId!!,  gson.toJson(selectedObj),selectedObj.downloadType)
                        dataBase.insertFilePath(1,  gson.toJson(selectedObj), selectedObj.eDetailingId.toString())
                    }

                } catch (fnfe1: FileNotFoundException) {
                    Log.e("tagABCD", fnfe1.message.toString())
                } catch (e: java.lang.Exception) {
                    Log.e("tagABVRDF", e.message!!)
                }
            }
            cutFile.await()
        }
        coroutineScope.invokeOnCompletion {
            cursor.close()
            File(inputFile).delete()
            getNumber++
            context as Activity
            context.runOnUiThread{ if(::downloadNumber_tv.isInitialized) downloadNumber_tv.setText(getNumber.toString()+"/"+allProductList.size.toString()) }
            startDownloading()
        }
    }

    fun unpackZipmethod(path: String, folder: File, title: String?) {
        var htmlPath =""
        var isInput: InputStream? = null
        val zis: ZipInputStream
        var filename: String=" "


        try {
            isInput = FileInputStream(folder.absoluteFile.toString() +"/"+ title)

            zis = ZipInputStream(BufferedInputStream(isInput))
            var ze: ZipEntry? = null
            val buffer = ByteArray(1024)
            var count: Int

            while (zis.nextEntry.also { ze = it } != null) {
                filename = ze!!.name

                val fmd = File("${folder.absoluteFile}/$filename")
                if (ze!!.isDirectory) {
                    fmd.mkdirs()
                    continue
                }
                else
                { fmd.getParentFile().mkdirs() }

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
        }
        finally
        {
            //if html path not empty then send data to webview activity
            if(!htmlPath.isEmpty())
            {
                Log.e("zipExtract","extract")
                selectedObj.fileName= title.toString()
                selectedObj.fileDirectoryPath=folder.absolutePath
                selectedObj.filePath=htmlPath
                val gson = Gson()
                dataBase.insertOrUpdateEDetailDownload(selectedObj.eDetailingId?.toInt(), selectedObj.fileId!!,  gson.toJson(selectedObj),selectedObj.downloadType)
                dataBase.insertFilePath(1,  gson.toJson(selectedObj), selectedObj.eDetailingId.toString())

                File(path).delete()

            }
            else{

            }}}


    fun downloadProgressAlert()
    {
        context as Activity
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.update_app_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        val headerTv=dialogView.findViewById<TextView>(R.id.headerTv)
        downloadNumber_tv=dialogView.findViewById<TextView>(R.id.downloadNumber_tv)
        downloadNumber_tv.setText(getNumber.toString()+"/"+allProductList.size.toString())
        headerTv.setText("Please wait for your content to be downloaded")
        val okBtn_rl = dialogView.findViewById<View>(R.id.update_btn) as Button
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as Button
        cancel_btn.visibility=View.VISIBLE
        okBtn_rl.setText("Ok")
        okBtn_rl.setOnClickListener {
            alertDialog.dismiss()
        }
        cancel_btn.setOnClickListener {
            cancelAutoDownload=true
            alertDialog.dismiss()
        }

        alertDialog.show()
    }



}