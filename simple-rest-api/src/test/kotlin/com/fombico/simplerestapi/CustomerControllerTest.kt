package com.fombico.simplerestapi

import io.mockk.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(CustomerController::class)
internal class CustomerControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun repository() = mockk<CustomerRepository>()
    }

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var customerRepository: CustomerRepository

    private val baseUrl = "/v1/customers"

    @Test
    fun `options() returns supported http methods`() {
        mvc.perform(request(OPTIONS, baseUrl))
                .andExpect(status().isOk)
                .andExpect(header().string("Allow", "GET,POST,HEAD,OPTIONS,PUT,DELETE"))
    }

    @Test
    fun `getCollection() returns a list of customers`() {
        val list = listOf(
                customer {
                    id = 101
                    name = "Joe"
                },
                customer {
                    id = 102
                    name = "Tom"
                })

        every { customerRepository.findAll() } returns list

        mvc.perform(request(GET, baseUrl))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.[0].id", `is`(101)))
                .andExpect(jsonPath("$.[0].name", `is`("Joe")))
                .andExpect(jsonPath("$.[1].id", `is`(102)))
                .andExpect(jsonPath("$.[1].name", `is`("Tom")))
    }

    @Test
    fun `getById() gets customer by id`() {
        every { customerRepository.findById(20) } returns
                Optional.of(customer {
                    id = 20
                    name = "Joe"
                })

        mvc.perform(request(GET, "$baseUrl/20"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`(20)))
                .andExpect(jsonPath("$.name", `is`("Joe")))
    }

    @Test
    fun `getById() returns 404 if customer is not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        mvc.perform(request(GET, "$baseUrl/20"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `post() inserts a customer`() {
        val customer = customer {
            id = 20
            name = "Joe"
        }

        every { customerRepository.findById(customer.id) } returns Optional.of(customer)
        every { customerRepository.save(any<Customer>()) } returns customer

        mvc.perform(request(POST, baseUrl)
                .content("""{
                        "name":"Joe"
                    }""".trimIndent())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andExpect(header().string("location", endsWith("$baseUrl/20")))
                .andExpect(jsonPath("$.id", `is`(20)))
                .andExpect(jsonPath("$.name", `is`("Joe")))
    }

    @Test
    fun `delete() deletes customer`() {
        val customer = customer {
            id = 20
            name = "Joe"
        }

        every { customerRepository.findById(customer.id) } returns Optional.of(customer)
        every { customerRepository.deleteById(customer.id) } just Runs

        mvc.perform(request(DELETE, "$baseUrl/20"))
                .andExpect(status().isNoContent)

        verify { customerRepository.deleteById(20) }
    }

    @Test
    fun `delete() returns 404 if customer is not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        mvc.perform(request(DELETE, "$baseUrl/20"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `head() returns 201 No Content if customer exists`() {
        every { customerRepository.findById(any()) } returns
                Optional.of(customer {
                    id = 20
                    name = "Joe"
                })

        mvc.perform(request(HEAD, "$baseUrl/20"))
                .andExpect(status().isNoContent)
    }

    @Test
    fun `head() returns 404 Not Found if customer is not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        mvc.perform(request(HEAD, "$baseUrl/20"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `put() updates a customer`() {
        every { customerRepository.findById(20) } returns
                Optional.of(customer {
                    id = 20
                    name = "Joe"
                })

        every { customerRepository.save(any<Customer>())} returns
                customer {
                    id = 20
                    name = "John"
                }

        mvc.perform(request(PUT, "$baseUrl/20")
                .content("""{
                    "id": 112345,
                    "name": "John"
                    }""".trimIndent())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`(20)))
                .andExpect(jsonPath("$.name", `is`("John")))
    }

    @Test
    fun `put() returns 404 Not Found if customer is not found`() {
        every { customerRepository.findById(any()) } returns Optional.empty()

        mvc.perform(request(PUT, "$baseUrl/20")
                .content("""{
                    "id": 20,
                    "name": "John"
                    }""".trimIndent())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }
}