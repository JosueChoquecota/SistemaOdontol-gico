/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.entities.Horario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {
    public String findHoraLegibleById(Connection con, Integer idHorario) throws SQLException {
        // La tabla Horarios tiene horario_inicio y horario_fin
        String SQL = "SELECT CONCAT(horario_inicio, ' - ', horario_fin) FROM Horarios WHERE id_horario = ?";
        String horaLegible = "Hora no definida";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idHorario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    horaLegible = rs.getString(1);
                }
            }
        }
        return horaLegible;
    }
    // NUEVO MÉTODO: Listar todos los horarios para el dropdown
    public List<Horario> findAll() {
        List<Horario> lista = new ArrayList<>();
        ConnectionDataBase db = new ConnectionDataBase();
        Connection con = null;
        
        String sql = "SELECT id_horario, horario_inicio, horario_fin FROM Horarios ORDER BY horario_inicio ASC";
        
        try {
            con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Horario h = new Horario();
                h.setIdHorario(rs.getInt("id_horario"));
                // Convertimos java.sql.Time a java.time.LocalTime
                h.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
                h.setHorarioFin(rs.getTime("horario_fin").toLocalTime());
                lista.add(h);
            }
        } catch (Exception e) {
            System.err.println("Error al listar horarios: " + e.getMessage());
        } finally {
            // Cerrar conexión si es necesario o dejar que el pool lo maneje
             try { if (con != null) con.close(); } catch (SQLException ex) { }
        }
        return lista;
    }
}
