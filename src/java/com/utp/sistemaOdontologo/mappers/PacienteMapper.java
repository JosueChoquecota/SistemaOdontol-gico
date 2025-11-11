/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.mappers;

import com.utp.sistemaOdontologo.dtos.PacienteDTORequest;
import com.utp.sistemaOdontologo.entities.Contacto;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.entities.TipoDocumento;

/**
 *
 * @author ASUS
 */
public class PacienteMapper {
    // 1. Mapeo para Contacto
public static Contacto toContactoEntity(PacienteDTORequest request) {
    Contacto contacto = new Contacto();
    contacto.setCorreo(request.getCorreo());
    contacto.setTelefono(request.getTelefono());
    contacto.setDireccion(request.getDireccion());
    // Asigna el tipoContacto si es necesario, o usa un valor por defecto
    return contacto;
}

// 2. Mapeo para PacienteDatos
public static PacienteDatos toPacienteEntity(PacienteDTORequest request, Integer idContacto) {
    PacienteDatos paciente = new PacienteDatos();
    
    // Asignar Contacto (Stub)
    Contacto contactoStub = new Contacto();
    contactoStub.setIdContacto(idContacto);
    paciente.setContacto(contactoStub);
    TipoDocumento tipoDocStub = new TipoDocumento();
    
    // 2. Asignar el ID del DTO al stub
    // Asumo que tu DTO tiene getidTipoDocumento()
    tipoDocStub.setIdTipoDocumento(request.getIdTipoDocumento()); 
    
    // 3. Asignar el stub a la entidad principal
    paciente.setTipoDocumento(tipoDocStub);
    // Asignar Tipo Documento (Stub)
    // Asumes que el DTO trae idTipoDocumento.
    // TipoDocumento tipoDocStub = new TipoDocumento();
    // tipoDocStub.setIdTipoDocumento(request.getIdTipoDocumento());
    // paciente.setTipoDocumento(tipoDocStub);

    // Asignar Datos Personales
    paciente.setNombres(request.getNombresPaciente());
    paciente.setApellidos(request.getApellidosPaciente());
    paciente.setDocumento(request.getDocumento());
    paciente.setSexo(request.getSexo()); // Si usa String
    // paciente.setFechaNacimiento(request.getFechaNacimiento()); // Si el DTO lo incluye
    
    return paciente;
}
}
