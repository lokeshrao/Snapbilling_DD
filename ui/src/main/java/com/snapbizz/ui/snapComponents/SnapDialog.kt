package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SnapDialog(
    header: String,
    message: String,
    onDismissRequest: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isSecondaryVisible:Boolean= true
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SnapText(
                    text = header,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val scrollState = remember { ScrollState(0) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    SnapText(text = message)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if(isSecondaryVisible) {
                        SnapOutlinedButton(
                            text = dismissButtonText,
                            onClick = {
                                onDismiss()
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    SnapButton(
                        text = confirmButtonText,
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth()
                    )
                }
            }
        }
    }
}

