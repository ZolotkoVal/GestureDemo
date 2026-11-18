package com.example.gesturedemo

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.io.File

class GestureTests {
 @get:Rule val rule = createComposeRule()
 private fun show(content: @Composable () -> Unit) {
  rule.setContent { MaterialTheme { Box(Modifier.fillMaxSize()) { content() } } }
 }
 private fun bounds(color: Color = Color.Blue): Rect {
  val image = rule.onRoot().captureToImage()
  val pixels = image.toPixelMap()
  var left = image.width; var top = image.height; var right = -1; var bottom = -1
  for (y in 0 until image.height step 3) for (x in 0 until image.width step 3) {
   val p = pixels[x,y]
   if (kotlin.math.abs(p.red-color.red)<0.03f && kotlin.math.abs(p.green-color.green)<0.03f && kotlin.math.abs(p.blue-color.blue)<0.03f) {
    left = minOf(left,x); top = minOf(top,y); right = maxOf(right,x); bottom = maxOf(bottom,y)
   }
  }
  assertTrue("Expected visible colored rectangle",right>left && bottom>top)
  return Rect(left.toFloat(),top.toFloat(),right.toFloat(),bottom.toFloat())
 }
 private fun shot(name: String) {
  val context = InstrumentationRegistry.getInstrumentation().targetContext
  val file = File(context.getExternalFilesDir(null),"$name.png")
  file.outputStream().use { rule.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG,100,it) }
 }
 @Test fun clickAlternatesColors() {
  show { ClickDemo() }
  val before = bounds()
  rule.onRoot().performTouchInput { click(before.center) }
  bounds(Color.DarkGray)
  rule.onRoot().performTouchInput { click(before.center) }
  bounds()
  shot("click-blue")
 }
}
