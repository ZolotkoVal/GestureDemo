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
 @Test fun twoPointersTranslateBox() {
  show { MultiTouchDemo() }
  val before = bounds()
  val c = before.center
  val r = before.width / 4f
  val delta = Offset(100f,140f)
  rule.onRoot().performTouchInput {
   pinch(c-Offset(r,0f),c-Offset(r,0f)+delta,c+Offset(r,0f),c+Offset(r,0f)+delta,800)
  }
  val after = bounds()
  shot("multitouch-translation")
  assertTrue("Two-pointer pan must move box",after.left>before.left+50f && after.top>before.top+70f)
 }
 @Test fun twoPointersRotateBox() {
  show { MultiTouchDemo() }
  val before = bounds()
  val c = before.center
  val r = before.width / 4f
  rule.onRoot().performTouchInput {
   pinch(c-Offset(r,0f),c-Offset(r*0.866f,r*0.5f),c+Offset(r,0f),c+Offset(r*0.866f,r*0.5f),800)
  }
  val after = bounds()
  shot("multitouch-rotation")
  assertTrue("Rotation must change silhouette",after.width > before.width*1.15f && after.height > before.height*1.15f)
 }
 @Test fun twoPointersScaleBox() {
  show { MultiTouchDemo() }
  val before = bounds()
  val c = before.center
  val r = before.width / 4f
  shot("multitouch-initial")
  rule.onRoot().performTouchInput {
   pinch(c-Offset(r,0f),c-Offset(r*1.7f,0f),c+Offset(r,0f),c+Offset(r*1.7f,0f),800)
  }
  val after = bounds()
  assertTrue("Pinch must enlarge box",after.width > before.width*1.25f && after.height > before.height*1.25f)
  shot("multitouch-scaled")
 }
 @Test fun photoScrollsInBothDirections() {
  show { ScrollModifiers() }
  val start = rule.onRoot().captureToImage().asAndroidBitmap()
  shot("photo-start")
  rule.onRoot().performTouchInput { swipe(Offset(250f,180f),Offset(60f,180f),800) }
  val horizontal = rule.onRoot().captureToImage().asAndroidBitmap()
  assertFalse("Horizontal scroll must change visible photo",start.sameAs(horizontal))
  shot("photo-horizontal")
  rule.onRoot().performTouchInput { swipe(Offset(180f,250f),Offset(180f,60f),800) }
  val vertical = rule.onRoot().captureToImage().asAndroidBitmap()
  assertFalse("Vertical scroll must change visible photo",horizontal.sameAs(vertical))
  shot("photo-vertical")
 }
 @Test fun scrollableMovesRedBoxVertically() {
  show { ScrollableModifier() }
  val before = bounds(Color.Red)
  rule.onRoot().performTouchInput { swipe(before.center,before.center + Offset(0f,180f),1500) }
  val after = bounds(Color.Red)
  assertTrue(after.top > before.top + 80f)
  assertEquals(before.left,after.left,4f)
  shot("scroll-red")
 }
 @Test fun pointerDragMovesBothAxes() {
  show { PointerInputDrag() }
  val before = bounds()
  rule.onRoot().performTouchInput { swipe(before.center,before.center + Offset(160f,180f),600) }
  val after = bounds()
  assertTrue(after.left > before.left + 80f && after.top > before.top + 100f)
  shot("drag-both-axes")
 }
 @Test fun dragMovesOnlyHorizontally() {
  show { DragDemo() }
  val before = bounds()
  rule.onRoot().performTouchInput { swipe(before.center,before.center + Offset(160f,100f),600) }
  val after = bounds()
  assertTrue(after.left > before.left + 80f)
  assertEquals(before.top,after.top,4f)
  shot("drag-horizontal")
 }
 @Test fun tapDoubleTapAndLongPress() {
  show { TapPressDemo() }
  val target = bounds().center
  rule.onRoot().performTouchInput { click(target) }
  rule.waitUntil(3000) { rule.onAllNodesWithText("onTap Detected").fetchSemanticsNodes().isNotEmpty() }
  rule.onRoot().performTouchInput { doubleClick(target) }
  rule.onNodeWithText("onDoubleTap Detected").assertIsDisplayed()
  rule.onRoot().performTouchInput { longClick(target, 1000) }
  rule.onNodeWithText("onLongPress Detected").assertIsDisplayed()
  shot("tap-longpress")
 }
 @Test fun clickAlternatesColors() {
  show { ClickDemo() }
  val before = bounds()
  rule.onRoot().performTouchInput { click(before.center) }
  android.os.SystemClock.sleep(500) // Ripple is rendered outside the Compose test clock.
  bounds(Color.DarkGray)
  rule.onRoot().performTouchInput { click(before.center) }
  shot("click-before-ripple-settles")
  android.os.SystemClock.sleep(500)
  bounds()
  shot("click-blue")
 }
}
