package com.dommy.version.net;

import android.content.Context;
import android.widget.Toast;

import com.dommy.version.util.Constant;
import com.dommy.version.util.NetworkUtil;
import com.dommy.version.R;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitRequest {
    private static int TIME_OUT = 30;

    private static X509TrustManager trustAllCert = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };
    private static SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);

    // httpclient
    public static OkHttpClient client = new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCert)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.URL_BASE)
            .client(client)
            .build();

    public static void sendGetRequest(String url, final ResultHandler resultHandler) {
        if (resultHandler.isNetDisconnected()) {
            resultHandler.onAfterFailure();
            return;
        }

        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<ResponseBody> call = getRequest.getUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                resultHandler.onBeforeResult();
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        resultHandler.onServerError();
                        resultHandler.onAfterFailure();
                        return;
                    }
                    String responseData = body.string();
                    resultHandler.onResult(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                    resultHandler.onFailure(e);
                    resultHandler.onAfterFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultHandler.onFailure(t);
                resultHandler.onAfterFailure();
            }
        });
    }

    private static void addMultiPart(Map<String, RequestBody> paramMap, String key, Object obj) {
        if (obj instanceof String) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) obj);
            paramMap.put(key, body);
        } else if (obj instanceof File) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) obj);
            paramMap.put(key + "\"; filename=\"" + ((File) obj).getName() + "", body);
        }
    }

    public static abstract class ResultHandler<T> {
        Context context;

        public ResultHandler(Context context) {
            this.context = context;
        }

        public boolean isNetDisconnected() {
            return NetworkUtil.isNetDisconnected(context);
        }

        public abstract void onBeforeResult();

        public abstract void onResult(String response);

        public void onServerError() {
            Toast.makeText(context, R.string.net_server_error, Toast.LENGTH_SHORT).show();
        }

        public abstract void onAfterFailure();

        public void onFailure(Throwable t) {
            if (t instanceof SocketTimeoutException || t instanceof ConnectException) {
                if (NetworkUtil.isNetworkConnected(context)) {
                    Toast.makeText(context, R.string.net_server_connected_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.net_not_connected, Toast.LENGTH_SHORT).show();
                }
            } else if (t instanceof Exception) {
                Toast.makeText(context, R.string.net_unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
