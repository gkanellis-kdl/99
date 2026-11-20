package com.example.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/** The tabs in the bar at the bottom. */
enum class Tab(val label: String, val icon: Int) {
    Menu("Menu", R.drawable.ic_local_cafe),
    Order("Order", R.drawable.ic_shopping_bag),
}

/** The pages of the Menu tab: the menu, and a drink opened from it. */
@Serializable
data object MenuRoute : NavKey

@Serializable
data class DrinkRoute(val drinkId: String) : NavKey

@Composable
fun CoffeeShopApp(orderViewModel: OrderViewModel = viewModel()) {
    val order by orderViewModel.items.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(Tab.Menu) }
    val menuBackStack = rememberNavBackStack(MenuRoute)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Back on the Order tab returns to the menu.
    BackHandler(enabled = tab == Tab.Order) { tab = Tab.Menu }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { item ->
                    NavigationBarItem(
                        selected = tab == item,
                        onClick = { tab = item },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item == Tab.Order && order.isNotEmpty()) {
                                        Badge { Text(order.size.toString()) }
                                    }
                                },
                            ) {
                                Icon(painterResource(item.icon), contentDescription = null)
                            }
                        },
                        label = { Text(item.label) },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Each screen's own top bar keeps clear of the status bar.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            when (tab) {
                Tab.Menu -> NavDisplay(
                    backStack = menuBackStack,
                    onBack = { menuBackStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<MenuRoute> {
                            MenuScreen(onDrinkClick = { drink -> menuBackStack.add(DrinkRoute(drink.id)) })
                        }
                        entry<DrinkRoute> { route ->
                            DrinkScreen(
                                drink = drinks.first { it.id == route.drinkId },
                                onBack = { menuBackStack.removeLastOrNull() },
                                onAdd = { drink, size ->
                                    orderViewModel.add(drink, size)
                                    menuBackStack.removeLastOrNull()
                                    scope.launch { snackbarHostState.showSnackbar("Added to your order") }
                                },
                            )
                        }
                    },
                )

                Tab.Order -> OrderScreen(
                    items = order,
                    onRemove = orderViewModel::remove,
                    onOrderPlaced = orderViewModel::clear,
                )
            }
        }
    }
}
