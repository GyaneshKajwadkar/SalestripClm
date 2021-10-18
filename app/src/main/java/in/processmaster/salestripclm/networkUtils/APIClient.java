package in.processmaster.salestripclm.networkUtils;

import android.util.Log;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {


    private static Retrofit retrofit = null;


    public static Retrofit getClient(int parent, String childUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        String commonUrl="";

        //if parent is one then used this default base url
        if(parent==1)
        {
            //parentUrl
            commonUrl="https://app.salestrip.in/api/";
        }
        // if parent is not one then use dynamic url
        else
          {
              //childUrl
          commonUrl=childUrl;
              Log.e("theChildUrlids",commonUrl);
          }

        retrofit = new Retrofit.Builder()
                .baseUrl(commonUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }


}
