<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Trabajadores</title>
    
    <%-- Asume que aquí incluyes tus estilos, Bootstrap y Bootstrap Icons --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        /* CSS Mínimo para el ajuste visual si es necesario */
        .bg-teal { background-color: #008080 !important; }
        .form-control-sm { height: calc(1.5em + .5rem + 2px); } /* Ajuste de altura para inputs pequeños */
    </style>
</head>
<body>
    
    <div class="d-flex" id="wrapper">
        
        <%@ include file="/WEB-INF/jspf/sideBar.jspf" %> 
        
        <div id="page-content-wrapper" class="p-4 w-100">
            
            <header class="d-flex justify-content-between align-items-center mb-4">
                <button class="btn btn-sm btn-info text-white">Help</button>
                <div class="d-flex align-items-center">
                    <span class="user me-2">Hola, Doctor!</span>
                    <img src="RESOURCES/imgs/doctor_icon.png" alt="Doctor" style="width: 40px; height: 40px; border-radius: 50%;">
                </div>
            </header>
            
            <h1 class="h3 fw-bold mb-4">Gestión de Trabajadores</h1>

            <div class="card shadow-sm mb-5">
                <div class="card-header bg-light">
                    <h3 class="h6 mb-0">Insertar Trabajador</h3>
                </div>
                <div class="card-body">
                    <form action="trabajadores" method="POST"> 
                        <input type="hidden" name="operacion" value="registrar_trabajador">

                        <div class="row g-3 align-items-end">
                            
                            <%-- COLUMNAS DE CAMPOS DE ENTRADA --%>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="nombre" class="form-label small">Nombre:</label>
                                <input type="text" name="nombre" class="form-control form-control-sm" placeholder="Primer Nombre" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="apellido" class="form-label small">Apellido:</label>
                                <input type="text" name="apellido" class="form-control form-control-sm" placeholder="Apellido" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="idTipoDocumento" class="form-label small">Tipo de Documento:</label>
                                <select name="idTipoDocumento" class="form-select form-select-sm" required>
                                    <option value="1">DNI</option>
                                    <option value="2">Pasaporte</option>
                                    <%-- Aquí iría la iteración de la BD con JSTL --%>
                                </select>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="documento" class="form-label small">N° de Documento:</label>
                                <input type="text" name="documento" class="form-control form-control-sm" placeholder="Documento" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="username">Usuario / Login:</label>
                                <input type="text" name="username" placeholder="Nombre de Usuario" required>
                            </div>

                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="correo" class="form-label small">Correo:</label>
                                <input type="email" name="correo" class="form-control form-control-sm" placeholder="Correo" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="contrasena" class="form-label small">Contraseña:</label>
                                <input type="password" name="contrasena" class="form-control form-control-sm" placeholder="Contraseña" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="telefono" class="form-label small">Teléfono:</label>
                                <input type="text" name="telefono" class="form-control form-control-sm" placeholder="Teléfono" required>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="idRol" class="form-label small">Rol:</label>
                                <select name="idRol" class="form-select form-select-sm" required>
                                    <option value="1">Odontólogo</option>
                                    <option value="2">Asistente</option>
                                    <option value="3">Recepcionista</option>
                                    <option value="4">Administrador</option>
                                    <%-- Usar IDs de Rol reales --%>
                                </select>
                            </div>
                                
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="colegiatura" class="form-label small">Colegiatura:</label>
                                <input type="text" name="colegiatura" class="form-control form-control-sm" placeholder="Primer Nombre">
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6">
                                <label for="idEspecialidad" class="form-label small">Especialidad:</label>
                                <select name="idEspecialidad" class="form-select form-select-sm">
                                    <option value="1">Odontología General</option>
                                    <option value="2">Ortodoncia</option>
                                    <option value="3">Endodoncia</option>
                                    <option value="4">Periodoncia</option>
                                    <option value="5">Odontopediatría</option>
                                    <option value="6">Cirugía Oral y Maxilofacial</option> 
                                    <%-- Iteración de Especialidades --%>
                                </select>
                            </div>

                            <%-- BOTONES DE ACCIÓN (Guardar) --%>
                            <div class="col-lg-3 col-md-4 col-sm-6 d-grid">
                                <button type="submit" class="btn btn-info btn-sm">Guardar</button>
                            </div>
                            
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="card shadow-sm">
                <div class="card-header bg-light d-flex justify-content-between align-items-center">
                    <h3 class="h6 mb-0">Trabajadores</h3>
                    <%-- Filtro y PDF --%>
                    <div class="d-flex gap-2">
                        <button class="btn btn-sm btn-outline-secondary">PDF</button>
                        <button class="btn btn-sm btn-outline-secondary">Filtrar</button>
                    </div>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Código</th>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>DNI/RUC</th>
                                    <th>Correo</th>
                                    <th>Teléfono</th>
                                    <th>Rol</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- ITERACIÓN DE DATOS DEL SERVICIO (listAll) --%>
                                <c:forEach var="t" items="${requestScope.listaTrabajadores}">
                                    <tr>
                                        <td><c:out value="${t.idTrabajador}" /></td>
                                        <td><c:out value="${t.nombre}" /></td>
                                        <td><c:out value="${t.apellido}" /></td>
                                        <td><c:out value="${t.documento}" /></td>
                                        <td><c:out value="${t.contacto != null ? t.contacto.correo : ''}" /></td>
                                        <td><c:out value="${t.contacto != null ? t.contacto.telefono : ''}" /></td>
                                        <td><c:out value="${t.rol}" /></td>

                                        <td>
                                            <a href="trabajadores?operacion=buscar_id&id=${t.idTrabajador}" 
                                               class="btn btn-sm btn-outline-info me-1">
                                               <i class="bi bi-pencil-square"></i>
                                            </a>

                                            <a href="trabajadores?operacion=eliminar_trabajador&id=${t.idTrabajador}" 
                                               class="btn btn-sm btn-outline-danger">
                                               <i class="bi bi-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>

                                       
                                
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>
    
</body>
</html>