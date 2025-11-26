<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Configuración del Sistema</title>
    
    <%-- ESTILOS GLOBALES --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    
    <style>
        /* Estilos específicos para igualar el diseño de la imagen */
        
        .config-header {
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
            margin-bottom: 30px;
        }
        
        .section-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
            margin-top: 10px;
        }
        
        .icon-box-teal {
            background-color: #17a2b8; /* Color Teal similar a la imagen */
            color: white;
            width: 35px;
            height: 35px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 5px;
            font-size: 1.2rem;
        }
        
        .section-title {
            font-weight: 800; /* Extra bold */
            font-size: 1.1rem;
            color: #000;
            margin: 0;
        }
        
        .form-label {
            font-weight: 700; /* Bold labels */
            font-size: 0.9rem;
            color: #000;
            margin-top: 5px;
        }
        
        .form-control {
            background-color: #fff;
            border: 1px solid #ced4da;
            border-radius: 0; /* Bordes cuadrados como en la imagen */
            padding: 10px;
            font-size: 0.9rem;
            color: #6c757d; /* Texto grisáceo */
        }
        
        /* Estilo especial para los Selects Teal */
        .form-select-teal {
            background-color: #17a2b8;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 10px;
            /* Flecha blanca personalizada */
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23ffffff' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M2 5l6 6 6-6'/%3e%3c/svg%3e");
        }
        
        .form-select-teal:focus {
            box-shadow: 0 0 0 0.25rem rgba(23, 162, 184, 0.25);
        }

        .sub-title-header {
            text-align: right;
        }
        
        .sub-title-header h5 {
            font-weight: bold;
            margin: 0;
            font-size: 1.1rem;
        }
        .sub-title-header small {
            color: #999;
        }
    </style>
</head>
<body>
    
    <div class="d-flex" id="wrapper">
        
        <%-- 1. SIDEBAR --%>
        <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>
        
        <%-- 2. CONTENIDO PRINCIPAL --%>
        <div id="page-content-wrapper" class="w-100 bg-white">
            
            <%-- A. BARRA SUPERIOR --%>
            <%@ include file="/WEB-INF/jspf/topBar.jspf" %> 

            <%-- B. CONTENIDO --%>
            <div class="container-fluid pe-5 ps-5"> 
                
                <%-- CABECERA DE TÍTULO --%>
                <div class="d-flex justify-content-between align-items-end config-header">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-sliders me-3 fs-2 text-dark"></i>
                        <h2 class="fw-bold m-0">Configuración</h2>
                    </div>
                    
                    <div class="sub-title-header d-none d-md-block">
                        <h5>Sistemas de Citas Odontologicas</h5>
                        <small>JosueChoquecota@gmail.com</small>
                    </div>
                </div>

                <form action="configuracion" method="POST">
                    <input type="hidden" name="operacion" value="guardar_configuracion">

                    <%-- ==================================================== --%>
                    <%-- SECCIÓN 1: DATOS DE USUARIO --%>
                    <%-- ==================================================== --%>
                    <div class="section-header">
                        <div class="icon-box-teal">
                            <i class="bi bi-person-vcard"></i> <%-- Icono de tarjeta --%>
                        </div>
                        <h4 class="section-title">Datos de Usuario</h4>
                    </div>

                    <div class="row g-4 mb-2">
                        <%-- Fila 1 --%>
                        <div class="col-md-2">
                            <label class="form-label">Nombre:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="nombre" value="Josue Cristian">
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Apellido:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="apellido" value="Choquecota Uruchi">
                        </div>

                        <%-- Fila 2 --%>
                        <div class="col-md-2">
                            <label class="form-label">Teléfono:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="telefono" value="978711585">
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Correo Electrónico:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="email" class="form-control" name="email" value="JosueChoquecota@gmail.com">
                        </div>

                        <%-- Fila 3 --%>
                        <div class="col-md-2">
                            <label class="form-label">RUC o DNI:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="documento" value="76472827">
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Razón social:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="razonSocial" value="Dashboard">
                        </div>

                        <%-- Fila 4 (Passwords) --%>
                        <div class="col-md-2">
                            <label class="form-label">Contraseña Actual:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="password" class="form-control" name="passActual" value="***************">
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Confirmar Contraseña:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="password" class="form-control" name="passConfirm" value="***************">
                        </div>
                    </div>


                    <%-- ==================================================== --%>
                    <%-- SECCIÓN 2: INFORMACIÓN DE NEGOCIO --%>
                    <%-- ==================================================== --%>
                    <div class="section-header">
                        <div class="icon-box-teal">
                            <i class="bi bi-buildings"></i> <%-- Icono de edificio --%>
                        </div>
                        <h4 class="section-title">Información de Negocio</h4>
                    </div>

                    <div class="row g-4 mb-2">
                        <%-- Fila 1 --%>
                        <div class="col-md-2">
                            <label class="form-label">Tipo de Negocio:</label>
                        </div>
                        <div class="col-md-4">
                            <select class="form-select form-select-teal" name="tipoNegocio">
                                <option value="Negocio" selected>Negocio</option>
                                <option value="Consultorio">Consultorio</option>
                                <option value="Clinica">Clínica</option>
                            </select>
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Dirección Física:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="direccion" value="av. Arequipa 7154">
                        </div>

                        <%-- Fila 2 --%>
                        <div class="col-md-2">
                            <label class="form-label">Horario Atención:</label>
                        </div>
                        <div class="col-md-4">
                            <div class="d-flex gap-2">
                                <select class="form-select form-select-teal w-50" name="horaEntrada">
                                    <option selected>Entrada</option>
                                    <option value="08:00">08:00 AM</option>
                                    <option value="09:00">09:00 AM</option>
                                </select>
                                <select class="form-select form-select-teal w-50" name="horaSalida">
                                    <option selected>Salida</option>
                                    <option value="18:00">06:00 PM</option>
                                    <option value="20:00">08:00 PM</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Departamento:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="departamento" value="Lima Metropolitana">
                        </div>

                        <%-- Fila 3 --%>
                        <div class="col-md-2">
                            <label class="form-label">Moneda Predet.:</label>
                        </div>
                        <div class="col-md-4">
                            <select class="form-select form-select-teal" name="moneda">
                                <option value="PEN" selected>S/ - PEN</option>
                                <option value="USD">$ - USD</option>
                            </select>
                        </div>
                        
                        <div class="col-md-2">
                            <label class="form-label">Distrito:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="distrito" value="Cercado de Lima">
                        </div>

                        <%-- Fila 4 --%>
                        <div class="col-md-2">
                            <label class="form-label">Provincia:</label>
                        </div>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="provincia" value="Lima">
                        </div>
                    </div>

                    <%-- BOTONES DE ACCIÓN (Opcional, no salen en la imagen pero son necesarios) --%>
                    <div class="row mt-4">
                        <div class="col-12 text-end">
                            <button type="submit" class="btn btn-custom-teal px-5 py-2 fw-bold">Guardar Cambios</button>
                        </div>
                    </div>

                </form>

            </div> 
        </div> 
    </div> 

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        /* Reutilizar tu estilo de botón principal */
        .btn-custom-teal {
            background-color: #17a2b8;
            color: white;
            border: none;
        }
        .btn-custom-teal:hover {
            background-color: #138496;
            color: white;
        }
    </style>
</body>
</html>