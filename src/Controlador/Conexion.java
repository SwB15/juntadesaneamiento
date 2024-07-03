package Controlador;

import java.sql.*;

public class Conexion{

    static String bd = "junta_saneamiento";
    static String login = "root";
    static String password = "";
    static String url = "jdbc:mysql://localhost/"+bd;
    static Connection connection = null;

    public Conexion(){
        try{
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url,login,password);
        }catch(SQLException | ClassNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public void desconectar(){
        try{
            connection.close();
        }catch(SQLException ex){}
    }
}
