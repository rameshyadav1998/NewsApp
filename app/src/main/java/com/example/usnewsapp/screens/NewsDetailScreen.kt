package com.example.usnewsapp.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.usnewsapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    navController: NavController,
    title: String,
    description: String?,
    imageUrl: String?,
    articleUrl: String
) {
    var likes by remember { mutableStateOf(0) }
    var comments by remember { mutableStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(articleUrl) {
        val articleId = articleUrl.replace("https://", "").replace("/", "-")
        likes = fetchLikes(formatArticleId(articleUrl))
        comments = fetchComments(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            ImageComponent(imageUrl,180.dp)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = description ?: "No description available", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "👍 Likes: $likes", fontSize = 14.sp)
                    Text(text = "💬 Comments: $comments", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Read Full Article")
                }
            }
        }
    }
}
fun formatArticleId(url: String): String {
    return url.removePrefix("https://")
        .removePrefix("http://")
        .replace("/", "-")
        .removeSuffix("-") // ✅ Remove trailing dash
}

suspend fun fetchLikes(articleId: String): Int {
    Log.e("MOVIEDTAILS", "fetchLikes articleId = $articleId")
    return withContext(Dispatchers.IO) {
        try {
            val response = URL("https://cn-news-info-api.herokuapp.com/likes/$articleId").readText()
            Log.e("MOVIEDTAILS", "fetchLikes:$response ")
            println("Likes API Response: $response")
            response.toIntOrNull() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}


suspend fun fetchComments(articleId: String): Int {
    return withContext(Dispatchers.IO) {
        try {
            URL("https://cn-news-info-api.herokuapp.com/comments/$articleId").readText().toInt()
        } catch (e: Exception) {
            0
        }
    }
}
