package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(items: List<OrderItem>, onRemove: (OrderItem) -> Unit, onOrderPlaced: () -> Unit) {
    var showPlaced by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your order") }) },
        bottomBar = {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Text("Total", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text(formatPrice(items.sumOf { it.price }), style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = { showPlaced = true },
                    enabled = items.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Place order")
                }
            }
        },
    ) { innerPadding ->
        if (items.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Your order is empty", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Pick something from the menu.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn(contentPadding = innerPadding) {
                items(items, key = { it.id }) { item ->
                    ListItem(
                        headlineContent = { Text(item.drink.name) },
                        supportingContent = { Text(item.size.label) },
                        leadingContent = {
                            Image(painterResource(item.drink.picture), contentDescription = null, Modifier.size(44.dp))
                        },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(formatPrice(item.price), style = MaterialTheme.typography.bodyLarge)
                                IconButton(onClick = { onRemove(item) }) {
                                    Icon(painterResource(R.drawable.ic_delete), contentDescription = "Remove")
                                }
                            }
                        },
                    )
                }
            }
        }
    }

    if (showPlaced) {
        // Closing the dialog either way empties the order: it has been placed.
        val close = {
            showPlaced = false
            onOrderPlaced()
        }
        AlertDialog(
            onDismissRequest = close,
            confirmButton = { TextButton(onClick = close) { Text("OK") } },
            title = { Text("Order placed") },
            text = { Text("Thanks! Your drinks will be ready in about 5 minutes.") },
        )
    }
}

@Preview
@Composable
fun OrderScreenPreview() {
    AppTheme {
        OrderScreen(
            items = listOf(OrderItem(0, drinks[1], Size.Medium), OrderItem(1, drinks[5], Size.Large)),
            onRemove = {},
            onOrderPlaced = {},
        )
    }
}
