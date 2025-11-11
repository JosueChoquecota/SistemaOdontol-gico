/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.Pago;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface IPagoRepository extends ICRUD<Pago, Integer> {
        Integer insert(Connection con, Pago pago) throws SQLException;
    Boolean update(Connection con, Pago pago) throws SQLException;
    Boolean delete(Connection con, Integer idPago) throws SQLException;
    List<Pago> listAll(Connection con) throws SQLException;
    Pago findById(Connection con, Integer idPago) throws SQLException;
}
