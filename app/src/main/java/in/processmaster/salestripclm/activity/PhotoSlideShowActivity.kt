package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.OtherFileAdapter
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.StringInterface
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
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
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_photo_slide_show.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PhotoSlideShowActivity : BaseActivity(), View.OnClickListener , ItemClickDisplayVisual, StringInterface {

    val SWIPE_MIN_DISTANCE = 120
    val SWIPE_MAX_OFF_PATH = 150
    val SWIPE_THRESHOLD_VELOCITY = 100
    var gestureDetector: GestureDetector? = null
    var gestureListener: OnTouchListener? = null
    var imageFrame: ViewFlipper? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    var position=0
    var parentRl: RelativeLayout? =null
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetWeb: ConstraintLayout
    var end_btn: Button? =null
    var isList=false
    var horizontal_rv:RecyclerView?=null
    var brandId=0
    var empId=0
    var startDateTime=""
    var doctorId=0
    var db : DatabaseHandler? = null
    var currentProduct_btn: Button?= null
    var otherProduct_btn: Button?= null
    var isCurrent=true;
    var otherFileAdapter :OtherFileAdapter?=null
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var adapterVisualFile: HorizontalImageViewAdapter? =null
    var arrayImage: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
    var eDetailingId=0
    var fab_send : FloatingActionButton?= null
    var fabLike : FloatingActionButton?= null
    var fabComment : FloatingActionButton?= null
    var mViewPagerAdapter: ViewPagerAdapter? = null
    var productParent_ll: LinearLayout?=null


    companion object {
        var model : DownloadFileModel?= null
    }

    var thread: Thread?= null
    var threadBrand: Thread?= null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_slide_show)

        imageFrame = findViewById(R.id.imageFrames) as ViewFlipper

        parentRl = findViewById(R.id.parentRl) as RelativeLayout
        productParent_ll = findViewById(R.id.productParent_ll) as LinearLayout

        horizontal_rv = findViewById(R.id.horizontal_rv)
        fab_send = findViewById(R.id.fab_send)

        currentProduct_btn = findViewById(R.id.currentProduct_btn)
        otherProduct_btn = findViewById(R.id.otherProduct_btn)

        fabLike    = findViewById<View>(R.id.fabLike) as FloatingActionButton
        fabComment = findViewById<View>(R.id.fabComment) as FloatingActionButton

        bottomSheetWeb=findViewById(R.id.bottomSheetWeb)as ConstraintLayout
        end_btn=findViewById(R.id.end_btn)as Button

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWeb)

        db=DatabaseHandler(this)

        horizontal_rv?.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        horizontal_rv?.itemAnimator = DefaultItemAnimator()


        if(intent.getSerializableExtra("imageArray")!=null)
        {
            arrayImage = intent.getSerializableExtra("imageArray") as ArrayList<DownloadFileModel>
            model = intent.getSerializableExtra("model") as DownloadFileModel

            position = intent.getIntExtra("position", 0)

            setImageArray(arrayImage,position,model!!)

            if(!model?.favFileName?.isEmpty()!!)
            {
                productParent_ll?.visibility= GONE
            }

            //getData
            val intent = intent
            //  val webUrlPath = intent.getStringExtra("webUrlPath")
            empId = intent.getIntExtra("empId", 0)
            startDateTime = intent.getStringExtra("currentDateTime").toString()
            brandId = intent.getIntExtra("brandId", 0)
            doctorId = intent.getIntExtra("doctorId", 0)
            //  val file = File(webUrlPath)

            db?.insertFileID(model!!.fileId,startDateTime)

            eDetailingId= model?.geteDetailingId()!!

            end_btn?.setOnClickListener({

                val currentDate: String = SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )
                db?.updateBrandEndTime(currentTime.toString(),startDateTime,doctorId,brandId)
                db?.updateendData(currentDate + " " + currentTime,startDateTime)
                onBackPressed()
                finish()

                /*     if (doctorId != 0) {
                         if (!startDateTime!!.isEmpty()) {

                             val currentDate: String = SimpleDateFormat(
                                 "dd-MM-yyyy",
                                 Locale.getDefault()
                             ).format(Date())
                             val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                                 Date()
                             )

                             var visualAdsModel = VisualAdsModel_Send()
                             visualAdsModel.setBrandId(brandId.toString())
                             visualAdsModel.setDoctorId(doctorId.toString())
                             visualAdsModel.setStartDate(startDateTime)
                             visualAdsModel.setEmpId(empId.toString())
                             visualAdsModel.setEndDate(currentDate + " " + currentTime)
                             val gson = Gson()

                         }
                     } else {
                         onBackPressed()
                         finish()
                     }*/
            })



            fabLike?.setOnClickListener({
                if (isList) {
                    db?.insertlike(0, model!!.fileId,startDateTime)
                    fabLike?.setColorFilter(Color.BLACK)
                    isList = false
                }
                else {
                    db?.insertlike(1,model!!.fileId,startDateTime)
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

                    setHorizontalAdapter(arrayImage, position,model!!)

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

            fab_send?.setOnClickListener({
                var intent = Intent(this, ImageSelectorActivity::class.java)
                intent.putExtra("filePath", model?.fileDirectoryPath)
                startActivity(intent)
            })

            setSlideViewTime()


            viewPagerMain.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    model=arrayImage.get(position)
                    setHorizontalAdapter(arrayImage, position, model!!)
                    adapterVisualFile?.notifyDataSetChanged()

                    likeCommentColor()
                }
                override fun onPageSelected(position: Int) {

                    //   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    //   model=arrayImage.get(position)
                    //   setHorizontalAdapter(arrayImage, position, model!!)
                    //   adapterVisualFile?.notifyDataSetChanged()


                }
                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            slideBrandWiseInsert(startDateTime,brandId)
        }

        if(intent.getStringExtra("singleSelection")!=null)
        {
            fab_send?.visibility=View.GONE

            var  selection = intent.getStringExtra("singleSelection")
            var  model = intent.getSerializableExtra("model") as DownloadFileModel


            val imageView = ImageView(this)
            val imbm = BitmapFactory.decodeFile(selection)
            imageView.setImageBitmap(imbm)
            imageFrame!!.addView(imageView)

            floating_action_button_image.visibility=View.VISIBLE

            var arrayImage: ArrayList<DownloadFileModel> = ArrayList()
            arrayImage.add(model)

            mViewPagerAdapter = ViewPagerAdapter(this, arrayImage)
            viewPagerMain.adapter = mViewPagerAdapter
        }

        floating_action_button_image.setOnClickListener({
            onBackPressed()
        })



    }

    fun setImageArray(arrayImage: ArrayList<DownloadFileModel>, position: Int, model: DownloadFileModel)
    {
        if(arrayImage.size==1)
        {
            val imageView = ImageView(this)
            val imbm = BitmapFactory.decodeFile(arrayImage.get(0).filePath)
            imageView.setImageBitmap(imbm)
            imageFrame!!.addView(imageView)

            mViewPagerAdapter = ViewPagerAdapter(this, arrayImage)
            viewPagerMain.adapter = mViewPagerAdapter

            if(intent.getSerializableExtra("imageArray")!=null)
            {
                gestureDetector = GestureDetector(this,MyGestureDetector())
                gestureListener = OnTouchListener { v, event -> if (gestureDetector!!.onTouchEvent(event)) true else false }
                setHorizontalAdapter(arrayImage, position,model)
                imageFrame!!.setOnTouchListener(gestureListener)
                imageFrame!!.setOnClickListener(this@PhotoSlideShowActivity)
            }

        }
        else
        {
            addFlipperImages(imageFrame!!, arrayImage, position)

            mViewPagerAdapter = ViewPagerAdapter(this, arrayImage)
            viewPagerMain.adapter = mViewPagerAdapter

            viewPagerMain.setCurrentItem(position);

            gestureDetector = GestureDetector(this,MyGestureDetector())
            gestureListener = OnTouchListener { v, event -> if (gestureDetector!!.onTouchEvent(event)) true else false }

            handler = Handler(Looper.getMainLooper())
            imageFrame!!.setOnClickListener(this@PhotoSlideShowActivity)
            imageFrame!!.setOnTouchListener(gestureListener)
            //   slideShowBtn = findViewById<View>(R.id.slideShowBtn) as TextView

            /*    slideShowBtn!!.setOnClickListener({
                    slideShowBtn?.visibility=View.GONE
                    runnable = Runnable {
                        handler!!.postDelayed(runnable!!, 3000)
                        imageFrame?.showNext()
                    }
                    handler!!.postDelayed(runnable!!, 500)
                })*/
            setHorizontalAdapter(arrayImage, position,model)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAllEdetailingProduct() : java.util.ArrayList<DevisionModel.Data.EDetailing>
    {
        var  edetailingList = db?.getAlleDetail() //fetch edetailing list from db
        var  filteredList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
        for (itemParent in edetailingList!! )
        {
            if(itemParent.isSaved==1)
            {
                var downloadedList = db?.getAllDownloadedData(itemParent.geteDetailId())

                if(downloadedList?.stream()!!.anyMatch({ o -> o.downloadType.equals("IMAGE") }))
                {
                    filteredList.add(itemParent)
                }
            }
        }
        return filteredList
    }

    private fun addFlipperImages(flipper: ViewFlipper, parent: ArrayList<DownloadFileModel>, position: Int) {

        flipper.removeAllViews()

        for (model in parent) {
            val imageView = ImageView(this)
            val imbm = BitmapFactory.decodeFile(model.filePath)
            imageView.setImageBitmap(imbm)
            flipper.addView(imageView)
        }

        flipper.setDisplayedChild(position)

    }



    inner class MyGestureDetector : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean
        {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)


            /*      if(floating_action_button_image.visibility==View.VISIBLE)
                  {
                      floating_action_button_image.visibility=View.GONE
                  }
                  else
                  {
                      floating_action_button_image.visibility=View.VISIBLE

                  }*/

            return true
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean
        {
            floating_action_button_image.visibility=View.GONE
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            return true
        }


        override fun onSingleTapUp(e: MotionEvent): Boolean {


            return true
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {


                if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH)
                {
                    return false
                }
                // right to left swipe
                if (e1.x - e2.x > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                    if(imageFrame?.displayedChild!! < arrayImage.size-1)
                    {

                        runnable?.let { handler?.removeCallbacks(it) }
                        imageFrame?.setInAnimation(inFromRightAnimation())
                        imageFrame?.setOutAnimation(outToLeftAnimation())

                        imageFrame?.showNext()

                        position= imageFrame?.getDisplayedChild()!!
                        model=arrayImage.get(position)
                        setImageArray(arrayImage,position,model!!)
                        adapterVisualFile?.notifyDataSetChanged()

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

                        db?.insertFileID(model!!.fileId, startDateTime)
                        setSlideViewTime()
                    }
                }

                else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                    //commonlib/
                    //mobilertc/


                    if(imageFrame?.displayedChild!! == 0)
                    {

                    }
                    else
                    {
                        runnable?.let { handler?.removeCallbacks(it) }
                        imageFrame?.setInAnimation(inFromLeftAnimation())
                        imageFrame?.setOutAnimation(outToRightAnimation())

                        imageFrame?.showPrevious()

                        position= imageFrame?.getDisplayedChild()!!
                        model=arrayImage.get(position)
                        setImageArray(arrayImage,position,model!!)
                        adapterVisualFile?.notifyDataSetChanged()

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

                        db?.insertFileID(model!!.fileId, startDateTime)
                        setSlideViewTime()
                    }
                }



            } catch (e: Exception) {
                // nothing
            }
            return false
        }
    }

    override fun onClick(view: View?) {}

    private fun inFromRightAnimation(): Animation? {
        val inFromRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, +1.2f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        inFromRight.duration = 200
        inFromRight.interpolator = AccelerateInterpolator()
        return inFromRight
    }

    private fun outToLeftAnimation(): Animation? {
        val outtoLeft: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, -1.2f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        outtoLeft.duration = 200
        outtoLeft.interpolator = AccelerateInterpolator()
        return outtoLeft
    }

    private fun inFromLeftAnimation(): Animation? {
        val inFromLeft: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.2f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        inFromLeft.duration = 200
        inFromLeft.interpolator = AccelerateInterpolator()
        return inFromLeft
    }

    private fun outToRightAnimation(): Animation? {
        val outtoRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, +1.2f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        outtoRight.duration = 200
        outtoRight.interpolator = AccelerateInterpolator()
        return outtoRight
    }

    fun setHorizontalAdapter(list: ArrayList<DownloadFileModel>, position: Int, model: DownloadFileModel)
    {
        adapterVisualFile  = HorizontalImageViewAdapter(list, this, position,model,imageFrame,this)
        horizontal_rv?.adapter = adapterVisualFile
    }


    //======================================Image view bottom sheet=================================================
    inner class HorizontalImageViewAdapter(
        var list: ArrayList<DownloadFileModel>,
        var context: Context,
        var positionConst: Int,
        var modeladapter: DownloadFileModel,
        var imageFrame: ViewFlipper?,
        var mCallback :StringInterface
    ) : RecyclerView.Adapter<HorizontalImageViewAdapter.MyViewHolder>() {

        var relativeViewList: ArrayList<LinearLayout> =  ArrayList();
        private var gs: GestureDetector? = null

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var imageThumb_iv: ImageView = view.findViewById(R.id.pics_iv)
            var parent_llImage: LinearLayout = view.findViewById(R.id.parent_llVideo)
            var imageTitle: TextView = view.findViewById(R.id.imageTitle)

        }

        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.downloaedimage_view, parent, false)


            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HorizontalImageViewAdapter.MyViewHolder, position: Int) {

            var downloadedfiles=list.get(position)
            holder.imageTitle.setText(list.get(position).fileName)
            holder.imageTitle.setSelected(true)

            relativeViewList.add(holder.parent_llImage)


            if(modeladapter.filePath.equals(downloadedfiles.filePath))
            {
                holder.parent_llImage.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
            }

            holder.parent_llImage.setOnClickListener({
                for (currentList in relativeViewList) {
                    currentList.setBackgroundColor(Color.TRANSPARENT)
                }
                holder.parent_llImage.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));

                imageFrame!!.setDisplayedChild(position)
                model=downloadedfiles
                viewPagerMain.setCurrentItem(position);

                mCallback.onClickString("callBack")


            })

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOptions = RequestOptions()
            requestOptions.isMemoryCacheable
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(Uri.fromFile(File(downloadedfiles.fileDirectoryPath,downloadedfiles.fileName)))
                .placeholder(circularProgressDrawable)
                .into(holder.imageThumb_iv)

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

        val storecomment= db?.getComment(model!!.fileId.toString(),startDateTime)
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

            db?.insertComment(comment_et.text.toString(),model!!.fileId,startDateTime)

            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)
            fabComment?.setColorFilter(Color.WHITE)

            alertDialog.dismiss()
            alertDialog.cancel()
        })

        alertDialog.show()
    }

    override fun onClickDisplayVisual(passingInterface: Int, brandIDInterface: Int,selectionType: Int)
    {

        db?.updateBrandEndTime(currentTime.toString(),startDateTime,doctorId,brandId)

        arrayImage.clear()

        for (itemParent in db!!.getAllDownloadedData(passingInterface) )
        {

            if(itemParent.downloadType.equals("IMAGE"))
            {
                arrayImage.add(itemParent)
            }

        }

        position=0
        model=arrayImage.get(0)

        imageFrame?.removeAllViews()

        setImageArray(arrayImage,0,model!!)

        isCurrent=true
        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.gray))
        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(this,R.color.appColor))

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        fabLike?.visibility=View.VISIBLE
        fabComment?.visibility=View.VISIBLE

        eDetailingId=passingInterface
        otherFileAdapter?.notifyDataSetChanged()

        brandId=brandIDInterface
        db?.insertStartTimeSlide(startDateTime,doctorId,brandId,model?.brandName,0,currentTime.toString())

        onClickString("")

        mViewPagerAdapter = ViewPagerAdapter(this, arrayImage)
        viewPagerMain.adapter = mViewPagerAdapter

        slideBrandWiseInsert(startDateTime,brandId)
    }


    override fun onClickString(passingInterface: String?)
    {

        likeCommentColor()

    }

    fun likeCommentColor()
    {
        db?.insertFileID(model!!.fileId, startDateTime)

        val isLike=db?.getLike(model!!.fileId.toString(),startDateTime)

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

        val storecomment= db?.getComment(model!!.fileId.toString(),startDateTime)

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
        var dbTimer=db?.getTime(model!!.fileId.toString(),startDateTime)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            dbTimer=dbTimer!!+1

                            //  Log.e("timerSlider",dbTimer.toString())
                            db?.insertTime(dbTimer!!, model!!.fileId ,startDateTime)
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

    //set image adapter
    inner class ViewPagerAdapter(context: Context, images: java.util.ArrayList<DownloadFileModel>) : PagerAdapter() {

        var context: Context
        var images: java.util.ArrayList<DownloadFileModel>
        var mLayoutInflater: LayoutInflater
        var doubleClick = false

        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as LinearLayout
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val itemView = mLayoutInflater.inflate(R.layout.itemexp, container, false)
            val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView
            val model = images[position]
            imageView.setImageBitmap(BitmapFactory.decodeFile(model.filePath))
            Objects.requireNonNull(container).addView(itemView)

            imageView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    if(intent.getSerializableExtra("imageArray")!=null)
                    {
                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                doubleClick = false
                            }, 200)

                        if (doubleClick)
                        {
                            floating_action_button_image.visibility=View.GONE
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                        }
                        else
                        {
                            doubleClick = true
                        }
                    }
                }
            })

            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
        {
            (container as ViewPager).removeView(`object` as View)
        }

        init {
            this.context = context
            this.images = images
            mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

    fun slideBrandWiseInsert(startDateTime: String,brandID:Int)
    {
        threadBrand?.interrupt()
        var dbTimer=db?.getBrandTime(brandID.toString(),startDateTime)
        threadBrand = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            dbTimer=dbTimer!!+1

                            Log.e("timerBrandWiseSlider",dbTimer.toString())
                            db?.insertBrandTime(dbTimer!!  ,startDateTime,brandID.toString())
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

}