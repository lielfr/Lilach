package org.cshaifasweng.winter;

import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Customer;
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
}
