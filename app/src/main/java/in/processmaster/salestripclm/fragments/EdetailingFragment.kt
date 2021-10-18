package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.Edetailing_Adapter
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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

class EdetailingFragment : BaseFragment() {

    var recyclerView:RecyclerView?=null
    var apiInterface: APIInterface? = null
    var sharePreferance: PreferenceClass?= null
    var loginModel : LoginModel?=null
    var adapter : Edetailing_Adapter?=null
    var position:Int=0
    var getAlleDetail: ArrayList<DevisionModel.Data.EDetailing>?= ArrayList()
    var progressView_parentRv: RelativeLayout?=null
    var progressMessage_tv: TextView? =null
    var viewPager: ViewPager?=null
    var tabs: TabLayout? = null
    var syncData_ll: LinearLayout?=null

    companion object {
        var filter_et : EditText?=null
    }

    //Database
    var db = DatabaseHandler(activity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.activity_edetailing_, container, false)
        initView(view)
        return view
    }

    fun initView(view: View)
    {
        recyclerView=view.findViewById(R.id.recyclerView)as RecyclerView
        progressView_parentRv=view.findViewById(R.id.progressView_parentRv) as RelativeLayout
        progressMessage_tv=view.findViewById(R.id.progressMessage_tv) as TextView
        filter_et=view.findViewById(R.id.filter_et)as EditText
        syncData_ll=view.findViewById(R.id.syncData_ll)as LinearLayout
        sharePreferance = PreferenceClass(activity)
        db = DatabaseHandler(activity)
        // Setting ViewPager for each Tabs
        viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        // Set Tabs inside Toolbar
        tabs = view.findViewById<View>(R.id.result_tabs) as TabLayout
        progressMessage_tv?.setText("Loading e-Detailing")
        activity?.let { enableProgress(progressView_parentRv!!, it) }

        var profileData =sharePreferance?.getPref("profileData")
        loginModel= Gson().fromJson(profileData, LoginModel::class.java)

        calladapter()

        filter_et!!.addTextChangedListener(filterTextWatcher)

        changeStatusBar()

        Handler(Looper.getMainLooper())
            .postDelayed({
                activity?.let { disableProgress(progressView_parentRv!!, it)}
            }, 200)


        syncData_ll?.setOnClickListener({

            progressMessage_tv?.setText("Loading e-Detailing")
            activity?.let { enableProgress(progressView_parentRv!!, it) }
            division_api()
        })

    }

    //change status bar color
    fun changeStatusBar()
    {
        val window: Window = activity?.getWindow()!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.appColor))
    }

    //FilterUsingEdit
    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            DownloadedFragment().editTextFilter(s.toString())
            NewDownloadsFragment().editTextFilter(s.toString())
          //  adapter?.getFilter()?.filter(s.toString());
        }

        override fun afterTextChanged(s: Editable) {
            // TODO Auto-generated method stub
        }
    }

    //call_divisioinApi
    private fun division_api()
    {

        apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(
            APIInterface::class.java
        )
        val jsonObject = JSONObject(loginModel?.getEmployeeObj().toString())

        var call: Call<DevisionModel> = apiInterface?.detailingApi(
            "bearer " + loginModel?.accessToken, jsonObject.getString(
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

                    for ((index, value) in db.alleDetail
                        ?.withIndex()!!) {

                        //store edetailing data to db
                        val gson = Gson()
                        db.insertOrUpdateEDetail(
                            value.geteDetailId().toString(),
                            gson.toJson(value)
                        )

                        if (index == response.body()?.data?.geteDetailingList()!!.size - 1) {
                            calladapter()
                        }
                    }

                    // clear database
                    for(dbList in db.getAlleDetail())
                    {
                        var isSet=false
                        for (mainList in response.body()?.data?.geteDetailingList()!!)
                        {
                            if (mainList.geteDetailId() == dbList.geteDetailId())
                            {
                                isSet = true
                            }
                        }

                        //this clear database and files from device which is not in used
                        if(!isSet)
                        {
                           db.deleteEdetailingData(dbList.geteDetailId().toString())

                            var downloadModelArrayList=db.getAllDownloadedData(dbList.geteDetailId())

                            //Delete files from folder before erase db
                            for(item in downloadModelArrayList)
                            {
                                val someDir = File(item.fileDirectoryPath)
                                someDir.deleteRecursively()
                            }

                            db.deleteEdetailDownloada(dbList.geteDetailId().toString())

                        }
                    }
                }

                activity?.let { disableProgress(progressView_parentRv!!, it) }

            }

            override fun onFailure(call: Call<DevisionModel?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                activity?.let { disableProgress(progressView_parentRv!!, it) }
            }
        })
    }

    //set recycler view
    fun calladapter()
    {
        setupViewPager(viewPager!!)
        tabs!!.setupWithViewPager(viewPager)
    }

    //checkInternet connection
    fun checkInternet()
    {
        if(activity?.let { isInternetAvailable(it) } ==true)
        {
            commonAlert(requireActivity(), "Error", "Something went wrong please try again later")
        }
        else
        {
            activity?.let { networkAlert(it) }
        }
    }

    // Add Fragments to Tabs
    private fun setupViewPager(
        viewPager: ViewPager //,
    )
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

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


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
        ) {
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return isPagingEnabled && super.onTouchEvent(event)
        }

        override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
            return isPagingEnabled && super.onInterceptTouchEvent(event)
        }

        fun setPagingEnabled(b: Boolean) {
            isPagingEnabled = b
        }
    }

}