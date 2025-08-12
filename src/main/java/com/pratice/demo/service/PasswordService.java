package com.pratice.demo.service;


public interface PasswordService {

    String encryptPassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
