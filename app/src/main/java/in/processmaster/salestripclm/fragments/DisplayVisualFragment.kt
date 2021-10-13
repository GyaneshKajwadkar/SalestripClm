package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.VisualFileAdapter
import `in`.processmaster.salestripclm.interfaceCode.DisplayVisualInterface
import `in`.processmaster.salestripclm.interfaceCode.ItemClickDisplayVisual
import `in`.processmaster.salestripclm.interfaceCode.SortingDisplayVisual
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Context
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class DisplayVisualFragment : BaseFragment(), DisplayVisualInterface, SortingDisplayVisual, ItemClickDisplayVisual {
    //declare XML component
    var doctorSearch_et: EditText? = null
    var close_imv: ImageView? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheet: ConstraintLayout
    var doctorList_rv: RecyclerView? = null
    var brand_rv: RecyclerView? = null
    var submitBtn: Button? = null
    var progressBar: ProgressBar? = null
  //  var back_imv: ImageView? = null
    var splitViewparent_ll:LinearLayout?=null
    var noData_tv:TextView?=null
    var division_spinner: Spinner?=null

    var progressView_parentRv: RelativeLayout?=null
    var progressMessage_tv: TextView? =null

    //local variable initilization
    var db = DatabaseHandler(activity)
    var adapter: BottomSheetDoctorAdapter? = null

    var adapterVisualFile: VisualFileAdapter? = null

    var apiInterface: APIInterface? = null
    var sharePreferance: PreferenceClass? = null

    var contextFragment= getContext()

    companion object
    {
        var doctorIdDisplayVisual = 0
        var doctor_et: EditText? = null
    }

    //arrayList initilazation
    var edetailingList: ArrayList<DevisionModel.Data.EDetailing>? = null
    var edetailingFavList: ArrayList<DevisionModel.Data.EDetailing>? = null

    //For all brands
    var downloadFilePathList: ArrayList<DownloadFileModel> = ArrayList()
    var storedDownloadedList: ArrayList<DownloadFileModel> = ArrayList()


    var favProductDoctorList: ArrayList<SyncModel.Data.Doctor.LinkedBrand> = ArrayList()

    var selectorButton_rv: RecyclerView? =null

    var favBrand_rl: RelativeLayout?=null

    var allbrandparent_ll: LinearLayout?=null
    var favParent_ll: LinearLayout?=null

    var allBrand_iv:ImageView?=null
    var favBrand_iv:ImageView?=null
    var sideparent_rl:RelativeLayout?=null

    var allBrandParent_ll:LinearLayout?=null
    var favBrand_frame:FrameLayout?=null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.activity_display_visual, container, false)
        db = DatabaseHandler(activity)
        initView(view)
        return view
    }

    fun  initView(view: View)
    {

        //-------------------------------------Initilazation
        doctor_et = view.findViewById(R.id.doctor_et) as EditText
        doctorSearch_et = view.findViewById(R.id.doctorSearch_et) as EditText
        bottomSheet = view.findViewById(R.id.bottomSheet) as ConstraintLayout
        doctorList_rv = view.findViewById(R.id.doctorList_rv) as RecyclerView
        brand_rv =view. findViewById(R.id.brand_rv) as RecyclerView
        favBrand_rl =view. findViewById(R.id.favBrand_rl) as RelativeLayout
        sideparent_rl =view. findViewById(R.id.sideparent_rl) as RelativeLayout

        progressView_parentRv=view.findViewById(R.id.progressView_parentRv) as RelativeLayout
        progressMessage_tv=view.findViewById(R.id.progressMessage_tv) as TextView

        submitBtn = view.findViewById(R.id.submitBtn) as Button
        close_imv = view.findViewById(R.id.close_imv) as ImageView
     //   back_imv = view.findViewById(R.id.back_imv) as ImageView
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        selectorButton_rv = view.findViewById(R.id.selectorButton_rv) as RecyclerView

        splitViewparent_ll = view.findViewById(R.id.splitViewparent_ll) as LinearLayout
        noData_tv = view.findViewById(R.id.noData_tv) as TextView
        division_spinner = view.findViewById(R.id.division_spinner) as Spinner
        allbrandparent_ll = view.findViewById(R.id.allbrandparent_ll) as LinearLayout
        favParent_ll = view.findViewById(R.id.favParent_ll) as LinearLayout
        allBrand_iv = view.findViewById(R.id.allBrand_iv) as ImageView
        favBrand_iv = view.findViewById(R.id.favBrand_iv) as ImageView

        allBrandParent_ll = view.findViewById(R.id.allBrandParent_ll) as LinearLayout
        favBrand_frame    = view.findViewById(R.id.favBrand_frame) as FrameLayout

        sharePreferance = PreferenceClass(activity)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

  /*      back_imv!!.setOnClickListener {
            activity?.onBackPressed()
        }*/


        doctor_et?.setOnClickListener({
            val state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                        BottomSheetBehavior.STATE_COLLAPSED
                    else
                        BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = state
        })

        close_imv?.setOnClickListener({
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        })

        submitBtn?.setOnClickListener({
            if (isInternetAvailable(requireActivity()) == false)
            {
                networkAlert(requireActivity())
            }
            else
            {
                sendVisual_api()
            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float)
            {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int)
            {
            }
        })

        progressMessage_tv?.setText("Loading e-Detailing")
        activity?.let { enableProgress(progressView_parentRv!!, it) }


        Handler(Looper.getMainLooper()).postDelayed({

            setDoctorList()
            setAdapter()
            setSelectorAdapter(downloadFilePathList)
            setUserFavAdapter()

            activity?.let { disableProgress(progressView_parentRv!!, it) }
        }, 50)

        callDownloadFragment()

        arguments?.let {
            val sendtext = requireArguments().getString("type")
            if(sendtext!=null)
            {
                doctor_et?.visibility=View.INVISIBLE
                doctor_et?.setText("1")
                doctorIdDisplayVisual=1
            }
        }


        allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))


        allbrandparent_ll?.setOnClickListener({

            allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white))
            favBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.gray))
            division_spinner?.isEnabled=true
            callDownloadFragment()
            sideparent_rl?.visibility=View.VISIBLE
            allBrandParent_ll?.visibility=View.VISIBLE
            division_spinner?.visibility=View.VISIBLE
            favBrand_frame?.visibility=View.GONE
            //   brand_rv?.isClickable=true

        })

        favParent_ll?.setOnClickListener({

            allBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.gray));
            favBrand_iv?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white));
            division_spinner?.isEnabled=false
          //  brand_rv?.isClickable=false
            onClickFavButton()
            setDownloadListAdapter(downloadFilePathList)


            sideparent_rl?.visibility=View.GONE

            allBrandParent_ll?.visibility=View.GONE
            division_spinner?.visibility=View.INVISIBLE
            favBrand_frame?.visibility=View.VISIBLE

            })

      //  edetailingFavList = db.getAllFavBrands()
    }

    fun setDoctorList()
    {
        var model = Gson().fromJson(db.getAllData(), SyncModel::class.java)      //get doctor list from db and convert it string to model class
        adapter = BottomSheetDoctorAdapter(
                model.data.doctorList,
                doctor_et!!,
                bottomSheetBehavior,
                this
        )
        doctorList_rv?.itemAnimator = DefaultItemAnimator()
        doctorList_rv?.adapter = adapter
        doctorSearch_et!!.addTextChangedListener(filterTextWatcher)
    }

    fun setSelectorAdapter(list: ArrayList<DownloadFileModel>)
    {
        var downloadTypeList: ArrayList<String> = ArrayList()
        downloadTypeList.add("All")
        //Add all download type
        for ((index, valueDownload) in list?.withIndex()!!)
        {
            downloadTypeList.add(valueDownload.downloadType)
        }

        //Removing duplicate value
        val hashSet: Set<String> = LinkedHashSet(downloadTypeList)
        downloadTypeList.clear()
        downloadTypeList.addAll(hashSet)



        val langAdapter = ArrayAdapter<String>(contextFragment!!, android.R.layout.simple_spinner_item, downloadTypeList)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        division_spinner?.setAdapter(langAdapter)
        division_spinner?.setSelection(0)

        division_spinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.selectedItem

                if(item.equals("All"))
                {
                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(storedDownloadedList)
                    setDownloadListAdapter(downloadFilePathList)
                   // adapterVisualFile?.notifyDataSetChanged()

                }
                else
                {
                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(storedDownloadedList)

                    var filterList: ArrayList<DownloadFileModel> = ArrayList()
                    var filterListFav: ArrayList<DownloadFileModel> = ArrayList()

                    for ((index, valueDownload) in downloadFilePathList?.withIndex()!!) {

                        if (valueDownload.downloadType.equals(item)) {
                            filterList?.add(valueDownload)
                        }
                    }

                    downloadFilePathList.clear()
                    downloadFilePathList.addAll(filterList)

                    setDownloadListAdapter(downloadFilePathList)

                   // adapterVisualFile?.notifyDataSetChanged()

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

        override fun afterTextChanged(s: Editable) {
            // TODO Auto-generated method stub
        }
    }

    //-------------------------------------check end page of web view and enable submit button according to it
    override fun onResume() {
        super.onResume()
        if (db.getAllSubmitVisual().size > 0) {

            Log.e("dbGetAll",db.allSubmitVisual.size.toString())
            if(db.allSubmitVisual.size==0)
            {
                return
            }

            submitBtn?.setEnabled(true)
            submitBtn?.visibility=View.VISIBLE
        }
    }

    //-------------------------------------submit Visual api
    private fun sendVisual_api() {
        var visualSendModel: ArrayList<VisualAdsModel_Send> = db.getAllSubmitVisual()
        //get all submit visual api

        apiInterface = APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(
                APIInterface::class.java
        )
        //initilize api interface

        var profileData = sharePreferance?.getPref("profileData")           //get profile data from share preferance
        var loginModel = Gson().fromJson(profileData, LoginModel::class.java)    //convert profile data string to model class
        enableProgress(progressBar!!, requireActivity())                                            //visble progress bar

        //call submit visual ads api interfae post method
        var call: Call<SyncModel> = apiInterface?.submitVisualAds(
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
                    submitBtn?.visibility=View.GONE
                    Toast.makeText(activity, "Data save successfully", Toast.LENGTH_LONG).show()
                    /*   db.deleteVisualAds(visualSendModel.get(0).id.toString())    // delete executed entry from db
                       if (db.getAllSubmitVisual().size > 0)                       // check database if another entries present
                       {
                           sendVisual_api()
                       }
                       else
                       {
                           submitBtn?.setEnabled(false)                           //if visual submit entries is over then button disable again
                       }*/
                } else {
                }
                disableProgress(progressBar!!, requireActivity())

            }

            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
                //on failure of api.
                checkInternet() // check internet connection
                call.cancel()
                disableProgress(progressBar!!, requireActivity()) // disable progress bar
            }
        })
    }

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
                //get and convert save file string to array list

                downloadFilePathLocal.setDownloadType(value.divisionName) //set division name
                downloadFilePathLocal.setBrandId(value.brandId)
                downloadFilePathLocal.setBrandName(value.brandName)
                downloadFilePathLocal.seteDetailingId(value.geteDetailId())
                downloadFilePathList.add(downloadFilePathLocal)
                storedDownloadedList.add(downloadFilePathLocal)

            /*    for ((index, valueDownload) in downloadFilePathLocal?.withIndex()!!)
                {
                    valueDownload.setDownloadType(value.divisionName) //set division name
                    valueDownload.setBrandId(value.brandId) //set brand id in model then add to array list


                    if(index==downloadFilePathLocal.size-1)
                    {
                        downloadFilePathList.add(valueDownload)
                    }
                }*/
            }

            if (index == edetailingList?.size!! - 1) {

                if(downloadFilePathList.size==0)
                {
                        splitViewparent_ll?.visibility=View.GONE
                        noData_tv?.visibility=View.VISIBLE
                }
                else
                {
                    setDownloadListAdapter(downloadFilePathList) //call initilize adapter method when loop on its last itration
                }

            }
        }

        if(edetailingList!!.size==0)
        {
            splitViewparent_ll?.visibility=View.GONE
            noData_tv?.visibility=View.VISIBLE

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

            downloadFilePathLocal.setDownloadType(value.divisionName) //set division name
            downloadFilePathLocal.setBrandId(value.brandId)
            downloadFilePathLocal.setBrandName(value.brandName)
            downloadFilePathLocal.seteDetailingId(value.geteDetailId())

        }
    }

    //-------------------------------------rearrange linkedBrandList according to priority
    fun rearrangeDownloadedList()
    {
        //this method is used to arrange list in Ascending Order with resp to priority of doctor linkedBrandList data
        Collections.sort(favProductDoctorList, object :
                Comparator<SyncModel.Data.Doctor.LinkedBrand> {

            override fun compare(
                    first: SyncModel.Data.Doctor.LinkedBrand?,
                    second: SyncModel.Data.Doctor.LinkedBrand?
            ): Int {
                val p1: SyncModel.Data.Doctor.LinkedBrand =
                        first as SyncModel.Data.Doctor.LinkedBrand
                val p2: SyncModel.Data.Doctor.LinkedBrand =
                        second as SyncModel.Data.Doctor.LinkedBrand
                return p1.priorityOrder.compareTo(p2.priorityOrder)
            }
        })

        var priorityList: ArrayList<DownloadFileModel> = ArrayList()
        var simpleList: ArrayList<DownloadFileModel> = ArrayList()

        //check and add is downloaded list have same brand or not
        for ((index, valueDownload) in downloadFilePathList?.withIndex()!!)
        {
            var found = false
            for ((index, valueFavBrand) in favProductDoctorList?.withIndex()!!)
            {
                if(valueFavBrand.brandId==valueDownload.brandId)
                {
                    found = true
                }
            }
            //if data not found add to simple list
            if (!found)
            {
                simpleList.add(valueDownload)
            }
            // if data is same then add to priority list
            else
            {
                valueDownload.setFileName(valueDownload.fileName + "*")
                priorityList.add(valueDownload)
            }


            if(index== downloadFilePathList?.size!! -1)
            {
                //clear and add all data to downloaded list and call download adapter
                downloadFilePathList.clear()
                storedDownloadedList.clear()
                downloadFilePathList.addAll(priorityList)
                storedDownloadedList.addAll(priorityList)
                downloadFilePathList.addAll(simpleList)
                storedDownloadedList.addAll(simpleList)
                setDownloadListAdapter(downloadFilePathList)

            }

        }
    }

    //-------------------------------------interface override method to get doctor id from bottom sheet adapter
    override fun onClickString(passingInterface: String) {
        doctorIdDisplayVisual= passingInterface?.toInt()!!

        callDownloadFragment()
    }
    //-------------------------------------interface override method to get doctor linkedBrandList from bottom sheet adapter
    override fun onClickDoctor(passingInterfaceList: java.util.ArrayList<SyncModel.Data.Doctor.LinkedBrand>)
    {
        favProductDoctorList.addAll(passingInterfaceList!!)
        //call rearrange linkedBrandList to set priority
        rearrangeDownloadedList()
    }

    //-------------------------------------initilize Visual file recycler view
    fun setDownloadListAdapter(list: ArrayList<DownloadFileModel>)
    {
        adapterVisualFile = VisualFileAdapter(list, requireActivity(), 1, this)
        brand_rv?.setLayoutManager(LinearLayoutManager(requireActivity()));
        brand_rv?.itemAnimator = DefaultItemAnimator()
        brand_rv?.adapter = adapterVisualFile
    }


    //-------------------------------------checkInternet connection
    fun checkInternet()
    {
        if(isInternetAvailable(requireActivity())==true)
        {
            commonAlert(requireActivity(), "Error", "Something went wrong please try again later")
        }
        else
        {
            networkAlert(requireActivity())
        }
    }


     fun getClickedData(message: String)
    {
        Log.e("getClickedData", "message")
    }


    //==========================================DoctorList_BottomSheet adapter==============================================
    class BottomSheetDoctorAdapter(
            public var doctorList: ArrayList<SyncModel.Data.Doctor>,
            var doctor_et: EditText,
            var bottomSheet: BottomSheetBehavior<ConstraintLayout>, var listener: DisplayVisualInterface
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

                //set doctor name to parent edit text
                doctor_et?.setText((modeldata?.doctorName))

                //close bottom sheet
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED)

                //call Display Visual interface method
                listener.onClickString(modeldata?.doctorId.toString())
                listener.onClickDoctor(modeldata?.linkedBrandList!!)


            })
        }
        override fun getItemCount(): Int
        {
            return filteredData?.size!!
        }


        //-------------------------------------filter list using text input from edit text
        override fun getFilter(): Filter? {
            return object : Filter() {
                // publis result when text editing complete
                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    filteredData = results.values as ArrayList<SyncModel.Data.Doctor>?
                    notifyDataSetChanged()
                }
                //filter list from parent list = doctor list
                // and add filter data to = filteredData list
                override fun performFiltering(constraint: CharSequence): FilterResults? {
                    var constraint = constraint
                    val results = FilterResults()
                    val FilteredArrayNames: ArrayList<SyncModel.Data.Doctor> = ArrayList()

                    // perform your search here using the searchConstraint String.
                    constraint = constraint.toString().toLowerCase()
                    for (i in 0 until doctorList?.size!!) {
                        val dataNames: SyncModel.Data.Doctor = doctorList?.get(i)!!
                        if (dataNames.doctorName.toLowerCase().startsWith(constraint.toString())) {
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

        if(favBrand_frame?.visibility==View.VISIBLE)
        {
            onClickFavButton()
        }
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



