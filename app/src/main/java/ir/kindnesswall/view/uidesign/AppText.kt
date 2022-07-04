package ir.kindnesswall.view.uidesign

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import timber.log.Timber

@Composable
fun AppTextRow(
    modifier: Modifier = Modifier,
    label: String,
    initValue: String,
    onTextChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = label,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(3f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.width(8.dp))
        AppTextField(modifier = Modifier.weight(7f), initValue, onTextChanged = onTextChanged)
    }
}

@Composable
fun AppTextField(modifier: Modifier = Modifier, name: String, onTextChanged: (String) -> Unit) {
    var text by remember {
        mutableStateOf(name)
    }

    TextField(
        value = text,
        modifier = modifier.fillMaxWidth(),
        onValueChange = {
            Timber.i(it)
            text = it
            onTextChanged(it)
        })
}

@Preview
@Composable
fun AppTextFieldPreview() {
    MdcTheme { AppTextField(name = "Hamed", onTextChanged = {}) }
}
