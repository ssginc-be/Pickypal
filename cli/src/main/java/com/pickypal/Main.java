package com.pickypal;

import com.pickypal.screen.LoginScreen;
import com.pickypal.screen.MainScreen;
import com.pickypal.screen.SignUpScreen;

import java.io.IOException;

/**
 * @author Queue-ri
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MainScreen mainScreen = new MainScreen();
        LoginScreen loginScreen = new LoginScreen();
        SignUpScreen signUpScreen = new SignUpScreen();

        int screenNum = mainScreen.start();
        switch (screenNum) {
            case 1: loginScreen.start(); break;
            case 2: signUpScreen.start(); break;
        }
    }
}