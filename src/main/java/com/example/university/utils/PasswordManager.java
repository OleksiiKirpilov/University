package com.example.university.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class PasswordManager {

    private static final int SALT_LENGTH = 16;
    private static final String SYMBOLS =
            "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "0123456789" + "!@#$%&*()_+-=[]|,./?><";

    private PasswordManager() {
    }

    /**
     * Calculates the hash of the given password and salt.
     * SHA-256 algorithm is used.
     * @param password represents user's password
     * @param salt value which will be concatenated to password as 'salt'
     * @return salted password hash value
     */
    public static String hash( String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String toHash = password + salt;
        byte[] encodedHash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    /**
     * Generates salt with default length
     * @return String - salt
     */
    public String generateSalt(){
        int length = SALT_LENGTH;
        StringBuilder text = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < length; ++i){
            int position = random.nextInt(SYMBOLS.length());
            text.append(SYMBOLS.charAt(position));
        }
        return text.toString();
    }

    /**
     * Checks if a given password's salted hash is equal to the hash value which we expect
     * @param password represents actual password
     * @param salt user's salt
     * @param expectedHash hash value which we expect to get
     * @return true if password is ok
     */
    public static boolean isExpectedPassword(String password, String salt,
                                             String expectedHash) throws NoSuchAlgorithmException {
        String hash = hash(password, salt);
        return expectedHash.equals(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            byteToHexSB(b, hexString);
        }
        return hexString.toString();
    }

    private static void byteToHexSB(byte number, StringBuilder sb) {
        // convert signed byte to unsigned int
        int n = 256 + number & 255;
        char highNibble = (char) (n / 16 + '0');
        char lowNibble = (char) (n % 16 + '0');
        highNibble += (highNibble > '9') ? 7 : 0;
        lowNibble += (lowNibble > '9') ? 7 : 0;
        sb.append(highNibble).append(lowNibble);
    }

}
