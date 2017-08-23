package br.com.celulasreligiosas.task;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public interface RequestApi {
    @GET("articles")
    Call<ResultNewsApi> getNewsApi(@Query("source") String source,
                                   @Query("sortBy") String sortBy,
                                   @Query("apiKey") String apiKey);
}
