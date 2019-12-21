package org.cshaifasweng.winter;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class APIAccess {
    private static LilachService service = null;
    private static String BASE_URL = "http://localhost:8080/";
    public static LilachService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            service = retrofit.create(LilachService.class);
        }

        return service;
    }

    public static void setAddress(String host, String port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }
}
