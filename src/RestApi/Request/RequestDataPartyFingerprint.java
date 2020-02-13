/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author root
 */
@Data
@AllArgsConstructor
public class RequestDataPartyFingerprint implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("data")
    private RequestDataFingerprint fingerprint;
}
