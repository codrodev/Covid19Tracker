package com.android.dubaicovid19.utility;

import java.io.IOException;

public class Exceptions {
    public static class ApiException extends IOException{
        String message;
        public ApiException(String message){
            this.message =message;
        }

    }
    public static class NoInternetException extends IOException{
        String message;
        public NoInternetException(String message){
            this.message =message;
        }

    }

}
