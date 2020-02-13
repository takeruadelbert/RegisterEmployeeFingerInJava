/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author takeru
 */
@Data
@NoArgsConstructor
public class Fingerprint implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("employeeId")
    private Long employeeId;

    @SerializedName("fingerType")
    private Integer fingerType;

    @SerializedName("templateFingerprint")
    private String templateFingerprint;

    @SerializedName("templateFingerprintLength")
    private Integer templateFingerprintLength;
}
