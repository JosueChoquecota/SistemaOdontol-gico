/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.dao;
import com.utp.sistemaOdontologo.entities.Cita;
import com.utp.sistemaOdontologo.entities.EstadoCita;
import com.utp.sistemaOdontologo.entities.Horario;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.entities.Trabajador;
import com.utp.sistemaOdontologo.mappers.CitaMapper;
import com.utp.sistemaOdontologo.repositories.ICitaRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class CitaDAO implements ICitaRepository {
    private HistoriaCitaDAO historiaCitaDAO = new HistoriaCitaDAO();
    
    @Override
    public Integer insert(Connection con, Cita cita) throws SQLException {
        // La tabla Citas tiene: id_paciente, id_trabajador, id_horario, id_estado, fecha, motivo, fecha_reg
        String SQL = "INSERT INTO Citas (id_paciente, id_trabajador, id_horario, id_estado, fecha, motivo, observacion) " +
                     "OUTPUT INSERTED.id_cita VALUES (?, ?, ?, ?, ?, ?,?)";
        
        Integer idCitaGenerado = null;
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            // 1. Asignar FKs
            ps.setInt(1, cita.getPaciente().getIdPaciente());
            ps.setInt(2, cita.getTrabajador().getIdTrabajador());
            ps.setInt(3, cita.getHorario().getIdHorario());
            ps.setInt(4, cita.getEstado().getIdEstado()); // El estado inicial es 'Pendiente'
            
            // 2. Asignar Datos Propios
            ps.setDate(5, java.sql.Date.valueOf(cita.getFechaCita())); // Conversión de LocalDate
            ps.setString(6, cita.getMotivo());
            ps.setString(7, "null"); // Conversión de LocalDateTime            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idCitaGenerado = rs.getInt(1);
                }
            }
        }
        
        if (idCitaGenerado == null) {
            throw new SQLException("Fallo al obtener ID de Cita generado.");
        }
        return idCitaGenerado;
    }
    @Override
    public List<Cita> listAll(Connection con) throws SQLException {
        List<Cita> lista = new ArrayList<>();

        // Tu query exacta con los ALIAS
        String sql = "SELECT " +
                     "  C.id_cita, C.fecha, C.motivo, " +
                     "  P.nombres AS pac_nom, P.apellidos AS pac_ape, P.documento AS pac_doc, " +
                     "  T.id_trabajador, T.nombre AS doc_nom, T.apellido AS doc_ape, " +
                     "  H.id_horario, H.horario_inicio, H.horario_fin, " +
                     "  E.nombre AS estado_nombre " +
                     "FROM Citas C " +
                     "INNER JOIN PacientesDatos P ON C.id_paciente = P.id_paciente " +
                     "INNER JOIN Trabajadores T ON C.id_trabajador = T.id_trabajador " +
                     "INNER JOIN Horarios H ON C.id_horario = H.id_horario " +
                     "INNER JOIN EstadoCitas E ON C.id_estado = E.id_estado";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = new Cita();

                // 1. Datos directos de la Cita
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setFechaCita(rs.getDate("fecha").toLocalDate());
                cita.setMotivo(rs.getString("motivo"));

                // 2. Llenar Objeto PACIENTE (Usando los Alias)
                PacienteDatos pac = new PacienteDatos();
                // OJO: Usamos "pac_nom", no "nombres"
                pac.setNombres(rs.getString("pac_nom")); 
                pac.setApellidos(rs.getString("pac_ape"));
                pac.setDocumento(rs.getString("pac_doc"));
                cita.setPaciente(pac);

                // 3. Llenar Objeto TRABAJADOR (Odontólogo)
                Trabajador doc = new Trabajador();
                doc.setIdTrabajador(rs.getInt("id_trabajador"));
                // OJO: Usamos "doc_nom", no "nombre"
                doc.setNombre(rs.getString("doc_nom"));
                doc.setApellido(rs.getString("doc_ape"));
                cita.setTrabajador(doc);

                // 4. Llenar Objeto HORARIO
                Horario hor = new Horario();
                hor.setIdHorario(rs.getInt("id_horario"));
                // Convertir de SQL Time a LocalTime
                hor.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
                hor.setHorarioFin(rs.getTime("horario_fin").toLocalTime());
                cita.setHorario(hor);

                // 5. Llenar Objeto ESTADO
                EstadoCita est = new EstadoCita();
                est.setNombreEstado(rs.getString("estado_nombre"));
                cita.setEstado(est);

                lista.add(cita);
            }
        }
        return lista;
    }
    
    @Override
    public Boolean delete(Connection con, Integer idCita) throws SQLException {
            // SQL para eliminar la fila principal de Citas
            String SQL = "DELETE FROM Citas WHERE id_cita = ?";

            try (PreparedStatement ps = con.prepareStatement(SQL)) {
                ps.setInt(1, idCita);
               
                int rowsAffected = ps.executeUpdate();                
                
                if (rowsAffected == 0) {
                    throw new SQLException("Error DAO: La cita con ID " + idCita + " no se encontró para eliminar.");
                }

                return rowsAffected > 0;
            }
    }
    @Override
    public Cita findById(Connection con, Integer idCita) throws SQLException {
    Cita cita = null;

        // La consulta SQL debe ser la misma que usaste en listAll, pero con un WHERE
        String SQL = "SELECT " + 
                     " c.id_cita, c.fecha, c.motivo," + // Incluye la columna de registro de tiempo
                        " p.id_paciente, p.nombres AS p_nombres, p.apellidos AS p_apellidos, " +
                        " t.id_trabajador, t.nombre AS t_nombres, t.apellido AS t_apellidos, " + 
                        " h.id_horario, h.horario_inicio, h.horario_fin, " + 
                        " e.id_estado, e.nombre AS e_descripcion " + 
                     "FROM Citas c " +
                     "INNER JOIN PacientesDatos p ON c.id_paciente = p.id_paciente " +
                     "INNER JOIN Trabajadores t ON c.id_trabajador = t.id_trabajador " +
                     "INNER JOIN Horarios h ON c.id_horario = h.id_horario " +
                     "INNER JOIN EstadoCitas e ON c.id_estado = e.id_estado " +
                     "WHERE c.id_cita = ?"; // <-- Condición de búsqueda
        
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, idCita); // Asigna el ID de la cita
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapeo del ResultSet a la Entidad Cita
                    cita = CitaMapper.toEntity(rs);
                }
            }
        }
        return cita;
    }
    @Override
    public Boolean update(Connection con, Cita cita) throws SQLException {
    String SQL = "UPDATE Citas SET " +
                 " id_paciente = ?, id_trabajador = ?, id_horario = ?, " +
                 " id_estado = ?, fecha = ?, motivo = ? " +
                 "WHERE id_cita = ?";
    
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            // 1. Asignar FKs y datos
            ps.setInt(1, cita.getPaciente().getIdPaciente()); 
            ps.setInt(2, cita.getTrabajador().getIdTrabajador());
            ps.setInt(3, cita.getHorario().getIdHorario());
            ps.setInt(4, cita.getEstado().getIdEstado());
            ps.setDate(5, java.sql.Date.valueOf(cita.getFechaCita()));
            ps.setString(6, cita.getMotivo());

            // 2. Condición WHERE
            ps.setInt(7, cita.getIdCita()); 

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Error DAO: La cita ID " + cita.getIdCita() + " no fue encontrada para actualizar.");
            }
            return rowsAffected > 0;
        }
    }

    public void updateEstado(Connection con, Integer idCita, Integer idEstado) throws SQLException {
        String SQL = "UPDATE Citas SET id_estado = ? WHERE id_cita = ? ";
        try(PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, idEstado);
            ps.setInt(2, idCita);
            ps.executeUpdate();
            
        }
    }
}
