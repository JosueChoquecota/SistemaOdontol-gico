/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;
import com.utp.sistemaOdontologo.entities.EstadoCita;
import java.sql.SQLException;
import java.sql.Connection;

public interface IEstadoCitaRepository extends ICRUD <EstadoCita, Integer> {
    Integer findIdByNombre(Connection con, String nombreEstado) throws SQLException;
}
