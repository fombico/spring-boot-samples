package com.fombico.contentnegotiation;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/person")
public class PersonController {

    @GetMapping()
    public Person getPerson() {
        return new Person("Joe", 20);
    }

    @PostMapping()
    public Person addPerson(@Valid @RequestBody Person person) {
        return person;
    }
}
