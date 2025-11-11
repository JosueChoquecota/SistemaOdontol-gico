/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.Cita;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;
import java.util.List;
/**
 *
 * @author ASUS
 */
public interface ICitaRepository extends ICRUD<Cita, Integer> {
    Integer insert(Connection con, Cita cita) throws SQLException;
    Cita findById(Connection con, Integer idCita) throws SQLException;
    List<Cita> listAll(Connection con) throws SQLException;
    Boolean delete(Connection con, Integer idCita) throws SQLException;
    Boolean update(Connection con, Cita cita) throws SQLException;
}