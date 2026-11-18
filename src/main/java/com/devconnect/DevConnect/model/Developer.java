package com.devconnect.DevConnect.model;

import java.util.ArrayList;
import java.util.List;

public class Developer {
    private Long id;
    private String name;
    private String email;
    private List<String> skills = new ArrayList<>();
    private String city;
    private String age;
    private String gender;
    private String experience;

    public Developer() {
    }

    public Developer(Long id, String name, String email, List<String> skills, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        setSkills(skills);
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills == null ? new ArrayList<>() : new ArrayList<>(skills);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
