/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.repositories.IContactoRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class ContactoDAO implements IContactoRepository{
    
    @Override
    public Integer insert(Connection con, Contacto contacto) throws SQLException {
        // Usamos OUTPUT INSERTED.id_contacto para SQL Server
        String SQL = "INSERT INTO Contactos ( correo, telefono, direccion,tipo_contacto) OUTPUT INSERTED.id_contacto VALUES (?, ?, ?,?);";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            
            ps.setString(1, contacto.getCorreo());
            ps.setString(2, contacto.getTelefono());
            ps.setString(3, contacto.getDireccion());
            ps.setString(4, contacto.getTipoContacto());
            
            try (ResultSet rs = ps.executeQuery()) { 
                if (rs.next()) {
                    return rs.getInt(1); 
                }
            }
        }
        throw new SQLException("Fallo al obtener ID de Contacto generado.");
    }

    @Override
    public Boolean update(Connection con, Contacto t) throws SQLException {
        String SQL = "UPDATE Contactos SET " + 
                         "telefono = ?, correo = ?, direccion = ? " + 
                         "WHERE id_contacto = ?";

            try (PreparedStatement ps = con.prepareStatement(SQL)) {

                // 1. Asignar nuevos valores (de la entidad Contacto fusionada)
                ps.setString(1, t.getTelefono());
                ps.setString(2, t.getCorreo());
                ps.setString(3, t.getDireccion());

                // 2. Condición WHERE (PK)
                ps.setInt(4, t.getIdContacto());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 0) {
                     // Esto forzará el rollback si el registro no existe
                     throw new SQLException("Error DAO: El Contacto ID " + t.getIdContacto() + " no existe o no se pudo actualizar.");
                }

                return rowsAffected > 0;
            }
        
    }
    

    @Override
    public Boolean delete(Connection con, Integer idContacto) throws SQLException {
        String SQL = "DELETE FROM Contactos WHERE id_contacto = ?;";
                try (PreparedStatement ps = con.prepareStatement(SQL)) {
                    ps.setInt(1, idContacto);
                    return ps.executeUpdate() > 0;
                }    
    }

    public Boolean delete(Connection con, Contacto t) throws SQLException {
        String SQL = "DELETE FROM Contactos WHERE id_contacto = ?";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            
            ps.setInt(1, t.getIdContacto());

            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected == 0) {
                 // Esto es crucial para forzar el ROLLBACK en el Service si la eliminación falla
                 throw new SQLException("Error DAO: El Contacto ID " + t.getIdContacto() + " no fue encontrado para eliminar.");
            }
            
            return rowsAffected > 0;
        }
    }
    @Override
    public Contacto findById(Connection con, Integer idContacto) throws SQLException {
        String SQL = "SELECT id_contacto, tipo_contacto, telefono, correo, direccion FROM Contactos WHERE id_contacto = ?";
        Contacto contacto = null;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idContacto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    contacto = new Contacto();

                    // Mapeo de campos
                    contacto.setIdContacto(rs.getInt("id_contacto"));
                    contacto.setTipoContacto(rs.getString("tipo_contacto"));
                    contacto.setTelefono(rs.getString("telefono"));
                    contacto.setCorreo(rs.getString("correo"));
                    contacto.setDireccion(rs.getString("direccion"));
                }
            }
        }
        return contacto;
    }

    @Override
    public List<Contacto> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
