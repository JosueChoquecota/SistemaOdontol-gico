/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.HorarioOdontologo;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.DayOfWeek;

/**
 *
 * @author ASUS
 */
public interface IHorarioOdontologoRepository extends ICRUD<HorarioOdontologo, Integer> {
    Boolean checkAvailability(Connection con, Integer idTrabajador, java.time.LocalDate fechaCita, Integer idHorario) throws SQLException;
}
