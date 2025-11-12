<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cita Registrada</title>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %>
    
    <style>
        /* CSS Mínimo y necesario para la apariencia del diseño de la imagen */
        body {
            /* Eliminamos background-color #3b9f9e de aquí */
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: Arial, sans-serif;
            margin: 0; /* Asegura que no haya márgenes por defecto */
            overflow: hidden; /* Evita barras de desplazamiento si la imagen es muy grande */

            /* Establecer la imagen de fondo */
            background-image: url('RESOURCES/imgs/img2.png');
            background-size: cover; /* Cubrir toda la ventana */
            background-position: center center; /* Centrar la imagen */
            background-repeat: no-repeat;
            background-attachment: fixed; /* Opcional: fija la imagen al desplazarse */
            position: relative; /* Necesario para el overlay */
            z-index: 0; /* Asegura que el body tenga un z-index base */
        }

        /* Overlay blanqueado sobre la imagen de fondo */
        body::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(255, 255, 255, 0.7); /* Blanco con 70% de opacidad */
            z-index: 1; /* Por encima de la imagen de fondo, pero debajo del contenido */
        }

        .check-circle {
            width: 90px;
            height: 90px;
            border-radius: 50%;
            background-color: #26a69a; /* Color Teal/Aguamarina del check */
            color: white;
            font-size: 50px;
            font-weight: bold;
            line-height: 90px;
            margin: 0 auto 15px;
            /* Aseguramos que el círculo esté sobre el overlay */
            position: relative; 
            z-index: 3; /* Más alto que el overlay */
        }
        .card-custom {
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            position: relative; /* Para que la tarjeta esté sobre el overlay */
            z-index: 2; /* Por encima del overlay, pero debajo del check-circle si lo necesitas superpuesto */
        }
        .sub-message {
            font-size: 11px;
            color: #777;
            margin-top: 5px;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <%-- Asumimos que el CitaController pasó el DTO de respuesta en el atributo "res" --%>
    <c:set var="cita" value="${requestScope.res}"/>
    
    <div class="card card-custom" style="max-width: 400px;">
        <div class="card-body p-5 text-center">
            
            <div class="check-circle rounded-circle text-white d-flex align-items-center justify-content-center">
                &#10003;
            </div>
            
            <h2 class="h4 fw-bold mt-3 mb-4">Se ha registrado tu cita!</h2>
            
            <hr class="my-3 mx-auto" style="width: 50%;">
            
            <p class="sub-message">
                "Su cita se ha quedado planeado"
            </p>
            <p class="sub-message">
                La información se guardó correctamente.
            </p>
          
            <hr class="my-4">
            
            
            <a href="citas?operacion=listar_citas" class="btn btn-primary mt-4 w-100">
                Ver Citas Agendadas
            </a>
            
        </div>
    </div>
    
    <%-- EL DIV DE LA IMAGEN YA NO ES NECESARIO AQUÍ --%>
    <%-- <div class="imagen">
        <img src="RESOURCES/imgs/img2.png"/>
    </div> --%>

</body>
</html>