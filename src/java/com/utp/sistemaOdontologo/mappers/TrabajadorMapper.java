/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.mappers;

import com.utp.sistemaOdontologo.dtos.ContactoInfoDTO;
import com.utp.sistemaOdontologo.dtos.EspecialidadDTO;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
import com.utp.sistemaOdontologo.dtos.UsuarioInfoDTO;
import com.utp.sistemaOdontologo.entities.Trabajador;
import com.utp.sistemaOdontologo.entities.Usuario;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.TipoDocumento;
import com.utp.sistemaOdontologo.entities.Especialidad;
import com.utp.sistemaOdontologo.entities.enums.Rol;
import java.util.List;
import java.util.stream.Collectors;



public class TrabajadorMapper {
   // --- 1. Mapeo para CONTACTO ---
    public static Contacto toContactoEntity(TrabajadorDTORequest request) {
        Contacto contacto = new Contacto();
        // Nota: Asumimos que el DTO tiene los campos de Contacto planos.
        contacto.setTipoContacto(request.getTipoContacto());
        contacto.setCorreo(request.getCorreo());
        contacto.setTelefono(request.getTelefono());
        contacto.setDireccion(request.getDireccion());
        // El tipoContacto (ENUM) lo puedes dejar para asignarlo directamente en la entidad Contacto si es fijo ('EMAIL/PHONE')
        return contacto;
    }

    // --- 2. Mapeo para USUARIO ---
    public static Usuario toUsuarioEntity(TrabajadorDTORequest request) {
        Usuario usuario = new Usuario();
        usuario.setUsuario(request.getUsuario());
        // La CONTRASENA se pasa PLANA. El HASHING se hará en el SERVICE.
        usuario.setContrasena(request.getContrasena()); 
        // El estado y el idEmpresa serán asignados por el SERVICE.
        return usuario;
    }

    // --- 3. Mapeo para TRABAJADOR (Final) ---
    // Recibe los IDs generados por el SERVICE.
    public static Trabajador toTrabajadorEntity(TrabajadorDTORequest request, Integer idContacto, Integer idUsuario) {
        Trabajador entity = new Trabajador();

        // Mapeo de campos directos del Trabajador
        entity.setNombre(request.getNombre());
        entity.setApellido(request.getApellido());
        entity.setColegiatura(request.getColegiatura());
        entity.setFechaRegistro(request.getFechaRegistro()); 
        entity.setDocumento(request.getDocumento());
        entity.setRol(Rol.ODONTOLOGO);
        // -------------------------------------------------------------
        // Asignación de IDs GENERADOS (Stub Entities)
        // -------------------------------------------------------------
        
        // Asigna el ID del Contacto generado
        Contacto contactoStub = new Contacto();
        contactoStub.setIdContacto(idContacto);
        entity.setContacto(contactoStub);

        // Asigna el ID del Usuario generado
        Usuario usuarioStub = new Usuario();
        usuarioStub.setIdUsuario(idUsuario);
        entity.setUsuario(usuarioStub);
        
        // -------------------------------------------------------------
        // Asignación de IDs de Catálogo (Del DTO Request)
        // -------------------------------------------------------------
        
        // Tipo Documento
        TipoDocumento tipoDocStub = new TipoDocumento();
        tipoDocStub.setIdTipoDocumento(request.getIdTipoDocumento());
        entity.setTipoDocumento(tipoDocStub);

        // Especialidad (Se asume que el DTO contiene el ID de Especialidad)
        if (request.getIdEspecialidad() != null) {
            Especialidad especialidadStub = new Especialidad();
            especialidadStub.setIdEspecialidad(request.getIdEspecialidad());
            entity.setEspecialidad(especialidadStub);
        }

        // Rol (Asumiendo que el DTO contiene el ID del Rol)
        entity.setRol(Rol.fromId(request.getIdRol()));

        // El idTrabajador se deja NULL para el INSERT.
        return entity;
    }
    public static List<TrabajadorDTOResponse> toListResponseDTO(List<Trabajador> entidades) {
        if (entidades == null) {
            return null;
        }
        // Usamos Streams de Java 8 para mapear cada elemento
        return entidades.stream()
                .map(TrabajadorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Trabajador completa (con sus FKs cargadas) a un DTO de Respuesta.
     */
    public static TrabajadorDTOResponse toResponseDTO(Trabajador entity) {
    if (entity == null) {
        return null;
    }
    TrabajadorDTOResponse response = new TrabajadorDTOResponse();

    // 1. Campos directos del Trabajador (Esta parte está correcta)
    response.setIdTrabajador(entity.getIdTrabajador());
    response.setNombre(entity.getNombre());
    response.setApellido(entity.getApellido());
    response.setColegiatura(entity.getColegiatura());
    response.setRol(entity.getRol().name());
    response.setFechaRegistro(entity.getFechaRegistro());
    response.setDocumento(entity.getDocumento());

    // 2. Composición de DTOs para exponer solo la información necesaria
    
    // Mapeo de Usuario (ESTE BLOQUE FALTABA EN TU CÓDIGO)
    if (entity.getUsuario() != null) {
        // Llama al sub-mapper y asigna el resultado
        response.setUsuario(toUsuarioInfoDTO(entity.getUsuario()));
    } else {
        // Si el DAO no cargó el usuario, al menos asigna un stub vacío si el DTO lo requiere
        // Pero para el 'update' no debería ser null si la transacción fue exitosa.
        response.setUsuario(null); 
    }

    // Mapeo de Contacto (ESTE BLOQUE FALTABA EN TU CÓDIGO)
    if (entity.getContacto() != null) {
        response.setContacto(toContactoInfoDTO(entity.getContacto()));
    }

    // Mapeo de Especialidad (ESTE BLOQUE FALTABA EN TU CÓDIGO)
    if (entity.getEspecialidad() != null) {
        response.setEspecialidad(toEspecialidadDTO(entity.getEspecialidad()));
    }


    return response;
}

    // =======================================================
    // MÉTODOS AUXILIARES PARA SUB-DTOs DE INFORMACIÓN
    // =======================================================
    
    // Asumimos que esta es una clase simple de DTO de información
    public static UsuarioInfoDTO toUsuarioInfoDTO(Usuario usuario) {
        UsuarioInfoDTO dto = new UsuarioInfoDTO();
        dto.setIdUsuario(usuario.getIdUsuario());

        // CORRECTION: Use the standard getter for the username field.
        // If your Usuario entity's field is 'username', the getter should be getUsername().
        // If your entity's getter is indeed getUsuario(), then the issue is likely elsewhere.
        dto.setUsuario(usuario.getUsuario()); 

        dto.setEstado(usuario.getEstado().name()); 
        return dto;
        }

    // Asumimos que esta es una clase simple de DTO de información
    public static ContactoInfoDTO toContactoInfoDTO(Contacto contacto) {
        ContactoInfoDTO dto = new ContactoInfoDTO();
        dto.setIdContacto(contacto.getIdContacto());
        dto.setTelefono(contacto.getTelefono());
        dto.setCorreo(contacto.getCorreo());
        dto.setDireccion(contacto.getDireccion());
        // Puedes añadir más campos de contacto si es necesario
        return dto;
    }
    
    // Asumimos que esta es una clase simple de DTO de información
    public static EspecialidadDTO toEspecialidadDTO(Especialidad especialidad) {
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombre(especialidad.getNombre());
        dto.setAcronimo(especialidad.getAcronimo());
        return dto;
    }
    
}
