/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import java.time.LocalDate;

public class Cita {
    private  Integer idCita;
    private  Paciente paciente;
    private  Trabajador trabajador;
    private  Horario horario;
    private EstadoCita estado;
    private  LocalDate fechaCita;
    private String motivo;
    private  LocalDate fechaRegistro;

    public Cita() {
    }
 
    public Cita(Integer idCita, Paciente paciente, Trabajador trabajador, Horario horario, EstadoCita estado, LocalDate fechaCita, String motivo, LocalDate fechaRegistro) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.trabajador = trabajador;
        this.horario = horario;
        this.estado = estado;
        this.fechaCita = fechaCita;
        this.motivo = motivo;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdCita() {
        return idCita;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public Trabajador getTrabajador() {
        return trabajador;
    }
    public Horario getHorario() {
        return horario;
    }
    public EstadoCita getEstado() {
        return estado;
    }
    public LocalDate getFechaCita() {
        return fechaCita;
    }
    public String getMotivo() {
        return motivo;
    }
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }  
}
