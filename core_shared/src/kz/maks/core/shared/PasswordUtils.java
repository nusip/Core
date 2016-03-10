package kz.maks.core.shared;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    private static final String ALGORITHM = "MD5";

    public static String getPasswordHash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            byte[] digest = messageDigest.digest(password.getBytes());
            return new HexBinaryAdapter().marshal(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getPasswordHash("maks"));
    }

}
