package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.BuildConfig
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.GenerateOTPModel
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.ProfileModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.progress_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.util.regex.Matcher
import java.util.regex.Pattern
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileeActivity : BaseActivity() {

    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_GALLERY_PHOTO = 2
    var mPhotoFile: File? = null


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        progressView_parentRv?.visibility=View.VISIBLE
        getProfileDataApi()

        personal_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
        personal_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
        personal_mb.iconTint=ContextCompat.getColorStateList(this, R.color.white)
        personalParent_ll.visibility= View.VISIBLE


        personal_mb.setOnClickListener({
            setToDefaultAll()
            personal_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
            personal_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
            personal_mb.iconTint=ContextCompat.getColorStateList(this, R.color.white)
            personalParent_ll.visibility= View.VISIBLE
        })

        contact_mb.setOnClickListener({
            setToDefaultAll()
            contact_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
            contact_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
            contact_mb.iconTint=ContextCompat.getColorStateList(this, R.color.white)
            contact_ll.visibility= View.VISIBLE

        })

        territory_mb.setOnClickListener({
            setToDefaultAll()
            territory_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.appColor))
            territory_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
            territory_mb.iconTint=ContextCompat.getColorStateList(this, R.color.white)
            territory_ll.visibility= View.VISIBLE
        })

        changeProfile_mb.setOnClickListener({
            selectImage()
        })

        backProfile.setOnClickListener({
            onBackPressed()
        })

        menuProfile?.setOnClickListener({

            val menuBuilder = MenuBuilder(this)
            val inflater = MenuInflater(this)
            inflater.inflate(R.menu.profilemenu, menuBuilder)
            val optionsMenu = MenuPopupHelper(this, menuBuilder, menuProfile!!)
            optionsMenu.setForceShowIcon(true)

            // Set Item Click Listener
            menuBuilder.setCallback(object : MenuBuilder.Callback {

                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.changepass_menu -> {
                            changePasswordAlert()
                        }
                    }
                    return true
                }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })
            // Display the menu
            optionsMenu.show()
        })

    }

    // call camera
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    //get real path from uri
    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    //set all view to default
    fun setToDefaultAll()
    {
        personal_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray))
        contact_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray))
        territory_mb.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray))

        personal_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.black))
        contact_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.black))
        territory_mb.setTextColor(ContextCompat.getColorStateList(this, R.color.black))

        personal_mb.iconTint=ContextCompat.getColorStateList(this, R.color.black)
        contact_mb.iconTint=ContextCompat.getColorStateList(this, R.color.black)
        territory_mb.iconTint=ContextCompat.getColorStateList(this, R.color.black)

        territory_ll.visibility= View.GONE
        contact_ll.visibility= View.GONE
        personalParent_ll.visibility= View.GONE

    }

    //Select image source
    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            "Take Photo", "Choose from Library",
            "Cancel"
        )
        val builder = AlertDialog.Builder(this)
        builder.setItems(
            items
        ) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                dispatchTakePictureIntent();
            } else if (items[item] == "Choose from Library") {
                dispatchGalleryIntent()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    //Result for selected image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    //   mPhotoFile = mCompressor.compressToFile(mPhotoFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Glide.with(this)
                    .load(mPhotoFile)
                    .apply(
                        RequestOptions().centerCrop()
                            .circleCrop()
                            .placeholder(R.drawable.zm_menu_icon_profile)
                    )
                    .into(changeProfilePic_civ)
                updateProfilePic()
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data?.data
                try {
                    mPhotoFile = /*mCompressor.compressToFile*/(File(getRealPathFromUri(selectedImage)))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Glide.with(this)
                    .load(mPhotoFile)
                    .apply(
                        RequestOptions().centerCrop()
                            .circleCrop()
                            .placeholder(R.drawable.zm_menu_icon_profile)
                    )
                    .into(changeProfilePic_civ)
                updateProfilePic()
            }
        }


    }

    //select using gallery
    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO)
    }


    //change password dialog
    fun changePasswordAlert() {

        val loginActivityWeakRef: WeakReference<Activity> = WeakReference<Activity>(this)

        if (loginActivityWeakRef.get()?.isFinishing() == false) {

            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView: View = inflater.inflate(R.layout.changepassword_alert, null)

            dialogBuilder.setView(dialogView)

            var alertDialog = dialogBuilder.create()

            alertDialog?.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

            val changepassword_parentl=dialogView.findViewById<View>(R.id.changepassword_parentll)as LinearLayout
            val guidlines_parentll=dialogView.findViewById<View>(R.id.guidlines_parentll)as LinearLayout
            val guidlines_tv=dialogView.findViewById<View>(R.id.guidlines_tv)as TextView

            val currentpassword_et=dialogView.findViewById<View>(R.id.currentpassword_et)as EditText
            val newpassword_et=dialogView.findViewById<View>(R.id.newpassword_et)as EditText
            val confirmpassword_et=dialogView.findViewById<View>(R.id.confirmpassword_et)as EditText

            val greaterGuidline_iv=dialogView.findViewById<View>(R.id.greaterGuidline_iv)as ImageView
            val multipleGuidline_iv=dialogView.findViewById<View>(R.id.multipleGuidline_iv)as ImageView
            val endGuidline_iv=dialogView.findViewById<View>(R.id.endGuidline_iv)as ImageView

            val okBtn_rl = dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout
            val cancelBtn_rl = dialogView.findViewById<View>(R.id.cancelBtn_rl) as RelativeLayout

            val progressViewDilog = dialogView.findViewById<View>(R.id.progressView_parentRv) as RelativeLayout

            newpassword_et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                )
                {
                    if (s.length != 0)
                    {
                        if(checkStringLength(s.toString()))
                        {
                            greaterGuidline_iv.setBackgroundResource(R.drawable.ic_checked)
                        }
                        else
                        {
                            greaterGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                        }
                        if(checkMultipleGuidlines(s.toString()))
                        {
                            multipleGuidline_iv.setBackgroundResource(R.drawable.ic_checked)
                        }
                        else
                        {
                            multipleGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                        }

                        if(checkEndGuidlines(s.toString()))
                        {
                            endGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                        }
                        else
                        {
                            endGuidline_iv.setBackgroundResource(R.drawable.ic_checked)
                        }
                    }
                    else
                    {
                        multipleGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                        greaterGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                        endGuidline_iv.setBackgroundResource(R.drawable.ic_cross)
                    }
                }
            })


            okBtn_rl.setOnClickListener {

                if(currentpassword_et.text.toString().isEmpty())
                {
                    currentpassword_et.requestFocus()
                    currentpassword_et.setError("Required")
                    return@setOnClickListener
                }
                if(newpassword_et.text.toString().isEmpty())
                {
                    newpassword_et.requestFocus()
                    newpassword_et.setError("Required")
                    return@setOnClickListener
                }

                if(!checkStringLength(newpassword_et.text.toString()) || !checkMultipleGuidlines(newpassword_et.text.toString()) || checkEndGuidlines(newpassword_et.text.toString()) )
                {
                    newpassword_et.requestFocus()
                    newpassword_et.setError("Wrong format")
                    return@setOnClickListener
                }

                if(confirmpassword_et.text.toString().isEmpty())
                {
                    confirmpassword_et.requestFocus()
                    confirmpassword_et.setError("Required")
                    return@setOnClickListener
                }

                if(!confirmpassword_et.text.toString().equals(newpassword_et.text.toString()))
                {
                    confirmpassword_et.requestFocus()
                    confirmpassword_et.setError("Password mismatch")
                    newpassword_et.setError("Password mismatch")

                    return@setOnClickListener
                }
                GeneralClass(this).HideKeyboard(it)
                changePasswordApi(currentpassword_et,newpassword_et,alertDialog,progressViewDilog)

            }

            cancelBtn_rl.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog?.show()
        }


    }

    //check password string length
    fun checkStringLength(string: String):Boolean
    {
        if(string.length>8)
        {
            return true
        }
        else{
            return false
        }

    }

    //validate multiple guidlines like capital, small letter, special character and number
    fun checkMultipleGuidlines(string: String):Boolean
    {
        return if (string.length > 0) {
            val letter: Pattern = Pattern.compile("[a-z]")
            val letterCapital: Pattern = Pattern.compile("[A-Z]")
            val digit: Pattern = Pattern.compile("[0-9]")
            val special: Pattern = Pattern.compile("[!@#$%*()_+=|<>?{}\\[\\]~-]")

            val hasLetter: Matcher = letter.matcher(string)
            val hasDigit: Matcher = digit.matcher(string)
            val hasSpecial: Matcher = special.matcher(string)
            val hasCapital: Matcher = letterCapital.matcher(string)
            hasLetter.find() && hasDigit.find() && hasSpecial.find() && hasCapital.find()
        }

        else false
    }

    //check new password containes & key
    fun checkEndGuidlines(string: String):Boolean
    {
        return if (string.length > 0)
        {
            val special: Pattern = Pattern.compile("[&]")
            val hasSpecial: Matcher = special.matcher(string)
            hasSpecial.find()
        }
        else false
    }

    fun getProfileDataApi()
    {
        var  apiInterface= APIClient.getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<ProfileModel> = apiInterface?.getProfileData(
            "bearer " + loginModelBase?.accessToken,
            loginModelBase.empId.toString()
        ) as Call<ProfileModel>
        call.enqueue(object : Callback<ProfileModel?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<ProfileModel?>?,
                response: Response<ProfileModel?>
            )
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getObject=response.body()?.getData()?.users?.get(0)

                    val options: RequestOptions = RequestOptions()
                        .centerCrop()
                        .placeholder(android.R.mipmap.sym_def_app_icon)
                        .error(android.R.mipmap.sym_def_app_icon)

                    Glide.with(this@ProfileeActivity).load(getObject?.absolutePath).apply(options).into(changeProfilePic_civ!!)


                    // dateOfbirth_tv.setText(getObject?.dateOfBirth)
                    gender_tv.setText(if(getObject?.gender == 1) "Male" else "Female")
                    maritalStatus_tv.setText(if(getObject?.marriedStatus == 1) "Un-Married" else "Married")
                    email_tv.setText(getObject?.emailId)

                    address_tv.setText(getObject?.address1+ " "+ getObject?.address2 )
                    city_tv.setText(getObject?.cityName )
                    state_tv.setText(getObject?.stateName)
                    country_tv.setText(getObject?.countryName)
                    phoneNumber_tv.setText(getObject?.phone)
                    mobile_tv.setText(getObject?.mobileNo)
                    pincode_tv.setText(getObject?.pinCode!!)
                    divison_tv.setText(getObject?.divisionName)
                    headquarter_tv.setText(getObject?.headQuaterName)
                    manager_tv.setText(getObject?.reportingManagerName)

                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    try
                    {
                        val d = sdf.parse(getObject?.joiningDate)
                        val d2 = sdf.parse(getObject?.dateOfBirth)
                        sdf.applyPattern("dd MMM yyyy")

                        joiningDate_tv.setText(sdf.format(d))
                        dateOfbirth_tv.setText(sdf.format(d2))
                    }
                    catch (ex: ParseException)
                    {
                        Log.v("Exception", ex.getLocalizedMessage())
                    }

                    name_tv.setText(getObject?.fullName)
                    designation_tv.setText(getObject?.hierDesc)

                    // fieldWork_id.setText(getObject?.field)
                    // degree_tv.setText(getObject?.)
                    // anniversary_tv.setText(if(getObject?.marriedStatus == 1) "Un-Married" else "Married")
                }
                else
                {
                    Toast.makeText(this@ProfileeActivity, "Server error ", Toast.LENGTH_SHORT).show()
                }
                progressView_parentRv?.visibility=View.GONE
            }

            override fun onFailure(call: Call<ProfileModel?>, t: Throwable?) {
                call.cancel()
                progressView_parentRv?.visibility=View.GONE
            }
        })
    }

    fun changePasswordApi(
        oldPassword: EditText,
        newPassword: EditText,
        alertDialog: AlertDialog,
        progressViewDilog: RelativeLayout
    )
    {
        progressViewDilog?.visibility=View.VISIBLE

        Log.e("callingApi","ChangePasswordApi")

        val paramObject = JSONObject()
        paramObject.put("empId",       loginModelBase.empId.toString())
        paramObject.put("oldPassword", oldPassword.text.toString())
        paramObject.put("newPassword", newPassword.text.toString())

        var call: Call<GenerateOTPModel> = getSecondaryApiInterface().changePassword(
            "bearer " + loginModelBase?.accessToken,
            paramObject
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GenerateOTPModel?>?,
                response: Response<GenerateOTPModel?>
            )
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getObject=response.body()

                    if(getObject?.data?.message.toString().equals("Password changed successfully"))
                    {
                        Toast.makeText(this@ProfileeActivity,getObject?.data?.message,Toast.LENGTH_LONG).show()
                        alertDialog?.dismiss()
                    }
                    else
                    {
                        oldPassword.requestFocus()
                        oldPassword.setError(getObject?.errorObj?.errorMessage)
                    }
                }
                else
                {
                    Toast.makeText(this@ProfileeActivity, "Server error ", Toast.LENGTH_SHORT).show()
                }
                progressViewDilog?.visibility=View.GONE
            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                call.cancel()
                progressViewDilog?.visibility=View.GONE

            }
        })
    }

    fun updateProfilePic()
    {
        progressView_parentRv?.visibility=View.VISIBLE

        val filePart = MultipartBody.Part.createFormData(
            "File1",
            mPhotoFile?.getName(),
            RequestBody.create(MediaType.parse("image/*"), mPhotoFile))


        val paramObject = JSONObject()
        paramObject.put("EmpId",     loginModelBase.empId.toString())
        paramObject.put("ImageName", mPhotoFile?.getName() )
        paramObject.put("ImagePath", mPhotoFile?.absolutePath.toString())
        paramObject.put("ImageExt",  mPhotoFile?.absolutePath?.substring(mPhotoFile?.absolutePath?.lastIndexOf(".")?.plus(1)!!))
        paramObject.put("AbsolutePath", mPhotoFile?.absolutePath.toString())

        var reqBody = RequestBody.create(MediaType.parse("text/plain"), paramObject.toString());

        var call: Call<GenerateOTPModel> = getSecondaryApiInterface().changeProflePic(
            "bearer " + loginModelBase?.accessToken,
            filePart,reqBody
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GenerateOTPModel?>?,
                response: Response<GenerateOTPModel?>
            )
            {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getObject=response.body()
                    loginModelBase.imageName=getObject?.data?.imageName.toString()

                    val gson = Gson()
                    sharePreferanceBase?.setPref("profileData", gson.toJson(loginModelBase))

                    var profileData =sharePreferanceBase?.getPref("profileData")
                    loginModelBase = Gson().fromJson(profileData, LoginModel::class.java)
                }
                else
                {
                    Toast.makeText(this@ProfileeActivity, "Server error ", Toast.LENGTH_SHORT).show()

                }

                progressView_parentRv?.visibility=View.GONE
            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                call.cancel()
                progressView_parentRv?.visibility=View.GONE
                Toast.makeText(this@ProfileeActivity, "Server error ", Toast.LENGTH_SHORT).show()

            }
            //TechnoSpark

        })

    }

}