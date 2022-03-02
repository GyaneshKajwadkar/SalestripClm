package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import `in`.processmaster.salestripclm.models.GenerateOTPModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.progress_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity : BaseActivity() {

    var apiInterface: APIInterface? = null
    var generateModel: GenerateOTPModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()   //Hiding Toolbar

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        initView()
    }

    fun initView()
    {

        signIn_tv!!.setOnClickListener{ onBackPressed() }

        sendOtp_btn!!.setOnClickListener{

            generalClass.HideKeyboard(currentFocus ?: View(this))

            if(sendOtp_btn?.getText().toString().equals("Change password"))
            {
                if(password_et?.getText().toString().isEmpty())
                {
                    password_et!!.setError("Required")
                    password_et!!.requestFocus()
                    return@setOnClickListener
                }
                if(conPass_et?.getText().toString().isEmpty())
                {
                    conPass_et!!.setError("Required")
                    conPass_et!!.requestFocus()
                    return@setOnClickListener
                }

                if(!TextUtils.equals(conPass_et?.getText().toString(),password_et?.getText().toString()))
                {
                    password_et!!.setError("Password mismatch")
                    password_et!!.requestFocus()
                    return@setOnClickListener
                }
                changePassword_api()
            }
            else
            {
                if(userName_et!!.getText().toString().isEmpty())
                {
                    userName_et!!.setError("Required")
                    userName_et!!.requestFocus()
                    return@setOnClickListener
                }
                if(companyCode_et!!.getText().toString().isEmpty())
                {
                    companyCode_et!!.setError("Required")
                    companyCode_et!!.requestFocus()
                    return@setOnClickListener
                }
                generateOTP_api()
            }
        }

    }

    //check otp alert
    fun checkOTpAlert(empId: String)
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.otpalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val resendOTP=
            dialogView.findViewById<View>(R.id.resendOTP) as TextView
        val verify_et=
            dialogView.findViewById<View>(R.id.verify_et) as EditText
        val verify_btn =
            dialogView.findViewById<View>(R.id.verify_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        val progressBar =
            dialogView.findViewById<View>(R.id.progressBar) as ProgressBar

        resendOTP.setOnClickListener{
            generateOTP_api()
            alertDialog.dismiss()
        }

        verify_btn.setOnClickListener{
            if(verify_et.getText().isEmpty())
            {
                verify_et.setError("Required")
                verify_btn.requestFocus()
                return@setOnClickListener
            }
            //call verify otp api
            verifyOTP_api(progressBar,verify_et.getText().toString(),empId,verify_et,alertDialog)
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    //generateOTp APi
    private fun generateOTP_api()
    {
        apiInterface= APIClientKot().getClient(1, "").create(APIInterface::class.java)
        alertClass.showProgressAlert("")
        var call: Call<GenerateOTPModel> = apiInterface?.generateOtpApi(
            companyCode_et?.getText().toString().uppercase(), userName_et?.getText().toString() , "user"
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(call: Call<GenerateOTPModel?>?, response: Response<GenerateOTPModel?>) {
                Log.e("generateOTp_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    generateModel = response.body()
                    if(generateModel?.getData()?.userData == null)
                    {
                        if(generateModel?.getErrorObj()?.errorMessage!!.equals("Invalid company code"))
                        {
                            companyCode_et?.setError(generateModel?.getErrorObj()?.errorMessage)
                            companyCode_et?.requestFocus()
                        }
                        else
                        {
                            userName_et?.setError(generateModel?.getErrorObj()?.errorMessage)
                            userName_et?.requestFocus()
                        }

                    }
                    else
                    {
                        Toast.makeText(this@ForgotActivity,generateModel?.getData()?.message,Toast.LENGTH_LONG).show()
                        checkOTpAlert(generateModel?.getData()?.userData?.empId.toString())
                    }
                }

                else
                {
                }
                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    //verifyeOTp APi
    private fun verifyOTP_api(progressBar: ProgressBar, otpString: String, empId: String, editText: EditText, alertDialog: AlertDialog)
    {
        apiInterface= APIClientKot().getClient(1, "").create(APIInterface::class.java)
        alertClass.showProgressAlert("")

        var call: Call<GenerateOTPModel> = apiInterface?.verifyOtpAPI(
            companyCode_et?.getText().toString().uppercase(), empId , otpString
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(call: Call<GenerateOTPModel?>?, response: Response<GenerateOTPModel?>) {
                Log.e("verifyeOTp_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var generateModel = response.body()

                    //check otp response
                    if(generateModel?.getData()?.isOTPMatched == true)
                    {
                        forgotParent_ll?.visibility = View.GONE
                        newPassParent_ll?.visibility = View.VISIBLE
                        sendOtp_btn?.setText("Change password")
                        alertDialog.dismiss()
                    }
                    else
                    {
                        editText.setError("Invalid OTP")
                        editText.requestFocus()
                    }
                }

                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()            }
        })
    }

    //changePassword APi
    private fun changePassword_api()
    {
        apiInterface= APIClientKot().getClient(1, "").create(APIInterface::class.java)

        alertClass.showProgressAlert("")

        var call: Call<GenerateOTPModel> = apiInterface?.changePassAPI(
            companyCode_et?.getText().toString().uppercase(), generateModel?.getData()?.userData?.empId.toString() , conPass_et?.getText().toString()
        ) as Call<GenerateOTPModel>
        call.enqueue(object : Callback<GenerateOTPModel?> {
            override fun onResponse(call: Call<GenerateOTPModel?>?, response: Response<GenerateOTPModel?>) {
                Log.e("generateOTp_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var generateModel = response.body()

                    if(generateModel?.getData()?.message.isNullOrEmpty())
                    {
                        Toast.makeText(this@ForgotActivity,generateModel?.getErrorObj()?.errorMessage,Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(this@ForgotActivity,generateModel?.getData()?.message,Toast.LENGTH_LONG).show()
                        onBackPressed()
                        finish()
                    }
                }

                else
                {
                }

                alertClass.hideAlert()             }

            override fun onFailure(call: Call<GenerateOTPModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

}