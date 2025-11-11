package com.utp.sistemaOdontologo.entities;
import java.time.LocalDate;

public class PacienteDatos {
    private Integer idPaciente;
    private TipoDocumento tipoDocumento;
    private Contacto contacto;
    private Usuario usuario;
    private String documento;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String nombres;
    private String apellidos;

    public PacienteDatos() {
    }

    public PacienteDatos(Integer idPaciente, TipoDocumento tipoDocumento, Contacto contacto, Usuario usuario, String documento, LocalDate fechaNacimiento, String sexo, String nombres, String apellidos) {
        this.idPaciente = idPaciente;
        this.tipoDocumento = tipoDocumento;
        this.contacto = contacto;
        this.usuario = usuario;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
