/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.Trabajador;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ITrabajadorRepository {
   // Inserción: Devuelve éxito (el ID ya lo generó el Service a través de Usuario/Contacto).
    Boolean insert(Connection con, Trabajador trabajador) throws SQLException;
    
    // Actualización
    Boolean update(Connection con, Trabajador trabajador) throws SQLException;
    
    // Eliminación
    Boolean delete(Connection con, Integer idTrabajador) throws SQLException;
    
    // Lectura de Datos (para Listar, Buscar y Eliminar - Transaccional)
    Trabajador findById(Connection con, Integer idTrabajador) throws SQLException;
    List<Trabajador> findAll(Connection con) throws SQLException;
    
    // Método auxiliar para el DELETE y UPDATE transaccional
    int[] findFksById(Connection con, Integer idTrabajador) throws SQLException;
}
