package com.example.usnewsapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.usnewsapp.R
import com.example.usnewsapp.view_model.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel = viewModel(),
    onClick: (String?, String?, String?, String?) -> Unit
) {
    val apiKey = "eab896af7bdd46ea9653537067d6e93d"
    val newsState by viewModel.newsState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchTopHeadlines(apiKey)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Latest News") })
        }
    ) { padding ->
        if (newsState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
                //   CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(newsState!!.articles) { article ->
                    Log.e("MOVIELIST", "NewsListScreen article = $article", )
                    NewsItem(
                        article.title,
                        article.description,
                        article.urlToImage,
                        article.url,
                        onClick
                    )
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    title: String,
    description: String?,
    imageUrl: String?,
    articleUrl: String?,
    onClick: (String, String?, String?, String?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(
                    title,
                    description ?: "No description available",
                    imageUrl ?: "",
                    articleUrl ?: ""
                )
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ImageComponent(imageUrl,180.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                description ?: "No description available",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ImageComponent(imageUrl: String?, height: Dp) {
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        error = painterResource(id = R.drawable.placeholder),
        placeholder = painterResource(id = R.drawable.placeholder)
    )
    Image(
        painter = painter,
        contentDescription = "News Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentScale = ContentScale.Crop
    )
}
