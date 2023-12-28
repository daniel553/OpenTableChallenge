package com.opentable.challenge.ui.component

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextWithExposedDropdownMenu(
    label: @Composable () -> Unit,
    options: List<DropdownMenuItem>,
    onSelected: (DropdownMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    expandedDefault: Boolean = false,
    optionDecoration: (DropdownMenuItem?) -> String = { option -> option?.text.orEmpty() },
) {
    var expanded by remember { mutableStateOf(expandedDefault) }
    var selected by remember { mutableStateOf<DropdownMenuItem?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = optionDecoration(selected),
            onValueChange = {},
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            //colors = ExposedDropdownMenuDefaults.textFieldColors(),
            isError = isError
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionDecoration(option)) },
                    onClick = {
                        selected = option
                        expanded = false
                        onSelected(option)
                    },
                    enabled = option.enabled,
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextWithExposedDropdownMenuPreview() {
    OpenTableChallengeTheme {
        OutlinedTextWithExposedDropdownMenu(
            label = { Text(text = "Label") },
            options = listOf(DropdownMenuItem("A", "")),
            onSelected = {},
            modifier = Modifier.width(200.dp)
        )
    }
}

data class DropdownMenuItem(val key: String, val text: String, val enabled: Boolean = true)
