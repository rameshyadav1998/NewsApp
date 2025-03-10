package com.example.usnewsapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usnewsapp.models.NewsResponse
import com.example.usnewsapp.news_repo.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class NewsViewModel() : ViewModel() {

    private val repository = NewsRepository()
    private val _newsState = MutableStateFlow<NewsResponse?>(null)
    val newsState: StateFlow<NewsResponse?> = _newsState

    fun fetchTopHeadlines(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines(apiKey)
                _newsState.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _newsState.value = null
            }
        }
    }
}
