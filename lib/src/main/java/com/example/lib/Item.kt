package com.example.lib

import java.util.UUID
import kotlin.math.roundToInt

class Item(
    var name: String? = "",
    var quantity: Int? = 0,
    var price: Double? = 0.0
) : Comparable<Item> {
    var id: String = UUID.randomUUID().toString()

    override fun compareTo(other: Item): Int {
        if (this.quantity!! > other.quantity!!) return 1
        if (this.quantity!! < other.quantity!!) return -1
        return 0
    }

    override fun toString(): String {
        return "${this.name} - Quantity: ${this.quantity}, Price: ${this.price}"
    }

    init {
        this.price = (this.price!! * 100.0).roundToInt() / 100.0
    }
}