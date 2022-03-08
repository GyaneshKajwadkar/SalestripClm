package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.adapter.Edetailing_Adapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EdetailingDownloadFragment : Fragment() {

    var recyclerView:RecyclerView?=null
    var sharePreferance: PreferenceClass?= null
    var adapter : Edetailing_Adapter?=null
    var position:Int=0
    var viewPager: ViewPager?=null
    var tabs: TabLayout? = null
    var syncData_ll: LinearLayout?=null

    companion object {
        var filter_et : EditText?=null
    }

    lateinit var db : DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.activity_edetailing_, container, false)
        if (activity != null && isAdded) { initView(view)  }

        return view }

    fun initView(view: View)
    {
        recyclerView=view.findViewById(R.id.recyclerView)as RecyclerView
        filter_et=view.findViewById(R.id.filter_et)as EditText
        syncData_ll=view.findViewById(R.id.syncData_ll)as LinearLayout
        sharePreferance = PreferenceClass(activity)
        db = DatabaseHandler(requireActivity())
        viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        tabs = view.findViewById<View>(R.id.result_tabs) as TabLayout

        calladapter()
        filter_et!!.addTextChangedListener(filterTextWatcher)

        syncData_ll?.setOnClickListener({ division_api()})

    }

    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            DownloadedFragment().editTextFilter(s.toString())
            NewDownloadsFragment().editTextFilter(s.toString())
        }
        override fun afterTextChanged(s: Editable) {}
    }

    //call_divisioinApi
    private fun division_api()
    {
        AlertClass(requireActivity()).showProgressAlert("Sync data")
        val jsonObject = JSONObject(loginModelHomePage?.getEmployeeObj().toString())

        var call: Call<DevisionModel> = HomePage.apiInterface?.detailingApi(
            "bearer " + loginModelHomePage?.accessToken, jsonObject.getString(
                "Division"
            )
        ) as Call<DevisionModel>
        call.enqueue(object : Callback<DevisionModel?> {
            override fun onResponse(
                call: Call<DevisionModel?>?,
                response: Response<DevisionModel?>
            )
            {
                Log.e("division_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    for ((index, value) in db.getAlleDetail()?.withIndex()!!) {
                        val gson = Gson()
                        db.insertOrUpdateEDetail(value.geteDetailId().toString(), gson.toJson(value))

                        if (index == response.body()?.getData()?.geteDetailingList()!!.size - 1)
                        { calladapter() }
                    }

                    // clear database
                    for(dbList in db.getAlleDetail())
                    {
                        var isSet=false
                        for (mainList in response.body()?.getData()?.geteDetailingList()!!)
                        {
                            if (mainList.geteDetailId() == dbList.geteDetailId())
                            { isSet = true }
                        }

                        //this clear database and files from device which is not in used
                        if(!isSet)
                        {
                           db.deleteEdetailingData(dbList.geteDetailId().toString())
                            var downloadModelArrayList=db.getAllDownloadedData(dbList.geteDetailId()!!)
                            for(item in downloadModelArrayList)
                            {
                                val someDir = File(item.fileDirectoryPath)
                                someDir.deleteRecursively()
                            }
                            db.deleteEdetailDownloada(dbList.geteDetailId().toString())

                        }}}
                AlertClass(requireActivity()).hideAlert()
            }

            override fun onFailure(call: Call<DevisionModel?>, t: Throwable?) {
                GeneralClass(requireActivity()).checkInternet()
                AlertClass(requireActivity()).hideAlert()
                call.cancel()
            }
        })
    }

    //set recycler view
    fun calladapter()
    {
        setupViewPager(viewPager!!)
        tabs!!.setupWithViewPager(viewPager)
    }

    // Add Fragments to Tabs
    private fun setupViewPager(viewPager: ViewPager)
    {
        if (!isAdded()) return;
        var adapter = ViewPagerAdapter(getChildFragmentManager())
        viewPager.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    internal class Adapter(manager: FragmentManager?) : FragmentPagerAdapter(
        manager!!,
        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        private val mFragmentList: MutableList<Fragment> = java.util.ArrayList()
        private val mFragmentTitleList: MutableList<String> = java.util.ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int { return mFragmentList.size }

        override fun getPageTitle(position: Int): CharSequence? { return mFragmentTitleList[position] }
    }


    @SuppressLint("WrongConstant")
    class ViewPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(     fm!!,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null
            if (position == 0) {
                fragment = NewDownloadsFragment()
            } else if (position == 1) {
                fragment = DownloadedFragment()
            }
            return fragment!!
        }


        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            if (position == 0)
            {
                title = "Download files"
            }
            else if (position == 1)
            {
                title = "Downloaded files"
            }
            return title
        }
    }


    //Custom view pager class to stop scroll
    class CustomViewPager : ViewPager {
        private var isPagingEnabled = true

        constructor(context: Context?) : super(context!!) {}
        constructor(context: Context?, attrs: AttributeSet?) : super(
            context!!,
            attrs
        ) {}

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return isPagingEnabled && super.onTouchEvent(event)
        }

        override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
            return isPagingEnabled && super.onInterceptTouchEvent(event)
        }

    }

}