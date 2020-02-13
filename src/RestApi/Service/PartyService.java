/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Service;

import RestApi.Endpoint.EndPoint;
import RestApi.Request.RequestDataPartyFingerprint;
import RestApi.Response.Party;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author takeru
 */
public interface PartyService {

    @GET(EndPoint.API_URL_SEARCH_EMPLOYEE_BY_NIK)
    Call<Party> apiSearchEmployeeByNik(@Query("nik") String nik);

    @PUT(EndPoint.API_URL_REGISTER_FINGERPRINT)
    Call<ResponseBody> apiRegisterFingerprint(@Path("partyId") Integer partyId, @Body RequestDataPartyFingerprint requestDataPartyFingerprint);
}
