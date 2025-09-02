package com.example.googlepinlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SecretDots(passwordLength: Int, dotSize: Dp = 20.dp, dotSpacing: Dp = 10.dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dotSpacing)
    ) {
        repeat(passwordLength) {
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
