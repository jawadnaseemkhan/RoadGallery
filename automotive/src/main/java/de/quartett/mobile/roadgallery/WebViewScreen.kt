package de.quartett.mobile.roadgallery

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text

@Composable
fun WebViewScreen(startUrl: String) {
    var url by remember { mutableStateOf(startUrl) }
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                    Text("Tab 1")
                }
                //                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                //                    Text("Tab 2")
                //                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(
                onClick = { /* No action needed, WebView will handle URL change */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go")
            }
            AndroidView(
                factory = {
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                    }
                },
                update = { it.loadUrl(url) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}