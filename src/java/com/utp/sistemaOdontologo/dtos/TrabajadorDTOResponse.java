/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.Usuario;
import java.time.LocalDate;

/**
 *
 * @author ASUS
 */
public class TrabajadorDTOResponse {
    private  Integer idTrabajador;
    private  Integer idUsuario;
    private  UsuarioInfoDTO usuario;
    private  ContactoInfoDTO contacto;
    private  EspecialidadDTO especialidad;
    private String telefono;
    private  Integer idContacto;
    private  Integer idTipoDocumento;
    private  String nombre;
    private  String apellido;
    private  String documento;
    private  String rol;
    private  String colegiatura;
    private  Integer idEspecialidad;
    private  LocalDate fechaRegistro;
    

    public TrabajadorDTOResponse() {
    }

    public TrabajadorDTOResponse(Integer idTrabajador, Integer idUsuario, UsuarioInfoDTO usuario, ContactoInfoDTO contacto, EspecialidadDTO especialidad, String telefono, Integer idContacto, Integer idTipoDocumento, String nombre, String apellido, String documento, String rol, String colegiatura, Integer idEspecialidad, LocalDate fechaRegistro) {
        this.idTrabajador = idTrabajador;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contacto = contacto;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.idContacto = idContacto;
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.rol = rol;
        this.colegiatura = colegiatura;
        this.idEspecialidad = idEspecialidad;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public UsuarioInfoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioInfoDTO usuario) {
        this.usuario = usuario;
    }

    public ContactoInfoDTO getContacto() {
        return contacto;
    }

    public void setContacto(ContactoInfoDTO contacto) {
        this.contacto = contacto;
    }

    public EspecialidadDTO getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(EspecialidadDTO especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
    