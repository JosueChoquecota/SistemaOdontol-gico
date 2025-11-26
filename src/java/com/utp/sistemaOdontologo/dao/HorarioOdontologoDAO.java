/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.entities.HorarioOdontologo;
import com.utp.sistemaOdontologo.repositories.IHorarioOdontologoRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.sql.*;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class HorarioOdontologoDAO implements IHorarioOdontologoRepository {

    @Override
    public Boolean checkAvailability(Connection con, Integer idTrabajador, java.time.LocalDate fechaCita, Integer idHorario) throws SQLException {

        // El SQL ya no usa dia_semana.name(), usa la columna DATE
        String SQL = "SELECT 1 FROM HorariosOdontologos WHERE id_trabajador = ? AND id_horario = ? AND dia_semana = ?";

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idTrabajador);
            ps.setInt(2, idHorario);

            // CORRECCIÓN: Conversión explícita de LocalDate a java.sql.Date
            ps.setDate(3, java.sql.Date.valueOf(fechaCita)); 

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }
    public void deleteByTrabajadorId(Connection con, Integer idTrabajador) throws SQLException {
        String sql = "DELETE FROM HorariosOdontologos WHERE id_trabajador = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTrabajador);
            ps.executeUpdate();
        }
    }
    public Integer findIdByHoraInicio(Connection con, java.time.LocalTime hora) throws SQLException {
        String sql = "SELECT id_horario FROM Horarios WHERE hora_inicio = ?";
        // Nota: SQL Server usa tipos TIME, asegúrate de que coincida con tu BD
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTime(1, java.sql.Time.valueOf(hora));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_horario");
                }
            }
        }
        return null; // Retorna null si no encuentra esa hora exacta
    }

    @Override
    public Integer insert(Connection con, HorarioOdontologo t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(Connection con, HorarioOdontologo t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(Connection con, Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<HorarioOdontologo> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public HorarioOdontologo findById(Connection con, Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
