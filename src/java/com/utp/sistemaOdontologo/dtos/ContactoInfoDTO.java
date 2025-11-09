/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

/**
 *
 * @author ASUS
 */
public class ContactoInfoDTO {
    private Integer idContacto;
    private String telefono;
    private String correo;
    private String direccion;

    public ContactoInfoDTO() {
    }

    public ContactoInfoDTO(Integer idContacto, String telefono, String correo, String direccion) {
        this.idContacto = idContacto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
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
