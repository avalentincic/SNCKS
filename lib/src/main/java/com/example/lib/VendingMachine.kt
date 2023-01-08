package com.example.lib

import io.github.serpro69.kfaker.faker
import java.util.UUID

val faker = faker {  }

class VendingMachine(
    var name: String? = "",
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
) {
    var id: String = UUID.randomUUID().toString()
    var items: ArrayList<Item> =  ArrayList<Item>()

    override fun toString(): String {
        return "${this.name} ($latitude, $longitude): ${this.items.toString()}"
    }

    fun generate(n: Int){
        for (i in 1..n){
            items.add(
                Item(
                    name=faker.food.dish(),
                    quantity=faker.random.nextInt(intRange = 0..10),
                    price=faker.random.nextDouble() + faker.random.nextInt(intRange = 0..5)
                )
            )
        }
    }

    fun updateItem(id:String, name: String, qty: Int, price: Double){
        items.first { it.id.equals(id, true)}.apply {
            this.name = name
            this.quantity = qty
            this.price = price
        }
    }
}