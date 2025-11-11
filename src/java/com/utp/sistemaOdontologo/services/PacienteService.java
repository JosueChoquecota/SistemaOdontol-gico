/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.services;

import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.dao.ContactoDAO;
import com.utp.sistemaOdontologo.dao.PacienteDAO;
import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.PacienteDTORequest;
import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.entities.TipoDocumento;
import com.utp.sistemaOdontologo.mappers.CitaMapper;
import com.utp.sistemaOdontologo.mappers.PacienteMapper;
import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class PacienteService {
        private ConnectionDataBase dbConnection;
        private PacienteDAO pacienteDAO;
        private ContactoDAO contactoDAO;

    public PacienteService() {
        this.dbConnection = new ConnectionDataBase();
        this.pacienteDAO = new PacienteDAO();
        this.contactoDAO = new ContactoDAO();
    }
    public Integer insert(PacienteDTORequest request) {
        Connection con = null;
        Integer idPacienteGenerado = null;
        
        // 1. Lógica de negocio (pre-validación, ej., verificar si el DNI es único)
        // if (pacienteDAO.findByDocumento(request.getDocumento()) != null) { return null; } 

        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); // INICIA LA TRANSACCIÓN

            // PASO 1: INSERTAR CONTACTO
            Contacto contacto = PacienteMapper.toContactoEntity(request); // Necesitas este mapper
            Integer idContactoGenerado = contactoDAO.insert(con, contacto); 
            
            if (idContactoGenerado == null) {
                throw new Exception("Fallo al obtener ID de Contacto.");
            }

            // PASO 2: INSERTAR PACIENTE
            PacienteDatos paciente = PacienteMapper.toPacienteEntity(request, idContactoGenerado);
            idPacienteGenerado = pacienteDAO.insert(con, paciente); 
            
            if (idPacienteGenerado == null) {
                throw new Exception("Fallo al obtener ID de Paciente.");
            }
            
            con.commit(); // CONFIRMA TRANSACCIÓN

        } catch (Exception e) {
            System.err.println("❌ Error transaccional al crear paciente. Detalle: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* Log rollback */ }
            return null; // Fallo total

        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
        }
        
        return idPacienteGenerado;
    }
    public Integer crearNuevoPacienteTransaccional(Connection con, CitaDTORequest request) throws Exception {

        if (request.getNombresPaciente() == null || request.getNombresPaciente().isEmpty()) {
            request.setNombresPaciente("PACIENTE_TEMP");
        }
        if (request.getApellidosPaciente() == null || request.getApellidosPaciente().isEmpty()) {
            request.setApellidosPaciente(request.getDocumento());
        }

        // Asignar FKs y datos obligatorios para Contacto/Paciente
        request.setIdTipoDocumento(1);      // Asumir que 1 es DNI/Cédula
        request.setTipoContacto("EMAIL");   // Asignar un valor válido (EMAIL o PHONE)

        // Si la tabla Contactos no permite NULL en dirección:
        if (request.getDireccion() == null || request.getDireccion().isEmpty()) {
            request.setDireccion("Desconocida");
        }
        // Si la tabla Contactos no permite NULL en correo:
        if (request.getCorreo() == null || request.getCorreo().isEmpty()) {
            request.setCorreo("temporal@utp.com");
        }

        // A. Contacto
        Contacto contacto = CitaMapper.toContactoEntity(request);
        Integer idContacto = contactoDAO.insert(con, contacto);
        if (idContacto == null) { throw new Exception("Fallo la inserción de Contacto."); }

        // B. Paciente
        PacienteDatos paciente = CitaMapper.toPacienteEntity(request, idContacto);
        Integer idPaciente = pacienteDAO.insert(con, paciente);

        if (idPaciente == null) { throw new Exception("Fallo la inserción de PacienteDatos."); }

        return idPaciente;
    }
    
    public List<PacienteDTOResponse> listAllPacientes() {
            Connection con = null;
            try {
                con = dbConnection.getConnection();
                // Llama al DAO con el JOIN implementado arriba
                return pacienteDAO.listAllP(con); 
            } catch (SQLException e) {
                System.err.println("Error al listar pacientes: " + e.getMessage());
                return new ArrayList<>(); // Devuelve lista vacía en caso de error
            } finally {
                try { if (con != null) con.close(); } catch (SQLException ex) { /* log */ }
            }
        }
    public Boolean updatePaciente(PacienteDTORequest request) {
       Connection con = null;
    try {
        con = dbConnection.getConnection();
        con.setAutoCommit(false); 

        // 1. OBTENER ENTIDADES ORIGINALES
        PacienteDatos pacienteExistente = pacienteDAO.findById(con, request.getIdPaciente());
        if (pacienteExistente == null) {
            throw new Exception("Paciente ID " + request.getIdPaciente() + " no existe para actualizar.");
        }
        Contacto contactoExistente = pacienteExistente.getContacto(); 
        
        if (contactoExistente == null || contactoExistente.getIdContacto() == null) {
             throw new Exception("Error de integridad: El paciente no tiene un registro de Contacto asociado.");
        }
        
        // 2. FUSIÓN DE DATOS (Aplicar los cambios del DTO a las Entidades Existentes)
        
        // 2a. Fusión de Contacto (CORRECTO)
        contactoExistente.setCorreo(request.getCorreo());
        contactoExistente.setTelefono(request.getTelefono());
        contactoExistente.setDireccion(request.getDireccion());
        contactoDAO.update(con, contactoExistente); // Ejecutar Update de Contacto

        // 2b. Fusión de PacientesDatos
        pacienteExistente.setNombres(request.getNombresPaciente());
        pacienteExistente.setApellidos(request.getApellidosPaciente());
        pacienteExistente.setDocumento(request.getDocumento());
        pacienteExistente.setFechaNacimiento(request.getFechaNacimiento());
        pacienteExistente.setSexo(request.getSexo());
        
        // === CORRECCIÓN DEL ERROR 'NullPointerException' ===
        // Verificar si el objeto TipoDocumento fue inicializado por el findById
        if (pacienteExistente.getTipoDocumento() == null) {
            // Si es null, lo inicializamos con un objeto vacío (Stub)
            pacienteExistente.setTipoDocumento(new TipoDocumento());
        }
        
        // Ahora es seguro llamar a getTipoDocumento() para actualizar el FK
        pacienteExistente.getTipoDocumento().setIdTipoDocumento(request.getIdTipoDocumento()); 
        // =================================================

        // 3. EJECUTAR UPDATE TRANSACCIONAL
        pacienteDAO.update(con, pacienteExistente);

        con.commit();
        return true;
        
    } catch (Exception e) {
        System.err.println("❌ ERROR FATAL al actualizar Paciente. Detalle: " + e.getMessage());
        try { if (con != null) con.rollback(); } catch (SQLException ex) { System.err.println("Rollback Error."); }
        return false;
    } finally {
        try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
    }
    }
    
    public PacienteDatos findById(Integer idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            return null;
        }

        Connection con = null;
        try {
            con = dbConnection.getConnection();
            
            // 1. Buscar la entidad PacienteDatos principal
            PacienteDatos paciente = pacienteDAO.findById(con, idPaciente);
            
            if (paciente == null) {
                return null;
            }
            
            // 2. Cargar la entidad Contacto asociada usando el FK del Paciente
            // Esto es crucial para un UPDATE o lectura completa.
            Integer idContacto = paciente.getContacto().getIdContacto();
            if (idContacto != null) {
                Contacto contacto = contactoDAO.findById(con, idContacto);
                
                // 3. Asignar el objeto Contacto completo de vuelta a la entidad Paciente
                if (contacto != null) {
                    paciente.setContacto(contacto);
                }
            }
            
            return paciente;

        } catch (Exception e) {
            System.err.println("❌ Error al buscar paciente ID " + idPaciente + ": " + e.getMessage());
            return null;
        } finally {
            try { 
                if (con != null) con.close(); 
            } catch (SQLException ex) { 
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }

    public Boolean deletePaciente(Integer idPaciente) {
    Connection con = null;
    try {
        con = dbConnection.getConnection();
        con.setAutoCommit(false); 

        // 1. OBTENER ENTIDAD EXISTENTE para obtener el ID de Contacto
        PacienteDatos pacienteAEliminar = pacienteDAO.findById(con, idPaciente);
        if (pacienteAEliminar == null) {
            throw new Exception("Paciente ID " + idPaciente + " no existe.");
        }
        
        Integer idContactoAEliminar = pacienteAEliminar.getContacto().getIdContacto();
        if (idContactoAEliminar == null) {
             throw new Exception("Error de integridad: El paciente no tiene un Contacto válido asociado.");
        }
        
        // 2. ELIMINACIÓN EN CASCADA (Orden Importante para FKs)
        
        // A. Eliminar el registro HIJO (PacientesDatos)
        // Se debe eliminar primero PacientesDatos, ya que este tiene la FK a Contactos.
        pacienteDAO.delete(con, pacienteAEliminar); 
        
        // B. Eliminar el registro PADRE (Contactos)
        Contacto contactoAEliminar = new Contacto();
        contactoAEliminar.setIdContacto(idContactoAEliminar);
        contactoDAO.delete(con, contactoAEliminar);

        con.commit();
        return true;
        
    } catch (Exception e) {
        System.err.println("❌ ERROR FATAL al eliminar Paciente. Detalle: " + e.getMessage());
        try { if (con != null) con.rollback(); } catch (SQLException ex) { System.err.println("Rollback Error."); }
        return false;
    } finally {
        try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
    }
}
}
