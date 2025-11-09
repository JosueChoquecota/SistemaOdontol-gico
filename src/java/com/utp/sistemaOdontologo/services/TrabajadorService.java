/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.services;

import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.dao.ContactoDAO;
import com.utp.sistemaOdontologo.dao.TrabajadorDAO;
import com.utp.sistemaOdontologo.dao.UsuarioDAO;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
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
   
    private ContactoDAO contactoDAO;
    private UsuarioDAO usuarioDAO;
    private TrabajadorDAO trabajadorDAO;
    private ConnectionDataBase dbConnection;
    private Empresa empresa;
    
    public TrabajadorService() {
        // Inicializamos los DAOs y el manejador de conexión
        contactoDAO = new ContactoDAO();
        usuarioDAO = new UsuarioDAO();
        trabajadorDAO = new TrabajadorDAO();
        dbConnection = new ConnectionDataBase();
    }
    
    public Boolean insert(TrabajadorDTORequest request) {
        
        Connection con = null;
        Integer idContactoGenerado = null;
        Integer idUsuarioGenerado = null;
        Boolean exito = false;
        
        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); // <--- INICIA LA TRANSACCIÓN
            
            // 1. Mapear y gestionar Contacto
            Contacto contacto = TrabajadorMapper.toContactoEntity(request);
            // El DAO ahora recibe la CONEXIÓN
            idContactoGenerado = contactoDAO.insert(con, contacto); 

            // 2. Mapear, hashear y gestionar Usuario (Lógica de Negocio/Seguridad)
            Usuario usuario = TrabajadorMapper.toUsuarioEntity(request);
            
            // Lógica de Servicio: Asignación de estado y hash
            usuario.setEstado(EstadoUsuario.ACTIVO);
            
            Empresa empresaStub = new Empresa();
            empresaStub.setIdEmpresa(1); 
            usuario.setEmpresa(empresaStub); // <--- ESTO DEBE ASIGNAR EL OBJETO        
            
            // 2.2. ENCRIPTACIÓN REALIZADA CON TU CLASE
            String clavePlana = usuario.getContrasena();
            String claveHasheada = EncriptarClave.encriptar(clavePlana); // <--- USO DE TU MÉTODO
            usuario.setContrasena(claveHasheada); // Asigna el hash a la entidad
            
            // El DAO ahora recibe la CONEXIÓN
            idUsuarioGenerado = usuarioDAO.insert(con, usuario); 
            
            // 3. Mapear y gestionar Trabajador
            Trabajador trabajador = TrabajadorMapper.toTrabajadorEntity(request, idContactoGenerado, idUsuarioGenerado);
            
            // El DAO ahora recibe la CONEXIÓN
            exito = trabajadorDAO.insert(con, trabajador); 
            
            // Si llegamos aquí sin excepción, hacemos COMMIT
            if (exito) {
                con.commit();
            } else {
                con.rollback(); // Si el insertTrabajador falló por alguna razón
            }
            
        } catch (Exception e) {
            System.err.println("Error transaccional al crear trabajador: " + e.getMessage());
            // Si hay excepción (e.g., error de SQL, hashing), hacemos ROLLBACK
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* log error */ }
            return false;
            
        } finally {
            // Siempre cerramos la conexión
            try { if (con != null) con.close(); } catch (SQLException ex) { /* log error */ }
        }
        
        return exito;
    }
    
    public Boolean delete(Integer idTrabajador) {
        Connection con = null;
        Boolean exito = false;

        try {
            // 1. OBTENER CONEXIÓN E INICIAR TRANSACCIÓN
            con = dbConnection.getConnection(); // Asumimos acceso a ConnectionDataBase
            con.setAutoCommit(false); 

            // 2. OBTENER FKS (idContacto, idUsuario)
            int[] fks = trabajadorDAO.findFksById(con, idTrabajador);
            int idContacto = fks[0];
            int idUsuario = fks[1];

            if (idContacto == 0 || idUsuario == 0) {
                // Si no encuentra las FKs, la eliminación de registros principales no puede continuar.
                throw new SQLException("No se encontraron registros de Contacto/Usuario asociados.");
            }

            // 3. ELIMINAR EN ORDEN INVERSO (Hijo a Padre): Trabajador -> Usuario -> Contacto
            trabajadorDAO.delete(con, idTrabajador); // Elimina la entrada principal
            usuarioDAO.delete(con, idUsuario);       // Elimina el acceso
            contactoDAO.delete(con, idContacto);     // Elimina los datos de contacto

            // 4. CONFIRMAR TRANSACCIÓN
            con.commit();
            exito = true;

        } catch (Exception e) {
            System.err.println("Error transaccional DELETE: No se pudo eliminar el trabajador. " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* Log rollback error */ }
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Log close error */ }
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
        usuarioExistente.setUsername(request.getUsername());
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
