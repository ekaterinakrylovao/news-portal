package com.example.newspaper.dtos;

public class UserDto {
    private String firstName;
    private String lastName;

    // Конструктор
    public UserDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Геттеры и сеттеры
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
