package com.example.movieapp.data

import com.example.movieapp.utils.apiUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class DataRepository {

    object retrofitClient {
        var apiService: ApiService? = null

        fun getInstance(): ApiService? {
            if (apiService == null) {

                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY

                val httpClient: OkHttpClient.Builder = OkHttpClient.Builder();
                httpClient.addInterceptor(logging)
                httpClient.apply {
                    addInterceptor(logging)
                    retryOnConnectionFailure(true)
                    readTimeout(38, TimeUnit.SECONDS)
                    connectTimeout(20, TimeUnit.SECONDS)
                }

                var retrofit = Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                apiService = retrofit.create(ApiService::class.java)
            }
            return apiService

        }
    }
}
