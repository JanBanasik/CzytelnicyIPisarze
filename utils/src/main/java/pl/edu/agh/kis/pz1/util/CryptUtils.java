package pl.edu.agh.kis.pz1.util;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptUtils {

    public static String sha512(String str) {
        return DigestUtils.sha1Hex(str);
    }

}
