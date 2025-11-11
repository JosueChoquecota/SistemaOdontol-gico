/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.mappers;
import java.sql.SQLException;

import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.CitaDTOResponse;
import com.utp.sistemaOdontologo.entities.Cita;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.EstadoCita;
import com.utp.sistemaOdontologo.entities.Horario;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.entities.TipoDocumento;
import com.utp.sistemaOdontologo.entities.Trabajador;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author ASUS
 */
public class CitaMapper {
    // =======================================================
    // 1. MÉTODOS DE ENTRADA (DIVISIÓN DEL REQUEST)
    // Usados por CitaService.crearNuevoPacienteTransaccional
    // =======================================================

    /**
     * Mapea datos de Contacto desde el DTORequest.
     */
    public static Contacto toContactoEntity(CitaDTORequest request) {
        Contacto contacto = new Contacto();
        // Asignación de campos de contacto
        contacto.setCorreo(request.getCorreo());
        contacto.setTelefono(request.getTelefono());
        contacto.setDireccion(request.getDireccion());
        contacto.setTipoContacto(request.getTipoContacto()); 
        return contacto;
    }

    /**
     * Mapea datos de PacienteDatos desde el DTORequest, usando el ID de Contacto generado.
     */
    public static PacienteDatos toPacienteEntity(CitaDTORequest request, Integer idContacto) {
        PacienteDatos paciente = new PacienteDatos();
        
        // Asignación de Contacto (Stub)
        Contacto contactoStub = new Contacto();
        contactoStub.setIdContacto(idContacto);
        paciente.setContacto(contactoStub);

        // Asignación de campos directos
        paciente.setNombres(request.getNombresPaciente());
        paciente.setApellidos(request.getApellidosPaciente());
        paciente.setDocumento(request.getDocumento());

        // Asignación de Tipo Documento (Stub)
        TipoDocumento tipoDocStub = new TipoDocumento();
        tipoDocStub.setIdTipoDocumento(request.getIdTipoDocumento());
        paciente.setTipoDocumento(tipoDocStub);

        return paciente;
    }
    
    // =======================================================
    // 2. MÉTODO PRINCIPAL DE CONVERSIÓN (ENTRADA A PERSISTENCIA)
    // Usado por CitaService.crearCita (Principal)
    // =======================================================

    /**
     * Convierte el DTO de Solicitud a la Entidad Cita (para la inserción final).
     */
    // CitaMapper.java

    public static Cita toEntity(CitaDTORequest request, Integer idEstadoCita) {
        if (request == null) { return null; }

        Cita cita = new Cita();

        // Si es una ACTUALIZACIÓN, el DTO debe contener el ID de la Cita
        if (request.getIdCita() != null) {
            cita.setIdCita(request.getIdCita()); 
        }

        // =================================================================
        // CORRECCIÓN y STUBBING
        // =================================================================

        // 1. Paciente (CORRECCIÓN: Inicializar el STUB antes de usarlo)
        PacienteDatos pacienteStub = new PacienteDatos();
        // Asumimos que el Service garantiza que el DTO Request tendrá el ID final (nuevo o existente)
        pacienteStub.setIdPaciente(request.getIdPaciente()); 
        cita.setPaciente(pacienteStub);

        // 2. Trabajador (Stub)
        Trabajador trabajador = new Trabajador();
        trabajador.setIdTrabajador(request.getIdTrabajador());
        cita.setTrabajador(trabajador);

        // 3. Horario (Stub)
        Horario horario = new Horario();
        horario.setIdHorario(request.getIdHorario());
        cita.setHorario(horario);

        // 4. Estado (Stub)
        EstadoCita estadoStub = new EstadoCita();
        estadoStub.setIdEstado(idEstadoCita); 
        cita.setEstado(estadoStub);

        // Datos Propios
        cita.setFechaCita(request.getFechaCita());
        cita.setMotivo(request.getMotivo());
        // La fecha de registro (fecha_reg) no se mapea, ya que la DB la maneja o solo se usa en el INSERT

        return cita;
        }

    public static Cita toEntity(ResultSet rs) throws SQLException {
        Cita cita = new Cita();

        // Mapeo de la Cita Principal
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setFechaCita(rs.getDate("fecha").toLocalDate()); 
        cita.setMotivo(rs.getString("motivo"));

        // Mapeo de Paciente (asumiendo alias p_nombres, p_apellidos en el SELECT JOIN)
        PacienteDatos paciente = new PacienteDatos();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNombres(rs.getString("p_nombres")); 
        paciente.setApellidos(rs.getString("p_apellidos"));
        cita.setPaciente(paciente);

        // Mapeo de Trabajador (asumiendo alias t_nombres en el SELECT JOIN)
        Trabajador trabajador = new Trabajador();
        trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
        trabajador.setNombre(rs.getString("t_nombres"));
        trabajador.setApellido(rs.getString("t_apellidos"));
        // Aquí puedes añadir Rol, Especialidad si el JOIN los trae
        cita.setTrabajador(trabajador);

        // Mapeo de Horario
        Horario horario = new Horario();
        horario.setIdHorario(rs.getInt("id_horario"));
        // Necesitas el inicio/fin del horario para la hora legible
        horario.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime()); 
        horario.setHorarioFin(rs.getTime("horario_fin").toLocalTime()); 
        cita.setHorario(horario); 

        // Mapeo de Estado
        EstadoCita estado = new EstadoCita();
        estado.setIdEstado(rs.getInt("id_estado"));
    estado.setNombreEstado(rs.getString("e_descripcion"));   
    cita.setEstado(estado);

        return cita;
    }
    // =======================================================
    // 3. MÉTODO DE SALIDA (ENTIDAD -> DTO RESPONSE)
    // Usado por CitaService para devolver la confirmación.
    // =======================================================

    /**
     * Convierte la entidad Cita (post-inserción) a un DTO de Respuesta.
     * Asumimos que el Service cargará el objeto Horario para obtener la hora legible.
     */
    public static CitaDTOResponse toResponseDTO(Cita cita, String horaLegible, String nombreDoctor, String nombrePaciente) {
        if (cita == null) { return null; }
        
        CitaDTOResponse response = new CitaDTOResponse();
        response.setIdCita(cita.getIdCita());
        response.setMensaje("Cita programada con éxito.");
        response.setFechaHoraRegistro(LocalDateTime.now());
        
        // Datos de la cita
        response.setFechaCita(cita.getFechaCita());
        response.setHoraCita(horaLegible); 
        response.setEstadoActual(cita.getEstado().getNombreEstado()); // Asumimos que el Service cargó el nombre del estado
        
        // Datos de las referencias
        response.setNombreOdontologo(nombreDoctor);
        response.setNombrePaciente(nombrePaciente);
        
        return response;
    }
        public static List<CitaDTOResponse> toDTOList(List<Cita> citas) {
    if (citas == null) {
        return null;
    }
    
    // El método ahora usa la sintaxis correcta para la expresión lambda de varias líneas
    return citas.stream()
        .map(cita -> {
            // Aquí, extraemos la información necesaria de la Entidad 'cita'
            String nombreDoctor = cita.getTrabajador() != null ? 
                                    cita.getTrabajador().getNombre()+ " " + cita.getTrabajador().getApellido() : 
                                    "N/A";
            
            String nombrePaciente = cita.getPaciente() != null ? 
                                      cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos() : 
                                      "N/A";
            
            // Asumimos que el objeto Horario tiene los campos inicio/fin cargados en el DAO
            String horaLegible = cita.getHorario() != null ? 
                                 cita.getHorario().getHorarioInicio() + " - " + cita.getHorario().getHorarioFin() : 
                                 "Hora Indefinida";

            // Retornamos la llamada al método auxiliar
            return toResponseDTO(
                cita, 
                horaLegible, 
                nombreDoctor, 
                nombrePaciente
            );
        })
        .collect(Collectors.toList()); 
    }
}
