package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.Edetailing_Adapter
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_downloaded2.*
import kotlinx.coroutines.*
import pl.droidsonroids.gif.GifImageView
import java.lang.Runnable


class DownloadedFragment : Fragment() {

    var recyclerView: RecyclerView?=null
    var nodata_gif: ImageView?= null
    lateinit var dbDownloadFrag : DatabaseHandler
    var nodownload_tv: TextView?= null
    var isFirstTimeOpen=true
    var getAlleDetailListDb: ArrayList<DevisionModel.Data.EDetailing> = ArrayList()
    lateinit var views: View

    companion object {
        var adapter : Edetailing_Adapter?=null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_downloaded2, container, false)

        recyclerView=views.findViewById(R.id.recyclerView)as RecyclerView
        nodownload_tv=views.findViewById(R.id.nodownload_tv)as TextView
        nodata_gif=views.findViewById(R.id.nodata_gif)as ImageView
        return views
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        val coroutine= viewLifecycleOwner.lifecycleScope.launch {

            dbDownloadFrag = DatabaseHandler.getInstance(activity?.applicationContext)
            getAlleDetailListDb= dbDownloadFrag.getSelectedeDetail(true)
        }
        coroutine.invokeOnCompletion {
            activity?.runOnUiThread {
                if(getAlleDetailListDb!!.size==0)
                {
                    nodata_gif?.visibility=View.VISIBLE
                    recyclerView?.visibility=View.INVISIBLE
                }
                else
                {
                    nodata_gif?.visibility=View.GONE
                    recyclerView?.visibility=View.VISIBLE
                }

                if(EdetailingDownloadFragment.filter_et?.text?.equals("") == false)
                {
                    editTextFilter(EdetailingDownloadFragment.filter_et?.text.toString())
                }

                isFirstTimeOpen=false

                adapter =  Edetailing_Adapter(
                    getAlleDetailListDb, activity, dbDownloadFrag
                )
                val layoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = layoutManager
                recyclerView?.adapter = adapter
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (!isAdded) return

        if(!isFirstTimeOpen)
        {
            activity?.runOnUiThread(Runnable {
                progressView.visibility=View.VISIBLE
                recyclerView?.visibility=View.INVISIBLE
            })

            Handler(Looper.getMainLooper())
                .postDelayed({
                    val coroutine= viewLifecycleOwner.lifecycleScope.launch{
                        val notify= async {
                            var   getAlleDetail= dbDownloadFrag.getSelectedeDetail(true)

                                getAlleDetailListDb.clear()
                                getAlleDetailListDb.addAll(getAlleDetail)

                          /*      activity?.runOnUiThread(Runnable {
                                   adapter =  Edetailing_Adapter(
                                        getAlleDetailListDb, sharePreferance, activity, db
                                    )
                                    recyclerView?.adapter = adapter
                                    adapter?.notifyDataSetChanged()

                                    if(getAlleDetail.size!=0)
                                    {   nodata_gif?.visibility=View.GONE
                                        recyclerView?.visibility=View.VISIBLE
                                    }
                                })*/

                        }
                        notify.await()
                    }
                    coroutine.invokeOnCompletion {
                        activity?.runOnUiThread(Runnable {
                            progressView.visibility=View.GONE
                            nodata_gif?.visibility=View.GONE
                            recyclerView?.visibility=View.VISIBLE
                            val layoutManager = LinearLayoutManager(activity)
                            recyclerView?.layoutManager = layoutManager
                            adapter =  Edetailing_Adapter(
                                getAlleDetailListDb, activity, dbDownloadFrag
                            )
                            recyclerView?.adapter = adapter
                          //  adapter?.notifyDataSetChanged()
                        }) }
                }, 300)
        }
    }

    fun editTextFilter(string: String)
    { adapter?.getFilter()?.filter(string) }

}