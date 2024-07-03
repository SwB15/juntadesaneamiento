package Funciones;

import Controlador.Conexion;
import Datos.DatosClientes;
import static Vista.SeleccionarClientes.rbtnNombre;
import static Vista.SeleccionarClientes.rbtnUsuario;
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
public class FuncionesClientes {

    private final Conexion mysql = new Conexion();
    private final Connection cn = Conexion.getConnection();
    private String sSQL = "";
    int totalRegistros = 0;
    public DefaultTableModel modelo;
    ResultSet rs;
    Statement st;
    PreparedStatement ps;
    String[] reg = new String[6];
    String where, buscarString;
    int buscarInt;

    public DefaultTableModel registros(int buscar) {
        String[] titulos = {"Id", "Codigo", "Nombre", "Apellido", "Direccion", "Medidor"};
        String[] registros = new String[6];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        try {
            ps = cn.prepareStatement("SELECT id, numerocliente, nombre, apellido, direccion, medidor WHERE id = ? ORDER BY id DESC");
            ps.setInt(1, buscar);
            rs = ps.executeQuery();

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("numerocliente");
                registros[2] = rs.getString("nombre");
                registros[3] = rs.getString("apellido");
                registros[4] = rs.getString("direccion");
                registros[5] = rs.getString("medidor");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }

    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {"Id", "Usuario", "Nombre", "Apellido", "Direccion", "Medidor"};
        String[] registros = new String[6];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, numerocliente, nombre, apellido, direccion, medidor FROM clientes WHERE nombre LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("numerocliente");
                registros[2] = rs.getString("nombre");
                registros[3] = rs.getString("apellido");
                registros[4] = rs.getString("direccion");
                registros[5] = rs.getString("medidor");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }

    public void seleccionarClientes(String buscar) {
        sSQL = "";
        String[] titulos = {"Id", "Usuario", "Nombre", "Apellido", "Direccion", "Medidor"};
        String[] registros = new String[6];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT * FROM clientes WHERE medidor IS NOT NULL";
        if (rbtnUsuario.isSelected()) {
            sSQL = sSQL + " AND numerocliente LIKE '%" + buscar + "%' ORDER BY numerocliente";
        }
        
        if (rbtnNombre.isSelected()) {
            sSQL = sSQL + " AND nombre LIKE '%" + buscar + "%' ORDER BY nombre";
        }

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);
            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("numerocliente");
                registros[2] = rs.getString("nombre");
                registros[3] = rs.getString("apellido");
                registros[4] = rs.getString("direccion");
                registros[5] = rs.getString("medidor");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    public boolean insertar(DatosClientes datos) {
        sSQL = "INSERT INTO clientes(numerocliente, nombre, apellido, direccion, medidor) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            pst.setInt(1, datos.getnumerocliente());
            pst.setString(2, datos.getNombre());
            pst.setString(3, datos.getApellido());
            pst.setString(4, datos.getDireccion());
            pst.setInt(5, datos.getMedidor());

            int N = pst.executeUpdate();
            return N != 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }

    public boolean editar(DatosClientes datos) {
        sSQL = "UPDATE clientes SET numerocliente = ?, nombre = ?, apellido = ?, direccion = ?, medidor = ? WHERE id = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getnumerocliente());
            pst.setString(2, datos.getNombre());
            pst.setString(3, datos.getApellido());
            pst.setString(4, datos.getDireccion());
            pst.setInt(5, datos.getMedidor());
            pst.setInt(6, datos.getId());

            int N = pst.executeUpdate();
            return N != 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }

    public boolean eliminar(DatosClientes datos) {
        sSQL = "DELETE FROM clientes WHERE id = ?";
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
}
