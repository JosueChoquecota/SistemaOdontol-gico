
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Comportamiento responsivo -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Multident</title>
        <%@include file="WEB-INF/jspf/styles.jspf" %>
        <link rel="stylesheet" href="RESOURCES/css/Formulario.css" type="text/css"/>
    </head>
    <body>

        <header class="header">

            <div class="box-logo">
                <img id="logo" src="RESOURCES/imgs/Vector.png"/>
                <h1 >Multident</h1>
            </div>

            <button class="menu-hmb">Menu</button>

            <div>

                <nav class="nav">
                    <ul class="nav-list">
                        <li><a href="#">Inicio</a></li>
                        <li><a href="#">Nosotros</a></li>
                        <li><a href="#">Cita</a></li>
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
            <div class="form">
                <form action="CitaController.do" method="POST">
                    <input type="hidden" name="operacion" value="registrar_cita">

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
                        <input type="text" name="documento" placeholder="********" required>
                        <input type="hidden" name="idTipoDocumento" value="1">
                    </div>

                    <div class="form-group">
                        <span>Telefono:</span>
                        <input type="text" name="telefono" placeholder="********" required>
                        <input type="hidden" name="tipoContacto" value="EMAIL">
                    </div>
                    <div class="form-group">
                        <span>Correo Electronico:</span>
                        <input type="text" name="correo" placeholder="Correo" required>
                    </div>

                    <div class="form-group icon-input">
                        <span>Motivo de Consulta:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-cons.png" alt="icono">
                            <select class="form-select select-custom" name="motivo" required>
                                <option value="">Seleccionar</option>
                                <option value="Dolor de muelas">Dolor de muelas</option>
                                <option value="Caries">Caries o fractura dental</option>
                                </select>
                        </div>
                    </div>

                    <div class="form-group icon-input">
                        <span>Fecha de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-fecha.png" alt="icono">
                            <input type="date" name="fechaCita" required>
                        </div>
                    </div>

                    <div class="form-group icon-input">
                        <span>Hora de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-hora.png" alt="icono">
                            <select class="form-select select-custom" name="idHorario" required>
                                <option value="">Seleccionar</option>
                                <option value="5">14:00 - 15:00</option>
                                <option value="6">15:00 - 16:00</option>
                                </select>
                        </div>
                    </div>

                    <div class="form-group icon-input">
                        <span>Escoja a su odontologo:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-odo.png" alt="icono">
                            <select class="form-select select-custom" name="idTrabajador" required>
                                <option value="">Seleccionar</option>
                                <option value="2">Dr. Josue Choquecota (ID 2)</option>
                                </select>
                        </div>
                    </div>

                    <div class="form-group icon-input">
                        <span>Metodo de pago:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-pago.png" alt="icono">
                            <select class="form-select select-custom" name="metodoPago" required>
                                <option value="">Seleccionar</option>
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
