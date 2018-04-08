package com.example.root.railways;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by root on 4/1/18.
 */

public interface ApiInterface {

    @GET("{date}/apikey/sed0n99h4b/")
    Call<CancelledTrains> getCancelledTrains(@Path("date") String date);
}
