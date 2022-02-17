package `in`.processmaster.salestripclm.fragments
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.OnlinePresentationActivity
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.adapter.SimpleListAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.PreCallModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.graphics.Color
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_display_visual.view.*
import kotlinx.android.synthetic.main.activity_submit_edetailing.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.view.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.view.bottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_visualads.view.close_imv
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.view.*
import kotlinx.android.synthetic.main.dcr_entry.*
import kotlinx.android.synthetic.main.dcr_entry.view.*
import kotlinx.android.synthetic.main.fragment_new_call.*
import kotlinx.android.synthetic.main.fragment_new_call.view.*
import kotlinx.android.synthetic.main.join_activity_view.view.*
import kotlinx.android.synthetic.main.join_activity_view.view.noData_tv
import kotlinx.android.synthetic.main.progress_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewCallFragment : Fragment() {
    var dataBase = DatabaseHandler(activity)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var views:View?=null
    private lateinit var adapter:BottomSheetDoctorAdapter
    var doctorList= SplashActivity.staticSyncData?.data?.doctorList!!
    var routeList= SplashActivity.staticSyncData?.data?.routeList
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

        dataBase = DatabaseHandler(activity)
        sharePreferance = PreferenceClass(activity)
        alertClass = AlertClass(requireActivity())

        bottomSheetBehavior = BottomSheetBehavior.from(views!!.bottomSheet)
        generalClassObject= GeneralClass(requireActivity())

        if(SplashActivity.staticSyncData?.data?.settingDCR?.roleType=="FS") {
            views!!.selectTeamsCv.visibility = View.GONE
            views!!.selectRoutesCv.setEnabled(true) }
        else
           views!!.selectRoutesCv.setEnabled(false)
           views!!.selectRoute_tv.setBackgroundColor(Color.parseColor("#FA8072"))


        views!!.selectTeamsCv.setOnClickListener({
            views!!.selectHeader_tv?.setText("Select Team")
            selectionType=0
            openCloseModel() })

        views!!.selectRoutesCv.setOnClickListener({
            if(!checkDCRusingShareP()) return@setOnClickListener
            views!!.selectHeader_tv?.setText("Select route")
            selectionType=1
            openCloseModel()})

        views!!.selectDoctorsCv.setOnClickListener({
            views!!.selectHeader_tv?.setText("Select Doctor")
            selectionType=2
            if(doctorList.size<=0)
            {   GeneralClass(requireActivity()).showSnackbar(it,"This route has no doctor")
                return@setOnClickListener
            }
            openCloseModel()})

        views!!.selectDoctorsCv.setEnabled(false)

        views!!.close_imv?.setOnClickListener({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})
        views!!.doctorSearch_et!!.addTextChangedListener(filterTextWatcher)

        views!!.startDetailing_btn.setOnClickListener({
            val intent = Intent(activity, OnlinePresentationActivity::class.java)
            intent.putExtra("doctorID", selectedDocID)
            intent.putExtra("doctorName", selectedDocName)
            startActivity(intent)
        })

        views!!.skipDetailing_btn.setOnClickListener({
            val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
            intent.putExtra("doctorID", selectedDocID)
            intent.putExtra("doctorName", selectedDocName)
            startActivity(intent)
        })

        routeList = routeList?.filter { s -> s.headQuaterName !=""} as java.util.ArrayList<SyncModel.Data.Route>
        checkDCRusingShareP()

        return views
    }

    fun checkDCRusingShareP():Boolean
    {
        if( sharePreferance?.getPref("todayDate") != generalClassObject?.currentDateMMDDYY() || sharePreferance?.getPref("dcrId")=="0") {
            checkCurrentDCR_API()
            return false }
        else return true
    }

    fun openCloseModel()
    {
        adapter =BottomSheetDoctorAdapter()
        views!!.doctorList_rv.setLayoutManager(GridLayoutManager(requireActivity(), 3))
        views!!.doctorList_rv.adapter = adapter

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

        var filteredDataRoute:ArrayList<SyncModel.Data.Route> = routeList as ArrayList<SyncModel.Data.Route>
        var filteredDataDoctor:ArrayList<SyncModel.Data.Doctor> = doctorList as ArrayList<SyncModel.Data.Doctor>


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
            {   if(filteredDataRoute.size<=0)  views!!.noData_tv?.visibility=View.VISIBLE
                else  views!!.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataRoute?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.routeName)
                holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
                holder.speciality_tv.visibility=View.GONE

                holder.parent_cv.setOnClickListener({
                    views!!.selectTeam_tv.setText((modeldata?.routeName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    onSelection()
                })
            }

            else if(selectionType==1)
            {    if(filteredDataRoute.size<=0)  views!!.noData_tv?.visibility=View.VISIBLE
                 else  views!!.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataRoute?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.routeName)
                holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
                holder.speciality_tv.visibility=View.GONE

                holder.parent_cv.setOnClickListener({
                    views!!.selectRoute_tv.setText((modeldata?.routeName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    onSelection()
                    applySelectionFilter(modeldata.routeId)
                })
            }

            else{
                if(filteredDataDoctor.size<=0)  views!!.noData_tv?.visibility=View.VISIBLE
                else  views!!.noData_tv?.visibility=View.GONE

                val modeldata = filteredDataDoctor?.get(position)
                holder.headerDoctor_tv.setText(modeldata?.doctorName)
                holder.route_tv.setText("Route- " + modeldata?.routeName)
                holder.speciality_tv.setText("Speciality- " + modeldata?.specialityName)

                holder.parent_cv.setOnClickListener({
                    views!!.selectDoctor_tv.setText((modeldata?.doctorName))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    setDoctor(modeldata)
                    onSelection()
                })
            }

        }
        override fun getItemCount(): Int
        {
            if(selectionType==0)
                return filteredDataDoctor?.size!!
            else if(selectionType==1)
                return filteredDataRoute?.size!!
            else
                return filteredDataDoctor?.size!!
        }


        //-------------------------------------filter list using text input from edit text
        override fun getFilter(): Filter? {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    if(selectionType==0)
                    { filteredDataDoctor = results.values as ArrayList<SyncModel.Data.Doctor> }
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
                        val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()
                        constraint = constraint.toString().lowercase()
                        for (i in 0 until doctorList?.size!!) {
                            val dataNames: SyncModel.Data.Doctor = doctorList?.get(i)!!
                            if (dataNames.doctorName.lowercase().startsWith(constraint.toString())) {
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
                            val routeName: SyncModel.Data.Route = routeList?.get(i)!!
                            if (routeName.routeName.lowercase().startsWith(constraint.toString())) {
                                FilteredArrayNames.add(routeName)
                            }
                        }
                        results.count = FilteredArrayNames.size
                        results.values = FilteredArrayNames
                        return results }
                    else
                    {   val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()
                        constraint = constraint.toString().lowercase()
                        for (i in 0 until doctorList?.size!!) {
                            val docNames: SyncModel.Data.Doctor = doctorList?.get(i)!!
                            if (docNames.doctorName.lowercase().startsWith(constraint.toString())) {
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
        selectedDocID=doctorDetailModel.doctorId
        selectedDocName=doctorDetailModel.doctorName
        preCallAnalysisApi()

        if(doctorDetailModel.mobileNo.isEmpty())
            views?.mobileNumberParent?.visibility=View.GONE
        if(doctorDetailModel.emailId.isEmpty())
            views?.emailParent?.visibility=View.GONE
        if(doctorDetailModel.cityName.isEmpty())
            views?.cityParent?.visibility=View.GONE

    }

    fun onSelection()
    {
    views!!.doctorSearch_et!!.setText("")

     if(selectionType==0)
     {   views?.doctorDetail_parent?.visibility=View.GONE
         views?.precall_parent?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views!!.selectTeam_tv.setBackgroundColor(Color.parseColor("#3CB371"))
         views!!.selectRoute_tv.setBackgroundColor(Color.parseColor("#FA8072"))
         views!!.selectRoute_tv.setText("Select route")
         views!!.selectDoctor_tv.setBackgroundColor(Color.parseColor("#A9A9A9"))
         views!!.selectDoctor_tv.setText("Select Doctor")
         views!!.selectRoutesCv.setEnabled(true)
         views!!.selectDoctorsCv.setEnabled(false)
     }
     else if(selectionType==1)
     {
         views?.doctorDetail_parent?.visibility=View.GONE
         views?.precall_parent?.visibility=View.GONE
         views?.noData_gif?.visibility=View.VISIBLE
         views!!.selectRoute_tv.setBackgroundColor(Color.parseColor("#3CB371"))
         views!!.selectDoctor_tv.setBackgroundColor(Color.parseColor("#FA8072"))
         views!!.selectDoctor_tv.setText("Select Doctor")
         views!!.selectDoctorsCv.setEnabled(true)
     }
     else
     {
         views!!.selectDoctor_tv.setBackgroundColor(Color.parseColor("#3CB371"))
     }
    }

    fun applySelectionFilter(id:Int)
    {
        if(selectionType==0)
        {

        }
        else if(selectionType==1)
         doctorList = SplashActivity.staticSyncData?.data?.doctorList!!.filter { s -> s.routeId == id } as java.util.ArrayList<SyncModel.Data.Doctor>

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
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty()!!) {
                    val analysisModel=response.body()?.getData()?.lastVisitSummary
                    views?.precall_parent?.visibility=View.VISIBLE

                    views?.lastVisitDate_tv?.setText(analysisModel?.strDcrDate)
                    views?.reportedTime_tv?.setText(analysisModel?.strReportedTime)

                    if(analysisModel?.visitPurpose==null) views?.visitPurpose_tv?.setText("-") else views?.visitPurpose_tv?.setText(analysisModel?.visitPurpose)
                    if(analysisModel?.workWithName==null) views?.workingWith_tv?.setText("-") else views?.workingWith_tv?.setText(analysisModel?.workWithName)
                    if(analysisModel?.remarks==null) views?.visitPurpose_tv?.visibility=View.INVISIBLE else views?.remark_tv?.setText(analysisModel?.remarks)


                    views!!.brandList_rv.layoutManager=LinearLayoutManager(requireActivity())
                    views!!.sampleGiven_rv.layoutManager=LinearLayoutManager(requireActivity())
                    views!!.giftGiven_rv.layoutManager=LinearLayoutManager(requireActivity())

                    var mainList= ArrayList<String>()
                    var subList= ArrayList<Int>()

                    if( analysisModel?.productList!=null)
                    {
                        for( data in analysisModel?.productList!!)
                        {
                            mainList.add(data.productName)
                        }
                        var adapterProduct=SimpleListAdapter(mainList,subList)
                        views!!.brandList_rv.adapter=adapterProduct
                    }

                    if( analysisModel?.sampleList!=null)
                    {
                        mainList= ArrayList<String>()
                        subList= ArrayList<Int>()
                        for( data in analysisModel?.sampleList!!) {
                            mainList.add(data.productName.toString())
                            subList.add(data.qty!!) }

                        var adapterSampleGiven=SimpleListAdapter(mainList,subList)
                        views!!.sampleGiven_rv.adapter=adapterSampleGiven
                    }

                    if( analysisModel?.giftList!=null)
                    {
                        mainList= ArrayList<String>()
                        subList= ArrayList<Int>()
                        for( data in analysisModel?.giftList!!)
                        {
                            mainList.add(data.productName.toString())
                            subList.add(data.qty!!)
                        }
                        var adapterGift=SimpleListAdapter(mainList,subList)
                        views!!.giftGiven_rv.adapter=adapterGift
                    }

                    views!!.total_tv.setText("Total: "+analysisModel?.lastPOBDetails?.totalPOB)

                    if(analysisModel?.lastPOBDetails?.remark == null)  views!!.remarkPOB_tv.visibility=View.GONE

                    views!!.remarkPOB_tv.setText("Remark: "+analysisModel?.lastPOBDetails?.remark)
                    views!!.datePob_tv.setText("Date: "+analysisModel?.lastPOBDetails?.lastPobDate)

                    views!!.demoSales_tv.setText("Sales: "+analysisModel?.docLastRCPADetail?.ownSales)
                    views!!.dateRCPA_tv.setText("Date: "+analysisModel?.docLastRCPADetail?.strRCPADate)

                }
                else
                    views?.noData_gif?.visibility=View.VISIBLE

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
    { var activityId=0; var startingStation=0; var endingStation=0;

        val dialogBuilder = AlertDialog.Builder(requireActivity()); val inflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dcr_entry, null)
        dialogBuilder.setView(dialogView); dialogBuilder.setCancelable(false); val alertDialog = dialogBuilder.create()

        val headerText = dialogView.findViewById<View>(R.id.doctorName_tv) as TextView
        val cancelImag = dialogView.findViewById<View>(R.id.back_iv) as ImageView

        headerText.setText("New DCR")
        dialogView.dcr_date_tv.setText(generalClassObject?.getCurrentDate())
        cancelImag.setOnClickListener({alertDialog.dismiss()})

        var fieldWorkingList= arrayOf("Select","HQ ","Ex Station","Out Station")

        val adapterRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getNonRouteListForSpinner())
        dialogView.activity_spin.setAdapter(adapterRoute)

        val startEndRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getRouteListForSpinner())
        dialogView.startingStation_spin.setAdapter(startEndRoute)
        dialogView.ending_spin.setAdapter(startEndRoute)

        val adapterField: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, fieldWorkingList)
        dialogView.workingArea_spin.setAdapter(adapterField)

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
                startingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        dialogView.ending_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    endingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        dialogView.activity_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    activityId = CommonListGetClass().getNonRouteListForSpinner()[position].routeId
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })



        dialogView.save_btn.setOnClickListener({
            val activitySeletd: String = dialogView.activity_spin.getSelectedItem().toString()
            val endingSeletd: String = dialogView.ending_spin.getSelectedItem().toString()
            val workAreaSeletd: String = dialogView.workingArea_spin.getSelectedItem().toString()
            val startingSeletd: String = dialogView.startingStation_spin.getSelectedItem().toString()

            if(workAreaSeletd.equals("Select")) {generalClassObject?.showSnackbar(it,"Working area not selected"); return@setOnClickListener}
            if(activitySeletd.equals("Select")) {generalClassObject?.showSnackbar(it,"Activity not selected"); return@setOnClickListener}
            if(startingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClassObject?.showSnackbar(it,"Start date not selected"); return@setOnClickListener}
            if(endingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClassObject?.showSnackbar(it,"End station not selected"); return@setOnClickListener}
            if(dialogView.remarkEt.text.isEmpty()) {generalClassObject?.showSnackbar(it,"Remark is empty"); return@setOnClickListener}
            val i: Int = workAreaSeletd.indexOf(' ')

            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]

            val commonSaveDcrModel=CommonModel.SaveDcrModel()
            commonSaveDcrModel.dcrDate= generalClassObject!!.currentDateMMDDYY()
            commonSaveDcrModel.empId= loginModelHomePage.empId
            commonSaveDcrModel.employeeId= loginModelHomePage.empId
            commonSaveDcrModel.workingType=workAreaSeletd.substring(0, i)
            commonSaveDcrModel.remark=dialogView.remarkEt.text.toString()
            commonSaveDcrModel.routeId=routeId
            commonSaveDcrModel.monthNo=month+1
            commonSaveDcrModel.year=year
            commonSaveDcrModel.dayCount="0"

            if(dialogView.startEndParent.visibility==View.VISIBLE)
            {
                commonSaveDcrModel.startingStation=startingStation
                commonSaveDcrModel.endingStation=endingStation
            }

            saveDCR_API(commonSaveDcrModel,alertDialog)

        })

        alertDialog.show()

    }
    fun checkCurrentDCR_API(){
        alertClass?.showAlert("")

        var call: Call<JsonObject> = apiInterface?.checkDCR_API("bearer " + loginModelHomePage.accessToken, loginModelHomePage.empId,generalClassObject!!.currentDateMMDDYY()) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                  val jsonObjError:JsonObject = response.body()?.get("errorObj") as JsonObject
                  if(jsonObjError.get("errorMessage").asString.isEmpty())
                  {
                      val data:JsonObject = response.body()?.get("data") as JsonObject
                      val dcrData:JsonObject = data?.get("dcrData") as JsonObject
                     if(dcrData.get("routeId").asString.isEmpty())
                     {
                         alertClass?.commonAlert("Alert!","Please submit tour program first")
                         alertClass?.hideAlert()
                         return
                     }

                    routeIdGetDCR=dcrData.get("routeId").asString
                      if(dcrData.get("dcrId").asInt==0) {
                          createDCRAlert(dcrData.get("routeId").asString)
                          sharePreferance?.setPref("dcrId", dcrData.get("dcrId").asString) }
                      else
                      sharePreferance?.setPref("todayDate",generalClassObject?.currentDateMMDDYY())
                      sharePreferance?.setPref("dcrId",dcrData.get("dcrId").asString)
                  }
                }
                alertClass?.hideAlert()
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                GeneralClass(requireActivity()).checkInternet()
                alertClass?.hideAlert()// check internet connection
                call.cancel()
            }
        })
    }

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog) {
        alertClass?.showAlert("")
        var call: Call<JsonObject> = apiInterface?.saveDCS("bearer " + loginModelHomePage.accessToken,dcrObject) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                alertClass?.hideAlert()

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