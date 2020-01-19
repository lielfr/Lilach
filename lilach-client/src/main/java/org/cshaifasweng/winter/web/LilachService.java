package org.cshaifasweng.winter.web;

import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.User;
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
}
