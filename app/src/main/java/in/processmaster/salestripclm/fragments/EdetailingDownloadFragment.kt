package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class EdetailingDownloadFragment : Fragment() {

    var sharePreferance: PreferenceClass?= null
    var position:Int=0
    var viewPager: ViewPager?=null
    var tabs: TabLayout? = null
    var syncData_ll: LinearLayout?=null
    lateinit var views:View
    var alertClass : AlertClass?=null

    companion object {
        var filter_et : EditText?=null
    }

    lateinit var db : DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.activity_edetailing_, container, false)

        if (isAdded) { initView(views)  }

        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded)  calladapter()
        }, 100)

        return views }

    fun initView(view: View)
    {
        filter_et=view.findViewById(R.id.filter_et)as EditText
        syncData_ll=view.findViewById(R.id.syncData_ll)as LinearLayout
        sharePreferance = PreferenceClass(activity)
        db = DatabaseHandler(activity)
        alertClass= activity?.let { AlertClass(it) }
        viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        tabs = view.findViewById<View>(R.id.result_tabs) as TabLayout

        filter_et!!.addTextChangedListener(filterTextWatcher)

        alertClass?.showProgressAlert("")
    }

    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            DownloadedFragment().editTextFilter(s.toString())
            NewDownloadsFragment().editTextFilter(s.toString())
        }
        override fun afterTextChanged(s: Editable) {}
    }

    //set recycler view
    fun calladapter()
    {
        if(views==null || isAdded==false) return

        viewLifecycleOwner.lifecycleScope.launch {
            activity?.runOnUiThread {
                viewPager?.let { setupViewPager(it) }
                tabs?.setupWithViewPager(viewPager)

                alertClass?.hideAlert()
            } }
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
                fragment = DownloadedFragment()
            } else if (position == 1) {
                fragment = NewDownloadsFragment()
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
                title = "Downloaded files"
            }
            else if (position == 1)
            {
                title = "Download files"

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

    override fun onSaveInstanceState(outState: Bundle) {}

}