package com.example.gesturedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent { MaterialTheme { Scaffold { padding -> MainScreen(Modifier.padding(padding)) } } }
 }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
 PointerInputDrag(modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() { MaterialTheme { MainScreen() } }
