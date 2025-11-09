/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

/**
 *
 * @author ASUS
 */
public class UsuarioInfoDTO {
    private Integer idUsuario;
    private String username;
    private String estado;

    public UsuarioInfoDTO() {
    }

    public UsuarioInfoDTO(Integer idUsuario, String username, String estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.estado = estado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
