package com.deadrudolph.encryptortest.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.deadrudolph.encryptortest.R

@Composable
fun SaveFileDialog(
    modifier: Modifier,
    message: String,
    onAccept: (fileName: String) -> Unit
) {

    Dialog(onDismissRequest = { }) {
        Content(modifier, message, onAccept)
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    message: String,
    onAccept: (fileName: String) -> Unit
) {
    val textFieldText = remember {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .padding(40.dp),
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = textFieldText.value,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontSize = 20.sp
            ),
            onValueChange = { newValue ->
                textFieldText.value = newValue
            })

        Button(modifier = Modifier
            .wrapContentWidth()
            .align(Alignment.CenterHorizontally),
            onClick = { onAccept(textFieldText.value) }
        ) {
            Text(
                text = stringResource(id = R.string.button_accept),
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
            )
        }
    }
}
