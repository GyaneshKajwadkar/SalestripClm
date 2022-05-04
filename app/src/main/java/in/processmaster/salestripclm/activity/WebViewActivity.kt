package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.HorizontalWebViewAdapter
import `in`.processmaster.salestripclm.adapter.OtherBrandSelectionAdapter
import `in`.processmaster.salestripclm.adapter.OtherFileAdapter
import `in`.processmaster.salestripclm.fragments.ShowDownloadedFragment
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.StoreVisualInterface
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.web_bottom_sheet.*
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.text.SimpleDateFormat
import java.util.*


class WebViewActivity : BaseActivity(), StoreVisualInterface , ItemClickDisplayVisual {

    private var gs: GestureDetector? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetWeb: ConstraintLayout
    var db = DatabaseHandler(this)
    var brandId=0
    var empId=0
    var startDateTime=""
    var doctorId=0
    var clicked=""

    var pnlFlash:CoordinatorLayout?=null
    var position=0
    var isList=false
    var threadBrand: Thread?= null


    var eDetailingId=0
    var isCurrent=true;

    var arrayweb: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var otherFileAdapter :OtherFileAdapter?=null

    var thread: Thread?= null
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

    var  filterImageList: ArrayList<DownloadFileModel> = ArrayList()
    var  filterVideoList: ArrayList<DownloadFileModel> = ArrayList()

    companion object {
        var modelweb : DownloadFileModel?= null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        pnlFlash = findViewById<CoordinatorLayout>(R.id.pnlFlash)

        bottomSheetWeb=findViewById(R.id.bottomSheetWeb)as ConstraintLayout

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWeb)

        horizontal_rv?.setLayoutManager(
                LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        )
        horizontal_rv?.itemAnimator = DefaultItemAnimator()

        webview.settings.javaScriptEnabled = true
        webview.settings.setDomStorageEnabled(true)
        webview.settings.setDatabaseEnabled(true)
        webview.settings.setLoadWithOverviewMode(true)
        webview.getSettings().setAllowContentAccess(true)
        webview.getSettings().setAllowFileAccess(true)
        webview.settings.setLoadWithOverviewMode(true)

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError
            ) {
                //Your code to do
                Log.e("webViewError", error.toString())
            }
        }


        openBottomSheet_iv.setOnClickListener()
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }


        if(intent.getSerializableExtra("webArray")!=null)
        {

            db = DatabaseHandler(this)

            arrayweb = intent.getSerializableExtra("webArray") as ArrayList<DownloadFileModel>
            modelweb = intent.getSerializableExtra("model") as DownloadFileModel

            position = intent.getIntExtra("position", 0)

            eDetailingId= modelweb?.eDetailingId!!

            val relativeParent = findViewById<RelativeLayout>(R.id.relativeParent)

         /*   if(!modelweb?.favFileName?.isEmpty()!!)
            {
                productParent_ll?.visibility= View.GONE
            }*/

            //  fab_send?.hide()

            fab_send?.setOnClickListener({
                var intent = Intent(this, ImageSelectorActivity::class.java)
                intent.putExtra("filePath",getFilesDir()?.getAbsolutePath() + "/Screenshots/")
                intent.putExtra("selection","send")
                startActivity(intent)
            })

            fab_sc?.setOnClickListener({
              //  fab_send?.hide()
              //  fab_sc?.hide()
                parentMenu.visibility=View.GONE
                parentScreeShotLL.visibility=View.GONE
                parent_sendLL.visibility=View.GONE

                val animation1 = AlphaAnimation(1f, 0f)
                animation1.duration = 100

                animation1.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                        pnlFlash?.setVisibility(View.VISIBLE);
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        pnlFlash?.visibility == View.GONE
                        screenShotShow_iv?.visibility == View.GONE
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }
                    // All the other override functions
                })

                pnlFlash?.startAnimation(animation1)

                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        getBitmapFromView(relativeParent)
                    }
                }, 150)

            })

            var file = File(arrayweb.get(position).filePath)
            webview.loadUrl("file:///$file")
            webview.setOnTouchListener(onTouch);

            setWebHorizontalAdapter(arrayweb, position, modelweb!!)

            //getData
            val intent = intent
            //  val webUrlPath = intent.getStringExtra("webUrlPath")
            empId = intent.getIntExtra("empId", 0)
           // startDateTime = intent.getStringExtra("currentDateTime").toString()
            startDateTime = ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime
            brandId = intent.getIntExtra("brandId", 0)
            doctorId = intent.getIntExtra("doctorId", 0)
            //  val file = File(webUrlPath)

            db?.insertFileID(modelweb!!.fileId,startDateTime,brandId)
            showWeb_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))

            setSlideViewTime()

            end_btn?.setOnClickListener({

                val currentDate: String = SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                        ).format(Date())
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                Date()
                        )


                        db?.updateendData(currentDate + " " + currentTime,startDateTime)
                        onBackPressed()
                        finish()
            })

            likeCommentColor()
            filterListVideoImage()

            showWeb_mb.setOnClickListener {
                if(clicked.equals("ZIP"))return@setOnClickListener
                setAllDefault("ZIP")
                runOnUiThread {
                    showWeb_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                    horizontalOther_rv.visibility=View.GONE
                    horizontal_rv.visibility=View.VISIBLE
                }
            }
            showimg_mb.setOnClickListener {
                if(clicked.equals("IMAGE"))return@setOnClickListener
                setAllDefault("IMAGE")
                runOnUiThread {
                    showimg_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                }
                setOtherBrandSelection(filterImageList)
            }
            showVideo_mb.setOnClickListener {
                if(clicked.equals("VIDEO"))return@setOnClickListener
                setAllDefault("VIDEO")
                runOnUiThread {
                    showVideo_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                }
                setOtherBrandSelection(filterVideoList)
            }

        }

        if(intent.getStringExtra("singleSelection")!=null)
        {
            var  selection = intent.getStringExtra("singleSelection")
            var file = File(selection)
            webview.loadUrl("file:///$file")

          //  fab_send?.visibility=View.GONE
          //  fab_sc?.visibility=View.GONE
            parentMenu.visibility=View.GONE
            parentScreeShotLL.visibility=View.GONE
            parent_sendLL.visibility=View.GONE
            floating_action_button.visibility=View.VISIBLE
            openBottomSheet_iv?.visibility=View.GONE

            webview.setOnTouchListener(onTouchSimple);

        }


        var haveScreenShots=checkScreenShots()
/*        val folder = File(getFilesDir()?.getAbsolutePath() + "/Screenshots/")

        if (folder.exists()) {

            var allFiles: Array<File> = folder.listFiles(object : FilenameFilter {
                override fun accept(dir: File?, name: String): Boolean {
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
                }
            })

            if(allFiles.size!=0)
            {
                haveScreenShots=true

            }
        }*/


        parentMenu.setOnClickListener({
            if(parentScreeShotLL?.visibility==View.VISIBLE)
            {
                if(haveScreenShots)
                {
                  //  fab_send?.visibility=View.GONE
                    parent_sendLL.visibility=View.GONE
                }
             //   fab_sc?.visibility=View.GONE
                parentScreeShotLL.visibility=View.GONE
            }
            else
            {
                if(haveScreenShots)
                {
                  //  fab_send?.show()
                  //  fab_send?.visibility=View.VISIBLE
                    parent_sendLL?.visibility=View.VISIBLE
                }
              //  fab_sc?.visibility=View.VISIBLE
                parentScreeShotLL.visibility=View.VISIBLE
            }
        })

        fabLike?.setOnClickListener({
            if (isList) {
                db?.insertlike(0, modelweb!!.fileId,startDateTime)
                fabLike?.setColorFilter(Color.BLACK)
                isList = false
            }
            else {
                db?.insertlike(1, modelweb!!.fileId,startDateTime)
                fabLike?.setColorFilter(Color.WHITE)
                isList = true
            }


        })

        fabComment?.setOnClickListener({
            commentDialog()
        })

        currentProduct_btn?.setOnClickListener({
            if(!isCurrent)
            {
                setAllDefault("ZIP")
                showWeb_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))

                otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
                currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))
                fabLike?.visibility=View.VISIBLE
                fabComment?.visibility=View.VISIBLE
                selectionBtn_parent?.visibility=View.VISIBLE

                setWebHorizontalAdapter(arrayweb, position, modelweb!!)

                isCurrent=true
            }
        })

        otherProduct_btn?.setOnClickListener({
            if(isCurrent)
            {

                edetailingList = getAllEdetailingProduct()

                otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))
                currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
                fabLike?.visibility=View.GONE
                fabComment?.visibility=View.GONE

                otherFileAdapter = OtherFileAdapter(edetailingList,this,this, eDetailingId)
                horizontal_rv?.adapter = otherFileAdapter
                otherFileAdapter?.notifyDataSetChanged()

                horizontalOther_rv.visibility=View.GONE
                horizontal_rv.visibility=View.VISIBLE
                selectionBtn_parent?.visibility=View.GONE

                isCurrent=false
            }

        })
        floating_action_button.setOnClickListener({
            onBackPressed()
        })
    }

    fun filterListVideoImage()
    {
        val runnable= Runnable {
            filterImageList.clear()
            filterVideoList.clear()

            val dowloadedAllList=dbBase.getAllDownloadedData(eDetailingId)

            for(item in dowloadedAllList)
            {
                if(item.downloadType.equals("IMAGE"))filterImageList.add(item)
            }
            for(item in dowloadedAllList)
            {
                if(item.downloadType.equals("VIDEO"))filterVideoList.add(item)
            }
            runOnUiThread { if(filterImageList.size==0)showimg_mb.visibility=View.GONE
            else showimg_mb.visibility=View.VISIBLE
                if(filterVideoList.size==0)showVideo_mb.visibility=View.GONE
                else showVideo_mb.visibility=View.VISIBLE }

        }
        Thread(runnable).start()
    }

    private val onTouchSimple = OnTouchListener { v, event ->
        if (gs == null) {
            gs = GestureDetector(this,
                object : SimpleOnGestureListener() {
                    override fun onDoubleTapEvent(e: MotionEvent): Boolean
                    {

                        return true
                    }

                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

                        if(floating_action_button.visibility==View.VISIBLE)
                        {
                            floating_action_button.visibility=View.GONE
                        }
                        else
                        {
                            floating_action_button.visibility=View.VISIBLE
                        }

                        return false
                    }
                })
        }
        gs!!.onTouchEvent(event)
        false
    }


    private val onTouch = OnTouchListener { v, event ->
        if (gs == null) {
            gs = GestureDetector(this,
                    object : SimpleOnGestureListener() {
                        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                            Log.e("double tap", "double tap")
                            //Double Tap
                            floating_action_button.visibility=View.GONE
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                            return true
                        }

                        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                            //Single Tab
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                            return false
                        }
                    })
        }
        gs!!.onTouchEvent(event)
        false
    }


     fun  checkScreenShots() : Boolean
    {
        val folder = File(getFilesDir()?.getAbsolutePath() + "/Screenshots/")

        if (folder.exists()) {

            var allFiles: Array<File> = folder.listFiles(object : FilenameFilter {
                override fun accept(dir: File?, name: String): Boolean {
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
                }
            })

            if(allFiles.size!=0)
            {
                return true
            }
        }
        return false

    }

/*
    fun getWebViewFilePath() {
        var edetailingList = db.getAlleDetail()   //fetch edetailing list from db

        for ((index, value) in edetailingList?.withIndex()!!) {

            if (value.isSaved == 1)    //check edetailing have saved file or not
            {
                var downloadFilePathLocal: ArrayList<DownloadFileModel> = Gson().fromJson(
                        value.filePath,
                        object : TypeToken<List<DownloadFileModel?>?>() {}.type
                )
                //get and convert save file string to array list
                var modelDownloadList :DownloadFileModel

                for ((index, valueDownload) in downloadFilePathLocal?.withIndex()!!)
                {
                    valueDownload.setDownloadType(value.divisionName) //set division name
                    valueDownload.setBrandId(value.brandId) //set brand id in model then add to array list
                    //  downloadFilePathList.add(valueDownload)
                    //   extractFileArray.add(valueDownload.zipExtractFilePath)
                    modelDownloadList=valueDownload
                    if(index==downloadFilePathLocal.size-1)
                    {
                        downloadFilePathList.addAll(listOf(modelDownloadList))
                    }

                }
            }

            if (index == edetailingList?.size!! - 1)
            {
                setWebHorizontalAdapter(downloadFilePathList, position, modelweb!!)
            }
        }
    }
*/

    fun setWebHorizontalAdapter(list: ArrayList<DownloadFileModel>, position: Int, modelweb: DownloadFileModel)
    {

        val adapterVisualFile = HorizontalWebViewAdapter(list, this, this, brandId, position)
        horizontal_rv?.adapter = adapterVisualFile

    }

 /*   override fun onClickString(fileString: File)
    {


        webview.loadUrl("file:///$fileString")

        *//*       val file = File(filePath)
                webview.loadUrl("file:///$file")

                if (!currentDateTime!!.isEmpty()) {
                    if (empId!=0) {
                        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                                Date()
                        )
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        var visualAdsModel = VisualAdsModel_Send()
                        visualAdsModel.setBrandId(brandId.toString())
                        visualAdsModel.setDoctorId(doctorId.toString())
                        visualAdsModel.setStartDate(currentDateTime)
                        visualAdsModel.setEmpId(empId.toString())
                        visualAdsModel.setEndDate(currentDate + " " + currentTime)
                        val gson = Gson()
                        db.insertOrUpdateVisualAds(gson.toJson(visualAdsModel), doctorId, brandId)
                    }
                }

*//*
        likeCommentColor()
    }*/

    open fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        try {

            val canvas = Canvas(bitmap)
            val bgDrawable = view.background
            if (bgDrawable != null)
            {
                bgDrawable.draw(canvas)
            }
            else
            {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            store(bitmap)
        }
        catch (e: java.lang.Exception)
        {
            Log.e("exceptionInCanvas", e.message.toString());
        }
        return bitmap
    }

    fun store(bm: Bitmap)
    {
        val uniqueString = UUID.randomUUID().toString()
        val mFolder = File("$filesDir/Screenshots")
        val dir = File(mFolder.absolutePath + "/" + uniqueString + "_ImageFile.jpg")
        if (!mFolder.exists()) {
            mFolder.mkdir()
        }

        try
        {
            val fOut = FileOutputStream(dir)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
            openScreenshot(dir)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }


    private fun openScreenshot(imageFile: File) {

        if (imageFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            
            runOnUiThread {
                screenShotShow_iv?.setImageBitmap(myBitmap)
              //  fab_sc?.show()
              //  fab_send?.show()
                parentMenu.visibility=View.VISIBLE

                var haveScreenShots=checkScreenShots()

                if(haveScreenShots)
                {
                   // fab_send?.show()
                   // fab_send?.visibility=View.VISIBLE
                    parent_sendLL?.visibility=View.VISIBLE
                }
                //fab_sc?.visibility=View.VISIBLE
                parentScreeShotLL.visibility=View.VISIBLE
            }

        }
    }



    fun commentDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.commentdialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val cancel_btn = dialogView.findViewById(R.id.cancel_btn) as Button
        val post_btn = dialogView.findViewById(R.id.post_btn) as Button
        val comment_et = dialogView.findViewById(R.id.comment_et) as EditText

        val storecomment= db?.getComment(modelweb!!.fileId.toString(),startDateTime)
        comment_et.setText(storecomment)

        cancel_btn.setOnClickListener({

            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)

            alertDialog.dismiss()
            alertDialog.cancel()
        })
        post_btn.setOnClickListener({

            if(comment_et.text.toString().isEmpty())
            {
                comment_et.setError("Post required")
                comment_et.requestFocus()
                return@setOnClickListener
            }

            db?.insertComment(comment_et.text.toString(), modelweb!!.fileId,startDateTime)
            fabComment?.setColorFilter(Color.WHITE)
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)


            alertDialog.dismiss()
            alertDialog.cancel()
        })

        alertDialog.show()
    }

    fun getAllEdetailingProduct() : java.util.ArrayList<DevisionModel.Data.EDetailing>
    {
        var  edetailingList = db.getAlleDetail() //fetch edetailing list from db
        var  filteredList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
        for (itemParent in edetailingList )
        {
            if(itemParent.isSaved==1)
            {
                var downloadedList = db.getAllDownloadedData(itemParent.geteDetailId()!!)
                var isAvailable=false
                for(itemChild in downloadedList)
                {
                    if(itemChild.downloadType.equals("ZIP")) isAvailable=true
                }
                if(isAvailable)  filteredList.add(itemParent); continue


              /*  if(downloadedList.stream().anyMatch({ o -> o.downloadType.equals("ZIP") }))
                {
                    filteredList.add(itemParent)
                }*/
            }
        }
        return filteredList
    }

    override fun onClickDisplayVisual(passingInterface: Int, brandIDInterface : Int,selectionType: Int)
    {
        arrayweb.clear()

        for (itemParent in db.getAllDownloadedData(passingInterface) )
        {

            if(itemParent.downloadType.equals("ZIP"))
            {
                arrayweb.add(itemParent)
            }

        }
        position=0
        modelweb =arrayweb.get(0)

        setWebHorizontalAdapter(arrayweb, position, modelweb!!)
        val file= File(modelweb?.filePath)
        webview.loadUrl("file:///$file")

        eDetailingId=passingInterface
        otherFileAdapter?.notifyDataSetChanged()
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        brandId=brandIDInterface
        dbBase?.insertStartTimeSlide(startDateTime,doctorId,brandId,
            PhotoSlideShowActivity.model?.brandName,0,currentTime.toString())


        isCurrent=true
        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))

        fabLike?.visibility=View.VISIBLE
        fabComment?.visibility=View.VISIBLE
        selectionBtn_parent?.visibility=View.VISIBLE

        filterListVideoImage()
        likeCommentColor()
        slideBrandWiseInsert(startDateTime,brandId)
    }



    fun likeCommentColor()
    {

        db?.insertFileID(modelweb!!.fileId, startDateTime,brandId)

        val isLike=db?.getLike(modelweb!!.fileId.toString(),startDateTime)
        val storecomment= db?.getComment(modelweb!!.fileId.toString(),startDateTime)

        if(storecomment!=null)
        {
            fabComment?.setColorFilter(Color.WHITE)

        }
        else
        {
            fabComment?.setColorFilter(Color.BLACK)

        }

        if(isLike!!)
        {
            fabLike?.setColorFilter(Color.WHITE)
            isList = true
        }
        else
        {
            fabLike?.setColorFilter(Color.BLACK)
            isList = false
        }
        setSlideViewTime()

    }


    fun setSlideViewTime()
    {
        thread?.interrupt()
        var dbTimer=db?.getTime(modelweb!!.fileId.toString(),startDateTime)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            dbTimer=dbTimer!!+1
                            Log.e("timerSlider",dbTimer.toString())
                            db?.insertTime(dbTimer!!, modelweb!!.fileId ,startDateTime)
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        thread?.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        thread?.interrupt()
    }

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    override fun onClickString(fileString: File?) {

        webview.loadUrl("file:///$fileString")

        /*       val file = File(filePath)
                webview.loadUrl("file:///$file")

                if (!currentDateTime!!.isEmpty()) {
                    if (empId!=0) {
                        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                                Date()
                        )
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        var visualAdsModel = VisualAdsModel_Send()
                        visualAdsModel.setBrandId(brandId.toString())
                        visualAdsModel.setDoctorId(doctorId.toString())
                        visualAdsModel.setStartDate(currentDateTime)
                        visualAdsModel.setEmpId(empId.toString())
                        visualAdsModel.setEndDate(currentDate + " " + currentTime)
                        val gson = Gson()
                        db.insertOrUpdateVisualAds(gson.toJson(visualAdsModel), doctorId, brandId)
                    }
                }

*/
        likeCommentColor()
    }

    fun slideBrandWiseInsert(startDateTime: String,brandID:Int)
    {
        threadBrand?.interrupt()
        var dbBaseTimer=dbBase?.getBrandTime(brandID.toString(),startDateTime)
        threadBrand = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            dbBaseTimer=dbBaseTimer!!+1

                            Log.e("timerBrandWiseSlider",dbBaseTimer.toString())
                            dbBase?.insertBrandTime(dbBaseTimer!!  ,startDateTime,brandID.toString())
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        threadBrand?.start()
    }

    override fun onStop() {
        super.onStop()
        threadBrand?.interrupt()
        thread?.interrupt()
    }

    fun setOtherBrandSelection(list: ArrayList<DownloadFileModel>)
    {
        runOnUiThread {
            horizontalOther_rv.visibility=View.VISIBLE
            horizontal_rv.visibility=View.GONE
        }
        horizontalOther_rv.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        val otherBrandAdapter= OtherBrandSelectionAdapter(
            this,
            list,
            clicked,
            end_btn,
            doctorId,
            eDetailingId
        )
        horizontalOther_rv.adapter=otherBrandAdapter
    }

    fun setAllDefault(clickedButton:String)
    {
        clicked=clickedButton
        showimg_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appDarkColor))
        showWeb_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appDarkColor))
        showVideo_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appDarkColor))

    }

    override fun onBackPressed() {
        if(intent.getSerializableExtra("webArray")!=null)
        else  super.onBackPressed()
    }

}