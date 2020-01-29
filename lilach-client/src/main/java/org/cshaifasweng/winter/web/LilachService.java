package org.cshaifasweng.winter.web;

import okhttp3.MultipartBody;
import org.cshaifasweng.winter.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface LilachService {
    @GET("catalog")
    Call<List<CatalogItem>> getAllItems();

    @PUT("/store/{sid}/catalog/{id}")
    Call<CatalogItem> updateItem(@Path("id") long id, @Path("sid") long storeId, @Body CatalogItem item);

    @DELETE("catalog/{id}")
    Call<Void> deleteItem(@Path("id") long id);

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

    @GET("/store/{id}/catalog")
    Call<List<CatalogItem>> getCatalogByStore(@Path("id") long id, @Query("singleItems") boolean singleItems);

    @GET("/store/{id}/catalog")
    Call<List<CatalogItem>> searchCatalogByStore(@Path("id") long id, @Query("search") String keywords);

    @POST("/store/{id}/catalog")
    Call<CatalogItem> newCatalogItem(@Path("id") long id, @Body CatalogItem item);

    @PUT("/customer/{id}")
    Call<Customer> updateCustomer(@Path("id") long id, @Body Customer customer);

    @POST("/order")
    Call<Order> newOrder(@Body Order order);

    @DELETE("/order/{id}")
    Call<Void> cancelOrder(@Path("id") long id);

    @GET("/customer/{id}/orders")
    Call<List<Order>> getOrdersByCustomer(@Path("id") long id);

    @GET("/customer/{id}/complaints")
    Call<List<Complaint>> getComplaintsByCustomer(@Path("id") long id);

    @Multipart
    @POST("/image")
    Call<String> uploadImage(@Part MultipartBody.Part image);

    @POST("/custom")
    Call<CustomItem> newCustomItem(@Body CustomItem item);

    @DELETE("/custom/{id}")
    Call<Void> deleteCustomItem(@Path("id") long id);

    @PUT("/order/{id}/delivered")
    Call<Void> markOrderAsDelivered(@Path("id") long id);

    @PUT("/employee/{id}")
    Call<Employee> updateEmployee(@Path("id") long id, @Body Employee employee);

    @GET("/user")
    Call<List<User>> getUsers(@Query("store") long storeId, @Query("type") UserType type);

    @GET("/user")
    Call<List<User>> getUsers(@Query("type") UserType type);

    @GET("/customer/{id}/stores")
    Call<List<Store>> getStoresByCustomer(@Path("id") long id);

    @GET("/complaint")
    Call<List<Complaint>> getAllComplaints();
}
