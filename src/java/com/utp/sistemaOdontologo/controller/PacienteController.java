package com.utp.sistemaOdontologo.controller;

import com.google.gson.Gson;
import com.utp.sistemaOdontologo.dtos.PacienteDTORequest;
import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.services.PacienteService;
import java.io.IOException;
import java.sql.Date; // O java.time.LocalDate según tu DTO
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // Opcional si usas web.xml
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PacienteController", urlPatterns = {"/pacientes"}) // Descomenta si usas anotaciones
public class PacienteController extends HttpServlet {

    private final PacienteService pacienteService = new PacienteService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuración para evitar problemas de caracteres (tildes, ñ)
        request.setCharacterEncoding("UTF-8");
        
        String operacion = request.getParameter("operacion");
        if (operacion == null) {
            operacion = "listar_pacientes"; // Acción por defecto
        }

        try {
            switch (operacion) {
                case "registrar_paciente":
                    registrarPaciente(request, response);
                    break;
                case "actualizar_paciente":
                    actualizarPaciente(request, response);
                    break;
                case "eliminar_paciente":
                    eliminarPaciente(request, response);
                    break;
                case "buscar_id":
                    buscarPorId(request, response);
                    break;
                case "listar_pacientes":
                    listarPacientes(request, response);
                    break;
                case "obtener_pacientes_json":
                    obtenerPacientesJson(request, response);
                    break;
                default:
                    request.setAttribute("error", "Operación no reconocida.");
                    listarPacientes(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en consola del servidor
            request.setAttribute("error", "Error inesperado en Pacientes: " + e.getMessage());
            // Redirigir a la lista para no romper la navegación
            listarPacientes(request, response);
        }
    }

    // =================================================================
    // 1. LISTAR (Muestra la tabla en Pacientes.jsp)
    // =================================================================
    private void listarPacientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<PacienteDTOResponse> lista = pacienteService.listAllPacientes();
            request.setAttribute("listaPacientes", lista);
            
            // Redirige a tu JSP principal
            request.getRequestDispatcher("Pacientes.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error al listar pacientes", e);
        }
    }

    // =================================================================
    // 2. REGISTRAR
    // =================================================================
    private void registrarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PacienteDTORequest dto = new PacienteDTORequest();

        try {
            // A. Datos Personales
            dto.setNombresPaciente(request.getParameter("nombresPaciente"));
            dto.setApellidosPaciente(request.getParameter("apellidosPaciente"));
            dto.setDocumento(request.getParameter("documento"));
            dto.setSexo(request.getParameter("sexo"));
            
            // Fecha Nacimiento (Manejo de posible null o formato incorrecto)
            String fechaNacStr = request.getParameter("fechaNacimiento");
            if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
                dto.setFechaNacimiento(LocalDate.parse(fechaNacStr)); // o LocalDate.parse(fechaNacStr)
            }

            // Tipo Documento (Por defecto 1 si no viene, o parsear del select)
            String tipoDoc = request.getParameter("idTipoDocumento");
            dto.setIdTipoDocumento(tipoDoc != null ? Integer.parseInt(tipoDoc) : 1); 

            // B. Datos de Contacto (El Service maneja la inserción en ambas tablas)
            dto.setCorreo(request.getParameter("correo"));
            dto.setTelefono(request.getParameter("telefono"));
            dto.setDireccion(request.getParameter("direccion"));

            // C. Llamada al Servicio
            Integer idGenerado = pacienteService.insert(dto);

            if (idGenerado != null && idGenerado > 0) {
                request.setAttribute("mensaje", "✅ Paciente registrado con éxito. ID: " + idGenerado);
            } else {
                request.setAttribute("error", "❌ No se pudo registrar el paciente. Intente nuevamente.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar: " + e.getMessage());
        }

        // Recargamos la lista para ver el nuevo registro
        listarPacientes(request, response);
    }

    // =================================================================
    // 3. ACTUALIZAR
    // =================================================================
    private void actualizarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PacienteDTORequest dto = new PacienteDTORequest();

        try {
            // Validar ID obligatorio para update
            String idParam = request.getParameter("idPaciente");
            if (idParam == null || idParam.isEmpty()) {
                throw new Exception("ID de paciente faltante.");
            }
            dto.setIdPaciente(Integer.parseInt(idParam));

            // Mapeo de campos a actualizar (Similar al registro)
            dto.setNombresPaciente(request.getParameter("nombresPaciente"));
            dto.setApellidosPaciente(request.getParameter("apellidosPaciente"));
            dto.setDocumento(request.getParameter("documento"));
            dto.setSexo(request.getParameter("sexo"));
            dto.setCorreo(request.getParameter("correo"));
            dto.setTelefono(request.getParameter("telefono"));
            dto.setDireccion(request.getParameter("direccion"));
            
            // Fechas y Selects
            String fechaNacStr = request.getParameter("fechaNacimiento");
            if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
                dto.setFechaNacimiento(LocalDate.parse(fechaNacStr));
            }
            String tipoDoc = request.getParameter("idTipoDocumento");
            dto.setIdTipoDocumento(tipoDoc != null ? Integer.parseInt(tipoDoc) : 1);

            // Ejecutar Update
            if (pacienteService.updatePaciente(dto)) {
                request.setAttribute("mensaje", "Paciente actualizado correctamente.");
            } else {
                request.setAttribute("error", "No se pudo actualizar el paciente.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error en actualización: " + e.getMessage());
        }

        listarPacientes(request, response);
    }

    // =================================================================
    // 4. ELIMINAR
    // =================================================================
    private void eliminarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id"); // O "idPaciente" según tu JSP
            if (idParam != null) {
                Integer idPaciente = Integer.parseInt(idParam);
                
                if (pacienteService.deletePaciente(idPaciente)) {
                    request.setAttribute("mensaje", "Paciente eliminado correctamente.");
                } else {
                    request.setAttribute("error", "No se pudo eliminar. Puede tener citas asociadas.");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        listarPacientes(request, response);
    }

    // =================================================================
    // 5. BUSCAR POR ID (Para llenar formulario de edición)
    // =================================================================
    private void buscarPorId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                Integer idPaciente = Integer.parseInt(idParam);
                
                // Nota: El service devuelve la ENTIDAD PacienteDatos, no DTO
                PacienteDatos paciente = pacienteService.findById(idPaciente);
                
                if (paciente != null) {
                    // Opción A: Enviar al JSP para editar en otra página
                    // request.setAttribute("pacienteEditar", paciente);
                    // request.getRequestDispatcher("paciente-editar.jsp").forward(request, response);
                    
                    // Opción B (Recomendada si usas Modals): Devolver JSON
                    String json = new Gson().toJson(paciente);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                    return; // Detenemos aquí para no redirigir
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }
        // Si falla o no es JSON, volvemos a la lista
        listarPacientes(request, response);
    }

    // =================================================================
    // 6. UTILIDAD JSON (Para AJAX o Datatables)
    // =================================================================
    private void obtenerPacientesJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<PacienteDTOResponse> lista = pacienteService.listAllPacientes();
        String json = new Gson().toJson(lista);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    // Wrappers estándar
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override public String getServletInfo() { return "Controlador de Pacientes"; }
}