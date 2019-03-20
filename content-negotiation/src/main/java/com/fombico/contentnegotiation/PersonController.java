package com.fombico.contentnegotiation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/person")
public class PersonController {

    @GetMapping()
    public ResponseEntity<Person> getPerson() {
        return ResponseEntity.ok(new Person("Joe", 20));
    }

    @PostMapping()
    public String addPerson(@Valid @RequestBody Person person) {
        return person.getName() + " is " + person.getAge() + " years old";
    }
}
