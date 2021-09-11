
package Funciones;

import Controlador.Conexion;
import Datos.DatosFacturas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SwichBlade15
 */
public class FuncionesFacturas {
    private final Conexion mysql = new Conexion();
    private final Connection cn = Conexion.getConnection();
    private String sSQL = "";
    public Integer totalRegistros;

    //Aqui se cargan los datos a ser utilizados dentro del sistema
    public void registros(String buscar) {
        DefaultTableModel modelo;
        String[] titulos = {"Codigo", "Boleta", "Mes", "Atraso", "Conexion", "F. Inicio", "F. Cierre", "E. Inicio", "E. Cierre", "Cons. Minimo", "Excedente", "Total", "Nombre", "Apellido"};
        String[] registros = new String[14];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, boleta, mes, vencimiento, atraso, conexion, fechainicio, fechacierre, estadoinicio, estadocierre, consumominimo, excedente, total, (SELECT nombre FROM clientes WHERE id = idclientes) AS nombre, (SELECT apellido FROM clientes WHERE id = idclientes) AS apellido  FROM facturas WHERE id LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("boleta");
                registros[2] = rs.getString("mes");
                registros[3] = rs.getString("vencimiento");
                registros[4] = rs.getString("atraso");
                registros[5] = rs.getString("conexion");
                registros[6] = rs.getString("fechainicio");
                registros[7] = rs.getString("fechacierre");
                registros[8] = rs.getString("estadoinicio");
                registros[9] = rs.getString("estadocierre");
                registros[10] = rs.getString("consumominimo");
                registros[11] = rs.getString("excedente");
                registros[12] = rs.getString("total");
                registros[13] = rs.getString("nombre")+" "+rs.getString("apellido");
                
                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    
    public DefaultTableModel mostrar(String buscar) {
        DefaultTableModel modelo;
        String[] titulos = {"Codigo", "Boleta", "Mes", "Vencimiento", "Fecha Cierre", "Estado Cierre", "Total", "Usuario"};
        String[] registros = new String[8];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, boleta, mes, vencimiento, fechacierre, estadocierre, total, (SELECT nombre FROM clientes WHERE id = idclientes) AS nombre, (SELECT apellido FROM clientes WHERE id = idclientes) AS apellido  FROM facturas WHERE id LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("boleta");
                registros[2] = rs.getString("mes");
                registros[3] = rs.getString("vencimiento");
                registros[4] = rs.getString("fechacierre");
                registros[5] = rs.getString("estadocierre");
                registros[6] = rs.getString("total");
                registros[7] = rs.getString("nombre")+" "+rs.getString("apellido");
                
                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }

    public boolean insertar(DatosFacturas datos, int idclientes) {
        sSQL = "INSERT INTO facturas(boleta, mes, vencimiento, atraso, conexion, fechainicio, fechacierre, estadoinicio, estadocierre, consumominimo, excedente, total, idclientes) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            pst.setString(1, datos.getBoleta());
            pst.setString(2, datos.getMes());
            pst.setDate(3, datos.getVencimiento());
            pst.setInt(4, datos.getAtraso());
            pst.setInt(5, datos.getConexion());
            pst.setDate(6, datos.getFechaInicio());
            pst.setDate(7, datos.getFechaCierre());
            pst.setInt(8, datos.getEstadoInicio());
            pst.setInt(9, datos.getEstadoCierre());
            pst.setInt(10, datos.getConsumoMinimo());
            pst.setInt(11, datos.getExcedente());
            pst.setInt(12, datos.getTotal());
            pst.setInt(13, idclientes);
            
            int N = pst.executeUpdate();
            return N != 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }

    public boolean editar(DatosFacturas datos, int idclientes) {
        sSQL = "UPDATE facturas SET boleta = ?, mes = ?, vencimiento = ?, atraso = ?, conexion = ?, fechainicio = ?, fechacierre = ?, estadoinicio = ?, estadocierre = ?, consumominimo = ?, excedente = ?, total = ?, idclientes = (SELECT id FROM clientes WHERE clientes LIKE '%" + idclientes + "%' limit 1) WHERE id = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            pst.setString(1, datos.getBoleta());
            pst.setString(2, datos.getMes());
            pst.setDate(3, datos.getVencimiento());
            pst.setInt(4, datos.getAtraso());
            pst.setInt(5, datos.getConexion());
            pst.setDate(6, datos.getFechaInicio());
            pst.setDate(7, datos.getFechaCierre());
            pst.setInt(8, datos.getEstadoInicio());
            pst.setInt(9, datos.getEstadoCierre());
            pst.setInt(10, datos.getConsumoMinimo());
            pst.setInt(11, datos.getExcedente());
            pst.setInt(12, datos.getTotal());
            pst.setInt(13, idclientes);
            pst.setInt(14, datos.getId());

            int N = pst.executeUpdate();
            return N != 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }

    public boolean eliminar(DatosFacturas datos) {
        sSQL = "DELETE FROM facturas WHERE id = ?";
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getId());
            int N = pst.executeUpdate();

            return N != 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }
    
    public int buscarClientes(int idclientes){
        sSQL = "SELECT id FROM clientes WHERE clientes.id = '"+idclientes+"'";
        int codigo = 0;
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {
                codigo = rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }
}
