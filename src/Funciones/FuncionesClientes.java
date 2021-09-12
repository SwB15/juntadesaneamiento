package Funciones;

import Controlador.Conexion;
import java.sql.Connection;
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

    public DefaultTableModel mostrar(String buscar) {
        DefaultTableModel modelo;
        String[] titulos = {"Codigo", "N° Usuario", "Usuario", "Direccion"};
        String[] registros = new String[4];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, usuarionumero, nombre, apellido, direccion FROM clientes WHERE id LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("usuarionumero");
                registros[2] = rs.getString("nombre") + " " + rs.getString("apellido");
                registros[3] = rs.getString("direccion");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }
}
