package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctorIdDisplayVisual
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag.Companion.doctor_et
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.DownloadedFolderAdapter
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_show_downloaded.*
import pl.droidsonroids.gif.GifImageView

class ShowDownloadedFragment : Fragment() {
    var video_rv: RecyclerView?=null
    var images_rv: RecyclerView?=null
    var html_rv: RecyclerView?=null

    var videoView_parent: LinearLayout?=null
    var images_parent: LinearLayout?=null
    var html_parent: LinearLayout?=null

    var selection_tv: TextView?=null
    var nestedScroll: NestedScrollView?=null
    var nodata_gif: GifImageView?=null
    var topSearchParent: CardView?=null
    var adapterVideo : DownloadedFolderAdapter?=null
    var adapterImage : DownloadedFolderAdapter?=null
    var adapterWeb : DownloadedFolderAdapter?=null
    var filterFavList_et : EditText?=null
    private var db: DatabaseHandler? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_show_downloaded, container, false)

        db = DatabaseHandler(activity)

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
            nodata_gif?.visibility=View.GONE
            selection_tv?.setText("Please wait...")


            val value = requireArguments().getInt("eDetailingID")
            val brandID = requireArguments().getInt("brandId")
            val selectionType = requireArguments().getInt("selectionType")

            var downloadList: ArrayList<DownloadFileModel> = ArrayList()
            if(selectionType==1)
            {
                 downloadList=db!!.getAllDownloadedData(value)
            }
            else
            {
                 downloadList=db!!.getAllFavList()
                if(downloadList.size==0)
                {
                    nodata_gif?.visibility=View.VISIBLE
                }
                else
                {
                    topSearchParent?.visibility=View.VISIBLE

                }
            }

            for ((index, valueDownload) in downloadList?.withIndex()!!)
            {
                if(valueDownload.downloadType.equals("VIDEO"))
                {
                    valueDownload.seteDetailingId(value)
                    arraylistVideo.add(valueDownload)
                }
                else  if(valueDownload.downloadType.equals("IMAGE"))
                {
                    valueDownload.seteDetailingId(value)
                    arraylistImages.add(valueDownload)
                }

                else
                {
                    valueDownload.seteDetailingId(value)
                    arraylistZip.add(valueDownload)
                }
            }

            if(arraylistVideo.size==0)
            {
                videoView_parent?.visibility=View.GONE
            }

            if(arraylistImages.size==0)
            {
                images_parent?.visibility=View.GONE
            }

            if(arraylistZip.size==0)
            {
                html_parent?.visibility=View.GONE
            }



            val sendtext = requireArguments().getString("type")

             adapterVideo= DownloadedFolderAdapter(sendtext,"VIDEO",arraylistVideo, requireActivity(),doctorIdDisplayVisual,brandID)
            video_rv!!.layoutManager = GridLayoutManager(activity, 4)
            video_rv?.itemAnimator = DefaultItemAnimator()
            video_rv?.adapter = adapterVideo

             adapterImage= DownloadedFolderAdapter(sendtext,"IMAGE", arraylistImages, requireActivity(), doctorIdDisplayVisual, brandID)
            images_rv!!.layoutManager = GridLayoutManager(activity, 4)
            images_rv?.itemAnimator = DefaultItemAnimator()
            images_rv?.adapter = adapterImage

             adapterWeb= DownloadedFolderAdapter(sendtext,"ZIP", arraylistZip, requireActivity(), doctorIdDisplayVisual, brandID)
            html_rv!!.layoutManager = GridLayoutManager(activity, 4)
            html_rv?.itemAnimator = DefaultItemAnimator()
            html_rv?.adapter = adapterWeb

            nestedScroll?.visibility=View.VISIBLE
            selection_tv?.visibility=View.GONE
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