package ex_02_Create_Classes.utils;

import java.util.Base64;

public class Base64Decoder {

    public static String decode(String encodedBase64) {
        byte[] decode = Base64.getUrlDecoder().decode(encodedBase64);

        StringBuilder sb = new StringBuilder();
        for (byte b : decode) {
            sb.append((char) b);
        }

        return sb.toString();
    }
}
