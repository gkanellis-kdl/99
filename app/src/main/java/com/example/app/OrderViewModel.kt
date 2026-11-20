package com.example.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** One drink in the order. The id tells two of the same drink and size apart. */
data class OrderItem(val id: Int, val drink: Drink, val size: Size) {
    val price: Int get() = drink.priceFor(size)
}

/** The order. It lives in memory only, so it's gone when the app quits. */
class OrderViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<OrderItem>>(emptyList())
    val items: StateFlow<List<OrderItem>> = _items.asStateFlow()

    private var nextId = 0

    fun add(drink: Drink, size: Size) {
        _items.update { it + OrderItem(nextId++, drink, size) }
    }

    fun remove(item: OrderItem) {
        _items.update { it - item }
    }

    fun clear() {
        _items.value = emptyList()
    }
}
