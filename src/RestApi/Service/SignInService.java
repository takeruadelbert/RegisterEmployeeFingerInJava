/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Service;

import RestApi.Endpoint.EndPoint;
import RestApi.Request.Login;
import RestApi.Response.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *
 * @author takeru
 */
public interface SignInService {

    @POST(EndPoint.API_URL_LOGIN)
    Call<ResponseBody> apiSignIn(@Body Login login);

    @GET(EndPoint.API_URL_FETCH_DATA_LOGIN)
    Call<User> apiGetDataSession();
}
