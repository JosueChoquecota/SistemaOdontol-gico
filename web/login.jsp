
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Comportamiento responsivo -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>JSP Page</title>
        <%@include  file="WEB-INF/jspf/styles.jspf" %>
        <link rel="stylesheet" href="RESOURCES/css/login.css"/>
    </head>
    <body>
        <section class="login-section" >
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-6 offset-lg-3">
                        <div class="card">
                            <div class="card-body">

                                <div class="login-form">
                                    <h2 class="login-title">Login</h2>
                                    <p class="card-comt">Necesitas cuenta verificada para acceder</p>
                                    <hr>
                                    <form action="UsuarioController" method="POST">
                                        <input type="hidden" name="operacion" value="login"/>

                                        <div class="mb-3">
                                            <label for="correo" class="form-label">Correo</label>
                                            <input type="text" class="form-control" name="username" id="username" placeholder="Ingresa tu usuario">
                                        </div>

                                        <div class="mb-3">
                                            <label for="clave" class="form-label">Contraseña</label>
                                            <input type="password" class="form-control" id="contrasena" name="contrasena" placeholder="***********">
                                        </div>
                                        <p class="forgot">Olvidaste tu contraseña?</p>

                                        
                                        <div class="btns">
                                            <label class="recordar"> <input type="checkbox" id="recordar">Recordar</label>
                                            <button type="submit" class="btn btn-primary">Ingresar</button>
                                        </div>

                                    </form>

                                </div>

                                <div class="imglogin">
                                    <img  src="RESOURCES/imgs/imglogin.png">
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
        </section>

        <!-- Header -->
        <% 
            String error = (String) request.getAttribute("error_login");
            if (error != null) {
        %>
            <div class="alert alert-danger" role="alert">
                <strong>¡Error de Sesión!</strong> <%= error %>
            </div>
        <%
            }
        %>
        <%@include  file="WEB-INF/jspf/scripts.jspf" %>
    </body>
</html>
