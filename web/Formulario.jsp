
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
                <form action="" method="POST">
                    <div class="form-group">
                        <span>Nombre:</span>
                        <input type="text" name="name" placeholder="Primer Nombre">
                    </div>
                    <div class="form-group">
                        <span>Apellido:</span>
                        <input type="text" name="name" placeholder="Primer Apellido">
                    </div>
                    <div class="form-group">
                        <span>DNI / Documento de identidad:</span>
                        <input type="text" name="name" placeholder="********">
                    </div>
                    <div class="form-group">
                        <span>Telefono:</span>
                        <input type="text" name="name" placeholder="********">
                    </div>
                    <div class="form-group">
                        <span>Correo Electronico:</span>
                        <input  type="text" name="name" placeholder="Correo">
                    </div>
                    <div class="form-group icon-input">
                        <span>Motivo de Consulta:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-cons.png" alt="icono">
                            <select class="form-select select-custom">
                                <option value="Options">Seleccionar</option>
                                <option value="dolor_muela">Dolor de muelas</option>
                                <option value="caries">Caries o fractura dental</option>
                                <option value="revision">Revisión o limpieza dental</option>
                                <option value="sangrado_encías">Sangrado de encías</option>
                                <option value="ortodoncia">Ortodoncia / Brackets</option>
                                <option value="blanqueamiento">Blanqueamiento dental</option>
                                <option value="extraccion">Extracción dental</option>
                                <option value="protesis">Prótesis o coronas</option>
                            </select>

                        </div> 
                    </div>
                    <div class="form-group icon-input">
                        <span>Fecha de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-fecha.png" alt="icono">
                            <input  type="date" name="name" placeholder="Motivo 1">
                        </div> 
                    </div>

                    <div class="form-group icon-input">
                        <span>Hora de Cita:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-hora.png" alt="icono">
                            <input  type="time" name="name" placeholder="Opcion 1">
                        </div> 
                    </div>

                    <div class="form-group icon-input">
                        <span>Escoja a su odontologo:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-odo.png" alt="icono">
                            <select class="form-select select-custom">
                                <option value="Options">Seleccionar</option>
                                <option value="ramirez">Dr. Ramírez</option>
                                <option value="torres">Dr. Torres</option>
                                <option value="huaman">Dr. Huaman</option>
                                <option value="salazar">Dr. Salazar</option>
                            </select>

                        </div> 
                    </div>
                    <div class="form-group icon-input">
                        <span>Metodo de pago:</span>
                        <div class="icon-wrapper">
                            <img src="RESOURCES/imgs/icono-pago.png" alt="icono">
                            <select class="form-select select-custom">
                                <option value="Options">Seleccionar</option>
                                <option value="yape">Yape</option>
                                <option value="plin">Plin</option>
                                <option value="bcp">Tran.BCP</option>
                                <option value="interbank">Tran.IBK</option>
                                <option value="visa">Visa</option>
                                <option value="mastercard">Mastercard</option>
                            </select>


                        </div> 
                    </div>

                    <label class="check"> <input type="checkbox">Recibir notificacion por: Correo / WhatsApp.</label>

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
