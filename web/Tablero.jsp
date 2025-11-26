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
           (Reemplazan a doctorTablero.css)
           ========================================= */

        /* Contenedor derecho que ocupa el espacio restante */
        #page-content-wrapper {
            flex-grow: 1;
            height: 100vh;
            overflow-y: auto; /* Scroll solo en el contenido, sidebar fijo */
            background-color: #fff;
        }

        /* Estilos para los Botones Superiores (Reemplazo de btn-verde) */
        .btn-custom-teal {
            background-color: #20c997; /* Color similar al diseño */
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

        /* Estilos para los Botones de Navegación (Reemplazo de btn-gris) */
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

        /* Título del mes */
        .titulo-mes {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
            margin: 0 20px;
            text-transform: uppercase;
        }

        /* Contenedor de la cabecera del calendario */
        .calendar-header {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            gap: 10px;
        }

        /* Ajuste para la columna de hora personalizada (script) */
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

    <%-- CONTENEDOR FLEX PRINCIPAL: Sidebar a la izquierda, Contenido a la derecha --%>
    <div class="d-flex" id="wrapper">

    <%-- 1. SIDEBAR --%>
    <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
    
    <%-- 2. CONTENIDO DERECHO --%>
    <div id="page-content-wrapper">
        
        <%-- A. BARRA SUPERIOR (Siempre va primero y sola) --%>
        <%@ include file="/WEB-INF/jspf/topBar.jspf" %>
        
        <%-- B. CONTENIDO ESPECÍFICO DEL TABLERO (Con padding) --%>
        <div class="container-fluid px-4">
            
            <%-- Fila de Botones de Estado (Debajo del TopBar) --%>
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

    <%-- SCRIPTS --%>
    <script>
        let calendar;
        
        // Función para ajustar la columna de tiempo (tu lógica original)
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

                // === AQUÍ ESTÁ EL CAMBIO ===
                // Apunta al Servlet con la operación nueva
                events: 'citas?operacion=obtener_citas_json', 

                // Resto de tu configuración...
                slotLabelFormat: {
                    hour: 'numeric',
                    hour12: true,
                    meridiem: 'short'
                },
                // ... (mantén tus eventos datesSet, viewDidMount, etc.)
                datesSet: function(info) {
                    document.getElementById('currentMonth').innerText = info.view.title;
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
            
            // VINCULAR BOTONES PERSONALIZADOS
            document.getElementById('btnPrev').addEventListener('click', () => calendar.prev());
            document.getElementById('btnNext').addEventListener('click', () => calendar.next());
            document.getElementById('btnToday').addEventListener('click', () => calendar.today());
            
            document.getElementById('btnMonth').addEventListener('click', () => {
                calendar.changeView('dayGridMonth');
                // Ocultar columna extra en vista mensual
                const col = document.querySelector('.custom-time-right');
                if(col) col.style.display = 'none';
            });
            
            document.getElementById('btnWeek').addEventListener('click', () => calendar.changeView('timeGridWeek'));
            document.getElementById('btnDay').addEventListener('click', () => calendar.changeView('timeGridDay'));
        });
    </script>
</body>
</html>