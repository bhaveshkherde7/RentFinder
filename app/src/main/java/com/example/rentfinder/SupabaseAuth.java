package com.example.rentfinder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;

public class SupabaseAuth {

    private static final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    public static void verifyOTP(
            String email,
            String otp,
            AuthCallback callback) {

        try {

            JSONObject json = new JSONObject();

            json.put("email", email);
            json.put("token", otp);
            json.put("type", "email");

            RequestBody body =
                    RequestBody.create(json.toString(), JSON);

            Request request = new Request.Builder()
                    .url(SupabaseConfig.SUPABASE_URL + "/auth/v1/verify")
                    .addHeader("apikey", SupabaseConfig.SUPABASE_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {

                    String responseBody = response.body() != null
                            ? response.body().string()
                            : "";

                    if (response.isSuccessful()) {
                        callback.onSuccess(responseBody);
                    } else {
                        callback.onError(responseBody);
                    }
                }
            });

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public interface AuthCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void sendOTP(String email, AuthCallback callback) {

        try {

            JSONObject json = new JSONObject();

            json.put("email", email);

            JSONObject options = new JSONObject();
            options.put("shouldCreateUser", true);

            json.put("options", options);

            RequestBody body =
                    RequestBody.create(json.toString(), JSON);

            Request request = new Request.Builder()
                    .url(SupabaseConfig.SUPABASE_URL + "/auth/v1/otp")
                    .addHeader("apikey", SupabaseConfig.SUPABASE_KEY)
                    .addHeader(
                            "Authorization",
                            "Bearer " + SupabaseConfig.SUPABASE_KEY
                    )
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {

                    String responseBody = response.body() != null
                            ? response.body().string()
                            : "";

                    if (response.isSuccessful()) {
                        callback.onSuccess(responseBody);
                    } else {
                        callback.onError(responseBody);
                    }
                }
            });

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
}

