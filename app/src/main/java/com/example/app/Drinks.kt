package com.example.app

import java.text.NumberFormat
import java.util.Locale

data class Drink(
    val id: String,
    val name: String,
    val description: String,
    /** The price of a Small, in cents. */
    val price: Int,
    /** A vector drawable in res/drawable, such as R.drawable.drink_mocha. */
    val picture: Int,
)

/** The sizes a drink comes in, and what each adds to a Small's price, in cents. */
enum class Size(val label: String, val extraPrice: Int) {
    Small("Small", 0),
    Medium("Medium", 50),
    Large("Large", 100),
}

fun Drink.priceFor(size: Size): Int = price + size.extraPrice

/** The menu, in the order it shows. */
val drinks = listOf(
    Drink(
        id = "espresso",
        name = "Espresso",
        description = "A short, intense shot with a hazelnut crema.",
        price = 280,
        picture = R.drawable.drink_espresso,
    ),
    Drink(
        id = "cappuccino",
        name = "Cappuccino",
        description = "Espresso under a deep cap of milk foam.",
        price = 360,
        picture = R.drawable.drink_cappuccino,
    ),
    Drink(
        id = "latte",
        name = "Latte",
        description = "Espresso with plenty of steamed milk and a thin layer of foam.",
        price = 390,
        picture = R.drawable.drink_latte,
    ),
    Drink(
        id = "flat-white",
        name = "Flat white",
        description = "A double shot with silky microfoam, stronger than a latte.",
        price = 370,
        picture = R.drawable.drink_flat_white,
    ),
    Drink(
        id = "mocha",
        name = "Mocha",
        description = "Espresso, chocolate and steamed milk, topped with cream.",
        price = 430,
        picture = R.drawable.drink_mocha,
    ),
    Drink(
        id = "iced-coffee",
        name = "Iced coffee",
        description = "Cold brew over ice with a splash of milk.",
        price = 340,
        picture = R.drawable.drink_iced_coffee,
    ),
)

/** Shows a price in cents as US dollars: 430 becomes "$4.30". */
fun formatPrice(cents: Int): String = NumberFormat.getCurrencyInstance(Locale.US).format(cents / 100.0)
