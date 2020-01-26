package org.cshaifasweng.winter.web;

import okhttp3.OkHttpClient;
import org.cshaifasweng.winter.models.User;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIAccess {
    private static LilachService service = null;
    private static String BASE_URL = "http://localhost:8080/";
    private static User currentUser = null;
    public static LilachService getService() {
        if (service == null) {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

            TokenInterceptor tokenInterceptor = new TokenInterceptor();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(tokenInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            service = retrofit.create(LilachService.class);
        }

        return service;
    }

    public static void setAddress(String host, String port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getImageUrl(String image) {
        return BASE_URL + "image/" + image;
    }
}
