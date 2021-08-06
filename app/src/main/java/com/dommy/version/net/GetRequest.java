package com.dommy.version.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetRequest {

    @GET
    Call<ResponseBody> getUrl(@Url String url);

}
