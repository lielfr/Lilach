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

    @POST("/catalog")
    Call<CatalogItem> newCatalogItem(@Body CatalogItem item);

    @PUT("/customer/{id}")
    Call<Customer> updateCustomer(@Path("id") long id, @Body Customer customer);

    @POST("/order")
    Call<Order> newOrder(@Body Order order);

    @DELETE("/order/{id}")
    Call<Void> cancelOrder(@Path("id") long id);

    @GET("/customer/{id}/orders")
    Call<List<Order>> getOrdersByCustomer(@Path("id") long id);

}
