<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard del Doctor</title>
    
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.14/index.global.min.js'></script>
    
    <%@ include file="/WEB-INF/jspf/styles.jspf" %>
    <link rel="stylesheet" href="RESOURCES/css/doctorTablero.css">
    
    <style>
    </style>
</head>

<body>
    <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
    
    <div class="contenedor-header">
        <div class="bienvenida">
            <button class="mi-boton btn-verde">Help</button>
            <h2 class="saludo">Hola, Doctor</h2>
        </div>
        
        <div class="botones">
            <button class="mi-boton btn-verde">Confirmada</button>
            <button class="mi-boton btn-verde">Atendida</button>
            <button class="mi-boton btn-verde">Cancelada</button>
            <button class="mi-boton btn-verde">Pendiente</button>
        </div>
        
        <div class="fila-3">
            <div class="grupo-botones">
                <button class="mi-boton btn-gris" id="btnPrev"><</button>
                <button class="mi-boton btn-gris" id="btnNext">></button>
                <button class="mi-boton btn-gris" id="btnToday">Hoy</button>
            </div>
            <h2 class="titulo-pixel" id="currentMonth">MAYO 2025</h2>
            <div class="grupo-botones">
                <button class="mi-boton btn-gris" id="btnMonth">Mes</button>
                <button class="mi-boton btn-gris" id="btnWeek">Semana</button>
                <button class="mi-boton btn-gris" id="btnDay">Dia</button>
            </div>
        </div>
        
        <div id="calendar"></div>
    </div>

    <script>
        let calendar;
        
        // ==================================================
        // FUNCIÓN CORREGIDA: COPIA ALTURAS REALES (PIXEL PERFECT)
        // ==================================================
        function createRightTimeColumn() {
            // 1. Limpieza previa
            const existing = document.querySelector('.custom-time-right');
            if (existing) {
                existing.remove();
            }
            
            // 2. Esperar renderizado
            setTimeout(function() {
                // Contenedor donde inyectaremos la columna
                const scrollContainer = document.querySelector('.fc-scroller-liquid-absolute');
                
                if (!scrollContainer) return; // Si no existe (ej: vista mensual), salimos
                
                // Buscamos las FILAS de la tabla de tiempos original (las que contienen los slots)
                // Usamos selectores específicos de FullCalendar v6
                const slotRows = document.querySelectorAll('.fc-timegrid-slots tr');
                
                if (slotRows.length === 0) return;
                
                // Crear contenedor
                const rightColumn = document.createElement('div');
                rightColumn.className = 'custom-time-right';
                
                // 3. Iterar por cada fila original para copiar su altura
                slotRows.forEach(function(row) {
                    // Intentamos buscar el texto de la hora (ej: "9am")
                    const labelEl = row.querySelector('.fc-timegrid-slot-label-cushion');
                    const timeText = labelEl ? labelEl.innerText : ''; // Si es media hora (vacío), queda vacío
                    
                    // OBTENER ALTURA REAL COMPUTADA
                    const rowHeight = row.offsetHeight; // Esto nos da la altura exacta, sea 40px, 60px, etc.
                    
                    const timeDiv = document.createElement('div');
                    timeDiv.className = 'custom-time-label';
                    timeDiv.innerText = timeText;
                    
                    // APLICAR ALTURA
                    timeDiv.style.height = rowHeight + 'px';
                    // APLICAR LINE-HEIGHT PARA CENTRAR VERTICALMENTE
                    timeDiv.style.lineHeight = rowHeight + 'px';
                    
                    rightColumn.appendChild(timeDiv);
                });
                
                // 4. Insertar en el DOM
                // Nos aseguramos de que el contenedor tenga position relative
                scrollContainer.style.position = 'relative'; 
                scrollContainer.appendChild(rightColumn);
                
                console.log('✅ Columna derecha ajustada dinámicamente.');
                
            }, 100); // Pequeño delay para asegurar que el navegador ya pintó la tabla
        }
        
        // ==================================================
        // INICIALIZACIÓN DEL CALENDARIO
        // ==================================================
        document.addEventListener('DOMContentLoaded', function() {
            const calendarEl = document.getElementById('calendar');
            
            calendar = new FullCalendar.Calendar(calendarEl, {
                locale: 'es',
                initialView: 'timeGridWeek',
                headerToolbar: false,
                allDaySlot: false,
                
                slotLabelFormat: {
                    hour: 'numeric',
                    hour12: true,
                    meridiem: 'short'
                },
                
                dayHeaderFormat: {
                    weekday: 'short',
                    day: 'numeric'
                },
                
                height: '75vh',
                expandRows: true, // Esto estira las filas para llenar el alto
                
                slotMinTime: '09:00:00',
                slotMaxTime: '18:00:00',
                
                events: [],
                
                // Eventos del ciclo de vida
                datesSet: function(info) {
                    // Actualizar título del mes
                    // (Añade tu lógica aquí si la tenías para actualizar #currentMonth)
                    createRightTimeColumn();
                },
                
                viewDidMount: function(info) {
                    createRightTimeColumn();
                },
                
                windowResize: function(view) {
                    // Recalcular si se cambia el tamaño de la ventana
                    createRightTimeColumn();
                }
            });
            
            calendar.render();
            
            // ==================================================
            // BOTONES DE NAVEGACIÓN
            // ==================================================
            document.getElementById('btnPrev').addEventListener('click', function() {
                calendar.prev();
            });
            
            document.getElementById('btnNext').addEventListener('click', function() {
                calendar.next();
            });
            
            document.getElementById('btnToday').addEventListener('click', function() {
                calendar.today();
            });
            
            document.getElementById('btnMonth').addEventListener('click', function() {
                calendar.changeView('dayGridMonth');
                // Ocultar columna en vista de mes
                const col = document.querySelector('.custom-time-right');
                if(col) col.style.display = 'none';
            });
            
            document.getElementById('btnWeek').addEventListener('click', function() {
                calendar.changeView('timeGridWeek');
            });
            
            document.getElementById('btnDay').addEventListener('click', function() {
                calendar.changeView('timeGridDay');
            });
        });
    </script>
</body>
</html>