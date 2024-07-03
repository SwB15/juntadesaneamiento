package Funciones;

import Controlador.Conexion;
import Datos.DatosFacturas;
import Vista.Facturas;
import static Vista.Facturas.dchVencimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SwichBlade15
 */
public class FuncionesFacturas {

    private final Conexion mysql = new Conexion();
    private final Connection cn = Conexion.getConnection();
    public DefaultTableModel modelo, modelo2, modelo3;
    Statement st;
    ResultSet rs;
    PreparedStatement ps;
    private String sSQL = "";
    public Integer totalRegistros;
    String[] reg = new String[2];
    String[] regclientes = new String[6];
    public int codigo;
    String fechaActual;

    //Aqui se cargan los datos a ser utilizados dentro del sistema
    public DefaultTableModel registros(int buscar) {
        String[] titulos = {"Codigo", "Boleta", "Mes", "Vencimiento", "Atraso", "Conexion", "F. Inicio", "F. Cierre", "E. Inicio", "E. Cierre", "Cons. Minimo", "Excedente", "Total", "Cliente", "idClientes"};
        String[] registros = new String[15];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        try {
            ps = cn.prepareStatement("SELECT id, boleta, mes, vencimiento, atraso, conexion, fechainicio, fechacierre, estadoinicio, estadocierre, consumominimo, excedente, total, (SELECT nombre FROM clientes WHERE id = idclientes) AS nombre, (SELECT apellido FROM clientes WHERE id = idclientes) AS apellido, (SELECT id FROM clientes WHERE id = idclientes) AS idClientes FROM facturas WHERE id = ? ORDER BY id DESC");
            ps.setInt(1, buscar);
            rs = ps.executeQuery();
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
                registros[13] = rs.getString("nombre") + " " + rs.getString("apellido");
                registros[14] = rs.getString("idClientes");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }

    //Al seleccionar un cliente para realizar una nueva factura, se obtiene el id de la ultima factura realizada al cliente
    public int datosClientes(int idClientes) {
        sSQL = "SELECT MAX(id)AS idfacturas FROM facturas WHERE idClientes = ?";

        try {
            ps = cn.prepareStatement(sSQL);
            ps.setInt(1, idClientes);
            rs = ps.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt("idfacturas");
            }
            return codigo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }
    }

    //Obtenido el id de la factura anterior, se proceden a cargar los datos del mes anterior del cliente
    public void llenarDatos(int buscar) {
        sSQL = "SELECT * FROM facturas WHERE id = ?";

        String[] titulos = {"Codigo", "Boleta", "Mes", "Vencimiento", "Atraso", "Conexion", "F. Inicio", "F. Cierre", "E. Inicio", "E. Cierre", "Cons. Minimo", "Excedente", "Total", "idClientes"};
        String[] registros = new String[14];
        totalRegistros = 0;
        modelo2 = new DefaultTableModel(null, titulos);

        try {
            ps = cn.prepareStatement(sSQL);
            ps.setInt(1, buscar);
            rs = ps.executeQuery();
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
                registros[13] = rs.getString("idClientes");

                totalRegistros = totalRegistros + 1;
                modelo2.addRow(registros);
            }
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    //Rellena el combobox de Mes en facturas
    public void meses() {
        switch (modelo2.getValueAt(0, 2).toString()) {
            case "ENERO":
                Facturas.cmbMes.setSelectedItem("FEBRERO");
                break;
            case "FEBRERO":
                Facturas.cmbMes.setSelectedItem("MARZO");
                break;
            case "MARZO":
                Facturas.cmbMes.setSelectedItem("ABRIL");
                break;
            case "ABRIL":
                Facturas.cmbMes.setSelectedItem("MAYO");
                break;
            case "MAYO":
                Facturas.cmbMes.setSelectedItem("JUNIO");
                break;
            case "JUNIO":
                Facturas.cmbMes.setSelectedItem("JULIO");
                break;
            case "JULIO":
                Facturas.cmbMes.setSelectedItem("AGOSTO");
                break;
            case "AGOSTO":
                Facturas.cmbMes.setSelectedItem("SEPTIEMBRE");
                break;
            case "SEPTIEMBRE":
                Facturas.cmbMes.setSelectedItem("OCTUBRE");
                break;
            case "OCTUBRE":
                Facturas.cmbMes.setSelectedItem("NOVIEMBRE");
                break;
            case "NOVIEMBRE":
                Facturas.cmbMes.setSelectedItem("DICIEMBRE");
                break;
            case "DICIEMBRE":
                Facturas.cmbMes.setSelectedItem("ENERO");
                break;
            default:
                break;
        }
    }

    //Obtiene la fecha actual y establece la fecha de vencimiento de la nueva factura
    public void vencimiento() {

        java.util.Date fech = new Date();
        int dia = 13;
        int mes = fech.getMonth() + 2;
        int ano = YearMonth.now().getYear();

        String formattedString = dia + "/" + mes + "/" + ano;
        System.out.println(formattedString);

        //Aplica formato requerido.
        try {
            SimpleDateFormat vencimiento = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = vencimiento.parse(formattedString);
            dchVencimiento.setDate(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clientes(String cliente) {
        String[] titulos = {"Codigo", "Usuario"};
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, nombre, apellido FROM clientes WHERE nombre LIKE '%" + cliente + "%' OR apellido LIKE '%" + cliente + "%' ORDER BY id DESC";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);

            while (rs.next()) {
                reg[0] = rs.getString("id");
                reg[1] = rs.getString("nombre") + " " + rs.getString("apellido");
                System.out.println("while " + reg[0]);

                totalRegistros = totalRegistros + 1;
                modelo.addRow(reg);
            }
            System.out.println("despues del while " + reg[0]);
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    //Inserta el cliente de la factura seleccionada mediante la tabla de facturas
    public DefaultTableModel clientesFactura(int idcliente) {
        String[] titulos = {"Codigo", "Numero", "Nombre", "Apellido", "Direccion", "Medidor"};
        totalRegistros = 0;
        modelo3 = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, numerocliente, nombre, apellido, direccion, medidor FROM clientes WHERE id = ? ORDER BY id DESC";

        try {
            ps = cn.prepareStatement(sSQL);
            ps.setInt(1, idcliente);
            rs = ps.executeQuery();

            while (rs.next()) {
                regclientes[0] = rs.getString("id");
                regclientes[1] = rs.getString("numerocliente");
                regclientes[2] = rs.getString("nombre");
                regclientes[3] = rs.getString("apellido");
                regclientes[4] = rs.getString("direccion");
                regclientes[5] = rs.getString("medidor");

                totalRegistros = totalRegistros + 1;
                modelo3.addRow(regclientes);
            }
            return modelo3;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }

    //Datos que se muestran en la tabla de Facturas
    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {"Codigo", "Boleta", "Mes", "Vencimiento", "Fecha Cierre", "Estado Cierre", "Total", "Usuario"};
        String[] registros = new String[8];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, boleta, mes, vencimiento, fechacierre, estadocierre, total, (SELECT nombre FROM clientes WHERE id = idclientes) AS nombre, (SELECT apellido FROM clientes WHERE id = idclientes) AS apellido FROM facturas WHERE boleta LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("boleta");
                registros[2] = rs.getString("mes");
                registros[3] = rs.getString("vencimiento");
                registros[4] = rs.getString("fechacierre");
                registros[5] = rs.getString("estadocierre");
                registros[6] = rs.getString("total");
                registros[7] = rs.getString("nombre") + " " + rs.getString("apellido");

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
    }
    
    //Datos que se muestran en la tabla de ListaFacturas
    public DefaultTableModel mostrarListaFacturas(String buscar) {
        String[] titulos = {"Seleccionar","Codigo", "Boleta", "Mes", "Vencimiento", "Fecha Cierre", "Estado Cierre", "Total", "Usuario"};
        Object[] registros = new Object[9];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "SELECT id, boleta, mes, vencimiento, fechacierre, estadocierre, total, (SELECT nombre FROM clientes WHERE id = idclientes) AS nombre, (SELECT apellido FROM clientes WHERE id = idclientes) AS apellido FROM facturas WHERE boleta LIKE '%" + buscar + "%' ORDER BY id DESC";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);

            while (rs.next()) {
                registros[0] = false;
                registros[1] = rs.getString("id");
                registros[2] = rs.getString("boleta");
                registros[3] = rs.getString("mes");
                registros[4] = rs.getString("vencimiento");
                registros[5] = rs.getString("fechacierre");
                registros[6] = rs.getString("estadocierre");
                registros[7] = rs.getString("total");
                registros[8] = rs.getString("nombre") + " " + rs.getString("apellido");

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
        sSQL = "UPDATE facturas SET boleta = ?, mes = ?, vencimiento = ?, atraso = ?, conexion = ?, fechainicio = ?, fechacierre = ?, estadoinicio = ?, estadocierre = ?, consumominimo = ?, excedente = ?, total = ?, idclientes = (SELECT id FROM clientes WHERE id LIKE '%" + idclientes + "%' limit 1) WHERE id = ?";

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
            pst.setInt(13, datos.getId());

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

    public int buscarClientes(int idclientes) {
        sSQL = "SELECT id FROM clientes WHERE clientes.id = '" + idclientes + "'";
        int codigo = 0;
        try {
            st = cn.createStatement();
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
