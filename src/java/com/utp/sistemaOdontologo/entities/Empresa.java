/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import java.time.LocalDate;

public class Empresa {
    private  Integer idEmpresa;
    private  String nombre;
    private  String ruc;
    private  String direccion;
    private  String telefono;
    private  String correo;
    private  String logo;
    private  LocalDate fechaCreacion;

    public Empresa() {
    }

    public Empresa(Integer idEmpresa, String nombre, String ruc, String direccion, String telefono, String correo, String logo, LocalDate fechaCreacion) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.logo = logo;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
