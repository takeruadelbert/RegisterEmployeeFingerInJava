/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author takeru
 */
@Data
@AllArgsConstructor
public class User implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("bidoata")
    private Biodata biodata;
}
