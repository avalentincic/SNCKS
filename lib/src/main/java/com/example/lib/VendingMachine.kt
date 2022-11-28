package com.example.lib

import io.github.serpro69.kfaker.faker
import java.util.UUID

val faker = faker {  }

class VendingMachine(
    val name: String,
    val latitude: Double,
    val longitude: Double
) {
    var id: UUID = UUID.randomUUID()
    var items: ArrayList<Item> =  ArrayList<Item>()

    override fun toString(): String {
        return "${this.name} ($latitude, $longitude): ${this.items.toString()}"
    }

    fun generate(n: Int){
        for (i in 1..n){
            items.add(
                Item(
                    faker.food.dish(),
                    faker.random.nextInt(intRange = 0..10),
                    faker.random.nextDouble() + faker.random.nextInt(intRange = 0..5)
                )
            )
        }
    }

    fun updateItem(id:String, name: String, qty: Int, price: Double){
        items.first { it.id.toString().equals(id, true)}.apply {
            this.name = name
            this.quantity = qty
            this.price = price
        }
    }
}