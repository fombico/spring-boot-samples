package com.fombico.contentnegotiation;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "person")
public class Person {
    @NotNull
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @NotNull
    @Min(1)
    @JacksonXmlProperty
    private int age;

    public Person(){}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
