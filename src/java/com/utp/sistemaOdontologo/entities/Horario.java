/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import java.time.LocalTime;

/**
 *
 * @author ASUS
 */
public class Horario {
    private  Integer idHorario;
    private  LocalTime horarioInicio;
    private  LocalTime horarioFin;

    public Horario() {
    }

    
    public Horario(Integer idHorario, LocalTime horarioInicio, LocalTime horarioFin) {
        this.idHorario = idHorario;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

   
    
}
