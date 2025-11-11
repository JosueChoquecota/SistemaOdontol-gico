/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;

import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.repositories.IPacienteRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements IPacienteRepository {
    
    // Método para buscar paciente por su documento (DNI, RUC, etc.)
    @Override
    public PacienteDatos findByDocumento(Connection con, String documento) throws SQLException {
    String SQL = "SELECT id_paciente, id_contacto, id_usuario FROM PacientesDatos WHERE documento = ?";        
    PacienteDatos paciente = null;

            try (PreparedStatement ps = con.prepareStatement(SQL)) {
                ps.setString(1, documento);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        paciente = new PacienteDatos();
                        paciente.setIdPaciente(rs.getInt("id_paciente"));
                        // NOTA: Se necesitan métodos findById en ContactoDAO/UsuarioDAO para cargar estos objetos.

                        // Solo cargamos los IDs, el Service los manejará
                        // int idContacto = rs.getInt("id_contacto"); 
                        // int idUsuario = rs.getInt("id_usuario");
                    }
                }
            }
            return paciente;
        }

   @Override
public Integer insert(Connection con, PacienteDatos paciente) throws SQLException {
    // Columnas: id_tipo_doc (1), id_contacto (2), documento (3), nombres (4), apellidos (5), sexo (6)
    String SQL = "INSERT INTO PacientesDatos (id_tipo_doc, id_contacto, documento, nombres, apellidos, sexo) " +
                 "OUTPUT INSERTED.id_paciente VALUES (?, ?, ?, ?, ?, ?);";

    try (PreparedStatement ps = con.prepareStatement(SQL)) {
        // 1. Asignar FKs y Documento
        ps.setInt(1, paciente.getTipoDocumento().getIdTipoDocumento());
        ps.setInt(2, paciente.getContacto().getIdContacto());
        ps.setString(3, paciente.getDocumento());
        
        // 2. ASIGNACIÓN FALTANTE (Parámetros 4, 5, 6)
        ps.setString(4, paciente.getNombres());    // <-- Parámetro 4 (Nombres)
        ps.setString(5, paciente.getApellidos());  // <-- Parámetro 5 (Apellidos)
        
        // El campo 'sexo' no viene en tu DTO Request, debes darle un valor por defecto o NULL
        // Asumiremos 'OTRO' como valor por defecto, ya que debe ser NOT NULL
        String sexoValor = paciente.getSexo() != null ? paciente.getSexo() : "OTRO";       
        ps.setString(6, sexoValor);             // <-- Parámetro 6 (Sexo)

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    throw new SQLException("Error DAO: Fallo al insertar Paciente.");
}

    @Override
    public String findNombreCompletoById(Connection con, Integer idPaciente) throws SQLException {
    // La consulta requiere JOIN con Contactos si los datos están divididos, o solo PacientesDatos
        String SQL = "SELECT nombres, apellidos FROM PacientesDatos WHERE id_paciente = ?";
        String nombreCompleto = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellidos");
                }
            }
        }
        return nombreCompleto;
    }

    @Override
    public List<PacienteDTOResponse> listAllP(Connection con) throws SQLException {
    String SQL = "SELECT p.id_paciente, p.nombres, p.apellidos, p.documento, c.correo, c.telefono "
                   + "FROM PacientesDatos p "
                   + "INNER JOIN Contactos c ON p.id_contacto = c.id_contacto "
                   + "ORDER BY p.id_paciente ASC"; // O el orden que desees

        // NOTA: Asumo que tienes un PacienteDTOResponse (o similar) para la lista
        List<PacienteDTOResponse> listaResponse = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            // 2. Iteración sobre los resultados y Mapeo al DTO de Respuesta
            while (rs.next()) {
                PacienteDTOResponse response = new PacienteDTOResponse();

                // Mapeo directo a los campos necesarios para la vista (Figma)
                response.setCodigo("P" + rs.getInt("id_paciente")); // Generar un código como C12/P12
                response.setNombre(rs.getString("nombres"));
                response.setApellido(rs.getString("apellidos"));
                response.setDniRuc(rs.getString("documento"));
                response.setCorreo(rs.getString("correo"));
                response.setTelefono(rs.getString("telefono"));

                listaResponse.add(response);
            }
        }

        // Manejo de excepciones (SQLException) es propagado al Service
        return listaResponse;    
    }

    @Override
    public Boolean update(Connection con, PacienteDatos t) throws SQLException {
   String SQL = "UPDATE PacientesDatos SET " + 
                     "id_tipo_doc = ?, documento = ?, nombres = ?, " +
                     "apellidos = ?, sexo = ?, fecha_nacimiento = ? " +
                     "WHERE id_paciente = ?";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            
            // 1. Asignar datos principales
            ps.setInt(1, t.getTipoDocumento().getIdTipoDocumento());
            ps.setString(2, t.getDocumento());
            ps.setString(3, t.getNombres());
            ps.setString(4, t.getApellidos());
            
            // 2. Manejo de campos opcionales (Sexo y Fecha de Nacimiento)
            
            // Sexo (Asumimos que la entidad tiene un getter para el String o Enum)
            String sexoValue = t.getSexo() != null ? t.getSexo() : null; // Asumimos que getSexo devuelve String
            if (sexoValue != null) {
                 ps.setString(5, sexoValue);
            } else {
                 ps.setNull(5, Types.VARCHAR);
            }

            // Fecha de Nacimiento (Manejo de LocalDate a SQL Date)
            if (t.getFechaNacimiento() != null) {
                ps.setDate(6, Date.valueOf(t.getFechaNacimiento()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            
            // 3. Condición WHERE
            ps.setInt(7, t.getIdPaciente());

            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected == 0) {
                 // Esto forzará el rollback si el registro no existe
                 throw new SQLException("Error DAO: El paciente ID " + t.getIdPaciente() + " no fue encontrado para actualizar.");
            }
            
            return rowsAffected > 0;
        }
    }
  

    
    public Boolean delete(Connection con, PacienteDatos t) throws SQLException {
    String SQL = "DELETE FROM PacientesDatos WHERE id_paciente = ?";

            try (PreparedStatement ps = con.prepareStatement(SQL)) {

                ps.setInt(1, t.getIdPaciente());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 0) {
                     // Esto es crucial para forzar el ROLLBACK en el Service si la eliminación falla
                     throw new SQLException("Error DAO: El paciente ID " + t.getIdPaciente() + " no fue encontrado para eliminar.");
                }

                return rowsAffected > 0;
            }
    }    


    @Override
    public PacienteDatos findById(Connection con, Integer id) throws SQLException {
    String SQL = "SELECT id_paciente, id_contacto, id_tipo_doc, nombres, apellidos, documento, fecha_nacimiento, sexo FROM PacientesDatos WHERE id_paciente = ?";
        PacienteDatos paciente = null;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = new PacienteDatos();

                    // Mapeo de campos principales
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                    paciente.setNombres(rs.getString("nombres"));
                    paciente.setApellidos(rs.getString("apellidos"));
                    paciente.setDocumento(rs.getString("documento"));
                    // ... otros campos como fecha_nacimiento, sexo ...

                    // Mapeo del FK de Contacto (como STUB)
                    // Esto permite que el PacienteService sepa qué ID de Contacto buscar a continuación.
                    Contacto contactoStub = new Contacto();
                    contactoStub.setIdContacto(rs.getInt("id_contacto"));
                    paciente.setContacto(contactoStub);

                    // Mapeo de Tipo Documento (Stub)
                    // [Similar a Contacto, si es necesario]
                }
            }
        }
        return paciente;  
    }

    @Override
    public List<PacienteDatos> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(Connection con, Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}