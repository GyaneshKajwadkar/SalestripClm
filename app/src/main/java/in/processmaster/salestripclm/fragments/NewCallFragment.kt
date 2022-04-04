package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.OnlinePresentationActivity
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.adapter.LastRCPA_Adapter
import `in`.processmaster.salestripclm.adapter.SimpleListAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.PreCallModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.GPSTracker
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import us.zoom.sdk.ZoomSDK
import java.util.*


class NewCallFragment : Fragment() {
    lateinit var db : DatabaseHandler
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var views:View?=null
    private lateinit var adapter:BottomSheetDoctorAdapter
    var doctorListArray:ArrayList<SyncModel.Data.Doctor> = ArrayList()
    var routeList: ArrayList<SyncModel.Data.Route> = ArrayList()
    var teamsList: ArrayList<SyncModel.Data.FieldStaffTeam> = ArrayList()
    var selectionType=0
    var selectedDocID=0
    var selectedDocName=""
    var sharePreferance: PreferenceClass? = null
    var generalClassObject:GeneralClass?=null
    var routeIdGetDCR=""
    var alertClass:AlertClass?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_new_call, container, false)

        if (activity != null && isAdded)
        { initView() }

        return views
    }

    fun initView()
    {

        db = DatabaseHandler(requireActivity())
        sharePreferance = PreferenceClass(activity)
        alertClass = AlertClass(requireActivity())

        SplashActivity.staticSyncData?.doctorList?.let { doctorListArray.addAll(it) }
        SplashActivity.staticSyncData?.routeList?.let { routeList.addAll(it) }

        bottomSheetBehavior = BottomSheetBehavior.from(views!!.bottomSheet)
        generalClassObject= GeneralClass(requireActivity())

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
    /*    else
            views?.selectRoutesCv?.setEnabled(false)*/
        //  views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))

        views?.docRetail_switch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(requireActivity(), R.color.darkBlue))
                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(requireActivity(), R.color.gray))
            } else {
                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(requireActivity(), R.color.darkBlue))
                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(requireActivity(), R.color.gray))

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

            views?.doctorDetail_parent?.visibility=View.GONE
            views?.precall_parent?.visibility=View.GONE
            views?.parentButton?.visibility=View.GONE
            views?.noData_gif?.visibility=View.VISIBLE

            views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
            views?.selectRoute_tv?.setText("Select route")
            views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
            views?.selectDoctor_tv?.setText("Select Doctor")
            views?.selectTeam_tv?.setText("Select team")
            views?.selectDoctorsCv?.setEnabled(false)
        }


        views?.selectTeamsCv?.setOnClickListener({
            views?.bottomSheetTitle_tv?.setText("Select Team")
            selectionType=0
            openCloseModel() })

        views?.selectRoutesCv?.setOnClickListener({


            if(checkDCRusingShareP(1)){
                views?.bottomSheetTitle_tv?.setText("Select route")
                selectionType=1
                openCloseModel()
            }
        })

        views?.selectDoctorsCv?.setOnClickListener({
            views?.bottomSheetTitle_tv?.setText("Select Doctor")
            selectionType=2
            if(doctorListArray.size<=0)
            {
                alertClass?.commonAlert("This route has no doctor","")
                return@setOnClickListener
            }
            openCloseModel()})

        views?.selectDoctorsCv?.setEnabled(false)

        adapter =BottomSheetDoctorAdapter()

        views?.close_imv?.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})
        views?.doctorSearch_et?.addTextChangedListener(filterTextWatcher)

        views?.startDetailing_btn?.setOnClickListener({

            if(views?.lastVisitDate_tv?.text?.equals(generalClassObject?.getCurrentDate()) == true && db.isEDetailingAvailable(selectedDocID, generalClassObject?.getCurrentDate()
                ))
            {
                alertClass?.commonAlert("Alert!","Doctor e-detailing already done for today")
                return@setOnClickListener
            }

            val intent = Intent(activity, OnlinePresentationActivity::class.java)
            intent.putExtra("doctorID", selectedDocID)
            intent.putExtra("doctorName", selectedDocName)
            intent.putExtra("skip", false)
            startActivityForResult(intent,3)
        })

        views?.skipDetailing_btn?.setOnClickListener({
            val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
            intent.putExtra("doctorID", selectedDocID)
            intent.putExtra("doctorName", selectedDocName)
            intent.putExtra("skip", true)
            startActivityForResult(intent,3)
        })

        staticSyncData?.fieldStaffTeamList?.let { teamsList.addAll(it) }
        routeList = routeList?.filter { s -> s.headQuaterName !=""} as java.util.ArrayList<SyncModel.Data.Route>
      //  checkDCRusingShareP(0)
    }

    fun checkDCRusingShareP(i: Int):Boolean
    {
        if(generalClassObject?.isInternetAvailable() == true)
        {
            callCoroutineApi(i)
            return false
        }

        else if(sharePreferance?.checkKeyExist("empIdSp")==false  ||
            sharePreferance?.checkKeyExist("todayDate")==false  || sharePreferance?.checkKeyExist("dcrId")==false || sharePreferance?.getPref("empIdSp") != loginModelHomePage.empId.toString())
        {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
            return false
        }
        else if( sharePreferance?.getPref("todayDate") != generalClassObject?.currentDateMMDDYY() || sharePreferance?.getPref("dcrId")=="0") {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
        return false
        }
        else{ return true }
    }

    fun callCoroutineApi(i: Int) {
        alertClass?.showProgressAlert("")
        val coroutineScope = CoroutineScope(Dispatchers.IO).launch {
            val api = async { checkCurrentDCR_API(i) }
            api.await()
        }

        coroutineScope.invokeOnCompletion {
            requireActivity().runOnUiThread(java.lang.Runnable {
                alertClass?.hideAlert()
            })
        }
    }

    fun openCloseModel()
    {
        adapter =BottomSheetDoctorAdapter()
        views?.doctorList_rv?.setLayoutManager(GridLayoutManager(requireActivity(), 3))
        views?.doctorList_rv?.adapter = adapter

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
            {    if(filteredDataRoute.size<=0)  views?.noData_tv?.visibility=View.VISIBLE
                 else  views?.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataRoute?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.routeName)
                holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
                holder.speciality_tv.visibility=View.GONE

                holder.parent_cv.setOnClickListener({
                    doctorListArray.clear()
                    views?.selectRoute_tv?.setText((modeldata?.routeName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    onSelection()
                    modeldata.routeId?.let { it1 -> applySelectionFilter(it1) }

                })
            }

            else{
                if(filteredDataDoctor.size<=0)  views?.noData_tv?.visibility=View.VISIBLE
                else  views?.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataDoctor?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.doctorName)
                holder.route_tv.setText("Route- " + modeldata?.routeName)
                holder.speciality_tv.setText("Speciality- " + modeldata?.specialityName)

                holder.parent_cv.setOnClickListener({
                    views?.selectDoctor_tv?.setText((modeldata?.doctorName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    setDoctor(modeldata)
                    onSelection()
                })
            }

        }
        override fun getItemCount(): Int
        {
            if(selectionType==0)
                return filteredDataTeams?.size
            else if(selectionType==1)
                return filteredDataRoute?.size
            else
                return filteredDataDoctor?.size
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
                    { filteredDataDoctor = results.values as ArrayList<SyncModel.Data.Doctor> }
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
                            if (dataNames.fullName?.lowercase()?.startsWith(constraint.toString()) == true) {
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
                            if (routeName.routeName?.lowercase()?.startsWith(constraint.toString()) == true) {
                                FilteredArrayNames.add(routeName)
                            }
                        }
                        results.count = FilteredArrayNames.size
                        results.values = FilteredArrayNames
                        return results }
                    else
                    {   val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()
                        constraint = constraint.toString().lowercase()
                        for (i in 0 until doctorListArray?.size!!) {
                            val docNames: SyncModel.Data.Doctor = doctorListArray?.get(i)
                            if (docNames.doctorName?.lowercase()?.startsWith(constraint.toString()) == true) {
                                FilteredArrayNames.add(docNames)
                            }
                        }
                        results.count = FilteredArrayNames.size
                        results.values = FilteredArrayNames
                        return results}
                }
            }
        }
    }

    fun setDoctor(doctorDetailModel: SyncModel.Data.Doctor)
    {
        views?.precall_parent?.visibility=View.GONE

        views?.doctorDetail_parent?.visibility=View.VISIBLE
        views?.doctorName_tv?.setText(doctorDetailModel.doctorName)
        views?.routeName_tv?.setText(doctorDetailModel.routeName)
        views?.mobileNumber_tv?.setText(doctorDetailModel.mobileNo)
        views?.emailId_tv?.setText(doctorDetailModel.emailId)
        views?.city_tv?.setText(doctorDetailModel.cityName)
        views?.specility_tv?.setText(doctorDetailModel.specialityName)
        views?.qualifiction_tv?.setText(doctorDetailModel.qualificationName)
        selectedDocID= doctorDetailModel.doctorId!!
        selectedDocName= doctorDetailModel.doctorName.toString()

        if(doctorDetailModel.mobileNo?.isEmpty() == true)
            views?.mobileNumberParent?.visibility=View.GONE
        if(doctorDetailModel.emailId?.isEmpty() == true)
            views?.emailParent?.visibility=View.GONE
        if(doctorDetailModel.cityName?.isEmpty() == true)
            views?.cityParent?.visibility=View.GONE

        if(generalClassObject?.isInternetAvailable() == true) {
            noInternet_tv.visibility = View.GONE
            preCallAnalysisApi()
        }
        else noInternet_tv.visibility=View.VISIBLE; views?.parentButton?.visibility=View.VISIBLE


    }

    fun onSelection()
    {
    views?.doctorSearch_et?.setText("")

     if(selectionType==0)
     {   views?.doctorDetail_parent?.visibility=View.GONE
         views?.precall_parent?.visibility=View.GONE
         views?.parentButton?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views?.selectTeam_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
         views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
         views?.selectRoute_tv?.setText("Select route")
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#A9A9A9"))
         views?.selectDoctor_tv?.setText("Select Doctor")
         views?.selectRoutesCv?.setEnabled(true)
         views?.selectDoctorsCv?.setEnabled(false)
     }
     else if(selectionType==1)
     {
         views?.doctorDetail_parent?.visibility=View.GONE
         views?.precall_parent?.visibility=View.GONE
         views?.parentButton?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
         views?.selectDoctor_tv?.setText("Select Doctor")
         views?.selectDoctorsCv?.setEnabled(true)
     }
     else
     {
         views?.selectDoctor_tv?.setBackgroundColor(Color.parseColor("#3CB371"))
     }
    }

    fun applySelectionFilter(id:Int)
    {
        if(selectionType==0)
        {    SplashActivity.staticSyncData?.routeList?.let { routeList.addAll(it) }
            val filterRouteUsingFieldStaff= routeList.filter {  s -> s.fieldStaffId == id }
            routeList.clear()
            routeList.addAll(filterRouteUsingFieldStaff)

        }
        else if(selectionType==1)
        {
            if(staticSyncData?.settingDCR?.isGeoLocationRequired == true)
            {
                val getGpsTracker=GPSTracker(requireActivity())
                val jsonObj=JSONObject(staticSyncData?.configurationSetting)
                val getRadius=jsonObj.getInt("SET011")

                val startPoint = Location("locationA")
               startPoint.setLatitude(getGpsTracker.latitude)
               // startPoint.setLatitude(22.724177793056885)
                startPoint.setLongitude(getGpsTracker.longitude)
               // startPoint.setLongitude(75.90522484470453)

                val docFirstFilter= SplashActivity.staticSyncData?.doctorList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>


            for(fetch in docFirstFilter)
                {

                   if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }

                    val endPoint = Location("locationB")
                    endPoint.latitude = fetch.latitude
                    endPoint.longitude = fetch.longitude
                    val distance = startPoint.distanceTo(endPoint).toInt()
                    doctorListArray.add(fetch)
                    if(distance <= getRadius){
                       // doctorListArray.add(fetch)
                    }
                }
            }
            else
            {
                doctorListArray = SplashActivity.staticSyncData?.doctorList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>
            }
        }
    }

    private fun preCallAnalysisApi() {

        views?.noData_gif?.visibility=View.GONE
        views?.analysisProgress?.visibility=View.VISIBLE

        var profileData = sharePreferance?.getPref("profileData")           //get profile data from share preferance
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)

        //call submit visual ads api interfae post method
        var call: Call<PreCallModel> = apiInterface?.priCallAnalysisApi("bearer " + loginModel?.accessToken,selectedDocID) as Call<PreCallModel>
        call.enqueue(object : Callback<PreCallModel?> {
            override fun onResponse(call: Call<PreCallModel?>?, response: Response<PreCallModel?>) {
                Log.e("preCallAnalysisApi", response.code().toString() + "")
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    val analysisModel=response.body()?.getData()?.lastVisitSummary
                    views?.precall_parent?.visibility=View.VISIBLE
                    views?.parentButton?.visibility=View.VISIBLE

                    //remark_ll
                    if(analysisModel?.visitPurpose?.let {
                            generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.visitPurpose_tr?.visibility=View.GONE }
                    else{ views?.visitPurpose_tv?.setText(analysisModel?.visitPurpose) }

                    if(analysisModel?.workWithName?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.workingWith_tr?.visibility=View.GONE }
                    else{ views?.workingWith_tv?.setText(analysisModel?.workWithName) }

                    if(analysisModel?.strDcrDate?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.lastVisit_tr?.visibility=View.GONE }
                    else{ views?.lastVisitDate_tv?.setText(analysisModel?.strDcrDate) }

                    if(analysisModel?.remarks?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.remark_ll?.visibility=View.GONE }
                    else{ views?.remark_tv?.setText(analysisModel?.remarks) }

                    if(analysisModel?.strReportedTime?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.reportedTime_tr?.visibility=View.GONE }
                    else{ views?.reportedTime_tv?.setText(analysisModel?.strReportedTime) }


                    views?.brandList_rv?.layoutManager=LinearLayoutManager(requireActivity())
                    views?.sampleGiven_rv?.layoutManager=LinearLayoutManager(requireActivity())
                    views?.giftGiven_rv?.layoutManager=LinearLayoutManager(requireActivity())

                    var mainList= ArrayList<String>()
                    var subList= ArrayList<Int>()

                    if( analysisModel?.productList!=null && analysisModel?.productList?.size!=0)
                    {
                        var i: Int = analysisModel?.productList?.size?.minus(1)!!
                        for( data in analysisModel?.productList!!)
                        {
                            data.productName?.let { mainList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)view?.noDataBrand?.visibility=View.VISIBLE
                                else view?.noDataBrand?.visibility=View.GONE
                            }
                        }
                        var adapterProduct=SimpleListAdapter(mainList,subList)
                        views?.brandList_rv?.adapter=adapterProduct
                    }
                    else
                    {
                        view?.noDataBrand?.visibility = View.VISIBLE
                        views?.brandList_rv?.adapter=SimpleListAdapter(mainList,subList)
                    }

                    mainList=ArrayList<String>()
                    subList= ArrayList<Int>()

                    if( analysisModel?.sampleList!=null && analysisModel?.sampleList?.size!=0)
                    {
                        var i: Int = analysisModel?.sampleList?.size?.minus(1)!!
                        for( data in analysisModel?.sampleList!!) {
                            mainList.add(data.productName.toString())
                            data.qty?.let { subList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)view?.noDataSample?.visibility=View.VISIBLE
                                else view?.noDataSample?.visibility=View.GONE
                            }
                        }

                        var adapterSampleGiven=SimpleListAdapter(mainList,subList)
                        views?.sampleGiven_rv?.adapter=adapterSampleGiven

                    }
                    else
                    {
                        view?.noDataSample?.visibility = View.VISIBLE
                        views?.sampleGiven_rv?.adapter=SimpleListAdapter(mainList,subList)
                    }

                    mainList=ArrayList<String>()
                    subList= ArrayList<Int>()

                    if( analysisModel?.giftList!=null && analysisModel?.giftList?.size!=0)
                    {
                         mainList=ArrayList<String>()
                         subList= ArrayList<Int>()
                        var i: Int = analysisModel?.giftList?.size?.minus(1)!!
                        for( data in analysisModel?.giftList!!)
                        {
                            mainList.add(data.productName.toString())
                            data.qty?.let { subList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)view?.noDataGift?.visibility=View.VISIBLE
                                else  view?.noDataGift?.visibility=View.GONE
                            }
                        }
                        var adapterGift=SimpleListAdapter(mainList,subList)
                        views?.giftGiven_rv?.adapter=adapterGift
                    }
                    else {
                        view?.noDataGift?.visibility = View.VISIBLE
                        views?.giftGiven_rv?.adapter=SimpleListAdapter(mainList,subList)
                    }

                    views?.total_tv?.setText("Total: "+analysisModel?.lastPOBDetails?.totalPOB)


                    if(analysisModel?.lastPOBDetails?.remark?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.remarkPOB_tv?.visibility=View.GONE }
                    else{ views?.remarkPOB_tv?.setText("Remark: "+analysisModel?.lastPOBDetails?.remark) }

                    if(analysisModel?.lastPOBDetails?.strPobDate?.let { generalClassObject?.checkStringNullEmpty(it) } == true)
                    { views?.datePob_tv?.visibility=View.GONE }
                    else{ views?.datePob_tv?.setText("Date: "+analysisModel?.lastPOBDetails?.strPobDate) }

                        if(analysisModel?.lastRCPADetails?.size!=0)
                        {
                            views?.lastRcpaDetail_rv?.layoutManager=LinearLayoutManager(requireActivity())
                            val adapter=LastRCPA_Adapter(analysisModel?.lastRCPADetails)
                            view?.lastRcpaDetail_rv?.adapter=adapter
                        }
                    else{ views?.lastRcpaHeader_tv?.visibility=View.GONE}

                   // viewDetail_lRcpaDetail
                   // viewDetail_lpobDetail

                /*    if(generalClassObject?.checkStringNullEmpty(analysisModel?.lastPOBDetails!!.strPobDate!!)!!)
                    { views!!.datePob_tv.setText("Date: --") }
                    else{
                        if(generalClassObject!!.checkDateValidation(analysisModel?.lastPOBDetails?.strPobDate!!))
                        { views!!.datePob_tv.setText("Date: "+analysisModel?.lastPOBDetails?.strPobDate) }
                        else{ views!!.datePob_tv.setText("Date: ----")} }*/




            //        views!!.demoSales_tv.setText("Sales: "+analysisModel?.docLastRCPADetail?.ownSales)
            //        views!!.dateRCPA_tv.setText("Date: "+analysisModel?.docLastRCPADetail?.strRCPADate)

                    /*if(generalClassObject?.checkStringNullEmpty(analysisModel?.docLastRCPADetail!!.strRCPADate!!)!!)
                    { views?.dateRCPA_tv?.setText("Date: --") }
                    else{ views?.dateRCPA_tv?.setText("Date: "+analysisModel?.docLastRCPADetail?.strRCPADate) }

                    if(generalClassObject?.checkStringNullEmpty(analysisModel?.docLastRCPADetail!!.ownSales!!)!!)
                    { views!!.demoSales_tv.setText("Sales: --") }
                    else{ views!!.demoSales_tv.setText("Sales: "+analysisModel?.docLastRCPADetail?.ownSales)}
*/
                }
                else views?.noData_gif?.visibility=View.VISIBLE

                views?.analysisProgress?.visibility=View.GONE
            }

            override fun onFailure(call: Call<PreCallModel?>, t: Throwable?) {
                views?.analysisProgress?.visibility=View.GONE
                views?.noData_gif?.visibility=View.VISIBLE
                GeneralClass(requireActivity()).checkInternet() // check internet connection
                call.cancel()
            }
        })
    }

    fun createDCRAlert(routeId: String)
    { var activityId=0; var startingStation=0; var endingStation=0; var fieldStaffId=0

        val dialogBuilder = AlertDialog.Builder(requireActivity()); val inflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dcr_entry, null)
        dialogBuilder.setView(dialogView); dialogBuilder.setCancelable(false); val alertDialog = dialogBuilder.create()

        val headerText = dialogView.findViewById<View>(R.id.doctorName_tv) as TextView
        val cancelImag = dialogView.findViewById<View>(R.id.back_iv) as ImageView
        val toggleSwitch = dialogView.findViewById<View>(R.id.toggleSwitch) as Switch
        val selectActivityHeader = dialogView.findViewById<View>(R.id.selectActivityHeader) as TextView

        headerText.setText("New DCR")
        dialogView.dcr_date_tv.setText(generalClassObject?.getCurrentDate())
        cancelImag.setOnClickListener({alertDialog.dismiss()})

        var fieldWorkingList= arrayOf("Select","HQ ","Ex Station","Out Station")

        val firstFilter= CommonListGetClass().getNonRouteListForSpinner().filter { s -> (s.routeId != -7)  }
        val secondFilter= firstFilter.filter { s -> (s.routeId != -11)  }
        val thirdFilter= secondFilter.filter { s -> (s.routeId != -6)  }

        val adapterRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, thirdFilter)
        dialogView.activity_spin.setAdapter(adapterRoute)

        val startEndRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getRouteListForSpinner())
        dialogView.startingStation_spin.setAdapter(startEndRoute)
        dialogView.ending_spin.setAdapter(startEndRoute)

        val workingWithList: ArrayAdapter<SyncModel.Data.WorkingWith> = ArrayAdapter<SyncModel.Data.WorkingWith>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getAccListForSpinner())
        dialogView.accomp_spin.setAdapter(workingWithList)

        val adapterField: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, fieldWorkingList)
        dialogView.workingArea_spin.setAdapter(adapterField)

        if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
        {
            dialogView.managerParent_ll.visibility=View.VISIBLE
        }



        dialogView.workingArea_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
               if(position==3)
                   dialogView.startEndParent.visibility=View.VISIBLE
                else
                   dialogView.startEndParent.visibility=View.GONE
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.startingStation_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                startingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId!!
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.ending_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    endingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId!!

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.accomp_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    fieldStaffId = CommonListGetClass().getAccListForSpinner()[position].empId!!

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.activity_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0) {
                    activityId =
                        CommonListGetClass().getNonRouteListForSpinner()[position].routeId!!
                    if(toggleSwitch.isChecked) dialogView.additionalParent.visibility=View.VISIBLE

                }
                else{
                    if(toggleSwitch.isChecked) dialogView.additionalParent.visibility=View.GONE
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })


        toggleSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
        if(isChecked) { selectActivityHeader.setText("Additional activity")
        if(!dialogView.activity_spin.getSelectedItem().toString().equals("Select")){dialogView.additionalParent.visibility=View.VISIBLE}
            if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
            {
                dialogView.managerParent_ll.visibility=View.VISIBLE
            }
        }
        else{
            dialogView.managerParent_ll.visibility=View.GONE
            selectActivityHeader.setText("Select activity")
            dialogView.additionalParent.visibility=View.GONE}
        }

        dialogView.save_btn.setOnClickListener({
            val activitySeletd: String = dialogView.activity_spin.getSelectedItem().toString()
            val endingSeletd: String = dialogView.ending_spin.getSelectedItem().toString()
            val workAreaSeletd: String = dialogView.workingArea_spin.getSelectedItem().toString()
            val startingSeletd: String = dialogView.startingStation_spin.getSelectedItem().toString()
            val accompaniedstr: String = dialogView.accomp_spin.getSelectedItem().toString()

            if(workAreaSeletd.equals("Select")) {generalClassObject?.showSnackbar(it,"Working area not selected"); return@setOnClickListener}
            if(activitySeletd.equals("Select") && !toggleSwitch.isChecked) {generalClassObject?.showSnackbar(it,"Activity not selected"); return@setOnClickListener}
            if(startingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClassObject?.showSnackbar(it,"Start date not selected"); return@setOnClickListener}
            if(endingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClassObject?.showSnackbar(it,"End station not selected"); return@setOnClickListener}

            if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
            {
                if(dialogView.objDayEt.text.isEmpty() && toggleSwitch.isChecked) {generalClassObject?.showSnackbar(it,"Objective of day is empty"); return@setOnClickListener}
                if(dialogView.fieldStaffEt.text.isEmpty() && toggleSwitch.isChecked) {generalClassObject?.showSnackbar(it,"Field staff is empty"); return@setOnClickListener}
            }

            if(dialogView.remarkEt.text.isEmpty() && !toggleSwitch.isChecked) {generalClassObject?.showSnackbar(it,"Remark is empty"); return@setOnClickListener}
            if(dialogView.additionalEt.text.isEmpty() && toggleSwitch.isChecked && !activitySeletd.equals("Select")) {generalClassObject?.showSnackbar(it,"Additional activity remark is empty"); return@setOnClickListener}
            val i: Int = workAreaSeletd.indexOf(' ')

            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]

            val commonSaveDcrModel=CommonModel.SaveDcrModel()
            commonSaveDcrModel.dcrDate= generalClassObject?.currentDateMMDDYY().toString()
            commonSaveDcrModel.empId= loginModelHomePage.empId?:0
            commonSaveDcrModel.employeeId= loginModelHomePage.empId?:0
            commonSaveDcrModel.workingType=workAreaSeletd.substring(0, i)
            commonSaveDcrModel.remark=dialogView.remarkEt.text.toString()
            commonSaveDcrModel.routeId=routeId
            commonSaveDcrModel.monthNo=month+1
            commonSaveDcrModel.year=year
            commonSaveDcrModel.dayCount="0"
            commonSaveDcrModel.additionalActivityRemark=dialogView.additionalEt.text.toString()
            commonSaveDcrModel.dcrType=if(toggleSwitch.isChecked) 0  else 1

            if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
            {
                commonSaveDcrModel.accompaniedWith=fieldStaffId
                commonSaveDcrModel.objectiveOfDay=dialogView.objDayEt.text.toString()
                commonSaveDcrModel.feedBack=dialogView.fieldStaffEt.text.toString()
            }

            if(dialogView.startEndParent.visibility==View.VISIBLE)
            {
                commonSaveDcrModel.startingStation=startingStation
                commonSaveDcrModel.endingStation=endingStation
            }

            saveDCR_API(commonSaveDcrModel,alertDialog)

        })

        alertDialog.show()

    }

    suspend fun checkCurrentDCR_API(i: Int) {

        val response = APIClientKot().getUsersService(2, sharePreferance?.getPref("secondaryUrl")!!).checkDCR_API(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId,
            generalClassObject?.currentDateMMDDYY()
        )
        withContext(Dispatchers.Main) {


            if (response!!.isSuccessful) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    // val jsonObjError: JsonObject = response.body()?.get("errorObj") as JsonObject
                    if (response.body()?.errorObj?.errorMessage?.isEmpty() == true) {
                       // val data: JsonObject = response.body()?.get("data") as JsonObject
                      //  val dcrData: JsonObject = data?.get("dcrData") as JsonObject
                        val dcrData=response.body()?.data?.dcrData
                        if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {

                            alertClass?.commonAlert("Alert!", "Please submit tour program first")
                            alertClass?.hideAlert()
                            return@withContext
                        }
                        routeIdGetDCR = dcrData?.routeId.toString()
                        dcrData?.dataSaveType="D"
                        sharePreferance?.setPref("dcrObj", Gson().toJson(dcrData))

                        if (dcrData?.dcrId == 0) {

                            createDCRAlert(dcrData?.routeId.toString())
                            sharePreferance?.setPref("dcrId", dcrData?.dcrId.toString())
                        } else {
                            sharePreferance?.setPref("todayDate", generalClassObject?.currentDateMMDDYY())
                            sharePreferance?.setPref("dcrId", dcrData?.dcrId.toString())
                            sharePreferance?.setPref("empIdSp", loginModelHomePage.empId.toString())
                            if(i==1)
                            {
                                views?.bottomSheetTitle_tv?.setText("Select route")
                                selectionType=1
                                openCloseModel()
                            }
                            }
                    } else {
                        GeneralClass(requireActivity()).checkInternet() }
                }

            }

        }



       /* var returnType=false;
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
        })*/

    }

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog) {
        alertClass?.showProgressAlert("")
        var call: Call<JsonObject> = apiInterface?.saveDCS("bearer " + loginModelHomePage.accessToken,dcrObject) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                alertClass?.hideAlert()
                    Log.e("gsuifsdguifdgsf",Gson().toJson(response.body()))
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val jsonObjError:JsonObject = response.body()?.get("errorObj") as JsonObject
                if(!jsonObjError.get("errorMessage").asString.isEmpty())
                    {
                    alertClass?.commonAlert("",jsonObjError.get("errorMessage").asString)
                    }
                    else {
                    val jsonObjData:JsonObject = response.body()?.get("data") as JsonObject
                    alertClass?.commonAlert("",jsonObjData.get("message").asString)
                    sharePreferance?.setPref("todayDate",generalClassObject?.currentDateMMDDYY())
                    sharePreferance?.setPref("dcrId",jsonObjData.get("dcrId").asString)

                   if(generalClassObject?.isInternetAvailable() == true)
                   {
                       CoroutineScope(Dispatchers.IO).launch {
                           val api = async { checkCurrentDCR_API(1) }
                           api.await()
                       }
                   }

                    alertDialog.cancel()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                generalClassObject?.checkInternet()
                alertClass?.hideAlert()
                alertDialog.cancel()// check internet connection
                call.cancel()
            }
        })
    }



}