import com.utp.sistemaOdontologo.dtos.CitaDTORequest;
import com.utp.sistemaOdontologo.dtos.CitaDTOResponse;
import com.utp.sistemaOdontologo.services.CitaService;
import com.utp.sistemaOdontologo.services.PacienteService;
import java.time.LocalDate;
import java.util.List;


public class CitaServiceTestPuro {
    private static final CitaService service = new CitaService();
    // Las variables de tiempo/ID se quedan sin inicialización compleja
    private static Integer ID_CITA_CREADA = null; 
    private static final int ID_TRABAJADOR_TEST = 2;
    private static final int ID_HORARIO_TEST = 6;
    private static final LocalDate FECHA_TEST = LocalDate.of(2025, 11, 7); 
    
    public static void main(String[] args) {
        System.out.println("--- INICIANDO TEST DE RESERVACIÓN TRANSACCIONAL ---");
        String timestampStr = String.valueOf(System.currentTimeMillis());
        // Generación dinámica del DNI dentro del main
        String dniDinamico = timestampStr.substring(5, 13); 
        
        //testCrearCitaNuevoPaciente(FECHA_TEST, dniDinamico);
        //testListarCitas();
        //testActualizarCita(6);
        //testEliminarCita(9);
        //testCompletarCita(10);

        System.out.println("--- PRUEBAS DE CITA FINALIZADAS ---");
    }
    private static void testCompletarCita(int idCita) {
    System.out.println("\n[4] TEST: Finalizar Cita y Pago (ID Cita: " + idCita + ")");
    
    try {
        // 1. Pre-Check (Opcional): Verificar el estado inicial del pago (debe ser PENDIENTE)
        
        // 2. EJECUTAR EL SERVICIO TRANSACCIONAL
        Boolean resultado = service.completarCitaYPago(idCita);

        if (resultado) {
            System.out.println("✅ ÉXITO: Cita y Pago actualizados a 'COMPLETADO'.");
            
            // 3. VERIFICACIÓN POST-ACTUALIZACIÓN
            // Usamos findById para leer el estado de vuelta (el Service debe traer el estado)
            CitaDTOResponse verif = service.findById(idCita);
            
            if (verif != null && "Completada".equalsIgnoreCase(verif.getEstadoActual())) {
                System.out.println("✅ VERIFICACIÓN EXITOSA: Estado de Cita cambiado a 'Completada'.");
                // Nota: El PagoDAO debe ser verificado para confirmar el estado 'COMPLETADO'
            } else {
                System.err.println("❌ FALLO DE ESTADO: La cita no reflejó el estado 'Completada'. Estado actual: " + (verif != null ? verif.getEstadoActual() : "N/A"));
            }
        } else {
            System.err.println("❌ FALLO CRÍTICO: El servicio devolvió FALSE. Rollback ejecutado.");
        }
    } catch (IllegalArgumentException e) {
        System.err.println("❌ FALLO: Error de pre-condición: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("❌ FALLO FATAL: Error de sistema durante la finalización. Mensaje: " + e.getMessage());
        e.printStackTrace();
    }
}
    private static void testCrearCitaNuevoPaciente(LocalDate fecha, String dni) {
        System.out.println("\n[1] TEST: Creación Cita y Historial (DNI: " + dni + ")");
        
        CitaDTORequest request = new CitaDTORequest();
        
        // --- A. DATOS DE LA CITA ---
        request.setIdTrabajador(ID_TRABAJADOR_TEST);
        request.setIdHorario(ID_HORARIO_TEST);
        request.setFechaCita(fecha);
        request.setMotivo("hola");
        
        // --- B. DATOS DEL PACIENTE (Nuevo) ---
        request.setIdPaciente(0);
        request.setNombresPaciente("Alex Test");
        request.setApellidosPaciente("Uruchi");
        request.setDocumento(dni); // DNI único generado
        request.setIdTipoDocumento(1); // DNI/Cédula
        
        // --- C. DATOS DE CONTACTO Y PAGO ---
        request.setCorreo("Alex_" + dni + "@test.com");
        request.setTelefono("970711585");
        request.setTipoContacto("EMAIL");
        request.setMetodoPago("TARJETA");
        
        try {
            // 2. EJECUTAR EL SERVICIO
            CitaDTOResponse response = service.crearCita(request);

            // ... (Resto de la verificación) ...
            
        } catch (Exception e) {
            System.err.println("❌ FALLO: Error de sistema o validación. Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void testListarCitas() {
        PacienteService serv = new PacienteService();
    System.out.println("\n[2] TEST: Listado de Citas (listAll)");

    try {
        // 1. Ejecutar el servicio de listado
        List<CitaDTOResponse> listaCitas = service.listAll();
        
        // 2. Verificación y reporte
        if (listaCitas != null && !listaCitas.isEmpty()) {
            System.out.println("✅ ÉXITO: Se encontraron " + listaCitas.size() + " citas.");
            System.out.println("------------------------------------------------------------------");
            
            // Imprimir detalles de las 3 primeras citas para verificación
            int count = 0;
            for (CitaDTOResponse cita : listaCitas) {
                System.out.println("   > ID: " + cita.getIdCita() + 
                                   " | Doctor: " + cita.getNombreOdontologo() + 
                                   " | Paciente: " + cita.getNombrePaciente() +
                                   " | Fecha: " + cita.getFechaCita() +
                                   " | Hora: " + cita.getHoraCita() +
                                   " | Estado: " + cita.getEstadoActual());
                count++;
                if (count >= 3) break; // Limitar la salida para no saturar la consola
            }
            System.out.println("------------------------------------------------------------------");
            
        } else if (listaCitas != null && listaCitas.isEmpty()) {
            System.out.println("⚠️ ATENCIÓN: La lista está vacía. No se encontró ninguna cita en la BD.");
        } else {
            System.err.println("❌ FALLO: El servicio devolvió NULL.");
        }
        
    } catch (Exception e) {
        System.err.println("❌ FALLO FATAL al listar. Mensaje: " + e.getMessage());
        e.printStackTrace();
    }
    
    } 
    
    private static void testActualizarCita(int idCita) {
    System.out.println("\n--- TEST: Actualizar Cita (Transaccional) ---");
    // 1. Preparar DTO con CAMBIOS
    CitaDTORequest dtoUpdate = new CitaDTORequest();
    // Campos CRUCIALES para el UPDATE
    dtoUpdate.setIdCita(idCita); 
    dtoUpdate.setIdTrabajador(ID_TRABAJADOR_TEST); 
    dtoUpdate.setIdHorario(ID_HORARIO_TEST); 
    // DATOS DE CAMBIO
    dtoUpdate.setFechaCita(LocalDate.of(2025, 11, 4)); 
    dtoUpdate.setMotivo("Actualizado"); 
    
    dtoUpdate.setIdPaciente(7); 
    try {
        Boolean resultado = service.updateCita(dtoUpdate);
        if (resultado) {
            System.out.println("✅ ÉXITO: Cita ID " + idCita + " actualizada correctamente.");
            // 3. VERIFICACIÓN POST-ACTUALIZACIÓN
            CitaDTOResponse verif = service.findById(idCita);
            if (verif != null && verif.getMotivo() != null && verif.getMotivo().contains("Actualizado")) {
                    System.out.println("✅ VERIFICACIÓN: El nuevo motivo se ha persistido.");
                } else {
                    String motivoActual = (verif != null) ? verif.getMotivo() : "Objeto Nulo";
                    System.err.println("❌ FALLO: El update se registró pero la verificación falló. Motivo actual: " + motivoActual);
                }
        } else {
            System.err.println("❌ FALLO: La actualización no tuvo éxito (Rollback ejecutado o ID no encontrado).");
        }
    } catch (Exception e) {
        System.err.println("❌ FALLO GRAVE: Error durante la actualización. Mensaje: " + e.getMessage());
    }
}
    private static void testEliminarCita(int idCita) {
    System.out.println("\n--- TEST: Eliminar Cita Transaccional (ID: " + idCita + ") ---");
    
        try {
            // 1. Pre-Check: Verificar que la cita existe antes de borrar
            CitaDTOResponse preCheck = service.findById(idCita);
            if (preCheck == null) {
                System.err.println("❌ ADVERTENCIA: Cita ID " + idCita + " no existe o fue eliminada previamente. Prueba omitida.");
                return;
            }

            System.out.println("   > Eliminando cita de: " + preCheck.getNombrePaciente() + " con Dr. " + preCheck.getNombreOdontologo());

            // 2. EJECUTAR ELIMINACIÓN TRANSACCIONAL
            // El servicio debe encargarse de borrar HistoriaCita y Pagos primero.
            Boolean resultado = service.deleteCita(idCita);

            if (resultado) {
                System.out.println("✅ ÉXITO: Eliminación transaccional completada.");

                // 3. VERIFICACIÓN POST-ELIMINACIÓN
                CitaDTOResponse postCheck = service.findById(idCita);

                if (postCheck == null) {
                    System.out.println("✅ VERIFICACIÓN EXITOSA: El registro principal ya no se encuentra.");
                } else {
                     System.err.println("❌ FALLO DE AUDITORÍA: El registro principal AÚN EXISTE.");
                }

            } else {
                System.err.println("❌ FALLO CRÍTICO: La eliminación no tuvo éxito (Se ejecutó ROLLBACK).");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO: Error de sistema o de integridad al eliminar. Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

}