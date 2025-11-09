/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import java.time.LocalDate;

/**
 *
 * @author ASUS
 */
public class HistorialCita {
    private  Integer idHistorial;
    private  Cita cita;
    private  EstadoCita estado;
    private  LocalDate fechaCambio;
    private  String observacion;

    public HistorialCita() {
    }

    
    
    public HistorialCita(Integer idHistorial, Cita cita, EstadoCita estado, LocalDate fechaCambio, String observacion) {
        this.idHistorial = idHistorial;
        this.cita = cita;
        this.estado = estado;
        this.fechaCambio = fechaCambio;
        this.observacion = observacion;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }
    public Cita getCita() {
        return cita;
    }
    public EstadoCita getEstado() {
        return estado;
    }
    public LocalDate getFechaCambio() {
        return fechaCambio;
    }
    public String getObservacion() {
        return observacion;
    }
}
