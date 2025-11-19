package com.utp.sistemaOdontologo.controller;

import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.CitaDTOResponse;
import com.utp.sistemaOdontologo.services.CitaService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


public class CitaController extends HttpServlet {

    private final CitaService citaService = new CitaService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String operacion = request.getParameter("operacion"); 
        if (operacion == null) {
            operacion = "listar_citas"; // Default a listar
        }

        try {
            switch (operacion) {
                case "registrar_cita":
                    registrarCita(request, response);
                    break;
                case "actualizar_cita":
                    actualizarCita(request, response);
                    break;
                case "eliminar_cita":
                    eliminarCita(request, response);
                    break;
                case "completar_cita":
                    completarCita(request, response); // Nuevo método de negocio
                    break;
                case "buscar_id":
                    buscarPorId(request, response);
                    break;
                case "listar_citas":
                    listarCitas(request, response);
                    break;
                 case "obtener_citas_json":
                obtenerCitasJson(request, response); // Llama al método JSON
                break;
                default:
                    request.setAttribute("error", "Operación no reconocida.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/general/error_sistema.jsp").forward(request, response);
        }
    }

    // =================================================================
    // MÉTODOS DE MANEJO DE FLUJO (UPDATE, DELETE, COMPLETAR)
    // =================================================================
    private void obtenerCitasJson(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Obtener la lista de DTOs
        List<CitaDTOResponse> lista = citaService.listAll();

        String citasJson = new Gson().toJson(lista); 

        // 3. Enviar la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(citasJson);
    }
    
    private void completarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idCitaParam = request.getParameter("idCita");
        
        try {
            if (idCitaParam == null || idCitaParam.isEmpty()) {
                throw new IllegalArgumentException("El ID de la cita es requerido.");
            }
            Integer idCita = Integer.parseInt(idCitaParam);

            // LLAMADA TRANSACCIONAL AL SERVICIO
            if (citaService.completarCitaYPago(idCita)) {
                request.setAttribute("mensaje", "✅ Cita finalizada y Pago marcado como COMPLETADO.");
            } else {
                request.setAttribute("error", "❌ No se pudo completar la cita. Revisar validaciones.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al completar: " + e.getMessage());
        }
        
        listarCitas(request, response); // Mostrar el listado actualizado
    }
    
    private void actualizarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        CitaDTORequest dto = new CitaDTORequest();
        
        try {
            // 1. Mapeo del ID de Cita (CRUCIAL)
            dto.setIdCita(Integer.parseInt(request.getParameter("idCita"))); 

            // 2. Mapeo de FKs y Datos (Solo campos que se pueden actualizar)
            dto.setIdTrabajador(Integer.parseInt(request.getParameter("idTrabajador")));
            dto.setIdHorario(Integer.parseInt(request.getParameter("idHorario")));
            dto.setMotivo(request.getParameter("motivo"));
            
            // Asumimos que el input de fecha viene en formato YYYY-MM-DD
            dto.setFechaCita(LocalDate.parse(request.getParameter("fechaCita"))); 
            
            // Lógica para IDs de Paciente (Deben venir ocultos o ser obtenidos del usuario logueado)
            dto.setIdPaciente(Integer.parseInt(request.getParameter("idPaciente"))); 
            
            // 3. LLAMADA TRANSACCIONAL
            if (citaService.updateCita(dto)) {
                request.setAttribute("mensaje", "Cita ID " + dto.getIdCita() + " actualizada con éxito.");
            } else {
                request.setAttribute("error", "No se pudo actualizar la cita.");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error en la actualización: " + e.getMessage());
        }
        
        listarCitas(request, response); // Redirigir siempre a la lista
    }

    private void eliminarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Integer idCita = Integer.parseInt(request.getParameter("idCita"));
            
            // LLAMADA TRANSACCIONAL (Elimina Pago, Historial, y Cita)
            if (citaService.deleteCita(idCita)) {
                request.setAttribute("mensaje", "Cita ID " + idCita + " eliminada con éxito.");
            } else {
                request.setAttribute("error", "No se pudo eliminar la cita.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        
        listarCitas(request, response);
    }
    
    // -----------------------------------------------------------------
    // MÉTODOS DE LECTURA (DEBEN SER IMPLEMENTADOS O LLAMADOS)
    // -----------------------------------------------------------------
    private void buscarPorId(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lógica: Cargar DTO, llamar a service.findById, redirigir a detalle.jsp
        String idParam = request.getParameter("idCita");
        try {
            Integer idCita = Integer.parseInt(idParam);
            CitaDTOResponse cita = citaService.findById(idCita);
            if (cita != null) {
                request.setAttribute("citaDetalle", cita);
                request.getRequestDispatcher("/WEB-INF/citas/detalle.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Cita no encontrada.");
                listarCitas(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al buscar: " + e.getMessage());
            listarCitas(request, response);
        }
    }
    
    private void listarCitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica: llamar a service.listAll(), adjuntar al request, redirigir a listado.jsp
        try {
            request.setAttribute("listaCitas", citaService.listAll());
            request.getRequestDispatcher("/WEB-INF/citas/listado.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al listar citas: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/general/error.jsp").forward(request, response);
        }
    }
    
    // Dentro de la clase CitaController

private void registrarCita(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

    CitaDTORequest dto = new CitaDTORequest();
    
    try {
        // =======================================================
        // 1. MAPEANDO DATOS DEL PACIENTE (Bloque B del DTO)
        // =======================================================
        
        // Datos Personales (Nombre, Apellido, DNI)
        dto.setNombresPaciente(request.getParameter("nombresPaciente")); // Mapea Nombre
        dto.setApellidosPaciente(request.getParameter("apellidosPaciente")); // Mapea Apellido
        dto.setDocumento(request.getParameter("documento")); // Mapea DNI / RUC

        // Datos de Contacto (Correo, Teléfono, Dirección, Sexo)
        dto.setCorreo(request.getParameter("correo"));
        dto.setTelefono(request.getParameter("telefono"));
        dto.setDireccion(request.getParameter("direccion"));
        
        // Campos que requieren conversión o valor fijo/default
        dto.setTipoContacto("EMAIL"); // Valor fijo, asumimos que el correo es el tipo principal
        dto.setIdTipoDocumento(1);    // Valor fijo, asumimos que 1 = DNI/RUC
        dto.setIdPaciente(0);         // Indicamos al Service que debe crear el paciente

        // =======================================================
        // 2. MAPEANDO DATOS DE LA CITA (Bloque A del DTO)
        // =======================================================
        
        // Estos campos provienen de la otra parte del formulario (Odontólogo, Fecha, Hora, Motivo)
        dto.setIdTrabajador(Integer.parseInt(request.getParameter("idTrabajador")));
        dto.setIdHorario(Integer.parseInt(request.getParameter("idHorario")));
        
        // Fecha y Motivo
        dto.setFechaCita(LocalDate.parse(request.getParameter("fechaCita"))); 
        dto.setMotivo(request.getParameter("motivo")); 
        
        // Campo de Pago (Si se incluyó un método de pago, aunque sin monto)
        dto.setMetodoPago(request.getParameter("metodoPago"));
        
        // 3. Llamada al Servicio
        CitaDTOResponse res = citaService.crearCita(dto);

        if (res.getIdCita() != null) {
            request.setAttribute("mensaje", res.getMensaje());
            request.getRequestDispatcher("/confirmacion.jsp").forward(request, response);
        } else {
            request.setAttribute("error", res.getMensaje());
            request.getRequestDispatcher("/reserva.jsp").forward(request, response);
        }
        
    } catch (NumberFormatException e) {
        request.setAttribute("error", "Error de formato en ID o Fecha. " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/citas/reserva.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("error", "Error en la reserva: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/citas/reserva.jsp").forward(request, response);
    }
}

    // Métodos Wrapper (Requeridos por el Patrón del Profesor)
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override public String getServletInfo() { return "Controlador para la gestión completa de Citas."; }
}
