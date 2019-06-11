package com.orestis.velen.quiz.login.facebookSignIn;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class FacebookSession {

    private static FacebookSession instance;

    private FacebookSession(){}

    public static FacebookSession getInstance() {
        if(instance == null) {
            instance =  new FacebookSession();
        }
        return instance;
    }

    public AccessToken getLastFacebookToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public void signOut() {
        LoginManager.getInstance().logOut();
    }
}
