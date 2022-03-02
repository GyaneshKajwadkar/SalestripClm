package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.Edetailing_Adapter
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.droidsonroids.gif.GifImageView

class DownloadedFragment : Fragment() {

    var recyclerView: RecyclerView?=null
    var sharePreferance: PreferenceClass?= null
    var nodata_gif: GifImageView?= null
    lateinit var db : DatabaseHandler
    var nodownload_tv: TextView?= null
    var isFirstTimeOpen=true
    var   getAlleDetailListDb: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()

    companion object {
        var adapter : Edetailing_Adapter?=null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_downloaded2, container, false)

        recyclerView=view.findViewById(R.id.recyclerView)as RecyclerView
        nodownload_tv=view.findViewById(R.id.nodownload_tv)as TextView
        nodata_gif=view.findViewById(R.id.nodata_gif)as GifImageView

        sharePreferance = PreferenceClass(activity)
        db = DatabaseHandler(requireActivity())
        getAlleDetailListDb= db.getSelectedeDetail(true)

        Handler(Looper.getMainLooper())
            .postDelayed({
                if(getAlleDetailListDb!!.size==0)
                {
                    nodata_gif?.visibility=View.VISIBLE
                    recyclerView?.visibility=View.GONE
                }
                else
                {
                    nodata_gif?.visibility=View.GONE
                    recyclerView?.visibility=View.VISIBLE
                }

                adapter =  Edetailing_Adapter(
                    getAlleDetailListDb, sharePreferance, requireActivity(), db
                )
                val layoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = layoutManager
                recyclerView?.adapter = adapter

                if(EdetailingDownloadFragment.filter_et?.text?.equals("") == false)
                {
                    editTextFilter(EdetailingDownloadFragment.filter_et?.text.toString())
                }

                isFirstTimeOpen=false
            }, 100)

        return view
        }

    override fun onResume() {
        super.onResume()
        var   getAlleDetail= db.getSelectedeDetail(true)

        if(!isFirstTimeOpen)
        {
            getAlleDetailListDb.clear()
            getAlleDetailListDb.addAll(getAlleDetail)
            adapter =  Edetailing_Adapter(
                getAlleDetailListDb, sharePreferance, requireActivity(), db
            )
            recyclerView?.adapter = adapter
            adapter?.notifyDataSetChanged()

            if(getAlleDetail.size!=0)
            {
                nodata_gif?.visibility=View.GONE
                recyclerView?.visibility=View.VISIBLE
            }
        }
    }

    fun editTextFilter(string: String)
    {
        adapter?.getFilter()?.filter(string);
    }

}