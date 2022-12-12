package com.example.proyectovalorant.model;

public class Objeto {

    //Creamos los atributos de la clase objeto
    private String id;
    private String titulo;
    private String descripcion;
    private String fotoId;

    //Implementamos su constructor
    public Objeto(String id,String titulo, String descripcion, String fotoId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fotoId = fotoId;
    }

    //Metodos Getters y Setters

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoId() {
        return fotoId;
    }

    public void setFotoId(String fotoId) {
        this.fotoId = fotoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Metodo toString para devolver valores como String

    @Override
    public String toString() {
        return "Objeto{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fotoId='" + fotoId + '\'' +
                '}';
    }
}
