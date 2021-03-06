package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CheckDcrClass
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
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.SwitchCompat
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomePage : BaseActivity(),NavigationView.OnNavigationItemSelectedListener/*, UserLoginCallback.ZoomDemoAuthenticationListener , MeetingServiceListener, InitAuthSDKCallback*/
{
    var drawerProfileIv: ImageView?=null
    var bottomNavigation: BottomNavigationView? = null
    var openFragmentStr=""
    private var fragmentRefreshListener: FragmentRefreshListener? = null
    var stopDownload =false
    lateinit var objDownloadManager : DownloadManagerClass
    var retailerString=""
    var firstCall=false
    private var isAutoDownload = false


    companion object {
        var loginModelHomePage= LoginModel()
        var apiInterface: APIInterface? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        alertClass = AlertClass(this)

        val sharePreferance = PreferenceClass(this)
        var profileData = sharePreferance.getPref("profileData")

         loginModelHomePage = Gson().fromJson(profileData, LoginModel::class.java)
        val jsonObj= JSONObject(loginModelHomePage?.configurationSetting)

        try{
            val checkZoom=jsonObj.getInt("SET059")
            if(checkZoom==0)
            {
                nav_view?.getMenu()?.getItem(3)?.setVisible(false)
            }
        }
        catch (e:Exception){}


        apiInterface = APIClientKot().getClient(2, sharePreferance?.getPref("secondaryUrl")).create(
            APIInterface::class.java)


        if(generalClass.isInternetAvailable()) {
            callingMultipleAPI()

        }
        else {
            initView()
            bottomNavigation?.selectedItemId= R.id.landingPage
        }

      isAutoDownload= sharePreferanceBase?.getPrefBool("isAutoDownload") == true

        val menuItem = nav_view.menu.findItem(R.id.nav_autoDownload)
        val switch_id = menuItem.actionView.findViewById<SwitchCompat>(R.id.switch_id)
        switch_id.setChecked(isAutoDownload)
        switch_id.setOnClickListener(View.OnClickListener {
            if(isAutoDownload){isAutoDownload=false}
            else {isAutoDownload=true}
            sharePreferanceBase?.setPrefBool("isAutoDownload", isAutoDownload)
        })
    }

    @SuppressLint("RestrictedApi")
    fun initView()
    {
        bottomNavigation=findViewById(R.id.navigationView)
        bottomNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        userName_tv?.setText(loginModelHomePage.userName)

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

                if(::objDownloadManager.isInitialized && generalClass.isInternetAvailable() && DownloadManagerClass.cancelAutoDownload==false)
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
                if(::objDownloadManager.isInitialized && generalClass.isInternetAvailable() && DownloadManagerClass.cancelAutoDownload==false)
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
                CheckDcrClass(this,"homeActivity").checkDCR_UsingSP(item)
               // checkDCRusingShareP(item)
            }

        }
        false
    }

    //open fragment method
    private fun openFragment(fragment: Fragment)
    {

        val transaction = supportFragmentManager.beginTransaction()
        if(!retailerString.isEmpty())
        {
            val bundle = Bundle()
            bundle.putString("retailerData", retailerString)
            fragment.arguments=bundle
        }
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
       // transaction.commit()
        retailerString=""
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
                  //  overridePendingTransition(0, 0)
                }

                   R.id.nav_createPresentation -> {
                  var intent = Intent(this, CreatePresentationActivity::class.java)
                  startActivity(intent)
                 // overridePendingTransition(0, 0)
              }

                R.id.sync_menu -> {
                    if (generalClass.isInternetAvailable()) callingMultipleAPI() //  sync_api()
                    else alertClass.networkAlert()
                    drawer_layout?.closeDrawers()
                    return false
                }

             /*   R.id.nav_screenshot -> {
                    var intent = Intent(this, ImageSelectorActivity::class.java)
                    intent.putExtra("filePath", getFilesDir()?.getAbsolutePath() + "/Screenshots/")
                    intent.putExtra("selection", "delete")
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }*/

                R.id.nav_autoDownload -> {
                    drawer_layout?.closeDrawers()
                    return false
                }

                R.id.nav_scheduled -> {
                    var intent = Intent(this, MeetingActivity::class.java)
                    startActivity(intent)
                 //   overridePendingTransition(0, 0)
                }

                R.id.nav_logout ->
                {
                    logoutAppAlert()
                }

                R.id.nav_createpob -> {
                    var intent = Intent(this, CreatePobActivity::class.java)
                    intent.putExtra("action","pob")
                    startActivity(intent)
                }

                R.id.nav_sob -> {
                    var intent = Intent(this, CreatePobActivity::class.java)
                    intent.putExtra("action","stock")
                    startActivity(intent)
                }

            }

         drawer_layout?.closeDrawers()
        return true
    }


    override fun onResume()
    {
        super.onResume()

        alertClass = AlertClass(this)

        createConnectivity(this)

        if(nav_view!=null)
        {
            nav_view?.getMenu()?.getItem(0)?.setChecked(true)
        }

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

      /*  if(sharePreferanceBase?.getPref("SyncDate")==null || sharePreferanceBase?.getPref("SyncDate").toString().isEmpty())
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

        }*/

        setImages()

    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    fun callingMultipleAPI()
    {
        alertClass.showProgressAlert("")
        var isException=false
    //    if (dbBase.getDatasCount() > 0) {

       // }

        val coroutineScope= CoroutineScope(IO+ generalClass.coroutineExceptionHandler).launch {

            try{
                val profileApi= async { profileApi() }
                profileApi.await()
            }
            catch (e:Exception)
            {
                isException=true
                alertClass.hideAlert()
                alertClass.lowNetworkAlert()
            }

            try {
                val sync= async { callingSyncAPI() }
                val deleteItem= async {  dbBase.deleteAll() }
                val divisionApi =async { callingDivisionAPI() }
                val credientialApi= async { getSheduleMeetingAPI() }
                val quantityApi= async { getQuantityAPI() }
                val doctorGraphApi= async { getDoctorGraphAPI() }
                val getDocCall= async { getDocCallAPI() }
                val initilizeZoom= async {
                    val jsonObj= JSONObject(loginModelHomePage?.configurationSetting)
                    val checkZoom=jsonObj.getInt("SET059")
                    if(checkZoom!=0)
                    {
                        var zoomSDKBase = ZoomSDK.getInstance()
                        if(!zoomSDKBase.isLoggedIn)
                        {
                            getCredientailAPI(this@HomePage)
                        }
                    }

                }

                deleteItem.await()
                sync.await()
                divisionApi.await()
                credientialApi.await()
                quantityApi.await()
                initilizeZoom.await()
                doctorGraphApi.await()
                getDocCall.await()
            }
            catch (e:Exception)
            {
                alertClass.hideAlert()
                alertClass.commonAlert("OOps!","Something went wrong please try again later.")
            }


        }
        coroutineScope.invokeOnCompletion {
            coroutineScope.cancel()
            if(!isException)
            {
                this.runOnUiThread(java.lang.Runnable {
                    alertClass.hideAlert()
                    if(!firstCall) initView()
                    firstCall=true
                    bottomNavigation?.selectedItemId= R.id.landingPage
                    //  generalClass.disableProgress(progressView_parentRv!!)
                })
            }

        }

      val offlineCoroutine=  CoroutineScope(IO+ generalClass.coroutineExceptionHandler).launch {
            try {
                val sendEdetailing= async { submitDCRCo() }
                val sendRetailerList= async { submitDCRRetailer() }
                sendEdetailing.await()
                sendRetailerList.await()
            }
            catch (e:Exception)
            { }
      }
        offlineCoroutine.invokeOnCompletion {
            submitPOBAPI()
        }

    }

    fun setImages()
    {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)

        drawerProfileIv?.let {
            Glide.with(applicationContext).load(loginModelHomePage.imageName).apply(options).into(it) }
        Glide.with(applicationContext).load(loginModelHomePage.imageName).apply(options).into(profile_image)
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
        if (response?.isSuccessful == true) {
            if (response?.code() == 200 && !response.body().toString().isEmpty()) {

                     if(response.body()?.getData()?.geteDetailingList()==null)
                    {  }
                    else{
                         withContext(Dispatchers.Default){
                             launch {
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


                             } }
                    }


            }
        }


        /*
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
        }*/
    }

    suspend fun callingSyncAPI()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).syncApiCoo("bearer " + loginModelHomePage.accessToken)
            }


        if (response?.isSuccessful == true)
        {
            //  alertClass.commonAlert("",Gson().toJson(response.body()))
            if (response?.code() == 200 && !response.body().toString().isEmpty())
            {
                staticSyncData=response.body()?.data
                val apiModel=response.body()?.data
                val gson=Gson()
                runBlocking {
                    withContext(Dispatchers.IO) {
                        launch {  dbBase?.deleteAll_SYNCAPI() }
                    }
                }

              //  runBlocking {
                    withContext(Dispatchers.Default){

                        launch { dbBase?.addSYNCAPIData(gson.toJson(apiModel?.settingDCR),
                            gson.toJson(apiModel?.workTypeList),"",
                            "",gson.toJson(apiModel?.workingWithList),gson.toJson(apiModel?.fieldStaffTeamList),""
                            ,apiModel?.configurationSetting, gson.toJson(apiModel?.schemeList),
                            "",0, gson.toJson(apiModel?.doctorList)) }
                        launch {   dbBase?.addRoutes(apiModel?.routeList) }
                        launch {  dbBase?.addRetailer(apiModel?.retailerList) }
                        launch {  dbBase?.addProduct(apiModel?.productList)  }

                    }
               // }
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
        {   Log.e("responseERROR", response?.errorBody().toString())
            generalClass.checkInternet()
        }


       /* withContext(Dispatchers.Main) {
            if (response?.isSuccessful == true)
            {
              //  alertClass.commonAlert("",Gson().toJson(response.body()))
            if (response?.code() == 200 && !response.body().toString().isEmpty())
            {
                   // dbBase?.addAPIData(Gson().toJson(response.body()),1)


                       staticSyncData=response.body()?.data
                        val apiModel=response.body()?.data
                        val gson=Gson()
              CoroutineScope(Dispatchers.Unconfined).launch {
                    val asyn=async {
                        dbBase?.deleteAll_SYNCAPI()

                        dbBase?.addSYNCAPIData(gson.toJson(apiModel?.settingDCR),
                            gson.toJson(apiModel?.workTypeList),"",
                            "",gson.toJson(apiModel?.workingWithList),gson.toJson(apiModel?.fieldStaffTeamList),""
                            ,apiModel?.configurationSetting, gson.toJson(apiModel?.schemeList),
                            "",0, gson.toJson(apiModel?.doctorList))
                        dbBase?.addRoutes(apiModel?.routeList)
                        dbBase?.addRetailer(apiModel?.retailerList)
                        dbBase?.addProduct(apiModel?.productList) }
                    asyn.await()
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
            {   Log.e("responseERROR", response?.errorBody().toString())
                generalClass.checkInternet()
            }
        }*/
    }

    suspend fun getQuantityAPI()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getQuantiyApiCoo("bearer " + loginModelHomePage.accessToken)
            }

            Log.e("quantitiveAPI",response.toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    val gson = Gson()
                    var model = response.body()
                    withContext(Dispatchers.Default) {
                        launch { dbBase?.addAPIData(gson.toJson(model?.getData()), 3) }
                    }

                }
                else Log.e("elsequantitiveAPI", response.code().toString())
            }
            else Log.e("quantitiveAPIERROR", response?.errorBody().toString())



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

            Log.e("getDoctorGraphAPI", response?.body().toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    var model = response.body()
                    withContext(Dispatchers.Main) {
                        launch { dbBase?.addAPIData(Gson().toJson(model?.getData()), 4) }
                    }
                }
                else Log.e("elseDoctorGraphAPI", response.code().toString())
            }
            else Log.e("DoctorGraphERROR", response?.errorBody().toString())

    }

    suspend fun profileApi()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getProfileData("bearer " + loginModelHomePage.accessToken,
                    loginModelHomePage.empId.toString())
            }

            Log.e("getProfileAPI", response?.body().toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    var model = response.body()
                    withContext(Dispatchers.Main) {
                        launch {   dbBase?.addAPIData(Gson().toJson(model?.getData()), 6) }
                    }
                }
                else Log.e("elseProfileAPI", response.code().toString())
            }
            else Log.e("getProfileAPIERROR", response?.errorBody().toString())

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
            runOnUiThread(java.lang.Runnable { alertClass?.showProgressAlert("") })
            try{
                val coroutineScope = CoroutineScope(Dispatchers.IO + generalClass.coroutineExceptionHandler).launch {

                   try {
                       val api = async { checkCurrentDCR_API(onMenuItemClickListener) }
                       api.await()
                   }
                   catch (e:Exception)
                   {
                       runOnUiThread { alertClass?.hideAlert()
                       alertClass?.networkAlert()
                       }
                   }
                }

                coroutineScope.invokeOnCompletion {
                    coroutineScope.cancel()
                    runOnUiThread(java.lang.Runnable {
                        alertClass?.hideAlert() })
                }
            }
            catch (e:Exception)
            {
                runOnUiThread { alertClass.networkAlert()
                alertClass.hideAlert()}

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

            if (response?.isSuccessful==true) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    if (response.body()?.errorObj?.errorMessage?.isEmpty() == true) {

                        val dcrData=response.body()?.data?.dcrData

                        if(staticSyncData?.settingDCR?.isCallPlanMandatoryForDCR==true && response.body()?.data?.isCPExiest == false)
                        {
                            runOnUiThread(java.lang.Runnable {
                                alertClass?.commonAlert("Alert!","Please submit your day plan first")

                            })
                            return
                        }
                        if (dcrData?.rtpApproveStatus?.lowercase() != "a") {

                            alertClass?.commonAlert("Alert!","Tour plan not approved")
                            return
                        }

                        if (dcrData?.dataSaveType?.lowercase() == "s") {

                            runOnUiThread(java.lang.Runnable {
                                alertClass?.commonAlert("Alert!","The DCR is submitted it cannot be unlocked please connect with your admin")
                                onMenuItemClickListener.setCheckable(false)
                                onMenuItemClickListener.setChecked(false)
                            })

                            return
                        }


                        if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {
                            runOnUiThread {
                                alertClass?.commonAlert("Alert!", "Please submit tour program first")
                                alertClass?.hideAlert()
                                onMenuItemClickListener.setCheckable(false)
                                onMenuItemClickListener.setChecked(false)
                            }

                            return
                        }

                        dcrData?.dataSaveType="D"
                        sharePreferanceBase?.setPref("dcrObj", Gson().toJson(dcrData))

                        if (dcrData?.dcrId == 0) {

                            runOnUiThread {
                                alertClass.createDCRAlert(
                                    dcrData?.routeId.toString(),
                                    dcrData?.routeName.toString()
                                )
                                onMenuItemClickListener.setCheckable(false)
                                onMenuItemClickListener.setChecked(false)
                            }

                            sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())

                        } else {
                            sharePreferanceBase?.setPref("todayDate", generalClass?.currentDateMMDDYY())
                            sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())
                            sharePreferanceBase?.setPref("empIdSp", loginModelHomePage.empId.toString())

                            if(dcrData?.otherDCR!=0)
                            {
                               // setOtherActivityView()
                                runOnUiThread {
                                    alertClass?.commonAlert("Alert!","You have not planned field working today. Kindly save it from Salestrip app")
                                    onMenuItemClickListener.setCheckable(false)
                                    onMenuItemClickListener.setChecked(false)
                                }
                                sharePreferanceBase?.setPref("otherActivitySelected","1")

                                return
                            }
                           // if(!setDcrCheck) {
                           //     setDcrCheck=true
                           //     bottomNavigation?.selectedItemId = R.id.callPage
                           // }
                            runOnUiThread {
                                toolbarTv?.setText("Create Calls")
                                val fragment = NewCallFragment()
                                openFragment(fragment)
                                openFragmentStr = "CallsFragment"
                                onMenuItemClickListener.setCheckable(true)
                                onMenuItemClickListener.setChecked(true)
                            }
                        }
                    } else {
                        GeneralClass(this@HomePage).checkInternet()
                        runOnUiThread {
                            onMenuItemClickListener.setCheckable(false)
                            onMenuItemClickListener.setChecked(false) }

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


    fun selectRetailerForEdit(toJson: String) {
        retailerString=toJson
        bottomNavigation?.selectedItemId= R.id.callPage

    }

    fun backToHome()
    {
        bottomNavigation?.selectedItemId= R.id.landingPage
    }

    fun onDcrChecked()
    {
        toolbarTv?.setText("Create Calls")
        val fragment = NewCallFragment()
        openFragment(fragment)
        openFragmentStr = "CallsFragment"
    }


}