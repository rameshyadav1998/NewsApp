package com.example.usnewsapp

import com.example.usnewsapp.service.NewsApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: NewsApiService // Your Retrofit API Interface

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Initialize Retrofit with MockWebServer
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use mock server's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testApiResponse() = runBlocking {
        // Sample Mock JSON Response (Matching NewsResponse Structure)
        val mockResponse = """
        {
            "status": "ok",
            "totalResults": 1,
            "articles": [
                {
                    "source": {
                        "id": "bbc-news",
                        "name": "BBC News"
                    },
                    "author": "John Doe",
                    "title": "Breaking News",
                    "description": "This is a test description",
                    "url": "https://example.com/news",
                    "urlToImage": "https://example.com/image.jpg",
                    "publishedAt": "2024-03-10T10:00:00Z",
                    "content": "Sample content for testing."
                }
            ]
        }
    """.trimIndent()

        // Enqueue Response in MockWebServer
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        // Call API
        val response = apiService.getTopHeadlines(apiKey = "11818")

        // Assert response
        assertEquals("ok", response.status)
        assertEquals(1, response.totalResults)
        assertEquals(1, response.articles.size)

        val firstArticle = response.articles[0]
        assertEquals("John Doe", firstArticle.author)
        assertEquals("Breaking News", firstArticle.title)
        assertEquals("This is a test description", firstArticle.description)
        assertEquals("https://example.com/news", firstArticle.url)
        assertEquals("https://example.com/image.jpg", firstArticle.urlToImage)
        assertEquals("2024-03-10T10:00:00Z", firstArticle.publishedAt)
        assertEquals("Sample content for testing.", firstArticle.content)
    }

}
