package org.wordpress.android.ui.prefs.accountsettings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.wordpress.android.R
import org.wordpress.android.ui.compose.theme.AppTheme

@Composable
fun DialogSuccessUi(
    onDismissRequest: () -> Unit,
) {
    val padding = 10.dp
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = padding),
        textAlign = TextAlign.Center,
        text = stringResource(R.string.account_closure_dialog_success_message),
        fontWeight = FontWeight.Bold,
    )
    FlatOutlinedButton(
        text = stringResource(R.string.ok),
        onClick = onDismissRequest,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.primary,
            backgroundColor = Color.Transparent,
        ),
    )
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDialogSuccessUi() {
    AppTheme {
        AccountClosureDialog(
            onDismissRequest = {},
        ) {
            DialogSuccessUi(
                onDismissRequest = {},
            )
        }
    }
}
