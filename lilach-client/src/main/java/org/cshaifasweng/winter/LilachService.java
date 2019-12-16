package org.cshaifasweng.winter;

import org.cshaifasweng.winter.models.CatalogItem;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface LilachService {
    @GET("catalog")
    Call<List<CatalogItem>> getAllItems();
}
