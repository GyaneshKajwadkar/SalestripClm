package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.adapter.VisualFileAdapter
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.SortingDisplayVisual
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_display_visual.view.*
import kotlinx.android.synthetic.main.bottom_sheet_visualads.view.*
import java.util.*


class PresentEDetailingFrag : Fragment(),  SortingDisplayVisual, ItemClickDisplayVisual {

    companion object
    {
        var doctorIdDisplayVisual = 0
        var doctor_et: EditText? = null
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheet: ConstraintLayout
    lateinit var db : DatabaseHandler
    var adapter: BottomSheetDoctorAdapter? = null
    var adapterVisualFile: VisualFileAdapter? = null
    var sharePreferance: PreferenceClass? = null
    var contextFragment= getContext()
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var edetailingFavList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var downloadFilePathList: ArrayList<DownloadFileModel> = ArrayList()
    var storedDownloadedList: ArrayList<DownloadFileModel> = ArrayList()
    var views:View?=null
    var doctorName=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.activity_display_visual, container, false)
        db = DatabaseHandler(requireActivity())
        initView(views!!)
        return views }

    fun  initView(views: View)
    {
        doctor_et = views.findViewById(R.id.doctor_et) as EditText
        bottomSheet = views.findViewById(R.id.bottomSheet) as ConstraintLayout
        sharePreferance = PreferenceClass(activity)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        doctor_et?.setOnClickListener({
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        })

        views!!.close_imv?.setOnClickListener({
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        })

        views!!.submitBtn?.setOnClickListener({

            val bundle = arguments
           /* if(bundle !=null)
            {*/
                  val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
                  intent.putExtra("doctorID", doctorIdDisplayVisual)
                  intent.putExtra("doctorName", doctorName)
                  arguments.let {
                      val doctorObj = requireArguments().getString("doctorObj")
                      if(doctorObj!=null)
                      { intent.putExtra("doctorObj", doctorObj) }
                  }
                    startActivityForResult(intent,2)
           /* }
            else{
                Toast.makeText(context,"Data save successfully",Toast.LENGTH_LONG).show()
                views!!.submitBtn?.visibility=View.INVISIBLE
            }*/
        })

       // setDoctorList()
        setAdapter()
        setSelectorAdapter(downloadFilePathList)
        setUserFavAdapter()

 /*       views!!.progressMessage_tv?.setText("Loading e-Detailing")
        GeneralClass(requireActivity()).enableProgress(views!!.progressView_parentRv!!)


        Handler(Looper.getMainLooper()).postDelayed({
            setDoctorList()
            setAdapter()
            setSelectorAdapter(downloadFilePathList)
            setUserFavAdapter()
            GeneralClass(requireActivity()).disableProgress(views!!.progressView_parentRv!!)
            //  activity?.let { disableProgress(progressView_parentRv!!, it) }
        }, 50)*/

        callDownloadFragment()

        arguments?.let {
            val sendtext = requireArguments().getString("type")
            if(sendtext!=null)
            {
                doctor_et?.visibility=View.INVISIBLE
                doctor_et?.setText("1")

                doctorIdDisplayVisual=requireArguments().getInt("doctorID")
                doctorName= requireArguments().getString("doctorName").toString()
            }

            val doctorObj = requireArguments().getString("doctorObj")
            if(doctorObj!=null)
            {
                var doctorObj= Gson().fromJson(doctorObj, SyncModel.Data.Doctor::class.java)
                if(doctorObj.product1Id!=0)
                {
                    setDoctorFavBrand(doctorObj.product1Id,0)

                    if(doctorObj.product2Id!=0)
                    {
                        setDoctorFavBrand(doctorObj.product2Id,1)
                        if(doctorObj.product3Id!=0)
                        {
                            setDoctorFavBrand(doctorObj.product3Id,2)
                            if(doctorObj.product4Id!=0)
                            {
                                setDoctorFavBrand(doctorObj.product4Id,3)
                                if(doctorObj.product5Id!=0)
                                {
                                    setDoctorFavBrand(doctorObj.product5Id,4)
                                }
                            }
                        }
                    }
                }
                setDownloadListAdapter(storedDownloadedList)
            }
        }


        views!!.allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))


        views!!.allbrandparent_ll?.setOnClickListener({

            views!!.allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))
            views!!.favBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.gray))
            views!!.division_spinner?.isEnabled=true
            callDownloadFragment()
            views!!.sideparent_rl?.visibility=View.VISIBLE
            views!!.allBrandParent_ll?.visibility=View.VISIBLE
            views!!.division_spinner?.visibility=View.VISIBLE
            views!!.favBrand_frame?.visibility=View.GONE
        })

        views!!.favParent_ll?.setOnClickListener({

            views!!.allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.gray));
            views!!.favBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white));
            views!!.division_spinner?.isEnabled=false
            onClickFavButton()
            setDownloadListAdapter(downloadFilePathList)
            views!!.sideparent_rl?.visibility=View.GONE
            views!!.allBrandParent_ll?.visibility=View.GONE
            views!!.division_spinner?.visibility=View.INVISIBLE
            views!!.favBrand_frame?.visibility=View.VISIBLE
        })

        db.deleteAllVisualAds()
        db.deleteAllChildVisual()

        setDoctorList()
    }

    fun setDoctorFavBrand(product1Id: Int?, i1: Int)
    {
        var indexMain = -1
        for((index,data) in storedDownloadedList.withIndex())
        {
            if(data.brandId==product1Id)
            { indexMain=index }
        }

        if(indexMain==-1)return

        val tempData=storedDownloadedList.get(indexMain)
         tempData.isFav=true
         storedDownloadedList.removeAt(indexMain)
        if(storedDownloadedList.size>=i1)  storedDownloadedList.add(i1,tempData)
        else storedDownloadedList.add(tempData)
    }

    fun setDoctorList()
    {
        adapter = BottomSheetDoctorAdapter(
            SplashActivity.staticSyncData?.doctorList as ArrayList<SyncModel.Data.Doctor>,
            doctor_et!!,
            bottomSheetBehavior
        )
        views!!.doctorList_rv?.itemAnimator = DefaultItemAnimator()
        views!!.doctorList_rv?.adapter = adapter
        views!!.doctorSearch_et!!.addTextChangedListener(filterTextWatcher)
    }

    fun setSelectorAdapter(list: ArrayList<DownloadFileModel>)
    {
        var downloadTypeList: ArrayList<String> = ArrayList()
        downloadTypeList.add("All")
        for ( valueDownload in list!!)
        { downloadTypeList.add(valueDownload.downloadType) }
        val hashSet: Set<String> = LinkedHashSet(downloadTypeList)
        downloadTypeList.clear()
        downloadTypeList.addAll(hashSet)


        val langAdapter = ArrayAdapter<String>(contextFragment!!, android.R.layout.simple_spinner_item, downloadTypeList)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        views!!.division_spinner?.setAdapter(langAdapter)
        views!!.division_spinner?.setSelection(0)

        views!!.division_spinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.selectedItem

                if(item.equals("All"))
                {
                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(storedDownloadedList)
                    setDownloadListAdapter(downloadFilePathList)
                }
                else
                {
                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(storedDownloadedList)

                    var filterList: ArrayList<DownloadFileModel> = ArrayList()

                    for ( valueDownload in downloadFilePathList!!) {

                        if (valueDownload.downloadType.equals(item)) {
                            filterList?.add(valueDownload)
                        }
                    }
                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(filterList)
                    setDownloadListAdapter(downloadFilePathList)
                }
                callDownloadFragment()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    //filter downloading list from buttons
    override fun onClickButton(downloadList: java.util.ArrayList<DownloadFileModel>)
    {
        downloadFilePathList.clear()
        downloadFilePathList.addAll(downloadList!!)
        adapterVisualFile?.notifyDataSetChanged()
    }

    //-------------------------------------text watcher- filter doctor list using edit text
    val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            adapter?.getFilter()?.filter(s.toString());
        }

        override fun afterTextChanged(s: Editable) {}
    }

    //-------------------------------------check end page of web view and enable submit button according to it
    override fun onResume() {
        super.onResume()
        if (db.getAllSubmitVisual().size > 0)
        {
            views!!.submitBtn?.setEnabled(true)
            views!!.submitBtn?.visibility=View.VISIBLE
        }
        else
        {
            views!!.submitBtn?.setEnabled(false)
            views!!.submitBtn?.visibility = View.GONE
        }
    }

   /* //-------------------------------------submit Visual api
    private fun sendVisual_api() {
        var visualSendModel: ArrayList<VisualAdsModel_Send> = db.getAllSubmitVisual()
        //get all submit visual api

        var profileData = sharePreferance?.getPref("profileData")           //get profile data from share preferance
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)    //convert profile data string to model class
        GeneralClass(requireActivity()).enableSimpleProgress(views!!.progressBar!!)                                            //visble progress bar

        //call submit visual ads api interfae post method
        var call: Call<SyncModel> = HomePage.apiInterface?.submitVisualAds(
            "bearer " + loginModel?.accessToken,
            visualSendModel
        ) as Call<SyncModel>
        call.enqueue(object : Callback<SyncModel?> {
            override fun onResponse(call: Call<SyncModel?>?, response: Response<SyncModel?>) {
                Log.e("sendVisual_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    // after execute first element of array list we delete it and again check if the db have more entries this method call itself again

                    db.deleteAllVisualAds()
                    db.deleteAllChildVisual()
                    views!!.submitBtn?.visibility=View.GONE
                    Toast.makeText(activity, "Data save successfully", Toast.LENGTH_LONG).show()

                } else {
                }
                GeneralClass(requireActivity()).disableSimpleProgress(views!!.progressBar!!)                                            //visble progress bar

            }

            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
                //on failure of api.
                GeneralClass(requireActivity()).checkInternet() // check internet connection
                call.cancel()
                GeneralClass(requireActivity()).disableSimpleProgress(views!!.progressBar!!)                                            //visble progress bar
            }
        })
    }
*/
    //-------------------------------------set download array list. It call  only when activity open
    fun setAdapter() {
        edetailingList = db.getAlleDetail()   //fetch edetailing list from db

        for ((index, value) in edetailingList?.withIndex()!!) {
            if (value.isSaved == 1)    //check edetailing have saved file or not
            {
                var downloadFilePathLocal: DownloadFileModel = Gson().fromJson(
                    value.filePath,
                    object : TypeToken<DownloadFileModel?>() {}.type
                )
                downloadFilePathLocal.downloadType=value.divisionName !!
                downloadFilePathLocal.brandId=value.brandId!!
                downloadFilePathLocal.brandName=value.brandName!!
                downloadFilePathLocal.eDetailingId=value.geteDetailId()!!
                downloadFilePathList.add(downloadFilePathLocal)
                storedDownloadedList.add(downloadFilePathLocal)
            }

            if (index == edetailingList?.size!! - 1) {

                if(downloadFilePathList.size==0)
                {
                    views!!.splitViewparent_ll?.visibility=View.GONE
                    views!!.noData_tv?.visibility=View.VISIBLE
                }
                else
                {
                    setDownloadListAdapter(downloadFilePathList) //call initilize adapter method when loop on its last itration
                }

            }
        }

        if(edetailingList!!.size==0)
        {
            views!!.splitViewparent_ll?.visibility=View.GONE
            views!!.noData_tv?.visibility=View.VISIBLE

        }

    }


    fun setUserFavAdapter()
    {
        edetailingFavList = db.getAllFavBrands()

        for ((index, value) in edetailingFavList?.withIndex()!!) {

            var downloadFilePathLocal: DownloadFileModel = Gson().fromJson(
                value.filePath,
                object : TypeToken<DownloadFileModel?>() {}.type
            )
            //get and convert save file string to array list

            downloadFilePathLocal.downloadType=value.divisionName!! //set division name
            downloadFilePathLocal.brandId=value.brandId!!
            downloadFilePathLocal.brandName=value.brandName!!
            downloadFilePathLocal.eDetailingId=value.geteDetailId()!!

        }
    }

    //-------------------------------------rearrange linkedBrandList according to priority
    fun rearrangeDownloadedList()
    {
//        Collections.sort(favProductDoctorList, object :
//            Comparator<SyncModel.Data.Doctor.LinkedBrand> {
//
//            override fun compare(
//                first: SyncModel.Data.Doctor.LinkedBrand?,
//                second: SyncModel.Data.Doctor.LinkedBrand?
//            ): Int {
//                val p1: SyncModel.Data.Doctor.LinkedBrand =
//                    first as SyncModel.Data.Doctor.LinkedBrand
//                val p2: SyncModel.Data.Doctor.LinkedBrand =
//                    second as SyncModel.Data.Doctor.LinkedBrand
//                return p1.priorityOrder.compareTo(p2.priorityOrder)
//            }
//        })
//
//        var priorityList: ArrayList<DownloadFileModel> = ArrayList()
//        var simpleList: ArrayList<DownloadFileModel> = ArrayList()
//
//        //check and add is downloaded list have same brand or not
//        for ((index, valueDownload) in downloadFilePathList?.withIndex()!!)
//        {
//            var found = false
//            for ((index, valueFavBrand) in favProductDoctorList?.withIndex()!!)
//            {
//                if(valueFavBrand.brandId==valueDownload.brandId)
//                {
//                    found = true
//                }
//            }
//            //if data not found add to simple list
//            if (!found)
//            {
//                simpleList.add(valueDownload)
//            }
//            // if data is same then add to priority list
//            else
//            {
//                valueDownload.setFileName(valueDownload.fileName + "*")
//                priorityList.add(valueDownload)
//            }
//
//
//            if(index== downloadFilePathList?.size!! -1)
//            {
//                //clear and add all data to downloaded list and call download adapter
//                downloadFilePathList.clear()
//                storedDownloadedList.clear()
//                downloadFilePathList.addAll(priorityList)
//                storedDownloadedList.addAll(priorityList)
//                downloadFilePathList.addAll(simpleList)
//                storedDownloadedList.addAll(simpleList)
//                setDownloadListAdapter(downloadFilePathList)
//
//            }
//
//        }
    }

    //-------------------------------------initilize Visual file recycler view
    fun setDownloadListAdapter(list: ArrayList<DownloadFileModel>)
    {
        adapterVisualFile = VisualFileAdapter(list, requireActivity(), 1, this)
        views!!.brand_rv?.setLayoutManager(LinearLayoutManager(requireActivity()));
        views!!.brand_rv?.itemAnimator = DefaultItemAnimator()
        views!!.brand_rv?.adapter = adapterVisualFile
    }


    //==========================================DoctorList_BottomSheet adapter==============================================
    inner class BottomSheetDoctorAdapter(
        public var doctorList: ArrayList<SyncModel.Data.Doctor>,
        var doctor_et: EditText,
        var bottomSheet: BottomSheetBehavior<ConstraintLayout>
    ) :
        RecyclerView.Adapter<BottomSheetDoctorAdapter.MyViewHolder>(), Filterable {

        var filteredData: ArrayList<SyncModel.Data.Doctor>? = doctorList

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            // initilize adapter view
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
            val modeldata = filteredData?.get(position)
            //set text of layout
            holder.headerDoctor_tv.setText(modeldata?.doctorName)
            holder.route_tv.setText("Route: " + modeldata?.routeName)
            holder.speciality_tv.setText("Speciality: " + modeldata?.specialityName)

            //click event of parent layout
            holder.parent_cv.setOnClickListener({
                doctor_et?.setText((modeldata?.doctorName))
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED)
                doctorIdDisplayVisual= modeldata?.doctorId!!
                rearrangeDownloadedList()
                callDownloadFragment()

            })
        }
        override fun getItemCount(): Int
        { return filteredData?.size!! }


        //-------------------------------------filter list using text input from edit text
        override fun getFilter(): Filter? {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    filteredData = results.values as ArrayList<SyncModel.Data.Doctor>?
                    notifyDataSetChanged()
                }

                override fun performFiltering(constraint: CharSequence): FilterResults? {
                    var constraint = constraint
                    val results = FilterResults()
                    val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()

                    constraint = constraint.toString().toLowerCase()
                    for (i in 0 until doctorList?.size!!) {
                        val dataNames: SyncModel.Data.Doctor = doctorList?.get(i)!!
                        if (dataNames.doctorName?.lowercase()?.startsWith(constraint.toString()) == true) {
                            FilteredArrayNames.add(dataNames)
                        }
                    }
                    results.count = FilteredArrayNames.size
                    results.values = FilteredArrayNames
                    return results
                }
            }
        }
    }

    //==========================================DoctorList_BottomSheet adapter==============================================


    override fun onClickDisplayVisual(eDetailinId: Int,brandID: Int,selectionType: Int)
    {
        val args = Bundle()
        args.putInt("eDetailingID", eDetailinId)
        args.putInt("brandId", brandID)
        args.putInt("selectionType", selectionType)

        val childFragment: Fragment = ShowDownloadedFragment()
        childFragment.setArguments(args)

        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_fragment_container, childFragment).commit()
    }

    fun onClickFavButton()
    {
        val args = Bundle()
        args.putInt("selectionType", 2)

        val childFragment2: Fragment = ShowDownloadedFragment()
        childFragment2.setArguments(args)

        val transaction2: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction2.replace(R.id.favBrand_frame, childFragment2).commit()
    }


    fun callDownloadFragment()
    {
        if(views!!.favBrand_frame?.visibility==View.VISIBLE)
        { onClickFavButton() }
        else
        {
            val childFragment: Fragment = ShowDownloadedFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_fragment_container, childFragment).commit()
        }
    }


    override fun onAttach(context: Context)
    {
        contextFragment=context
        super.onAttach(context)
    }


}



