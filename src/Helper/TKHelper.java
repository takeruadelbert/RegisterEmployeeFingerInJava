package Helper;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author STN-COM-01
 */
public class TKHelper {

    private static final String CONFIG_JSON_FILE = "src/session/data.json";
    private static Cipher ecipher;
    private static Cipher dcipher;
    public static String DES_key;
    private static final int MAX_TEMPLATE_LENGTH = 2200;

    public static String encryptStringToSHA512Format(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);
        while (hashText.length() < 32) {
            hashText += "0";
        }
        return hashText;
    }

    public static JSONObject readJSONFile() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(CONFIG_JSON_FILE));
            return data;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean updateJSONFileSingleDataWithParent(String type, String key, String value) {
        try {
            JSONObject data = readJSONFile();
            JSONObject temp = (JSONObject) data.get(type);
            temp.put(key, value);

            FileWriter file = new FileWriter(CONFIG_JSON_FILE);
            file.write(data.toJSONString());
            file.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateJSONFileSingleData(String key, String value) {
        try {
            JSONObject data = readJSONFile();
            data.put(key, value);
            System.out.println("data = " + data);

            FileWriter file = new FileWriter(CONFIG_JSON_FILE);
            file.write(data.toJSONString());
            file.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateJSONFile(JSONObject data) {
        try {
            FileWriter file = new FileWriter(CONFIG_JSON_FILE);
            file.write(data.toJSONString());
            file.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // using DES (Data Encryption Standard) Algorithm
    public static String encryptString(String input) {
        try {
            SecretKey key;
            if (getDESKey().isEmpty()) {
                // generate secret key using DES algorithm
                key = KeyGenerator.getInstance("DES").generateKey();
                DES_key = new String(Base64.getEncoder().encode(key.getEncoded()));
            } else {
                DES_key = getDESKey();
                key = convertStringToSecretKey(DES_key);
            }

            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] utf8 = input.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            enc = BASE64EncoderStream.encode(enc);
            return new String(enc);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String decryptString(String encryptedInput) {
        try {
            dcipher = Cipher.getInstance("DES");
            String temp_key = getDESKey();
            if (!temp_key.isEmpty()) {
                SecretKey key = convertStringToSecretKey(temp_key);
                dcipher.init(Cipher.DECRYPT_MODE, key);

                byte[] dec = BASE64DecoderStream.decode(encryptedInput.getBytes());
                byte[] utf8 = dcipher.doFinal(dec);
                return new String(utf8, "UTF8");
            } else {
                System.out.println("Failed to Decrypt.");
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void discardDESKeyAttribute() {
        DES_key = null;
    }

    private static String getDESKey() {
        JSONObject data = readJSONFile();
        String DES_key = data.get("DES_key").toString();
        return DES_key;
    }

    private static SecretKey convertStringToSecretKey(String string_key) {
        byte[] encodedKey = Base64.getDecoder().decode(string_key);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
        return key;
    }

    public static String CalculatePercentageTemplateFingerprint(int templateLength) {
        if (templateLength > 0) {
            double result = (templateLength * 1.00 / MAX_TEMPLATE_LENGTH * 1.00) * 100;
            return (int) result + "%";
        } else {
            return "-";
        }
    }

    public static String GetFingerType(int indexFinger) {
        String fingerType = "";
        switch (indexFinger) {
            case 0:
                fingerType = "Left Pinky";
                break;
            case 1:
                fingerType = "Left Ring";
                break;
            case 2:
                fingerType = "Left Middle";
                break;
            case 3:
                fingerType = "Left Index";
                break;
            case 4:
                fingerType = "Left Thumb";
                break;
            case 5:
                fingerType = "Right Thumb";
                break;
            case 6:
                fingerType = "Right Index";
                break;
            case 7:
                fingerType = "Right Middle";
                break;
            case 8:
                fingerType = "Right Ring";
                break;
            default:
                fingerType = "Right Pinky";
                break;
        }
        return fingerType;
    }
}
