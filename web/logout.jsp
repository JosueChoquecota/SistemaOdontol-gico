<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. OBTENER LA SESIÓN ACTUAL
    // 'false' significa que no cree una nueva si no existe
    HttpSession sesionActual = request.getSession(false);

    if (sesionActual != null) {
        // 2. DESTRUIR LA SESIÓN
        // Esto borra el atributo "usuario" y cualquier dato guardado
        sesionActual.invalidate();
    }

    // 3. REDIRIGIR AL LOGIN
    // Envía al navegador de vuelta a la pantalla de ingreso
    response.sendRedirect("login.jsp");
%>