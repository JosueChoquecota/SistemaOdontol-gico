
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard | Lista de Pacientes</title>
    
    <%-- INCLUIR STYLES.JSPF AQUÍ --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 

    <style>
        /* CSS Mínimo y necesario para la barra lateral (debe ir en un archivo CSS externo para producción) */
        .bg-teal {
            /* Usando un color turquesa aproximado al diseño */
            background-color: #008080 !important; 
        }
        .active-link {
            /* Estilo para la pestaña activa (Pacientes) */
            background-color: rgba(255, 255, 255, 0.15); 
            border-radius: 5px;
        }
        #wrapper {
            /* El contenedor principal debe forzar el alto de la pantalla */
            min-height: 100vh; 
        }
        /* Esto asegura que el sidebar tenga un ancho fijo y el contenido se adapte */
        #page-content-wrapper {
            flex-grow: 1; 
            min-width: 0;
        }
    </style>
</head>
<body>
    
    <div class="d-flex" id="wrapper">
        
        <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
        
        <div id="page-content-wrapper" class="p-4 w-100">
            
            <header class="d-flex justify-content-between align-items-center mb-4">
                <button class="btn btn-sm btn-secondary">Help</button>
                <span class="user me-2">Hola, Doctor!</span>
            </header>
            
            <section class="content">
                <h1 class="mb-4">Lista de Pacientes</h1>

                <div class="card shadow-sm mb-5">
                    <div class="card-header bg-light">
                        <h3 class="h6 mb-0">Registrar Pacientes</h3>
                    </div>
                    <div class="card-body">
                        <form id="pacienteForm" action="trabajadores?operacion=registrar_paciente" method="POST"> 
                            <input type="hidden" name="operacion" value="registrar_paciente">
                            
                            <div class="row g-3">
                                
                                <%-- FILA 1: Nombre, Apellido, DNI, Teléfono --%>
                                <div class="col-md-3">
                                    <label for="nombresPaciente" class="form-label small">Nombre:</label>
                                    <input type="text" name="nombresPaciente" class="form-control" placeholder="Primer Nombre" required>
                                </div>
                                <div class="col-md-3">
                                    <label for="apellidosPaciente" class="form-label small">Apellido:</label>
                                    <input type="text" name="apellidosPaciente" class="form-control" placeholder="Apellido" required>
                                </div>
                                <div class="col-md-3">
                                    <label for="documento" class="form-label small">DNI / RUC:</label>
                                    <input type="text" name="documento" class="form-control" placeholder="Documento" required>
                                </div>
                                <div class="col-md-3">
                                    <label for="telefono" class="form-label small">Teléfono:</label>
                                    <input type="text" name="telefono" class="form-control" placeholder="Teléfono" required>
                                </div>

                                <%-- FILA 2: Correo, Sexo, Dirección, Botón --%>
                                <div class="col-md-3">
                                    <label for="correo" class="form-label small">Correo:</label>
                                    <input type="email" name="correo" class="form-control" placeholder="Correo" required>
                                </div>
                                <div class="col-md-3">
                                    <label for="sexo" class="form-label small">Sexo:</label>
                                    <select name="sexo" class="form-select" required>
                                        <option value="">Seleccionar</option>
                                        <option value="F">Femenino</option>
                                        <option value="M">Masculino</option>
                                        <option value="OTRO">Otro</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <label for="direccion" class="form-label small">Dirección:</label>
                                    <input type="text" name="direccion" class="form-control" placeholder="Dirección">
                                </div>
                                
                                <%-- Botón al final de la fila --%>
                                <div class="col-md-3 d-flex align-items-end justify-content-end">
                                    <button type="submit" class="btn btn-info w-100">Reservar</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="table-box">
                    <h3>Pacientes</h3>
                    <p>Tabla de pacientes debe ser cargada aquí.</p>
                </div>
            </section>
        </div>
    </div>
    
    <%-- INCLUIR SCRIPTS DE JS AQUÍ (Bootstrap JS) --%>
</body>
</html>