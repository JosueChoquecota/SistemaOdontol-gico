/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public interface ICRUD <T, K> {
    Integer insert(Connection con, T t) throws SQLException;
    Boolean update(Connection con, T t) throws SQLException;
    Boolean delete(Connection con, K id) throws SQLException;
    List<T> listAll(Connection con) throws SQLException;
    T findById(Connection con, K id) throws SQLException;
}
