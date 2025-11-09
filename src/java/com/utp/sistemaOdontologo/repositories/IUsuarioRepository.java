
package com.utp.sistemaOdontologo.repositories;
import com.utp.sistemaOdontologo.entities.Usuario;
import java.sql.Connection;
import java.sql.SQLException;


public interface IUsuarioRepository {
    Integer insert(Connection con, Usuario usuario) throws SQLException;
    // Actualización: Recibe la conexión y la entidad con el ID a actualizar.
    // Maneja la complejidad de actualizar solo el username/estado o la contraseña.
    Boolean update(Connection con, Usuario usuario) throws SQLException; 
    
    // Eliminación: Recibe la conexión y el ID para la eliminación transaccional.
    Boolean delete(Connection con, Integer idUsuario) throws SQLException;
    
    // Búsqueda (útil para login y búsquedas transaccionales)
    Usuario findById(Connection con, Integer idUsuario) throws SQLException;
    
    Usuario findByUsername(Connection con, String username) throws SQLException;
}
