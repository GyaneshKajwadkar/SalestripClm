package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.CommonListGetClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.DisplayVisualFragment.Companion.doctorIdDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_submit_edetailing.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.bottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_visualads.close_imv
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.common_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SubmitE_DetailingActivity : BaseActivity(), IdNameBoll_interface {

    var visualSendModel= ArrayList<VisualAdsModel_Send>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var sampleArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var selectionType=0
    var selectedPurposeID=0
    var dcrId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_edetailing)


        val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(this,
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getWorkTypeForSpinner())
        visitPurpose_spinner.setAdapter(adapterVisit)

        visitPurpose_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              if(position!=0)
                selectedPurposeID = CommonListGetClass().getWorkTypeForSpinner()[position].workId
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        dcrId= PreferenceClass(this).getPref("dcrId").toInt()

        checkRecyclerView_rv.layoutManager=LinearLayoutManager(this)
        workingWithRv.layoutManager=LinearLayoutManager(this)
        sample_rv.layoutManager=LinearLayoutManager(this)
        gift_rv.layoutManager=LinearLayoutManager(this)

        doctorName_tv.setText(intent.getStringExtra("doctorName"))

        if(intent.getIntExtra("doctorID",0)!=0) doctorIdDisplayVisual= intent.getIntExtra("doctorID",0)!!

        if(intent.getBooleanExtra("skip",false))
        {
            dbBase.deleteAllVisualAds()
            dbBase.deleteAllChildVisual()
        }



        visualSendModel = DatabaseHandler(this).getAllSubmitVisual()
        edetailing_rv.layoutManager=LinearLayoutManager(this)
        edetailing_rv.adapter=EdetallingAdapter()

        if(visualSendModel.size==0)notData_tv.visibility=View.VISIBLE

        back_iv.setOnClickListener({onBackPressed()})
        workingWith_tv.setOnClickListener({openCloseModel(1)
            selectHeader_tv.setText("Select Work with")})
        clickSample_tv.setOnClickListener({openCloseModel(2)
            selectHeader_tv.setText("Select Samples")})
        giftClick_tv.setOnClickListener({openCloseModel(3)
            selectHeader_tv.setText("Select Gifts")})


        close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

         val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)

        var listSample = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Sample"}
      //  var listSample = Sample!!.filter { s -> s.actualBalanceQty != 0}
        var Gift = quantityModel.employeeSampleBalanceList!!.filter { s -> s.productType == "Gift"}
        var listGift = Gift!!.filter { s -> s.actualBalanceQty != 0}

        for(workWith in staticSyncData?.data?.workingWithList!!)
        {
            val data =IdNameBoll_model()
            data.id=workWith.emailId
            data.name=workWith.fullName
            workWithArray.add(data)
        }

        for(sample in listSample)
        {
            val data =IdNameBoll_model()
            data.id= sample.productId.toString()
            data.name=sample?.productName!!
            data.availableQty=sample?.actualBalanceQty?.toInt()!!
            sampleArray.add(data)
        }

        for(gift in listGift)
        {
            val data =IdNameBoll_model()
            data.id= gift.productId.toString()
            data.name=gift?.productName!!
            data.availableQty=gift?.actualBalanceQty?.toInt()!!
            giftArray.add(data)
        }


        submitDetailing_btn.setOnClickListener({

            var firstSample=sampleArray!!.filter { s -> s.isChecked == true }
            var sampleQTy = firstSample!!.filter { s -> s.qty == 0}

            var firstGift=giftArray!!.filter { s -> s.isChecked == true }
            var giftyQTy = firstGift!!.filter { s -> s.qty == 0}

//            if(sampleQTy.size>0 ) {
//                alertClass.commonAlert("","Sample quantity not be zero")
//                return@setOnClickListener
//            }
            if(giftyQTy.size>0) {
                alertClass.commonAlert("","Gift quantity not be zero")
                return@setOnClickListener
            }

            val saveModel=getSaveData()

            val quantityModel=Gson().fromJson(dbBase.getApiDetail(3),CommonModel.QuantityModel.Data::class.java)
            var quantityArray=quantityModel.employeeSampleBalanceList!! as ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>

            for((index, model) in quantityArray!!.withIndex())
            {
                for (data in sampleArray)
                {
                    if(data.id.toInt()==model.productId && data.isChecked) {
                        model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                        quantityArray.set(index, model) }
                }
                for (data in giftArray)
                {
                    if(data.id.toInt()==model.productId && data.isChecked) {
                        model.actualBalanceQty = model.actualBalanceQty?.minus(data.qty)
                        quantityArray.set(index, model) }
                }
            }

            if(!GeneralClass(this).isInternetAvailable())
            {   dbBase.insertOrUpdateSaveAPI(dcrId, Gson().toJson(saveModel),"feedback")
                val commonModel=CommonModel.QuantityModel.Data()
                commonModel.employeeSampleBalanceList=quantityArray
                dbBase.addAPIData(Gson().toJson(commonModel),3)

                callRunnableAlert("Data save successfully")
            }
            else{ submitDcr(saveModel,quantityArray) }
        })
    }

    fun getSaveData():Send_EDetailingModel
    {

        var saveModel= Send_EDetailingModel()
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]

        saveModel.dcrId=dcrId
        saveModel.doctorId=doctorIdDisplayVisual
        saveModel.remark=remark_Et.text.toString()
        saveModel.followUpRemark=remark_Et.text.toString()
        saveModel.addedThrough="W"
        saveModel.visitPurpose=selectedPurposeID
        saveModel.productDetailCount=visualSendModel.size
        saveModel.empId=loginModelHomePage.empId
        saveModel.mode=1
        saveModel.dcrYear=year
        saveModel.dcrMonth=month
        saveModel.callTiming= if (c.get(Calendar.AM_PM)==Calendar.AM) "M" else "E"

        val workWithTemp=workWithArray!!.filter { s -> s.isChecked == true }
        var workWithStr=""
        for (data in workWithTemp)
        {    if(workWithStr!="") workWithStr=workWithStr.plus(",")
            workWithStr=workWithStr.plus(data.id)
        }
        saveModel.workWith=workWithStr

        val tempGiftList=giftArray!!.filter { s -> s.isChecked == true }
        var giftList=ArrayList<Send_EDetailingModel.GiftList>()
        for(data in tempGiftList)
        {
            var giftModel=Send_EDetailingModel.GiftList()
            giftModel.dcrId=dcrId
            giftModel.empId=loginModelHomePage.empId
            giftModel.qty=data.qty
            giftModel.productId=data.id.toInt()
            giftModel.type="GIFT"
            giftList.add(giftModel)
        }

        val tempSampleList=sampleArray!!.filter { s -> s.isChecked == true }
        var sampleList=ArrayList<Send_EDetailingModel.SampleList>()
        for(data in tempSampleList)
        {
            var sampleModel=Send_EDetailingModel.SampleList()
            sampleModel.dcrId=dcrId
            sampleModel.empId=loginModelHomePage.empId
            sampleModel.qty=data.qty
            sampleModel.productId=data.id.toInt()
            sampleList.add(sampleModel)
        }

        var productList=ArrayList<Send_EDetailingModel.ProductList>()
        for(data in visualSendModel)
        {
            var productModel=Send_EDetailingModel.ProductList()
            productModel.dcrId=dcrId
            productModel.doctorId=data.doctorId?.toInt()
            productModel.productId=data.brandId?.toInt()
            productModel.remark=data.feedback
            productList.add(productModel)
        }

        saveModel.giftList.addAll(giftList)
        saveModel.sampleList.addAll(sampleList)
        saveModel.productList.addAll(productList)
        saveModel.eDetailList.addAll(visualSendModel)
        saveModel.dcrDate=generalClass.getCurrentDateTimeApiForamt()
        return saveModel
    }

    fun openCloseModel(type:Int)
    {   selectionType=type

        if(type==1) {
            if (workWithArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            checkRecyclerView_rv.adapter = CheckboxSpinnerAdapter(workWithArray, this)
        }
        if(type==2) {
            if (sampleArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            checkRecyclerView_rv.adapter = CheckboxSpinnerAdapter(sampleArray, this)

        }
        if(type==3) {
            if (giftArray.size == 0) noDataCheckAdapter_tv.visibility = View.VISIBLE else noDataCheckAdapter_tv.visibility = View.GONE
            checkRecyclerView_rv.adapter = CheckboxSpinnerAdapter(giftArray, this)
        }

        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state
    }


    fun submitDcr(
        saveModel: Send_EDetailingModel,
        quantityArray: ArrayList<CommonModel.QuantityModel.Data.EmployeeSampleBalance>
    ) {
        alertClass?.showAlert("")
        var call: Call<JsonObject> = HomePage.apiInterface?.submitEdetailingApi("bearer " + loginModelHomePage.accessToken,saveModel) as Call<JsonObject>
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
                        val commonModel=CommonModel.QuantityModel.Data()
                        commonModel.employeeSampleBalanceList=quantityArray
                        dbBase.addAPIData(Gson().toJson(commonModel),3)
                        val jsonObjData:JsonObject = response.body()?.get("data") as JsonObject
                        callRunnableAlert(jsonObjData.get("message").asString)
                    } } }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass?.hideAlert()
                call.cancel()
            } }) }

    fun callRunnableAlert(message:String)
    {
        if(!intent.getBooleanExtra("skip",false)) dbBase.insertdoctorData(doctorIdDisplayVisual,generalClass.getCurrentDate())
        dbBase.deleteAllVisualAds()
        dbBase.deleteAllChildVisual()

        val r: Runnable = object : Runnable {
            override fun run() {
                if(AlertClass.retunDialog) {
                   if(intent.getBooleanExtra("skip",false))setResult(3)
                    else setResult(2)

                    finish()
                }}}
        alertClass.commonAlertWithRunnable("",message,r)
    }

//----------------------------------Show edetailing inner adapter------------------------------------
    inner class EdetallingAdapter() :
        RecyclerView.Adapter<EdetallingAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var brandName_headerTv: TextView = view.findViewById(R.id.brandName_headerTv)
            var startEndTime_tv: TextView = view.findViewById(R.id.startEndTime_tv)
            var feeback_et: EditText = view.findViewById(R.id.feeback_et)
            var ratingBar: RatingBar = view.findViewById(R.id.ratingBar)

        }
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.edetalling_feedback, parent, false)
            return MyViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int)
        {
            var objectDetailing=visualSendModel.get(position)
            holder.brandName_headerTv.setText(objectDetailing.brandName)

            val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val dateFormatterSet = SimpleDateFormat("HH:mm:ss")
            val startDate: Date = dateFormatter.parse(objectDetailing.startDate)
            val endDate: Date = dateFormatter.parse(objectDetailing.endDate)

            if(position==0)
            {
                detailingMainDateTime_tv.setText(dateFormatterSet.format(startDate)+" - "+dateFormatterSet.format(endDate))
            }

            val hours = objectDetailing.monitorTime / 3600;
            val minutes = (objectDetailing.monitorTime % 3600) / 60;
            val seconds = objectDetailing.monitorTime % 60;

            val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

           // holder.startEndTime_tv.setText(objectDetailing.brandWiseStartTime+"-"+objectDetailing.brandWiseStopTime)
            holder.startEndTime_tv.setText(timeString)

            holder.feeback_et.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { visualSendModel.get(position).feedback=p0.toString() }
            })

            holder.ratingBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
                override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                    visualSendModel.get(position).rating=p1
                }
            })
        }
        override fun getItemCount(): Int
        {
            return visualSendModel.size;
        }

    }

    override fun onChangeArray(
        passingArrayList: java.util.ArrayList<IdNameBoll_model>, isUpdate: Boolean) {
        if(selectionType==1)
        { workWithArray= ArrayList<IdNameBoll_model>()
          workWithArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = workWithArray!!.filter { s -> s.isChecked == true }
                workingWithRv.adapter =
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this,0,this)
            }
        }
        if(selectionType==2)
        { sampleArray= ArrayList<IdNameBoll_model>()
            sampleArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = sampleArray!!.filter { s -> s.isChecked == true }
                sample_rv.adapter =
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this, 1, this)
            }
        }
        if(selectionType==3)
        { giftArray= ArrayList<IdNameBoll_model>()
            giftArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = giftArray!!.filter { s -> s.isChecked == true }
                gift_rv.adapter =
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this, 1, this)
            }
        }



    }

}