package com.example.usnewsapp.news_repo

import com.example.usnewsapp.models.NewsResponse
import com.example.usnewsapp.retofit_builder.RetrofitInstance
import com.example.usnewsapp.service.NewsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository {
    suspend fun getTopHeadlines(apiKey: String): NewsResponse {
        return RetrofitInstance.api.getTopHeadlines(apiKey = apiKey)
    }
}