<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard del Doctor</title>
    
    <%-- LIBRERÍA FULLCALENDAR --%>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.14/index.global.min.js'></script>
    
    <%-- ESTILOS GLOBALES (BOOTSTRAP + ICONOS) --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %>
    
    <style>
        /* =========================================
           ESTILOS SEGUROS PARA EL TABLERO
           ========================================= */

        #page-content-wrapper {
            flex-grow: 1;
            height: 100vh;
            overflow-y: auto; 
            background-color: #fff;
        }

        .btn-custom-teal {
            background-color: #20c997; 
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 5px;
            font-weight: 500;
            margin-right: 5px;
            margin-bottom: 10px;
        }
        .btn-custom-teal:hover {
            background-color: #1aa179;
            color: white;
        }

        .btn-custom-grey {
            background-color: #6c757d;
            color: white;
            border: none;
            padding: 6px 15px;
            border-radius: 4px;
        }
        .btn-custom-grey:hover {
            background-color: #5a6268;
            color: white;
        }

        .titulo-mes {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
            margin: 0 20px;
            text-transform: uppercase;
        }

        .calendar-header {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            gap: 10px;
        }

        /* Ajuste para la columna de hora personalizada */
        .custom-time-right {
            position: absolute;
            right: 0;
            top: 0;
            width: 50px;
            background: #fff;
            border-left: 1px solid #ddd;
            z-index: 10;
            pointer-events: none; 
        }
        .custom-time-label {
            text-align: center;
            font-size: 0.85em;
            color: #666;
            border-bottom: 1px solid #eee; 
            box-sizing: border-box;
        }
    </style>
</head>

<body>

    <%-- CONTENEDOR FLEX PRINCIPAL --%>
    <div class="d-flex" id="wrapper">

    <%-- 1. SIDEBAR --%>
    <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
    
    <%-- 2. CONTENIDO DERECHO --%>
    <div id="page-content-wrapper">
        
        <%-- A. BARRA SUPERIOR --%>
        <%@ include file="/WEB-INF/jspf/topBar.jspf" %>
        
        <%-- B. CONTENIDO ESPECÍFICO DEL TABLERO --%>
        <div class="container-fluid px-4">
            
            <%-- Fila de Botones de Estado --%>
            <div class="d-flex justify-content-end flex-wrap gap-2 mb-4">
                <button class="btn-custom-teal">Confirmada</button>
                <button class="btn-custom-teal">Atendida</button>
                <button class="btn-custom-teal">Cancelada</button>
                <button class="btn-custom-teal">Pendiente</button>
            </div>
            
            <%-- Cabecera del Calendario --%>
            <div class="calendar-header bg-light p-3 rounded shadow-sm d-flex flex-wrap justify-content-between align-items-center mb-3">
                <div class="btn-group mb-2 mb-md-0">
                    <button class="btn-custom-grey" id="btnPrev"><i class="bi bi-chevron-left"></i></button>
                    <button class="btn-custom-grey" id="btnNext"><i class="bi bi-chevron-right"></i></button>
                    <button class="btn-custom-grey" id="btnToday">Hoy</button>
                </div>
                
                <div class="titulo-mes fw-bold fs-4 text-center mb-2 mb-md-0" id="currentMonth">MAYO 2025</div>
                
                <div class="btn-group">
                    <button class="btn-custom-grey" id="btnMonth">Mes</button>
                    <button class="btn-custom-grey" id="btnWeek">Semana</button>
                    <button class="btn-custom-grey" id="btnDay">Dia</button>
                </div>
            </div>
            
            <%-- Calendario --%>
            <div class="card shadow-sm mb-4">
                <div class="card-body p-0">
                    <div id="calendar"></div>
                </div>
            </div>
            
        </div> <%-- Fin container-fluid --%>
        
    </div> <%-- Fin page-content-wrapper --%>
    </div> <%-- Fin wrapper --%>

    <%-- ========================================================== --%>
    <%-- MODAL: DETALLE DE CITA (Para Acciones Rápidas)             --%>
    <%-- ========================================================== --%>
    <div class="modal fade" id="modalDetalleCita" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-light">
                    <h5 class="modal-title fw-bold text-dark">
                        <i class="bi bi-info-circle-fill text-primary me-2"></i>Detalle de Cita
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="fw-bold text-muted small">PACIENTE / MOTIVO</label>
                        <p class="fs-5 text-dark fw-bold mb-0" id="modalTitulo">Cargando...</p>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <label class="fw-bold text-muted small">HORA INICIO</label>
                            <p class="fs-6" id="modalInicio">--:--</p>
                        </div>
                        <div class="col-6">
                            <label class="fw-bold text-muted small">ESTADO ACTUAL</label>
                            <div id="modalEstadoBadge"></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <%-- Formulario AJAX para completar la cita --%>
                    <form action="citas" method="POST" id="formCompletar">
                        <input type="hidden" name="operacion" value="completar_cita">
                        <input type="hidden" name="idCita" id="inputIdCita">
                        
                        <button type="button" class="btn btn-link text-secondary text-decoration-none me-2" data-bs-dismiss="modal">Cerrar</button>
                        <button type="submit" class="btn btn-success fw-bold px-4">
                            <i class="bi bi-check-circle-fill me-2"></i>Terminar Cita
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- SCRIPTS --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        let calendar;
        
        // Función para ajustar la columna de tiempo (Visual)
        function createRightTimeColumn() {
            const existing = document.querySelector('.custom-time-right');
            if (existing) existing.remove();
            
            setTimeout(function() {
                const scrollContainer = document.querySelector('.fc-scroller-liquid-absolute');
                if (!scrollContainer) return;
                
                const slotRows = document.querySelectorAll('.fc-timegrid-slots tr');
                if (slotRows.length === 0) return;
                
                const rightColumn = document.createElement('div');
                rightColumn.className = 'custom-time-right';
                
                slotRows.forEach(function(row) {
                    const labelEl = row.querySelector('.fc-timegrid-slot-label-cushion');
                    const timeText = labelEl ? labelEl.innerText : '';
                    const rowHeight = row.offsetHeight;
                    
                    const timeDiv = document.createElement('div');
                    timeDiv.className = 'custom-time-label';
                    timeDiv.innerText = timeText;
                    timeDiv.style.height = rowHeight + 'px';
                    timeDiv.style.lineHeight = rowHeight + 'px';
                    
                    rightColumn.appendChild(timeDiv);
                });
                
                scrollContainer.style.position = 'relative'; 
                scrollContainer.appendChild(rightColumn);
            }, 100);
        }

        document.addEventListener('DOMContentLoaded', function() {
            const calendarEl = document.getElementById('calendar');
            
            calendar = new FullCalendar.Calendar(calendarEl, {
                locale: 'es',
                initialView: 'timeGridWeek',
                headerToolbar: false,
                allDaySlot: false,
                height: 'auto',
                contentHeight: 600,
                
                // Fuente de datos (Tu Servlet)
                events: 'citas?operacion=obtener_citas_json', 
                
                // === ACCIÓN AL HACER CLIC ===
                eventClick: function(info) {
                    const evento = info.event;
                    const idCita = evento.id;
                    const titulo = evento.title;
                    const fechaInicio = evento.start ? evento.start.toLocaleString() : 'Sin fecha';
                    const color = evento.backgroundColor;

                    // Llenar el Modal
                    const elTitulo = document.getElementById('modalTitulo');
                    const elInicio = document.getElementById('modalInicio');
                    const elInput = document.getElementById('inputIdCita');
                    const elBadge = document.getElementById('modalEstadoBadge');

                    if(elTitulo) elTitulo.innerText = titulo;
                    if(elInicio) elInicio.innerText = fechaInicio;
                    if(elInput) elInput.value = idCita;
                    
                    if(elBadge) {
                        elBadge.innerHTML = `<span class="badge" style="background-color: ${color}; color: #fff;">Estado</span>`;
                    }

                    // Abrir el Modal
                    const modalEl = document.getElementById('modalDetalleCita');
                    if(modalEl) {
                        const modal = new bootstrap.Modal(modalEl);
                        modal.show();
                    }
                },

                slotLabelFormat: {
                    hour: 'numeric',
                    hour12: true,
                    meridiem: 'short'
                },
                dayHeaderFormat: {
                    weekday: 'short',
                    day: 'numeric'
                },
                slotMinTime: '09:00:00',
                slotMaxTime: '18:00:00',
                expandRows: true,
                
                datesSet: function(info) {
                    const titleEl = document.getElementById('currentMonth');
                    if(titleEl) titleEl.innerText = info.view.title;
                    createRightTimeColumn();
                },
                viewDidMount: function(info) {
                    createRightTimeColumn();
                },
                windowResize: function(view) {
                    createRightTimeColumn();
                }
            });
            
            calendar.render();
            
            // Botones de navegación
            const btnPrev = document.getElementById('btnPrev');
            if(btnPrev) btnPrev.addEventListener('click', () => calendar.prev());
            
            const btnNext = document.getElementById('btnNext');
            if(btnNext) btnNext.addEventListener('click', () => calendar.next());
            
            const btnToday = document.getElementById('btnToday');
            if(btnToday) btnToday.addEventListener('click', () => calendar.today());
            
            const btnMonth = document.getElementById('btnMonth');
            if(btnMonth) btnMonth.addEventListener('click', () => {
                calendar.changeView('dayGridMonth');
                const col = document.querySelector('.custom-time-right');
                if(col) col.style.display = 'none';
            });
            
            const btnWeek = document.getElementById('btnWeek');
            if(btnWeek) btnWeek.addEventListener('click', () => calendar.changeView('timeGridWeek'));
            
            const btnDay = document.getElementById('btnDay');
            if(btnDay) btnDay.addEventListener('click', () => calendar.changeView('timeGridDay'));
            
            
            // =========================================================
            // LÓGICA AJAX: TERMINAR LA CITA Y CAMBIAR COLOR AL INSTANTE
            // =========================================================
            const formCompletar = document.getElementById('formCompletar');
            
            if (formCompletar) {
                formCompletar.addEventListener('submit', function(e) {
                    e.preventDefault(); // 1. Evita que la página se recargue

                    const datos = new URLSearchParams(new FormData(this));

                    // 2. Envia la petición al servidor
                    fetch('citas', { 
                        method: 'POST',
                        body: datos
                    })
                    .then(response => {
                        if (response.ok) {
                            // 3. ÉXITO: Ocultar Modal
                            const modalElemento = document.getElementById('modalDetalleCita');
                            const modalInstancia = bootstrap.Modal.getInstance(modalElemento);
                            modalInstancia.hide();

                            // 4. CAMBIAR COLOR VISUALMENTE (Sin recargar todo)
                            const idCita = document.getElementById('inputIdCita').value;
                            const evento = calendar.getEventById(idCita);
                            
                            if (evento) {
                                // Pinta la cita de VERDE
                                evento.setProp('backgroundColor', '#198754'); 
                                evento.setProp('borderColor', '#198754');
                                evento.setProp('textColor', '#ffffff');
                            }
                            
                        } else {
                            alert("Hubo un error al conectar con el servidor.");
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("Error técnico al completar la cita.");
                    });
                });
            }
        });
    </script>
</body>
</html>