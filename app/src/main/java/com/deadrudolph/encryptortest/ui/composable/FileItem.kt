package com.deadrudolph.encryptortest.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deadrudolph.encryptortest.R
import com.deadrudolph.encryptortest.domain.model.FileModel

@Composable
fun FileItem(
    modifier: Modifier,
    fileItem: FileModel,
    onEncryptClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onDecryptClicked: () -> Unit,
) {

    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Gray)
                .clickable { onDecryptClicked() }
                .align(Alignment.CenterVertically),
            text = fileItem.name,
        )
        if (fileItem.isEncrypted) {
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    onDecryptClicked()
                }
            ) {
                Text(text = stringResource(id = R.string.button_decrypt))
            }
        } else {
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    onEncryptClicked()
                }
            ) {
                Text(text = stringResource(id = R.string.button_encrypt))
            }
        }
        Button(
            modifier = Modifier
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            onClick = {
                onDeleteClicked()
            }
        ) {
            Text(text = stringResource(id = R.string.button_delete))
        }

        Spacer(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        )
        if (fileItem.isEncrypted) {
            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Lock,
                contentDescription = "Icon Lock"
            )
        }
    }

}
