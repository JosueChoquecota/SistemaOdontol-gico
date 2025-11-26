/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.services;

import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.dao.CitaDAO;
import com.utp.sistemaOdontologo.dao.ContactoDAO;
import com.utp.sistemaOdontologo.dao.HistoriaCitaDAO;
import com.utp.sistemaOdontologo.dao.HorarioOdontologoDAO;
import com.utp.sistemaOdontologo.dao.PagoDAO;
import com.utp.sistemaOdontologo.dao.TrabajadorDAO;
import com.utp.sistemaOdontologo.dao.UsuarioDAO;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
import com.utp.sistemaOdontologo.entities.Cita;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.Empresa;
import com.utp.sistemaOdontologo.entities.Trabajador;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.enums.EstadoUsuario;
import com.utp.sistemaOdontologo.mappers.TrabajadorMapper;
import com.utp.sistemaOdontologo.security.EncriptarClave;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ASUS
 */
public class TrabajadorService {
   
    private final ConnectionDataBase dbConnection;
    private final TrabajadorDAO trabajadorDAO;
    private final ContactoDAO contactoDAO;
    private final UsuarioDAO usuarioDAO;
    
    // NUEVOS DAOS PARA LA ELIMINACIÓN EN CASCADA
    private final HorarioOdontologoDAO horarioOdontologoDAO;
    private final CitaDAO citaDAO;
    private final HistoriaCitaDAO historiaCitaDAO;
    private final PagoDAO pagoDAO;

    public TrabajadorService() {
        this.dbConnection = new ConnectionDataBase();
        this.trabajadorDAO = new TrabajadorDAO();
        this.contactoDAO = new ContactoDAO();
        this.usuarioDAO = new UsuarioDAO();
        
        // ¡¡¡AQUÍ ESTABA EL ERROR!!! TE FALTABAN ESTAS LÍNEAS:
        this.horarioOdontologoDAO = new HorarioOdontologoDAO();
        this.citaDAO = new CitaDAO();
        this.historiaCitaDAO = new HistoriaCitaDAO();
        this.pagoDAO = new PagoDAO();
    }
    
    public Integer insert(TrabajadorDTORequest request) {
        
        Connection con = null;
        Integer idContactoGenerado = null;
        Integer idUsuarioGenerado = null;
        Integer idTrabajadorGenerado = null; // Variable para capturar el ID final
        
        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); // INICIA LA TRANSACCIÓN
            
            // 1. Mapear e insertar Contacto (Devuelve Integer)
            Contacto contacto = TrabajadorMapper.toContactoEntity(request);
            idContactoGenerado = contactoDAO.insert(con, contacto); 

            // 2. Mapear, hashear e insertar Usuario (Devuelve Integer)
            Usuario usuario = TrabajadorMapper.toUsuarioEntity(request);
            usuario.setEstado(EstadoUsuario.ACTIVO);
            
            Empresa empresaStub = new Empresa();
            empresaStub.setIdEmpresa(1);
            usuario.setEmpresa(empresaStub);
            
            String clavePlana = usuario.getContrasena();
            String claveHasheada = EncriptarClave.encriptar(clavePlana); 
            usuario.setContrasena(claveHasheada);
            
            idUsuarioGenerado = usuarioDAO.insert(con, usuario); // DAO inserta y devuelve ID
            
            // 3. Mapear e insertar Trabajador
            Trabajador trabajador = TrabajadorMapper.toTrabajadorEntity(request, idContactoGenerado, idUsuarioGenerado);
            
            // Cambiamos a llamar un método que devuelve el ID, o lo asignamos si el DAO devuelve Boolean
            // Asumiendo que tu trabajadorDAO tiene el insert que devuelve el ID:
            idTrabajadorGenerado = trabajadorDAO.insert(con, trabajador); // Asumo que este método devuelve el ID generado.
            
            // 4. Finalizar Transacción
            if (idTrabajadorGenerado != null && idTrabajadorGenerado > 0) {
                con.commit();
            } else {
                con.rollback(); 
                return null; // Falló la inserción del trabajador
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error transaccional al crear trabajador: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { System.err.println("Rollback Error."); }
            return null; // Fallo total
            
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
        }
        
        // Retorna el ID generado para cumplir con el contrato ICRUD (Integer)
        return idTrabajadorGenerado; 
    }
    
    // MÉTODO DE ELIMINACIÓN CORREGIDO (Ya no dará NullPointer)
    public boolean eliminarTrabajador(Integer idTrabajador) {
        Connection con = null;
        boolean exito = false;

        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); 

            // 1. ELIMINAR HORARIOS
            horarioOdontologoDAO.deleteByTrabajadorId(con, idTrabajador);

            // 2. ELIMINAR CITAS ASOCIADAS (Ahora citaDAO ya no es null)
            List<Cita> citasDelDoctor = citaDAO.findByIdTrabajador(con, idTrabajador);
            
            if (citasDelDoctor != null) {
                for (Cita c : citasDelDoctor) {
                    historiaCitaDAO.deleteByCitaId(con, c.getIdCita());
                    pagoDAO.deleteByCitaId(con, c.getIdCita());
                    citaDAO.delete(con, c.getIdCita());
                }
            }

            // 3. ELIMINAR TRABAJADOR
            trabajadorDAO.delete(con, idTrabajador);
            
            con.commit(); 
            exito = true;

        } catch (Exception e) {
            System.err.println("Error transaccional DELETE: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { }
        }

        return exito;
    }

    private final TrabajadorMapper mapper = new TrabajadorMapper(); // Instancia del Mapper para usar sus métodos

    public TrabajadorDTOResponse findById(Integer idTrabajador) {
        Connection con = null;
        try {
            // 1. CONEXIÓN (se cierra automáticamente al salir del try-with-resources si lo usas)
            con = dbConnection.getConnection();

            // 2. BÚSQUEDA: El DAO usa JOINs para obtener la Entidad completa
            Trabajador entidad = trabajadorDAO.findById(con, idTrabajador); 

            // 3. MAPEO: El Servicio convierte la Entidad a DTO de Respuesta
            if (entidad != null) {
                return TrabajadorMapper.toResponseDTO(entidad);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar trabajador por ID: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* log close error */ }
        }
        return null;
    }

    public List<TrabajadorDTOResponse> findAll() {
        Connection con = null;
        try {
            con = dbConnection.getConnection();

            // 1. LISTADO: El DAO usa JOINs para obtener todas las Entidades
            List<Trabajador> entidades = trabajadorDAO.findAll(con);

            // 2. MAPEO: El Servicio convierte la Lista de Entidades a Lista de DTOs
            if (entidades != null && !entidades.isEmpty()) {
                return TrabajadorMapper.toListResponseDTO(entidades);
            }
        } catch (Exception e) {
            System.err.println("Error al listar trabajadores: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* log close error */ }
        }
        return new ArrayList<>(); // Devuelve lista vacía en caso de error o no encontrar nada
    }
    
    // Dentro de la clase TrabajadorService

    public Boolean update(TrabajadorDTORequest request) {
       Connection con = null;
        Boolean exito = false;
    
    try {
        // 1. OBTENER CONEXIÓN E INICIAR TRANSACCIÓN
        con = dbConnection.getConnection();
        con.setAutoCommit(false); 

        // 2. OBTENER FKs ORIGINALES (ID de Usuario y Contacto)
        // Necesitas el ID del Trabajador (request.getIdTrabajador()) para buscar sus FKs
        int[] fks = trabajadorDAO.findFksById(con, request.getIdTrabajador());
        int idContacto = fks[0];
        int idUsuario = fks[1];
        
        // ***************************************************************
        // 3. CARGAR ENTIDADES EXISTENTES (CLAVE PARA SOLUCIONAR EL ERROR)
        // ***************************************************************
        // Carga la entidad Usuario COMPLETA desde la BD (incluyendo su ESTADO)
        Usuario usuarioExistente = usuarioDAO.findById(con, idUsuario);
        Contacto contactoExistente = contactoDAO.findById(con, idContacto);

        if (usuarioExistente == null || contactoExistente == null) {
            throw new SQLException("No se encontraron registros de Contacto/Usuario asociados al ID: " + request.getIdTrabajador());
        }

        // 4. PREPARAR ENTIDADES PARA UPDATE (MERGE)
        
        // A. Preparar Contacto: Actualizar los campos que vienen del DTO
        contactoExistente.setCorreo(request.getCorreo());
        contactoExistente.setTelefono(request.getTelefono());
        contactoExistente.setDireccion(request.getDireccion());
        // El ID de Contacto ya está en la entidad existente.

        // B. Preparar Usuario: Actualizar Username y Contraseña. MANTENER el Estado.
        usuarioExistente.setUsuario(request.getUsuario());
        // Encriptar y actualizar la contraseña si el campo no está vacío
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            // Asumiendo que tienes una clase para hashear la contraseña
            // usuarioExistente.setContrasena(HashingUtil.hashPassword(request.getContrasena()));
        }
        // El Estado (ACTIVO o INACTIVO) se mantiene del objeto 'usuarioExistente'
        
        // C. Preparar Trabajador: Actualizar los campos que vienen del DTO
        // Similar a Contacto y Usuario, puedes cargar el Trabajador existente
        // y actualizar sus campos (nombre, apellido, colegiatura, etc.)

        // 5. EJECUTAR UPDATES
        contactoDAO.update(con, contactoExistente);
        usuarioDAO.update(con, usuarioExistente);
        // trabajadorDAO.update(con, trabajadorExistente); // Asume que necesitas actualizar Trabajadores también

        // 6. CONFIRMAR TRANSACCIÓN
        con.commit();
        exito = true;
        
    } catch (Exception e) {
        System.err.println("Error transaccional UPDATE: No se pudo actualizar el trabajador. Mensaje: " + e.getMessage());
        try { 
            if (con != null) { con.rollback(); }
        } catch (SQLException ex) { 
            System.err.println("Error durante rollback: " + ex.getMessage());
        }
        exito = false;
    } finally {
        try { 
            if (con != null) { con.close(); }
        } catch (SQLException ex) { 
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
    return exito;
}
}
