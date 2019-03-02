package com.fombico.simplerestapi

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

fun customer(block: Customer.() -> Unit): Customer = Customer().apply(block)

@Entity
data class Customer(
        @Id @GeneratedValue
        var id: Long = 0,
        var name: String? = null
)
