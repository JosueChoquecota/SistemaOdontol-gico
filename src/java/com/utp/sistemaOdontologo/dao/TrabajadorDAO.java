/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;
import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.Especialidad;
import com.utp.sistemaOdontologo.entities.TipoDocumento;
import com.utp.sistemaOdontologo.entities.Trabajador;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.enums.EstadoUsuario;
import com.utp.sistemaOdontologo.entities.enums.Rol;
import com.utp.sistemaOdontologo.repositories.ITrabajadorRepository;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;


/**
 *
 * @author ASUS
 */
public class TrabajadorDAO implements ITrabajadorRepository {

    private ConnectionDataBase pstm;
    private ResultSet res;
    private ConnectionDataBase con;
    
    @Override
    public Integer insert(Connection con, Trabajador trabajador) throws SQLException {
        String SQL = "INSERT INTO Trabajadores (id_usuario, id_contacto, id_tipo_doc, id_especialidad, nombre, apellido, colegiatura, rol, fecha_registro) OUTPUT INSERTED.id_trabajador VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE());";
        Integer idGenerado = null;

        // 1. Usar Statement.RETURN_GENERATED_KEYS (o OUTPUT INSERTED.id_trabajador en SQL Server)
        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            // Asignación de las Claves Foráneas de Paciente/Contacto
            ps.setInt(1, trabajador.getUsuario().getIdUsuario());
            ps.setInt(2, trabajador.getContacto().getIdContacto());
            ps.setInt(3, trabajador.getTipoDocumento().getIdTipoDocumento());

            // 2. Manejo de Especialidad NULL (Posición 4)
            if (trabajador.getEspecialidad() != null && trabajador.getEspecialidad().getIdEspecialidad() != null) {
                ps.setInt(4, trabajador.getEspecialidad().getIdEspecialidad());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            // 3. Campos Directos y Rol (Posiciones 5, 6, 7, 8)
            ps.setString(5, trabajador.getNombre());
            ps.setString(6, trabajador.getApellido());
            ps.setString(7, trabajador.getColegiatura());
            ps.setString(8, trabajador.getRol().name()); // Usar el nombre del ENUM

            // 4. Ejecutar y obtener ID (executeQuery porque usamos OUTPUT INSERTED)
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idGenerado = rs.getInt(1); // Captura el ID de la columna OUTPUT
                }
            }
        }

        // 5. Devolver el ID generado para cumplir con el contrato ICRUD
        if (idGenerado == null) {
            throw new SQLException("Error DAO: Fallo al obtener ID de Trabajador generado.");
        }
        return idGenerado;
    }

    @Override
    public Boolean update(Connection con, Trabajador trabajador) throws SQLException {
    throw new UnsupportedOperationException("Método UPDATE aún no implementado.");
        }

    @Override
        public Boolean delete(Connection con, Integer idTrabajador) throws SQLException {
    String SQL = "DELETE FROM Trabajadores WHERE id_trabajador = ?;";
            try (PreparedStatement ps = con.prepareStatement(SQL)) {
                ps.setInt(1, idTrabajador);
                return ps.executeUpdate() > 0;
            }
        }

    @Override
        public int[] findFksById(Connection con, Integer idTrabajador) throws SQLException {
            String sql = "SELECT id_contacto, id_usuario FROM Trabajadores WHERE id_trabajador = ?";
            int[] fks = new int[2]; 
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idTrabajador);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        fks[0] = rs.getInt("id_contacto");
                        fks[1] = rs.getInt("id_usuario");
                    }
                }
            }
            return fks; // Devuelve {0, 0} si no lo encuentra o los IDs
        }

    @Override
    public List<Trabajador> findAll(Connection con) throws SQLException {
    
        // Consulta SQL con JOINs para obtener Trabajador, Contacto, Usuario, Especialidad, etc.
        String sql = "SELECT T.*, C.*, U.*, E.nombre AS especialidad_nombre " +
                     "FROM Trabajadores T " +
                     "INNER JOIN Usuarios U ON T.id_usuario = U.id_usuario " +
                     "INNER JOIN Contactos C ON T.id_contacto = C.id_contacto " +
                     "INNER JOIN TiposDocumentos TD ON T.id_tipo_doc = TD.id_tipo_doc " +
                     "LEFT JOIN Especialidades E ON T.id_especialidad = E.id_especialidad"; 

        List<Trabajador> lista = new ArrayList<>();

        // Usar try-with-resources para manejar el PreparedStatement y ResultSet
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Trabajador t = new Trabajador();
                t.setIdTrabajador(rs.getInt("id_trabajador"));
                t.setNombre(rs.getString("nombre")); // <-- Esta línea falta o el nombre de la columna es incorrecto
                t.setApellido(rs.getString("apellido"));
                // ... (otros mapeos directos) ...

                // 2. CONSTRUCCIÓN DE ENTIDAD ANIDADA: USUARIO
                // Se asume que la columna id_usuario está listada en el SELECT
                if (rs.getObject("id_usuario") != null) { 
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setUsername(rs.getString("username")); // <-- ¡VERIFICA ESTE NOMBRE DE COLUMNA!
                    usuario.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
                    // 

                    t.setUsuario(usuario); // <-- ESTA ASIGNACIÓN DEBE SUCEDER
                }

                // 3. CONSTRUCCIÓN DE ENTIDAD ANIDADA: CONTACTO
                if (rs.getObject("id_contacto") != null) {
                    Contacto contacto = new Contacto();
                    contacto.setIdContacto(rs.getInt("id_contacto"));
                    contacto.setCorreo(rs.getString("correo")); // <-- ¡VERIFICA ESTE NOMBRE DE COLUMNA!
                    // ... otros campos del Contacto

                    t.setContacto(contacto); // <-- ESTA ASIGNACIÓN DEBE SUCEDER
                }
                String rolDB = rs.getString("rol"); // El nombre de la columna en la tabla Trabajadores es 'rol'

                if (rolDB != null) {
                    // La conversión a ENUM requiere que el String coincida. 
                    // Por seguridad, se suele convertir a mayúsculas antes de usar valueOf().
                    t.setRol(Rol.valueOf(rolDB.toUpperCase()));
                } else {
                    // Manejo de error si el rol fuera NULL en la DB (lo cual no debería pasar)
                    // Pero si pasa, puedes lanzar una excepción o asignar un valor por defecto.
                }
                // ... Y asignar las entidades relacionadas (Usuario, Contacto, etc.)
                lista.add(t); 
            }
        }

        return lista;
    }
    
    @Override 
    public Trabajador findById(Connection con, Integer idTrabajador) throws SQLException { 
        // Consulta SQL con JOINs (similar a findAll, pero con WHERE)
    String sql = "SELECT T.*, C.*, U.*, TD.nombre AS tipo_doc_nombre, E.nombre AS especialidad_nombre " +
                 "FROM Trabajadores T " +
                 "INNER JOIN Usuarios U ON T.id_usuario = U.id_usuario " +
                 "INNER JOIN Contactos C ON T.id_contacto = C.id_contacto " +
                 "INNER JOIN TiposDocumentos TD ON T.id_tipo_doc = TD.id_tipo_doc " +
                 "LEFT JOIN Especialidades E ON T.id_especialidad = E.id_especialidad " +
                 "WHERE T.id_trabajador = ?"; 
                 
    Trabajador trabajador = null;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idTrabajador);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
        trabajador = new Trabajador();
        Usuario usuario = new Usuario();
        // 1. Mapeo de campos directos del Trabajador
        trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
        trabajador.setNombre(rs.getString("nombre"));
        trabajador.setApellido(rs.getString("apellido"));
        trabajador.setColegiatura(rs.getString("colegiatura"));
        trabajador.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate()); // Asumiendo conversión a LocalDate
        trabajador.setRol(Rol.valueOf(rs.getString("rol").toUpperCase()));
        trabajador.setUsuario(usuario);
        // Mapeo del Tipo de Documento
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setIdTipoDocumento(rs.getInt("id_tipo_doc"));
        tipoDoc.setNombre(rs.getString("tipo_doc_nombre"));
        trabajador.setTipoDocumento(tipoDoc);

        // Mapeo de Especialidad (Manejo de NULL)
        if (rs.getObject("id_especialidad") != null) {
            Especialidad especialidad = new Especialidad();
            especialidad.setIdEspecialidad(rs.getInt("id_especialidad"));
            especialidad.setNombre(rs.getString("especialidad_nombre"));
            trabajador.setEspecialidad(especialidad);
        }
        
        // -------------------------------------------------------------
        // 2. Mapeo de Usuario (Para UsuarioInfoDTO)
        // -------------------------------------------------------------
        if (rs.getObject("id_usuario") != null) { 
            
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            // **CORRECCIÓN DE CONVENCIÓN (asumiendo campo en DB y entidad):**
            usuario.setUsername(rs.getString("username")); 
            usuario.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
            // Puedes mapear el ID de empresa si es necesario para el DTO
            
            trabajador.setUsuario(usuario); 
        }

        // -------------------------------------------------------------
        // 3. Mapeo de Contacto (Para ContactoInfoDTO)
        // -------------------------------------------------------------
        if (rs.getObject("id_contacto") != null) {
            Contacto contacto = new Contacto();
            contacto.setIdContacto(rs.getInt("id_contacto"));
            
            // ************ ESTOS CAMPOS DEBEN SER MAPEADOS ************
            contacto.setCorreo(rs.getString("correo")); 
            contacto.setTelefono(rs.getString("telefono")); 
            contacto.setDireccion(rs.getString("direccion"));
            contacto.setTipoContacto(rs.getString("tipo_contacto"));
            
            trabajador.setContacto(contacto); 
        }
    }
    }
    
    return trabajador;
    }
    }

    @Override
    public String findNombreById(Connection con, Integer idTrabajador) throws SQLException {
    String SQL = "SELECT nombre, apellido FROM Trabajadores WHERE id_trabajador = ?";
        String nombreCompleto = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idTrabajador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                }
            }
        }
        return nombreCompleto;
    }

    @Override
    public List<Trabajador> listAll(Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
