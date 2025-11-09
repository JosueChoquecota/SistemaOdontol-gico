/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dtos;

/**
 *
 * @author ASUS
 */
public class EspecialidadDTO {
    private Integer idEspecialidad;
    private String nombre;
    private String acronimo;

    public EspecialidadDTO() {
    }

    public EspecialidadDTO(Integer idEspecialidad, String nombre, String acronimo) {
        this.idEspecialidad = idEspecialidad;
        this.nombre = nombre;
        this.acronimo = acronimo;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }
    
    
}
