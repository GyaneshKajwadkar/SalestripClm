package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.staticSyncData
import `in`.processmaster.salestripclm.adapter.CheckboxSpinnerAdapter
import `in`.processmaster.salestripclm.adapter.TextWithEditAdapter
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.interfaceCode.IdNameBoll_interface
import `in`.processmaster.salestripclm.models.IdNameBoll_model
import `in`.processmaster.salestripclm.models.SaveEdetailingToDB_Model
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.models.VisualAdsModel_Send
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_submit_edetailing.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.bottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_visualads.close_imv
import kotlinx.android.synthetic.main.checkbox_bottom_sheet.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SubmitE_DetailingActivity : AppCompatActivity(), IdNameBoll_interface {

    var db = DatabaseHandler(this)
    var visualSendModel= ArrayList<VisualAdsModel_Send>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var workWithArray=ArrayList<IdNameBoll_model>()
    var sampleArray=ArrayList<IdNameBoll_model>()
    var giftArray=ArrayList<IdNameBoll_model>()
    var selectionType=0;
    var selectedPurposeID=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_edetailing)

        var workinWithList= ArrayList<SyncModel.Data.WorkingWith>()
        var visitPurposeList= ArrayList<SyncModel.Data.WorkType>()

        var workingobj=SyncModel.Data.WorkingWith()
        var visitobj=SyncModel.Data.WorkType()

        workingobj.fullName="Select"
        visitobj.workType="Select"

        workinWithList?.add(workingobj)
        visitPurposeList?.add(visitobj)

        workinWithList?.addAll(staticSyncData?.data?.workingWithList!!)
        visitPurposeList?.addAll(staticSyncData?.data?.workTypeList!!)

        val adapterVisit: ArrayAdapter<SyncModel.Data.WorkType> = ArrayAdapter<SyncModel.Data.WorkType>(this,
            android.R.layout.simple_spinner_dropdown_item, visitPurposeList!!)
        visitPurpose_spinner.setAdapter(adapterVisit)

        visitPurpose_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              if(position!=0)
                selectedPurposeID = visitPurposeList[position].workId
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        checkRecyclerView_rv.layoutManager=LinearLayoutManager(this)
        workingWithRv.layoutManager=LinearLayoutManager(this)
        sample_rv.layoutManager=LinearLayoutManager(this)
        gift_rv.layoutManager=LinearLayoutManager(this)

        doctorName_tv.setText(intent.getStringExtra("doctorName"))

        visualSendModel = DatabaseHandler(this).getAllSubmitVisual()
        edetailing_rv.layoutManager=LinearLayoutManager(this)
        edetailing_rv.adapter=EdetallingAdapter()

        back_iv.setOnClickListener({onBackPressed()})
        workingWith_tv.setOnClickListener({openCloseModel(1)
            selectHeader_tv.setText("Select Work with")})
        clickSample_tv.setOnClickListener({openCloseModel(2)
            selectHeader_tv.setText("Select Samples")})
        giftClick_tv.setOnClickListener({openCloseModel(3)
            selectHeader_tv.setText("Select Gifts")})


        close_imv.setOnClickListener({   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)})

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)



        var listSample = staticSyncData?.data?.productList!!.filter { s -> s.allowSample == true}
        var listGift = staticSyncData?.data?.productList!!.filter { s -> s.productType == 3}


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
            data.name=sample.productName
            sampleArray.add(data)
        }

        for(gift in listGift)
        {
            val data =IdNameBoll_model()
            data.id= gift.productId.toString()
            data.name=gift.productName
            giftArray.add(data)
        }


        submitDetailing_btn.setOnClickListener({
         //   if(GeneralClass(this).isInternetAvailable())
            var saveModel= SaveEdetailingToDB_Model()
            saveModel.remark=remark_Et.text.toString()
            saveModel.purposeTovisit=selectedPurposeID
            saveModel.giftList.addAll(giftArray!!.filter { s -> s.isChecked == true })
            saveModel.sampleList.addAll(sampleArray!!.filter { s -> s.isChecked == true })
            saveModel.workWithList.addAll(workWithArray!!.filter { s -> s.isChecked == true })
            saveModel.visualArray.addAll(visualSendModel)

            val time = System.currentTimeMillis()
            db.insertOrUpdateAPI(time.toInt(), Gson().toJson(saveModel))

            db.deleteAllVisualAds()
            db.deleteAllChildVisual()
            finish()

        })
    }

    fun openCloseModel(type:Int)
    {   selectionType=type

        if(type==1)
            checkRecyclerView_rv.adapter=CheckboxSpinnerAdapter(workWithArray,this)
        if(type==2)
            checkRecyclerView_rv.adapter=CheckboxSpinnerAdapter(sampleArray,this)
        if(type==3)
            checkRecyclerView_rv.adapter=CheckboxSpinnerAdapter(giftArray,this)


        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_COLLAPSED
            else
                BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state
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
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this,0)
            }
        }
        if(selectionType==2)
        { sampleArray= ArrayList<IdNameBoll_model>()
            sampleArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = sampleArray!!.filter { s -> s.isChecked == true }
                sample_rv.adapter =
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this,1)
            }
        }
        if(selectionType==3)
        { giftArray= ArrayList<IdNameBoll_model>()
            giftArray.addAll(passingArrayList)
            if(isUpdate) {
                var sendingList = giftArray!!.filter { s -> s.isChecked == true }
                gift_rv.adapter =
                    TextWithEditAdapter(sendingList as ArrayList<IdNameBoll_model>, this,1)
            }
        }



    }

}