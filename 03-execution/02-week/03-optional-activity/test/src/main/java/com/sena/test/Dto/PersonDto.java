package com.sena.test.Dto;

public class PersonDto {

    private Integer id;
    private String name;
    private int edad;
    
    public PersonDto(Integer id, String name, int edad) {
        this.id = id;
        this.name = name;
        this.edad = edad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    

}
