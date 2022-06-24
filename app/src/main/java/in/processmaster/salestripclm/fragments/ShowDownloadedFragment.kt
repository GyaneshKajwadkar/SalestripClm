package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.DownloadedFolderAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctorIdDisplayVisual
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctor_et
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ShowDownloadedFragment : Fragment() {
    var video_rv: RecyclerView?=null
    var images_rv: RecyclerView?=null
    var html_rv: RecyclerView?=null

    var videoView_parent: LinearLayout?=null
    var images_parent: LinearLayout?=null
    var html_parent: LinearLayout?=null

    var selection_tv: TextView?=null
    var nestedScroll: NestedScrollView?=null
    var nodata_gif: ImageView?=null
    var topSearchParent: CardView?=null
    var adapterVideo : DownloadedFolderAdapter?=null
    var adapterImage : DownloadedFolderAdapter?=null
    var adapterWeb : DownloadedFolderAdapter?=null
    var filterFavList_et : EditText?=null
    var isCustomePresentation=false;

    private var db: DatabaseHandler? = null
    companion object
    {
        var currentTime = ""
        var currentDate = ""
    }

    lateinit var views : View

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_show_downloaded, container, false)
        video_rv=views.findViewById(R.id.video_rv)
        images_rv=views.findViewById(R.id.images_rv)
        html_rv=views.findViewById(R.id.html_rv)
        nodata_gif=views.findViewById(R.id.nodata_gif)
        topSearchParent=views.findViewById(R.id.topSearchParent)

        nestedScroll=views.findViewById(R.id.nestedScroll)
        filterFavList_et=views.findViewById(R.id.filterFavList_et)

        selection_tv=views.findViewById(R.id.selection_tv)

        videoView_parent=views.findViewById(R.id.videoView_parent)
        images_parent=views.findViewById(R.id.images_parent)
        html_parent=views.findViewById(R.id.html_parent)
        return views
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isAdded) {
            if(currentDate.isEmpty()&& currentDate.isEmpty())
            {
                currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            }

            var arraylistVideo:ArrayList<DownloadFileModel> = ArrayList()
            var arraylistImages:ArrayList<DownloadFileModel> = ArrayList()
            var arraylistZip:ArrayList<DownloadFileModel> = ArrayList()

            val bundle = arguments

            if(doctor_et?.text.toString().isEmpty())
            {
                nodata_gif?.visibility=View.VISIBLE
                selection_tv?.setText("Doctor not selected")
                selection_tv?.visibility=View.VISIBLE
                nestedScroll?.visibility=View.INVISIBLE
            }
            else if(!doctor_et?.text.toString().isEmpty() && bundle==null)
            {
                nodata_gif?.visibility=View.VISIBLE
                selection_tv?.setText("Brand not selected")
                selection_tv?.visibility=View.VISIBLE
                nestedScroll?.visibility=View.INVISIBLE
            }
            else
            {
                activity?.let { AlertClass(it).showProgressAlert("") }
                val coroutine= viewLifecycleOwner.lifecycleScope.launch{
                    var call= async {
                        var downloadList: ArrayList<DownloadFileModel> = ArrayList()
                        db = DatabaseHandler(activity)

                        isCustomePresentation=  requireArguments().getBoolean("presentation")
                        var value = 0
                        var brandID = 0

                        if(isCustomePresentation){
                            val presentationName=  requireArguments().getString("presentationName")
                            db?.getAllPresentationItem(presentationName)?.let { downloadList.addAll(it) }
                        }
                        else
                        {
                            val selectionType = requireArguments().getInt("selectionType")
                            value = requireArguments().getInt("eDetailingID")
                            brandID = requireArguments().getInt("brandId")

                            if(selectionType==1)
                            {
                                downloadList= db?.getAllDownloadedData(value) as ArrayList<DownloadFileModel>
                            }
                            else
                            {
                                downloadList= db?.getAllFavList() as ArrayList<DownloadFileModel>
                                if(downloadList.size==0) activity?.runOnUiThread {  nodata_gif?.visibility=View.VISIBLE }
                                else activity?.runOnUiThread { topSearchParent?.visibility=View.VISIBLE }
                            }
                        }


                        for ((index, valueDownload) in downloadList?.withIndex()!!)
                        {
                            if(valueDownload.downloadType.equals("VIDEO"))
                            {
                                //   valueDownload.eDetailingId=value
                                arraylistVideo.add(valueDownload)
                            }
                            else  if(valueDownload.downloadType.equals("IMAGE"))
                            {
                                //  valueDownload.eDetailingId=value
                                arraylistImages.add(valueDownload)
                            }
                            else
                            {
                                // valueDownload.eDetailingId=value
                                arraylistZip.add(valueDownload)
                            }
                        }

                        if(arraylistVideo.size==0) activity?.runOnUiThread{ videoView_parent?.visibility=View.GONE}

                        if(arraylistImages.size==0) activity?.runOnUiThread{ images_parent?.visibility=View.GONE}

                        if(arraylistZip.size==0)activity?.runOnUiThread{ html_parent?.visibility=View.GONE}
                        var presentationName=""
                        if(isCustomePresentation) presentationName= requireArguments().getString("presentationName")!!


                        adapterVideo= activity?.let {
                            DownloadedFolderAdapter(presentationName,"VIDEO",arraylistVideo,
                                it,doctorIdDisplayVisual,isCustomePresentation)
                        }
                        adapterImage= activity?.let {
                            DownloadedFolderAdapter(presentationName,"IMAGE", arraylistImages,
                                it, doctorIdDisplayVisual, isCustomePresentation)
                        }
                        adapterWeb= activity?.let {
                            DownloadedFolderAdapter(presentationName,"ZIP", arraylistZip,
                                it, doctorIdDisplayVisual, isCustomePresentation)
                        }


                        activity?.runOnUiThread {
                            nodata_gif?.visibility=View.GONE
                            selection_tv?.setText("Please wait...")


                            video_rv?.layoutManager = GridLayoutManager(activity, 5)
                            video_rv?.itemAnimator = DefaultItemAnimator()
                            video_rv?.adapter = adapterVideo

                            images_rv?.layoutManager = GridLayoutManager(activity, 5)
                            images_rv?.itemAnimator = DefaultItemAnimator()
                            images_rv?.adapter = adapterImage

                            html_rv?.layoutManager = GridLayoutManager(activity, 5)
                            html_rv?.itemAnimator = DefaultItemAnimator()
                            html_rv?.adapter = adapterWeb

                            nestedScroll?.visibility=View.VISIBLE
                            selection_tv?.visibility=View.GONE

                            Handler(Looper.getMainLooper()).postDelayed({
                                val activity: Activity? = activity

                                if (activity != null && isAdded)
                                {
                                    AlertClass(activity).hideAlert()
                                }
                            },100)
                        }

                    }
                    call.await()
                }
            }
            filterFavList_et?.addTextChangedListener(filterTextWatcher)

        }
    }


    //FilterUsingEdit
    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            adapterVideo?.getFilter()?.filter(s.toString())
            adapterImage?.getFilter()?.filter(s.toString())
            adapterWeb?.getFilter()?.filter(s.toString())
        }

        override fun afterTextChanged(s: Editable) {
            // TODO Auto-generated method stub
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {}

}