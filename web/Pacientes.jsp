<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Pacientes</title>
    
    <%-- ESTILOS GLOBALES --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    
    <style>
        /* Estilos para diferenciar visualmente el modo Edición */
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
                    
                    <%-- CABECERA --%>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h1 class="h3 mb-0 text-dark fw-bold">Pacientes</h1>
                            <p class="text-muted mb-0">Administra la información de tus pacientes aquí.</p>
                        </div>
                        
                        <%-- BOTÓN NUEVO (Abre modal vacío) --%>
                        <button type="button" class="btn btn-success shadow-sm px-4 py-2" data-bs-toggle="modal" data-bs-target="#modalGestionPaciente">
                            <i class="bi bi-person-plus-fill me-2"></i>Nuevo Paciente
                        </button>
                        
                        <%-- BOTÓN OCULTO PARA ACTIVAR EDICIÓN AUTOMÁTICA (TRUCO INFALIBLE) --%>
                        <button id="btnTriggerEdit" type="button" style="display: none;" data-bs-toggle="modal" data-bs-target="#modalGestionPaciente"></button>
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

                    <!-- ============================================= -->
                    <!-- TABLA DE PACIENTES                            -->
                    <!-- ============================================= -->
                    <div class="card shadow-sm border-0 rounded-3">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4 py-3">ID</th>
                                            <th>Paciente</th>
                                            <th>DNI / RUC</th>
                                            <th>Contacto</th>
                                            <th class="text-end pe-4">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${listaPacientes}">
                                            <tr>
                                                <td class="ps-4 fw-bold text-muted">#${p.idPaciente}</td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <div class="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-3 shadow-sm" style="width: 40px; height: 40px; font-weight: bold;">
                                                            ${p.nombresPaciente.substring(0,1)}
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-dark">${p.nombresPaciente} ${p.apellidosPaciente}</div>
                                                            <small class="text-muted text-uppercase">${p.sexo eq 'M' ? 'Masculino' : (p.sexo eq 'F' ? 'Femenino' : 'Otro')}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td><span class="badge bg-light text-dark border">${p.documento}</span></td>
                                                <td>
                                                    <div class="d-flex flex-column small">
                                                        <span><i class="bi bi-telephone me-2 text-muted"></i>${p.telefono}</span>
                                                        <span><i class="bi bi-envelope me-2 text-muted"></i>${p.correo}</span>
                                                    </div>
                                                </td>
                                                
                                                <td class="text-end pe-4">
                                                    <div class="btn-group">
                                                        <%-- BOTÓN EDITAR: Llama al controlador --%>
                                                        <a href="pacientes?operacion=buscar_id&id=${p.idPaciente}" 
                                                           class="btn btn-sm btn-outline-primary" 
                                                           data-bs-toggle="tooltip" title="Editar Información">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </a>

                                                        <button onclick="confirmarEliminacion(${p.idPaciente})" 
                                                                class="btn btn-sm btn-outline-danger" 
                                                                data-bs-toggle="tooltip" title="Eliminar">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                        <c:if test="${empty listaPacientes}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5">
                                                    <div class="text-muted opacity-50">
                                                        <i class="bi bi-people display-1"></i>
                                                        <p class="mt-2">No se encontraron pacientes.</p>
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

    <!-- ================================================================= -->
    <!-- MODAL UNIVERSAL (SIRVE PARA REGISTRAR Y PARA ACTUALIZAR)          -->
    <!-- ================================================================= -->
    <div class="modal fade" id="modalGestionPaciente" data-bs-backdrop="static" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                
                <div class="modal-header ${pacienteEditar != null ? 'bg-warning' : 'bg-teal'} text-white">
                    <h5 class="modal-title fw-bold">
                        <c:choose>
                            <c:when test="${pacienteEditar != null}">
                                <i class="bi bi-pencil-square me-2"></i>Editar Paciente #${pacienteEditar.idPaciente}
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-person-plus-fill me-2"></i>Nuevo Registro
                            </c:otherwise>
                        </c:choose>
                    </h5>
                    
                    <c:choose>
                        <c:when test="${pacienteEditar != null}">
                             <a href="pacientes" class="btn-close" aria-label="Close"></a>
                        </c:when>
                        <c:otherwise>
                             <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="modal-body p-4">
                    <form action="pacientes" method="POST"> 
                        <input type="hidden" name="operacion" value="${pacienteEditar != null ? 'actualizar_paciente' : 'registrar_paciente'}">
                        
                        <c:if test="${pacienteEditar != null}">
                            <input type="hidden" name="idPaciente" value="${pacienteEditar.idPaciente}">
                        </c:if>
                        
                        <div class="row g-3">
                            <div class="col-12"><h6 class="fw-bold text-muted mb-3">Datos Personales</h6></div>
                            
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Nombres</label>
                                <input type="text" name="nombresPaciente" class="form-control" required
                                       value="${pacienteEditar != null ? pacienteEditar.nombres : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Apellidos</label>
                                <input type="text" name="apellidosPaciente" class="form-control" required
                                       value="${pacienteEditar != null ? pacienteEditar.apellidos : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">DNI / RUC</label>
                                <input type="text" name="documento" class="form-control" required
                                       value="${pacienteEditar != null ? pacienteEditar.documento : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Sexo</label>
                                <select name="sexo" class="form-select" required>
                                    <option value="">Seleccionar...</option>
                                    <option value="F" ${pacienteEditar.sexo == 'F' ? 'selected' : ''}>Femenino</option>
                                    <option value="M" ${pacienteEditar.sexo == 'M' ? 'selected' : ''}>Masculino</option>
                                </select>
                            </div>
                            
                            <div class="col-12 mt-4"><h6 class="fw-bold text-muted mb-3">Información de Contacto</h6></div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Teléfono</label>
                                <input type="text" name="telefono" class="form-control" required
                                       value="${pacienteEditar != null ? pacienteEditar.contacto.telefono : ''}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Correo Electrónico</label>
                                <input type="email" name="correo" class="form-control" required
                                       value="${pacienteEditar != null ? pacienteEditar.contacto.correo : ''}">
                            </div>
                            <div class="col-12">
                                <label class="form-label small fw-bold">Dirección</label>
                                <input type="text" name="direccion" class="form-control" 
                                       value="${pacienteEditar != null ? pacienteEditar.contacto.direccion : ''}">
                            </div>
                        </div>

                        <div class="modal-footer border-0 px-0 pb-0 mt-4">
                            <c:if test="${pacienteEditar != null}">
                                <a href="pacientes" class="btn btn-light border">Cancelar Edición</a>
                            </c:if>
                            <c:if test="${pacienteEditar == null}">
                                <button type="button" class="btn btn-light border" data-bs-dismiss="modal">Cancelar</button>
                            </c:if>
                            
                            <button type="submit" class="btn ${pacienteEditar != null ? 'btn-warning text-dark' : 'btn-success'} px-5 fw-bold">
                                ${pacienteEditar != null ? 'Guardar Cambios' : 'Registrar'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- ================================================================= --%>
    <%-- SCRIPTS                                                           --%>
    <%-- ================================================================= --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // 1. AUTO-APERTURA DEL MODAL (VERSIÓN ROBUSTA)
        document.addEventListener("DOMContentLoaded", function() {
            // Verificamos si existe la variable 'pacienteEditar' del servidor
            <c:if test="${pacienteEditar != null}">
                console.log("✅ MODO EDICIÓN ACTIVADO: Intentando abrir modal...");
                
                // MÉTODO INFALIBLE: Clic en un botón oculto
                // Esto evita problemas de inicialización manual de Bootstrap
                var btnTrigger = document.getElementById('btnTriggerEdit');
                if(btnTrigger) {
                    btnTrigger.click();
                } else {
                    console.error("❌ No se encontró el botón trigger");
                }
            </c:if>
        });

        // 2. ELIMINACIÓN
        function confirmarEliminacion(id) {
            if (confirm("¿Estás seguro de eliminar al paciente #" + id + "?")) {
                window.location.href = "pacientes?operacion=eliminar_paciente&id=" + id;
            }
        }
    </script>
</body>
</html>