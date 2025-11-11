/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.repositories;

import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.entities.HistorialCita;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

public interface IPacienteRepository extends ICRUD<PacienteDatos, Integer>{
    Integer insert(Connection con, PacienteDatos paciente) throws SQLException;
    PacienteDatos findByDocumento(Connection con, String documento) throws SQLException;
    String findNombreCompletoById(Connection con, Integer idPaciente) throws SQLException;
    List<PacienteDatos> listAll(Connection con) throws SQLException;
    List<PacienteDTOResponse> listAllP(Connection con) throws SQLException;
}
