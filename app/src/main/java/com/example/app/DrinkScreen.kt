package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkScreen(drink: Drink, onBack: () -> Unit, onAdd: (Drink, Size) -> Unit) {
    var size by rememberSaveable { mutableStateOf(Size.Medium) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(painterResource(drink.picture), contentDescription = null, Modifier.size(200.dp))
            Text(drink.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                drink.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                Size.entries.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = size == option,
                        onClick = { size = option },
                        shape = SegmentedButtonDefaults.itemShape(index, Size.entries.size),
                    ) {
                        Text(option.label)
                    }
                }
            }
            Button(onClick = { onAdd(drink, size) }, modifier = Modifier.fillMaxWidth()) {
                Text("Add to order · ${formatPrice(drink.priceFor(size))}")
            }
        }
    }
}

@Preview
@Composable
fun DrinkScreenPreview() {
    AppTheme {
        DrinkScreen(drink = drinks[4], onBack = {}, onAdd = { _, _ -> })
    }
}
