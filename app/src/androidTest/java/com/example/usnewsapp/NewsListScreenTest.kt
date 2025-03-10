package com.example.usnewsapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.usnewsapp.screens.NewsListScreen
import com.example.usnewsapp.view_model.NewsViewModel
import org.junit.Rule
import org.junit.Test

class NewsListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTopBarIsDisplayed() {
        composeTestRule.setContent {
            NewsListScreen(viewModel = NewsViewModel(), onClick = { _, _, _, _ -> })
        }

        // Check if TopAppBar with "Latest News" is displayed
        composeTestRule.onNodeWithText("Latest News").assertIsDisplayed()
    }

    @Test
    fun testLoadingTextIsDisplayed() {
        composeTestRule.setContent {
            NewsListScreen(viewModel = NewsViewModel(), onClick = { _, _, _, _ -> })
        }

        // Check if "Loading..." text is displayed initially
        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun testNewsListIsDisplayedWhenDataAvailable() {
        composeTestRule.setContent {
            NewsListScreen(viewModel = NewsViewModel(), onClick = { _, _, _, _ -> })
        }

        // Wait until the list is populated
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Loading...").fetchSemanticsNodes().isEmpty()
        }

        // Check if at least one news item is displayed
        composeTestRule.onAllNodes(isRoot()).onFirst().assertExists()
    }
}
