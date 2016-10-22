package com.maksim88.diffutilsadvrecycler.api;

import com.maksim88.diffutilsadvrecycler.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maksim on 22.10.16.
 */

public interface UserEndpointService {

    @GET("?")
    Call<Result> getUsers(@Query("seed") String seed, @Query("results") String resultsCount);
}
