package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
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
    lateinit var db : DatabaseHandler
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var views:View?=null
    var adapter:BottomSheetDoctorAdapter =BottomSheetDoctorAdapter()
    var doctorListArray:ArrayList<SyncModel.Data.Doctor> = ArrayList()
    var retailerListArray:ArrayList<SyncModel.Data.Retailer> = ArrayList()
    var routeList: ArrayList<SyncModel.Data.Route> = ArrayList()
    var ristrictedRouteList: ArrayList<SyncModel.Data.Route> = ArrayList()
    var teamsList: ArrayList<SyncModel.Data.FieldStaffTeam> = ArrayList()
    var selectionType=0
    var selectedDocID=0
    var selectedDocName=""
    var sharePreferance: PreferenceClass? = null
    var generalClassObject:GeneralClass?=null
    var routeIdGetDCR=""
    var alertClass:AlertClass?=null
    var  docCallModel : DailyDocVisitModel.Data= DailyDocVisitModel.Data()
    var isSecondTime = false
    var doctorObject=SyncModel.Data.Doctor()
    var isRetailerAttached=false
    companion object {
        var retailerObj= SyncModel.Data.Retailer()
        var instance: NewCallFragment? = null
    }

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views= inflater.inflate(R.layout.fragment_new_call, container, false)

        if (activity != null && isAdded)
        {
            initView()
            instance = this;
        }
        return views
    }

    fun initView()
    {
        bottomSheetBehavior = BottomSheetBehavior.from(views!!.bottomSheet)

        val strtext = arguments?.getString("retailerData")
        db = DatabaseHandler(activity)
        sharePreferance = PreferenceClass(activity)
        alertClass = AlertClass(activity)
        generalClassObject= activity?.let { GeneralClass(it) }

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

                    val responseDocCall=db.getApiDetail(5)
                    if(!responseDocCall.equals("")) {
                        docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
                    }
                    if(staticSyncData?.settingDCR?.isRestrictedParty==true)
                    {
                        if(sharePreferance?.getPref("dcrObj")?.isEmpty() == false) {
                            var dcrModel = Gson().fromJson(sharePreferance?.getPref("dcrObj"), GetDcrToday.Data.DcrData::class.java)

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

                    if(sharePreferance?.getPref("otherActivitySelected").equals("1") && sharePreferance?.checkKeyExist("todayDate")==true && sharePreferance?.getPref("todayDate") == generalClassObject?.currentDateMMDDYY() )
                    {
                        if(generalClassObject?.isInternetAvailable()==false)
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
                       /* Handler(Looper.getMainLooper()).postDelayed({
                            if(!isChecked){
                                if(isRetailerAttached==false) {
                                    isRetailerAttached = true
                                    activity?.runOnUiThread {
                                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                                        transaction?.replace(R.id.frameRetailer_view, RetailerFillFragment(this@NewCallFragment))
                                        transaction?.disallowAddToBackStack()
                                        transaction?.commit()
                                    }
                                }
                            }
                        },50)*/

                    }

                    views?.selectTeamsCv?.setOnClickListener({
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
                    /*  if(checkDCRusingShareP())
                        { openCloseModel() }*/
                    })

                    views?.selectRoutesCv?.setOnClickListener({
                        selectionType=1
                        views?.bottomSheetTitle_tv?.setText("Select route")
                        if(!SplashActivity.staticSyncData?.settingDCR?.roleType.equals("MAN") ){
                            if(generalClassObject?.isInternetAvailable() == true)
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
                            else{ openCloseModel() }
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
                                alertClass?.commonAlert("This route has no doctor","")
                                return@setOnClickListener
                            }
                        }
                        else{
                            views?.bottomSheetTitle_tv?.setText("Select Retailer")
                            if(retailerListArray.size<=0)
                            {
                                alertClass?.commonAlert("This route has no Retailer","")
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

                  /*  views?.startDetailing_btn?.setOnClickListener({

                        val isAlreadyContain=docCallModel.dcrDoctorlist?.any{ s -> s.doctorId == selectedDocID }
                        if(isAlreadyContain == true) {
                            views?.parentButton?.visibility=View.GONE
                            alertClass?.commonAlert("Alert!","This doctor DCR is already submitted")
                            return@setOnClickListener
                        }


                         val intent = Intent(activity, OnlinePresentationActivity::class.java)
                        intent.putExtra("doctorID", selectedDocID)
                        intent.putExtra("doctorName", selectedDocName)
                        intent.putExtra("skip", false)
                        intent.putExtra("doctorObj", Gson().toJson(doctorObject))
                        intent.putExtra("isPresentation", true)
                        startActivityForResult(intent,3)

                       })

                    views?.skipDetailing_btn?.setOnClickListener({
                        val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
                        intent.putExtra("doctorID", selectedDocID)
                        intent.putExtra("doctorName", selectedDocName)
                        intent.putExtra("skip", true)
                        startActivityForResult(intent,3)
                    })*/
                }
                alertClass?.hideAlert()
            }
        }



    /*    else
            views?.selectRoutesCv?.setEnabled(false)*/
        //  views?.selectRoute_tv?.setBackgroundColor(Color.parseColor("#FA8072"))
    //  checkDCRusingShareP(0)
    }

    fun getInstance(): NewCallFragment? {
        return instance
    }

/*    fun checkDCRusingShareP():Boolean
    {
        if(generalClassObject?.isInternetAvailable() == true)
        {
            callCoroutineApi()
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
    }*/

    fun callCoroutineApi() {
        alertClass?.hideAlert()
        alertClass?.showProgressAlert("")
        try{
            val coroutineScope = CoroutineScope(Dispatchers.IO + generalClassObject!!?.coroutineExceptionHandler).launch {
                val api = async { checkCurrentDCR_API() }
                api.await()
            }

            coroutineScope.invokeOnCompletion {
                activity?.runOnUiThread(java.lang.Runnable {
                    alertClass?.hideAlert()
                })
            }
        }
        catch (e:Exception)
        {
            alertClass?.hideAlert()
            alertClass?.lowNetworkAlert()
        }


    }

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
                        alertClass?.showProgressAlert("")
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

        val responseDocCall=db.getApiDetail(5)
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

        val dcrRetailerList=db.getAllSaveSendRetailer("retailerFeedback")
        val eDetailingArray=db.getAllSaveSend("feedback")
        for ((index,data) in doctTempList.withIndex())
        {
            val isAlreadyContain=eDetailingArray.any{ s -> s.doctorId == data.doctorId }
            if(isAlreadyContain==true) {
                doctorListArray.remove(data)
            }
        }
        for ((index,data) in rectTempList.withIndex())
        {
            val isAlreadyContain=dcrRetailerList.any{ s -> s.retailerId == data.retailerId }
            if(isAlreadyContain == true) {
                retailerListArray.remove(data)
            }
        }
    }

    fun setRetailer(retailerModel:SyncModel.Data.Retailer)
    {
        val isAlreadyContain=docCallModel.dcrRetailerlist?.any{ s -> s.retailerId == retailerModel.retailerId }
        if(isAlreadyContain == true) {
            views?.selectDoctor_tv?.setText("Select Retailer")
           // views?.parentButton?.visibility=View.GONE
            alertClass?.commonAlert("Alert!","Dcr already done for this retailer")
            alertClass?.hideAlert()
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
               transaction?.commit()

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
                    alertClass?.hideAlert()
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)}, 2)
            }

        }
    }


    fun setDoctor(doctorDetailModel: SyncModel.Data.Doctor)
    {
      //  views?.precall_parent?.visibility=View.GONE
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
        doctorObject=doctorDetailModel;

        if(doctorDetailModel.mobileNo?.isEmpty() == true)
            views?.mobileNumberParent?.visibility=View.GONE
        if(doctorDetailModel.emailId?.isEmpty() == true)
            views?.emailParent?.visibility=View.GONE
        if(doctorDetailModel.cityName?.isEmpty() == true)
            views?.cityParent?.visibility=View.GONE



        if(generalClassObject?.isInternetAvailable() == true) {

                    views?.frameRetailer_view?.visibility=View.GONE
                    views?.framePreCall_view?.visibility=View.VISIBLE
                    val transaction = childFragmentManager.beginTransaction()
                    transaction?.replace(R.id.framePreCall_view, PreCallsFragment(doctorDetailModel))
                    transaction?.disallowAddToBackStack()
                    transaction?.commit()

                     views?.noInternet_tv?.visibility = View.GONE
                     views?.noData_gif?.visibility = View.GONE

         //   preCallAnalysisApi(doctorDetailModel)
        }
        else noInternet_tv.visibility=View.VISIBLE; /*views?.parentButton?.visibility=View.VISIBLE*/

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
               // startPoint.setLatitude(22.724177793056885)
                startPoint.setLongitude(getGpsTracker.longitude)
               // startPoint.setLongitude(75.90522484470453)


               /* if(views?.docRetail_switch?.isChecked==true )
                {*/
                    val docFirstFilter= SplashActivity.staticSyncData?.doctorList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>

                    for(fetch in docFirstFilter)
                    {
                        if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }

                        val endPoint = Location("locationB")
                        endPoint.latitude = fetch.latitude
                        endPoint.longitude = fetch.longitude
                        val distance = startPoint.distanceTo(endPoint).toInt()
                       // doctorListArray.add(fetch)
                        if(distance <= getRadius){
                           doctorListArray.add(fetch)
                        }
                    }
               // }
              /*  else
                {

                    val retailFirstFilter= SplashActivity.staticSyncData?.retailerList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Retailer>

                    for(fetch in retailFirstFilter)
                    {

                        if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }

                        val endPoint = Location("locationB")
                        endPoint.latitude = fetch.latitude
                        endPoint.longitude = fetch.longitude
                        val distance = startPoint.distanceTo(endPoint).toInt()
                      //  retailerListArray.add(fetch)
                        if(distance <= getRadius){
                           retailerListArray.add(fetch)
                        }
                    }
                }*/
            }
            else if(staticSyncData?.settingDCR?.isRetailerFencingRequired == true && views?.docRetail_switch?.isChecked==false){
                val retailFirstFilter= SplashActivity.staticSyncData?.retailerList?.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Retailer>

                val getGpsTracker=GPSTracker(activity)
                val jsonObj=JSONObject(staticSyncData?.configurationSetting)
                val getRadius=jsonObj.getInt("SET011")

                val startPoint = Location("locationA")
                startPoint.setLatitude(getGpsTracker.latitude)
                // startPoint.setLatitude(22.724177793056885)
                startPoint.setLongitude(getGpsTracker.longitude)
                // startPoint.setLongitude(75.90522484470453)
                for(fetch in retailFirstFilter)
                {

                    if(fetch.latitude==0.00 || fetch.longitude==0.00) { continue }

                    val endPoint = Location("locationB")
                    endPoint.latitude = fetch.latitude
                    endPoint.longitude = fetch.longitude
                    val distance = startPoint.distanceTo(endPoint).toInt()
                    //  retailerListArray.add(fetch)
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

   /* private fun preCallAnalysisApi(doctorDetailModel: SyncModel.Data.Doctor) {

        views?.noData_gif?.visibility=View.GONE
        views?.analysisProgress?.visibility=View.VISIBLE

        var profileData = sharePreferance?.getPref("profileData")           //get profile data from share preferance
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)

        //call submit visual ads api interfae post method
        var call: Call<PreCallModel>? = apiInterface?.priCallAnalysisApi("bearer " + loginModel?.accessToken,selectedDocID) as? Call<PreCallModel>
        call?.enqueue(object : Callback<PreCallModel?> {
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


                    views?.brandList_rv?.layoutManager=LinearLayoutManager(activity)
                    views?.sampleGiven_rv?.layoutManager=LinearLayoutManager(activity)
                    views?.giftGiven_rv?.layoutManager=LinearLayoutManager(activity)

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

                        val isAlreadyContain=docCallModel.dcrDoctorlist?.any{ s -> s.doctorId == doctorDetailModel.doctorId }
                        if(isAlreadyContain == true) {
                            views?.parentButton?.visibility=View.GONE
                            alertClass?.commonAlert("Alert!","Doctor e-detailing already done for today")
                        }

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
                            views?.lastRcpaDetail_rv?.layoutManager=LinearLayoutManager(activity)
                            val adapter=LastRCPA_Adapter(analysisModel?.lastRCPADetails)
                            view?.lastRcpaDetail_rv?.adapter=adapter
                        }
                    else{ views?.lastRcpaHeader_tv?.visibility=View.GONE}

                   // viewDetail_lRcpaDetail
                   // viewDetail_lpobDetail

                *//*    if(generalClassObject?.checkStringNullEmpty(analysisModel?.lastPOBDetails!!.strPobDate!!)!!)
                    { views!!.datePob_tv.setText("Date: --") }
                    else{
                        if(generalClassObject!!.checkDateValidation(analysisModel?.lastPOBDetails?.strPobDate!!))
                        { views!!.datePob_tv.setText("Date: "+analysisModel?.lastPOBDetails?.strPobDate) }
                        else{ views!!.datePob_tv.setText("Date: ----")} }*//*




            //        views!!.demoSales_tv.setText("Sales: "+analysisModel?.docLastRCPADetail?.ownSales)
            //        views!!.dateRCPA_tv.setText("Date: "+analysisModel?.docLastRCPADetail?.strRCPADate)

                    *//*if(generalClassObject?.checkStringNullEmpty(analysisModel?.docLastRCPADetail!!.strRCPADate!!)!!)
                    { views?.dateRCPA_tv?.setText("Date: --") }
                    else{ views?.dateRCPA_tv?.setText("Date: "+analysisModel?.docLastRCPADetail?.strRCPADate) }

                    if(generalClassObject?.checkStringNullEmpty(analysisModel?.docLastRCPADetail!!.ownSales!!)!!)
                    { views!!.demoSales_tv.setText("Sales: --") }
                    else{ views!!.demoSales_tv.setText("Sales: "+analysisModel?.docLastRCPADetail?.ownSales)}
*//*
                }
                else views?.noData_gif?.visibility=View.VISIBLE

                views?.analysisProgress?.visibility=View.GONE
            }

            override fun onFailure(call: Call<PreCallModel?>, t: Throwable?) {
                views?.analysisProgress?.visibility=View.GONE
                views?.noData_gif?.visibility=View.VISIBLE
                activity?.let { GeneralClass(it).checkInternet() } // check internet connection
                call.cancel()
            }
        })
    }*/

    suspend fun checkCurrentDCR_API() {

        val response = APIClientKot().getUsersService(2, sharePreferance?.getPref("secondaryUrl")!!).checkDCR_API(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId,
            generalClassObject?.currentDateMMDDYY()
        )
        withContext(Dispatchers.Main) {


            if (response!!.isSuccessful) {

                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    Log.e("dcrObject",Gson().toJson(response.body()))
                    if (response.body()?.errorObj?.errorMessage?.isEmpty() == true) {
                        val dcrData=response.body()?.data?.dcrData

                        if(staticSyncData?.settingDCR?.isCallPlanMandatoryForDCR==true && response.body()?.data?.isCPExiest == false)
                        {
                            alertClass?.commonAlert("Alert!","Please submit you day plan first")
                            return@withContext
                        }

                        if (dcrData?.rtpApproveStatus?.lowercase() != "a") {
                            alertClass?.commonAlert("Alert!","Tour plan not approved")
                            return@withContext
                        }

                        if (dcrData?.dataSaveType?.lowercase() == "s") {
                            alertClass?.commonAlert("Alert!","The DCR is submitted it cannot be unlocked please connect with your admin")
                            return@withContext
                        }

                        if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {
                            alertClass?.commonAlert("Alert!", "Please submit tour program first")
                            alertClass?.hideAlert()
                            return@withContext
                        }

                        routeIdGetDCR = dcrData?.routeId.toString()
                        dcrData?.dataSaveType="D"
                        sharePreferance?.setPref("dcrObj", Gson().toJson(dcrData))

                        if (dcrData?.dcrId == 0) {
                           // createDCRAlert(dcrData?.routeId.toString())
                               alertClass?.createDCRAlert(dcrData?.routeId.toString(),dcrData?.routeName.toString())
                            sharePreferance?.setPref("dcrId", dcrData?.dcrId.toString())
                        } else {
                            sharePreferance?.setPref("todayDate", generalClassObject?.currentDateMMDDYY())
                            sharePreferance?.setPref("dcrId", dcrData?.dcrId.toString())
                            sharePreferance?.setPref("empIdSp", loginModelHomePage.empId.toString())

                            views?.bottomSheetTitle_tv?.setText(if(selectionType==1)"Select route" else "Select team")

                            if(dcrData?.otherDCR!=0)
                            {
                                setOtherActivityView()
                                sharePreferance?.setPref("otherActivitySelected","1")
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

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog, checked: Boolean) {
        alertClass?.showProgressAlert("")
        var call: Call<JsonObject>? = apiInterface?.saveDCS("bearer " + loginModelHomePage.accessToken,dcrObject) as? Call<JsonObject>
        call?.enqueue(object : Callback<JsonObject?> {
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

                    if(!checked)
                    {
                        alertClass?.commonAlert("",jsonObjData.get("message").asString + "And kindly moved to Salestrip to submit DCR")

                    }
                    else
                    {
                        alertClass?.commonAlert("",jsonObjData.get("message").asString)
                    }
                    sharePreferance?.setPref("todayDate",generalClassObject?.currentDateMMDDYY())
                    sharePreferance?.setPref("dcrId",jsonObjData.get("dcrId").asString)

                    if(!checked) sharePreferance?.setPref("otherActivitySelected","1")

                 /*  if(generalClassObject?.isInternetAvailable() == true && checked)
                   {
                       CoroutineScope(Dispatchers.IO).launch {
                           val api = async { checkCurrentDCR_API() }
                           api.await()
                       }
                   }
*/
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

    fun setOtherActivityView()
    {
        alertClass?.commonAlert("Alert!","You have not planned field working today. Kindly save it from Salestrip app")

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
            val responseDocCall=db.getApiDetail(5)
            if(!responseDocCall.equals("")) {
                docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
            }
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
            transaction?.commit()

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

}