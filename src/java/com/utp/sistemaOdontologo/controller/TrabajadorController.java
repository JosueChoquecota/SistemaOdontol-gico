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

        String operacion = request.getParameter("operacion"); 

        if (operacion == null || operacion.isEmpty()) {
            operacion = "listar_trabajadores"; 
        }

        try {
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
                case "actualizar_trabajador":
                    actualizarTrabajador(request, response);
                    break;
                default:
                    listarTrabajadores(request, response);
                    break;
            }
        } catch (Exception e) {
            // Si ocurre un error GRAVE que no fue capturado en los métodos individuales
            e.printStackTrace(); 
            request.setAttribute("error", "Error crítico en el servidor: " + e.getMessage());
            // Solo intentamos listar si la respuesta no ha sido enviada aún
            if (!response.isCommitted()) {
                listarTrabajadores(request, response);
            }
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD (Lógica de Negocio + Redirección Única)
    // -------------------------------------------------------------------------
    
    private void listarTrabajadores(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        try {
            List<TrabajadorDTOResponse> lista = trabajadorService.findAll();
            request.setAttribute("listaTrabajadores", lista);
            request.getRequestDispatcher("Trabajadores.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("❌ Error FATAL en listarTrabajadores (Posible error en JSP): " + e.getMessage());
            e.printStackTrace();
            // No volvemos a llamar a forward aquí para evitar bucles infinitos
        }
    }

    private void registrarTrabajador(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        TrabajadorDTORequest dto = new TrabajadorDTORequest();
        Integer idTrabajadorGenerado = null;
        
        try {
            // 1. Mapeo de Datos
            dto.setNombre(request.getParameter("nombre"));
            dto.setApellido(request.getParameter("apellido"));
            dto.setColegiatura(request.getParameter("colegiatura"));
            dto.setIdTipoDocumento(Integer.parseInt(request.getParameter("idTipoDocumento")));
            dto.setIdRol(Integer.parseInt(request.getParameter("idRol")));
            dto.setDocumento(request.getParameter("documento"));
            
            String idEsp = request.getParameter("idEspecialidad");
            if (idEsp != null && !idEsp.isEmpty() && !idEsp.equals("0")) {
                dto.setIdEspecialidad(Integer.parseInt(idEsp));
            }

            dto.setCorreo(request.getParameter("correo"));
            dto.setTelefono(request.getParameter("telefono"));
            dto.setDireccion("Desconocida"); 
            dto.setTipoContacto("EMAIL"); 
            
            dto.setUsuario(request.getParameter("username"));
            dto.setContrasena(request.getParameter("contrasena"));
            dto.setFechaRegistro(LocalDate.now());
            
            // 2. Servicio
            idTrabajadorGenerado = trabajadorService.insert(dto);

            if (idTrabajadorGenerado != null && idTrabajadorGenerado > 0) {
                request.setAttribute("mensaje", "✅ Trabajador registrado con ID: " + idTrabajadorGenerado);
            } else {
                request.setAttribute("error", "❌ No se pudo registrar.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar: " + e.getMessage());
        }
        
        // 3. Redirección Final (Fuera del try-catch)
        listarTrabajadores(request, response);
    }

    private void buscarPorId(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        try {
            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("ID requerido.");
            }
            int id = Integer.parseInt(idParam);
            TrabajadorDTOResponse trabajador = trabajadorService.findById(id);

            if (trabajador != null) {
                // Enviamos el objeto para activar el MODAL en el JSP
                request.setAttribute("trabajadorEditar", trabajador);
            } else {
                request.setAttribute("error", "Trabajador no encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al buscar: " + e.getMessage());
        }
        
        // 3. Redirección Final (Fuera del try-catch)
        listarTrabajadores(request, response); 
    }

    private void actualizarTrabajador(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
        TrabajadorDTORequest dto = new TrabajadorDTORequest();

        try {
            String idTrabajadorParam = request.getParameter("idTrabajador");
            if (idTrabajadorParam == null || idTrabajadorParam.isEmpty()) {
                 throw new IllegalArgumentException("ID de trabajador faltante.");
            }
            dto.setIdTrabajador(Integer.parseInt(idTrabajadorParam));

            // Mapeo de Datos
            dto.setNombre(request.getParameter("nombre"));
            dto.setApellido(request.getParameter("apellido"));
            dto.setColegiatura(request.getParameter("colegiatura"));
            dto.setIdTipoDocumento(Integer.parseInt(request.getParameter("idTipoDocumento")));
            dto.setIdRol(Integer.parseInt(request.getParameter("idRol")));
            dto.setDocumento(request.getParameter("documento"));

            String idEsp = request.getParameter("idEspecialidad");
            if (idEsp != null && !idEsp.isEmpty()) {
                dto.setIdEspecialidad(Integer.parseInt(idEsp));
            }

            // IMPORTANTE: Mapear IDs ocultos de Usuario y Contacto si tu JSP los envía
            // dto.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
            // dto.setIdContacto(Integer.parseInt(request.getParameter("idContacto")));

            dto.setUsuario(request.getParameter("username"));
            
            String pass = request.getParameter("contrasena");
            if(pass != null && !pass.trim().isEmpty()) {
                dto.setContrasena(pass);
            }

            dto.setCorreo(request.getParameter("correo"));
            dto.setTelefono(request.getParameter("telefono"));

            if (trabajadorService.update(dto)) { 
                request.setAttribute("mensaje", "Trabajador actualizado con éxito.");
            } else {
                request.setAttribute("error", "No se pudo actualizar el trabajador.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error actualización: " + e.getMessage());
        }

        // 3. Redirección Final (Fuera del try-catch)
        listarTrabajadores(request, response);
    }

    private void eliminarTrabajador(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        try {
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                if (trabajadorService.eliminarTrabajador(id)) {
                    request.setAttribute("mensaje", "Trabajador eliminado.");
                } else {
                    request.setAttribute("error", "No se pudo eliminar.");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        
        // 3. Redirección Final (Fuera del try-catch)
        listarTrabajadores(request, response);
    }
    
    // -------------------------------------------------------------------------
    // Métodos Wrapper
    // -------------------------------------------------------------------------
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    @Override public String getServletInfo() { return "Controlador Trabajadores"; }
}