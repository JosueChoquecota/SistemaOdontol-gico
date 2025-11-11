/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.controller;


import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
import com.utp.sistemaOdontologo.services.TrabajadorService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;


public class TrabajadorController extends HttpServlet {
    
    private final TrabajadorService trabajadorService = new TrabajadorService();

    // -------------------------------------------------------------------------
    // Lógica Centralizada (processRequest)
    // -------------------------------------------------------------------------
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Parámetro para saber qué acción ejecutar (login, registro_trabajador, etc.)
        String operacion = request.getParameter("operacion"); 

        if (operacion == null) {
            operacion = ""; // Evita NullPointerException si no se envía la operación
        }

        switch (operacion) {
            case "registrar_trabajador":
                    registrarTrabajador(request, response);
                    break;
                case "listar_trabajadores":
                    listarTrabajadores(request, response);
                    break;
                case "buscar_id":
                    buscarPorId(request, response);
                    break;
                case "eliminar_trabajador":
                    eliminarTrabajador(request, response);
                    break;
                // Agrega el caso para actualizar si está implementado:
                case "actualizar_trabajador":
                    actualizarTrabajador(request, response);
                    break;
                default:
                    // Si la operación es desconocida, redirigir a una página de error
                    request.setAttribute("error", "Operación no reconocida: " + operacion);
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    break;
        }
    }

    // -------------------------------------------------------------------------
    // Método Auxiliar: registrarTrabajador (Toda la lógica de mapeo)
    // -------------------------------------------------------------------------
    private void registrarTrabajador(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       
}
    // Dentro de la clase TrabajadorController

    private void buscarPorId(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        try {
            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("Se requiere el ID del trabajador para buscar.");
            }

            int id = Integer.parseInt(idParam);

            // 1. Llamada al Servicio (Devuelve TrabajadorDTOResponse)
            TrabajadorDTOResponse trabajador = trabajadorService.findById(id);

            if (trabajador != null) {
                // 2. Adjuntar el objeto al request
                request.setAttribute("trabajadorAEditar", trabajador);

                // 3. Redirigir al formulario o página de edición
                request.getRequestDispatcher("/WEB-INF/trabajador/editar.jsp").forward(request, response);
            } else {
                // No encontrado
                request.setAttribute("error", "Trabajador con ID " + id + " no encontrado.");
                listarTrabajadores(request, response); // Mostrar la lista con un error
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "El ID proporcionado no es un número válido.");
            listarTrabajadores(request, response); 
        } catch (Exception e) {
            request.setAttribute("error", "Error al buscar el trabajador: " + e.getMessage());
            listarTrabajadores(request, response); 
        }
    }
    // Dentro de la clase TrabajadorController
    private void listarTrabajadores(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // 1. Llamada al Servicio (Devuelve List<TrabajadorDTOResponse>)
            List<TrabajadorDTOResponse> lista = trabajadorService.findAll();

            // 2. Adjuntar la lista al objeto request para que la JSP la lea
            request.setAttribute("listaTrabajadores", lista);

            // 3. Redirigir a la JSP del listado
            request.getRequestDispatcher("/WEB-INF/trabajador/listado.jsp").forward(request, response);

        } catch (Exception e) {
            // Manejo de errores
            request.setAttribute("error", "Error al cargar la lista de trabajadores: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/general/error.jsp").forward(request, response);
        }
    }
    private void eliminarTrabajador(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        try {
            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("El ID del trabajador es requerido para eliminar.");
            }

            int id = Integer.parseInt(idParam);

            // 1. Llamada al Servicio
            if (trabajadorService.delete(id)) {
                request.setAttribute("mensaje", "Trabajador eliminado con éxito.");
            } else {
                request.setAttribute("error", "No se pudo eliminar el trabajador. Es posible que no exista.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "El ID proporcionado para eliminar no es un número válido.");
        } catch (Exception e) {
            request.setAttribute("error", "Error en el sistema al eliminar: " + e.getMessage());
        }

        // 2. Redirigir siempre a la lista después de un POST de eliminación
        listarTrabajadores(request, response);
    }
    
    
    private void actualizarTrabajador(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
        TrabajadorDTORequest dto = new TrabajadorDTORequest();

        try {
            // --- 1. Mapeo del ID a Actualizar ---
            String idTrabajadorParam = request.getParameter("idTrabajador");
            if (idTrabajadorParam == null || idTrabajadorParam.isEmpty()) {
                 throw new IllegalArgumentException("El ID del trabajador es requerido para la actualización.");
            }
            dto.setIdTrabajador(Integer.parseInt(idTrabajadorParam));

            // --- 2. Mapeo de Parámetros (Similar a Registrar) ---
            // (AQUÍ DEBES REPLICAR TODO EL CÓDIGO DE MAPEO DE registrarTrabajador)
            // ... (Asegúrate de mapear los IDs de Usuario y Contacto si los necesitas para el update) ...

            // Ejemplo del mapeo de campos directos (DEBES REPLICAR EL DE registrarTrabajador)
            dto.setNombre(request.getParameter("nombre"));
            dto.setApellido(request.getParameter("apellido"));
            dto.setColegiatura(request.getParameter("colegiatura"));
            dto.setIdTipoDocumento(Integer.parseInt(request.getParameter("idTipoDocumento")));
            dto.setIdRol(Integer.parseInt(request.getParameter("idRol")));

            // Asume que los IDs de Usuario y Contacto vienen ocultos en el formulario de edición
            dto.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario"))); 
            dto.setIdContacto(Integer.parseInt(request.getParameter("idContacto"))); 

            dto.setUsername(request.getParameter("username"));
            dto.setContrasena(request.getParameter("contrasena")); // El Service manejará si esto es NULL/Vacío

            // -------------------------------------------------------------
            // AÑADIR ESTO: Campos de Contacto (Necesarios para el ContactoDAO.update)
            // -------------------------------------------------------------
            dto.setCorreo(request.getParameter("correo"));
            dto.setTelefono(request.getParameter("telefono"));
            dto.setDireccion(request.getParameter("direccion"));
            dto.setTipoContacto(request.getParameter("tipo_contacto"));

            // --- 3. Llamada al Servicio ---
            if (trabajadorService.update(dto)) {
                request.setAttribute("mensaje", "Trabajador actualizado con éxito.");
            } else {
                request.setAttribute("error", "No se pudo actualizar el trabajador.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error durante la actualización: " + e.getMessage());
        }

        // 4. Redirigir siempre a la lista después de un POST de actualización
        listarTrabajadores(request, response);
    }
    
    
    
    // -------------------------------------------------------------------------
    // Métodos Wrapper (Requiere el Patrón del Profesor)
    // -------------------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador para la gestión de Trabajadores y su lógica de negocio.";
    }
}