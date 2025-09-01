package com.professionalnetworking.userservice.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash a password for the first time
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Check that an unencrypted password matches one that has
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
