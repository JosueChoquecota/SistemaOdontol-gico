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
        if (operacion == null) {
            operacion = "listar_citas";
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
                case "buscar_id":
                    buscarPorId(request, response);
                    break;
                case "listar_citas":
                    listarCitas(request, response);
                    break;
                case "obtener_citas_json":
                    obtenerCitasJson(request, response);
                    break;
                default:
                    listarCitas(request, response);
                    break;
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
    private void obtenerCitasJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener tus citas normales
        List<CitaDTOResponse> lista = citaService.listAll();

        // 2. Convertirlas al formato de FullCalendar
        List<Map<String, Object>> eventos = new ArrayList<>();

        for (CitaDTOResponse c : lista) {
            Map<String, Object> evento = new HashMap<>();

            evento.put("id", c.getIdCita());

            // Título: Qué se ve en la cajita del calendario
            // Ej: "Juan Perez - Limpieza Dental"
            String titulo = "";
            if (c.getPaciente() != null) {
                titulo += c.getPaciente().getNombresPaciente() + " " + c.getPaciente().getApellidosPaciente();
            }
            titulo += " - " + c.getMotivo();
            evento.put("title", titulo);

            // Fecha y Hora de Inicio (ISO8601: "2025-11-25T10:00:00")
            // Asumimos que c.getHoraCita() devuelve algo como "10:00:00" o "10:00"
            String fechaHora = c.getFechaCita().toString() + "T" + c.getHoraCita();
            // Pequeña corrección si la hora viene sin segundos (ej: 10:00 -> 10:00:00)
            if (c.getHoraCita().length() == 5) {
                fechaHora += ":00";
            }
            evento.put("start", fechaHora);

            // (Opcional) Duración estimada 30 min si no tienes hora fin
            // FullCalendar lo calcula solo si no lo pones, pero se ve mejor si lo tiene.

            // Colores según estado
            String estado = (c.getEstado() != null) ? c.getEstado().toUpperCase() : "PENDIENTE";
            switch (estado) {
                case "ATENDIDO":
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
                    evento.put("textColor", "#000000"); // Texto negro para contraste
                    break;
            }

            eventos.add(evento);
        }

        // 3. Enviar JSON
        String json = new Gson().toJson(eventos);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    // =================================================================
    // REGISTRAR
    // =================================================================
    private void registrarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CitaDTORequest dto = new CitaDTORequest();

        try {
            // 1. IDs seleccionados en los Dropdowns
            dto.setIdPaciente(Integer.parseInt(request.getParameter("idPaciente")));
            dto.setIdTrabajador(Integer.parseInt(request.getParameter("idOdontologo")));
            
            // CORRECCIÓN CRÍTICA: Leemos el ID del Horario, NO la hora texto
            dto.setIdHorario(Integer.parseInt(request.getParameter("idHorario")));

            // 2. Fecha
            dto.setFechaCita(LocalDate.parse(request.getParameter("fechaCita")));
            
            // 3. Motivo y Observaciones
            dto.setMotivo(request.getParameter("motivo"));
            dto.setObservaciones(request.getParameter("observaciones"));
            
            // 4. Estado Inicial
            dto.setEstado("PENDIENTE");

            // 5. Llamada al Servicio
            boolean resultado = citaService.insertarCitaAdministrativa(dto);

            if (resultado) {
                request.setAttribute("mensaje", "✅ Cita registrada correctamente.");
            } else {
                request.setAttribute("error", "❌ No se pudo registrar la cita. Verifique disponibilidad.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Ver error en consola del servidor
            request.setAttribute("error", "Error de formato: Faltan datos numéricos (Paciente, Doctor u Horario).");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el sistema: " + e.getMessage());
        }

        // Recargamos la lista
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