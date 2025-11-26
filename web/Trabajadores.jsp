<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Trabajadores</title>
    
    <%-- ESTILOS GLOBALES --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    
    <style>
        .modal-header.bg-warning { color: #000 !important; }
        .modal-header.bg-teal { background-color: #008080; color: white; }
    </style>
</head>
<body>
    
    <div class="d-flex" id="wrapper">
        
        <%-- 1. SIDEBAR --%>
        <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
        
        <%-- 2. CONTENIDO PRINCIPAL --%>
        <div id="page-content-wrapper" class="w-100">
            
            <%-- A. BARRA SUPERIOR --%>
            <%@ include file="/WEB-INF/jspf/topBar.jspf" %> 

            <%-- B. CONTENIDO --%>
            <div class="container-fluid p-4"> 
                
                <section class="content">
                    
                    <%-- CABECERA Y BOTÓN NUEVO --%>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h1 class="h3 mb-0 text-dark fw-bold">Trabajadores</h1>
                            <p class="text-muted mb-0">Gestiona el personal médico y administrativo.</p>
                        </div>
                        
                        <%-- BOTÓN NUEVO (Abre modal vacío) --%>
                        <button type="button" class="btn btn-custom-teal shadow-sm px-4 py-2" data-bs-toggle="modal" data-bs-target="#modalGestionTrabajador">
                            <i class="bi bi-person-badge-fill me-2"></i>Nuevo Trabajador
                        </button>
                        
                        <%-- BOTÓN OCULTO TRIGGER PARA EDICIÓN --%>
                        <button id="btnTriggerEdit" type="button" style="display: none;" data-bs-toggle="modal" data-bs-target="#modalGestionTrabajador"></button>
                    </div>

                    <%-- MENSAJES DE ALERTA --%>
                    <c:if test="${not empty mensaje}">
                        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>${mensaje}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <div class="card shadow-sm border-0 rounded-3">
                        <div class="card-header bg-white py-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <h5 class="mb-0 text-muted"><i class="bi bi-list-ul me-2"></i>Directorio de Personal</h5>
                                <div class="d-flex gap-2">
                                    <button class="btn btn-sm btn-outline-secondary">PDF</button>
                                    <button class="btn btn-sm btn-outline-secondary">Filtrar</button>
                                </div>
                            </div>
                        </div>
                        
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light text-secondary">
                                        <tr>
                                            <th class="ps-4">ID</th>
                                            <th>Trabajador</th>
                                            <th>Documento</th>
                                            <th>Rol / Cargo</th>
                                            <th>Contacto</th>
                                            <th class="text-end pe-4">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="t" items="${listaTrabajadores}">
                                            <tr>
                                                <td class="ps-4 fw-bold text-muted">#${t.idTrabajador}</td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <div class="rounded-circle bg-success text-white d-flex justify-content-center align-items-center me-3 shadow-sm" style="width: 40px; height: 40px; font-weight: bold;">
                                                            ${t.nombre.substring(0,1)}
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-dark">${t.nombre} ${t.apellido}</div>
                                                            <small class="text-muted">${t.usuario.usuario}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                
                                                <%-- CORRECCIÓN APLICADA AQUÍ: t.documento --%>
                                                <td><span class="badge bg-light text-dark border">${t.documento}</span></td>
                                                <td>
                                                    <span class="badge bg-info text-dark bg-opacity-10 border border-info">${t.rol}</span>
                                                </td>
                                                <td>
                                                    <div class="d-flex flex-column small">
                                                        <%-- CORRECCIÓN APLICADA AQUÍ: t.contacto.telefono --%>
                                                        <span><i class="bi bi-telephone me-2 text-muted"></i>${t.contacto != null ? t.contacto.telefono : '-'}</span>
                                                        <span><i class="bi bi-envelope me-2 text-muted"></i>${t.contacto != null ? t.contacto.correo : '-'}</span>
                                                    </div>
                                                </td>
                                                
                                                <td class="text-end pe-4">
                                                    <div class="btn-group">
                                                        <a href="trabajadores?operacion=buscar_id&id=${t.idTrabajador}" 
                                                           class="btn btn-sm btn-outline-primary" 
                                                           data-bs-toggle="tooltip" title="Editar">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </a>

                                                        <button onclick="confirmarEliminacion(${t.idTrabajador})" 
                                                                class="btn btn-sm btn-outline-danger" 
                                                                data-bs-toggle="tooltip" title="Eliminar">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                        <c:if test="${empty listaTrabajadores}">
                                            <tr>
                                                <td colspan="6" class="text-center py-5">
                                                    <div class="text-muted opacity-50">
                                                        <i class="bi bi-person-badge display-1"></i>
                                                        <p class="mt-2">No hay trabajadores registrados.</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </section>
            </div> 
        </div> 
    </div> 

    <div class="modal fade" id="modalGestionTrabajador" data-bs-backdrop="static" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                
                <div class="modal-header ${trabajadorEditar != null ? 'bg-warning' : 'bg-teal'} text-white">
                    <h5 class="modal-title fw-bold">
                        <c:choose>
                            <c:when test="${trabajadorEditar != null}">
                                <i class="bi bi-pencil-square me-2"></i>Editar Trabajador #${trabajadorEditar.idTrabajador}
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-person-badge-fill me-2"></i>Nuevo Trabajador
                            </c:otherwise>
                        </c:choose>
                    </h5>
                    
                    <c:choose>
                        <c:when test="${trabajadorEditar != null}">
                             <a href="trabajadores" class="btn-close" aria-label="Close"></a>
                        </c:when>
                        <c:otherwise>
                             <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="modal-body p-4">
                    <form action="trabajadores" method="POST"> 
                        
                        <%-- INPUTS OCULTOS --%>
                        <input type="hidden" name="operacion" value="${trabajadorEditar != null ? 'actualizar_trabajador' : 'registrar_trabajador'}">
                        <c:if test="${trabajadorEditar != null}">
                            <input type="hidden" name="idTrabajador" value="${trabajadorEditar.idTrabajador}">
                        </c:if>
                        
                        <div class="row g-3">
                            <%-- DATOS PERSONALES --%>
                            <div class="col-12"><h6 class="fw-bold text-muted mb-2">Datos Personales</h6></div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Nombre</label>
                                <input type="text" name="nombre" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.nombre : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Apellido</label>
                                <input type="text" name="apellido" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.apellido : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Tipo Documento</label>
                                <%-- AGREGADO id="selectTipoDocumento" para el Script --%>
                                <select name="idTipoDocumento" id="selectTipoDocumento" class="form-select" required>
                                    <option value="1">DNI</option>
                                    <option value="2">Cédula de Ciudadanía</option>
                                    <option value="3">Pasaporte</option>
                                    <option value="4">Carné de Extranjería</option>
                                    <option value="5">RUC</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">N° Documento</label>
                                <%-- AGREGADO id="inputDocumento" y maxlength para el Script --%>
                                <input type="text" name="documento" id="inputDocumento" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.documento : ''}"
                                       maxlength="15"
                                       placeholder="Ingrese número">
                            </div>

                            <%-- CREDENCIALES Y ROL --%>
                            <div class="col-12 mt-3"><h6 class="fw-bold text-muted mb-2">Credenciales y Rol</h6></div>
                            
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Usuario / Login</label>
                                <input type="text" name="username" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.username : ''}">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Contraseña</label>
                                <input type="password" name="contrasena" class="form-control" 
                                       placeholder="${trabajadorEditar != null ? '(Dejar vacío para no cambiar)' : ''}" 
                                       ${trabajadorEditar == null ? 'required' : ''}>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Rol</label>
                                <select name="idRol" class="form-select" required>
                                    <option value="1">Odontólogo</option>
                                    <option value="2">Asistente</option>
                                    <option value="3">Recepcionista</option>
                                    <option value="4">Administrador</option>
                                </select>
                            </div>

                            <%-- DATOS PROFESIONALES --%>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Colegiatura (Opcional)</label>
                                <input type="text" name="colegiatura" class="form-control" 
                                       value="${trabajadorEditar != null ? trabajadorEditar.colegiatura : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Especialidad</label>
                                <select name="idEspecialidad" class="form-select">
                                    <option value="1">Odontología General</option>
                                    <option value="2">Ortodoncia</option>
                                    <option value="3">Endodoncia</option>
                                    <option value="4">Periodoncia</option>
                                    <option value="5">Odontopediatría</option>
                                    <option value="6">Cirugía Oral</option>
                                </select>
                            </div>

                            <%-- CONTACTO --%>
                            <div class="col-12 mt-3"><h6 class="fw-bold text-muted mb-2">Contacto</h6></div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Correo</label>
                                <input type="email" name="correo" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.contacto.correo : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Teléfono</label>
                                <input type="text" name="telefono" class="form-control" required
                                       value="${trabajadorEditar != null ? trabajadorEditar.contacto.telefono : ''}">
                            </div>
                            
                        </div>
                        
                        <div class="modal-footer border-0 px-0 pb-0 mt-4">
                            <c:if test="${trabajadorEditar != null}">
                                <a href="trabajadores" class="btn btn-light border">Cancelar</a>
                            </c:if>
                            <c:if test="${trabajadorEditar == null}">
                                <button type="button" class="btn btn-light border" data-bs-dismiss="modal">Cancelar</button>
                            </c:if>
                            
                            <button type="submit" class="btn ${trabajadorEditar != null ? 'btn-warning text-dark' : 'btn-custom-teal'} px-5 fw-bold text-white">
                                ${trabajadorEditar != null ? 'Guardar Cambios' : 'Registrar'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- SCRIPTS --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            
            // 1. LÓGICA DE AUTO-APERTURA (Para edición)
            <c:if test="${trabajadorEditar != null}">
                console.log("Modo Edición Trabajador Detectado.");
                var btnTrigger = document.getElementById('btnTriggerEdit');
                if(btnTrigger) btnTrigger.click();
            </c:if>

            // 2. LÓGICA PARA VALIDACIÓN DNI/RUC
            const selectTipo = document.getElementById('selectTipoDocumento');
            const inputDoc = document.getElementById('inputDocumento');


            // Activar listener y ejecutar al inicio
            if(selectTipo) {
                selectTipo.addEventListener('change', configurarCampoDocumento);
                configurarCampoDocumento(); // Ejecutar 1 vez al cargar
            }
        });

        // 3. ELIMINACIÓN
        function confirmarEliminacion(id) {
            if (confirm("¿Estás seguro de eliminar al trabajador #" + id + "?")) {
                window.location.href = "trabajadores?operacion=eliminar_trabajador&id=" + id;
            }
        }
    </script>
    
    <style>
        /* Replicamos el estilo custom aquí por si acaso */
        .bg-teal { background-color: #008080 !important; }
        .btn-custom-teal {
            background-color: #008080;
            color: white;
            border: none;
        }
        .btn-custom-teal:hover {
            background-color: #006666;
            color: white;
        }
    </style>
</body>
</html>