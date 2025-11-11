/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import com.utp.sistemaOdontologo.entities.enums.DiaSemana;

/**
 *
 * @author ASUS
 */
public class HorarioOdontologo {
    private  Integer idHorarioOdontologo;
    private  Trabajador trabajador;
    private  Horario horario;
    private  DiaSemana diaSemanaa;

    public HorarioOdontologo() {
    }

    
    public HorarioOdontologo(Integer idHorarioOdontologo, Trabajador trabajador, Horario horario, DiaSemana diaSemanaa) {
        this.idHorarioOdontologo = idHorarioOdontologo;
        this.trabajador = trabajador;
        this.horario = horario;
        this.diaSemanaa = diaSemanaa;
    }

    public Integer getIdHorarioOdontologo() {
        return idHorarioOdontologo;
    }

    public void setIdHorarioOdontologo(Integer idHorarioOdontologo) {
        this.idHorarioOdontologo = idHorarioOdontologo;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public DiaSemana getDiaSemanaa() {
        return diaSemanaa;
    }

    public void setDiaSemanaa(DiaSemana diaSemanaa) {
        this.diaSemanaa = diaSemanaa;
    }
    
    
    
}




