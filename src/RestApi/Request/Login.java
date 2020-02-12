/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestApi.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author takeru
 */
@Data
@AllArgsConstructor
public class Login {

    private String username;
    private String password;
}
