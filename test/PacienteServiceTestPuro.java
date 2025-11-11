
import com.utp.sistemaOdontologo.dtos.PacienteDTORequest;
import com.utp.sistemaOdontologo.dtos.PacienteDTOResponse;
import com.utp.sistemaOdontologo.entities.PacienteDatos;
import com.utp.sistemaOdontologo.services.PacienteService;
import java.time.LocalDate;
import java.util.List;


public class PacienteServiceTestPuro {
    private static final PacienteService service = new PacienteService();
    // IDs que deben existir en tu base de datos para que la prueba funcione:
    private static final int ID_PACIENTE_EXISTENTE = 7; 
    private static final int ID_CONTACTO_EXISTENTE = 27; 
    private static final int ID_TIPO_DOC_EXISTENTE = 1; 

    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBAS DE ACTUALIZACIÓN DE PACIENTE ---");
        String timestampStr = String.valueOf(System.currentTimeMillis());

        String dniDinamico = timestampStr.substring(5, 13); 

        // Ejecutar los tests
        //testBuscarPaciente(ID_PACIENTE_EXISTENTE);
        //testActualizarPaciente(ID_PACIENTE_EXISTENTE, ID_CONTACTO_EXISTENTE);
        //testEliminarPaciente(ID_PACIENTE_EXISTENTE);
        //testListarPacientes();
        testCrearPacienteExitoso(dniDinamico);
        System.out.println("--- PRUEBAS FINALIZADAS ---");
    }

    // =======================================================
    // 1. TEST DE BÚSQUEDA (READ)
    // =======================================================
    private static void testBuscarPaciente(int id) {
        System.out.println("\n[1] TEST: Buscar Paciente por ID (" + id + ")");
        try {
            // Asumimos que findById en PacienteService devuelve la entidad PacienteDatos completa
            PacienteDatos paciente = service.findById(id); 
            
            if (paciente != null) {
                System.out.println("✅ ÉXITO: Paciente encontrado. Nombre Original: " + paciente.getNombres());
            } else {
                System.err.println("❌ FALLO: El paciente ID " + id + " no fue encontrado.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO GRAVE: Error en la búsqueda. Mensaje: " + e.getMessage());
        }
    }

    // =======================================================
    // 2. TEST DE ACTUALIZACIÓN TRANSACCIONAL (UPDATE)
    // =======================================================
    private static void testActualizarPaciente(int idPaciente, int idContacto) {
        System.out.println("\n[2] TEST: Actualizar Datos de Paciente y Contacto");
        
        // 1. PREPARAR DTO con los nuevos valores y FKs
        PacienteDTORequest dtoUpdate = new PacienteDTORequest();
        
        // Claves Primarias (CRUCIALES para el WHERE en el DAO)
        dtoUpdate.setIdPaciente(idPaciente); 
        dtoUpdate.setIdContacto(idContacto); 
        
        // DATOS DE PACIENTESDATOS (Nuevos)
        dtoUpdate.setNombresPaciente("ANA MARÍA [ACTUALIZADO]"); // <-- CAMBIO
        dtoUpdate.setApellidosPaciente("RAMÍREZ [ACTUALIZADO]"); // <-- CAMBIO
        dtoUpdate.setDocumento("99998888");
        dtoUpdate.setIdTipoDocumento(ID_TIPO_DOC_EXISTENTE);
        dtoUpdate.setFechaNacimiento(LocalDate.of(1995, 5, 20)); // Nuevo dato
        dtoUpdate.setSexo("M");
        
        // DATOS DE CONTACTOS (Nuevos)
        dtoUpdate.setCorreo("ana.maria.update@email.com"); // <-- CAMBIO
        dtoUpdate.setTelefono("977777777"); // <-- CAMBIO
        dtoUpdate.setDireccion("Av. Actualizada 456");

        try {
            // 2. EJECUTAR UPDATE TRANSACCIONAL
            Boolean resultado = service.updatePaciente(dtoUpdate); // Asume que este método ya existe

            if (resultado) {
                System.out.println("✅ ÉXITO: Paciente y Contacto actualizados transaccionalmente.");
                
                // 3. VERIFICACIÓN POST-ACTUALIZACIÓN
                PacienteDatos verif = service.findById(idPaciente);
                if (verif != null && verif.getNombres().contains("[ACTUALIZADO]")) {
                    System.out.println("✅ VERIFICACIÓN: El nombre se actualizó correctamente.");
                } else {
                    System.err.println("❌ FALLO: La actualización no se reflejó en la base de datos.");
                }
            } else {
                System.err.println("❌ FALLO: El servicio devolvió FALSE. Revisar logs.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO GRAVE: Error durante la actualización. Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =======================================================
    // TEST DE ELIMINACIÓN TRANSACCIONAL
    // =======================================================
    public static void testEliminarPaciente(int idPaciente) {
        System.out.println("\n--- TEST: Eliminar Paciente y Contacto (ID: " + idPaciente + ") ---");
        
        try {
            // 1. Pre-Check: Verificar que el paciente existe antes de borrar
            PacienteDatos preCheck = service.findById(idPaciente); 
            if (preCheck == null) {
                System.err.println("❌ ADVERTENCIA: Paciente ID " + idPaciente + " no existe. Prueba omitida.");
                return;
            }
            
            // 2. EJECUTAR ELIMINACIÓN TRANSACCIONAL
            // El Service elimina PacientesDatos -> Contactos
            Boolean resultado = service.deletePaciente(idPaciente);

            if (resultado) {
                System.out.println("✅ ÉXITO: Paciente y Contacto eliminados transaccionalmente.");
                
                // 3. VERIFICACIÓN POST-ELIMINACIÓN
                PacienteDatos postCheck = service.findById(idPaciente);
                
                if (postCheck == null) {
                    System.out.println("✅ VERIFICACIÓN FINAL: El registro fue borrado de la BD.");
                } else {
                     System.err.println("❌ FALLO DE AUDITORÍA: El registro principal AÚN EXISTE.");
                }

            } else {
                System.err.println("❌ FALLO CRÍTICO: El servicio devolvió FALSE. Revisar logs de la transacción.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO GRAVE: Error durante la eliminación. Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void testListarPacientes() {
        PacienteService ser = new PacienteService();
        System.out.println("TEST: Lista pacientes");
        
        List<PacienteDTOResponse> listaPacientes = ser.listAllPacientes();
        if (listaPacientes != null && !listaPacientes.isEmpty()) {
            System.out.println("Se encontró");
            System.out.println("---------------------------");
            
            int count =0;
            for (PacienteDTOResponse paciente : listaPacientes) {
                System.out.println("ID: " + paciente.getCodigo()+
                                    "NOMBRE: " + paciente.getNombre() +
                                    "APELLIDO: " + paciente.getApellido() +
                                    "DNI: " + paciente.getDniRuc() +
                                    "Correo: " + paciente.getCorreo() +
                                    "Telefono: " + paciente.getTelefono()
                );
                count++;
                if (count >=10) break;
            }
            System.out.println("----------------------------------------------");
        } else if(listaPacientes != null && listaPacientes.isEmpty()) {
            System.out.println("LISTA VACIA");
        } else {
            System.out.println("FALLO: EL SERVIDOR DEVOLVIO NULL");
        } 
    }
private static void testCrearPacienteExitoso(String dni) {
        System.out.println("\n[1] TEST: Crear Paciente Exitoso (DNI: " + dni + ")");
        
        // 1. PREPARAR DTO
        PacienteDTORequest request = new PacienteDTORequest();
        
        // Asumiendo que el formulario de admisión pide todos estos campos
        request.setNombresPaciente("Luisa Maria");
        request.setApellidosPaciente("Torres");
        request.setDocumento(dni); // DNI único
        request.setIdTipoDocumento(1); // Ejemplo: DNI (Debe existir en la BD)
        request.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        request.setSexo("F"); // Asumiendo que 'F' o 'FEMENINO' es válido
        
        // Datos de Contacto (Para ContactoDAO)
        request.setCorreo("luisa.torres" + dni + "@test.com");
        request.setTelefono("987654321");
        request.setDireccion("Calle 5, Lote 12");

        try {
            // 2. EJECUTAR SERVICIO
            Integer idPacienteGenerado = service.insert(request);

            // 3. VERIFICACIÓN
            if (idPacienteGenerado != null && idPacienteGenerado > 0) {
                System.out.println("✅ ÉXITO: Paciente creado transaccionalmente.");
                System.out.println("   > ID Paciente Generado: " + idPacienteGenerado);
                System.out.println("   > Se crearon 2 registros (PacienteDatos + Contacto).");
            } else {
                System.err.println("❌ FALLO: La inserción devolvió NULL o 0. Rollback ejecutado.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO GRAVE: Error de sistema durante la inserción. Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
