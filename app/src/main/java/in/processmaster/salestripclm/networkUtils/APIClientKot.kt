package `in`.processmaster.salestripclm.networkUtils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClientKot {

    fun getUsersService(parent : Int, childUrl : String): APIInterface{
        var commonUrl = ""

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        if (parent == 1) commonUrl = "https://app.salestrip.in/api/"
         else  commonUrl = childUrl


        return Retrofit.Builder()
            .client(client)
            .baseUrl(commonUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    fun getClient(parent: Int, childUrl: String?): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        var commonUrl: String? = ""

        // commonUrl="https://pms-test.azurewebsites.net/";
        commonUrl = if (parent == 1)  "https://app.salestrip.in/api/"
        else  childUrl


        val retrofit = Retrofit.Builder()
            .baseUrl(commonUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit
    }
}