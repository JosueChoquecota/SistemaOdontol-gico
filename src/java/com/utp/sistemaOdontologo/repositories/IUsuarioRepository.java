
package com.utp.sistemaOdontologo.repositories;
import com.utp.sistemaOdontologo.entities.Usuario;
import java.sql.Connection;
import java.sql.SQLException;


public interface IUsuarioRepository extends ICRUD<Usuario, Integer>{
    Integer insert(Connection con, Usuario usuario) throws SQLException;

    Boolean update(Connection con, Usuario usuario) throws SQLException; 
    
    Boolean delete(Connection con, Integer idUsuario) throws SQLException;
    
    Usuario findById(Connection con, Integer idUsuario) throws SQLException;
    Usuario findByUsername(Connection con, String username) throws SQLException;
}
