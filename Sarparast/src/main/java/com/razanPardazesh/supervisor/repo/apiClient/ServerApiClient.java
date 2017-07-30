package com.razanPardazesh.supervisor.repo.apiClient;

import android.content.Context;

import com.example.zikey.sarparast.BuildConfig;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zikey on 03/06/2017.
 */

public class ServerApiClient {

    private static Retrofit retrofitWithHeader = null;
    private static Retrofit retrofitWithoutHeader = null;

    private static OkHttpClient.Builder initHeader(final Context context) {

        final PreferenceHelper preferenceHelper = new PreferenceHelper(context);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("xAthorize", preferenceHelper.getString(PreferenceHelper.TOKEN_ID))
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);
//                if (response.code() == 401) {
//                    SessionManagement.getInstance(context).clearMemberData();
//                    LoginActivity.start(context);
//                    System.exit(0);
//                }

                return chain.proceed(request);
            }
        });
        return httpClient;
    }


    public static Retrofit getClient(Context context) {

        final PreferenceHelper preferenceHelper = new PreferenceHelper(context);

        if (retrofitWithoutHeader == null) {
            retrofitWithoutHeader = new Retrofit.Builder()
                    .baseUrl("http://" + preferenceHelper.getString(NetworkTools.URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithoutHeader;
    }

    public static Retrofit getClientWithHeader(Context context) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(context);

        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        url = url.replace(":75", "");
        url = url+ BuildConfig.PORT;

        if (retrofitWithHeader == null) {
            retrofitWithHeader = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(initHeader(context).build())
                    .build();
        }
        return retrofitWithHeader;
    }


}
