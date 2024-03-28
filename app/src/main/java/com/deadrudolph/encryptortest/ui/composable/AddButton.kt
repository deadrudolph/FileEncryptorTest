package com.deadrudolph.encryptortest.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AddButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            imageVector = Icons.Rounded.AddCircle,
            contentDescription = "Icon add"
        )
    }
}
