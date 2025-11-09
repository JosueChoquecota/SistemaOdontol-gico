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
            
            // 2. Verificar estado (opcional, pero buena práctica)
            if (usuarioDB.getEstado() != EstadoUsuario.ACTIVO) {
                 // Bloqueado o inactivo
                 return null;
            }

            // 3. VERIFICACIÓN DE CONTRASEÑA (LÓGICA DE SEGURIDAD)
            String clavePlana = request.getContrasena();
            String hashAlmacenado = usuarioDB.getContrasena();
            
            // En este punto, tu EncriptarClave DEBE tener un método 'comparar' o 'check'
            // Ya que solo tienes el método 'encriptar', compararemos los hashes:
            
            // Genera el hash de la clave plana ingresada
            String hashIngresado = EncriptarClave.encriptar(clavePlana); 
            
            // Compara el hash ingresado con el hash almacenado
            if (hashIngresado.equals(hashAlmacenado)) { 
                
                // 4. ÉXITO: Mapear la entidad a un DTO seguro para la sesión
                // Si la clave coincide, devolvemos el objeto UsuarioInfoDTO
                // Necesitarás un Mapper auxiliar para esto. Usaremos una conversión directa por ahora.
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
