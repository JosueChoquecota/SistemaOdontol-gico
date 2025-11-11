/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.utp.sistemaOdontologo.dtos.TrabajadorDTORequest;
import com.utp.sistemaOdontologo.services.TrabajadorService;
import java.time.LocalDate;


public class TrabajadorServiceTest {

   public static void main(String[] args) {
        System.out.println("--- Iniciando Test de Inserción Transaccional (Java Puro) ---");
        
        // 1. Instanciar el servicio (la capa de negocio)
        TrabajadorService trabajadorService = new TrabajadorService();

        // 2. Preparar: Simular los datos que el Controlador obtendría del formulario/Postman
        TrabajadorDTORequest dto = new TrabajadorDTORequest();
        
        // --- Datos de Trabajador ---
        dto.setNombre("Alex");
        dto.setApellido("Uruchi");
        dto.setColegiatura("777462"); 
        dto.setFechaRegistro(LocalDate.now());

        // Claves Foráneas de Catálogo (Asumimos que estos IDs existen en la BD)
        dto.setIdTipoDocumento(1);
        dto.setIdRol(2);
        dto.setIdEspecialidad(2);
        
        // --- Datos de Usuario (Login) ---
        dto.setUsername("Alexll");
        dto.setContrasena("ClaveSegura2025!");
        
        // --- Datos de Contacto ---
        dto.setTipoContacto("EMAIL");
        dto.setCorreo("Alex.fu@ejemplo.com");
        dto.setTelefono("983455182");
        dto.setDireccion("Av. Test 103");
        
        // 3. EJECUTAR: Llamar al Service para realizar la transacción de 3 pasos
        // Cambiamos el tipo de resultado a Integer
        Integer idTrabajadorGenerado = trabajadorService.insert(dto);

        // 4. VERIFICAR: Imprimir el resultado
        System.out.println("\n--- Resultado de la Prueba ---");
        
        // CORRECCIÓN: Verificamos si el ID generado es válido (no nulo y mayor que 0)
        if (idTrabajadorGenerado != null && idTrabajadorGenerado > 0) {
            System.out.println("✅ PRUEBA EXITOSA: El trabajador se insertó correctamente.");
            System.out.println("   > ID del Trabajador Generado: " + idTrabajadorGenerado);
            System.out.println("Verificar en la BD los 3 nuevos registros (Contacto, Usuario, Trabajador).");
        } else {
            System.err.println("❌ PRUEBA FALLIDA: La inserción falló o el servicio devolvió NULL.");
            System.err.println("   > Resultado del Servicio: " + idTrabajadorGenerado);
        }
    }
}