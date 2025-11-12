/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.sistemaOdontologo.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class ConnectionDataBase {
    private static ConnectionDataBase instance;
    private static Connection con;
    private static final String URL = "jdbc:sqlserver://sql5111.site4now.net;databaseName=db_abf869_dbsistemaodon;Encrypt=True;TrustServerCertificate=True;Instance=MSSQLSERVER";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String USER = "db_abf869_dbsistemaodon_admin";
    private static final String PASS = "Sistema_2025";

    public ConnectionDataBase() {
    }

    public synchronized static ConnectionDataBase getInstance() {

        if (instance == null) {
            instance = new ConnectionDataBase();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
      try {
            // Cargar el driver solo una vez (opcional en JDBC 4.0+)
            Class.forName(DRIVER); 
            // Crear y devolver la nueva conexión
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión abierta."); // Descomentar para debug
            return con; 
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC no encontrado: " + e.getMessage());
        }
    }


    public void close() {
        instance = null;
    }
    
    
    public static void main(String[] args) {
        ConnectionDataBase db = new ConnectionDataBase();
        
        System.out.println(db);
    }
}
