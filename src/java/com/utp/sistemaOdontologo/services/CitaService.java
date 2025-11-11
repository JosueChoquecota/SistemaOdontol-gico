package com.utp.sistemaOdontologo.services;

import com.utp.sistemaOdontologo.connection.ConnectionDataBase;
import com.utp.sistemaOdontologo.dao.CitaDAO;
import com.utp.sistemaOdontologo.dao.ContactoDAO;
import com.utp.sistemaOdontologo.dao.EstadoCitaDAO;
import com.utp.sistemaOdontologo.dao.HistoriaCitaDAO;
import com.utp.sistemaOdontologo.dao.HorarioDAO;
import com.utp.sistemaOdontologo.dao.HorarioOdontologoDAO;
import com.utp.sistemaOdontologo.dao.PacienteDAO;
import com.utp.sistemaOdontologo.dao.PagoDAO;
import com.utp.sistemaOdontologo.dao.TrabajadorDAO;
import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.CitaDTOResponse;
import com.utp.sistemaOdontologo.entities.Cita;
import com.utp.sistemaOdontologo.mappers.CitaMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class CitaService {
    // Inyección o Instanciación de DAOs
    private ConnectionDataBase dbConnection;
    private ContactoDAO contactoDAO;
    private PacienteDAO pacienteDAO;
    private HorarioOdontologoDAO horarioDAO;
    private EstadoCitaDAO estadoDAO;
    private CitaDAO citaDAO;
    private TrabajadorDAO trabajadorDAO; // Para obtener datos del doctor
    private HorarioDAO horarioInfoDAO; // Para obtener la hora legible
    private HistoriaCitaDAO historiaCitaDAO;
    private PagoDAO pagoDAO;
    private PacienteService pacienteService;
    
    public CitaService() {
        dbConnection = new ConnectionDataBase();
        contactoDAO = new ContactoDAO();
        pacienteDAO = new PacienteDAO();
        horarioDAO = new HorarioOdontologoDAO();
        estadoDAO = new EstadoCitaDAO();
        citaDAO = new CitaDAO();
        trabajadorDAO = new TrabajadorDAO();
        horarioInfoDAO = new HorarioDAO();
        historiaCitaDAO = new HistoriaCitaDAO();
        pagoDAO = new PagoDAO();
        pacienteService = new PacienteService();
    }
    
    public CitaDTOResponse crearCita(CitaDTORequest request) {
        Connection con = null;
        Integer idPacienteFinal = request.getIdPaciente();
        CitaDTOResponse response = new CitaDTOResponse();
        
        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false);

            if (idPacienteFinal == null || idPacienteFinal == 0) {
                idPacienteFinal = pacienteService.crearNuevoPacienteTransaccional(con, request);
            }
            
            Integer idEstadoInicial = estadoDAO.findIdByNombre(con, "Pendiente"); 
            if (idEstadoInicial == null) {
                throw new Exception("El estado 'Pendiente' no existe en la base de datos.");
            }

            Cita citaParaInsertar = CitaMapper.toEntity(request, idEstadoInicial);
            // Aseguramos que la FK del paciente esté correcta en la entidad Cita
            citaParaInsertar.getPaciente().setIdPaciente(idPacienteFinal); 
            
            Integer idCitaGenerado = citaDAO.insert(con, citaParaInsertar); 
            citaParaInsertar.setIdCita(idCitaGenerado); // Asigna el ID para usarlo en auditoría/respuesta
            final String OBSERVACION_INICIAL = "Cita creada exitosamente con estado 'Pendiente'.";

                        historiaCitaDAO.insert(con, 
                                               idCitaGenerado, 
                                               idEstadoInicial, 
                                               OBSERVACION_INICIAL);
            // PASO 5: REGISTRAR EL PRIMER EVENTO EN EL HISTORIAL
            final String ESTADO_PAGO_INICIAL = "PENDIENTE";
            // --- CORRECCIÓN CLAVE ---
            String metodoPagoNormalizado = request.getMetodoPago().toUpperCase();    
            pagoDAO.insert(con, idCitaGenerado, metodoPagoNormalizado, ESTADO_PAGO_INICIAL);
            con.commit(); // ÉXITO: CONFIRMA TRANSACCIÓN

  
            // Carga datos para el DTO Response (Asumimos que los DAOs tienen un método findById)
            String nombreDoctor = trabajadorDAO.findNombreById(con, request.getIdTrabajador());
            String nombrePaciente = pacienteDAO.findNombreCompletoById(con, idPacienteFinal);
            String horaLegible = horarioInfoDAO.findHoraLegibleById(con, request.getIdHorario());
            
            // Mapeo final
            response = CitaMapper.toResponseDTO(citaParaInsertar, horaLegible, nombreDoctor, nombrePaciente);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL en creación de Cita. Detalle: " + e.getMessage());
            try { 
                if (con != null) con.rollback(); 
            } catch (SQLException ex) { 
                System.err.println("Error durante el rollback: " + ex.getMessage()); 
            }
            
            // Prepara una respuesta de error
            response.setMensaje("❌ Fallo la reserva de la cita: " + e.getMessage());
        } finally {
            try { 
                if (con != null) con.close(); 
            } catch (SQLException ex) { 
                System.err.println("Error al cerrar la conexión: " + ex.getMessage()); 
            }
        }
        return response;
    }  
    public List<CitaDTOResponse> listAll() {
    Connection con = null;
    List<CitaDTOResponse> listaResponse = new ArrayList<>();
    
    try {
        con = dbConnection.getConnection(); 
        // 1. Obtener la lista de entidades Cita del DAO
        List<Cita> listaCitas = citaDAO.listAll(con); 
        // 2. Mapear cada entidad a su DTO de respuesta
        for (Cita cita : listaCitas) {
            
            String nombreDoctor = cita.getTrabajador().getNombre() + " " + cita.getTrabajador().getApellido();
            String nombrePaciente = cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos();
            String horaLegible = cita.getHorario().getHorarioInicio().toString(); 
            
            CitaDTOResponse response = CitaMapper.toResponseDTO(cita, horaLegible, nombreDoctor, nombrePaciente);
            listaResponse.add(response);
        }     
    } catch (Exception e) {
        System.err.println("❌ Error al listar las citas: " + e.getMessage());
    } finally {
        try { if (con != null) con.close(); } catch (SQLException ex) { /* log error */ }
    }
    return listaResponse;
}
    public Boolean deleteCita(Integer idCita) {

            if (idCita == null || idCita <= 0) {
            throw new IllegalArgumentException("ID de cita inválido para la eliminación.");
            }

            Connection con = null;
            Boolean exito = false;

            try {
                con = dbConnection.getConnection();
                con.setAutoCommit(false); 

                // 1. ELIMINAR REGISTROS HIJOS (Pagos, Historial)
                historiaCitaDAO.delete(con, idCita);
                pagoDAO.deleteByCitaId(con, idCita); // <-- CORRECTO: Se ejecuta antes del padre.

                // 2. ELIMINAR REGISTRO PADRE
                citaDAO.delete(con, idCita);
                
                con.commit();
                exito = true;

            } catch (Exception e) {
                System.err.println("❌ ERROR FATAL al eliminar Cita. Detalle: " + e.getMessage());
                try { if (con != null) con.rollback(); } catch (SQLException ex) { /* Log rollback */ }
            } finally {
                try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
            }
        return exito;
    }  
    public CitaDTOResponse findById(Integer idCita) {
        if (idCita == null || idCita <= 0) {
            return null;
        }

        Connection con = null;
        try {
            con = dbConnection.getConnection();
            
            // 1. Buscar la entidad principal Cita
            Cita cita = citaDAO.findById(con, idCita); 
            
            if (cita == null) {
                return null; // Cita no encontrada
            }
            
            String nombreDoctor = trabajadorDAO.findNombreById(con, cita.getTrabajador().getIdTrabajador());
            String nombrePaciente = pacienteDAO.findNombreCompletoById(con, cita.getPaciente().getIdPaciente());
            String horaLegible = horarioInfoDAO.findHoraLegibleById(con, cita.getHorario().getIdHorario());
            
            // 3. Mapeo final a DTO de respuesta
            return CitaMapper.toResponseDTO(cita, horaLegible, nombreDoctor, nombrePaciente);

        } catch (Exception e) {
            System.err.println("❌ Error al buscar cita ID " + idCita + ": " + e.getMessage());
            return null;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Log close error */ }
        }
    }
    public Boolean updateCita(CitaDTORequest request) {
        Connection con = null;

        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); 

            // 1. OBTENER LA CITA ORIGINAL (para mantener FKs y el ID de estado)
            Cita citaExistente = citaDAO.findById(con, request.getIdCita()); 

            if (citaExistente == null) {
                throw new Exception("Cita no encontrada para actualizar.");
            }

            // 3. FUSIÓN DE DATOS
            citaExistente.setMotivo(request.getMotivo());
            citaExistente.setFechaCita(request.getFechaCita());

            // B. Actualizar FKs (Doctor, Horario, etc.)
            citaExistente.getTrabajador().setIdTrabajador(request.getIdTrabajador());
            citaExistente.getHorario().setIdHorario(request.getIdHorario());

            // 4. EJECUTAR UPDATE
            citaDAO.update(con, citaExistente); 

            con.commit();
            return true;

        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL al actualizar Cita. Detalle: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { /* Log rollback */ }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
        }
    }
    
    public Boolean completarCitaYPago(Integer idCita) {
        if (idCita == null || idCita <= 0) {
            throw new IllegalArgumentException("ID de Cita inválido.");
        }

        Connection con = null;
        Boolean exito = false;

        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false); // 1. INICIA TRANSACCIÓN

            final String ESTADO_CITA_FINAL = "Completada";
            final int ID_ESTADO_CITA_FINAL = 4;
            final String ESTADO_PAGO_FINAL = "COMPLETADO";

            // 1. ACTUALIZAR ESTADO EN TABLA CITAS (A ID=4)
            // Necesitas un método en CitaDAO para actualizar solo el id_estado
            citaDAO.updateEstado(con, idCita, ID_ESTADO_CITA_FINAL);

            // 2. ACTUALIZAR ESTADO EN TABLA PAGOS (A 'COMPLETADO')
            pagoDAO.updateEstadoPago(con, idCita, ESTADO_PAGO_FINAL);

            // 3. REGISTRAR EN HISTORIAL (Auditoría)
            historiaCitaDAO.insert(con, idCita, ID_ESTADO_CITA_FINAL, "Cita finalizada y pago completado.");

            con.commit(); // CONFIRMA TRANSACCIÓN
            exito = true;

        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL al completar Cita. Detalle: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ex) { System.err.println("Rollback Error."); }
        } finally {
            try { if (con != null) con.close(); } catch (SQLException ex) { /* Cierra conexión */ }
        }
        return exito;
    }
}
