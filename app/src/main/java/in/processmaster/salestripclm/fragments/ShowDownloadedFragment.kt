package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctorIdDisplayVisual
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctor_et
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.DownloadedFolderAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_show_downloaded, container, false)

        if(currentDate.isEmpty()&& currentDate.isEmpty())
        {
            currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        }

        video_rv=view.findViewById(R.id.video_rv)
        images_rv=view.findViewById(R.id.images_rv)
        html_rv=view.findViewById(R.id.html_rv)
        nodata_gif=view.findViewById(R.id.nodata_gif)
        topSearchParent=view.findViewById(R.id.topSearchParent)

        nestedScroll=view.findViewById(R.id.nestedScroll)
        filterFavList_et=view.findViewById(R.id.filterFavList_et)

        selection_tv=view.findViewById(R.id.selection_tv)

        videoView_parent=view.findViewById(R.id.videoView_parent)
        images_parent=view.findViewById(R.id.images_parent)
        html_parent=view.findViewById(R.id.html_parent)

        var arraylistVideo:ArrayList<DownloadFileModel> = ArrayList()
        var arraylistImages:ArrayList<DownloadFileModel> = ArrayList()
        var arraylistZip:ArrayList<DownloadFileModel> = ArrayList()

        val bundle = arguments

        if(doctor_et?.text.toString().isEmpty())
        {
            nodata_gif?.visibility=View.VISIBLE
            selection_tv?.setText("Doctor not selected")
            selection_tv?.visibility=View.VISIBLE
            nestedScroll?.visibility=View.GONE
        }
        else if(!doctor_et?.text.toString().isEmpty() && bundle==null)
        {
            nodata_gif?.visibility=View.VISIBLE
            selection_tv?.setText("Brand not selected")
            selection_tv?.visibility=View.VISIBLE
            nestedScroll?.visibility=View.GONE
        }
        else
        {
            AlertClass(requireActivity()).showProgressAlert("")
            val runnable= Runnable {
             var downloadList: ArrayList<DownloadFileModel> = ArrayList()
             db = DatabaseHandler(requireActivity())

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
                     if(downloadList.size==0) requireActivity().runOnUiThread {  nodata_gif?.visibility=View.VISIBLE }
                     else requireActivity().runOnUiThread { topSearchParent?.visibility=View.VISIBLE }
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

                if(arraylistVideo.size==0) requireActivity().runOnUiThread{ videoView_parent?.visibility=View.GONE}

                if(arraylistImages.size==0) requireActivity().runOnUiThread{ images_parent?.visibility=View.GONE}

                if(arraylistZip.size==0)requireActivity().runOnUiThread{ html_parent?.visibility=View.GONE}
                var presentationName=""
                if(isCustomePresentation) presentationName= requireArguments().getString("presentationName")!!


                adapterVideo= DownloadedFolderAdapter(presentationName,"VIDEO",arraylistVideo, requireActivity(),doctorIdDisplayVisual,isCustomePresentation)
                adapterImage= DownloadedFolderAdapter(presentationName,"IMAGE", arraylistImages, requireActivity(), doctorIdDisplayVisual, isCustomePresentation)
                adapterWeb= DownloadedFolderAdapter(presentationName,"ZIP", arraylistZip, requireActivity(), doctorIdDisplayVisual, isCustomePresentation)


                requireActivity().runOnUiThread {
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
                       AlertClass(requireActivity()).hideAlert()
                   },100)
                }
            }
            Thread(runnable).start()

        }

        filterFavList_et?.addTextChangedListener(filterTextWatcher)

        return view
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


}