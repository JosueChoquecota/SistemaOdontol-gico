

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Comportamiento responsivo -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Multident</title>
        <%@include  file="WEB-INF/jspf/styles.jspf" %>
        <link rel="stylesheet" href="RESOURCES/css/Home.css"/>
        <link rel="stylesheet" href="RESOURCES/css/Footer.css"/>
    </head>
    <body>
        <!-- Header -->
        <%@include  file="WEB-INF/jspf/header.jspf" %>

        <section class="layout">

             <div class="wrap">
                    <div class="wrap-text">
                        <h1>Clínica Dental <br> 
                            Multident</h1>
                        <p>Te damos la bienvenida a nuestra moderna sede de San Juan de <br>Lurigancho, un espacio diseñado pensando en tu total comodidad y <br>bienestar.</p>
                        <button type="submit" class="button">Nuestros Servicios</button>
                        <button type="submit" class="button-cita">Cita</button>
                    </div>
                </div>
            
            <main>
                <section id="servicios">
                    <div class="contenedor espaciado">
                        <h1>Nuestros Servicios</h1>
                        <p>Tenemos una amplia gama de servicios diseñados para cuidar y mejorar tu salud bucal en todas sus dimensiones. 
                            Nuestro<br>compromiso es ofrecerte opciones integrales y especializadas que abarcan desde limpiezas dentales regulares hasta <br>procedimientos avanzados de odontología estética y reconstructiva.
                            Entre nuestros servicios se encuentran:</p>
                    </div>
                </section>
            </main>

            <%@include  file="WEB-INF/jspf/footer.jspf" %>
        </section>

        <%@include  file="WEB-INF/jspf/scripts.jspf" %>
    </body>
</html>
