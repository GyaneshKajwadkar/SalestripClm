package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.OtherFileAdapter
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
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
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_video_player.*
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VideoPlayerActivity : BaseActivity() , ItemClickDisplayVisual, PlayerControlView.VisibilityListener
{

    private var mPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
     var uris:ArrayList<Uri> = ArrayList()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetWeb: ConstraintLayout
    var end_btn: Button? =null
    var horizontal_rv: RecyclerView?=null
    var position=0
    var brandId=0
    var empId=0
    var startDateTime=""
    var doctorId=0
    var db = DatabaseHandler(this)
    var isList=false

    var eDetailingId=0
    var currentProduct_btn: Button?= null
    var otherProduct_btn: Button?= null
    var isCurrent=true;
    var otherFileAdapter : OtherFileAdapter?=null
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var arrayVideo: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
    var fabLike : FloatingActionButton?= null
    var fabComment : FloatingActionButton?= null
    var productParent_ll: LinearLayout?=null

    var doubleclick= false
    var thread: Thread?= null


    companion object {
        var videoModel : DownloadFileModel?= null
    }

    private val hlsUrl =
        "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"

    override fun onCreate (savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)
        bottomSheetWeb=findViewById(R.id.bottomSheetWeb) as ConstraintLayout
        end_btn=findViewById(R.id.end_btn) as Button
        horizontal_rv = findViewById<RecyclerView>(R.id.horizontal_rv)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWeb)
        productParent_ll = findViewById(R.id.productParent_ll) as LinearLayout

        currentProduct_btn = findViewById<Button> (R.id.currentProduct_btn)
        otherProduct_btn = findViewById<Button> (R.id.otherProduct_btn)

        // uris.add(URL(hlsUrl))
      // uris.add(URL("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"))
      // uris.add(URL("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"))

        floating_action_backBtn.setOnClickListener({
            onBackPressed()
        })

        playerView.setControllerVisibilityListener(object : PlayerControlView.VisibilityListener {
            override fun onVisibilityChange(i: Int) {

                if(intent.getSerializableExtra("videoArray")==null)
                {
                    if (playerView?.isControllerVisible())
                    {
                        floating_action_backBtn.visibility=View.VISIBLE
                        // Do something if controls are visible
                    }
                    else
                    {
                        floating_action_backBtn.visibility=View.GONE
                        // Do something else if controls are not showing
                    }
                }

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initPlayer() {
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Bind the player to the view.
        playerView.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.seekTo(playbackPosition)

     //   mPlayer!!.prepare(buildMediaSource(uris), false, false)

        if(intent.getSerializableExtra("videoArray")!=null)
        {
            arrayVideo = intent.getSerializableExtra("videoArray") as ArrayList<DownloadFileModel>
            videoModel = intent.getSerializableExtra("model") as DownloadFileModel

            if(!videoModel?.favFileName?.isEmpty()!!)
            {
                productParent_ll?.visibility= View.GONE
            }


            position = intent.getIntExtra("position", 0)
            if(arrayVideo!=null )
            {
                for (filePath in arrayVideo)
                {
                    var myFile = File(filePath.filePath)
                    uris.add(Uri.fromFile(myFile))
                }
                mPlayer!!.prepare(buildMediaSourceVideoArray(uris), false, false)
                mPlayer!!.seekTo(position, 0)
                mPlayer!!.setPlayWhenReady(true)

                setHorizontalAdapter(arrayVideo,position, videoModel!!)

                mPlayer!!.addListener(object : Player.EventListener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {

                      /*  if(playWhenReady) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                        }*/

                    }
                })

                eDetailingId= videoModel?.geteDetailingId()!!

                //getData
                val intent = intent
                //  val webUrlPath = intent.getStringExtra("webUrlPath")
                empId = intent.getIntExtra("empId", 0)
                startDateTime = intent.getStringExtra("currentDateTime").toString()
                brandId = intent.getIntExtra("brandId", 0)
                doctorId = intent.getIntExtra("doctorId", 0)
                //  val file = File(webUrlPath)


                db?.insertFileID(videoModel!!.fileId,startDateTime)
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

                 fabLike    = findViewById<View>(R.id.fabLike) as FloatingActionButton
                 fabComment = findViewById<View>(R.id.fabComment) as FloatingActionButton

                fabLike?.setOnClickListener({

                    if (isList) {
                        db?.insertlike(0, videoModel!!.fileId,startDateTime)
                        fabLike?.setColorFilter(Color.BLACK)
                        isList = false
                    }
                    else {
                        db?.insertlike(1, videoModel!!.fileId,startDateTime)
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
                        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
                        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))
                        fabLike?.visibility=View.VISIBLE
                        fabComment?.visibility=View.VISIBLE

                        setHorizontalAdapter(arrayVideo, position, videoModel!!)

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

                        isCurrent=false
                    }
                })
            }

            playerView.getVideoSurfaceView()?.setOnClickListener({ view ->

                if(doubleclick)
                {
                    mPlayer!!.setPlayWhenReady(false)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleclick=false
                }, 300)

                doubleclick=true

                playerView.showController()

            })


        }


        if(intent.getStringExtra("singleSelection")!=null)
        {
            var  selection = intent.getStringExtra("singleSelection")

            val myFile=File(selection)
          var  videoUrl = Uri.fromFile(myFile).toString()
          val uri = Uri.parse(videoUrl)
          val mediaSource: MediaSource = buildMediaSourceSingle(uri)!!

          mPlayer!!.prepare(mediaSource, false, false)

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
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }

    private fun buildMediaSource(): MediaSource {
        val userAgent =
            Util.getUserAgent(playerView.context, playerView.context.getString(R.string.app_name))

        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        val hlsMediaSource =
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(hlsUrl))

        return hlsMediaSource
    }

    private fun buildMediaSource(uris: ArrayList<URL>): ConcatenatingMediaSource {
        val userAgent = Util.getUserAgent(this, "MusicPlayer")
        val defaultMediaSource = DefaultDataSourceFactory(this, userAgent)
        val progressiveMediaSource = ProgressiveMediaSource.Factory(defaultMediaSource)
        val mediaSources = ArrayList<MediaSource>()

        for (uri in uris) {
            mediaSources.add(progressiveMediaSource.createMediaSource(Uri.parse(uri.toString())))
        }

        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(mediaSources)

        return concatenatingMediaSource
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

    fun setHorizontalAdapter(list: ArrayList<DownloadFileModel>, position: Int, videoModel: DownloadFileModel)
    {

        val adapterVisualFile = HorizontalVideoViewAdapter(list, this, this.position)
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
    class HorizontalVideoViewAdapter(
            var list: ArrayList<DownloadFileModel>,
            var context: Context,
            var positionConst: Int
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
                holder.parent_llVideo.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
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
        }

        override fun getItemCount(): Int {
            return list?.size!!
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

        val storecomment= db?.getComment(videoModel!!.fileId.toString(),startDateTime)
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

            db?.insertComment(comment_et.text.toString(), videoModel!!.fileId,startDateTime)
            fabComment?.setColorFilter(Color.WHITE)
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)


            alertDialog.dismiss()
            alertDialog.cancel()
        })

        alertDialog.show()
    }

    override fun onClickDisplayVisual(passingInterface: Int, brandID : Int,selectionType: Int) {

        mPlayer!!.setPlayWhenReady(false)
        mPlayer!!.stop()
        arrayVideo.clear()

        for (itemParent in db.getAllDownloadedData(passingInterface) )
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
        mPlayer!!.prepare(buildMediaSourceVideoArray(uris), false, false)
        mPlayer!!.seekTo(position, 0)
        mPlayer!!.setPlayWhenReady(false)

        setHorizontalAdapter(arrayVideo, position, videoModel!!)

        eDetailingId=passingInterface
        otherFileAdapter?.notifyDataSetChanged()

        isCurrent=true
        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        fabLike?.visibility=View.VISIBLE
        fabComment?.visibility=View.VISIBLE

        likeCommentColor()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllEdetailingProduct() : java.util.ArrayList<DevisionModel.Data.EDetailing>
    {
        var  edetailingList = db.getAlleDetail() //fetch edetailing list from db
        var  filteredList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
        for (itemParent in edetailingList )
        {
            if(itemParent.isSaved==1)
            {
                var downloadedList = db.getAllDownloadedData(itemParent.geteDetailId())

                if(downloadedList.stream().anyMatch({ o -> o.downloadType.equals("VIDEO") }))
                {
                    filteredList.add(itemParent)
                }
            }
        }
        return filteredList
    }


    fun likeCommentColor()
    {
        db?.insertFileID(videoModel!!.fileId, startDateTime)

        val isLike=db?.getLike(videoModel!!.fileId.toString(),startDateTime)

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

        val storecomment= db?.getComment(videoModel!!.fileId.toString(),startDateTime)

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
        var dbTimer=db?.getTime(videoModel!!.fileId.toString(),startDateTime)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {

                            if(mPlayer?.isPlaying!!)
                            {
                                dbTimer=dbTimer!!+1
                                Log.e("timerSlider",dbTimer.toString())
                                db?.insertTime(dbTimer!!, videoModel!!.fileId ,startDateTime)
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





}


