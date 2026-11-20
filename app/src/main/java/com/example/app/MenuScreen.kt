package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onDrinkClick: (Drink) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(stringResource(R.string.app_name))
                        Text(
                            "What would you like today?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(drinks, key = { it.id }) { drink ->
                ListItem(
                    headlineContent = { Text(drink.name) },
                    supportingContent = {
                        Column {
                            Text(drink.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(
                                "From ${formatPrice(drink.price)}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    },
                    leadingContent = {
                        Image(painterResource(drink.picture), contentDescription = null, Modifier.size(64.dp))
                    },
                    modifier = Modifier.clickable { onDrinkClick(drink) },
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreen(onDrinkClick = {})
    }
}
