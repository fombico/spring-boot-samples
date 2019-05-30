package com.fombico.testcontainers.samples;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ApiController {

    private final StringRedisTemplate stringRedisTemplate;
    private final BookRepository bookRepository;

    @GetMapping("/foo")
    public String get() {
        return stringRedisTemplate.opsForValue().get("foo");
    }

    @PutMapping("/foo")
    public void set(@RequestBody String value) {
        stringRedisTemplate.opsForValue().set("foo", value);
    }

    @GetMapping("/bar")
    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    @PostMapping("/bar")
    public void addBook(@RequestBody Book book) {
        bookRepository.save(book);
    }
}
