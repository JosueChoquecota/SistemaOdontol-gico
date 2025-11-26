/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ASUS
 */
public class CitaDTORequest {
    // =================================================================
    // A. CAMPOS DE LA CITA (Tabla CITAS)
    // =================================================================
    
    // Identificadores
    private Integer idCita;         // Para actualizaciones (UPDATE)
    private Integer idTrabajador;   // Odontólogo seleccionado
    private Integer idHorario;      // Slot de tiempo (Calculado en Service o enviado directo)
    private Integer idPaciente;     // ID del paciente (Para flujo Admin)

    // Datos Temporales
    private LocalDate fechaCita;    // Fecha de la cita
    
    // 🔥 CORRECCIÓN 1: Agregado para coincidir con el JSP (name="horaCita")
    private LocalTime horaCita;     
    
    private LocalTime horarioFin;   // Opcional, se puede calcular sumando 30min
    
    // Datos Descriptivos
    private String motivo;          // Motivo de la consulta
    private String estado;          // PENDIENTE, ATENDIDO, etc.
    
    // 🔥 CORRECCIÓN 2: Agregado para el textarea del Modal
    private String observaciones;   

    // =================================================================
    // B. DATOS DEL PACIENTE (Solo para flujo de Registro Público)
    // =================================================================
    
    // Identificación
    private String documento;       // DNI/RUC
    private Integer idTipoDocumento;// 1=DNI
    private String nombresPaciente;    
    private String apellidosPaciente; 
    
    // Contacto
    private String tipoContacto;    // "EMAIL"
    private String telefono;           
    private String correo;             
    private String direccion;          
    
    // =================================================================
    // C. DATOS DE PAGO (Opcional)
    // =================================================================
    private String metodoPago;
    
    public CitaDTORequest() {
    }

    public CitaDTORequest(Integer idCita, Integer idTrabajador, Integer idHorario, Integer idPaciente, LocalDate fechaCita, LocalTime horaCita, LocalTime horarioFin, String motivo, String estado, String observaciones, String documento, Integer idTipoDocumento, String nombresPaciente, String apellidosPaciente, String tipoContacto, String telefono, String correo, String direccion, String metodoPago) {
        this.idCita = idCita;
        this.idTrabajador = idTrabajador;
        this.idHorario = idHorario;
        this.idPaciente = idPaciente;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.horarioFin = horarioFin;
        this.motivo = motivo;
        this.estado = estado;
        this.observaciones = observaciones;
        this.documento = documento;
        this.idTipoDocumento = idTipoDocumento;
        this.nombresPaciente = nombresPaciente;
        this.apellidosPaciente = apellidosPaciente;
        this.tipoContacto = tipoContacto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(LocalTime horaCita) {
        this.horaCita = horaCita;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombresPaciente() {
        return nombresPaciente;
    }

    public void setNombresPaciente(String nombresPaciente) {
        this.nombresPaciente = nombresPaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(String tipoContacto) {
        this.tipoContacto = tipoContacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

}
