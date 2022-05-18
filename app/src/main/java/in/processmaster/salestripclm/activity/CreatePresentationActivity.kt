package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.StringListAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.interfaceCode.StringInterface
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_presentation.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class CreatePresentationActivity : BaseActivity(), StringInterface {

    var selectedViewList: ArrayList<DownloadFileModel> = ArrayList()
    var createdPresentatedList: ArrayList<String> = ArrayList()
    lateinit var brandAdapter:  Brand_Adapter
    lateinit var alertDialogEdit:  AlertDialog
    var updatePresentationName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_presentation)

        customePresentataion_rv.layoutManager=LinearLayoutManager(this)
        doctorName_tv.text="Create presentation"
        back_iv.setOnClickListener { onBackPressed() }
        runBlocking {
            processAllPages()
        }
        createButton.setOnClickListener {
            if(editButton.text.toString().equals("Update"))
            {
                editButton.text="Edit"
                selectedViewList.clear()
                deleteButton.visibility=View.INVISIBLE
                brandAdapter.notifyDataSetChanged()
                return@setOnClickListener
            }

            if(selectedViewList.size==0)
            {
                generalClass.showSnackbar(it,"No item selected")
                return@setOnClickListener}

            enterNameAlert()
        }
        editButton.setOnClickListener {
            if(editButton.text.toString().equals("Update"))
            {
                if(selectedViewList.size==0)
                {
                    generalClass.showSnackbar(it,"No item selected")
                    return@setOnClickListener}

                dbBase.updatePresentaionData(updatePresentationName,Gson().toJson(selectedViewList))
                editButton.text="Edit"
                deleteButton.visibility=View.INVISIBLE
                selectedViewList.clear()
                brandAdapter.notifyDataSetChanged()
                alertClass.commonAlert("","Data update successfully")

            }
            else{ editPresentationAlert() }
        }
        deleteButton.setOnClickListener {

            val r: Runnable = object : Runnable {
                override fun run() {
                    if(AlertClass.retunDialog)
                    {
                        dbBase.deletePresentaionData(updatePresentationName)
                        runBlocking {
                            withContext(Dispatchers.IO){
                                launch {
                                    createdPresentatedList=dbBase.getAllSavedPresentationName()
                                    if(createdPresentatedList.size==0) runOnUiThread { editButton.visibility=View.INVISIBLE }
                                }
                            }
                        }


                        editButton.text="Edit"
                        deleteButton.visibility=View.INVISIBLE
                        selectedViewList.clear()
                        brandAdapter.notifyDataSetChanged()
                        alertClass.commonAlert("","Data delete successfully")
                    }
                }
            }
            alertClass.twoButtonAlert("Cancel","Yes",
                2,"You want to delete presentation","Are you sure?",r)


        }

    }

    suspend fun processAllPages() = withContext(Dispatchers.IO) {
        launch {
            val edetailingList = dbBase.getSelectedeDetail(true)
            brandAdapter=Brand_Adapter(edetailingList)
            customePresentataion_rv.adapter=brandAdapter
        }
        launch {
            createdPresentatedList=dbBase.getAllSavedPresentationName()
            if(createdPresentatedList.size==0) runOnUiThread { editButton.visibility=View.INVISIBLE }
        }
        launch {  }
    }


    //Brand adapter -----------------------
   inner class Brand_Adapter(
       val brandList: ArrayList<DevisionModel.Data.EDetailing>,
        ) : RecyclerView.Adapter<Brand_Adapter.ViewHolders>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            return ViewHolders(LayoutInflater.from(parent.context).inflate(R.layout.brandlist_header, parent, false))
        }


        override fun onBindViewHolder(holder:ViewHolders, position: Int) {
            val modeldata = brandList?.get(position)
            holder.brandName_tv.text=modeldata?.brandName
            holder.downloadedItem_rv.layoutManager=GridLayoutManager(this@CreatePresentationActivity,7)

            runBlocking {
                withContext(Dispatchers.IO){
                    launch {
                        val itemList= modeldata?.geteDetailId()?.let { dbBase.getAllDownloadedData(it) }
                        itemList?.removeAll { it.downloadType == "" }


                        Collections.sort(itemList, Comparator<DownloadFileModel?> { a1, a2 ->
                            a1?.downloadType.toString().compareTo(a2?.downloadType.toString())
                        })

                        this@CreatePresentationActivity.runOnUiThread{holder.downloadedItem_rv.adapter=DownloadContent_Adapter(itemList)}

                    } }
            }


        }

        override fun getItemCount(): Int {
            return brandList!!.size
        }

       inner class ViewHolders(view: View): RecyclerView.ViewHolder(view){
            var brandName_tv=view.findViewById<TextView>(R.id.brandName_tv)
            var downloadedItem_rv=view.findViewById<RecyclerView>(R.id.downloadedItem_rv)
        }
        }

    inner class DownloadContent_Adapter(
        val doctorList: ArrayList<DownloadFileModel>?,
    ) : RecyclerView.Adapter<DownloadContent_Adapter.ViewHolders>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            return ViewHolders(LayoutInflater.from(parent.context).inflate(R.layout.common_downloaded_content_view, parent, false))
        }

        override fun onBindViewHolder(holder:ViewHolders, position: Int) {
            val modeldata = doctorList?.get(position)
            holder.title_tv.setText(modeldata?.fileName)
            holder.downloadedType_tv.setText(modeldata?.downloadType)
            if(modeldata?.downloadType.equals("VIDEO") || modeldata?.downloadType.equals("IMAGE"))
            {
                if(modeldata?.downloadType.equals("VIDEO") ) holder.play_iv.visibility=View.VISIBLE
                else holder.play_iv.visibility=View.INVISIBLE
                val circularProgressDrawable = CircularProgressDrawable(this@CreatePresentationActivity)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                val requestOptions = RequestOptions()
                requestOptions.isMemoryCacheable
                Glide.with(this@CreatePresentationActivity).setDefaultRequestOptions(requestOptions)
                    .load(Uri.fromFile(File(modeldata?.fileDirectoryPath, modeldata?.fileName)))
                    .placeholder(circularProgressDrawable)
                    .into(holder.thumb_iv)
            }
            else if(modeldata?.downloadType.equals("ZIP"))
            {
                holder.downloadedType_tv.setText("Web view")
                holder.play_iv.visibility=View.INVISIBLE
                holder.html_wv.settings.javaScriptEnabled = true
                holder.html_wv.settings.setDomStorageEnabled(true)
                holder.html_wv.settings.setDatabaseEnabled(true)
                holder.html_wv.settings.setLoadWithOverviewMode(true)
                holder.html_wv.getSettings().setAllowContentAccess(true)
                holder.html_wv.getSettings().setAllowFileAccess(true)
                holder.html_wv.getSettings().setUseWideViewPort(true);
                holder.html_wv.setInitialScale(1);
                var filePath=File(modeldata?.filePath)
                holder.html_wv.loadUrl("file:///$filePath")
                holder.html_wv.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        view.loadUrl(url)
                        return true
                    }
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                        Log.e("webViewError", error.toString())
                    }

                }
            }
            val filtered = selectedViewList!!.filter { it.fileId==modeldata?.fileId }
            if(filtered.size!=0)
            {
                holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(
                    this@CreatePresentationActivity,
                    R.color.appColor))
            }


            holder.parent_llVideo.setOnClickListener {
                val color = (holder.parent_llVideo.getBackground() as ColorDrawable).color

                if (color == Color.WHITE) {
                    holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(
                        this@CreatePresentationActivity,
                        R.color.appColor
                    ))
                    modeldata?.let { it1 -> selectedViewList.add(it1) }

                } else {
                    for ((index,data) in selectedViewList.withIndex())
                    {
                        if(data.fileId==modeldata?.fileId) {
                            selectedViewList.removeAt(index)
                            break;
                        }
                    }

                    holder.parent_llVideo.setBackgroundColor(
                        ContextCompat.getColor(
                            this@CreatePresentationActivity,
                            R.color.white
                        ))
                }
            }
        }

        override fun getItemCount(): Int {
            return doctorList!!.size
        }

      inner class ViewHolders(view: View): RecyclerView.ViewHolder(view){
            var title_tv=view.findViewById<TextView>(R.id.title_tv)
          var downloadedType_tv=view.findViewById<TextView>(R.id.downloadedType_tv)
          var play_iv=view.findViewById<ImageView>(R.id.play_iv)
          var thumb_iv=view.findViewById<ImageView>(R.id.thumb_iv)
          var html_wv=view.findViewById<WebView>(R.id.html_wv)
          var parent_llVideo=view.findViewById<LinearLayout>(R.id.parent_llVideo)
        }
    }

    //Enter name alert---------------
    fun enterNameAlert() {
        val dialogBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView: View = inflater.inflate(R.layout.create_name, null)
            dialogBuilder.setView(dialogView)

            val alertDialog: AlertDialog = dialogBuilder.create()
            alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val okBtn_rl = dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton

            val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as AppCompatButton

            var nameEditText = dialogView.findViewById<View>(R.id.nameEditText) as EditText



            okBtn_rl.setOnClickListener {

                for(item in createdPresentatedList)
                {
                    if(item.equals(nameEditText.text.toString()))
                    {generalClass.showSnackbar(it,"This name is already in use")
                        return@setOnClickListener}
                }
                dbBase.insertCreatePresentaionData(nameEditText.text.toString(),Gson().toJson(selectedViewList))
                createdPresentatedList.clear()
                createdPresentatedList=dbBase.getAllSavedPresentationName()

                if(createdPresentatedList.size!=0) editButton.visibility=View.VISIBLE
                selectedViewList.clear()
                brandAdapter.notifyDataSetChanged()
                alertClass.commonAlert("","Presentation save successfully")
                alertDialog.dismiss()
            }

            cancel_btn.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()

    }

    fun editPresentationAlert(){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.stringlist_alert, null)
        dialogBuilder.setView(dialogView)

        alertDialogEdit = dialogBuilder.create()
        alertDialogEdit.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl = dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton
        okBtn_rl.visibility=View.INVISIBLE

        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as AppCompatButton

        var stringRecycler = dialogView.findViewById<View>(R.id.stringRecycler) as RecyclerView

        stringRecycler.layoutManager=LinearLayoutManager(this)
        stringRecycler.adapter= StringListAdapter(createdPresentatedList,this)

        cancel_btn.setOnClickListener {
            alertDialogEdit.dismiss()
        }

        alertDialogEdit.show()
    }


    override fun onClickString(passingString: String?) {
        if(::alertDialogEdit.isInitialized && alertDialogEdit!=null)
        { alertDialogEdit.dismiss()}
        updatePresentationName=passingString!!
        selectedViewList.clear()
        selectedViewList.addAll(dbBase.getAllPresentationItem(passingString))
        brandAdapter.notifyDataSetChanged()
        editButton.text="Update"
        deleteButton.visibility=View.VISIBLE
    }


}