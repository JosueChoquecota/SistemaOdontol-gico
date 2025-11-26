<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Multident - Reserva</title>
        <%@include file="WEB-INF/jspf/styles.jspf" %>
        <link rel="stylesheet" href="RESOURCES/css/Formulario.css" type="text/css"/>
    </head>
    <body>

        <header class="header">
            <div class="box-logo">
                <img id="logo" src="RESOURCES/imgs/Vector.png"/>
                <h1>Multident</h1>
            </div>
            <button class="menu-hmb">Menu</button>
            <div>
                <nav class="nav">
                    <ul class="nav-list">
                        <li><a href="#">Inicio</a></li>
                        <li><a href="#">Nosotros</a></li>
                        
                        <%-- 
                           🔴 AQUÍ ESTÁ LA SOLUCIÓN:
                           El enlace apunta al Servlet ("citas"), NO al archivo JSP.
                           Al hacer clic aquí, el Controller busca los datos y recarga la página.
                        --%>
                        <li><a href="citas?operacion=vista_reserva">Cita</a></li>
                        
                        <li><a href="#">Sede</a></li>
                    </ul>
                </nav>
            </div>
            <button type="submit" class="boton">Contacto</button>
            <div class="text"> 
                <h1>Agenda tu atención con nosotros!</h1>
                <p>Ingresa tu información para separar tu cita. Recibirás <br> 
                    la información por correo o teléfono.</p>
            </div>
        </header>

        <div class="container">
            
            <%-- VALIDACIÓN VISUAL: Si las listas están vacías, avisamos al usuario --%>
            <c:if test="${empty listaHorarios or empty listaOdontologos}">
                <div class="alert alert-warning m-3 text-center">
                    <strong>⚠️ Aviso:</strong> Los horarios y doctores no se han cargado.<br>
                    Por favor, <a href="citas?operacion=vista_reserva" class="alert-link">haz clic aquí para recargar el formulario correctamente</a>.
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger m-3">${error}</div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success m-3">${mensaje}</div>
            </c:if>

            <div class="form">
                <form action="citas" method="POST">
                    <input type="hidden" name="operacion" value="registrar_cita">
                    <%-- Campo para saber que venimos del formulario público --%>
                    <input type="hidden" name="origen" value="publico">

                    <div class="form-group">
                        <span>Nombre:</span>
                        <input type="text" name="nombresPaciente" placeholder="Primer Nombre" required>
                    </div>
                    <div class="form-group">
                        <span>Apellido:</span>
                        <input type="text" name="apellidosPaciente" placeholder="Primer Apellido" required>
                    </div>
                    <div class="form-group">
                        <span>DNI / Documento de identidad:</span>
                        <input type="text" name="documento" placeholder="********" required maxlength="15">
                        <input type="hidden" name="idTipoDocumento" value="1">
                    </div>

                    <div class="form-group">
                        <span>Telefono:</span>
                        <input type="text" name="telefono" placeholder="********" required>
                        <input type="hidden" name="tipoContacto" value="EMAIL">
                    </div>
                    <div class="form-group">
                        <span>Correo Electronico:</span>
                        <input type="email" name="correo" placeholder="Correo" required>
                    </div>

                    <%-- MOTIVO --%>
                    <div class="form-group icon-input">
                        <span>Motivo de Consulta:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-cons.png" alt="icono">
                            <select class="form-select select-custom" name="motivo" required>
                                <option value="" selected disabled>Seleccionar</option>
                                <option value="Dolor de muelas">Dolor de muelas</option>
                                <option value="Caries">Caries o fractura dental</option>
                                <option value="Ortodoncia">Ortodoncia</option>
                                <option value="Limpieza">Limpieza Dental</option>
                                <option value="Consulta General">Consulta General</option>
                            </select>
                        </div>
                    </div>

                    <%-- FECHA --%>
                    <div class="form-group icon-input">
                        <span>Fecha de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-fecha.png" alt="icono">
                            <input type="date" name="fechaCita" required>
                        </div>
                    </div>

                    <%-- HORARIO (CARGADO DESDE BD) --%>
                    <div class="form-group icon-input">
                        <span>Hora de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-hora.png" alt="icono">
                            <select class="form-select select-custom" name="idHorario" required>
                                <option value="" selected disabled>Seleccionar Horario</option>
                                
                                <c:forEach var="h" items="${listaHorarios}">
                                    <option value="${h.idHorario}">
                                        ${h.horarioInicio} - ${h.horarioFin}
                                    </option>
                                </c:forEach>
                                
                            </select>
                        </div>
                    </div>

                    <%-- ODONTÓLOGO (CARGADO DESDE BD) --%>
                    <div class="form-group icon-input">
                        <span>Escoja a su odontologo:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-odo.png" alt="icono">
                            <select class="form-select select-custom" name="idOdontologo" required>
                                <option value="" selected disabled>Seleccionar</option>
                                
                                <c:forEach var="doc" items="${listaOdontologos}">
                                    <option value="${doc.idTrabajador}">
                                        Dr. ${doc.nombre} ${doc.apellido} (${doc.especialidad != null ? doc.especialidad.nombre : 'General'})
                                    </option>
                                </c:forEach>
                                
                            </select>
                        </div>
                    </div>

                    <div class="form-group icon-input">
                        <span>Metodo de pago:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-pago.png" alt="icono">
                            <select class="form-select select-custom" name="metodoPago" required>
                                <option value="" selected disabled>Seleccionar</option>
                                <option value="YAPE">Yape</option>
                                <option value="TARJETA">Tarjeta (Visa/MC)</option>
                                <option value="EFECTIVO">Efectivo</option>
                            </select>
                        </div>
                    </div>

                    <label class="check"> 
                        <input type="checkbox" name="recibirNotificacion" value="true">
                        Recibir notificacion por: Correo / WhatsApp.
                    </label>

                    <div class="btn">
                        <button type="submit" class="btnprimary">Enviar</button>
                    </div>

                </form>
            </div>
        </div>

        <div class="imagen">
            <img src="RESOURCES/imgs/img2.png"/>
        </div>

    </body>
</html>