package com.example.proyectapp_web;

public class User {
    private String id;
    private String nombre;
    private String email;
    private String rol;
    private String img;
    public User(String id, String nombre, String email, String rol, String img) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.img = img;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
