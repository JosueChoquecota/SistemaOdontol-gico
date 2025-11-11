/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

import java.time.LocalDate;

/**
 *
 * @author ASUS
 */
public class CitaDTORequest {
    // =================================================================
    // A. CAMPOS PRINCIPALES DE LA CITA (Tabla CITAS)
    // =================================================================
    
    // 1. FKs de la Cita (Selecciones del formulario)
    private Integer idCita;
    private Integer idTrabajador;     // Odontólogo seleccionado
    private Integer idHorario;        // Slot de tiempo seleccionado
    
    private LocalDate fechaCita;      // Fecha de la cita
    private String motivo;            // Motivo de la consulta
    
    // 2. Identificación del Paciente
    private Integer idPaciente;       // ID del paciente existente (Será NULL en este flujo)

    // =================================================================
    // B. DATOS DEL PACIENTE (Para PacientesDatos y Contactos, si es nuevo)
    // =================================================================
    
    // Identificación
    private String documento;         // Número de DNI/Documento
    private Integer idTipoDocumento;  // ID del tipo de documento (ej: DNI=1)
    private String nombresPaciente;   
    private String apellidosPaciente; 
    
    // Datos de Contacto (Para la tabla Contactos)
    private String tipoContacto;    // Ej: "EMAIL" o "PHONE"
    private String telefono;          
    private String correo;            
    private String direccion;         
    
    // =================================================================
    // C. DATOS DE PAGO Y NOTIFICACIÓN (Ajustados)
    // =================================================================
    private String metodoPago;        // Mapea a: Método de pago (para inserción en Pagos, sin monto)
    
    public CitaDTORequest() {
    }

    public CitaDTORequest(Integer idCita, Integer idTrabajador, Integer idHorario, LocalDate fechaCita, String motivo, Integer idPaciente, String documento, Integer idTipoDocumento, String nombresPaciente, String apellidosPaciente, String tipoContacto, String telefono, String correo, String direccion, String metodoPago) {
        this.idCita = idCita;
        this.idTrabajador = idTrabajador;
        this.idHorario = idHorario;
        this.fechaCita = fechaCita;
        this.motivo = motivo;
        this.idPaciente = idPaciente;
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

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
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
