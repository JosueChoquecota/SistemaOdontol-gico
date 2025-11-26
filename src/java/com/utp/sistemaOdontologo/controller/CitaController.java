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
import com.utp.sistemaOdontologo.dao.HorarioDAO;
import com.utp.sistemaOdontologo.services.PacienteService;
import com.utp.sistemaOdontologo.services.TrabajadorService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CitaController extends HttpServlet {

    // 1. INSTANCIAS DE LOS SERVICIOS NECESARIOS
    private final CitaService citaService = new CitaService();
    private final PacienteService pacienteService = new PacienteService();
    private final TrabajadorService trabajadorService = new TrabajadorService();
    private final HorarioDAO horarioDAO = new HorarioDAO(); // Para cargar el dropdown

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String operacion = request.getParameter("operacion");
        if (operacion == null) operacion = "listar_citas";

        try {
            switch (operacion) {
                case "registrar_cita": registrarCita(request, response); break;
                case "actualizar_cita": actualizarCita(request, response); break;
                case "eliminar_cita": eliminarCita(request, response); break;
                
                // CASO CLAVE PARA AJAX
                case "completar_cita": completarCita(request, response); break;
                
                case "buscar_id": buscarPorId(request, response); break;
                case "listar_citas": listarCitas(request, response); break;
                case "vista_reserva": mostrarFormularioReserva(request, response); break;
                
                // CASO CLAVE PARA CALENDARIO
                case "obtener_citas_json": obtenerCitasJson(request, response); break; 
                
                default: listarCitas(request, response); break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            listarCitas(request, response);
        }
    }

    // =================================================================
    // LISTAR (CARGA DE TABLA Y DROPDOWNS)
    // =================================================================
    private void listarCitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Listas principales
            request.setAttribute("listaCitas", citaService.listAll());
            request.setAttribute("listaPacientes", pacienteService.listAllPacientes());
            request.setAttribute("listaOdontologos", trabajadorService.findAll());
            
            // 2. Lista de horarios para el dropdown
            request.setAttribute("listaHorarios", horarioDAO.findAll());

            request.getRequestDispatcher("Citas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("Citas.jsp").forward(request, response);
        }
    }
    private void mostrarFormularioReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Cargar la lista de Horarios Disponibles
            request.setAttribute("listaHorarios", horarioDAO.findAll());
            
            // 2. Cargar la lista de Doctores (Odontólogos)
            request.setAttribute("listaOdontologos", trabajadorService.findAll());
            
            // 3. Redirigir al JSP del formulario (Asegúrate del nombre correcto del archivo)
            // Supongamos que tu archivo se llama "reserva.jsp" o está en una carpeta
            request.getRequestDispatcher("/Formulario.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "No se pudo cargar el formulario.");
            // Redirige a error o index
        }
    }
    // =========================================================
    // JSON PARA FULLCALENDAR
    // =========================================================
    private void obtenerCitasJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<CitaDTOResponse> lista = citaService.listAll();
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (CitaDTOResponse c : lista) {
            Map<String, Object> evento = new HashMap<>();
            
            // IMPORTANTE: ID como String para que JS lo encuentre
            evento.put("id", String.valueOf(c.getIdCita())); 
            
            String titulo = "";
            if (c.getPaciente() != null) {
                // Ajusta según tu DTO (getNombresPaciente o getNombre)
                titulo += c.getPaciente().getNombresPaciente(); 
            }
            titulo += " - " + c.getMotivo();
            evento.put("title", titulo);
            
            // Fecha y Hora
            String fechaHora = c.getFechaCita().toString() + "T" + c.getHoraCita();
            if (c.getHoraCita().length() == 5) fechaHora += ":00";
            evento.put("start", fechaHora);
            
            // Colores
            String estado = (c.getEstado() != null) ? c.getEstado().toUpperCase() : "PENDIENTE";
            switch (estado) {
                case "ATENDIDO":
                case "COMPLETADA": // Verifica cómo lo guardas en BD (ID 4)
                    evento.put("backgroundColor", "#198754"); // Verde
                    evento.put("borderColor", "#198754");
                    break;
                case "CANCELADO":
                    evento.put("backgroundColor", "#dc3545"); // Rojo
                    evento.put("borderColor", "#dc3545");
                    break;
                case "PENDIENTE":
                default:
                    evento.put("backgroundColor", "#ffc107"); // Amarillo
                    evento.put("borderColor", "#ffc107");
                    evento.put("textColor", "#000000");
                    break;
            }
            eventos.add(evento);
        }

        String json = new Gson().toJson(eventos);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    // =========================================================
    // COMPLETAR CITA (AJAX)
    // =========================================================
    private void completarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer idCita = Integer.parseInt(request.getParameter("idCita"));
            if (citaService.completarCitaYPago(idCita)) {
                // Responde OK (200) sin cuerpo, suficiente para AJAX
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo completar");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // =================================================================
    // REGISTRAR
    // =================================================================
    private void registrarCita(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        CitaDTORequest dto = new CitaDTORequest();
        
        try {
            // ==================================================================
            // 1. LOGICA HÍBRIDA: ¿PACIENTE NUEVO O EXISTENTE?
            // ==================================================================
            String idPacienteParam = request.getParameter("idPaciente");

            if (idPacienteParam != null && !idPacienteParam.isEmpty()) {
                // CASO A: Viene del Panel Administrativo (Seleccionó un paciente existente)
                dto.setIdPaciente(Integer.parseInt(idPacienteParam));
            } else {
                // CASO B: Viene del Formulario Público (Es un paciente nuevo)
                dto.setIdPaciente(0); // Marcamos con 0 para que el servicio sepa que debe crearlo
                
                // Capturamos los datos personales del formulario
                dto.setNombresPaciente(request.getParameter("nombresPaciente"));
                dto.setApellidosPaciente(request.getParameter("apellidosPaciente"));
                dto.setDocumento(request.getParameter("documento"));
                dto.setTelefono(request.getParameter("telefono"));
                dto.setCorreo(request.getParameter("correo"));
                dto.setTipoContacto("EMAIL"); 
                dto.setIdTipoDocumento(1); // Valor por defecto (DNI)
            }

            // ==================================================================
            // 2. DATOS COMUNES DE LA CITA
            // ==================================================================
            dto.setIdTrabajador(Integer.parseInt(request.getParameter("idOdontologo")));
            dto.setIdHorario(Integer.parseInt(request.getParameter("idHorario")));
            dto.setFechaCita(LocalDate.parse(request.getParameter("fechaCita")));
            dto.setMotivo(request.getParameter("motivo"));
            dto.setMetodoPago(request.getParameter("metodoPago")); // Puede venir null si es admin
            
            // Observaciones es opcional, validamos null para evitar errores en BD si no lo maneja
            String obs = request.getParameter("observaciones");
            dto.setObservaciones(obs != null ? obs : ""); 
            
            dto.setEstado("PENDIENTE");

            // ==================================================================
            // 3. LLAMADA AL SERVICIO
            // ==================================================================
            boolean exito = false;
            
            if (dto.getIdPaciente() == 0) {
                // Flujo Público: El servicio 'crearCita' crea Paciente + Cita
                CitaDTOResponse res = citaService.crearCita(dto);
                exito = (res.getIdCita() != null);
            } else {
                // Flujo Admin: El servicio 'insertarCitaAdministrativa' solo crea Cita
                exito = citaService.insertarCitaAdministrativa(dto);
            }

            // ==================================================================
            // 4. RESPUESTA Y REDIRECCIÓN
            // ==================================================================
            if (exito) {
                request.setAttribute("mensaje", "✅ Cita registrada correctamente.");
                
                // Si vino del formulario público, redirigimos a la confirmación o limpiamos
                if (idPacienteParam == null) { 
                     request.getRequestDispatcher("/confirmacion.jsp").forward(request, response);
                     return; // Importante para no ejecutar el listarCitas de abajo
                }
            } else {
                request.setAttribute("error", "❌ No se pudo registrar. Verifique disponibilidad.");
                if (idPacienteParam == null) {
                     request.getRequestDispatcher("/Formulario.jsp").forward(request, response);
                     return;
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de formato: Faltan datos numéricos.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el sistema: " + e.getMessage());
        }
        
        // Por defecto (si es admin), volvemos a la lista
        listarCitas(request, response);
    }

    // =================================================================
    // ACTUALIZAR
    // =================================================================
    private void actualizarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CitaDTORequest dto = new CitaDTORequest();

        try {
            dto.setIdCita(Integer.parseInt(request.getParameter("idCita")));
            dto.setIdPaciente(Integer.parseInt(request.getParameter("idPaciente")));
            dto.setIdTrabajador(Integer.parseInt(request.getParameter("idOdontologo")));
            
            // CORRECCIÓN AQUÍ TAMBIÉN
            dto.setIdHorario(Integer.parseInt(request.getParameter("idHorario")));
            
            dto.setFechaCita(LocalDate.parse(request.getParameter("fechaCita")));
            dto.setMotivo(request.getParameter("motivo"));
            dto.setObservaciones(request.getParameter("observaciones"));
            dto.setEstado(request.getParameter("estado"));

            if (citaService.updateCita(dto)) {
                request.setAttribute("mensaje", "Cita actualizada con éxito.");
            } else {
                request.setAttribute("error", "No se pudo actualizar.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar: " + e.getMessage());
        }

        listarCitas(request, response);
    }

    // =================================================================
    // ELIMINAR / CANCELAR
    // =================================================================
    private void eliminarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer idCita = Integer.parseInt(request.getParameter("id"));
            if (citaService.deleteCita(idCita)) {
                request.setAttribute("mensaje", "Cita cancelada/eliminada.");
            } else {
                request.setAttribute("error", "Error al eliminar.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }
        listarCitas(request, response);
    }

    // Método auxiliar para rellenar el modal de edición
    private void buscarPorId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            CitaDTOResponse cita = citaService.findById(id);
            request.setAttribute("citaEditar", cita);
            listarCitas(request, response);
        } catch (Exception e) {
            listarCitas(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}