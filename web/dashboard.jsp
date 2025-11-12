<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard</title>

    <%@ include file="/WEB-INF/jspf/styles.jspf" %>
    <style>
      body {
        margin: 0;
        font-family: 'Poppins', sans-serif;
        background-color: #f8f9fa;
        display: flex;
      }

      .sidebar {
        width: 250px;
        height: 100vh;
        background-color: #157347;
        color: white;
        position: fixed;
        top: 0;
        left: 0;
        padding-top: 20px;
      }

      .content {
        margin-left: 250px;
        padding: 40px;
        flex-grow: 1;
      }

      .content h1 {
        font-size: 2rem;
        margin-bottom: 10px;
      }

      .content p {
        color: #555;
      }
    </style>
  </head>

  <body>
    <%@ include file="/WEB-INF/jspf/sideBar.jspf" %>

    <div class="content">
      <h1>Bienvenido al Sistema Odontológico</h1>
      <p>Aquí irá tu contenido principal.</p>
    </div>
  </body>
</html>
