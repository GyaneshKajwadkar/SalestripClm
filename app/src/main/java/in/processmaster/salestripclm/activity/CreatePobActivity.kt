package `in`.processmaster.salestripclm.activity

import IntegerInterface
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.adapter.*
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag
import `in`.processmaster.salestripclm.interfaceCode.EditInterface
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.interfaceCode.productTransferIndividual
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_pob.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.*
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.pob_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreatePobActivity : BaseActivity(), IdNameBoll_interface, productTransfer,
    productTransferIndividual, EditInterface, IntegerInterface {

    val myCalendar = Calendar.getInstance()
    var dateSelection=0
    private lateinit var bottomSheetDocRetSelect: BottomSheetBehavior<ConstraintLayout>
    var stokistArray=ArrayList<IdNameBoll_model>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var selectedStockist=IdNameBoll_model()
    var pobProductSelectAdapter=PobProductAdapter()
    lateinit var selectedPobAdapter : SelectedPobAdapter
    var adapter: BottomSheetDoctorAdapter =BottomSheetDoctorAdapter()
    var teamsList: java.util.ArrayList<SyncModel.Data.FieldStaffTeam> = java.util.ArrayList()
    var selectionType=0
    var doctorListArray: java.util.ArrayList<SyncModel.Data.Doctor> = java.util.ArrayList()
    var retailerListArray: java.util.ArrayList<SyncModel.Data.Retailer> = java.util.ArrayList()
    var isManager=false
    var mode=1;

    //pob initilize------
    var mainProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var selectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var unSelectedProductList:ArrayList<SyncModel.Data.Product> = ArrayList()
    var checkIsDcrSave=false
    var passingSchemeList:ArrayList<SyncModel.Data.Scheme> = ArrayList()
    var selectedDoctorId=0
    var globalSaveModel: CreatePOBModel.Data.pobObject?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pob)

        Handler(Looper.getMainLooper()).postDelayed({
            initView()
        },500)

    }

   fun  initView()
    {
        back_iv.setOnClickListener { onBackPressed() }
        doctorName_tv.setText("Create POB")
        doctorName_tv.visibility= View.VISIBLE
        bottomSheetDocRetSelect = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet_submitScreen)
        selectedPob_rv.layoutManager=LinearLayoutManager(this)
        pobApiList_rv.layoutManager=LinearLayoutManager(this)

        if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
        {
            isManager=true
        }
        else if(SplashActivity.staticSyncData?.settingDCR?.roleType=="FS") {
            checkUser_ll.visibility = View.GONE
            isManager=false
        }

        getPobNumber()

        var datePicker= DatePickerDialog(this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH])

      //  datePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis())
       // myCalendar.add(Calendar.MONTH, +1)
        datePicker.getDatePicker().setMaxDate(myCalendar.getTimeInMillis())

        selectDate_et.setOnClickListener {
            dateSelection=1
            datePicker.show()
        }

        startDate_ll.setOnClickListener {
            dateSelection=2
            datePicker.show()
        }
        startDatePOB_et.setOnClickListener {
            dateSelection=2
            datePicker.show()
        }

        endDate_ll.setOnClickListener {
            dateSelection=3
            datePicker.show()
        }

        endDatePOB_et.setOnClickListener {
            dateSelection=3
            datePicker.show()
        }

        assignStockist.setOnClickListener {
            openCloseBottomSheet()
        }

        selectParty_et.setOnClickListener {
            if(isManager){
                if(selectTeam_et.text.toString().isEmpty()){
                    generalClass.showSnackbar(it,"Select Team first")
                    return@setOnClickListener }
            }
            selectionType=2
            if(docRetail_switch?.isChecked == true)
            {
                bottomSheetTitle_tv?.setText("Select Doctor")
                if(doctorListArray.size<=0)
                {
                    alertClass?.commonAlert("This route has no doctor","")
                    return@setOnClickListener
                }
            }
            else{
                bottomSheetTitle_tv?.setText("Select Retailer")
                if(retailerListArray.size<=0)
                {
                    alertClass?.commonAlert("This route has no Retailer","")
                    return@setOnClickListener
                }
            }

            openCloseModel()
        }


        val coroutine= CoroutineScope(
            Dispatchers.IO).launch {
            val filterOne = async {
                var listStokist = SplashActivity.staticSyncData?.retailerList?.filter { s -> s.type == "STOCKIST" }
                if (listStokist != null) {
                    for(stockist in listStokist) {
                        val data =IdNameBoll_model()
                        data.id= stockist.retailerId.toString()
                        data.city= stockist.cityName.toString()
                        data.name= stockist.shopName.toString()
                        stokistArray.add(data)
                    }
                }}
            val filterTwo = async {
                val string = Gson().toJson(SplashActivity.staticSyncData)
                val data= Gson().fromJson(string, SyncModel.Data::class.java)
                mainProductList.addAll(data.productList.filter { s -> (s.productType==1) } as ArrayList<SyncModel.Data.Product>)
                unSelectedProductList=ArrayList(mainProductList)
            }
            val filterThree = async {
                val getSchemeList= SplashActivity.staticSyncData?.schemeList
                val filterByTypeSchemeList= getSchemeList?.filter { data -> (data?.schemeFor=="S" || data?.schemeFor=="H") }

                val getDocDetail: SyncModel.Data.Doctor? = SplashActivity.staticSyncData?.doctorList?.find { it.doctorId == PresentEDetailingFrag.doctorIdDisplayVisual }

                getSchemeList?.clear()
                filterByTypeSchemeList?.sortedBy { it.schemeFor }?.let { getSchemeList?.addAll(it) }

                getSchemeList?.let { passingSchemeList.addAll(it)}

                getSchemeList?.forEachIndexed { indexH, hElement ->

                    val separated: Array<String>? = hElement.schemeForId?.split(",")?.toTypedArray()
                    val event: String? = separated?.find { it ==getDocDetail?.fieldStaffId?.toString() }

                    if(hElement.schemeFor.equals("H") && event!="")
                    {
                        getSchemeList?.forEachIndexed { indexS, SElement ->

                            val separated: Array<String>? = SElement.schemeForId?.split(",")?.toTypedArray()
                            val event: String? = separated?.find { it ==getDocDetail?.stateId?.toString() }
                            if(SElement.schemeFor.equals("S") && event!="")
                            {
                                if(hElement.productId==SElement.productId) {
                                    passingSchemeList.removeAt(indexS)
                                }
                            }
                        }
                    }
                }
            }
            val filterFour = async {
                SplashActivity.staticSyncData?.doctorList?.let   { doctorListArray.addAll(it) }
                SplashActivity.staticSyncData?.retailerList?.let { retailerListArray.addAll(it) }
                SplashActivity.staticSyncData?.fieldStaffTeamList?.let { teamsList.addAll(it) }
            }

            filterOne.await()
            filterTwo.await()
            filterThree.await()
            filterFour.await()
            }
        coroutine.invokeOnCompletion {
            runOnUiThread {
                checkRecyclerView_rv.layoutManager= LinearLayoutManager(this)
                checkRecyclerView_rv.adapter = CheckboxSpinnerAdapter(stokistArray, this)
                checkRecyclerView_rv.setNestedScrollingEnabled(false);
            }
        }

        pobProduct_btn.setOnClickListener({
            callPobSelectAlert()
        })

        docRetail_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.darkBlue))
                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.gray))
            } else {
                retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.darkBlue))
                doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.gray))
            }
            selectTeam_et.setText("")
            selectParty_et.setText("")
        }

        selectTeam_et?.setOnClickListener({
            selectionType=1
            bottomSheetTitle_tv?.setText("Select Team")
            openCloseModel()
        })

        doctorSearch_et?.addTextChangedListener(filterTextWatcher)

        close_selection_imv.setOnClickListener({ bottomSheetDocRetSelect.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        submitBtn.setOnClickListener {
           /* if(pobNumber_et.text.toString().isEmpty())
            {  alertClass.commonAlert("OOPS!","Something went wrong please try again later")
               return@setOnClickListener
            }*/
             if(selectDate_et.text.toString().isEmpty())
            {  alertClass.commonAlert("","Please select POB date")
                return@setOnClickListener
            }
            else if(selectParty_et.text.toString().isEmpty())
            {  alertClass.commonAlert("","Please select Party")
                return@setOnClickListener
            }
            else if(remarkPOB_Et.text.toString().isEmpty())
            {
                remarkPOB_Et.requestFocus()
                remarkPOB_Et.setError("Required")
                return@setOnClickListener
            }
            submitPobModel()
        }

        selectDate_et.setText(generalClass.getCurrentDate())
        startDatePOB_et.setText(generalClass.getCurrentDate())
        endDatePOB_et.setText(generalClass.getCurrentDate())

        filterPreviousBtn.setOnClickListener {
            val date1 =  SimpleDateFormat("MM/dd/yyyy").parse(startDatePOB_et.text.toString())
            val date2 =  SimpleDateFormat("MM/dd/yyyy").parse(endDatePOB_et.text.toString())
            val compareTwo= date2.compareTo(date1)
            if(compareTwo<0) {
                generalClass.showSnackbar(it,"End date is greater then start date")
                return@setOnClickListener
            }
            fetchPreviousPOB_API()
        }

        createNew_btn.setOnClickListener {
            mode=1
          //  setEditShow(2)
            setToDefault()
            createNew_btn.visibility=View.GONE
        }

        fetchPreviousPOB_API()
    }

    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            adapter?.getFilter()?.filter(s.toString())
        }
        override fun afterTextChanged(s: Editable) {}
    }

    fun openCloseBottomSheet()
    {
        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state
    }


    fun getPobNumber(){
        var call: Call<TeamsModel>? = apiInterface?.getPobNumber("bearer " + HomePage.loginModelHomePage.accessToken) as? Call<TeamsModel>
        call?.enqueue(object : Callback<TeamsModel?> {
            override fun onResponse(call: Call<TeamsModel?>?, response: Response<TeamsModel?>) {
                Log.e("getPobNumber_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    pobNumber_et.setText(response.body()?.getData()?.pobNo)
                }
                else
                { }
            }

            override fun onFailure(call: Call<TeamsModel?>, t: Throwable?) {
                call.cancel()
            }
        })
    }

    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateCalendar()
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    fun updateCalendar()
    {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if(dateSelection==1) {
            selectDate_et.setText(sdf.format(myCalendar.getTime()))
        }
            else if(dateSelection==2)  startDatePOB_et.setText(sdf.format(myCalendar.getTime()))
             else
             {
                 endDatePOB_et.setText(sdf.format(myCalendar.getTime()))
             }
    }

    override fun onChangeArray(
        passingArrayList: ArrayList<IdNameBoll_model>,
        isUpdate: Boolean,
        selectionType: Int
    ) {
        stokistArray= ArrayList<IdNameBoll_model>()
        stokistArray.addAll(passingArrayList)
        stokistArray.forEachIndexed { index, element ->
            if(element.isChecked) {
                selectedStockist=element
                stockistName.visibility=View.VISIBLE
                stockistName.text="Stockist name - "+element.name }
            element.isChecked=false
            stokistArray.set(index,element)
        }
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)  bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    fun callPobSelectAlert()
    {
        val dialogBuilder = AlertDialog.Builder(this, R.style.my_dialog)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.pobcreatealert, null)

        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 25)
        alertDialog.getWindow()?.setBackgroundDrawable(inset)

        val wmlp: WindowManager.LayoutParams? = alertDialog.getWindow()?.getAttributes()

        wmlp?.gravity = Gravity.TOP or Gravity.RIGHT


        val closePob_iv = dialogView.findViewById<View>(R.id.closePob_iv) as ImageView
        val okPob_iv = dialogView.findViewById<View>(R.id.okPob_iv) as TextView
        val filterRv = dialogView.findViewById<View>(R.id.filterRv) as RecyclerView
        val pobProduct_rv = dialogView.findViewById<View>(R.id.pobProduct_rv) as RecyclerView
        pobProduct_rv.layoutManager=LinearLayoutManager(this)
        filterRv.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))

        val productSearch_et = dialogView.findViewById<View>(R.id.productSearch_et) as EditText
        productSearch_et?.addTextChangedListener(filterTextPobWatcher)

        var categoryList:ArrayList<String> =ArrayList<String>()

        for(categoryName in mainProductList)
        {
            categoryName.categoryName?.let { categoryList.add(it) }
        }

        val uniqueValues: HashSet<String> = HashSet(categoryList)
        val categoryListFiltered :ArrayList<CommonModel.FilterModel> =ArrayList<CommonModel.FilterModel>()

        for(categoryName in uniqueValues)
        {
            val filterModel= CommonModel.FilterModel()
            filterModel.categoryName= categoryName.toString()
            categoryListFiltered.add(filterModel)
        }


        val pobProductSelectAdapter=
            PobProductAdapter(unSelectedProductList, passingSchemeList,this,1,productSearch_et)
        pobProduct_rv.adapter= pobProductSelectAdapter

        val filterAdapter= ButtonFilterAdapter(categoryListFiltered, pobProductSelectAdapter)
        filterRv.adapter=filterAdapter

        productSearch_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pobProductSelectAdapter?.getFilter()?.filter(s.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        okPob_iv.setOnClickListener{
            runOnUiThread{
                generalClass.hideKeyboard(this,it)
            }
            pobProductSelectAdapter.setSelction()
            alertDialog.dismiss()
        }

        closePob_iv.setOnClickListener{

            alertDialog.dismiss()
        }
        alertDialog.show()

    }

    val filterTextPobWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            pobProductSelectAdapter?.getFilter()?.filter(s.toString())
        }
        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onClickButtonProduct(selectedList: ArrayList<SyncModel.Data.Product>, type: Int) {
        if(selectedList.size==0) return
        if(type==1)
        {
            alertClass.showProgressAlert("")
            val runnable = java.lang.Runnable {

                for ((index,selected) in selectedList.withIndex())
                {
                    if(selected?.notApi?.insertedProductId!=0)
                    {
                        selected.notApi.isSaved=true
                        selectedProductList.add(selected)

                        val hashSet = HashSet<SyncModel.Data.Product>()
                        hashSet.addAll(selectedProductList)
                        selectedProductList.clear()
                        selectedProductList.addAll(hashSet)

                        // unSelectedProductList.removeAt(index)
                    }
                }
                runOnUiThread {

                    // pobProductSelectAdapter.notifyDataSetChanged()
                    setPobAdapter()
                    //pobProduct_rv.scrollToPosition(0)
                    Handler(Looper.getMainLooper()).postDelayed({
                        //   closeBottomSheet()
                        alertClass.hideAlert()
                    }, 1)
                }
            }
            Thread(runnable).start()
        }
        else
        {

        }
    }

    fun setPobAdapter()
    {
        runOnUiThread{
            /*   pobProductSelectAdapter=PobProductAdapter(unSelectedProductList, passingSchemeList,this)
               pobProduct_rv.adapter= pobProductSelectAdapter*/

            selectedPobAdapter= SelectedPobAdapter(selectedProductList,this,this,this,checkIsDcrSave)
            selectedPob_rv.adapter= selectedPobAdapter

            calculateTotalProduct()
        }
    }

    fun calculateTotalProduct()
    {
        val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }
        var calculation=0.0
        for(data in filterSelectecd)
        { calculation= data.notApi.amount?.plus(calculation)!! }
        totalProductPrice_tv.setText("Grand Total: "+String.format("%.2f", calculation))
    }

    override fun onClickButtonProduct(productModel: SyncModel.Data.Product, positon: Int) {
        for ((index,data) in unSelectedProductList?.withIndex()!!)
        {

            if(productModel?.productId==data.productId)
            {
                productModel.notApi=SyncModel.Data.Product.NotApiData()
                unSelectedProductList?.set(index, productModel)
                selectedProductList.removeAt(positon)

                runOnUiThread{
                    setPobAdapter()
                    alertClass?.hideAlert()
                }
            }

        }    }

    override fun onClickEdit(productModel: SyncModel.Data.Product, positon: Int) {
    }

    fun openCloseModel()
    {
        doctorSearch_et?.setText("")

        adapter =BottomSheetDoctorAdapter()
        doctorList_rv?.setLayoutManager(GridLayoutManager(this, 3))
        doctorList_rv?.adapter = adapter
        adapter.notifyDataSetChanged()

        val state =
            if (bottomSheetDocRetSelect.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDocRetSelect.state = state
    }

    inner class BottomSheetDoctorAdapter() :
        RecyclerView.Adapter<BottomSheetDoctorAdapter.MyViewHolder>(),
        Filterable {

        var filteredDataTeams: java.util.ArrayList<SyncModel.Data.FieldStaffTeam> = teamsList as java.util.ArrayList<SyncModel.Data.FieldStaffTeam>
        var filteredDataDoctor: java.util.ArrayList<SyncModel.Data.Doctor> = doctorListArray as java.util.ArrayList<SyncModel.Data.Doctor>
        var filteredDataRetailer: java.util.ArrayList<SyncModel.Data.Retailer> = retailerListArray as java.util.ArrayList<SyncModel.Data.Retailer>


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
            if(selectionType==1)
            {

              val modeldata = filteredDataTeams?.get(position)
              holder.headerDoctor_tv.setText(modeldata?.fullName)
              holder.route_tv.setText("Head Quater Name- " + modeldata?.headQuaterName)
              holder.speciality_tv.visibility=View.GONE


                holder.parent_cv.setOnClickListener({
                    selectTeam_et?.setText((modeldata?.fullName))
                    bottomSheetDocRetSelect.setState(BottomSheetBehavior.STATE_COLLAPSED)
                    modeldata.fieldStaffId?.let { it1 -> applySelectionFilter(it1) }

                })
            }

            else if(selectionType==2)
            {
                if( docRetail_switch?.isChecked==true)
                {
                    val modeldata = filteredDataDoctor?.get(position)
                    holder.headerDoctor_tv.setText(modeldata?.doctorName)
                    holder.route_tv.setText("Route- " + modeldata?.routeName)
                    holder.speciality_tv.setText("Speciality- " + modeldata?.specialityName)

                    holder.parent_cv.setOnClickListener({
                        selectParty_et?.setText((modeldata?.doctorName))
                        bottomSheetDocRetSelect.setState(BottomSheetBehavior.STATE_COLLAPSED)
                        selectedDoctorId= modeldata?.doctorId!!

                    })
                }
                else{
                    val modeldata = filteredDataRetailer?.get(position)
                    holder.headerDoctor_tv.setText(modeldata?.shopName)
                    holder.route_tv.setText("Route- " + modeldata?.routeName)
                    holder.speciality_tv.setText("Headquater- " + modeldata?.headQuaterName)

                    holder.parent_cv.setOnClickListener({
                        selectParty_et?.setText((modeldata?.shopName))
                        bottomSheetDocRetSelect.setState(BottomSheetBehavior.STATE_COLLAPSED)
                        selectedDoctorId= modeldata?.retailerId!!
                    })
                }
            }
        }
        override fun getItemCount(): Int
        {
            if(selectionType==1)
                return filteredDataTeams?.size
            else
                if(docRetail_switch?.isChecked == true) return filteredDataDoctor?.size
                else return filteredDataRetailer.size
        }

        //-------------------------------------filter list using text input from edit text
        override fun getFilter(): Filter? {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    if(selectionType==1)
                    { filteredDataTeams = results.values as java.util.ArrayList<SyncModel.Data.FieldStaffTeam>
                    }
                    else
                    {
                        if(docRetail_switch?.isChecked == true)
                        {
                            filteredDataDoctor = results.values as java.util.ArrayList<SyncModel.Data.Doctor>
                        }
                        else
                        {
                            filteredDataRetailer= results.values as java.util.ArrayList<SyncModel.Data.Retailer>
                        }
                    }
                    notifyDataSetChanged()
                }
                override fun performFiltering(constraint: CharSequence): FilterResults? {
                    var constraint = constraint
                    val results = FilterResults()
                    if(selectionType==1)
                    {
                        val FilteredArrayNames: java.util.ArrayList<SyncModel.Data.FieldStaffTeam> =
                            java.util.ArrayList()
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


                    else {
                        constraint = constraint.toString().lowercase()

                        if (docRetail_switch?.isChecked == true)
                        {
                            val FilteredArrayNames: java.util.ArrayList<SyncModel.Data.Doctor> =
                                java.util.ArrayList()

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
                            val FilteredArrayNames: java.util.ArrayList<SyncModel.Data.Retailer> =
                                java.util.ArrayList()
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

    fun applySelectionFilter(id:Int)
    {
        selectParty_et.setText("")
        if (docRetail_switch?.isChecked == true)
        {
            doctorListArray.clear()
            val filterRouteUsingFieldStaff= SplashActivity.staticSyncData?.doctorList?.filter {  s -> s.fieldStaffId == id }
            filterRouteUsingFieldStaff?.let { doctorListArray.addAll(it) }
        }
        else
        {
            retailerListArray.clear()
            val filterRetailerUsingFieldStaff= SplashActivity.staticSyncData?.retailerList?.filter {  s -> s.fieldStaffId == id }
            filterRetailerUsingFieldStaff?.let { retailerListArray.addAll(it) }

        }
    }

    fun submitPobModel()
    {
        var pobSaveModel= CreatePOBModel.Data.pobObject()
        if(mode==2){
            if(globalSaveModel!=null)
            {
                pobSaveModel = globalSaveModel!!
            }
        }
        else{
            pobSaveModel.pobDate=generalClass.getCurrentDateTimeApiForamt()
            pobSaveModel.strPOBDate=generalClass.getCurrentDateTimeApiForamt()
        }

        pobSaveModel.partyId= selectedDoctorId
        pobSaveModel.employeeId= HomePage.loginModelHomePage.empId
        pobSaveModel.empId= HomePage.loginModelHomePage.empId
        pobSaveModel.remark=remarkPOB_Et.text.toString()
        pobSaveModel.pobNo=pobNumber_et.text.toString()
        pobSaveModel.isProductWisePOB=true
        if (docRetail_switch?.isChecked == true) pobSaveModel.pobType="DOCTOR"
        else pobSaveModel.pobType="RETAILER"
        pobSaveModel.mode=mode

        val filterSelectecd=selectedProductList.filter { s -> (s.notApi.isSaved==true) }

        if(filterSelectecd.size==0) {
            alertClass?.commonAlert("","Please fill at least one POB")
            return
        }

        pobSaveModel.pobDetailList= ArrayList()
        for(dataObj in filterSelectecd)
        {
            val pobObje=DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList()
            pobObje.productId=dataObj.notApi.insertedProductId
            pobObje.rate=dataObj.notApi.rate
            pobObje.qty=dataObj.notApi.qty
            pobObje.amount=dataObj.notApi.amount
            pobObje.schemeId=dataObj.notApi.schemeId
            pobObje.totalQty=dataObj.notApi.totalQty
            pobObje.freeQty=dataObj.notApi.freeQty
            pobObje.pobId=dataObj.notApi.pobId
            pobObje.pobNo=dataObj.notApi.pobNo
            pobSaveModel.pobDetailList?.add(pobObje)
        }
        if(filterSelectecd.size!=0)
        {
            val jsonObj= JSONObject(SplashActivity.staticSyncData?.configurationSetting)
            val checkStockistRequired=jsonObj.getInt("SET014")
            if(checkStockistRequired==1)
            {
                alertClass.commonAlert("Stockist unselected","Please assign stockist in POB section")
                return
            }
        }

        if(selectedStockist.id!=null && selectedStockist.id!="" && filterSelectecd.size!=0) pobSaveModel.stockistId=selectedStockist.id.toInt()


        if(!generalClass.isInternetAvailable())
        {
            saveIfServerNotWork(pobSaveModel)
        }
        else
        {
            submitDCR_API(pobSaveModel)
        }

    }

    fun saveIfServerNotWork(pobSaveModel: CreatePOBModel.Data.pobObject)
    {
        val min = 10
        val max = 90
        val random = Random().nextInt(max - min + 1) + min
        pobSaveModel.randomNumber=random
        dbBase.insertOrUpdateSaveAPI(random, Gson().toJson(pobSaveModel),"createOnlyPOB")
        alertClass.commonAlert("","Offline data save successfully")
        setToDefault()
    }


    //Submit pob to server
    private fun submitDCR_API(pobSaveModel: CreatePOBModel.Data.pobObject)
    {
        alertClass.showProgressAlert("")
        apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<GenerateOTPModel>? = apiInterface?.submitPOB(
            "bearer " + HomePage.loginModelHomePage.accessToken, pobSaveModel
        ) as? Call<GenerateOTPModel>
        call?.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(call: Call<GenerateOTPModel?>?, response: Response<GenerateOTPModel?>) {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty()==true) {
                    alertClass.commonAlert("",response.body()?.getData()?.message.toString())
                    setToDefault()
                    callServerDocRetailerApi()
                }
                else {
                    alertClass.commonAlert("",response.body()?.getErrorObj()?.errorMessage.toString())
                    saveIfServerNotWork(pobSaveModel)
                }
                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
                saveIfServerNotWork(pobSaveModel)
            }
        })
    }

    // api to fetch previous pob using selected date
    private fun fetchPreviousPOB_API()
    {
        var format = SimpleDateFormat("dd/MM/yyyy")
        val startDate = format.parse(startDatePOB_et.text.toString())
        val endDate = format.parse(endDatePOB_et.text.toString())

        format = SimpleDateFormat("MM/dd/yyyy")
        val inputDate = format.format(startDate)
        val outputDate = format.format(endDate)

        alertClass.showProgressAlert("")
       // progressBar_pb.visibility=View.VISIBLE
        apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<CreatePOBModel>? = apiInterface?.fetchPreviousPOB(
            "bearer " + HomePage.loginModelHomePage.accessToken, inputDate,outputDate) as? Call<CreatePOBModel>
        call?.enqueue(object : Callback<CreatePOBModel?> {
            override fun onResponse(call: Call<CreatePOBModel?>?, response: Response<CreatePOBModel?>) {
               // Log.e("fetchPreviousPOB_api", Gson().toJson(response.body()))
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty()==true) {

                    if(response?.body()?.getData()?.pobList?.size!=0)
                    {
                        noData_tv.visibility=View.GONE
                        pobApiList_rv.visibility=View.VISIBLE
                        pobApiList_rv.adapter= response.body()?.getData()
                            ?.let { PobApiListAdapter(it?.pobList,this@CreatePobActivity,this@CreatePobActivity) }
                    }
                    else
                    {
                        noData_tv.visibility=View.VISIBLE
                        pobApiList_rv.visibility=View.GONE
                    }
                }
                else { alertClass.commonAlert("",response.body()?.getErrorObj()?.errorMessage.toString()) }
                alertClass.hideAlert()
               // progressBar_pb.visibility=View.GONE
            }

            override fun onFailure(call: Call<CreatePOBModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
                progressBar_pb.visibility=View.GONE
            }
        })
    }

    //Fetching pob using id available in Pob list. Works on edit button
    private fun fetchPOB_ById_API(id:Int)
    {
        alertClass.showProgressAlert("")
        apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<CreatePOBModel>? = apiInterface?.getPOBByID(
            "bearer " + HomePage.loginModelHomePage.accessToken, id) as? Call<CreatePOBModel>
        call?.enqueue(object : Callback<CreatePOBModel?> {
            override fun onResponse(call: Call<CreatePOBModel?>?, response: Response<CreatePOBModel?>) {
                Log.e("fetchPOB_ById_API", Gson().toJson(response.body()))
                if (response.code() == 200 && response.body()
                        ?.getErrorObj()?.errorMessage?.isEmpty()==true)
                {
                    val modelObj=response.body()?.getData()?.pobData
                    if (modelObj != null) {
                        globalSaveModel=modelObj
                    }

                    selectTeam_et.setText("")

                    pobNumber_et.setText(modelObj?.pobNo)
                    remarkPOB_Et.setText(modelObj?.remark)
                    stockistName.text="Stockist name - "+modelObj?.stockistName
                    if(modelObj?.stockistName?.isEmpty()==false) stockistName.visibility=View.VISIBLE
                     val filterDate=generalClass.convertApiDateTime_toDate(modelObj?.pobDate)
                    if(filterDate.isEmpty()==false) selectDate_et.setText(filterDate)
                    if(modelObj?.pobType.equals("DOCTOR")) {
                        docRetail_switch.isChecked = true
                        doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(this@CreatePobActivity, R.color.darkBlue))
                        retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(this@CreatePobActivity, R.color.gray))
                    }
                    else {
                        docRetail_switch.isChecked = false
                        doctorHeader_tv.setTextColor(ContextCompat.getColorStateList(this@CreatePobActivity, R.color.gray))
                        retailerHeader_tv.setTextColor(ContextCompat.getColorStateList(this@CreatePobActivity, R.color.darkBlue))
                    }

                    if( modelObj?.pobDetailList?.size!=0)
                    {
                        selectedProductList.clear()
                        for (pobProducts in modelObj?.pobDetailList!!)
                        {
                            for((index,availableProduct) in unSelectedProductList.withIndex())
                            {
                                if(availableProduct.productId==pobProducts.productId)
                                {
                                    availableProduct.notApi.amount=pobProducts.amount
                                    availableProduct.notApi.rate=pobProducts.rate
                                    availableProduct.notApi.qty=pobProducts.qty
                                    availableProduct.notApi.totalQty=pobProducts.totalQty
                                    availableProduct.notApi.scheme=pobProducts.schemeNameWithQty
                                    availableProduct.notApi.schemeId=pobProducts.schemeId
                                    availableProduct.notApi.salesQty=pobProducts.schemeSalesQty
                                    availableProduct.notApi.salesQtyMain=pobProducts.schemeSalesQty
                                    availableProduct.notApi.freeQty=pobProducts.freeQty
                                    availableProduct.notApi.freeQtyMain=pobProducts.schemeFreeQty
                                    availableProduct.notApi.insertedProductId=pobProducts.productId
                                    availableProduct.notApi.isSaved=true
                                    availableProduct.notApi.pobId= pobProducts?.pobId!!
                                    availableProduct.notApi.pobNo= pobProducts?.pobNo.toString()

                                    unSelectedProductList.set(index,availableProduct)
                                    selectedProductList.add(availableProduct)

                                }
                            }
                        }
                        setPobAdapter()
                    }
                    selectedDoctorId= modelObj.partyId!!
                    createNew_btn.visibility=View.VISIBLE
                    calculateTotalProduct()
                    selectParty_et.setText(modelObj?.partyName)
                }
                else { alertClass.commonAlert("",response.body()?.getErrorObj()?.errorMessage.toString()) }
                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<CreatePOBModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    //Interfae for passing pob id from pob list adapter
    override fun passid(id: Int, selectionType: Int)
    {
        mode=2
        id?.let { fetchPOB_ById_API(it) }
       // setEditShow(selectionType)
    }

    //use for set layout to its default
    fun setToDefault()
    {
        mode=1
        selectDate_et.setText(generalClass.getCurrentDate())
        selectTeam_et.setText("")
        selectParty_et.setText("")
        remarkPOB_Et.setText("")
        totalProductPrice_tv.setText("Order total: 00")
        stockistName.visibility=View.GONE
        pobNumber_et.setText("")

       for((index,unSelected) in unSelectedProductList.withIndex())
       {
           if(unSelected.notApi.isSaved) {
               unSelected.notApi.isSaved = false
               unSelected.notApi.qty=null
               unSelected.notApi.totalQty=null
               unSelectedProductList.set(index,unSelected)
           }

       }

        selectedProductList = ArrayList()
        setPobAdapter()
        getPobNumber()
    }

    override fun onResume() {
        super.onResume()
        alertClass = AlertClass(this)
        createConnectivity(this)

    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }


    /*  fun checkCurrentDCR_API() {
        var format = SimpleDateFormat("dd/MM/yyyy")
        val dateIs = format.parse(selectDate_et.text.toString())

        format = SimpleDateFormat("MM/dd/yyyy")
        val inputDate = format.format(dateIs)

        val  apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<GetDcrToday>? = apiInterface?.checkDCR__API(
            "bearer " + HomePage.loginModelHomePage.accessToken,
            HomePage.loginModelHomePage.empId,
            inputDate
        )
        call?.enqueue(object : Callback<GetDcrToday?> {
            override fun onResponse(call: Call<GetDcrToday?>?, response: Response<GetDcrToday?>) {
                Log.e("checkCC_api", response.code().toString() + "")
                alertClass.hideAlert()

                if (response.body().toString().isEmpty() || response.body()?.errorObj?.errorMessage?.isEmpty()==false) {
                    alertClass.commonAlert("","Something went wrong please try again later")
                } else {

                    val dcrData=response.body()?.data?.dcrData

                    if (dcrData?.dcrId == 0) {
                        alertClass.commonAlert("","Dcr not submitted. Please submit dcr first")
                        selectDate_et.setText("")
                    } else {


                    }
                }

            }

            override fun onFailure(call: Call<GetDcrToday?>, t: Throwable?) {
                generalClass.checkInternet()
                alertClass.hideAlert()
                call.cancel()
            }
        })
    }*/

    /*fun setEditShow(selectionType: Int)
    {
        if(selectionType==1) {
            submitBtn.visibility = View.INVISIBLE
            assignStockist.visibility=View.GONE
            pobProduct_btn.visibility=View.GONE
            remarkPOB_Et.isEnabled=false
            selectTeam_et.isEnabled=false
            selectParty_et.isEnabled=false
            docRetail_switch.isEnabled=false
            docRetail_switch.isClickable=false
            selectDate_et.isEnabled=false
            if(::selectedPobAdapter.isInitialized)
            {
                selectedPobAdapter.isClickable=false
            }
        }
        else {
            submitBtn.visibility = View.VISIBLE
            assignStockist.visibility=View.VISIBLE
            pobProduct_btn.visibility=View.VISIBLE
            selectTeam_et.isEnabled=true
            selectParty_et.isEnabled=true
            docRetail_switch.isEnabled=true
            docRetail_switch.isClickable=true
            selectDate_et.isEnabled=true
            if(::selectedPobAdapter.isInitialized)
            {
                selectedPobAdapter.isClickable=true
            }
        }
    }*/

    fun callServerDocRetailerApi()
    {
        val coroutineScope= CoroutineScope(Dispatchers.IO + generalClass.coroutineExceptionHandler).launch {
            try
            {
                val getDocCall= async { getDocCallAPI() }
                getDocCall.await()
            }
            catch (e:Exception)
            { }
        }
        coroutineScope.invokeOnCompletion {
            coroutineScope.cancel()
        }
    }

}