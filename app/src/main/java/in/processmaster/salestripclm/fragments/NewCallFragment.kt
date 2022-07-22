package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageAlertClass
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageGeneralClass
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageSharePref
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CheckDcrClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.interfaceCode.StringInterface
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.GPSTracker
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.bottom_sheet_visualads.view.*
import kotlinx.android.synthetic.main.dcr_entry.view.*
import kotlinx.android.synthetic.main.fragment_new_call.*
import kotlinx.android.synthetic.main.fragment_new_call.view.*
import kotlinx.android.synthetic.main.join_activity_view.view.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class NewCallFragment : Fragment(),StringInterface {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var views:View?=null
    var adapter:BottomSheetDoctorAdapter =BottomSheetDoctorAdapter()
    var doctorListArray:ArrayList<SyncModel.Data.Doctor> = ArrayList()
    var retailerListArray:ArrayList<SyncModel.Data.Retailer> = ArrayList()
    var routeList: ArrayList<SyncModel.Data.Route> = ArrayList()
    var ristrictedRouteList: ArrayList<SyncModel.Data.Route> = ArrayList()
    var teamsList: ArrayList<SyncModel.Data.FieldStaffTeam> = ArrayList()
    var selectionType=0
    var  docCallModel : DailyDocVisitModel.Data= DailyDocVisitModel.Data()
    var isSecondTime = false
    var doctorObject=SyncModel.Data.Doctor()
    var isRetailerAttached=false
    var preCallObj= PreCallsFragment()
    companion object {
        var retailerObj= SyncModel.Data.Retailer()
        var instance: NewCallFragment? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_new_call, container, false)

        if (activity != null && isAdded)
        {
            initView()
            instance = this
        }



        return views
    }

    fun initView()
    {
        bottomSheetBehavior = BottomSheetBehavior.from(views!!.bottomSheet)

        val strtext = arguments?.getString("retailerData")

        if(strtext?.isEmpty()==false)
        {
            isRetialerEdit()
        }
        else
        {
            val coroutine=viewLifecycleOwner.lifecycleScope.launch {
                val task=async {
                    SplashActivity.staticSyncData?.doctorList?.let   { doctorListArray.addAll(it) }
                    SplashActivity.staticSyncData?.routeList?.let    { routeList.addAll(it) }
                    SplashActivity.staticSyncData?.retailerList?.let { retailerListArray.addAll(it) }

                    staticSyncData?.fieldStaffTeamList?.let { teamsList.addAll(it) }

                    val iterator = teamsList.iterator()
                    while(iterator.hasNext()){
                        val item = iterator.next()
                        if(item.empId == loginModelHomePage.empId){
                            iterator.remove()
                        }
                    }

                    routeList = routeList?.filter { s -> s.headQuaterName !=""} as java.util.ArrayList<SyncModel.Data.Route>

                   /* val responseDocCall=db.getApiDetail(5)
                    if(!responseDocCall.equals("")) {
                        docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
                    }*/
                    if(staticSyncData?.settingDCR?.isRestrictedParty==true)
                    {
                        if(HomePage.homePageSharePref?.getPref("dcrObj")?.isEmpty() == false) {
                            var dcrModel = Gson().fromJson(HomePage.homePageSharePref?.getPref("dcrObj"), GetDcrToday.Data.DcrData::class.java)

                            var routeListPartList: ArrayList<SyncModel.Data.Route> = ArrayList()
                            val items: List<String>? = dcrModel?.routeId?.split(",")

                            if (items != null) {
                                for (data in items) {
                                    for(route in routeList)
                                    {
                                        if(route.routeId==data.toInt())
                                        { routeListPartList.add(route) }
                                    } }}
                            routeList.clear()
                            ristrictedRouteList.addAll(routeListPartList)
                        }
                    }
                    adapter =BottomSheetDoctorAdapter()
                }
                task.await()
            }
            coroutine.invokeOnCompletion {
                coroutine.cancel()
                activity?.runOnUiThread {
                    if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
                    {
                        views?.selectRoutesCv?.setEnabled(false)
                    }
                    else if(SplashActivity.staticSyncData?.settingDCR?.roleType=="FS") {
                        views?.selectTeamsCv?.visibility = View.GONE
                        views?.selectTeamHeader_tv?.visibility = View.GONE
                        views?.selectRoutesCv?.setEnabled(true)
                        views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
                    }

                    if(homePageSharePref?.getPref("otherActivitySelected").equals("1") && HomePage.homePageSharePref?.checkKeyExist("todayDate")==true && homePageSharePref?.getPref("todayDate") == HomePage.homePageGeneralClass?.currentDateMMDDYY() )
                    {
                        if(HomePage.homePageGeneralClass?.isInternetAvailable()==false)
                        {
                            setOtherActivityView()
                        }

                    }

                    views?.docRetail_switch?.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            views?.selectDoctor_tv?.setText("Select Doctor")
                            views?.selectionDocRet_tv?.setText("Select Doctor")
                            activity?.let {
                                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(it, R.color.darkBlue))
                                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(it, R.color.gray))
                            }
                        }
                        else
                        {
                            views?.selectDoctor_tv?.setText("Select Retailer")
                            views?.selectionDocRet_tv?.setText("Select Retailer")

                            activity?.let {
                                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(it, R.color.darkBlue))
                                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(it, R.color.gray))
                            }


                        }
                        if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
                        {
                            views?.selectRoutesCv?.setEnabled(false)
                            views?.selectTeam_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
                            views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
                        }
                        else if(SplashActivity.staticSyncData?.settingDCR?.roleType=="FS") {
                            views?.selectTeamsCv?.visibility = View.GONE
                            views?.selectTeamHeader_tv?.visibility = View.GONE
                            views?.selectRoutesCv?.setEnabled(true)
                            views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
                        }

                        activity?.runOnUiThread {
                            views?.doctorDetail_parent?.visibility=View.INVISIBLE
                         //   views?.precall_parent?.visibility=View.INVISIBLE
                         //   views?.parentButton?.visibility=View.INVISIBLE
                            views?.noData_gif?.visibility=View.VISIBLE
                            views?.frameRetailer_view?.visibility=View.GONE
                            views?.framePreCall_view?.visibility=View.GONE
                            views?.retailer_parent?.visibility=View.GONE
                            views?.selectRoute_tv?.setText("Select route")
                            views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
                            views?.selectTeam_tv?.setText("Select team")
                            views?.selectDoctorsCv?.setEnabled(false)
                        }

                    }

                    views?.selectTeamsCv?.setOnClickListener({

                        if(homePageGeneralClass?.checkCurrentDateIsValid() == false)
                        {
                            homePageAlertClass?.commonAlert("Date error","Device date is not correct. Please set it to current date")
                            return@setOnClickListener
                        }

                        selectionType=0
                        views?.bottomSheetTitle_tv?.setText("Select Team")
                        if(isAdded)
                        {
                            activity.let {
                                if( CheckDcrClass(it,"callFragment").checkDCR_UsingSP())
                                {
                                    openCloseModel()
                                }
                            }
                        }
                    })

                    views?.selectRoutesCv?.setOnClickListener({
                        if(homePageGeneralClass?.checkCurrentDateIsValid() == false)
                        {
                            homePageAlertClass?.commonAlert("Date error","Device date is not correct. Please set it to current date")
                            return@setOnClickListener
                        }

                        selectionType=1
                        views?.bottomSheetTitle_tv?.setText("Select route")
                        if(!SplashActivity.staticSyncData?.settingDCR?.roleType.equals("MAN") ){

                            activity.let {
                                if( CheckDcrClass(it,"callFragment").checkDCR_UsingSP())
                                {
                                    openCloseModel()
                                }
                            }
                         /*   if(generalClassObject?.isInternetAvailable() == true)
                            {
                                callCoroutineApi()
                            }
                            else if(sharePreferance?.checkKeyExist("empIdSp")==false  ||
                                sharePreferance?.checkKeyExist("todayDate")==false  || sharePreferance?.checkKeyExist("dcrId")==false || sharePreferance?.getPref("empIdSp") != loginModelHomePage.empId.toString())
                            {
                                alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
                            }
                            else if( sharePreferance?.getPref("todayDate") != generalClassObject?.currentDateMMDDYY() || sharePreferance?.getPref("dcrId")=="0") {
                                alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
                            }
                            else{ openCloseModel() }*/
                        }
                        else{ openCloseModel() }
                    })

                    views?.selectDoctorsCv?.setOnClickListener({
                        removeDoneDcrFromList()
                        if(view?.docRetail_switch?.isChecked == true)
                        {
                            views?.bottomSheetTitle_tv?.setText("Select Doctor")
                            if(doctorListArray.size<=0)
                            {
                                HomePage.homePageAlertClass?.commonAlert("This route has no doctor","")
                                return@setOnClickListener
                            }
                        }
                        else{
                            views?.bottomSheetTitle_tv?.setText("Select Retailer")
                            if(retailerListArray.size<=0)
                            {
                                HomePage.homePageAlertClass?.commonAlert("This route has no Retailer","")
                                return@setOnClickListener
                            }
                        }

                        selectionType=2
                        openCloseModel()
                        adapter.notifyDataSetChanged()

                    })

                    views?.selectDoctorsCv?.setEnabled(false)

                    views?.close_selection_imv?.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

                    views?.doctorSearch_et?.addTextChangedListener(filterTextWatcher)
                }
                HomePage.homePageAlertClass?.hideAlert()
            }
        }
        callPreCallFrag()
    }

    fun getInstance(): NewCallFragment? {
        return instance
    }

 /*   fun callCoroutineApi() {
        homePageAlertClass?.hideAlert()
        homePageAlertClass?.showProgressAlert("")
        try{
            val coroutineScope = CoroutineScope(Dispatchers.IO + HomePage.homePageGeneralClass!!?.coroutineExceptionHandler).launch {
                val api = async { checkCurrentDCR_API() }
                api.await()
            }
            coroutineScope.invokeOnCompletion {
                activity?.runOnUiThread(java.lang.Runnable {
                    homePageAlertClass?.hideAlert()
                })
            }
        }
        catch (e:Exception)
        {
            homePageAlertClass?.hideAlert()
            homePageAlertClass?.lowNetworkAlert()
        }
    }*/

    fun openCloseModel()
    {
        if(!isAdded) return
        if(::bottomSheetBehavior.isInitialized==false)return

        views?.doctorSearch_et?.setText("")

        adapter =BottomSheetDoctorAdapter()
        views?.doctorList_rv?.setLayoutManager(GridLayoutManager(activity, 3))
        views?.doctorList_rv?.adapter = adapter
        adapter.notifyDataSetChanged()

        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state
    }

    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            adapter?.getFilter()?.filter(s.toString())
        }

        override fun afterTextChanged(s: Editable) {

            // TODO Auto-generated method stub
        }
    }

    inner class BottomSheetDoctorAdapter() :
        RecyclerView.Adapter<BottomSheetDoctorAdapter.MyViewHolder>(),
        Filterable {

        var filteredDataTeams:ArrayList<SyncModel.Data.FieldStaffTeam> = teamsList as ArrayList<SyncModel.Data.FieldStaffTeam>
        var filteredDataRoute:ArrayList<SyncModel.Data.Route> = routeList as ArrayList<SyncModel.Data.Route>
        var filteredDataDoctor:ArrayList<SyncModel.Data.Doctor> = doctorListArray as ArrayList<SyncModel.Data.Doctor>
        var filteredDataRetailer:ArrayList<SyncModel.Data.Retailer> = retailerListArray as ArrayList<SyncModel.Data.Retailer>


        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var headerDoctor_tv: TextView = view.findViewById(R.id.headerDoctor_tv)
            var route_tv: TextView = view.findViewById(R.id.route_tv)
            var speciality_tv: TextView = view.findViewById(R.id.speciality_tv)
            var parent_cv: CardView = view.findViewById(R.id.parent_cv)
        }
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.doctorlist_view, parent, false)
            return MyViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            if(selectionType==0)
            {   if(filteredDataTeams.size<=0)  views?.noData_tv?.visibility=View.VISIBLE
                else  views?.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataTeams?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.fullName)
                holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
                holder.speciality_tv.visibility=View.GONE

                holder.parent_cv.setOnClickListener({

                    views?.selectTeam_tv?.setText((modeldata?.fullName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    onSelection()
                    modeldata.fieldStaffId?.let { it1 -> applySelectionFilter(it1) }

                })
            }

            else if(selectionType==1)
            {
                if(filteredDataRoute.size<=0)  views?.noData_tv?.visibility=View.VISIBLE
                 else  views?.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataRoute?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.routeName)
                holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
                holder.speciality_tv.visibility=View.GONE

                holder.parent_cv.setOnClickListener({
                    doctorListArray.clear()
                    retailerListArray.clear()
                    views?.selectRoute_tv?.setText((modeldata?.routeName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    onSelection()
                    modeldata.routeId?.let { it1 -> applySelectionFilter(it1) }
                })
            }

            else{
                if(filteredDataDoctor.size<=0)  views?.noData_tv?.visibility=View.VISIBLE
                else  views?.noData_tv?.visibility=View.GONE

                if( views?.docRetail_switch?.isChecked==true)
                {
                    val modeldata = filteredDataDoctor?.get(position)
                    holder.headerDoctor_tv.setText(modeldata?.doctorName)
                    holder.route_tv.setText("Route- " + modeldata?.routeName)
                    holder.speciality_tv.setText("Speciality- " + modeldata?.specialityName)

                    holder.parent_cv.setOnClickListener({

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                        views?.selectDoctor_tv?.setText((modeldata?.doctorName))
                        onSelection()
                        Handler(Looper.getMainLooper()).postDelayed({
                            activity?.runOnUiThread {
                                setDoctor(modeldata)
                            }
                        },400)
                    })
                }
                else{
                    val modeldata = filteredDataRetailer?.get(position)
                    holder.headerDoctor_tv.setText(modeldata?.shopName)
                    holder.route_tv.setText("Route- " + modeldata?.routeName)
                    holder.speciality_tv.setText("Headquater- " + modeldata?.headQuaterName)

                    holder.parent_cv.setOnClickListener({
                        homePageAlertClass?.showProgressAlert("")
                        setRetailer(modeldata)
                    })
                }
            }
        }
        override fun getItemCount(): Int
        {
            if(selectionType==0)
                return filteredDataTeams?.size
            else if(selectionType==1)
                return filteredDataRoute?.size
            else
                if(view?.docRetail_switch?.isChecked == true) return filteredDataDoctor?.size
                else return filteredDataRetailer.size
        }

         //-------------------------------------filter list using text input from edit text
        override fun getFilter(): Filter? {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    if(selectionType==0)
                    { filteredDataTeams = results.values as ArrayList<SyncModel.Data.FieldStaffTeam> }
                    else if(selectionType==1)
                    { filteredDataRoute = results.values as ArrayList<SyncModel.Data.Route> }
                    else
                    {
                        if(views?.docRetail_switch?.isChecked == true)
                        {
                            filteredDataDoctor = results.values as ArrayList<SyncModel.Data.Doctor>
                        }
                        else
                        {
                            filteredDataRetailer= results.values as ArrayList<SyncModel.Data.Retailer>
                        }
                       }
                    notifyDataSetChanged()
                }
                override fun performFiltering(constraint: CharSequence): FilterResults? {
                    var constraint = constraint
                    val results = FilterResults()
                    if(selectionType==0)
                    {
                        val FilteredArrayNames: ArrayList<SyncModel.Data.FieldStaffTeam> = ArrayList()
                        constraint = constraint.toString().lowercase()
                        for (i in 0 until teamsList?.size!!) {
                            val dataNames: SyncModel.Data.FieldStaffTeam = teamsList?.get(i)
                            if (dataNames.fullName?.lowercase()?.contains(constraint.toString()) == true) {
                                FilteredArrayNames.add(dataNames)
                            }
                        }
                        results.count = FilteredArrayNames.size
                        results.values = FilteredArrayNames
                        return results
                    }
                    else if(selectionType==1)
                    { val FilteredArrayNames: ArrayList<SyncModel.Data.Route> = ArrayList()
                        constraint = constraint.toString().lowercase()
                        for (i in 0 until routeList?.size!!) {
                            val routeName: SyncModel.Data.Route = routeList?.get(i)
                            if (routeName.routeName?.lowercase()?.contains(constraint.toString()) == true) {
                                FilteredArrayNames.add(routeName)
                            }
                        }
                        results.count = FilteredArrayNames.size
                        results.values = FilteredArrayNames
                        return results }
                    else {
                        constraint = constraint.toString().lowercase()

                        if (views?.docRetail_switch?.isChecked == true)
                        {
                            val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()

                            for (i in 0 until doctorListArray?.size!!) {
                                val docNames: SyncModel.Data.Doctor = doctorListArray?.get(i)
                                if (docNames.doctorName?.lowercase()?.contains(constraint.toString()) == true) {
                                    FilteredArrayNames.add(docNames)
                                }
                            }
                            results.count = FilteredArrayNames.size
                            results.values = FilteredArrayNames
                            return results
                        }
                        else
                        {
                            val FilteredArrayNames: ArrayList<SyncModel.Data.Retailer> = ArrayList()
                            for (i in 0 until retailerListArray?.size!!) {
                                val docNames: SyncModel.Data.Retailer = retailerListArray?.get(i)
                                if (docNames.shopName?.lowercase()?.contains(constraint.toString()) == true) {
                                    FilteredArrayNames.add(docNames) } }
                            results.count = FilteredArrayNames.size
                            results.values = FilteredArrayNames
                            return results
                        }

                    }
                }
            }
        }
    }

    fun removeDoneDcrFromList()
    {
        Log.e("doctorListOne",doctorListArray.size.toString())
        val responseDocCall= HomePage.homePageDataBase?.getApiDetail(5)
        if(!responseDocCall.equals("")) {

            docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)

            val doctTempList: ArrayList<SyncModel.Data.Doctor> = ArrayList()
            doctTempList.addAll(doctorListArray)

            for ((index,data) in doctTempList.withIndex())
            {
                for (item in docCallModel.dcrDoctorlist!!)
                {
                    if(item.doctorId==data.doctorId) {
                        doctorListArray.remove(data)
                    }
                }
            }
            val rectTempList: ArrayList<SyncModel.Data.Retailer> = ArrayList()
            rectTempList.addAll(retailerListArray)
            for ((index,data) in rectTempList.withIndex())
            {
                val isAlreadyContain=docCallModel.dcrRetailerlist?.any{ s -> s.retailerId == data.retailerId }
                if(isAlreadyContain == true) {
                    retailerListArray.remove(data)
                }
            }
        }
        val doctTempList: ArrayList<SyncModel.Data.Doctor> = ArrayList()
        val rectTempList: ArrayList<SyncModel.Data.Retailer> = ArrayList()
        doctTempList.addAll(doctorListArray)
        rectTempList.addAll(retailerListArray)
        Log.e("doctorListTwo",doctorListArray.size.toString())
        val dcrRetailerList=HomePage.homePageDataBase?.getAllSaveSendRetailer("retailerFeedback")
        val eDetailingArray=HomePage.homePageDataBase?.getAllSaveSend("feedback")
        for ((index,data) in doctTempList.withIndex())
        {
            val isAlreadyContain=eDetailingArray?.any{ s -> s.doctorId == data.doctorId }
            if(isAlreadyContain==true) {
                doctorListArray.remove(data)
            }
        }
        for ((index,data) in rectTempList.withIndex())
        {
            val isAlreadyContain=dcrRetailerList?.any{ s -> s.retailerId == data.retailerId }
            if(isAlreadyContain == true) {
                retailerListArray.remove(data)
            }
        }
        Log.e("doctorListThree",doctorListArray.size.toString())

    }

    fun setRetailer(retailerModel:SyncModel.Data.Retailer)
    {
        val isAlreadyContain=docCallModel.dcrRetailerlist?.any{ s -> s.retailerId == retailerModel.retailerId }
        if(isAlreadyContain == true) {
            views?.selectDoctor_tv?.setText("Select Retailer")
            homePageAlertClass?.commonAlert("Alert!","Dcr already done for this retailer")
            homePageAlertClass?.hideAlert()
            views?.noData_gif?.visibility=View.VISIBLE
            views?.retailer_parent?.visibility=View.GONE
            views?.frameRetailer_view?.visibility=View.GONE
            views?.framePreCall_view?.visibility=View.GONE
            views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
            return
        }

        onSelection()

        val coroutine= CoroutineScope(Dispatchers.IO).launch {
            val task= async {
                val transaction = childFragmentManager.beginTransaction()
               transaction?.replace(R.id.frameRetailer_view, RetailerFillFragment(this@NewCallFragment))
               transaction?.disallowAddToBackStack()
               transaction?.commitAllowingStateLoss()
            }
            task.await()
        }
        coroutine.invokeOnCompletion {
            coroutine.cancel()
            activity?.runOnUiThread {
                retailerObj=retailerModel
                views?.selectDoctor_tv?.setText((retailerModel?.shopName))
                views?.noData_gif?.visibility=View.GONE
                views?.retailer_parent?.visibility=View.VISIBLE
                views?.routeNameRetailer_tv?.setText(retailerModel.routeName)
                views?.mobileRetailer_tv?.setText(retailerModel.contactPerson)
                views?.emailIdRetail_tv?.setText(retailerModel.emailId)
                views?.cityRetail_tv?.setText(retailerModel.cityName)
                views?.shopNameRetail_tv?.setText(retailerModel.shopName)
                if(retailerModel.mobileNo?.isEmpty() == true)
                    views?.mobileParent_tr?.visibility=View.GONE
                if(retailerModel.emailId?.isEmpty() == true)
                    views?.emailParentRetail?.visibility=View.GONE
                if(retailerModel.cityName?.isEmpty() == true)
                    views?.cityParentRetailer?.visibility=View.GONE

                views?.frameRetailer_view?.visibility=View.VISIBLE
                views?.framePreCall_view?.visibility=View.GONE
                Handler(Looper.getMainLooper()).postDelayed({
                    homePageAlertClass?.hideAlert()
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)}, 2)
            }
        }
    }


    fun setDoctor(doctorDetailModel: SyncModel.Data.Doctor)
    {
        views?.doctorDetail_parent?.visibility=View.VISIBLE
        views?.doctorName_tv?.setText(doctorDetailModel.doctorName)
        views?.routeName_tv?.setText(doctorDetailModel.routeName)
        views?.mobileNumber_tv?.setText(doctorDetailModel.mobileNo)
        views?.emailId_tv?.setText(doctorDetailModel.emailId)
        views?.city_tv?.setText(doctorDetailModel.cityName)
        views?.specility_tv?.setText(doctorDetailModel.specialityName)
        views?.qualifiction_tv?.setText(doctorDetailModel.qualificationName)
        doctorObject=doctorDetailModel;

        if(doctorDetailModel.mobileNo?.isEmpty() == true)
            views?.mobileNumberParent?.visibility=View.GONE
        if(doctorDetailModel.emailId?.isEmpty() == true)
            views?.emailParent?.visibility=View.GONE
        if(doctorDetailModel.cityName?.isEmpty() == true)
            views?.cityParent?.visibility=View.GONE

        views?.frameRetailer_view?.visibility=View.GONE
        views?.framePreCall_view?.visibility=View.VISIBLE
        views?.noInternet_tv?.visibility = View.GONE
        views?.noData_gif?.visibility = View.GONE

        preCallObj.setViewPreCall(doctorDetailModel)
    }

    fun onSelection()
    {
    views?.doctorSearch_et?.setText("")

     if(selectionType==0)
     {   views?.doctorDetail_parent?.visibility=View.GONE
       // views?.precall_parent?.visibility=View.GONE
       // views?.parentButton?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views?.selectTeam_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
         views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
         views?.selectRoute_tv?.setText("Select route")
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
         views?.selectDoctor_tv?.setText("Select Doctor")
         views?.selectRoutesCv?.setEnabled(true)
         views?.selectDoctorsCv?.setEnabled(false)
         views?.frameRetailer_view?.visibility=View.GONE
         views?.framePreCall_view?.visibility=View.GONE
         views?.retailer_parent?.visibility=View.GONE
     }
     else if(selectionType==1)
     {
         views?.doctorDetail_parent?.visibility=View.GONE
       //  views?.precall_parent?.visibility=View.GONE
       //  views?.parentButton?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
         if(views?.docRetail_switch?.isChecked==true) views?.selectDoctor_tv?.setText("Select Doctor")
             else  views?.selectDoctor_tv?.setText("Select Retailer")

         views?.selectDoctorsCv?.setEnabled(true)
         views?.frameRetailer_view?.visibility=View.GONE
         views?.framePreCall_view?.visibility=View.GONE
         views?.retailer_parent?.visibility=View.GONE
     }
     else
     {
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
     }
    }

    fun applySelectionFilter(id:Int)
    {
        if(selectionType==0)
        {
            if(staticSyncData?.settingDCR?.isRestrictedParty==false)
            {
                routeList.clear()
                val filterRouteUsingFieldStaff= SplashActivity.staticSyncData?.routeList?.filter {  s -> s.fieldStaffId == id }
                filterRouteUsingFieldStaff?.let { routeList.addAll(it) }
            }
            else
            {
                val filterRouteUsingFieldStaff= ristrictedRouteList?.filter {  s -> s.fieldStaffId == id }
                routeList.clear()
                filterRouteUsingFieldStaff?.let { routeList.addAll(it) }
            }
        }
        else if(selectionType==1)
        {
            if(staticSyncData?.settingDCR?.isDoctorFencingRequired == true && views?.docRetail_switch?.isChecked==true)
            {
                val getGpsTracker=GPSTracker(activity)
                val jsonObj=JSONObject(staticSyncData?.configurationSetting)
                val getRadius=jsonObj.getInt("SET011")

                val startPoint = Location("locationA")
                startPoint.setLatitude(getGpsTracker.latitude)
                startPoint.setLongitude(getGpsTracker.longitude)

                val docFirstFilter= SplashActivity.staticSyncData?.doctorList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>

                    for(fetch in docFirstFilter)
                    {
                        if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }
                        val endPoint = Location("locationB")
                        endPoint.latitude = fetch.latitude
                        endPoint.longitude = fetch.longitude
                        val distance = startPoint.distanceTo(endPoint).toInt()
                        if(distance <= getRadius){
                           doctorListArray.add(fetch)
                        }
                    }
            }
            else if(staticSyncData?.settingDCR?.isRetailerFencingRequired == true && views?.docRetail_switch?.isChecked==false){
                val retailFirstFilter= SplashActivity.staticSyncData?.retailerList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Retailer>

                val getGpsTracker=GPSTracker(activity)
                val jsonObj=JSONObject(staticSyncData?.configurationSetting)
                val getRadius=jsonObj.getInt("SET011")

                val startPoint = Location("locationA")
                startPoint.setLatitude(getGpsTracker.latitude)
                startPoint.setLongitude(getGpsTracker.longitude)
                for(fetch in retailFirstFilter)
                {
                    if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }
                    val endPoint = Location("locationB")
                    endPoint.latitude = fetch.latitude
                    endPoint.longitude = fetch.longitude
                    val distance = startPoint.distanceTo(endPoint).toInt()
                    if(distance <= getRadius){
                        retailerListArray.add(fetch)
                    }
                }
            }
            else
            {
                if(views?.docRetail_switch?.isChecked==true)
                {
                    doctorListArray = SplashActivity.staticSyncData?.doctorList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>
                }
                else
                {
                    retailerListArray = SplashActivity.staticSyncData?.retailerList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Retailer>
                }

            }

        }

    }

    /*suspend fun checkCurrentDCR_API() {

        val response = APIClientKot().getUsersService(2, HomePage.homePageSharePref?.getPref("secondaryUrl")!!).checkDCR_API(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId,
            HomePage.homePageGeneralClass?.currentDateMMDDYY()
        )
        withContext(Dispatchers.Main) {


            if (response!!.isSuccessful) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    Log.e("dcrObject",Gson().toJson(response.body()))
                    if (response.body()?.errorObj?.errorMessage?.isEmpty() == true) {
                        val dcrData=response.body()?.data?.dcrData

                        if(staticSyncData?.settingDCR?.isCallPlanMandatoryForDCR==true && response.body()?.data?.isCPExiest == false)
                        {
                            homePageAlertClass?.commonAlert("Alert!","Please submit you day plan first")
                            return@withContext
                        }

                        if (dcrData?.rtpApproveStatus?.lowercase() != "a") {
                            homePageAlertClass?.commonAlert("Alert!","Tour plan not approved")
                            return@withContext
                        }

                        if (dcrData?.dataSaveType?.lowercase() == "s") {
                            homePageAlertClass?.commonAlert("Alert!","The DCR is submitted it cannot be unlocked please connect with your admin")
                            return@withContext
                        }

                        if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {
                            homePageAlertClass?.commonAlert("Alert!", "Please submit tour program first")
                            homePageAlertClass?.hideAlert()
                            return@withContext
                        }

                        routeIdGetDCR = dcrData?.routeId.toString()
                        dcrData?.dataSaveType="D"
                        HomePage.homePageSharePref?.setPref("dcrObj", Gson().toJson(dcrData))

                        if (dcrData?.dcrId == 0) {
                           // createDCRAlert(dcrData?.routeId.toString())
                            homePageAlertClass?.createDCRAlert(dcrData?.routeId.toString(),dcrData?.routeName.toString())
                            HomePage.homePageSharePref?.setPref("dcrId", dcrData?.dcrId.toString())
                        } else {
                            homePageSharePref?.setPref("todayDate", HomePage.homePageGeneralClass?.currentDateMMDDYY())
                            homePageSharePref?.setPref("dcrId", dcrData?.dcrId.toString())
                            homePageSharePref?.setPref("empIdSp", loginModelHomePage.empId.toString())

                            views?.bottomSheetTitle_tv?.setText(if(selectionType==1)"Select route" else "Select team")

                            if(dcrData?.otherDCR!=0)
                            {
                                setOtherActivityView()
                                homePageSharePref?.setPref("otherActivitySelected","1")
                                return@withContext
                            }
                            openCloseModel()
                            }
                    } else {
                        activity?.let { GeneralClass(it).checkInternet() }
                    }
                }
            }
        }

       *//* var returnType=false;
        alertClass?.showProgressAlert("")

        var call: Call<JsonObject> = apiInterface?.checkDCR_API("bearer " + loginModelHomePage.accessToken, loginModelHomePage.empId,generalClassObject!!.currentDateMMDDYY()) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                  val jsonObjError:JsonObject = response.body()?.get("errorObj") as JsonObject
                  if(jsonObjError.get("errorMessage").asString.isEmpty())
                  {
                      val data:JsonObject = response.body()?.get("data") as JsonObject
                      val dcrData:JsonObject = data?.get("dcrData") as JsonObject
                     if(dcrData.get("routeId").asInt==0)
                     {
                         alertClass?.commonAlert("Alert!","Please submit tour program first")
                         alertClass?.hideAlert()
                         returnType=false
                         return
                     }

                      if( dcrData.get("strDCRDate").asString == generalClassObject?.getCurrentDate() )
                      {
                          returnType=true; return }

                    routeIdGetDCR=dcrData.get("routeId").asString
                      if(dcrData.get("dcrId").asInt==0) {
                          returnType=false
                          createDCRAlert(dcrData.get("routeId").asString)
                          sharePreferance?.setPref("dcrId", dcrData.get("dcrId").asString) }
                      else
                      sharePreferance?.setPref("todayDate",generalClassObject?.currentDateMMDDYY())
                      sharePreferance?.setPref("dcrId",dcrData.get("dcrId").asString)
                      returnType=true
                  }
                }
                alertClass?.hideAlert()
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                GeneralClass(requireActivity()).checkInternet()
                alertClass?.hideAlert()// check internet connection
                call.cancel()
                returnType=false
            }
        })*//*
    }*/

    fun setOtherActivityView()
    {
        homePageAlertClass?.commonAlert("Alert!","You have not planned field working today. Kindly save it from Salestrip app")
        views?.selectRoutesCv?.setEnabled(false)
        views?.selectTeamsCv?.setEnabled(false)
        views?.selectTeam_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
        views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
    }

    override fun onResume() {
        super.onResume()
        if(::bottomSheetBehavior.isInitialized==false && views!=null) bottomSheetBehavior = BottomSheetBehavior.from(views!!.bottomSheet)

        if(isSecondTime && docRetail_switch.isChecked) {
            if(views==null) return
            views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
            views?.doctorDetail_parent?.visibility=View.GONE
          // views?.precall_parent?.visibility=View.GONE
          // views?.parentButton?.visibility=View.GONE
            views?.noData_gif?.visibility=View.VISIBLE
            views?.framePreCall_view?.visibility=View.GONE
            /*val responseDocCall=db.getApiDetail(5)
            if(!responseDocCall.equals("")) {
                docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
            }*/
        }
        else isSecondTime=true

        if(views?.frameRetailer_view?.visibility==View.VISIBLE)
        {
            views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
            views?.frameRetailer_view?.visibility=View.GONE
            views?.framePreCall_view?.visibility=View.GONE
            views?.noData_gif?.visibility=View.VISIBLE
            views?.retailer_parent?.visibility=View.VISIBLE
        }

    }

    fun isRetialerEdit()
    {
        val strtext = arguments?.getString("retailerData")
        view?.docRetail_switch?.isChecked=false
        views?.noData_gif?.visibility=View.GONE
        views?.noInternet_tv?.visibility=View.VISIBLE
        views?.noInternet_tv?.setText("Please wait..")
        views?.selectionDocRet_tv?.setText("Select Retailer")
        views?.selectRoutesCv?.setEnabled(false)
        views?.selectTeamsCv?.visibility=View.GONE
        views?.selectTeamHeader_tv?.visibility = View.GONE
        views?.selectDoctorsCv?.setEnabled(false)
        view?.docRetail_switch?.isChecked=false
        views?.docRetail_switch?.setEnabled(false)

        views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
        views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
        views?.retailerHeader_tv?.setTextColor(activity?.let { ContextCompat.getColorStateList(it, R.color.darkBlue) })
        views?.doctorHeader_tv?.setTextColor(activity?.let { ContextCompat.getColorStateList(it, R.color.gray) })

        Handler(Looper.getMainLooper()).postDelayed({
            var  retailerModel= Gson().fromJson(strtext, DailyDocVisitModel.Data.DcrDoctor::class.java)

            val retailerFragment=RetailerFillFragment(this@NewCallFragment)
            val bundle = Bundle()
            bundle.putString("retailerData", strtext)
            retailerFragment.arguments=bundle

            val transaction = childFragmentManager.beginTransaction()
            transaction?.replace(R.id.frameRetailer_view, retailerFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commitAllowingStateLoss()

            Handler(Looper.getMainLooper()).postDelayed({
                activity?.runOnUiThread {
                    views?.selectRoute_tv?.text=retailerModel.routeName
                    views?.selectDoctor_tv?.text=retailerModel.shopName
                    views?.frameRetailer_view?.visibility=View.VISIBLE
                    views?.noInternet_tv?.visibility=View.GONE

                }
            },10)
        },10)


    }

    override fun onSaveInstanceState(outState: Bundle) {}

    override fun onClickString(passingInterface: String?) {
        views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
        views?.frameRetailer_view?.visibility=View.GONE
        views?.framePreCall_view?.visibility=View.GONE
        views?.noData_gif?.visibility=View.VISIBLE
        views?.retailer_parent?.visibility=View.GONE
    }

    fun callPreCallFrag()
    {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.framePreCall_view, preCallObj)
        transaction?.disallowAddToBackStack()
        transaction?.commitAllowingStateLoss()
    }

}