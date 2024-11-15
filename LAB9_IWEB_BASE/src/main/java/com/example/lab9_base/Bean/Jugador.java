package com.example.lab9_base.Bean;

public class Jugador {
    private int idJugadores;
    private String nombre;
    private int edad;
    private String posicion;
    private String club;
    private Seleccion selecion;

    public int getIdJugadores() {
        return idJugadores;
    }

    public void setIdJugadores(int idJugadores) {
        this.idJugadores = idJugadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public Seleccion getSelecion() {
        return selecion;
    }

    public void setSelecion(Seleccion selecion) {
        this.selecion = selecion;
    }

}
