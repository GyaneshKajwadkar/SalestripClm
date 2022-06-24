package `in`.processmaster.salestripclm.activity

import DocManagerModel
import DoctorManagerSelector_Adapter
import IntegerInterface
import SelectedDocManList_adapter
import SelectorInterface
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.adapter.ImageSelectorAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.GenerateOTPModel
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.FileUtils
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.progress_view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.io.FileUtils.getFile
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MailActivity : BaseActivity(),SelectorInterface,IntegerInterface {

    var arrayListSelectorDoctor: ArrayList<DocManagerModel> = ArrayList()
    var arrayListSelectorTeams: ArrayList<DocManagerModel> = ArrayList()
    var selectedAdapeter :SelectedDocManList_adapter? = null
    var selectedAdapeterTeams :SelectedDocManList_adapter? = null
    var constructorList: ArrayList<DocManagerModel> = ArrayList()
    var constructorListTeam: ArrayList<DocManagerModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        selectManagers_cv.setOnClickListener({
            selectDoctorManager_alert(2)

            cc_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))
            selectTeams_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.black))
        })

        selectDoctors_cv.setOnClickListener({
            selectDoctorManager_alert(1)
            to_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.appColor))
            selectDoctor_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.black))
        })

        //  var model = Gson().fromJson(dbBase.getAllData(), SyncModel::class.java)

        for(item in SplashActivity.staticSyncData?.doctorList!!)
        {
            val selectorModel = DocManagerModel()
            selectorModel.name= item.doctorName.toString()
            selectorModel.routeName= item.routeName.toString()
            selectorModel.specialityName= item.specialityName.toString()
            selectorModel.id= item.doctorId!!
            arrayListSelectorDoctor.add(selectorModel)
        }

        selectedoctor_rv.setLayoutManager(GridLayoutManager(this, 5))
        recyclerView_teams.setLayoutManager(GridLayoutManager(this, 5))

        arrayListSelectorTeams= getTeamsApi(this@MailActivity,"Please wait...")

        var adapter= ImageSelectorAdapter(retriveAttachment(), this,false)
        attachment_rv.setLayoutManager(GridLayoutManager(this, 2))
        attachment_rv.adapter=adapter

        buttonSend.setOnClickListener({
            if(constructorList.size==0)
            {
                to_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                selectDoctor_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                return@setOnClickListener
            }
            /*  if(constructorListTeam.size==0)
              {
                  cc_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                  selectTeams_tv.setTextColor(ContextCompat.getColorStateList(this, R.color.zm_red))
                  return@setOnClickListener
              }*/
            if(subject_et.text.toString().isEmpty())
            {
                subject_et.requestFocus()
                subject_et.setError("Required")
                return@setOnClickListener
            }
            if(description_et.text.toString().isEmpty())
            {
                description_et.requestFocus()
                description_et.setError("Required")
                return@setOnClickListener
            }
            sendEmailApi()

        })


    }

    fun retriveAttachment(): ArrayList<ImageSelectorActivity.SendImage>? {
        val intent = getIntent()
        val args = intent.getBundleExtra("attachment")
        return args?.getSerializable("ARRAYLIST") as ArrayList<ImageSelectorActivity.SendImage>?
    }


    fun selectDoctorManager_alert(selectionType: Int)
    {
        if(alertDialog!=null)
        {
            alertDialog?.dismiss()
        }

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.selectionalert, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val list_rv= dialogView.findViewById<View>(R.id.list_rv) as RecyclerView
        val search_et= dialogView.findViewById<View>(R.id.search_et) as EditText
        val ok_btn= dialogView.findViewById<View>(R.id.ok_btn) as Button
        val headerTv= dialogView.findViewById<View>(R.id.headerTv) as TextView
        if(selectionType==1)headerTv.setText("Select Doctor")
            else headerTv.setText("Select Route")

        val layoutManager = LinearLayoutManager(this)
        list_rv.layoutManager=layoutManager
        var adapterView : DoctorManagerSelector_Adapter? = null

        if(selectionType==1)
        {
            adapterView= DoctorManagerSelector_Adapter(arrayListSelectorDoctor,this,selectionType)
        }
        else
        {
            adapterView= DoctorManagerSelector_Adapter(arrayListSelectorTeams,this,selectionType)
        }
        list_rv.adapter = adapterView

        search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int)
            {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            )
            {
                adapterView?.getFilter()?.filter(s.toString());
            }
        })

        ok_btn.setOnClickListener({
            alertDialog?.dismiss()
        })

        alertDialog?.show()
    }

    override fun selectorArray(docMangerList: ArrayList<DocManagerModel>, selectionType: Int)
    {
        if(selectionType==1)
        {
            arrayListSelectorDoctor=docMangerList;
        }
        else
        {
            arrayListSelectorTeams=docMangerList;
        }

        callSelectedAdapter(selectionType)

    }

    fun callSelectedAdapter(selectionType: Int)
    {
        if(selectionType==1)
        {
            constructorList= ArrayList()
            constructorList = arrayListSelectorDoctor.filter { s -> s.checked==true } as ArrayList<DocManagerModel>

//            for (item in arrayListSelectorDoctor) {
//                if(item.getChecked()!!)
//                {
//                    constructorList.add(item)
//                }
//            }

            selectedAdapeter=SelectedDocManList_adapter(constructorList,this,selectionType)
            selectedoctor_rv.adapter=selectedAdapeter
            selectedAdapeter?.notifyDataSetChanged()
        }
        else
        {
            constructorListTeam= ArrayList()

            constructorListTeam = arrayListSelectorTeams.filter { s -> s.checked==true } as ArrayList<DocManagerModel>

//            for (item in arrayListSelectorTeams) {
//                if(item.getChecked()!!)
//                {
//                    constructorListTeam.add(item)
//                }
//            }

            selectedAdapeterTeams=SelectedDocManList_adapter(constructorListTeam,this,selectionType)
            recyclerView_teams.adapter=selectedAdapeterTeams
            selectedAdapeterTeams?.notifyDataSetChanged()
        }

    }

    override fun passid(id: Int, selectionType: Int)
    {
        if(selectionType==1)
        {
            for ((iMain, itemMain) in arrayListSelectorDoctor.withIndex())
            {
                if(itemMain.id== id)
                {
                    itemMain.checked=false
                    arrayListSelectorDoctor.set(iMain,itemMain)
                    selectedAdapeter?.notifyDataSetChanged()
                }
            }
        }
        else
        {
            for ((iMain, itemMain) in arrayListSelectorTeams.withIndex())
            {
                if(itemMain.id== id)
                {
                    itemMain.checked=false
                    arrayListSelectorTeams.set(iMain,itemMain)
                    selectedAdapeterTeams?.notifyDataSetChanged()
                }
            }
        }
        callSelectedAdapter(selectionType)
    }

    //Get teams api
    fun sendEmailApi()
    {

        alertClass.showProgressAlert("")

        val selectedImage=retriveAttachment()
        val surveyImagesParts = selectedImage?.size?.let { arrayOfNulls<MultipartBody.Part>(it) }

        val arrAttachment = JSONArray()

        for ((index, value) in selectedImage!!.withIndex()) {

            val surveyBody: RequestBody = RequestBody.create(
                MediaType.parse("image/*"),
                value?.file
            )

            val objectAttachment = JSONObject()
            objectAttachment.put("docName",value?.file?.name.toString())
            arrAttachment.put(objectAttachment)

            surveyImagesParts?.set(index, MultipartBody.Part.createFormData("file"+index,
                value.file?.name.toString(),
                surveyBody)
            )
        }

        val paramObject = JSONObject()
        paramObject.put("to", "")
        paramObject.put("cc","" )
        paramObject.put("subject",subject_et.text.toString())
        paramObject.put("description",description_et.text.toString())
        paramObject.put("IsSelected", "false")
        paramObject.put("MailId", "0")
        paramObject.put("EmpId", "0")
        paramObject.put("IsSeen", "false")
        paramObject.put("entryBy", loginModelHomePage.empId)
        paramObject.put("mode", "1")
        paramObject.put("mailStatus", "S")
        paramObject.put("isFileAttached", "true")

        val arr = JSONArray()
        for( item in constructorList)
        {
            val arrayObject = JSONObject()
            arrayObject.put("sendBy",loginModelHomePage.empId)
            arrayObject.put("sendTo",item.id)
            arrayObject.put("sendType","To")
            arrayObject.put("isSeen","false")
            arrayObject.put("isStarred","false")
            arr.put(arrayObject)
        }
        for( item in constructorListTeam)
        {
            val arrayObject = JSONObject()

            arrayObject.put("sendBy",loginModelHomePage.empId)
            arrayObject.put("sendTo",item.id)
            arrayObject.put("sendType","Cc")
            arrayObject.put("isSeen","false")
            arrayObject.put("isStarred","false")
            arr.put(arrayObject)
        }

        paramObject.put("mailDetailList", arr)
        paramObject.put("attachmentList", arrAttachment)

        var reqBody = RequestBody.create(MediaType.parse("text/plain"), paramObject.toString());

        var call: Call<GenerateOTPModel>? = surveyImagesParts?.let {
            apiInterface?.sendEmail(
                "bearer " + loginModelHomePage.accessToken,
                it,reqBody
            )
        } as? Call<GenerateOTPModel>

        call?.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(
                call: Call<GenerateOTPModel?>?,
                response: Response<GenerateOTPModel?>
            ) {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    if(response.body()?.getErrorObj()?.errorMessage?.isEmpty()==false)
                    {

                        generalClass.showSnackbar(window.decorView.rootView,
                            response.body()?.getErrorObj()?.errorMessage.toString()
                        )
                    }
                    else
                    {
                        response.body()?.getData()?.message?.let {
                            generalClass.showSnackbar(window.decorView.rootView,
                                it
                            )
                        }
                        onBackPressed()
                    }
                }
                else
                {  generalClass.showSnackbar(window.decorView.rootView, "Server error") }
                alertClass.hideAlert()
            }
            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                call.cancel()
                alertClass.hideAlert()
                generalClass.showSnackbar(window.decorView.rootView, "Server error")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        alertClass = AlertClass(this)

    }
}