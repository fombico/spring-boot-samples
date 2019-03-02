package com.fombico.simplerestapi

import org.springframework.http.HttpMethod.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder

@RestController
@RequestMapping("/v1/customers")
class CustomerController(val customerRepository: CustomerRepository) {

    @RequestMapping(method = [RequestMethod.OPTIONS])
    fun options(): ResponseEntity<Any> {
        return ResponseEntity.ok()
                .allow(GET, POST, HEAD, OPTIONS, PUT, DELETE)
                .build()
    }

    @GetMapping
    fun getCollection(): ResponseEntity<Collection<Customer>> {
        return ResponseEntity.ok(customerRepository.findAll().toList())
    }

    @GetMapping(value = ["/{id}"])
    fun getById(@PathVariable id: Long): ResponseEntity<Customer> {
        return customerRepository.findById(id)
                .map { customer -> ResponseEntity.ok(customer) }
                .orElseThrow { CustomerNotFoundException() }
    }

    @PostMapping
    fun post(@RequestBody customer: Customer): ResponseEntity<Customer> {
        val saved = customerRepository.save(customer)
        val uri = MvcUriComponentsBuilder.fromController(this.javaClass)
                .path("/{id}")
                .buildAndExpand(saved.id)
                .toUri()
        return ResponseEntity.created(uri).body(saved)
    }

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return customerRepository.findById(id)
                .map { customer ->
                    customerRepository.deleteById(customer.id)
                    ResponseEntity.noContent().build<Void>()
                }
                .orElseThrow { CustomerNotFoundException() }
    }

    @RequestMapping(method = [RequestMethod.HEAD], value = ["/{id}"])
    fun head(@PathVariable id: Long): ResponseEntity<Void> {
        return customerRepository.findById(id)
                .map { ResponseEntity.noContent().build<Void>() }
                .orElseThrow { CustomerNotFoundException() }
    }

    @PutMapping(value = ["/{id}"])
    fun put(@PathVariable id: Long,
            @RequestBody customer: Customer): ResponseEntity<Customer> {
        return customerRepository.findById(id)
                .map { existingCustomer ->
                    val saved = customerRepository.save(customer {
                        this.id = existingCustomer.id
                        name = customer.name
                    })
                    ResponseEntity.ok(saved)
                }
                .orElseThrow { CustomerNotFoundException() }
    }
}