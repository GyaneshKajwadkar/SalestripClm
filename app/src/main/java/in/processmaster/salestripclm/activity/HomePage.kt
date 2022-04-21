package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.EdetailingDownloadFragment
import `in`.processmaster.salestripclm.fragments.HomeFragment
import `in`.processmaster.salestripclm.fragments.NewCallFragment
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DownloadManagerClass
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import us.zoom.sdk.ZoomSDK
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener/*, UserLoginCallback.ZoomDemoAuthenticationListener , MeetingServiceListener, InitAuthSDKCallback*/
{
    var drawerProfileIv: ImageView?=null
    var bottomNavigation: BottomNavigationView? = null
    var openFragmentStr=""
    private var fragmentRefreshListener: FragmentRefreshListener? = null
    var stopDownload =false
    lateinit var downloadManager :DownloadManager
    lateinit var objDownloadManager : DownloadManagerClass

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

        if(generalClass.isInternetAvailable()) callingMultipleAPI()
        else {
           /* if (dbBase?.getApiDetail(1) != "") {
                val coroutineScope= CoroutineScope(Dispatchers.IO).launch {
                    val syncSmallData= async {  staticSyncData= dbBase?.getSYNCApiData(0)}
                    val syncRouteData= async {  staticSyncData?.routeList=dbBase?.allRoutes}
                    val syncRetailerData= async {  staticSyncData?.retailerList=dbBase?.allRetailers}
                    val syncProductData= async {  staticSyncData?.productList=dbBase?.allProduct}

                    syncSmallData.await()
                    syncRouteData.await()
                    syncRetailerData.await()
                    syncProductData.await()

                }
            }*/
            bottomNavigation?.selectedItemId= R.id.landingPage
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("RestrictedApi")
    fun initView()
    {
        bottomNavigation=findViewById(R.id.navigationView)
        bottomNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        userName_tv?.setText(loginModelHomePage.userName)
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
            val optionsMenu = MenuPopupHelper(this, menuBuilder, menu_iv)
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
        menu_img?.setOnClickListener({ drawer_layout.openDrawer(Gravity.LEFT) })

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

        if(staticSyncData?.configurationSetting==null)
        {
            alertClass?.commonAlert("Server error","Something went wrong please Sync data or try again later")
            return@OnNavigationItemSelectedListener true
        }
        when (item.itemId) {
            R.id.landingPage -> {
                toolbarTv?.setText("Salestrip CLM")
                val fragment = HomeFragment()
                openFragment(fragment)
                openFragmentStr = "HomeFragment"
                return@OnNavigationItemSelectedListener true
            }

            R.id.downloadVisualPage -> {

                if(::objDownloadManager.isInitialized && generalClass.isInternetAvailable())
                {
                    if(objDownloadManager?.getNumber<objDownloadManager?.allProductList.size)
                    {
                        objDownloadManager.downloadProgressAlert()
                        return@OnNavigationItemSelectedListener false
                    }
                }
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
                if(::objDownloadManager.isInitialized && generalClass.isInternetAvailable())
                {
                    if(objDownloadManager?.getNumber<objDownloadManager?.allProductList.size)
                    {
                        objDownloadManager.downloadProgressAlert()
                        return@OnNavigationItemSelectedListener false
                    }
                }

                if (openFragmentStr.equals("CallsFragment")) {
                    return@OnNavigationItemSelectedListener true
                }
                checkDCRusingShareP(item)
               // checkDcrApi(item)
            /*    if( && !setDcrCheck){
                    return@OnNavigationItemSelectedListener false
                }
                else
                {
                    toolbarTv?.setText("Create Calls")

                    if (openFragmentStr.equals("CallsFragment")) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val fragment = NewCallFragment()
                    openFragment(fragment)
                    openFragmentStr = "CallsFragment"
                    return@OnNavigationItemSelectedListener true
                }*/


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
        runOnUiThread(java.lang.Runnable {

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

            stopDownload=true

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
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val runnable= java.lang.Runnable {
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
        }
        Thread(runnable).start()

        runOnUiThread{
            drawer_layout?.closeDrawers()
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

        if(sharePreferanceBase?.getPref("SyncDate")==null || sharePreferanceBase?.getPref("SyncDate").toString().isEmpty())
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
                  //  if (dbBase?.getApiDetail(1) != "") staticSyncData = Gson().fromJson(dbBase?.getApiDetail(1), SyncModel::class.java)
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

    //    if (dbBase.getDatasCount() > 0) {
            dbBase.deleteAll()
       // }

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

        drawerProfileIv?.let {
            Glide.with(this).load(loginModelHomePage.imageName).apply(options).into(it) }
        Glide.with(this).load(loginModelHomePage.imageName).apply(options).into(profile_image)
    }


    suspend fun callingDivisionAPI()
    {
        val jsonObject = JSONObject(loginModelHomePage.getEmployeeObj().toString())

        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).detailingApiCoo( "bearer " + loginModelHomePage.accessToken, jsonObject.getString(
                    "Division"
                ))
            }
        withContext(Dispatchers.Main) {
            if (response?.isSuccessful == true)
            {
                if (response?.code() == 200 && !response.body().toString().isEmpty())
                {
                    if(response.body()?.getData()?.geteDetailingList()==null)
                    {  }
                    else{

                    for ((index, value) in response.body()?.getData()?.geteDetailingList()?.withIndex()!!) {
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
                        for (mainList in response.body()?.getData()?.geteDetailingList()!!) {
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

                     //get all remaining download file
                     val list= dbBase.getAlleDetail().filter { s-> s.isSaved==0} as ArrayList<DevisionModel.Data.EDetailing>
                        if(list.size!=0 && generalClass.isInternetAvailable())
                        {
                            objDownloadManager= DownloadManagerClass(this@HomePage,dbBase,list)
                            objDownloadManager.startDownloading()
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
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).syncApiCoo("bearer " + loginModelHomePage.accessToken)
            }
        withContext(Dispatchers.Main) {
            if (response?.isSuccessful == true)
            {
              //  alertClass.commonAlert("",Gson().toJson(response.body()))
            if (response?.code() == 200 && !response.body().toString().isEmpty())
                {
                   // dbBase?.addAPIData(Gson().toJson(response.body()),1)
                    staticSyncData=response.body()?.data
                    val apiModel=response.body()?.data
                    val gson=Gson()
                    dbBase?.deleteAll_SYNCAPI()

                    dbBase?.addSYNCAPIData(gson.toJson(apiModel?.settingDCR),
                         gson.toJson(apiModel?.workTypeList),"",
                         "",gson.toJson(apiModel?.workingWithList),gson.toJson(apiModel?.fieldStaffTeamList),""
                         ,apiModel?.configurationSetting, gson.toJson(apiModel?.schemeList),
                         "",0, gson.toJson(apiModel?.doctorList))
                    dbBase?.addRoutes(apiModel?.routeList)
                    dbBase?.addRetailer(apiModel?.retailerList)
                    dbBase?.addProduct(apiModel?.productList)

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
            {   Log.e("responseERROR", response?.errorBody().toString())
                generalClass.checkInternet()
            }
        }

    }

    suspend fun getQuantityAPI()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getQuantiyApiCoo("bearer " + loginModelHomePage.accessToken)
            }
        withContext(Dispatchers.Main) {
            Log.e("quantitiveAPI",response.toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    val gson = Gson()
                    var model = response.body()

                    dbBase?.addAPIData(gson.toJson(model?.getData()), 3)

                }
                else Log.e("elsequantitiveAPI", response.code().toString())
            }
            else Log.e("quantitiveAPIERROR", response?.errorBody().toString())

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
           // toolbarTv?.setText("Create Calls")
           // val fragment = NewCallFragment()
           // openFragment(fragment)
           // openFragmentStr = "CallsFragment"
        }
    }

    suspend fun getDoctorGraphAPI()
    {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -6)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val c:Date=cal.time
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).visitDoctorGraphApi("bearer " + loginModelHomePage.accessToken,df.format(c),generalClass.currentDateMMDDYY())
            }
        withContext(Dispatchers.Main) {
            Log.e("getDoctorGraphAPI", response?.body().toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    var model = response.body()
                    dbBase?.addAPIData(Gson().toJson(model?.getData()), 4)
                  }
                else Log.e("elseDoctorGraphAPI", response.code().toString())
            }
            else Log.e("DoctorGraphERROR", response?.errorBody().toString())
        }
    }


    suspend fun profileApi()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getProfileData("bearer " + loginModelHomePage.accessToken,
                    loginModelHomePage.empId.toString())
            }
        withContext(Dispatchers.Main) {
            Log.e("getProfileAPI", response?.body().toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    var model = response.body()
                    dbBase?.addAPIData(Gson().toJson(model?.getData()), 6)
                }
                else Log.e("elseProfileAPI", response.code().toString())
            }
            else Log.e("getProfileAPIERROR", response?.errorBody().toString())
        }
    }

    fun getFragmentRefreshListener(): FragmentRefreshListener? {
        return fragmentRefreshListener
    }

    fun setFragmentRefreshListener(fragmentRefreshListener: FragmentRefreshListener?) {
        this.fragmentRefreshListener = fragmentRefreshListener
    }

    interface FragmentRefreshListener {
        fun onRefresh()
    }

    fun checkDCRusingShareP(onMenuItemClickListener: MenuItem)
    {


        if(generalClass?.isInternetAvailable() == true)
        {
            alertClass?.showProgressAlert("")
            val coroutineScope = CoroutineScope(Dispatchers.IO).launch {
                val api = async { checkCurrentDCR_API(onMenuItemClickListener) }
                 api.await()
            }

            coroutineScope.invokeOnCompletion {
                runOnUiThread(java.lang.Runnable {
                    alertClass?.hideAlert() })
            }
            //return false
        }

        else if(sharePreferanceBase?.checkKeyExist("empIdSp")==false  ||
            sharePreferanceBase?.checkKeyExist("todayDate")==false  || sharePreferanceBase?.checkKeyExist("dcrId")==false || sharePreferanceBase?.getPref("empIdSp") != loginModelHomePage.empId.toString())
        {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
            onMenuItemClickListener.setCheckable(false)
            onMenuItemClickListener.setChecked(false)
        }
        else if( sharePreferanceBase?.getPref("todayDate") != generalClass?.currentDateMMDDYY() || sharePreferanceBase?.getPref("dcrId")=="0") {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
            onMenuItemClickListener.setCheckable(false)
            onMenuItemClickListener.setChecked(false)
        }
        else{
            toolbarTv?.setText("Create Calls")
            val fragment = NewCallFragment()
            openFragment(fragment)
            openFragmentStr = "CallsFragment"
            onMenuItemClickListener.setCheckable(true)
            onMenuItemClickListener.setChecked(true)}
    }

/*    fun callCoroutineApi() :Boolean {
        var callCoroutineReturn=false

        alertClass?.showProgressAlert("")
        val coroutineScope = CoroutineScope(Dispatchers.IO).launch {
            val api = async { checkCurrentDCR_API(onMenuItemClickListener) }
            callCoroutineReturn= api.await()
        }

        coroutineScope.invokeOnCompletion {
            runOnUiThread(java.lang.Runnable {
                alertClass?.hideAlert() })
        }
        return callCoroutineReturn
    }*/


    suspend fun checkCurrentDCR_API(onMenuItemClickListener: MenuItem) {

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!).checkDCR_API(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId,
            generalClass?.currentDateMMDDYY()
        )
        withContext(Dispatchers.Main) {

            if (response!!.isSuccessful) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if (response.body()?.errorObj?.errorMessage?.isEmpty() == true) {

                        val dcrData=response.body()?.data?.dcrData

                        if(staticSyncData?.settingDCR?.isCallPlanMandatoryForDCR==true && response.body()?.data?.isCPExiest == true)
                        {
                            alertClass?.commonAlert("Alert!","Please submit your day plan first")
                            return@withContext
                        }

                        if (dcrData?.dataSaveType?.lowercase() == "s") {
                            alertClass?.commonAlert("Alert!","The DCR is submitted it cannot be unlocked please connect with your admin")
                            onMenuItemClickListener.setCheckable(false)
                            onMenuItemClickListener.setChecked(false)
                            return@withContext
                        }


                        if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {

                            alertClass?.commonAlert("Alert!", "Please submit tour program first")
                            alertClass?.hideAlert()
                            onMenuItemClickListener.setCheckable(false)
                            onMenuItemClickListener.setChecked(false)
                            return@withContext
                        }

                        dcrData?.dataSaveType="D"
                        sharePreferanceBase?.setPref("dcrObj", Gson().toJson(dcrData))

                        if (dcrData?.dcrId == 0) {

                           // createDCRAlert(dcrData?.routeId.toString())
                            alertClass.createDCRAlert(
                                dcrData?.routeId.toString(),
                                dcrData?.routeName.toString()
                            )
                            sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())
                            onMenuItemClickListener.setCheckable(false)
                            onMenuItemClickListener.setChecked(false)
                        } else {
                            sharePreferanceBase?.setPref("todayDate", generalClass?.currentDateMMDDYY())
                            sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())
                            sharePreferanceBase?.setPref("empIdSp", loginModelHomePage.empId.toString())

                            if(dcrData?.otherDCR!=0)
                            {
                               // setOtherActivityView()

                                alertClass?.commonAlert("Alert!","You have not planned field working today. Kindly save it from Salestrip app")
                                sharePreferanceBase?.setPref("otherActivitySelected","1")
                                onMenuItemClickListener.setCheckable(false)
                                onMenuItemClickListener.setChecked(false)
                                return@withContext
                            }
                           // if(!setDcrCheck) {
                           //     setDcrCheck=true
                           //     bottomNavigation?.selectedItemId = R.id.callPage
                           // }
                            toolbarTv?.setText("Create Calls")


                            val fragment = NewCallFragment()
                            openFragment(fragment)
                            openFragmentStr = "CallsFragment"
                            onMenuItemClickListener.setCheckable(true)
                            onMenuItemClickListener.setChecked(true)

                        }
                    } else {
                        GeneralClass(this@HomePage).checkInternet()
                        onMenuItemClickListener.setCheckable(false)
                        onMenuItemClickListener.setChecked(false)
                     }
                }
            }
        }

    }

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog, checked: Boolean) {
        alertClass?.showProgressAlert("")
        var call: Call<JsonObject> = apiInterface?.saveDCS("bearer " + loginModelHomePage.accessToken,dcrObject) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                alertClass?.hideAlert()
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val jsonObjError: JsonObject = response.body()?.get("errorObj") as JsonObject
                    if(!jsonObjError.get("errorMessage").asString.isEmpty())
                    {
                        alertClass?.commonAlert("",jsonObjError.get("errorMessage").asString)
                    }
                    else {
                        val jsonObjData: JsonObject = response.body()?.get("data") as JsonObject

                        if(!checked)
                        {
                            alertClass?.commonAlert("",jsonObjData.get("message").asString + "And kindly moved to Salestrip to submit DCR")
                        }
                        else
                        {
                            alertClass?.commonAlert("",jsonObjData.get("message").asString)
                        }

                        if(!checked) sharePreferanceBase?.setPref("otherActivitySelected","1")

                       /* if(generalClass?.isInternetAvailable() == true && checked)
                        {
                            CoroutineScope(Dispatchers.IO).launch {
                                val api = async { checkCurrentDCR_API() }
                                api.await()
                            }
                        }*/

                        alertDialog.cancel()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass?.hideAlert()
                alertDialog.cancel()// check internet connection
                call.cancel()
            }
        })
    }





}