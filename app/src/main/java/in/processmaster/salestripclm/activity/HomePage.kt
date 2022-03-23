package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.fragments.EdetailingDownloadFragment
import `in`.processmaster.salestripclm.fragments.HomeFragment
import `in`.processmaster.salestripclm.fragments.NewCallFragment
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.PreferenceClass
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONObject
import us.zoom.sdk.ZoomSDK
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener/*, UserLoginCallback.ZoomDemoAuthenticationListener , MeetingServiceListener, InitAuthSDKCallback*/
{
    var drawer_layout : DrawerLayout?=null
    var drawerProfileIv: ImageView?=null
    var bottomNavigation: BottomNavigationView? = null
    var openFragmentStr=""


    companion object {
        var loginModelHomePage= LoginModel()
        var apiInterface: APIInterface? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val sharePreferance = PreferenceClass(this)
        var profileData = sharePreferance.getPref("profileData")

         loginModelHomePage = Gson().fromJson(profileData, LoginModel::class.java)
        apiInterface = APIClientKot().getClient(2, sharePreferance?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        initView()

        if(generalClass.isInternetAvailable())
            callingMultipleAPI()
        else {
            if (dbBase?.getApiDetail(1) != "") {
                staticSyncData = Gson().fromJson(dbBase?.getApiDetail(1), SyncModel::class.java)
            }
            bottomNavigation?.selectedItemId= R.id.landingPage
        }

    }


    @SuppressLint("RestrictedApi")
    fun initView()
    {
        bottomNavigation=findViewById(R.id.navigationView)
        bottomNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        drawer_layout=findViewById(R.id.drawer_layout) as DrawerLayout
        userName_tv?.setText(loginModelHomePage.userName!!)
        //change status bar colour
        changeStatusBar()

        try
        {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0)
            val version = pInfo.versionName
            appVersion_tv?.setText("v.${version}")
        }
        catch (e: PackageManager.NameNotFoundException)
        { e.printStackTrace() }

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
                            logoutAppAlert() } }
                    return true }
                override fun onMenuModeChange(menu: MenuBuilder) {}
            })
            // Display the menu
            optionsMenu.show()
        })
        menu_img?.setOnClickListener({ drawer_layout!!.openDrawer(Gravity.LEFT) })

        val headerView: View = nav_view?.inflateHeaderView(R.layout.nav_header_main)!!
        var drawerTv=  headerView.findViewById(R.id.profileName_tv_drawer) as TextView
        drawerProfileIv=  headerView.findViewById(R.id.profile_iv_drawer) as ImageView

        drawerTv?.setText(loginModelHomePage.userName)

        setImages()
        nav_view?.setNavigationItemSelectedListener(this)
        nav_view?.getMenu()?.getItem(0)?.setChecked(true)

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

                val fragment = EdetailingDownloadFragment()
                openFragment(fragment)
                openFragmentStr = "EdetailingFragment"
                return@OnNavigationItemSelectedListener true
            }

            R.id.displayVisualPage -> {
                toolbarTv?.setText("Presentation")
                val fragment = PresentEDetailingFrag()
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

            R.id.nav_home -> { }

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
                if (generalClass.isInternetAvailable()) callingMultipleAPI() //  sync_api()
                else alertClass.networkAlert()
            }

            R.id.nav_scheduled -> {
                var intent = Intent(this, MeetingActivity::class.java)
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
            if(sharePreferanceBase?.getPref("SyncDate").toString().equals(formattedDate))
            { }
            else
            {
                sharePreferanceBase?.setPref("SyncDate", formattedDate)
                if(generalClass.isInternetAvailable()) callingMultipleAPI()
                else {
                    if (dbBase?.getApiDetail(1) != "") staticSyncData = Gson().fromJson(dbBase?.getApiDetail(1), SyncModel::class.java)
                }
            }

        }

        setImages()

    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    fun callingMultipleAPI()
    {
        alertClass.showProgressAlert("")

        if (dbBase.getDatasCount() > 0) {
            dbBase.deleteAll()
        }

        val coroutineScope= CoroutineScope(IO).launch {
            val sync= async { callingSyncAPI() }

            val divisionApi =async { callingDivisionAPI() }

            val credientialApi= async { getSheduleMeetingAPI() }

            val quantityApi= async { getQuantityAPI() }

            val sendEdetailing= async { submitDCRCo() }

            val doctorGraphApi= async { getDoctorGraphAPI() }

            val getDocCall= async { getDocCallAPI() }

            val profileApi= async { profileApi() }

            val initilizeZoom= async {
                var zoomSDKBase = ZoomSDK.getInstance()
                if(!zoomSDKBase.isLoggedIn)
                {
                    getCredientailAPI(this@HomePage)
                }
            }
            sync.await()
            divisionApi.await()
            credientialApi.await()
            quantityApi.await()
            initilizeZoom.await()
            sendEdetailing.await()
            doctorGraphApi.await()
            getDocCall.await()
            profileApi.await()

        }
        coroutineScope.invokeOnCompletion {
            this.runOnUiThread(java.lang.Runnable {
                alertClass.hideAlert()
                bottomNavigation?.selectedItemId= R.id.landingPage
              //  generalClass.disableProgress(progressView_parentRv!!)
            })
        }
    }

    fun setImages()
    {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(android.R.mipmap.sym_def_app_icon)
            .error(android.R.mipmap.sym_def_app_icon)

        Glide.with(this).load(loginModelHomePage.imageName).apply(options).into(drawerProfileIv!!)
        Glide.with(this).load(loginModelHomePage.imageName).apply(options).into(profile_image)
    }


    suspend fun callingDivisionAPI()
    {
        val jsonObject = JSONObject(loginModelHomePage.getEmployeeObj().toString())

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).detailingApiCoo( "bearer " + loginModelHomePage.accessToken, jsonObject.getString(
            "Division"
        ))
        withContext(Dispatchers.Main) {
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    for ((index, value) in response.body()?.getData()?.geteDetailingList()?.withIndex()!!) {
                        //store edetailing data to db
                        val gson = Gson()
                        dbBase.insertOrUpdateEDetail(
                            value.geteDetailId().toString(),
                            gson.toJson(value)
                        )
                    }

                    // clear database
                    for (dbList in dbBase.getAlleDetail()!!) {
                        var isSet = false
                        for (mainList in response.body()?.getData()?.geteDetailingList()!!) {
                            if (mainList.geteDetailId() == dbList.geteDetailId()) {
                                isSet = true
                            }
                        }

                        //this clear database and files from device which is not in used
                        if (!isSet) {
                            dbBase.deleteEdetailingData(dbList.geteDetailId().toString())

                            var downloadModelArrayList =
                                dbBase.getAllDownloadedData(dbList.geteDetailId()!!)

                            //Delete files from folder before erase db
                            for (item in downloadModelArrayList!!) {
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
        ).syncApiCoo("bearer " + loginModelHomePage.accessToken)
        withContext(Dispatchers.Main) {
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    dbBase?.addAPIData(Gson().toJson(response.body()),1)
                    staticSyncData=response.body()
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

    suspend fun getQuantityAPI()
    {
        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).getQuantiyApiCoo("bearer " + loginModelHomePage.accessToken)
        withContext(Dispatchers.Main) {
            Log.e("quantitiveAPI",response.toString())
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty()!!) {
                    val gson = Gson()
                    var model = response.body()

                    dbBase?.addAPIData(gson.toJson(model?.getData()), 3)

                }
                else Log.e("elsequantitiveAPI", response.code().toString())
            }
            else Log.e("quantitiveAPIERROR", response.errorBody().toString())

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==3){
            toolbarTv?.setText("Create Calls")
            val fragment = NewCallFragment()
            openFragment(fragment)
            openFragmentStr = "CallsFragment"
        }
    }

    suspend fun getDoctorGraphAPI()
    {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -6)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val c:Date=cal.time
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).visitDoctorGraphApi("bearer " + loginModelHomePage.accessToken,df.format(c),generalClass.currentDateMMDDYY())
        withContext(Dispatchers.Main) {
            Log.e("getDoctorGraphAPI", response.body().toString()!!)
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage!!.isEmpty()) {
                    var model = response.body()
                    dbBase?.addAPIData(Gson().toJson(model?.getData()), 4)
                  }
                else Log.e("elseDoctorGraphAPI", response.code().toString())
            }
            else Log.e("DoctorGraphERROR", response.errorBody().toString())
        }
    }


    suspend fun profileApi()
    {
        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).getProfileData("bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId.toString())
        withContext(Dispatchers.Main) {
            Log.e("getProfileAPI", response?.body().toString()!!)
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage!!.isEmpty()) {
                    var model = response.body()
                    dbBase?.addAPIData(Gson().toJson(model?.getData()), 6)
                }
                else Log.e("elseProfileAPI", response.code().toString())
            }
            else Log.e("getProfileAPIERROR", response.errorBody().toString())
        }
    }

}