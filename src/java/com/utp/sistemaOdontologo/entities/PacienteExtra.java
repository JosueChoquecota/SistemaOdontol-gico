/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.entities;

import com.utp.sistemaOdontologo.entities.enums.Sexo;
import java.time.LocalDate;


public class PacienteExtra {
    private  Integer idExtra;
    private  Paciente paciente;
    private  LocalDate fechaNacimiento;
    private Sexo sexo;

    public PacienteExtra() {
    }

    public PacienteExtra(Integer idExtra, Paciente paciente, LocalDate fechaNacimiento, Sexo sexo) {
        this.idExtra = idExtra;
        this.paciente = paciente;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
    }

    public Integer getIdExtra() {
        return idExtra;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    
}
