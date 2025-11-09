/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import com.utp.sistemaOdontologo.entities.enums.Rol;
import java.time.LocalDate;

/**
 *
 * @author ASUS
 */
public class Trabajador {
    private  Integer idTrabajador;
    private  Usuario usuario;
    private Contacto contacto;
    private  TipoDocumento tipoDocumento;
    private  String nombre;
    private  String apellido;
    private Rol rol;
    private  String colegiatura;
    private Especialidad especialidad;
    private  LocalDate fechaRegistro;

    public Trabajador() {
    }

    public Trabajador(Integer idTrabajador, Usuario usuario, Contacto contacto, TipoDocumento tipoDocumento, String nombre, String apellido, Rol rol, String colegiatura, Especialidad especialidad, LocalDate fechaRegistro) {
        this.idTrabajador = idTrabajador;
        this.usuario = usuario;
        this.contacto = contacto;
        this.tipoDocumento = tipoDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.colegiatura = colegiatura;
        this.especialidad = especialidad;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

  
    
}
