
import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.dtos.TrabajadorDTOResponse;
import com.utp.sistemaOdontologo.services.TrabajadorService;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class TrabajadorServiceTestPuro {
    private static final TrabajadorService service = new TrabajadorService();
    private static final int ID_TRABAJADOR_TEST = 4; // ID que debe existir en la BD
    
    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBAS CRUD POST-INSERCIÓN ---");
        
        // Ejecutar las pruebas en orden
        testListar();
        //testActualizarUsuario(ID_TRABAJADOR_TEST);

        //testEliminar(ID_TRABAJADOR_TEST);

        
        System.out.println("--- PRUEBAS FINALIZADAS ---");
    }

    // =======================================================
    // 1. LISTAR TODOS (findAll)
    // =======================================================
    private static void testListar() {
    System.out.println("\n--- TEST: Listar Trabajadores ---");
    try {
        List<TrabajadorDTOResponse> lista = service.findAll();
        
        if (lista != null && !lista.isEmpty()) {
            System.out.println("✅ ÉXITO: Se encontraron " + lista.size() + " registros.");
            
            // CORRECCIÓN: Iterar sobre la lista completa
            System.out.println("\n--- DETALLES DE LA LISTA ---");
            for (TrabajadorDTOResponse registro : lista) {
                // Asegúrate de que el nombre esté asignado en el DAO
                String nombreCompleto = registro.getNombre() + " " + registro.getApellido();

                System.out.println("----------------------------------------");
                System.out.println("   > Nombre Completo: " + nombreCompleto);
                System.out.println("   > ID: " + registro.getIdTrabajador());
                System.out.println("   > Username: " + registro.getUsuario().getUsername());
                System.out.println("   > Correo: " + registro.getContacto().getCorreo());
                System.out.println("   > Rol Asignado: " + registro.getRol());
            }
            System.out.println("--- FIN DE DETALLES ---");

        } else {
            System.err.println("❌ FALLO: La lista de trabajadores está vacía o es nula.");
        }
    } catch (Exception e) {
        System.err.println("❌ FALLO GRAVE: Error al listar. Mensaje: " + e.getMessage());
    }
}
    
    
    private static void testEliminar(int id) {
    System.out.println("\n--- TEST: Eliminar Trabajador (ID: " + id + ") ---");
    try {
        // 1. Verificar si el registro existe antes de intentar eliminarlo
        TrabajadorDTOResponse preCheck = service.findById(id);
        if (preCheck == null) {
            System.err.println("❌ ADVERTENCIA: Trabajador ID " + id + " no existe. Prueba omitida.");
            return;
        }

        // 2. EJECUTAR ELIMINACIÓN TRANSACCIONAL
        Boolean resultado = service.delete(id);

        if (resultado) {
            System.out.println("✅ ÉXITO: Eliminación transaccional completada (Trabajador, Usuario, Contacto borrados).");
            
            // 3. VERIFICACIÓN POST-ELIMINACIÓN
            TrabajadorDTOResponse postCheck = service.findById(id);
            if (postCheck == null) {
                System.out.println("✅ VERIFICACIÓN EXITOSA: La búsqueda por ID confirma la eliminación.");
            } else {
                 System.err.println("❌ VERIFICACIÓN FALLIDA: El registro AÚN EXISTE después de la eliminación.");
            }

        } else {
            System.err.println("❌ FALLO: La eliminación no tuvo éxito (Posible error de BD o Rollback ejecutado).");
        }
    } catch (Exception e) {
        System.err.println("❌ FALLO: Error durante la eliminación. Mensaje: " + e.getMessage());
    }
}
    
    private static void testActualizarUsuario(int id) {
        System.out.println("\n--- TEST: Actualizar Usuario/Contacto (Transaccional) ---");
        try {
            // 1. PREPARAR DTO con CAMBIOS
            TrabajadorDTORequest dtoUpdate = new TrabajadorDTORequest();
            
            // ID Principal (CRUCIAL para UPDATE)
            dtoUpdate.setIdTrabajador(id); 
            
            // --- DATOS A MODIFICAR ---
            
            // 1. Usuario (Username y Contraseña)
            dtoUpdate.setUsername("usuario.actualizado" + id); // <-- CAMBIO DE USERNAME
            dtoUpdate.setContrasena("NuevaClave2026!"); // <-- CAMBIO DE CLAVE (Debe hashearse)

            // 2. Contacto (Correo y Teléfono)
            dtoUpdate.setCorreo("contacto.nuevo." + id + "@updated.com"); // <-- CAMBIO DE CORREO
            dtoUpdate.setTelefono("911111111"); // <-- CAMBIO DE TELÉFONO
            dtoUpdate.setTipoContacto("EMAIL");
            dtoUpdate.setDireccion("Dirección Actualizada N°" + id);

            // 3. Datos de Trabajador (Se necesitan todos los campos FK originales)
            // NOTA: Para un test real, harías un findById primero para obtener los FKs originales
            dtoUpdate.setNombre("Carlos");
            dtoUpdate.setApellido("López"); 
            dtoUpdate.setColegiatura("777111");
            dtoUpdate.setIdTipoDocumento(1); 
            dtoUpdate.setIdRol(2);           
            dtoUpdate.setIdEspecialidad(3);  
            
            // 4. EJECUTAR UPDATE TRANSACCIONAL
            Boolean resultado = service.update(dtoUpdate);

            if (resultado) {
                System.out.println("✅ ÉXITO: Actualización transaccional completada.");
                
                // 5. VERIFICACIÓN POST-ACTUALIZACIÓN
                TrabajadorDTOResponse verif = service.findById(id);

// Verifica la consistencia del objeto antes de acceder a las FKs
if (verif.getUsuario() == null) {
    // Si la propiedad Usuario es NULL aquí, es un fallo en el Mapper o DAO.
    System.err.println("❌ ERROR: La propiedad UsuarioInfoDTO es nula después de la actualización.");
    return;
}

System.out.println("   > Nuevo Username: " + verif.getUsuario().getUsername()); // <-- DEBE SER getUsername()
System.out.println("   > Nuevo Correo: " + verif.getContacto().getCorreo());
            } else {
                System.err.println("❌ FALLO: La actualización no tuvo éxito (Rollback ejecutado).");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLO: Error durante la actualización. Mensaje: " + e.getMessage());
        }
    }

}
