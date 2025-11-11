/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
