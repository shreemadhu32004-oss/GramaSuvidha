package com.gramasuvidha.app.model;

public class User {
    private String name;
    private String phone;
    private String village;
    private String password;
    private String role; // "citizen" or "admin"

    public User() {}

    public User(String name, String phone, String village, String password, String role) {
        this.name = name;
        this.phone = phone;
        this.village = village;
        this.password = password;
        this.role = role;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getVillage() { return village; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setVillage(String village) { this.village = village; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}
