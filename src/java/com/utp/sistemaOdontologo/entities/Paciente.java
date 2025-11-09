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
public class Paciente {
    private  Integer idPacient;
    private  TipoDocumento tipoDocumento;
    private  String primerNombre;
    private  String apellido;
    private  LocalDate fechaRegistro;
    private  Contacto contacto;

    public Paciente() {
    }

    
    
    public Paciente(Integer idPacient, TipoDocumento tipoDocumento, String primerNombre, String apellido, LocalDate fechaRegistro, Contacto contacto) {
        this.idPacient = idPacient;
        this.tipoDocumento = tipoDocumento;
        this.primerNombre = primerNombre;
        this.apellido = apellido;
        this.fechaRegistro = fechaRegistro;
        this.contacto = contacto;
    }

    public Integer getIdPacient() {
        return idPacient;
    }
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }
    public String getPrimerNombre() {
        return primerNombre;
    }
    public String getApellido() {
        return apellido;
    }
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    public Contacto getContacto() {
        return contacto;
    } 
}
