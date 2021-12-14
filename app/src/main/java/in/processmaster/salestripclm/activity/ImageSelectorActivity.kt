package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.ImageSelectorAdapter
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_image_selecror.*
import pl.droidsonroids.gif.GifImageView
import java.io.File
import java.io.FilenameFilter
import java.io.Serializable


class ImageSelectorActivity : BaseActivity() {


    var adapter=ImageSelectorAdapter()
    var nodata_gif: GifImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_selecror)

        nodata_gif=findViewById(R.id.nodata_gif)

        val folder = File(intent.getStringExtra("filePath"))
        val selection = intent.getStringExtra("selection")

        if(selection.equals("delete"))
        {
            //  sendImageWatsup?.visibility=View.GONE
            //  sendImgGmail?.visibility=View.GONE
            //  delete_imv?.visibility=View.VISIBLE
        }

        else
        {

        }

        if (folder.exists())
        {
            getAllImages()
        }
        else
        {
            nodata_gif?.visibility=View.VISIBLE
        }


        back_imv?.setOnClickListener({
            onBackPressed()
        })


        delete_imv.setOnClickListener({
            var getArray= adapter.getAllWorkRows()
            var filterList= getArray?.filter  { s -> s.isSend == true}
            if(filterList?.size==0 || filterList==null)
            {
                Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            deleteAlert(filterList)
        })


        sendImgGmail?.setOnClickListener({


            var getArray= adapter.getAllWorkRows()
            var filterList= getArray?.filter  { s -> s.isSend == true}

            if(filterList?.size==0 || filterList==null)
            {
                Toast.makeText(this, "No attachment selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent: Intent = Intent(
                this@ImageSelectorActivity,
                MailActivity::class.java
            )
            val args = Bundle()
            args.putSerializable(
                "ARRAYLIST",
                filterList as Serializable
            )
            intent.putExtra("attachment", args)
            startActivity(intent)



            /*          var getArray= adapter.getAllWorkRows()
                        var filterList= getArray?.filter  { s -> s.isSend == true}
                        if(filterList?.size==0)
                        {
                            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }


                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND_MULTIPLE
                        intent.putExtra(Intent.EXTRA_EMAIL,  "kajwadkar13@gmail.com")
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Screenshots salestrip CLM")
                        intent .setPackage("com.google.android.gm")

                        intent.type = "image/jpeg"

                        val files: ArrayList<Uri> = ArrayList<Uri>()

                        for (path in filterList!!)
                        {
                            val file = File(path.file?.absolutePath)
                            val uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                            files.add(uri)
                        }

                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
                        startActivity(intent)*/
        })

    }


    //for dynamic grid view set no of column according to screen
    object Utility {
        fun calculateNoOfColumns(context: Context, columnWidthDp: Float): Int { // For example columnWidthdp=180
            val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }

    fun getAllImages()
    {
        val mNoOfColumns = Utility.calculateNoOfColumns(this, 220F)

        val folder = File(intent.getStringExtra("filePath"))


        var allFiles: Array<File> = folder.listFiles(object : FilenameFilter {
            override fun accept(dir: File?, name: String): Boolean {
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
            }
        })

        var fileList:ArrayList<SendImage>?=ArrayList()

        for ((index, value) in allFiles.withIndex())
        {
            var imageModel=SendImage()
            imageModel.file=value
            fileList?.add(imageModel)

            if(index==allFiles.size-1)
            {
                adapter=ImageSelectorAdapter(fileList, this,true)
                multipleImage_rv.layoutManager = GridLayoutManager(this, mNoOfColumns)
                multipleImage_rv.itemAnimator = DefaultItemAnimator()
                multipleImage_rv.adapter = adapter
            }
        }
        if(fileList?.size==0)
        {
            nodata_gif?.visibility=View.VISIBLE
            //   multipleImage_rv.visibility=View.GONE
        }
        adapter.notifyDataSetChanged()

    }

    //Model class for sending image
    class SendImage :Serializable
    {
        var file:File?=null
            get() = field
            set(value) { field = value }

        var isSend: Boolean? = null
            get() = field
            set(value) { field = value }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val pm: PackageManager = packageManager
        val app_installed: Boolean
        app_installed = try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    fun deleteAlert(filterList: List<SendImage>?)
    {
        progressImageSelector.visibility=View.VISIBLE

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val message_tv = dialogView.findViewById<View>(R.id.message_tv) as TextView
        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setText("Delete")
        message_tv.setText("Are you sure you want to delete selected files?")
        mainHeading_tv.setText("Delete Files!")

        exit_btn.setOnClickListener{
            alertDialog.dismiss()
            for ((index, path) in filterList?.withIndex()!!)
            {
                val file = File(path.file?.absolutePath)
                file.delete()

                if(index==filterList.size-1)
                {
                    getAllImages()
                    progressImageSelector.visibility=View.GONE
                }
            }
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
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