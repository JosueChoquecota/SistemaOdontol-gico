/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author ASUS
 */
public class CitaDTOResponse {
    private Integer idCita;           // ID de la cita creada
    private String mensaje;           // Mensaje de confirmación o error
    private LocalDateTime fechaHoraRegistro; // Hora exacta de la creación
    
    // Información mapeada para el usuario
    private LocalDate fechaCita;
    private String horaCita;          // Hora legible (ej: 14:00 - 15:00)
    private String nombreOdontologo;
    private String nombrePaciente;
    private String estadoActual;
    private String motivo;
    
    public CitaDTOResponse() {
    }

    public CitaDTOResponse(Integer idCita, String mensaje, LocalDateTime fechaHoraRegistro, LocalDate fechaCita, String horaCita, String nombreOdontologo, String nombrePaciente, String estadoActual, String motivo) {
        this.idCita = idCita;
        this.mensaje = mensaje;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.nombreOdontologo = nombreOdontologo;
        this.nombrePaciente = nombrePaciente;
        this.estadoActual = estadoActual;
        this.motivo = motivo;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getNombreOdontologo() {
        return nombreOdontologo;
    }

    public void setNombreOdontologo(String nombreOdontologo) {
        this.nombreOdontologo = nombreOdontologo;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
}
