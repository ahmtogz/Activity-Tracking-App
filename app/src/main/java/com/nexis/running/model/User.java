package com.nexis.running.model;

public class User implements IUser {
    private String name;
    private String email;
    private String password;
    private String gender;
    private int weight;
    private int age;

    public User() {

    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }


    public User(String name, String email, String password, String gender, int weight, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.weight = weight;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getGender() {
        return this.gender;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public int getAge() {
        return this.age;
    }
}

