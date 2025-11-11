/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.HistorialCita;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface IHistorialCitaRepository extends ICRUD<HistorialCita, Integer>{
    Integer insert(Connection con, HistorialCita t) throws SQLException;
    Boolean update(Connection con, HistorialCita t) throws SQLException;
    Boolean delete(Connection con, Integer idHistorial) throws SQLException;
    List<HistorialCita> listAll(Connection con) throws SQLException;
    HistorialCita findById(Connection con, Integer idHistorial) throws SQLException;
}
