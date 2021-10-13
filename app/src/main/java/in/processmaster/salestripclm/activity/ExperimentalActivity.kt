package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.ViewPagerAdapter
import `in`.processmaster.salestripclm.models.DownloadFileModel
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager


class ExperimentalActivity : BaseActivity() {

    var mViewPager: ViewPager? = null
    var arrayImage: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()

    // images array
    var images = intArrayOf(R.drawable.login_page_bg, R.drawable.salestrip_final_logo_brochure, R.drawable.login_page_bg, R.drawable.login_page_bg
      )

    // Creating Object of ViewPagerAdapter
    var mViewPagerAdapter: ViewPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experimental)

        if(intent.getSerializableExtra("imageArray")!=null)
        {
            arrayImage = intent.getSerializableExtra("imageArray") as ArrayList<DownloadFileModel>

        }


        mViewPager = findViewById<View>(R.id.viewPagerMain) as ViewPager
        mViewPagerAdapter = ViewPagerAdapter(this, arrayImage)
        mViewPager!!.adapter = mViewPagerAdapter


    }


    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }
}