package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.OtherBrandSelectionAdapter
import `in`.processmaster.salestripclm.adapter.OtherFileAdapter
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.StoreVisualInterface
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.web_bottom_sheet.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VideoPlayerActivity : BaseActivity() , ItemClickDisplayVisual, PlayerControlView.VisibilityListener,
    StoreVisualInterface
{
    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
     var uris:ArrayList<Uri> = ArrayList()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var position=0
    var brandId=0
    var empId=0
    var startDateTime=""
    var doctorId=0
    var isList=false

    var eDetailingId=0
    var isCurrent=true;
    var otherFileAdapter : OtherFileAdapter?=null
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var arrayVideo: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
    var doubleclick= false
    var thread: Thread?= null
    var threadBrand: Thread?= null
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    lateinit var adapterVisualFile : HorizontalVideoViewAdapter
    var clicked=""
    var  filterWebList: ArrayList<DownloadFileModel> = ArrayList()
    var  filterImageList: ArrayList<DownloadFileModel> = ArrayList()

    companion object {
        var videoModel : DownloadFileModel?= null
    }

    override fun onCreate (savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWeb)

        floating_action_backBtn.setOnClickListener({
            onBackPressed()
        })

        playerView.setControllerVisibilityListener(object : PlayerControlView.VisibilityListener {
            override fun onVisibilityChange(i: Int) {

                if(intent.getSerializableExtra("videoArray")==null)
                {
                    if (playerView.isControllerVisible())
                    {
                        floating_action_backBtn.visibility=View.VISIBLE
                    }
                    else
                    {
                        floating_action_backBtn.visibility=View.GONE
                    }
                }

            }
        })

        slideBrandWiseInsert(startDateTime,brandId)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initPlayer() {
        mPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = mPlayer
        mPlayer?.playWhenReady = true
        mPlayer?.seekTo(playbackPosition)

        if(intent.getSerializableExtra("videoArray")!=null)
        {
            arrayVideo = intent.getSerializableExtra("videoArray") as ArrayList<DownloadFileModel>
            videoModel = intent.getSerializableExtra("model") as DownloadFileModel

      /*      if(!videoModel?.favFileName?.isEmpty()!!)
            { productParent_ll?.visibility= View.GONE }*/

            showVideo_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))


            openBottomSheet_iv.setOnClickListener()
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

            position = intent.getIntExtra("position", 0)
            if(arrayVideo!=null )
            {
                for (filePath in arrayVideo)
                {
                    var myFile = File(filePath.filePath)
                    uris.add(Uri.fromFile(myFile))
                }
                mPlayer?.prepare(buildMediaSourceVideoArray(uris), false, false)
                mPlayer?.seekTo(position, 0)
                mPlayer?.setPlayWhenReady(true)

                setHorizontalAdapter(arrayVideo)

                mPlayer?.addListener(object : Player.EventListener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {

                    }

                    override fun onPositionDiscontinuity(reason: Int) {
                        adapterVisualFile.notifyDataSetChanged()
                                          }
                })

                eDetailingId= videoModel?.eDetailingId!!

                //getData
                val intent = intent
                //  val webUrlPath = intent.getStringExtra("webUrlPath")
                empId = intent.getIntExtra("empId", 0)
                startDateTime = intent.getStringExtra("currentDateTime").toString()
                brandId = intent.getIntExtra("brandId", 0)
                doctorId = intent.getIntExtra("doctorId", 0)
                //  val file = File(webUrlPath)


                videoModel?.fileId?.let { dbBase?.insertFileID(it,startDateTime,brandId) }
                setSlideViewTime()

                end_btn?.setOnClickListener({

                    val currentDate: String = SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    ).format(Date())
                    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                            Date()
                    )
                    dbBase?.updateendData(currentDate + " " + currentTime,startDateTime)
                    onBackPressed()
                    finish()
                })

                fabLike?.setOnClickListener({

                    if (isList) {
                        dbBase?.insertlike(0, videoModel!!.fileId,startDateTime)
                        fabLike?.setColorFilter(Color.BLACK)
                        isList = false
                    }
                    else {
                        dbBase?.insertlike(1, videoModel!!.fileId,startDateTime)
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
                        setAllDefault("VIDEO")
                        showVideo_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))

                        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
                        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))
                        fabLike?.visibility=View.VISIBLE
                        fabComment?.visibility=View.VISIBLE
                        selectionBtn_parent?.visibility=View.VISIBLE

                        setHorizontalAdapter(arrayVideo)

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
            }

            showVideo_mb.setOnClickListener {
                if(clicked.equals("VIDEO"))return@setOnClickListener
                setAllDefault("VIDEO")
                runOnUiThread {
                    showVideo_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                    horizontalOther_rv.visibility=View.GONE
                    horizontal_rv.visibility=View.VISIBLE
                }
            }
            showWeb_mb.setOnClickListener {
                if(clicked.equals("ZIP"))return@setOnClickListener
                setAllDefault("ZIP")
                runOnUiThread {
                    showWeb_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                }
                setOtherBrandSelection(filterWebList)
            }
            showimg_mb.setOnClickListener {
                if(clicked.equals("IMAGE"))return@setOnClickListener
                setAllDefault("IMAGE")
                runOnUiThread {
                    showimg_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
                }
                setOtherBrandSelection(filterImageList)
            }

            filterListWebmage()

            playerView.getVideoSurfaceView()?.setOnClickListener({ view ->

                if(doubleclick)
                {
                    mPlayer?.setPlayWhenReady(false)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
                else
                {
                    mPlayer?.setPlayWhenReady(true)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleclick=false
                }, 300)

                doubleclick=true

                playerView.showController()

            })

            likeCommentColor()
        }


        if(intent.getStringExtra("singleSelection")!=null)
        {
            var  selection = intent.getStringExtra("singleSelection")

            val myFile=File(selection)
          var  videoUrl = Uri.fromFile(myFile).toString()
          val uri = Uri.parse(videoUrl)
          val mediaSource: MediaSource? = buildMediaSourceSingle(uri)

            mediaSource?.let { mPlayer?.prepare(it, false, false) }
            openBottomSheet_iv?.visibility=View.GONE
        }



    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()

        createConnectivity(this)

        if (Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()

        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
        stopConnectivity(this)

    }

    override fun onStop() {
        super.onStop()
        threadBrand?.interrupt()
        thread?.interrupt()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer?.playWhenReady == true
        playbackPosition = mPlayer?.currentPosition!!
        currentWindow = mPlayer?.currentWindowIndex!!
        mPlayer?.release()
        mPlayer = null
    }


    private fun buildMediaSourceVideoArray(uris: ArrayList<Uri>): ConcatenatingMediaSource {
        val userAgent = Util.getUserAgent(this, "MusicPlayer")
        val defaultMediaSource = DefaultDataSourceFactory(this, userAgent)
        val progressiveMediaSource = ProgressiveMediaSource.Factory(defaultMediaSource)
        val mediaSources = ArrayList<MediaSource>()

        for (uri in uris) {
            mediaSources.add(progressiveMediaSource.createMediaSource(uri))
        }

        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(mediaSources)

        return concatenatingMediaSource
    }

    private fun buildMediaSourceSingle(uri: Uri): MediaSource? {
        return ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(this, "Exoplayer-local")).createMediaSource(uri)
    }

    fun setHorizontalAdapter(list: ArrayList<DownloadFileModel>)
    {

        adapterVisualFile = HorizontalVideoViewAdapter(list, this, this.position,this)
        horizontal_rv?.setLayoutManager(
                LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        )
        horizontal_rv?.itemAnimator = DefaultItemAnimator()
        horizontal_rv?.adapter = adapterVisualFile
    }

    //====================================== Video view bottom sheet =================================================
    inner class HorizontalVideoViewAdapter(
            var list: ArrayList<DownloadFileModel>,
            var context: Context,
            var positionConst: Int,
            var stringInterface: StoreVisualInterface,
    ) : RecyclerView.Adapter<HorizontalVideoViewAdapter.MyViewHolder>() {

        var relativeViewList: ArrayList<LinearLayout> =  ArrayList();
        private var gs: GestureDetector? = null

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var videoThumb_iv: ImageView = view.findViewById(R.id.videoThumb_iv)
            var parent_llVideo: LinearLayout = view.findViewById(R.id.parent_llVideo)
            var videoTitle: TextView = view.findViewById(R.id.videoTitle)
        }

        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.downloaedvideo_view, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HorizontalVideoViewAdapter.MyViewHolder, position: Int) {

            var downloadedfiles=list.get(position)
            holder.videoTitle.setText(list.get(position).fileName)
            holder.videoTitle.setSelected(true)

            relativeViewList.add(holder.parent_llVideo)

            if(positionConst==position)
            {
             //   holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
            }

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth =  5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOptions = RequestOptions()
            requestOptions.isMemoryCacheable
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(Uri.fromFile(File(downloadedfiles.fileDirectoryPath,downloadedfiles.fileName)))
                    .placeholder(circularProgressDrawable)
                    .into(holder.videoThumb_iv)

            if( mPlayer?.currentWindowIndex==position)
            {
                holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
                videoModel=downloadedfiles
                likeCommentColor()
            }
            else{
                holder.parent_llVideo.setBackgroundColor(Color.TRANSPARENT)

            }


            holder.parent_llVideo.setOnClickListener({
                for (currentList in relativeViewList) {
                    currentList.setBackgroundColor(Color.TRANSPARENT)
                }
                holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
                var filePath=File(downloadedfiles.filePath)


                mPlayer?.playWhenReady=false
                mPlayer?.stop()

                mPlayer?.prepare(buildMediaSourceVideoArray(uris), true, true)
                mPlayer?.seekTo(position, C.TIME_UNSET)
                mPlayer?.setPlayWhenReady(true)

                videoModel=downloadedfiles
                stringInterface.onClickString(filePath)


            })
        }

        override fun getItemCount(): Int {
            return list?.size
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

        val storecomment= dbBase?.getComment(videoModel?.fileId.toString(),startDateTime)
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

            videoModel?.let { it1 -> dbBase?.insertComment(comment_et.text.toString(), it1?.fileId,startDateTime) }
            fabComment?.setColorFilter(Color.WHITE)
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)


            alertDialog.dismiss()
            alertDialog.cancel()
        })

        alertDialog.show()
    }

    override fun onClickDisplayVisual(passingInterface: Int, brandIDInterface : Int,selectionType: Int) {

        mPlayer?.setPlayWhenReady(false)
        mPlayer?.stop()
        arrayVideo.clear()

        for (itemParent in dbBase.getAllDownloadedData(passingInterface) )
        {

            if(itemParent.downloadType.equals("VIDEO"))
            {
                arrayVideo.add(itemParent)
            }

        }
        position=0
        videoModel =arrayVideo.get(0)

        uris.clear()

        for (filePath in arrayVideo)
        {
            var myFile = File(filePath.filePath)
            uris.add(Uri.fromFile(myFile))
        }

        mPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = mPlayer
        mPlayer?.prepare(buildMediaSourceVideoArray(uris), false, false)
        mPlayer?.seekTo(position, 0)
        mPlayer?.setPlayWhenReady(true)

        setHorizontalAdapter(arrayVideo)

        eDetailingId=passingInterface
        otherFileAdapter?.notifyDataSetChanged()

        brandId=brandIDInterface
        dbBase?.insertStartTimeSlide(startDateTime,doctorId,brandId, videoModel?.brandName,0,currentTime.toString())


        isCurrent=true
        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))
        selectionBtn_parent?.visibility=View.VISIBLE
        filterListWebmage()
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        fabLike?.visibility=View.VISIBLE
        fabComment?.visibility=View.VISIBLE

        likeCommentColor()
        slideBrandWiseInsert(startDateTime,brandId)
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
                            if(mPlayer?.isPlaying == true)
                            {
                                dbBaseTimer=dbBaseTimer+1
                                Log.e("timerBrandWiseSlider",dbBaseTimer.toString())
                                dbBase?.insertBrandTime(dbBaseTimer  ,startDateTime,brandID.toString())
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        threadBrand?.start()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllEdetailingProduct() : java.util.ArrayList<DevisionModel.Data.EDetailing>
    {
        var  edetailingList = dbBase.getAlleDetail() //fetch edetailing list from db
        var  filteredList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
        for (itemParent in edetailingList )
        {
            if(itemParent.isSaved==1)
            {
                var downloadedList = dbBase.getAllDownloadedData(itemParent.geteDetailId())

                var isAvailable=false
                for(itemChild in downloadedList)
                {
                    if(itemChild.downloadType.equals("VIDEO")) isAvailable=true
                }
                if(isAvailable)  filteredList.add(itemParent); continue

              /*  if(downloadedList.stream().anyMatch({ o -> o.downloadType.equals("VIDEO") }))
                {
                    filteredList.add(itemParent)
                }*/
            }
        }
        return filteredList
    }


    fun likeCommentColor()
    {
        videoModel?.fileId?.let { dbBase?.insertFileID(it, startDateTime,brandId) }

        val isLike=dbBase?.getLike(videoModel?.fileId.toString(),startDateTime)

        if(isLike)
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

        val storecomment= dbBase?.getComment(videoModel?.fileId.toString(),startDateTime)

        if(storecomment!=null)
        {
            fabComment?.setColorFilter(Color.WHITE)
        }
        else
        {
            fabComment?.setColorFilter(Color.BLACK)
        }

    }


    fun setSlideViewTime()
    {
        thread?.interrupt()
        var dbTimer=dbBase?.getTime(videoModel?.fileId.toString(),startDateTime)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {

                            if(mPlayer?.isPlaying == true)
                            {
                                dbTimer=dbTimer+1
                                Log.e("timerSlider",dbTimer.toString())
                                videoModel?.fileId?.let {
                                    dbBase?.insertTime(dbTimer,
                                        it,startDateTime)
                                }
                            }

                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread?.start()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        thread?.interrupt()
    }

    override fun onVisibilityChange(visibility: Int) {
    }

    override fun onClickString(filePath: File?) {
        likeCommentColor()
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
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

    fun filterListWebmage()
    {
        val runnable= Runnable {  filterWebList.clear()
            filterImageList.clear()

            val dowloadedAllList=dbBase.getAllDownloadedData(eDetailingId)

            for(item in dowloadedAllList)
            {
                if(item.downloadType.equals("ZIP"))filterWebList.add(item)
                if(item.downloadType.equals("IMAGE"))filterImageList.add(item)
            }

            runOnUiThread { if(filterWebList.size==0)showWeb_mb.visibility=View.GONE
            else showWeb_mb.visibility=View.VISIBLE
                if(filterImageList.size==0)showimg_mb.visibility=View.GONE
                else showimg_mb.visibility=View.VISIBLE }

        }
        Thread(runnable).start()
    }

    override fun onBackPressed() {
        if(intent.getSerializableExtra("videoArray")!=null)
        else  super.onBackPressed()
    }
}


