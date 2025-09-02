package com.example.googlepinlock

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PressEffectButton(
    modifier: Modifier = Modifier,
    normalColor: Color = Color.White.copy(alpha = 0.2f),
    pressedColor: Color = Color.Green,
    triggerFlow: MutableSharedFlow<Unit> = remember { MutableSharedFlow() }, // 外部触发动画
    itemIndex: Int = 0,       // 当前 item 索引
    onClick: () -> Unit = {},
    animationDuration: Int = 250,
    content: @Composable () -> Unit,
) {
    val cornerAnim = remember { Animatable(50f) }
    val colorAnim = remember { Animatable(normalColor) }
    val scaleAnim = remember { Animatable(1f) }
    derivedStateOf { }
    // 监听外部 triggerScale 执行缩放动画
    LaunchedEffect(triggerFlow) {
        triggerFlow.collectLatest {
            val delayPerItem = 50L // 每个 item 延迟 50ms
            delay(itemIndex * delayPerItem)
            scaleAnim.animateTo(0.8f, tween(100))
            scaleAnim.animateTo(1f, tween(100))
        }

    }
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .background(
                color = colorAnim.value,
                shape = RoundedCornerShape(percent = cornerAnim.value.toInt())
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        // 启动协程播放按下动画
                        coroutineScope {
                            launch {
                                cornerAnim.animateTo(16f, animationSpec = tween(animationDuration))
                            }
                            launch {
                                colorAnim.animateTo(
                                    pressedColor, animationSpec = tween(animationDuration)
                                )
                            }
                        }

                        val released = tryAwaitRelease() // 等待手指抬起

                        // 松开动画
                        coroutineScope {
                            launch {
                                cornerAnim.animateTo(50f, animationSpec = tween(animationDuration))
                            }
                            launch {
                                colorAnim.animateTo(
                                    normalColor, animationSpec = tween(animationDuration)
                                )
                            }
                            if (released) onClick()
                        }
                    })
            }, contentAlignment = Alignment.Center
    ) {
        content()
    }
}