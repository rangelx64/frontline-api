package com.ffx64.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Gera hash da senha usando BCrypt
     * 
     * @param rawPassword Senha em texto plano
     * @return Hash da senha
     */
    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Verifica se a senha corresponde ao hash
     * 
     * @param rawPassword    Senha em texto plano
     * @param hashedPassword Hash da senha
     * @return true se a senha está correta, false caso contrário
     */
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
