/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;
import com.utp.sistemaOdontologo.entities.HistorialCita;
import com.utp.sistemaOdontologo.repositories.IHistorialCitaRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;
import java.sql.PreparedStatement;
/**
 *
 * @author ASUS
 */
public class HistoriaCitaDAO implements IHistorialCitaRepository {
    
    public void insert(Connection con, Integer idCita, Integer idEstadoInicial, String observacion) throws SQLException {
        // La tabla HistoriaCita tiene: id_cita, id_estado, fecha_cambio, observaciones
        // Usamos GETDATE() en SQL Server para el timestamp de la transacción.
        String SQL = "INSERT INTO HistoriaCita (id_cita, id_estado, observaciones, fecha_cambio) VALUES (?, ?, ?, GETDATE())";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            
            // 1. Asignar IDs
            ps.setInt(1, idCita);
            ps.setInt(2, idEstadoInicial);
            
            // 2. Asignar Observación
            ps.setString(3, observacion);
            
            // 3. Ejecutar (No esperamos un valor de retorno como un ID generado, solo la ejecución)
            ps.executeUpdate();
        }
    }
    @Override
    public Integer insert(Connection con, HistorialCita t) throws SQLException {
        String SQL = "INSERT INTO HistoriaCita (id_cita, id_estado, fecha_cambio, observaciones) OUTPUT INSERTED.id_historial VALUES (?, ?, GETDATE(), ?)";        
        Integer idHistorialGenerado = null;
        
        try(PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, t.getCita().getIdCita());
            ps.setInt(2, t.getEstado().getIdEstado());
            ps.setString(3, t.getObservacion());
            
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    idHistorialGenerado = rs.getInt(1);
                }
            }
        }
         if (idHistorialGenerado == null) {
            throw new SQLException("ERROR DAO: No se pudo Obtener ID de HistorialCita.");
        }
         return idHistorialGenerado;
    }

    @Override
    public Boolean delete(Connection con, Integer idHistorial) throws SQLException {
      String SQL = "DELETE FROM HistoriaCita WHERE id_historial = ?";
        try(PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idHistorial);
            return ps.executeUpdate() > 0;
        }
    }
    
    // En HistoriaCitaDAO.java
    public void deleteByCitaId(Connection con, Integer idCita) throws SQLException {
        // Borra TODAS las filas de historial que pertenezcan a esta cita
        String sql = "DELETE FROM HistoriaCita WHERE id_cita = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            ps.executeUpdate();
        }
    }
    @Override
    public List<HistorialCita> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public HistorialCita findById(Connection con, Integer idHistorial) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(Connection con, HistorialCita t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
