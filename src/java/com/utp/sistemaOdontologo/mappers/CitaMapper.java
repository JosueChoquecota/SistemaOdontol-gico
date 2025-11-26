/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.mappers;
import java.sql.SQLException;

import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.CitaDTOResponse;
import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
import com.utp.sistemaOdontologo.dtos.UsuarioInfoDTO;
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
    // 1. MAPEO DE BASE DE DATOS (DAO -> ENTIDAD)
    // =======================================================
    public static Cita toEntity(ResultSet rs) throws SQLException {
        Cita cita = new Cita();

        // 1. Cita Principal
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setFechaCita(rs.getDate("fecha").toLocalDate());
        cita.setMotivo(rs.getString("motivo"));

        // 2. Paciente (Usando los Alias del DAO: pac_nom, pac_ape)
        PacienteDatos paciente = new PacienteDatos();
        // Verifica si tu objeto Paciente usa setIdPaciente o setIdUsuario
        paciente.setIdPaciente(rs.getInt("id_paciente")); 
        paciente.setNombres(rs.getString("pac_nom"));     // <--- Alias corregido
        paciente.setApellidos(rs.getString("pac_ape"));   // <--- Alias corregido
        paciente.setDocumento(rs.getString("pac_doc"));   // <--- Alias corregido
        cita.setPaciente(paciente);

        // 3. Trabajador (Usando los Alias del DAO: doc_nom, doc_ape)
        Trabajador trabajador = new Trabajador();
        trabajador.setIdTrabajador(rs.getInt("id_trabajador"));
        trabajador.setNombre(rs.getString("doc_nom"));    // <--- Alias corregido
        trabajador.setApellido(rs.getString("doc_ape"));  // <--- Alias corregido
        cita.setTrabajador(trabajador);

        // 4. Horario
        Horario horario = new Horario();
        horario.setIdHorario(rs.getInt("id_horario"));
        horario.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
        horario.setHorarioFin(rs.getTime("horario_fin").toLocalTime());
        cita.setHorario(horario);

        // 5. Estado
        EstadoCita estado = new EstadoCita();
        // Verifica si la columna es id_estado o id_estado_cita en tu BD
        // estado.setIdEstado(rs.getInt("id_estado")); 
        estado.setNombreEstado(rs.getString("estado_nombre")); // <--- Alias corregido
        cita.setEstado(estado);

        return cita;
    }

    // =======================================================
    // 2. ENTIDAD -> DTO RESPONSE (Para el JSP)
    // =======================================================
    public static CitaDTOResponse toResponseDTO(Cita entity) {
        if (entity == null) return null;

        CitaDTOResponse dto = new CitaDTOResponse();
        
        // Datos Planos
        dto.setIdCita(entity.getIdCita());
        dto.setMotivo(entity.getMotivo());
        dto.setFechaCita(entity.getFechaCita());
        
        if (entity.getEstado() != null) {
            dto.setEstado(entity.getEstado().getNombreEstado()); // o getNombreEstado()
        }

        // Hora Legible
        if(entity.getHorario() != null && entity.getHorario().getHorarioInicio() != null) {
            dto.setHoraCita(entity.getHorario().getHorarioInicio().toString());
        } else {
            dto.setHoraCita("--:--");
        }

        // --- ANIDACIÓN DE OBJETOS (CRUCIAL PARA EL JSP) ---

        // Paciente
        if(entity.getPaciente() != null) {
            // Usamos tu DTO de Paciente existente
            PacienteDTOResponse p = new PacienteDTOResponse();
            
            // NOTA: El JSP usa ${c.paciente.nombre}.
            // Si tu PacienteDTOResponse tiene setNombresPaciente, asegúrate 
            // de que tenga un getter getNombre() o actualiza el JSP a ${c.paciente.nombresPaciente}
            p.setNombresPaciente(entity.getPaciente().getNombres());
            p.setApellidosPaciente(entity.getPaciente().getApellidos());
            p.setDocumento(entity.getPaciente().getDocumento());
            
            dto.setPaciente(p); 
        }

        // Odontólogo
        if(entity.getTrabajador() != null) {
            TrabajadorDTOResponse doc = new TrabajadorDTOResponse();
            doc.setNombre(entity.getTrabajador().getNombre());
            doc.setApellido(entity.getTrabajador().getApellido());
            
            dto.setOdontologo(doc); 
        }

        return dto;
    }

    // =======================================================
    // 3. LISTA ENTIDADES -> LISTA DTOs
    // =======================================================
    public static List<CitaDTOResponse> toDTOList(List<Cita> citas) {
        if (citas == null) return null;
        
        // Esta es la forma correcta y simple. Llama al método de arriba automáticamente.
        return citas.stream()
                .map(CitaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =======================================================
    // 4. DTO REQUEST -> ENTIDAD (Para Insertar)
    // =======================================================
    public static Cita toEntity(CitaDTORequest request, Integer idEstadoCita) {
        if (request == null) return null;

        Cita cita = new Cita();
        if (request.getIdCita() != null) {
            cita.setIdCita(request.getIdCita());
        }

        // Paciente (Solo ID para insertar)
        PacienteDatos paciente = new PacienteDatos();
        paciente.setIdPaciente(request.getIdPaciente());
        cita.setPaciente(paciente);

        // Trabajador
        Trabajador trabajador = new Trabajador();
        trabajador.setIdTrabajador(request.getIdTrabajador());
        cita.setTrabajador(trabajador);

        // Horario
        Horario horario = new Horario();
        horario.setIdHorario(request.getIdHorario());
        cita.setHorario(horario);

        // Estado
        EstadoCita estado = new EstadoCita();
        estado.setIdEstado(idEstadoCita);
        cita.setEstado(estado);

        // Datos
        cita.setFechaCita(request.getFechaCita());
        cita.setMotivo(request.getMotivo());

        return cita;
    }
}