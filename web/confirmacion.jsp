<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cita Registrada - Multident</title>
    
    <%-- ESTILOS GLOBALES --%>
    <%@ include file="/WEB-INF/jspf/styles.jspf" %> 
    
    <style>
        body {
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            
            /* IMAGEN DE FONDO */
            background-image: url('RESOURCES/imgs/img2.png'); /* Asegúrate que la ruta sea correcta */
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            position: relative;
        }

        /* Overlay blanco semitransparente para que el texto sea legible */
        body::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(255, 255, 255, 0.85); /* Blanco al 85% */
            z-index: 1;
        }

        /* Tarjeta Central */
        .card-confirmacion {
            position: relative;
            z-index: 2; /* Encima del overlay */
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1); /* Sombra suave */
            width: 90%;
            max-width: 420px;
            padding: 40px 30px;
            text-align: center;
            border: none; /* Sin borde gris de bootstrap */
        }

        /* Círculo del Check */
        .check-circle {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #20c997; /* Tu color Teal */
            color: white;
            font-size: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 25px; /* Centrado y margen abajo */
            box-shadow: 0 4px 10px rgba(32, 201, 151, 0.3); /* Sombra verde suave */
        }

        /* Títulos y Textos */
        h2 {
            color: #333;
            font-weight: 700;
            font-size: 1.5rem;
            margin-bottom: 15px;
        }

        p.mensaje {
            color: #666;
            font-size: 0.95rem;
            margin-bottom: 5px;
            line-height: 1.5;
        }

        hr.divider {
            margin: 25px auto;
            width: 60%;
            border: 0;
            border-top: 1px solid #eee;
        }

        /* Botón personalizado */
        .btn-continuar {
            background-color: #20c997;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 12px 0;
            font-weight: 600;
            width: 100%;
            transition: all 0.3s ease;
            font-size: 1rem;
            text-decoration: none;
            display: inline-block;
        }

        .btn-continuar:hover {
            background-color: #1aa179;
            color: white;
            transform: translateY(-2px); /* Efecto de elevación */
            box-shadow: 0 4px 12px rgba(32, 201, 151, 0.2);
        }
    </style>
</head>
<body>

    <div class="card-confirmacion">
        <!-- Icono Check -->
        <div class="check-circle">
            <i class="bi bi-check-lg"></i>
        </div>

        <!-- Título -->
        <h2>¡Cita Registrada!</h2>

        <!-- Mensajes -->
        <p class="mensaje">Su cita ha quedado agendada correctamente.</p>
        <p class="mensaje text-muted small">Hemos enviado los detalles a su correo electrónico.</p>

        <!-- Línea divisora -->
        <hr class="divider">

        <!-- Botón de Acción -->
        <%-- 
           Si es un paciente (público), probablemente quieras que vuelva al inicio (index.jsp).
           Si es un admin, quizás quieras que vuelva a la lista de citas.
           Aquí asumo que es flujo público por el diseño.
        --%>
        <a href="index.jsp" class="btn-continuar">
            Volver al Inicio
        </a>
    </div>

</body>
</html>