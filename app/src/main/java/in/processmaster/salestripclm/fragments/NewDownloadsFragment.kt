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

class NewDownloadsFragment : Fragment() {

    var recyclerView: RecyclerView?=null
    var sharePreferance: PreferenceClass?= null
    lateinit var db : DatabaseHandler
    var nodownload_tv: TextView?= null
    var getAlleDetailListDb: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
    var isFirstTimeOpen=true
    companion object { var adapter : Edetailing_Adapter?=null }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_new_downloads, container, false)

        recyclerView=view.findViewById(R.id.recyclerView)as RecyclerView
        nodownload_tv=view.findViewById(R.id.nodownload_tv)as TextView

        sharePreferance = PreferenceClass(activity)
        db = DatabaseHandler(requireActivity())

        Handler(Looper.getMainLooper()).postDelayed({

        getAlleDetailListDb= db.getSelectedeDetail(false)

        if(getAlleDetailListDb.size==0)nodownload_tv?.visibility=View.VISIBLE

        adapter =  Edetailing_Adapter(
            getAlleDetailListDb, sharePreferance, requireActivity(), db)

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

       var geteDetail= db.getSelectedeDetail(false)
        if(geteDetail.size!=getAlleDetailListDb.size && !isFirstTimeOpen)
        {
            getAlleDetailListDb.clear()
            getAlleDetailListDb.addAll(geteDetail)
            if(getAlleDetailListDb.size==0)nodownload_tv?.visibility=View.VISIBLE

            adapter =  Edetailing_Adapter(
                getAlleDetailListDb, sharePreferance, requireActivity(), db
            )
            recyclerView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    fun editTextFilter(string: String)
    {
        adapter?.getFilter()?.filter(string);
    }

}