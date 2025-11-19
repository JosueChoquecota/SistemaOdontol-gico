<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Citas Médicas</title>
    
 <!-- 1. Bootstrap CSS (del jspf/styles.jspf) -->
<%@ include file="/WEB-INF/jspf/styles.jspf" %>

<!-- 2. Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<!-- 3. Tu CSS personalizado AL FINAL -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/RESOURCES/css/citaMedicas.css">
</head>

<body class="d-flex">

    <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>

    <div class="main-content-wrapper flex-grow-1 vh-100 overflow-auto">
        
        <div class="container-fluid p-4">

            <div class="d-flex justify-content-between align-items-center mb-4">
                <button class="btn btn-secondary bg-teal border-0">Help</button>
                <h2 class="fw-bold m-0 text-dark-grey">Hola, Doctor!</h2>
            </div>

            <div class="d-flex align-items-center section-title-container">
                <div class="icon-box me-2">
                    <i class="bi bi-journal-plus text-white fs-5"></i>
                </div>
                <h3 class="fw-bold m-0 text-dark-grey">Reservar cita Médica</h3>
            </div>

            <form action="TuServlet" method="POST">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label for="paciente" class="form-label fw-bold">Paciente</label>
                        <select class="form-select" id="paciente" name="idPaciente">
                            <option selected disabled>Seleccionar Paciente</option>
                            <option value="1">Juan Perez</option>
                            <option value="2">Maria Garcia</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label for="motivo" class="form-label fw-bold">Motivo de cita</label>
                        <input type="text" class="form-control" id="motivo" name="motivoCita" placeholder="Ej: Dolor de muelas">
                    </div>
                    <div class="col-md-3">
                        <label for="fechaCita" class="form-label fw-bold">Fecha de Cita</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-calendar-event"></i></span>
                            <input type="date" class="form-control" id="fechaCita" name="fechaCita">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <label for="horaCita" class="form-label fw-bold">Hora</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-clock"></i></span>
                            <select class="form-select" id="horaCita" name="horaCita">
                                <option selected disabled>Seleccionar hora</option>
                                <option value="09:00">09:00 AM</option>
                                <option value="09:30">09:30 AM</option>
                                <option value="10:00">10:00 AM</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row mt-4">
                    <div class="col-12">
                        <label for="especificaciones" class="form-label fw-bold">Especificaciones</label>
                        <textarea class="form-control" id="especificaciones" name="especificaciones" rows="5" placeholder="Cita"></textarea>
                    </div>
                </div>

                <div class="row mt-4 mb-5">
    <div class="col-12 d-flex justify-content-end gap-3">
        
        <button type="button" class="btn btn-large-action btn-outline-teal">
            Imprimir PDF
        </button>
        
        <button type="submit" class="btn btn-large-action btn-solid-teal">
            Reservar
        </button>

</div>
</div>
            </form>

            <div class="d-flex align-items-center mb-4">
                <div class="icon-box me-2">
                    <i class="bi bi-archive-fill text-white fs-5"></i>
                </div>
                <h3 class="fw-bold m-0 text-dark-grey">Reportes</h3>
            </div>

            <div class="table-responsive mb-5">
                <table class="table align-middle">
                    <thead style="border-bottom: 2px solid #dee2e6;">
                        <tr class="fw-bold">
                            <th scope="col">Cod</th>
                            <th scope="col">Paciente</th>
                            <th scope="col">Dentista</th>
                            <th scope="col">Motivo de Cita</th>
                            <th scope="col">Fecha de cita</th>
                            <th scope="col">Hora de cita</th>
                            <th scope="col">Estado</th>
                            <th scope="col" class="text-end"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="fw-bold text-secondary">P01</td>
                            <td class="fw-bold text-secondary">Paciente_1</td>
                            <td class="fw-bold text-secondary">Dentista_2</td>
                            <td class="fw-bold text-secondary">Caries</td>
                            <td class="fw-bold text-secondary">10/09/25</td>
                            <td class="fw-bold text-secondary">16:00 pm</td>
                            <td class="fw-bold text-secondary">Pendiente</td>
                            <td class="text-end">
                                <div class="d-flex justify-content-end gap-2">
                                    <a href="#" class="btn btn-sm text-white bg-teal btn-table-action">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="#" class="btn btn-dark btn-sm text-white btn-table-action">
                                        <i class="bi bi-trash-fill"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>

</body>
</html>