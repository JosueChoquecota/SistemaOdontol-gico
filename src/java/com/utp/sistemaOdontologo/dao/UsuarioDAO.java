/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;
import com.utp.sistemaOdontologo.entities.Empresa;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.enums.EstadoUsuario;
import com.utp.sistemaOdontologo.repositories.IUsuarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ASUS
 */
public class UsuarioDAO implements IUsuarioRepository{
    
    @Override
    public Integer insert(Connection con, Usuario usuario) throws SQLException {
        // La entidad Usuario ya tiene el HASH, Estado y idEmpresa asignados por el Service
        String SQL = "INSERT INTO Usuarios (id_empresa, username, contraseña, estado) OUTPUT INSERTED.id_usuario VALUES (?, ?, ?, ?);";
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            Integer idEmpresaFK = null;
            if (usuario.getEmpresa() != null) {
                idEmpresaFK = usuario.getEmpresa().getIdEmpresa();
            } else {
                // En un sistema real, esto DEBERÍA fallar, pero si usamos la empresa 1 por defecto,
                // podemos asignarlo aquí si el Service falló al hacerlo.
                idEmpresaFK = 1; 
            }
            ps.setInt(1, idEmpresaFK);
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getContrasena()); // Clave ya hasheada
            ps.setString(4, usuario.getEstado().name());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Fallo al obtener ID de Usuario generado.");
    }

    @Override
    public Boolean update(Connection con, Usuario usuario) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE Usuarios SET username = ?, estado = ?");
        boolean actualizarContrasena = (usuario.getContrasena() != null);
        int paramIndex = 1;

        if (actualizarContrasena) {
            // Si hay una clave nueva (ya hasheada), añade la columna al UPDATE
            sql.append(", contraseña = ?");
        }

        sql.append(" WHERE id_usuario = ?");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // 1. Asignar username y estado
            ps.setString(paramIndex++, usuario.getUsername());
            ps.setString(paramIndex++, usuario.getEstado().name());

            // 2. Asignar contraseña (Condicional)
            if (actualizarContrasena) {
                ps.setString(paramIndex++, usuario.getContrasena()); // El hash
            }

            // 3. Asignar ID (Final)
            ps.setInt(paramIndex, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Boolean delete(Connection con, Integer idUsuario) throws SQLException {
        String SQL = "DELETE FROM Usuarios WHERE id_usuario = ?;";
                try (PreparedStatement ps = con.prepareStatement(SQL)) {
                    ps.setInt(1, idUsuario);
                    return ps.executeUpdate() > 0;
                }   
    }

    @Override
    public Usuario findById(Connection con, Integer idUsuario) throws SQLException {
        String SQL = "SELECT id_usuario, id_empresa, username, estado FROM Usuarios WHERE id_usuario = ?";
    Usuario usuario = null;

    try (PreparedStatement ps = con.prepareStatement(SQL)) {
        ps.setInt(1, idUsuario);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = new Usuario();
                
                // Mapeo de campos simples
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
                
                // Mapeo del FK Empresa (Stub)
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(rs.getInt("id_empresa"));
                usuario.setEmpresa(empresa);
                
                // NOTA: La contraseña (contrasena) NO se recupera aquí por seguridad
            }
        }
    }
    return usuario;
    }

    @Override
    public Usuario findByUsername(Connection con, String username) throws SQLException {
// Es crucial seleccionar la columna 'contraseña' para la verificación
    String SQL = "SELECT id_usuario, id_empresa, username, contraseña, estado FROM Usuarios WHERE username = ?";
    Usuario usuario = null;

    try (PreparedStatement ps = con.prepareStatement(SQL)) {
        ps.setString(1, username);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = new Usuario();
                // Mapeo completo (¡incluyendo la contraseña hasheada!)
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setContrasena(rs.getString("contraseña")); // CLAVE CRUCIAL
                usuario.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
                // ... Mapear Empresa stub ...
            }
        }
    }
    return usuario;   
    }
    
    
}
