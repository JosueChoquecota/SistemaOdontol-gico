/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.entities.EstadoCita;
import com.utp.sistemaOdontologo.repositories.IEstadoCitaRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class EstadoCitaDAO implements IEstadoCitaRepository {

    @Override
    public Integer findIdByNombre(Connection con, String nombreEstado) throws SQLException {
    String SQL = "SELECT id_estado FROM EstadoCitas WHERE nombre = ?";

            try (PreparedStatement ps = con.prepareStatement(SQL)) {
                ps.setString(1, nombreEstado);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_estado");
                    }
                }
            }
        // Lanza excepción si el estado 'Pendiente' no está en la BD (error de catálogo)
        throw new SQLException("Error de catálogo: Estado de Cita '" + nombreEstado + "' no encontrado.");
    }    

    @Override
    public Integer insert(Connection con, EstadoCita t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(Connection con, EstadoCita t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(Connection con, Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EstadoCita> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EstadoCita findById(Connection con, Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
