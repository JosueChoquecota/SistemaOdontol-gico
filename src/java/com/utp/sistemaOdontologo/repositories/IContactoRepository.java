package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.entities.Cita;
import com.utp.sistemaOdontologo.entities.Contacto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IContactoRepository extends ICRUD<Contacto, Integer> {
    Integer insert(Connection con, Contacto contacto) throws SQLException;
    Boolean update(Connection con, Contacto contacto) throws SQLException;
    Boolean delete(Connection con, Integer idContacto) throws SQLException;   
    Contacto findById(Connection con, Integer idContacto) throws SQLException;
    List<Contacto> listAll(Connection con) throws SQLException;
}
