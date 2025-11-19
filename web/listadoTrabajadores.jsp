<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lista de Trabajadores</title>
        </head>
    <body>
        <div id="page-content-wrapper">
            <h1>Gestión de Trabajadores</h1>

            <%-- Mostrar Mensaje de Éxito/Error --%>
            <c:if test="${requestScope.mensaje != null}"><div class="alert alert-success">${requestScope.mensaje}</div></c:if>
            <c:if test="${requestScope.error != null}"><div class="alert alert-danger">${requestScope.error}</div></c:if>

            <table>
                <thead>
                    <tr>
                        <th>ID</th><th>Nombre</th><th>Rol</th><th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${requestScope.listaTrabajadores}">
                        <tr>
                            <td>${t.idTrabajador}</td>
                            <td>${t.nombre} ${t.apellido}</td>
                            <td>${t.rol}</td>
                            <td>[Botones de acción]</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>