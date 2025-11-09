/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.Especialidad;
import com.utp.sistemaOdontologo.entities.TipoDocumento;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.enums.Rol;
import java.time.LocalDate;

/**
 *
 * @author ASUS
 */
public class TrabajadorDTORequest {
    private Integer idTrabajador; 
    private String nombre;
    private String apellido;
    private String colegiatura; // Puede ser NULL
    
    // IDs de Tablas de Catálogo (FKs que ya existen)
    private Integer idTipoDocumento; 
    private Integer idEspecialidad;  // Puede ser NULL
    private Integer idRol;           // ID del ENUM Rol
    
    private LocalDate fechaRegistro; // Se puede asignar en el Servicio/DAO
    
    // -----------------------------------------------------------------
    // B. Campos de Usuario (Para el INSERT en la tabla Usuarios)
    // -----------------------------------------------------------------
    private Integer idUsuario;
    private String username;
    private String contrasena;    // La clave en texto plano del formulario
    
    // -----------------------------------------------------------------
    // C. Campos de Contacto (Para el INSERT en la tabla Contactos)
    // -------------------------------------------------
    private Integer idContacto;
    private String tipoContacto;
    private String telefono;
    private String correo;
    private String direccion;

    public TrabajadorDTORequest() {
    }

    public TrabajadorDTORequest(Integer idTrabajador, String nombre, String apellido, String colegiatura, Integer idTipoDocumento, Integer idEspecialidad, Integer idRol, LocalDate fechaRegistro, Integer idUsuario, String username, String contrasena, Integer idContacto, String tipoContacto, String telefono, String correo, String direccion) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.colegiatura = colegiatura;
        this.idTipoDocumento = idTipoDocumento;
        this.idEspecialidad = idEspecialidad;
        this.idRol = idRol;
        this.fechaRegistro = fechaRegistro;
        this.idUsuario = idUsuario;
        this.username = username;
        this.contrasena = contrasena;
        this.idContacto = idContacto;
        this.tipoContacto = tipoContacto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
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

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
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

    

   
}
