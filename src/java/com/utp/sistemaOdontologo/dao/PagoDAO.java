package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.entities.Pago;
import com.utp.sistemaOdontologo.repositories.IPagoRepository;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PagoDAO implements IPagoRepository {
    
    public void insert(Connection con, Integer idCita, String metodoPago, String estadoPago) throws SQLException {
        // La tabla Pagos tiene: id_cita, monto, metodo, estado_pago, fecha_pago
        String SQL = "INSERT INTO Pagos (id_cita, monto, metodo, estado_pago, fecha_pago) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idCita);
            ps.setDouble(2, 0.00); // MONTO INICIAL FIJO CERO (Pago Pendiente)
            ps.setString(3, metodoPago);
            ps.setString(4, estadoPago); // EJ: 'PENDIENTE'
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            
            ps.executeUpdate();
        }
    }
    
    public void updateEstadoPago(Connection con, Integer idCita, String nuevoEstado) throws SQLException {
        // Actualiza el estado y la fecha del pago (para tener el timestamp de cuándo se completó)
        String SQL = "UPDATE Pagos SET estado_pago = ?, fecha_pago = GETDATE() WHERE id_cita = ?";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            ps.executeUpdate();
        }
    }
    public void deleteByCitaId(Connection con, Integer idCita) throws SQLException {

        // 1. Validar que el ID de la Cita sea válido
        if (idCita == null || idCita <= 0) {
            throw new IllegalArgumentException("ID de Cita inválido para la eliminación de Pagos.");
        }

        // 2. QUERY CORRECTO: Elimina Pagos usando la CLAVE FORÁNEA (id_cita)
        String SQL = "DELETE FROM Pagos WHERE id_cita = ?";

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idCita);

            // Ejecutar. No lanzamos excepción si rowsAffected es 0, ya que puede que la cita nunca tuvo pago.
            ps.executeUpdate(); 
        }
    }
    
   

    @Override
    public Integer insert(Connection con, Pago pago) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(Connection con, Pago pago) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(Connection con, Integer idPago) throws SQLException {
           throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody 
    }

    @Override
    public List<Pago> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Pago findById(Connection con, Integer idPago) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
