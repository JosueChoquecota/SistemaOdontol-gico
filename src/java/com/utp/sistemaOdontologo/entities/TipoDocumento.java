/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

/**
 *
 * @author ASUS
 */
public class TipoDocumento {
    private Integer idTipoDocumento;
    private String nombre;
    private int longitud;

    public TipoDocumento() {
    }

    
    
    public TipoDocumento(Integer idTipoDocumento, String nombre, int longitud) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
        this.longitud = longitud;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }
    public String getNombre() {
        return nombre;
    }
    public int getLongitud() {
        return longitud;
    }  

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }
    
    
}
