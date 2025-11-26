/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.controller;

import com.utp.sistemaOdontologo.dtos.LoginDTORequest;
import com.utp.sistemaOdontologo.dtos.UsuarioInfoDTO;
import com.utp.sistemaOdontologo.services.UsuarioService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
public class LoginController extends HttpServlet {
    private UsuarioService usuarioService = new UsuarioService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        LoginDTORequest dto = new LoginDTORequest();
        // Asumimos que los parámetros son 'username' y 'contrasena'
        dto.setUsername(request.getParameter("username"));
        dto.setContrasena(request.getParameter("contrasena")); 

        UsuarioInfoDTO usuarioAutenticado = usuarioService.login(dto);

        if (usuarioAutenticado != null) {
            // 1. ÉXITO: Crear sesión y redirigir al Dashboard
            request.getSession().setAttribute("usuario", usuarioAutenticado);

            // Redirigir al dashboard (ejemplo: index.jsp o dashboard.jsp)
            // Usamos sendRedirect para que el navegador haga una nueva petición (limpia la URL POST)
            response.sendRedirect(request.getContextPath() + "/Tablero.jsp"); 
        } else {
            // 2. FALLO: Establecer mensaje de error y regresar a la vista de login

            // Asignar el mensaje de error para mostrar en la JSP
            request.setAttribute("error_login", "Usuario o contraseña inválidos o cuenta inactiva.");

            // Regresar a la página de login para que la JSP pueda leer el atributo "error_login"
            // NOTA: Asegúrate de que tu JSP de login esté en la raíz o en una ruta accesible (ej: /login.jsp)
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
