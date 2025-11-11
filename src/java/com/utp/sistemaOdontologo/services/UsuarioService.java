/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.services;

import java.sql.Connection;
import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.dao.UsuarioDAO;
import com.utp.sistemaOdontologo.dtos.LoginDTORequest;
import com.utp.sistemaOdontologo.dtos.UsuarioInfoDTO;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.enums.EstadoUsuario;
import com.utp.sistemaOdontologo.security.EncriptarClave;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class UsuarioService {
    private UsuarioDAO usuarioDAO;
    private ConnectionDataBase dbConnection;
            
    public UsuarioService() {
        usuarioDAO = new UsuarioDAO();
        dbConnection = new ConnectionDataBase();
    }

    public UsuarioInfoDTO login(LoginDTORequest request) {
        Connection con = null;
        
        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false);
            
            // 1. Buscar usuario por username
            Usuario usuarioDB = usuarioDAO.findByUsername(con, request.getUsername());
            
            if (usuarioDB == null) {
                // Usuario no encontrado
                return null; 
            }
            if (usuarioDB.getEstado() != EstadoUsuario.ACTIVO) {
                 return null;
            }

            String clavePlana = request.getContrasena();
            String hashAlmacenado = usuarioDB.getContrasena();
            
            String hashIngresado = EncriptarClave.encriptar(clavePlana); 
            
            if (hashIngresado.equals(hashAlmacenado)) { 
                
                UsuarioInfoDTO dtoResponse = new UsuarioInfoDTO();
                dtoResponse.setUsername(usuarioDB.getUsername());
                dtoResponse.setIdUsuario(usuarioDB.getIdUsuario());
                dtoResponse.setEstado(usuarioDB.getEstado().name());
                
                return dtoResponse;
            }
            
        } catch (Exception e) {
            System.err.println("Error de login o conexión: " + e.getMessage());
        } 
        return null;
    }
}
