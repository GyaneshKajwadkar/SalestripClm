package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.*
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.progress_view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import us.zoom.sdk.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class HomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener/*, UserLoginCallback.ZoomDemoAuthenticationListener , MeetingServiceListener, InitAuthSDKCallback*/
{
    var drawer_layout : DrawerLayout?=null
    //  private var mZoomSDK: ZoomSDK? = null
    var drawerProfileIv: ImageView?=null
    var bottomNavigation: BottomNavigationView? = null
    var openFragmentStr=""
    val generalClass= GeneralClass(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //  mZoomSDK = ZoomSDK.getInstance()
        initView()

        if(generalClass.isInternetAvailable())
            callingMultipleAPI()
        else
            staticSyncData=Gson().fromJson(dbBase?.getAllDataSync(), SyncModel::class.java)

        // dbBase?.insertOrUpdateAPI(3,"")

    }


    @SuppressLint("RestrictedApi")
    fun initView()
    {
        bottomNavigation=findViewById(R.id.navigationView)
        bottomNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        drawer_layout=findViewById(R.id.drawer_layout) as DrawerLayout

        //Retrive user data
        //close Navigation drawer
        //   drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // sharePreferance = PreferenceClass(this)
        // var profilePicPath =sharePreferance?.getPref("profilePic")

        //retrive profile image
        /*  if(!profilePicPath?.isEmpty()!!)
          {
              var options = BitmapFactory.Options()
              options.inSampleSize = 2
              profile_image!!.setImageBitmap(BitmapFactory.decodeFile(profilePicPath, options));
          }*/

        //Retrive user data
        //       var profileData =sharePreferance?.getPref("profileData")
//        loginModel= Gson().fromJson(profileData, LoginModel::class.java)
        userName_tv?.setText(loginModelBase?.userName)

        //change status bar colour
        changeStatusBar()


        /* try {
             val jsonObject = JSONObject(loginModelBase?.getEmployeeObj().toString())
         } catch (err: JSONException) {
             Log.d("Error", err.toString())
         }*/

        //Set app version to text view
        try
        {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0)
            val version = pInfo.versionName
            appVersion_tv?.setText("v.${version}")
        }
        catch (e: PackageManager.NameNotFoundException)
        {
            e.printStackTrace()
        }


        //call sync api
        if(generalClass.isInternetAvailable())
        {
            // sync_api()
            // getsheduledMeeting_api()

            /*    GlobalScope.launch(Dispatchers.IO)
                {
                    var zoomSDKBase = ZoomSDK.getInstance()
                    if(!zoomSDKBase.isLoggedIn)
                    {
                        getCredientail_api(this@HomePage)
                    }
                }*/

            Picasso
                .get()
                .load(loginModelBase?.imageName)
                .noFade()
                .into(profile_image)
        }

        //Logout
        menu_iv?.setOnClickListener({

            val menuBuilder = MenuBuilder(this)
            val inflater = MenuInflater(this)
            inflater.inflate(R.menu.menu_main, menuBuilder)
            val optionsMenu = MenuPopupHelper(this, menuBuilder, menu_iv!!)
            optionsMenu.setForceShowIcon(true)

            // Set Item Click Listener
            menuBuilder.setCallback(object : MenuBuilder.Callback {

                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.logout_menu -> {
                            logoutAppAlert()
                        }
                    }
                    return true
                }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })
            // Display the menu
            optionsMenu.show()
        })
        menu_img?.setOnClickListener({
            drawer_layout!!.openDrawer(Gravity.LEFT)
        })

        val headerView: View = nav_view?.inflateHeaderView(R.layout.nav_header_main)!!
        var drawerTv=  headerView.findViewById(R.id.profileName_tv_drawer) as TextView
        drawerProfileIv=  headerView.findViewById(R.id.profile_iv_drawer) as ImageView

        drawerTv?.setText(loginModelBase?.userName)
        /* if(!profilePicPath?.isEmpty()!!)
         {
             var options = BitmapFactory.Options()
             options.inSampleSize = 2
             drawerProfileIv!!.setImageBitmap(BitmapFactory.decodeFile(profilePicPath, options));
         }*/

        Picasso
            .get()
            .load(loginModelBase?.imageName)
            .noFade()
            .into(drawerProfileIv)

        nav_view?.setNavigationItemSelectedListener(this)
        nav_view?.getMenu()?.getItem(0)?.setChecked(true)

        //   InitAuthSDKHelper.getInstance().initSDK(this, this)


        //This is timer for zoom api
        /* var count=0
         val T = Timer()
         T.scheduleAtFixedRate(object : TimerTask() {
             override fun run() {
                 runOnUiThread {
                    Log.e("hfioshfisgfuio",count.toString())
                     count++
                 }
             }
         }, 1000, 1000)*/

    }

    //sync api
    private fun sync_api()
    {
        progressMessage_tv?.setText("Sync in progress")
        generalClass.enableProgress(progressView_parentRv!!)

        var call: Call<SyncModel> = getSecondaryApiInterface().syncApi("bearer " + loginModelBase?.accessToken) as Call<SyncModel>
        call.enqueue(object : Callback<SyncModel?> {
            override fun onResponse(call: Call<SyncModel?>?, response: Response<SyncModel?>) {
                Log.e("sync_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if (dbBase.datasCount > 0) {
                        dbBase.deleteAll()
                    }
                    dbBase?.addSyncData(Gson().toJson(response.body()))

                    bottomNavigation?.selectedItemId= R.id.landingPage
                    division_api()
                }

                // if token expire go to login page again
                if (response.code() == 401) {
                    Log.e("responseCode", "error")
                    sharePreferanceBase?.setPrefBool("isLogin", false)
                    val intent = Intent(this@HomePage, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    generalClass.disableProgress(progressView_parentRv!!)
                }
            }

            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                generalClass.disableProgress(progressView_parentRv!!)

            }
        })
    }

    private fun division_api()
    {

        val jsonObject = JSONObject(loginModelBase?.getEmployeeObj().toString())

        var call: Call<DevisionModel> = getSecondaryApiInterface().detailingApi(
            "bearer " + loginModelBase?.accessToken, jsonObject.getString(
                "Division"
            )
        ) as Call<DevisionModel>
        call.enqueue(object : Callback<DevisionModel?> {
            override fun onResponse(
                call: Call<DevisionModel?>?,
                response: Response<DevisionModel?>
            ) {
                Log.e("division_api", response.code().toString() + "")

                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    for ((index, value) in response.body()?.data?.geteDetailingList()
                        ?.withIndex()!!) {
                        //store edetailing data to db
                        val gson = Gson()
                        dbBase.insertOrUpdateEDetail(
                            value.geteDetailId().toString(),
                            gson.toJson(value)
                        )
                    }

                    // clear database
                    for (dbList in dbBase.getAlleDetail()) {
                        var isSet = false
                        for (mainList in response.body()?.data?.geteDetailingList()!!) {
                            if (mainList.geteDetailId() == dbList.geteDetailId()) {
                                isSet = true
                            }
                        }

                        //this clear database and files from device which is not in used
                        if (!isSet) {
                            dbBase.deleteEdetailingData(dbList.geteDetailId().toString())

                            var downloadModelArrayList =
                                dbBase.getAllDownloadedData(dbList.geteDetailId())

                            //Delete files from folder before erase db
                            for (item in downloadModelArrayList) {
                                val someDir = File(item.fileDirectoryPath)
                                someDir.deleteRecursively()
                            }

                            dbBase.deleteEdetailDownloada(dbList.geteDetailId().toString())

                        }
                    }

                    //    Log.e("isZoomLogin",mZoomSDK!!.isLoggedIn!!.toString()+" 456")

                }
                generalClass.disableProgress(progressView_parentRv!!)

            }

            override fun onFailure(call: Call<DevisionModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                generalClass.disableProgress(progressView_parentRv!!)

            }
        })
    }

    //change status bar color
    fun changeStatusBar()
    {
        val window: Window = this.getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.appColor))
    }

    //set bottom navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.landingPage -> {

                toolbarTv?.setText("Salestrip CLM")
                val fragment = HomeFragment()
                openFragment(fragment)
                openFragmentStr = "HomeFragment"
                return@OnNavigationItemSelectedListener true
            }

            R.id.downloadVisualPage -> {
                toolbarTv?.setText("Download Content")

                if (openFragmentStr.equals("EdetailingFragment")) {
                    return@OnNavigationItemSelectedListener true
                }

                val fragment = EdetailingFragment()
                openFragment(fragment)
                openFragmentStr = "EdetailingFragment"
                return@OnNavigationItemSelectedListener true
            }

            R.id.displayVisualPage -> {
                toolbarTv?.setText("Presentation")
                val fragment = DisplayVisualFragment()
                openFragment(fragment)
                openFragmentStr = "DisplayVisualFragment"
                return@OnNavigationItemSelectedListener true
            }

            R.id.callPage->{
                toolbarTv?.setText("Create Calls")
                val fragment = NewCallFragment()
                openFragment(fragment)
                openFragmentStr = "CallsFragment"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //open fragment method
    private fun openFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    //on back button press open exit alert
    override fun onBackPressed()
    {
       // val openFragment: DoctorDetailFragment? = supportFragmentManager.findFragmentByTag("detailDoctor") as DoctorDetailFragment?
       // if (openFragment != null && openFragment.isVisible()) {
       //     if(getSupportFragmentManager().getBackStackEntryCount() > 0)
       //         getSupportFragmentManager().popBackStack()
       //     return
       // }

        exitAppAlert(this)
    }


    //exit app alert
    fun logoutAppAlert()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val message_tv = dialogView.findViewById<View>(R.id.message_tv) as TextView
        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setText("Logout")
        message_tv.setText("Are you sure you want to logout?")
        mainHeading_tv.setText("Logout!")

        exit_btn.setOnClickListener{

            sharePreferanceBase?.setPrefBool("isLogin", false)

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawer_layout!!.closeDrawers()
        when (item.itemId) {

            R.id.nav_home -> {

            }

            R.id.nav_profile -> {
                var intent = Intent(this, ProfileeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.nav_schedule -> {
                var intent = Intent(this, SetSchedule_Activity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.nav_screenshot -> {
                var intent = Intent(this, ImageSelectorActivity::class.java)
                intent.putExtra("filePath", getFilesDir()?.getAbsolutePath() + "/Screenshots/")
                intent.putExtra("selection", "delete")
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.sync_menu -> {
                //call sync api
                if (generalClass.isInternetAvailable()) {
                    sync_api()
                }
            }

            R.id.nav_scheduled -> {
                var intent = Intent(this, JoinMeetingActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.nav_logout ->
            {
                logoutAppAlert()
            }
        }

        return true
    }


    override fun onResume()
    {
        super.onResume()

        createConnectivity(this)

        if(nav_view!=null)
        {
            nav_view?.getMenu()?.getItem(0)?.setChecked(true)
        }

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        if(sharePreferanceBase?.getPref("SyncDate")==null || sharePreferanceBase?.getPref("SyncDate")!!.isEmpty())
        {
            sharePreferanceBase?.setPref("SyncDate", formattedDate)
        }
        else
        {
            if(sharePreferanceBase?.getPref("SyncDate")==formattedDate)
            { }
            else
            {
                if(generalClass.isInternetAvailable())
                    callingMultipleAPI()
                else
                    staticSyncData=Gson().fromJson(dbBase?.getAllDataSync(), SyncModel::class.java)

                sharePreferanceBase?.setPref("SyncDate", formattedDate)
            }

        }

        var profileData =sharePreferanceBase?.getPref("profileData")
        loginModelBase = Gson().fromJson(profileData, LoginModel::class.java)

        Picasso
            .get()
            .load(loginModelBase?.imageName)
            .noFade()
            .into(profile_image)

        Picasso
            .get()
            .load(loginModelBase?.imageName)
            .noFade()
            .into(drawerProfileIv)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    fun callingMultipleAPI()
    {
        //   progressMessage_tv?.setText("Please wait....")
        generalClass.enableProgress(progressView_parentRv!!)

        val coroutineScope= CoroutineScope(IO).launch {
            val ans= async {
                callingSyncAPI()
            }

            val divisionApi =async {
                callingDivisionAPI()
            }

            val credientialApi= async {
                getSheduleMeetingAPI()
            }

            val initilizeZoom= async {
                var zoomSDKBase = ZoomSDK.getInstance()
                if(!zoomSDKBase.isLoggedIn)
                {
                    getCredientailAPI(this@HomePage)
                }
            }

            ans.await()
            divisionApi.await()
            credientialApi.await()
            initilizeZoom.await()

        }
        coroutineScope.invokeOnCompletion {
            this.runOnUiThread(java.lang.Runnable {
                generalClass.disableProgress(progressView_parentRv!!)
            })
        }

    }

    suspend fun callingDivisionAPI()
    {
        val jsonObject = JSONObject(loginModelBase?.getEmployeeObj().toString())

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).detailingApiCoo( "bearer " + loginModelBase?.accessToken, jsonObject.getString(
            "Division"
        ))
        withContext(Dispatchers.Main) {
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    for ((index, value) in response.body()?.data?.geteDetailingList()
                        ?.withIndex()!!) {
                        //store edetailing data to db
                        val gson = Gson()
                        dbBase.insertOrUpdateEDetail(
                            value.geteDetailId().toString(),
                            gson.toJson(value)
                        )

                    }

                    // clear database
                    for (dbList in dbBase.getAlleDetail()) {
                        var isSet = false
                        for (mainList in response.body()?.data?.geteDetailingList()!!) {
                            if (mainList.geteDetailId() == dbList.geteDetailId()) {
                                isSet = true
                            }
                        }

                        //this clear database and files from device which is not in used
                        if (!isSet) {
                            dbBase.deleteEdetailingData(dbList.geteDetailId().toString())

                            var downloadModelArrayList =
                                dbBase.getAllDownloadedData(dbList.geteDetailId())

                            //Delete files from folder before erase db
                            for (item in downloadModelArrayList) {
                                val someDir = File(item.fileDirectoryPath)
                                someDir.deleteRecursively()
                            }

                            dbBase.deleteEdetailDownloada(dbList.geteDetailId().toString())

                        }
                    }
                }

                // if token expire go to login page again
                else
                {
                    Log.e("responseCode", "error")
                    sharePreferanceBase?.setPrefBool("isLogin", false)
                    val intent = Intent(this@HomePage, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else
            {
                generalClass.checkInternet()
            }
        }
    }

    suspend fun callingSyncAPI()
    {
        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).syncApiCoo("bearer " + loginModelBase?.accessToken)
        withContext(Dispatchers.Main) {
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    if (dbBase.datasCount > 0) {
                        dbBase.deleteAll()
                    }
                    dbBase?.addSyncData(Gson().toJson(response.body()))
                    bottomNavigation?.selectedItemId= R.id.landingPage
                    staticSyncData=response.body();
                }

                // if token expire go to login page again
                else{
                    Log.e("responseCode", "error")
                    sharePreferanceBase?.setPrefBool("isLogin", false)
                    val intent = Intent(this@HomePage, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else
            {   Log.e("responseERROR", response.errorBody().toString())
                generalClass.checkInternet()
            }
        }

    }

    //exit app alert
    fun exitAppAlert(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val exit_btn =
            dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setOnClickListener {
            finish();
            System.exit(0);
            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}