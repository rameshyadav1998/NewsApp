package com.example.usnewsapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.usnewsapp.screens.NewsDetailScreen
import com.example.usnewsapp.screens.NewsListScreen
import com.example.usnewsapp.view_model.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           NewsApp()
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val viewModel: NewsViewModel = viewModel()

    NavHost(navController, startDestination = "newsList") {
        composable("newsList") {
            NewsListScreen(viewModel) { title, description, imageUrl, articleUrl ->
                val encodedTitle = Uri.encode(title?.ifEmpty { "No Title" })
                val encodedDescription = Uri.encode(description?.ifEmpty { "No description available" } ?: "No description available")
                val encodedImageUrl = Uri.encode(imageUrl?.ifEmpty { "no_image" } ?: "no_image")
                val encodedArticleUrl = Uri.encode(articleUrl?.ifEmpty { "no_url" })
                val route = "newsDetail/$encodedTitle/$encodedDescription/$encodedImageUrl/$encodedArticleUrl"
                navController.navigate(route)
            }
        }

        composable("newsDetail/{title}/{description}/{imageUrl}/{articleUrl}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "No Title"
            val description = backStackEntry.arguments?.getString("description") ?: "No Description"
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.takeIf { it != "no_image" } ?: ""
            val articleUrl = backStackEntry.arguments?.getString("articleUrl")?.takeIf { it != "no_url" } ?: ""
            NewsDetailScreen(navController, title, description, imageUrl, articleUrl)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsApp()
}