package com.example.googlepinlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Composable
fun UnlockScreen() {

    // 用来触发所有按钮缩放动画
    val triggerFlow = remember { MutableSharedFlow<Unit>() }
    val scop = rememberCoroutineScope()
    val password = remember { mutableStateListOf<Char>() }
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "输入 PIN 码",
            color = Color.White,
            modifier = Modifier.padding(top = 40.dp),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        SecretDots(passwordLength = password.size, dotSize = 20.dp, dotSpacing = 10.dp)

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 36.dp, top = 36.dp, end = 36.dp, bottom = 16.dp)
        ) {
            items(12) {
                val normalColor = if (it == 9 || it == 11) Color.Cyan else Color.White.copy(alpha = 0.2f)
                val normalTextColor = if (it == 9 || it == 11) Color.Black else Color.White
                PressEffectButton(
                    normalColor = normalColor,
                    triggerFlow = triggerFlow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    itemIndex = it,
                    onClick = {
                        // 只有点击第10个 item 才触发全局动画
                        if (it == 9) {
                            if (password.isNotEmpty()) {
                                password.removeAt(password.size - 1)
                            }

                        } else if (it == 11) {
                            scop.launch { triggerFlow.emit(Unit) }
                            password.clear()
                        } else {
                            password.add(it.toChar())
                        }
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }) {

                    when (it) {
                        9 -> {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Home",
                                tint = Color.Black
                            )
                        }

                        11 -> {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Home",
                                tint = Color.Black
                            )
                        }

                        else -> {
                            Text(
                                text = "${it + 1}",
                                color = normalTextColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                    }
                }
            }
        }

        //浅蓝色

        Button(
            modifier = Modifier.padding(bottom = 30.dp),
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFADD8E6))
        ) {
            Text("紧急呼叫", color = Color.Black)
        }

    }
}
