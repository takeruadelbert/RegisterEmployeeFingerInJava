/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi;

import Helper.TKHelper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author takeru
 */
public class ServiceGenerator {

    private static final Integer TIMEOUT = 5; // in second(s)
    private static final String HTTP_PROTOCOL = "http://";

    private static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        return okhttpBuilder;
    }

    private static HttpLoggingInterceptor interceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public static <S> S createBaseService(Class<S> serviceClass) {
        OkHttpClient.Builder builder = builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newReq = request.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", TKHelper.getTokenBearer())
                        .build();
                return chain.proceed(newReq);
            }
        });

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTP_PROTOCOL + TKHelper.getHost())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }
}
