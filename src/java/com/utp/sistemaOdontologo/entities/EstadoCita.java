/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

/**
 *
 * @author ASUS
 */
public class EstadoCita {
    private  Integer idEstado;
    private  String nombreEstado;

    public EstadoCita() {
    }

    public EstadoCita(Integer idEstado, String nombreEstado) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
    }
    public Integer getIdEstado() {
        return idEstado;
    }
    public String getNombreEstado() {
        return nombreEstado;
    }
}
