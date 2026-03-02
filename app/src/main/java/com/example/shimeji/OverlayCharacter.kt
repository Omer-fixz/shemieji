package com.example.shimeji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.WindowManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun OverlayCharacter(
    windowManager: WindowManager,
    layoutParams: WindowManager.LayoutParams,
    context: Context
) {
    var currentFrame by remember { mutableIntStateOf(0) }
    var posX by remember { mutableFloatStateOf(layoutParams.x.toFloat()) }
    var posY by remember { mutableFloatStateOf(layoutParams.y.toFloat()) }
    
    val spriteSheet = remember { loadSpriteSheet(context) }
    val frameCount = 4
    val frameWidth = spriteSheet.width / frameCount

    // Animation loop
    LaunchedEffect(Unit) {
        while (true) {
            delay(200)
            currentFrame = (currentFrame + 1) % frameCount
        }
    }

    // Random walk
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            if (Random.nextFloat() > 0.5f) {
                posX += Random.nextInt(-50, 50)
                layoutParams.x = posX.toInt()
                windowManager.updateViewLayout(null, layoutParams)
            }
        }
    }

    Box(
        modifier = Modifier
            .size(64.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    posX += dragAmount.x
                    posY += dragAmount.y
                    layoutParams.x = posX.toInt()
                    layoutParams.y = posY.toInt()
                    windowManager.updateViewLayout(null, layoutParams)
                }
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawSpriteFrame(spriteSheet, currentFrame, frameWidth)
        }
    }
}

fun DrawScope.drawSpriteFrame(spriteSheet: Bitmap, frame: Int, frameWidth: Int) {
    val srcLeft = frame * frameWidth
    val imageBitmap = spriteSheet.asImageBitmap()
    
    drawImage(
        image = imageBitmap,
        srcOffset = androidx.compose.ui.unit.IntOffset(srcLeft, 0),
        srcSize = androidx.compose.ui.unit.IntSize(frameWidth, spriteSheet.height),
        dstOffset = androidx.compose.ui.unit.IntOffset.Zero,
        dstSize = androidx.compose.ui.unit.IntSize(size.width.toInt(), size.height.toInt())
    )
}

fun loadSpriteSheet(context: Context): Bitmap {
    return try {
        // Try to load from drawable resources
        BitmapFactory.decodeResource(context.resources, R.drawable.ramadan_lantern_sprite)
    } catch (e: Exception) {
        // Fallback to dummy sprite sheet if image not found
        createDummySpriteSheet()
    }
}

fun createDummySpriteSheet(): Bitmap {
    val width = 256
    val height = 64
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    
    val paint = android.graphics.Paint().apply {
        style = android.graphics.Paint.Style.FILL
    }
    
    // Create 4 frames with different colors
    val colors = intArrayOf(
        android.graphics.Color.RED,
        android.graphics.Color.GREEN,
        android.graphics.Color.BLUE,
        android.graphics.Color.YELLOW
    )
    
    for (i in 0..3) {
        paint.color = colors[i]
        canvas.drawCircle(
            (i * 64 + 32).toFloat(),
            32f,
            28f,
            paint
        )
    }
    
    return bitmap
}
