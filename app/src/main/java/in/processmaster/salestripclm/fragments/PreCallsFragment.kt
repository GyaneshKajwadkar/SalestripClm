package `in`.processmaster.salestripclm.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in` .processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageAlertClass
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageDataBase
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageGeneralClass
import `in`.processmaster.salestripclm.activity.HomePage.Companion.homePageSharePref
import `in`.processmaster.salestripclm.activity.OnlinePresentationActivity
import `in`.processmaster.salestripclm.adapter.LastRCPA_Adapter
import `in`.processmaster.salestripclm.adapter.SimpleListAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.PreCallModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_pre_calls.view.*
import kotlinx.android.synthetic.main.fragment_pre_calls.view.analysisProgress
import kotlinx.android.synthetic.main.fragment_pre_calls.view.brandList_rv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.datePob_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.giftGiven_rv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.lastRcpaDetail_rv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.lastRcpaHeader_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.lastVisitDate_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.lastVisit_tr
import kotlinx.android.synthetic.main.fragment_pre_calls.view.noDataBrand
import kotlinx.android.synthetic.main.fragment_pre_calls.view.noDataGift
import kotlinx.android.synthetic.main.fragment_pre_calls.view.noDataSample
import kotlinx.android.synthetic.main.fragment_pre_calls.view.noData_gif
import kotlinx.android.synthetic.main.fragment_pre_calls.view.parentButton
import kotlinx.android.synthetic.main.fragment_pre_calls.view.precall_parent
import kotlinx.android.synthetic.main.fragment_pre_calls.view.remarkPOB_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.remark_ll
import kotlinx.android.synthetic.main.fragment_pre_calls.view.remark_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.reportedTime_tr
import kotlinx.android.synthetic.main.fragment_pre_calls.view.reportedTime_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.sampleGiven_rv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.startDetailing_btn
import kotlinx.android.synthetic.main.fragment_pre_calls.view.total_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.visitPurpose_tr
import kotlinx.android.synthetic.main.fragment_pre_calls.view.visitPurpose_tv
import kotlinx.android.synthetic.main.fragment_pre_calls.view.workingWith_tr
import kotlinx.android.synthetic.main.fragment_pre_calls.view.workingWith_tv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PreCallsFragment() : Fragment() {

    var viewParent:View?=null
    var  docCallModel : DailyDocVisitModel.Data= DailyDocVisitModel.Data()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewParent= inflater.inflate(`in`.processmaster.salestripclm.R.layout.fragment_pre_calls, container, false)
        return  viewParent
        }

    fun setViewPreCall(doctorDetailModel: SyncModel.Data.Doctor){
        if(homePageGeneralClass?.isInternetAvailable() == true) {
            viewParent?.noInternet_tv?.visibility=View.INVISIBLE
            preCallAnalysisApi(doctorDetailModel)
        }
        else {
            viewParent?.parentButton?.visibility=View.VISIBLE
            viewParent?.analysisProgress?.visibility=View.GONE
            viewParent?.noData_gif?.visibility=View.VISIBLE
            viewParent?.noInternet_tv?.visibility=View.VISIBLE
            viewParent?.parentPreCall_nv?.visibility=View.INVISIBLE
        }

         viewParent?.startDetailing_btn?.setOnClickListener({

             val responseDocCall=homePageDataBase?.getApiDetail(5)
             if(!responseDocCall.equals("")) {
                 docCallModel = Gson().fromJson(responseDocCall, DailyDocVisitModel.Data::class.java)
             }

             val isAlreadyContain=docCallModel.dcrDoctorlist?.any{ s -> s.doctorId == doctorDetailModel.doctorId }
             if(isAlreadyContain == true) {
                 viewParent?.parentButton?.visibility=View.GONE

                 homePageAlertClass?.commonAlert("Alert!","This doctor DCR is already submitted")
                 return@setOnClickListener
             }

             val intent = Intent(activity, OnlinePresentationActivity::class.java)
             intent.putExtra("doctorID", doctorDetailModel.doctorId)
             intent.putExtra("doctorName", doctorDetailModel.doctorName)
             intent.putExtra("skip", false)
             intent.putExtra("doctorObj", Gson().toJson(doctorDetailModel))
             intent.putExtra("isPresentation", true)
             startActivityForResult(intent,3)
         })

     }


     fun preCallAnalysisApi(doctorDetailModel: SyncModel.Data.Doctor) {

        viewParent?.analysisProgress?.visibility=View.VISIBLE
         viewParent?.parentPreCall_nv?.visibility=View.INVISIBLE

        var profileData = homePageSharePref?.getPref("profileData")           //get profile data from share preferance
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)

        //call submit visual ads api interfae post method
        var call: Call<PreCallModel>? = doctorDetailModel.doctorId?.let {
            HomePage.apiInterface?.priCallAnalysisApi("bearer " + loginModel?.accessToken,
                it
            )
        } as? Call<PreCallModel>
        call?.enqueue(object : Callback<PreCallModel?> {
            override fun onResponse(call: Call<PreCallModel?>?, response: Response<PreCallModel?>) {
                Log.e("preCallAnalysisApi", response.code().toString() + "")
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {

                    val analysisModel=response.body()?.getData()?.lastVisitSummary

                    viewParent?.parentPreCall_nv?.visibility=View.VISIBLE
                    viewParent?.noData_gif?.visibility=View.GONE

                    viewParent?.precall_parent?.visibility=View.VISIBLE
                    viewParent?.parentButton?.visibility=View.VISIBLE

                    //remark_ll
                    if(analysisModel?.visitPurpose?.let {
                            homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.visitPurpose_tr?.visibility=View.GONE }
                    else{ viewParent?.visitPurpose_tv?.setText(analysisModel?.visitPurpose) }

                    if(analysisModel?.workWithName?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.workingWith_tr?.visibility=View.GONE }
                    else{ viewParent?.workingWith_tv?.setText(analysisModel?.workWithName) }

                    if(analysisModel?.strDcrDate?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.lastVisit_tr?.visibility=View.GONE }
                    else{ viewParent?.lastVisitDate_tv?.setText(analysisModel?.strDcrDate) }

                    if(analysisModel?.remarks?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.remark_ll?.visibility=View.GONE }
                    else{ viewParent?.remark_tv?.setText(analysisModel?.remarks) }

                    if(analysisModel?.strReportedTime?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.reportedTime_tr?.visibility=View.GONE }
                    else{ viewParent?.reportedTime_tv?.setText(analysisModel?.strReportedTime) }


                    viewParent?.brandList_rv?.layoutManager= LinearLayoutManager(activity)
                    viewParent?.sampleGiven_rv?.layoutManager= LinearLayoutManager(activity)
                    viewParent?.giftGiven_rv?.layoutManager= LinearLayoutManager(activity)

                    var mainList= ArrayList<String>()
                    var subList= ArrayList<Int>()

                    if( analysisModel?.productList!=null && analysisModel?.productList?.size!=0)
                    {
                        var i: Int = analysisModel?.productList?.size?.minus(1)!!
                        for( data in analysisModel?.productList!!)
                        {
                            data.productName?.let { mainList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)viewParent?.noDataBrand?.visibility=View.VISIBLE
                                else viewParent?.noDataBrand?.visibility=View.GONE
                            }
                        }
                        var adapterProduct= SimpleListAdapter(mainList,subList)
                        viewParent?.brandList_rv?.adapter=adapterProduct
                    }
                    else
                    {
                        viewParent?.noDataBrand?.visibility = View.VISIBLE
                        viewParent?.brandList_rv?.adapter= SimpleListAdapter(mainList,subList)
                    }

                    mainList= ArrayList<String>()
                    subList= ArrayList<Int>()

                    if( analysisModel?.sampleList!=null && analysisModel?.sampleList?.size!=0)
                    {
                        var i: Int = analysisModel?.sampleList?.size?.minus(1)!!
                        for( data in analysisModel?.sampleList!!) {
                            mainList.add(data.productName.toString())
                            data.qty?.let { subList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)viewParent?.noDataSample?.visibility=View.VISIBLE
                                else viewParent?.noDataSample?.visibility=View.GONE
                            }
                        }

                        var adapterSampleGiven= SimpleListAdapter(mainList,subList)
                        viewParent?.sampleGiven_rv?.adapter=adapterSampleGiven

                        val isAlreadyContain=docCallModel.dcrDoctorlist?.any{ s -> s.doctorId == doctorDetailModel.doctorId }
                        if(isAlreadyContain == true) {
                            viewParent?.parentButton?.visibility=View.GONE
                            homePageAlertClass?.commonAlert("Alert!","Doctor e-detailing already done for today")
                        }

                    }
                    else
                    {
                        viewParent?.noDataSample?.visibility = View.VISIBLE
                        viewParent?.sampleGiven_rv?.adapter= SimpleListAdapter(mainList,subList)
                    }

                    mainList= ArrayList<String>()
                    subList= ArrayList<Int>()

                    if( analysisModel?.giftList!=null && analysisModel?.giftList?.size!=0)
                    {
                        mainList= ArrayList<String>()
                        subList= ArrayList<Int>()
                        var i: Int = analysisModel?.giftList?.size?.minus(1)!!
                        for( data in analysisModel?.giftList!!)
                        {
                            mainList.add(data.productName.toString())
                            data.qty?.let { subList.add(it) }
                            if (i-- == 0) {
                                if(mainList.size==0)viewParent?.noDataGift?.visibility=View.VISIBLE
                                else  viewParent?.noDataGift?.visibility=View.GONE
                            }
                        }
                        var adapterGift= SimpleListAdapter(mainList,subList)
                        viewParent?.giftGiven_rv?.adapter=adapterGift
                    }
                    else {
                        viewParent?.noDataGift?.visibility = View.VISIBLE
                        viewParent?.giftGiven_rv?.adapter= SimpleListAdapter(mainList,subList)
                    }

                    viewParent?.total_tv?.setText("Total: "+analysisModel?.lastPOBDetails?.totalPOB)


                    if(analysisModel?.lastPOBDetails?.remark?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.remarkPOB_tv?.visibility=View.GONE }
                    else{ viewParent?.remarkPOB_tv?.setText("Remark: "+analysisModel?.lastPOBDetails?.remark) }

                    if(analysisModel?.lastPOBDetails?.strPobDate?.let { homePageGeneralClass?.checkStringNullEmpty(it) } == true)
                    { viewParent?.datePob_tv?.visibility=View.GONE }
                    else{ viewParent?.datePob_tv?.setText("Date: "+analysisModel?.lastPOBDetails?.strPobDate) }

                    if(analysisModel?.lastRCPADetails?.size!=0)
                    {
                        viewParent?.lastRcpaDetail_rv?.layoutManager= LinearLayoutManager(activity)
                        val adapter= LastRCPA_Adapter(analysisModel?.lastRCPADetails)
                        viewParent?.lastRcpaDetail_rv?.adapter=adapter
                    }
                    else{ viewParent?.lastRcpaHeader_tv?.visibility=View.GONE}
                }
                else viewParent?.noData_gif?.visibility=View.VISIBLE

                viewParent?.analysisProgress?.visibility=View.GONE
            }

            override fun onFailure(call: Call<PreCallModel?>, t: Throwable?) {
                viewParent?.analysisProgress?.visibility=View.GONE
                viewParent?.noData_gif?.visibility=View.VISIBLE
                activity?.let { GeneralClass(it).checkInternet() } // check internet connection
                call.cancel()
            }
        })
    }

}