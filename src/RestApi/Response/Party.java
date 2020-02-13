/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author takeru
 */
@Data
@NoArgsConstructor
public class Party implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("nik")
    private String nik;

    @SerializedName("dataFingerprints")
    private List<Fingerprint> fingerprints;
}
