package org.cshaifasweng.winter.web;

import org.cshaifasweng.winter.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface LilachService {
    @GET("catalog")
    Call<List<CatalogItem>> getAllItems();

    @PUT("catalog/{id}")
    Call<Void> updateItem(@Path("id") long id, @Body CatalogItem item);

    @POST("/customer")
    Call<Customer> newCustomer(@Body Customer customer);

    @GET("/login")
    Call<Void> login(@Query("email") String email, @Query("password") String password);

    @GET("/logout")
    Call<Void> logout();

    @GET("/user/me")
    Call<User> getCurrentUser();

    @POST("/complaint")
    Call<Complaint> newComplaint(@Body Complaint complaint);

    @PUT("/complaint/{id}")
    Call<Complaint> handleComplaint(@Path("id") long id, @Body Complaint complaint);

    @GET("/store")
    Call<List<Store>> getAllStores();

    @GET("/store/{id}/catalog")
    Call<List<CatalogItem>> getCatalogByStore(@Path("id") long id);

}
