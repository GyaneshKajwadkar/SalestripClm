package `in`.processmaster.salestripclm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import `in` .processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.ImageSelectorActivity
import `in`.processmaster.salestripclm.activity.PhotoSlideShowActivity
import `in`.processmaster.salestripclm.adapter.OtherFileAdapter
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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_photo_slide_show.view.*
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.web_bottom_sheet.*
import kotlinx.android.synthetic.main.web_bottom_sheet.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PhotoFragment : Fragment(), View.OnClickListener , ItemClickDisplayVisual, StringInterface  {

    val SWIPE_MIN_DISTANCE = 120
    val SWIPE_MAX_OFF_PATH = 150
    val SWIPE_THRESHOLD_VELOCITY = 100
    var gestureDetector: GestureDetector? = null
    var gestureListener: View.OnTouchListener? = null
    var imageFrame: ViewFlipper? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    var position=0
    var parentRl: RelativeLayout? =null
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetWeb: ConstraintLayout
    var isList=false
    var brandId=0
    var empId=0
    var startDateTime=""
    var doctorId=0

    var isCurrent=true;
    var otherFileAdapter : OtherFileAdapter?=null
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var adapterVisualFile: HorizontalImageViewAdapter? =null
    var arrayImage: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
    var eDetailingId=0

    var mViewPagerAdapter: ViewPagerAdapter? = null
    var productParent_ll: LinearLayout?=null
    lateinit var dbBase :DatabaseHandler

    companion object {
        var model : DownloadFileModel?= null
    }

    var thread: Thread?= null
    var threadBrand: Thread?= null
    lateinit var viewFrag : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewFrag= inflater.inflate(R.layout.fragment_photo, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWeb)
        dbBase= DatabaseHandler(requireActivity())

        viewFrag.horizontal_rv?.setLayoutManager(
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
     //   viewFrag.horizontal_rv?.itemAnimator = DefaultItemAnimator()

        viewFrag.floating_action_button_image.setOnClickListener({})


     /*   if(intent.getSerializableExtra("imageArray")!=null)
        {
            alertClass.showProgressAlert("")
        }*/
        val runnable= Runnable { initView() }
        Thread(runnable).start()

        return  viewFrag
    }

    fun initView(){
        if(arguments?.getString("imageArray")!=null)
        {

            arrayImage = arguments?.getString("imageArray") as ArrayList<DownloadFileModel>
            PhotoSlideShowActivity.model =arguments?.getString("model") as DownloadFileModel

            position = arguments?.getInt("position", 0)!!

            setImageArray(arrayImage,position, PhotoSlideShowActivity.model)

          //  val intent = intent
            //  val webUrlPath = intent.getStringExtra("webUrlPath")
            empId = arguments?.getInt("empId")!!
            // startDateTime = intent.getStringExtra("currentDateTime").toString()
            startDateTime = ShowDownloadedFragment.currentDate + " " + ShowDownloadedFragment.currentTime
            brandId = arguments?.getInt("brandId", 0)!!
            doctorId = arguments?.getInt("doctorId", 0)!!
            //  val file = File(webUrlPath)

            PhotoSlideShowActivity.model?.fileId?.let { dbBase?.insertFileID(it,startDateTime,brandId) }

            eDetailingId= PhotoSlideShowActivity.model?.eDetailingId!!

            viewFrag.end_btn?.setOnClickListener({

                val currentDate: String = SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )
                dbBase?.updateendData(currentDate + " " + currentTime,startDateTime)
                requireActivity().onBackPressed()
                requireActivity().finish()
            })


            viewFrag.fabLike?.setOnClickListener({
                if (isList) {
                    PhotoSlideShowActivity.model?.fileId?.let { it1 -> dbBase?.insertlike(0, it1,startDateTime) }
                    fabLike?.setColorFilter(Color.BLACK)
                    isList = false
                }
                else {
                    PhotoSlideShowActivity.model?.fileId?.let { it1 -> dbBase?.insertlike(1, it1,startDateTime) }
                    fabLike?.setColorFilter(Color.WHITE)
                    isList = true
                }


            })

            viewFrag.fabComment?.setOnClickListener({
                commentDialog()
            })

            viewFrag.currentProduct_btn?.setOnClickListener({
                if(!isCurrent)
                {
                    otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.gray))
                    currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.appColor))
                    fabLike?.visibility=View.VISIBLE
                    fabComment?.visibility=View.VISIBLE

                    setHorizontalAdapter(arrayImage, position, PhotoSlideShowActivity.model)

                    isCurrent=true
                    isCurrent=true
                }
            })


            otherProduct_btn?.setOnClickListener({
                if(isCurrent)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        edetailingList = getAllEdetailingProduct()
                    }

                    otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.appColor))
                    currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.gray))
                    fabLike?.visibility=View.GONE
                    fabComment?.visibility=View.GONE

                    otherFileAdapter = OtherFileAdapter(edetailingList,requireActivity(),this, eDetailingId)
                    horizontal_rv?.adapter = otherFileAdapter
                    otherFileAdapter?.notifyDataSetChanged()

                    isCurrent=false
                }

            })

            viewFrag.fab_send?.setOnClickListener({
             /*   var intent = Intent(this, ImageSelectorActivity::class.java)
                intent.putExtra("filePath", PhotoSlideShowActivity.model?.fileDirectoryPath)
                startActivity(intent)*/
            })

            setSlideViewTime()


            viewFrag.viewPagerMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    PhotoSlideShowActivity.model =arrayImage.get(position)
                    setHorizontalAdapter(arrayImage, position, PhotoSlideShowActivity.model)
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
          //  requireActivity().runOnUiThread {  alertClass.hideAlert() }

        }

        if(arguments?.getString("singleSelection")!=null)
        {


            var  selection = arguments?.getString("singleSelection")
            var  model = arguments?.getString("model") as DownloadFileModel


            val imageView = ImageView(requireActivity())
            val imbm = BitmapFactory.decodeFile(selection)

            var arrayImage: ArrayList<DownloadFileModel> = ArrayList()
            arrayImage.add(model)

            requireActivity().runOnUiThread {
                viewFrag.fab_send?.visibility=View.GONE

                imageView.setImageBitmap(imbm)
                imageFrame?.addView(imageView)
                viewFrag.floating_action_button_image.visibility=View.VISIBLE

                mViewPagerAdapter = ViewPagerAdapter(requireActivity(), arrayImage)
                viewFrag.viewPagerMain.adapter = mViewPagerAdapter
            }

        }

    }

    fun setImageArray(arrayImage: ArrayList<DownloadFileModel>, position: Int, model: DownloadFileModel?)
    {
        if(arrayImage.size==1)
        {
            val imageView = ImageView(requireActivity())
            val imbm = BitmapFactory.decodeFile(arrayImage.get(0).filePath)
            imageView.setImageBitmap(imbm)
            imageFrame?.addView(imageView)

            mViewPagerAdapter = ViewPagerAdapter(requireActivity(), arrayImage)
            viewFrag.viewPagerMain.adapter = mViewPagerAdapter

            if(arguments?.getString("imageArray")!=null)
            {
                gestureDetector = GestureDetector(requireActivity(),MyGestureDetector())
                gestureListener =
                    View.OnTouchListener { v, event -> if (gestureDetector?.onTouchEvent(event) == true) true else false }
                setHorizontalAdapter(arrayImage, position,model)
                imageFrame?.setOnTouchListener(gestureListener)
               // imageFrame?.setOnClickListener(this)
            }

        }
        else
        {


            mViewPagerAdapter = ViewPagerAdapter(requireActivity(), arrayImage)


            requireActivity().runOnUiThread {
                imageFrame?.let { addFlipperImages(it, arrayImage, position) }

                viewFrag.viewPagerMain.adapter = mViewPagerAdapter

                viewFrag.viewPagerMain.setCurrentItem(position);
                gestureDetector = GestureDetector(requireActivity(),MyGestureDetector())
                gestureListener =
                    View.OnTouchListener { v, event -> if (gestureDetector?.onTouchEvent(event) == true) true else false }
            }


            handler = Handler(Looper.getMainLooper())
          //  imageFrame?.setOnClickListener(this@PhotoSlideShowActivity)
            imageFrame?.setOnTouchListener(gestureListener)
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
        var  edetailingList = dbBase?.getAlleDetail() //fetch edetailing list from dbBase
        var  filteredList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
        for (itemParent in edetailingList )
        {
            if(itemParent.isSaved==1)
            {
                var downloadedList = dbBase?.getAllDownloadedData(itemParent.geteDetailId())

                if(downloadedList?.stream()?.anyMatch({ o -> o.downloadType.equals("IMAGE") }) == true)
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
            val imageView = ImageView(requireActivity())
            val imbm = BitmapFactory.decodeFile(model.filePath)
            requireActivity().runOnUiThread{
                imageView.setImageBitmap(imbm)
                flipper.addView(imageView)
            }
        }

        flipper.setDisplayedChild(position)

    }



    inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
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
                        PhotoSlideShowActivity.model =arrayImage.get(position)
                        setImageArray(arrayImage,position, PhotoSlideShowActivity.model)
                        adapterVisualFile?.notifyDataSetChanged()

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

                        PhotoSlideShowActivity.model?.fileId?.let { dbBase?.insertFileID(it, startDateTime,brandId) }
                        setSlideViewTime()
                    }
                }

                else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                    //commonlib/
                    //mobilertc/


                    if(imageFrame?.displayedChild == 0)
                    {

                    }
                    else
                    {
                        runnable?.let { handler?.removeCallbacks(it) }
                        imageFrame?.setInAnimation(inFromLeftAnimation())
                        imageFrame?.setOutAnimation(outToRightAnimation())

                        imageFrame?.showPrevious()

                        position= imageFrame?.getDisplayedChild()!!
                        PhotoSlideShowActivity.model =arrayImage.get(position)
                        setImageArray(arrayImage,position, PhotoSlideShowActivity.model)
                        adapterVisualFile?.notifyDataSetChanged()

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

                        PhotoSlideShowActivity.model?.fileId?.let { dbBase?.insertFileID(it, startDateTime,brandId) }
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

    fun setHorizontalAdapter(list: ArrayList<DownloadFileModel>, position: Int, model: DownloadFileModel?)
    {
        adapterVisualFile  =
            model?.let { HorizontalImageViewAdapter(list, requireActivity(), position, it,imageFrame,this) }
        requireActivity().runOnUiThread {
            horizontal_rv?.adapter = adapterVisualFile
        }
    }


    //======================================Image view bottom sheet=================================================
    inner class HorizontalImageViewAdapter(
        var list: ArrayList<DownloadFileModel>,
        var context: Context,
        var positionConst: Int,
        var modeladapter: DownloadFileModel,
        var imageFrame: ViewFlipper?,
        var mCallback : StringInterface
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

                imageFrame?.setDisplayedChild(position)
                PhotoSlideShowActivity.model =downloadedfiles
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
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.commentdialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val cancel_btn = dialogView.findViewById(R.id.cancel_btn) as Button
        val post_btn = dialogView.findViewById(R.id.post_btn) as Button
        val comment_et = dialogView.findViewById(R.id.comment_et) as EditText

        val storecomment= dbBase?.getComment(PhotoSlideShowActivity.model?.fileId.toString(),startDateTime)
        comment_et.setText(storecomment)

        cancel_btn.setOnClickListener({

            val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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

            PhotoSlideShowActivity.model?.fileId?.let { it1 ->
                dbBase?.insertComment(comment_et.text.toString(),
                    it1,startDateTime)
            }

            val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0)
            fabComment?.setColorFilter(Color.WHITE)

            alertDialog.dismiss()
            alertDialog.cancel()
        })

        alertDialog.show()
    }

    override fun onClickDisplayVisual(passingInterface: Int, brandIDInterface: Int,selectionType: Int)
    {

        arrayImage.clear()

        for (itemParent in dbBase?.getAllDownloadedData(passingInterface) )
        {

            if(itemParent.downloadType.equals("IMAGE"))
            {
                arrayImage.add(itemParent)
            }

        }

        position=0
        PhotoSlideShowActivity.model =arrayImage.get(0)

        imageFrame?.removeAllViews()

        setImageArray(arrayImage,0, PhotoSlideShowActivity.model)

        isCurrent=true
        otherProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.gray))
        currentProduct_btn?.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.appColor))

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        fabLike?.visibility=View.VISIBLE
        fabComment?.visibility=View.VISIBLE

        eDetailingId=passingInterface
        otherFileAdapter?.notifyDataSetChanged()

        brandId=brandIDInterface
        dbBase?.insertStartTimeSlide(startDateTime,doctorId,brandId,
            PhotoSlideShowActivity.model?.brandName,0,currentTime.toString())

        onClickString("")

        mViewPagerAdapter = ViewPagerAdapter(requireActivity(), arrayImage)
        viewPagerMain.adapter = mViewPagerAdapter

        slideBrandWiseInsert(startDateTime,brandId)
    }


    override fun onClickString(passingInterface: String?)
    {
        likeCommentColor()
    }

    fun likeCommentColor()
    {
        PhotoSlideShowActivity.model?.fileId?.let { dbBase?.insertFileID(it, startDateTime,brandId) }

        val isLike=dbBase?.getLike(PhotoSlideShowActivity.model?.fileId.toString(),startDateTime)

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

        val storecomment= dbBase?.getComment(PhotoSlideShowActivity.model?.fileId.toString(),startDateTime)

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
        var dbBaseTimer=dbBase?.getTime(PhotoSlideShowActivity.model?.fileId.toString(),startDateTime)

        thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        requireActivity().runOnUiThread {
                            dbBaseTimer=dbBaseTimer+1
                            Log.e("timerSlider",dbBaseTimer.toString())
                            PhotoSlideShowActivity.model?.fileId?.let {
                                dbBase?.insertTime(dbBaseTimer,
                                    it,startDateTime)
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


            requireActivity().runOnUiThread {
                imageView.setImageBitmap(BitmapFactory.decodeFile(model.filePath))
                Objects.requireNonNull(container).addView(itemView)
            }

            imageView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    if( getArguments()?.getString("imageArray")!=null)
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
        //createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        //stopConnectivity(this)
        System.gc()
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
                        requireActivity().runOnUiThread {
                            dbBaseTimer=dbBaseTimer+1

                            Log.e("timerBrandWiseSlider",dbBaseTimer.toString())
                            dbBase?.insertBrandTime(dbBaseTimer  ,startDateTime,brandID.toString())
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