package com.example.gesturedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ClickDemo(modifier: Modifier = Modifier) {
 var colorState by remember { mutableStateOf(true) }
 var bgColor by remember { mutableStateOf(Color.Blue) }
 val clickHandler = {
  colorState = !colorState
  bgColor = if (colorState) Color.Blue else Color.DarkGray
 }
 Box(modifier.clickable { clickHandler() }.background(bgColor).size(100.dp))
}
