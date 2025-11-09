package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.Contacto;

import java.sql.Connection;
import java.sql.SQLException;

public interface IContactoRepository {
    Integer insert(Connection con, Contacto contacto) throws SQLException;
    Boolean update(Connection con, Contacto contacto) throws SQLException;
    
    // Eliminación: Recibe la conexión y el ID para la eliminación transaccional.
    Boolean delete(Connection con, Integer idContacto) throws SQLException;
    
    // Búsqueda (útil para el UPDATE si el Service necesita datos originales)
    Contacto findById(Connection con, Integer idContacto) throws SQLException;
}
