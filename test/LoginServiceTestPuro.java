
import com.utp.sistemaOdontologo.dtos.LoginDTORequest;
import com.utp.sistemaOdontologo.dtos.UsuarioInfoDTO;
import com.utp.sistemaOdontologo.services.UsuarioService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class LoginServiceTestPuro {
    private static final UsuarioService service = new UsuarioService();
    
    // Asumimos que este usuario y clave están actualmente en la base de datos (con la clave hasheada)
    private static final String USERNAME_EXISTENTE = "Alex12"; 
    private static final String CLAVE_CORRECTA = "ClaveSegura2025!"; 

    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBAS DE AUTENTICACIÓN ---");
        
        testLoginExitoso();
        testLoginFallidoClave();
        testLoginFallidoUsuario();
        
        System.out.println("--- PRUEBAS DE LOGIN FINALIZADAS ---");
    }

    // =======================================================
    // 1. TEST DE LOGIN EXITOSO
    // =======================================================
    private static void testLoginExitoso() {
        System.out.println("\n[1] TEST: Credenciales Correctas");
        
        LoginDTORequest request = new LoginDTORequest();
        request.setUsername(USERNAME_EXISTENTE);
        request.setContrasena(CLAVE_CORRECTA);

        UsuarioInfoDTO usuario = service.login(request);

        if (usuario != null) {
            System.out.println("✅ ÉXITO: Autenticación correcta.");
            System.out.println("   > Usuario autenticado: " + usuario.getUsername());
            System.out.println("   > Estado: " + usuario.getEstado());
        } else {
            System.err.println("❌ FALLO CRÍTICO: La autenticación falló, revise la lógica de comparación de hashes.");
        }
    }

    // =======================================================
    // 2. TEST DE LOGIN FALLIDO (Clave Incorrecta)
    // =======================================================
    private static void testLoginFallidoClave() {
        System.out.println("\n[2] TEST: Clave Incorrecta");

        LoginDTORequest request = new LoginDTORequest();
        request.setUsername(USERNAME_EXISTENTE);
        request.setContrasena("clave_incorrecta_99"); // Clave incorrecta

        UsuarioInfoDTO usuario = service.login(request);

        if (usuario == null) {
            System.out.println("✅ ÉXITO: Autenticación rechazada (Resultado esperado).");
        } else {
            System.err.println("❌ FALLO: La autenticación debería haber fallado, pero devolvió un usuario.");
        }
    }

    // =======================================================
    // 3. TEST DE LOGIN FALLIDO (Usuario Inexistente)
    // =======================================================
    private static void testLoginFallidoUsuario() {
        System.out.println("\n[3] TEST: Usuario Inexistente");

        LoginDTORequest request = new LoginDTORequest();
        request.setUsername("usuario_inexistente_123");
        request.setContrasena(CLAVE_CORRECTA);

        UsuarioInfoDTO usuario = service.login(request);

        if (usuario == null) {
            System.out.println("✅ ÉXITO: Usuario no encontrado (Resultado esperado).");
        } else {
            System.err.println("❌ FALLO: Devolvió un usuario para un username inexistente.");
        }
    }
}
