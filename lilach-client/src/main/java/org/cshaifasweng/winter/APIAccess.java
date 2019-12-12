package org.cshaifasweng.winter;

import retrofit2.Retrofit;

public class APIAccess {
    private static LilachService service = null;
    private static final String BASE_URL = "http://localhost:8080/api/";
    public static LilachService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
            service = retrofit.create(LilachService.class);
        }

        return service;
    }
}
