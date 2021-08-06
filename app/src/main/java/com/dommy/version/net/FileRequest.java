package com.dommy.version.net;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FileRequest {

    @Multipart
    @POST
    Call<ResponseBody> postMap(@Url String url, @PartMap Map<String, RequestBody> paramMap);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);
}
