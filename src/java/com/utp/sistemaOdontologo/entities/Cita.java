/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import java.time.LocalDate;

public class Cita {
    private  Integer idCita;
    private  PacienteDatos paciente;
    private  Trabajador trabajador;
    private  Horario horario;
    private EstadoCita estado;
    private  LocalDate fechaCita;
    private String motivo;
    private  String motivacion;

    public Cita() {
    }

    public Cita(Integer idCita, PacienteDatos paciente, Trabajador trabajador, Horario horario, EstadoCita estado, LocalDate fechaCita, String motivo, String motivacion) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.trabajador = trabajador;
        this.horario = horario;
        this.estado = estado;
        this.fechaCita = fechaCita;
        this.motivo = motivo;
        this.motivacion = motivacion;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public PacienteDatos getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDatos paciente) {
        this.paciente = paciente;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }
 
   
    
}
