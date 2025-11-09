/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities.enums;

import java.util.stream.Stream;

public enum Rol {
    ODONTOLOGO(1),
    ASISTENTE(2),
    RECEPCIONISTA(3),
    ADMINISTRADOR(4);
    
    private final Integer id;

    private Rol(Integer id) {
        this.id = id;
    }

    public static Rol getODONTOLOGO() {
        return ODONTOLOGO;
    }

    public static Rol getASISTENTE() {
        return ASISTENTE;
    }

    public static Rol getRECEPCIONISTA() {
        return RECEPCIONISTA;
    }

    public static Rol getADMINISTRADOR() {
        return ADMINISTRADOR;
    }

    public Integer getId() {
        return id;
    }
    
    // 4. Método estático de utilidad para mapear el ID al Enum (¡Lo que faltaba!)
    public static Rol fromId(Integer id) {
        if (id == null) {
            return null; // Manejo de nulos si es necesario
        }
        return Stream.of(Rol.values())
                     .filter(r -> r.getId().equals(id))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("ID de Rol desconocido: " + id));
    }
}
