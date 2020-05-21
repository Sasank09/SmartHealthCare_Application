package com.example.smarthealthcare.Model;

public class PatientModel {
    private String prescription;
    private String description;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String bp;
    private int age;
    private int heartbeat;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PatientModel() {
    }

    public PatientModel(String prescription, String description, String name, String email, String phone, String address, String password, String bp, int age, int heartbeat, String key) {
        this.prescription = prescription;
        this.description = description;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.bp = bp;
        this.age = age;
        this.heartbeat = heartbeat;
        this.key = key;
    }

    public int getage() {
        return age;
    }

    public void setage(int age) {
        this.age = age;
    }

    public String getbp() {
        return bp;
    }

    public void setbp(String  bp) {
        this.bp = bp;
    }

    public int getheartbeat() {
        return heartbeat;
    }

    public void setheartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getprescription() {
        return prescription;
    }

    public void setprescription(String prescription) {
        this.prescription = prescription;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
