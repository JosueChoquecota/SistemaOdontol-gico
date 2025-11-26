<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- Necesario para fechas --%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Citas Médicas</title>
    
    <%-- ESTILOS GLOBALES --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    
    <style>
        .modal-header.bg-purple { background-color: #6f42c1; color: white; }
        .btn-custom-purple { background-color: #6f42c1; color: white; border: none; }
        .btn-custom-purple:hover { background-color: #59359a; color: white; }
        
        /* Badges de estado */
        .badge-pendiente { background-color: #ffc107; color: #000; }
        .badge-atendido { background-color: #198754; color: #fff; }
        .badge-cancelado { background-color: #dc3545; color: #fff; }
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
                    
                    <%-- CABECERA Y BOTÓN NUEVA CITA --%>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h1 class="h3 mb-0 text-dark fw-bold">Citas Médicas</h1>
                            <p class="text-muted mb-0">Agenda y gestiona las consultas de los pacientes.</p>
                        </div>
                        
                        <button type="button" class="btn btn-custom-purple shadow-sm px-4 py-2" data-bs-toggle="modal" data-bs-target="#modalGestionCita">
                            <i class="bi bi-calendar-plus-fill me-2"></i>Nueva Cita
                        </button>
                        
                        <%-- TRIGGER OCULTO PARA EDICIÓN --%>
                        <button id="btnTriggerEdit" type="button" style="display: none;" data-bs-toggle="modal" data-bs-target="#modalGestionCita"></button>
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
                                <h5 class="mb-0 text-muted"><i class="bi bi-journal-medical me-2"></i>Programación</h5>
                                <div class="d-flex gap-2">
                                    <button class="btn btn-sm btn-outline-secondary">Hoy</button>
                                    <button class="btn btn-sm btn-outline-secondary">Semana</button>
                                </div>
                            </div>
                        </div>
                        
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light text-secondary">
                                        <tr>
                                            <th class="ps-4">Cod</th>
                                            <th>Paciente</th>
                                            <th>Odontólogo</th>
                                            <th>Motivo</th>
                                            <th>Fecha / Hora</th>
                                            <th>Estado</th>
                                            <th class="text-end pe-4">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="c" items="${listaCitas}">
                                            <tr>
                                                <td class="ps-4 fw-bold text-muted">#${c.idCita}</td>
                                                
                                                <%-- COLUMNA PACIENTE --%>
                                                <td>
                                                    <div class="d-flex flex-column">
                                                        <span class="fw-bold text-dark">${c.paciente.nombresPaciente} ${c.paciente.apellidosPaciente}</span>
                                                        <small class="text-muted">DNI: ${c.paciente.documento}</small>
                                                    </div>
                                                </td>
                                                
                                                <%-- COLUMNA ODONTOLOGO --%>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <i class="bi bi-person-workspace me-2 text-primary"></i>
                                                        <span>${c.odontologo.nombre} ${c.odontologo.apellido}</span>
                                                    </div>
                                                </td>

                                                <td>${c.motivo}</td>
                                                
                                                <%-- FECHA Y HORA --%>
                                                <td>
                                                    <div class="d-flex flex-column">
                                                        <span class="fw-bold"><i class="bi bi-calendar3 me-1"></i>${c.fechaCita}</span>
                                                        <small class="text-muted"><i class="bi bi-clock me-1"></i>${c.horaCita}</small>
                                                    </div>
                                                </td>
                                                
                                                <%-- ESTADO CON COLORES --%>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${c.estado == 'PENDIENTE'}">
                                                            <span class="badge badge-pendiente border">Pendiente</span>
                                                        </c:when>
                                                        <c:when test="${c.estado == 'ATENDIDO'}">
                                                            <span class="badge badge-atendido">Atendido</span>
                                                        </c:when>
                                                        <c:when test="${c.estado == 'CANCELADO'}">
                                                            <span class="badge badge-cancelado">Cancelado</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${c.estado}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                
                                                <td class="text-end pe-4">
                                                    <div class="btn-group">
                                                        <a href="citas?operacion=buscar_id&id=${c.idCita}" 
                                                           class="btn btn-sm btn-outline-primary" title="Editar">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </a>
                                                        <button onclick="confirmarEliminacion(${c.idCita})" 
                                                                class="btn btn-sm btn-outline-danger" title="Cancelar Cita">
                                                            <i class="bi bi-x-circle"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                        <%-- MENSAJE SI NO HAY DATOS --%>
                                        <c:if test="${empty listaCitas}">
                                            <tr>
                                                <td colspan="7" class="text-center py-5">
                                                    <div class="text-muted opacity-50">
                                                        <i class="bi bi-calendar-x display-1"></i>
                                                        <p class="mt-2">No hay citas programadas.</p>
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

    <div class="modal fade" id="modalGestionCita" data-bs-backdrop="static" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                
                <div class="modal-header bg-purple text-white">
                    <h5 class="modal-title fw-bold">
                        <c:choose>
                            <c:when test="${citaEditar != null}">
                                <i class="bi bi-pencil-square me-2"></i>Reprogramar / Editar Cita #${citaEditar.idCita}
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-calendar-plus me-2"></i>Nueva Cita Médica
                            </c:otherwise>
                        </c:choose>
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <div class="modal-body p-4">
                    <form action="citas" method="POST"> 
                        <input type="hidden" name="operacion" value="${citaEditar != null ? 'actualizar_cita' : 'registrar_cita'}">
                        <c:if test="${citaEditar != null}">
                            <input type="hidden" name="idCita" value="${citaEditar.idCita}">
                        </c:if>
                        
                        <div class="row g-3">
                            <div class="col-12"><h6 class="fw-bold text-muted mb-2">Información del Paciente</h6></div>

                            <%-- DROPDOWN DE PACIENTES --%>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Paciente</label>
                                <select name="idPaciente" class="form-select" required>
                                    <option value="" selected disabled>Seleccionar Paciente</option>
                                    
                                    <%-- Itera sobre la lista de pacientes enviada por el Servlet --%>
                                    <c:forEach var="p" items="${listaPacientes}">
                                        <option value="${p.idPaciente}" ${citaEditar != null && citaEditar.idPaciente == p.idPaciente ? 'selected' : ''}>
                                            ${p.documento} - ${p.nombresPaciente} ${p.apellidosPaciente}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <%-- DROPDOWN DE ODONTÓLOGOS --%>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Odontólogo Tratante</label>
                                <select name="idOdontologo" class="form-select" required>
                                    <option value="" selected disabled>Seleccionar Doctor</option>
                                    
                                    <%-- Itera sobre la lista de odontólogos enviada por el Servlet --%>
                                    <c:forEach var="doc" items="${listaOdontologos}">
                                        <option value="${doc.idTrabajador}" ${citaEditar != null && citaEditar.idOdontologo == doc.idTrabajador ? 'selected' : ''}>
                                            Dr. ${doc.nombre} ${doc.apellido} (${doc.especialidad.nombre})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-12 mt-3"><h6 class="fw-bold text-muted mb-2">Detalles de la Cita</h6></div>

                            <%-- BLOQUE DE FECHA Y HORA --%>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Fecha</label>
                                <input type="date" name="fechaCita" class="form-control" required
                                       value="${citaEditar != null ? citaEditar.fechaCita : ''}">
                            </div>

                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Horario Disponible</label>
                                
                                <%-- CAMBIO: Usamos SELECT en lugar de INPUT TIME --%>
                                <select name="idHorario" class="form-select" required>
                                    <option value="" selected disabled>Seleccionar Horario</option>
                                    
                                    <c:forEach var="h" items="${listaHorarios}">
                                        <%-- 
                                            Logica de selección: Si estamos editando, comparamos
                                            la hora de inicio del horario con la hora de la cita guardada 
                                        --%>
                                        <c:set var="selected" value="" />
                                        <c:if test="${citaEditar != null && citaEditar.horaCita == h.horarioInicio.toString()}">
                                            <c:set var="selected" value="selected" />
                                        </c:if>
                                        
                                        <%-- Value es el ID, pero mostramos INICIO - FIN --%>
                                        <option value="${h.idHorario}" ${selected}>
                                            ${h.horarioInicio} - ${h.horarioFin}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <%-- ESTADO (Solo visible en edición) --%>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Estado</label>
                                <select name="estado" class="form-select" ${citaEditar == null ? 'disabled' : ''}>
                                    <option value="PENDIENTE" ${citaEditar.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                    <option value="ATENDIDO" ${citaEditar.estado == 'ATENDIDO' ? 'selected' : ''}>Atendido</option>
                                    <option value="CANCELADO" ${citaEditar.estado == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
                                </select>
                                <%-- Si es nuevo, mandamos input oculto por defecto --%>
                                <c:if test="${citaEditar == null}">
                                    <input type="hidden" name="estado" value="PENDIENTE">
                                </c:if>
                            </div>

                            <%-- DROPDOWN DE MOTIVO --%>
                            <div class="col-12">
                                <label class="form-label small fw-bold">Motivo de Consulta</label>
                                <select name="motivo" class="form-select mb-2" required>
                                    <option value="" selected disabled>Seleccione el motivo...</option>
                                    <option value="Consulta General" ${citaEditar.motivo == 'Consulta General' ? 'selected' : ''}>Consulta General / Revisión</option>
                                    <option value="Limpieza Dental" ${citaEditar.motivo == 'Limpieza Dental' ? 'selected' : ''}>Limpieza Dental (Profilaxis)</option>
                                    <option value="Dolor de Muelas" ${citaEditar.motivo == 'Dolor de Muelas' ? 'selected' : ''}>Dolor de Muelas / Urgencia</option>
                                    <option value="Ortodoncia" ${citaEditar.motivo == 'Ortodoncia' ? 'selected' : ''}>Control de Ortodoncia (Brackets)</option>
                                    <option value="Blanqueamiento" ${citaEditar.motivo == 'Blanqueamiento' ? 'selected' : ''}>Blanqueamiento Dental</option>
                                    <option value="Extracción" ${citaEditar.motivo == 'Extracción' ? 'selected' : ''}>Extracción de Diente</option>
                                    <option value="Otros" ${citaEditar.motivo == 'Otros' ? 'selected' : ''}>Otros</option>
                                </select>
                                
                                <%-- CAMPO OPCIONAL PARA ESPECIFICAR MÁS --%>
                                <textarea name="observaciones" class="form-control" rows="2" placeholder="Observaciones adicionales (Opcional)...">${citaEditar != null ? citaEditar.observaciones : ''}</textarea>
                            </div>

                        </div>
                        
                        <div class="modal-footer border-0 px-0 pb-0 mt-4">
                            <button type="button" class="btn btn-light border" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-custom-purple px-5 fw-bold">
                                ${citaEditar != null ? 'Guardar Cambios' : 'Agendar Cita'}
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
            // Auto-apertura si estamos editando
            <c:if test="${citaEditar != null}">
                var btnTrigger = document.getElementById('btnTriggerEdit');
                if(btnTrigger) btnTrigger.click();
            </c:if>
        });

        function confirmarEliminacion(id) {
            if (confirm("¿Estás seguro de cancelar la cita #" + id + "?")) {
                window.location.href = "citas?operacion=eliminar_cita&id=" + id;
            }
        }
    </script>
</body>
</html>