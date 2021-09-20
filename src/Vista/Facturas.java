package Vista;

import Controlador.Conexion;
import Datos.DatosFacturas;
import Funciones.FuncionesFacturas;
import Vista.Notificaciones.Aceptar_Cancelar;
import Vista.Notificaciones.Advertencia;
import Vista.Notificaciones.Fallo;
import Vista.Notificaciones.Realizado;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SwichBlade15
 */
public final class Facturas extends javax.swing.JInternalFrame {

    DatosFacturas datos = new DatosFacturas();
    FuncionesFacturas funcion = new FuncionesFacturas();
    private final Conexion mysql = new Conexion();
    private final Connection cn = Conexion.getConnection();
    SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
    Date d;
    DefaultTableModel modelo, modelo2;
    Statement st;
    ResultSet rs;
    int codigo;
    String sSQL, boleta;

    public Facturas() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        inhabilitar();
        this.setBackground(new Color(0, 0, 0, 0));
        this.setIconifiable(false);
        this.setBorder(null);

        mostrar("");
        numeroBoleta();
        boleta();
        botonesTransparentes();
        txtBoleta.setText(boleta);
    }

    public void mostrar(String buscar) {
        try {
            modelo = funcion.mostrar(buscar);
            tblFacturas.setModel(modelo);
            ocultar_columnas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void registros(int buscar) {
        try {
            modelo2 = funcion.registros(buscar);
            ocultar_columnas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void ocultar_columnas() {
        tblFacturas.getColumnModel().getColumn(0).setMaxWidth(0);
        tblFacturas.getColumnModel().getColumn(0).setMinWidth(0);
        tblFacturas.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void habilitar() {
        txtIdfacturas.setText("");
        txtIdclientes.setText("");
        txtBoleta.setText("");
        txtInicioMedidor.setText("");
        txtCierreMedidor.setText("");
        txtNumeroUsuario.setText("");
        txtClientes.setText("");
        txtDireccion.setText("");
        txtConsumoExcedente.setText("0");
        txtConsumoTotal.setText("0");
        txtImporteMinimo.setText("0");
        txtImporteExcedentes.setText("0");
        txtImporteAtrasos.setText("0");
        txtImporteConexion.setText("0");
        txtImporteMedidor.setText("0");
        txtImporteIva.setText("0");
        txtImporteTotal.setText("0");
        
        cmbMes.setSelectedIndex(0);
        dchVencimiento.setCalendar(null);
        dchFechaInicio.setCalendar(null);
        dchFechaCierre.setCalendar(null);
    }

    public void inhabilitar() {
        txtIdfacturas.setText("");
        txtIdclientes.setText("");
        txtBoleta.setText("");
        txtInicioMedidor.setText("");
        txtCierreMedidor.setText("");
        txtNumeroUsuario.setText("");
        txtClientes.setText("");
        txtDireccion.setText("");
        txtConsumoExcedente.setText("0");
        txtConsumoTotal.setText("0");
        txtImporteMinimo.setText("0");
        txtImporteExcedentes.setText("0");
        txtImporteAtrasos.setText("0");
        txtImporteConexion.setText("0");
        txtImporteMedidor.setText("0");
        txtImporteIva.setText("0");
        txtImporteTotal.setText("0");
        
        cmbMes.setSelectedIndex(0);
        dchVencimiento.setCalendar(null);
        dchFechaInicio.setCalendar(null);
        dchFechaCierre.setCalendar(null);
    }

    public int numeroBoleta() {
        sSQL = "SELECT MAX(id)AS boleta FROM facturas";

        try {
            st = cn.createStatement();
            rs = st.executeQuery(sSQL);
            while (rs.next()) {
                codigo = rs.getInt("boleta");
            }
            return codigo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }
    }

    public void boleta() {
        if (codigo >= 0 && codigo <= 9) {
            codigo++;
            boleta = "00000" + codigo;
        } else if (codigo > 9 && codigo < 100) {
            codigo++;
            boleta = "0000" + codigo;
        } else if (codigo >= 100 && codigo <= 999) {
            codigo++;
            boleta = "000" + codigo;
        } else if (codigo >= 1000 && codigo <= 9999) {
            codigo++;
            boleta = "00" + codigo;
        } else if (codigo >= 10000 && codigo <= 99999) {
            codigo++;
            boleta = "0" + codigo;
        }
    }

    private void consumo() {
        int consumoTotal = Integer.parseInt(txtConsumoMinimo.getText()) + Integer.parseInt(txtConsumoExcedente.getText());
        txtConsumoTotal.setText(String.valueOf(consumoTotal));
    }

    private void llamarCliente() {
        SeleccionarClientes dialog = new SeleccionarClientes(f, true);
        dialog.setVisible(true);
    }

    public void botonesTransparentes() {
        btnEliminar.setOpaque(false);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setBorderPainted(false);

        btnGuardar.setOpaque(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);

        btnNuevo.setOpaque(false);
        btnNuevo.setContentAreaFilled(false);
        btnNuevo.setBorderPainted(false);

        btnImprimir.setOpaque(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setBorderPainted(false);

        btnSeleccionarClientes.setOpaque(false);
        btnSeleccionarClientes.setContentAreaFilled(false);
        btnSeleccionarClientes.setBorderPainted(false);
    }

    private void calculo() {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFacturas = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        txtIdfacturas = new javax.swing.JTextField();
        txtIdclientes = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNumeroUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtClientes = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        btnSeleccionarClientes = new javax.swing.JButton();
        lblFondoInterno4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBoleta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        dchVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        lblCerrar = new javax.swing.JLabel();
        lblFondoBuscador = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dchFechaInicio = new com.toedter.calendar.JDateChooser();
        dchFechaCierre = new com.toedter.calendar.JDateChooser();
        lblFondoInterno2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtInicioMedidor = new javax.swing.JTextField();
        txtCierreMedidor = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtConsumoMinimo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtConsumoExcedente = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtConsumoTotal = new javax.swing.JTextField();
        lblFondoInterno3 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtImporteMinimo = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtImporteExcedentes = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtImporteAtrasos = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtImporteConexion = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtImporteMedidor = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtImporteIva = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtImporteTotal = new javax.swing.JTextField();
        lblFondoInterno1 = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        setBorder(null);
        setOpaque(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Facturas");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 180, -1));

        txtBuscar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(0, 102, 255));
        txtBuscar.setText("Buscar");
        txtBuscar.setBorder(null);
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarFocusLost(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(838, 74, 139, 17));

        tblFacturas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tblFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFacturas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 465, 975, 130));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Guardar32.png"))); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 410, 40, -1));

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Nuevo32.png"))); // NOI18N
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 410, 40, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Eliminar32.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 410, 40, -1));

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imprimir32.png"))); // NOI18N
        getContentPane().add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 40, -1));
        getContentPane().add(txtIdfacturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 80, -1));
        getContentPane().add(txtIdclientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 79, -1));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Usuario N°");
        jPanel11.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        txtNumeroUsuario.setEditable(false);
        txtNumeroUsuario.setBackground(new java.awt.Color(255, 255, 255));
        txtNumeroUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroUsuarioActionPerformed(evt);
            }
        });
        jPanel11.add(txtNumeroUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 100, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Cliente:");
        jPanel11.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        txtClientes.setEditable(false);
        txtClientes.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.add(txtClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 213, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Direccion:");
        jPanel11.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        txtDireccion.setEditable(false);
        txtDireccion.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 213, -1));

        btnSeleccionarClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/BotonSeleccionar.png"))); // NOI18N
        btnSeleccionarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarClientesActionPerformed(evt);
            }
        });
        jPanel11.add(btnSeleccionarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 100, -1));

        lblFondoInterno4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno3.png"))); // NOI18N
        jPanel11.add(lblFondoInterno4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, -1));

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 250, 180));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Boleta N°:");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        txtBoleta.setEditable(false);
        txtBoleta.setBackground(new java.awt.Color(255, 255, 255));
        txtBoleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBoletaActionPerformed(evt);
            }
        });
        jPanel12.add(txtBoleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 77, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mes:");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 90, -1));

        cmbMes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));
        jPanel12.add(cmbMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 100, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Vencimiento:");
        jPanel12.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, -1));
        jPanel12.add(dchVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 100, 20));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno4.png"))); // NOI18N
        jPanel12.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 80));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 350, 80));

        lblCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Cerrar32.png"))); // NOI18N
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
        });
        getContentPane().add(lblCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 14, -1, -1));

        lblFondoBuscador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoBuscador.png"))); // NOI18N
        getContentPane().add(lblFondoBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Fecha Lectura");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Inicio");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Cierre");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 80, -1));
        jPanel7.add(dchFechaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, -1));
        jPanel7.add(dchFechaCierre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 100, -1));

        lblFondoInterno2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno2.png"))); // NOI18N
        jPanel7.add(lblFondoInterno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 90));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 270, 90));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Estado del medidor");
        jPanel8.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Inicio");
        jPanel8.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 70, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Cierre");
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 70, -1));

        txtInicioMedidor.setEditable(false);
        txtInicioMedidor.setBackground(new java.awt.Color(255, 255, 255));
        txtInicioMedidor.setText("0");
        txtInicioMedidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInicioMedidorActionPerformed(evt);
            }
        });
        jPanel8.add(txtInicioMedidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 75, -1));

        txtCierreMedidor.setText("0");
        txtCierreMedidor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCierreMedidorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCierreMedidorFocusLost(evt);
            }
        });
        txtCierreMedidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCierreMedidorActionPerformed(evt);
            }
        });
        jPanel8.add(txtCierreMedidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 75, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno2.png"))); // NOI18N
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 90));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 215, 270, -1));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Consumo en MRS");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Minimo");
        jPanel9.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, -1));

        txtConsumoMinimo.setEditable(false);
        txtConsumoMinimo.setBackground(new java.awt.Color(255, 255, 255));
        txtConsumoMinimo.setText("10");
        txtConsumoMinimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsumoMinimoActionPerformed(evt);
            }
        });
        jPanel9.add(txtConsumoMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 75, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Exedentes:");
        jPanel9.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 40, 70, -1));

        txtConsumoExcedente.setText("0");
        txtConsumoExcedente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtConsumoExcedenteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConsumoExcedenteFocusLost(evt);
            }
        });
        txtConsumoExcedente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsumoExcedenteActionPerformed(evt);
            }
        });
        txtConsumoExcedente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtConsumoExcedenteKeyTyped(evt);
            }
        });
        jPanel9.add(txtConsumoExcedente, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 60, 75, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Total:");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 70, -1));

        txtConsumoTotal.setEditable(false);
        txtConsumoTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtConsumoTotal.setText("0");
        txtConsumoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsumoTotalActionPerformed(evt);
            }
        });
        jPanel9.add(txtConsumoTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 75, -1));

        lblFondoInterno3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno2.png"))); // NOI18N
        jPanel9.add(lblFondoInterno3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, -1));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, 270, -1));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Importes");
        jPanel14.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 170, -1));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Minimo");
        jPanel14.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        txtImporteMinimo.setEditable(false);
        txtImporteMinimo.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteMinimo.setText("99");
        txtImporteMinimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteMinimoActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 75, -1));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Excedentes");
        jPanel14.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        txtImporteExcedentes.setEditable(false);
        txtImporteExcedentes.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteExcedentes.setText("99");
        txtImporteExcedentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteExcedentesActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteExcedentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 75, -1));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Atrasos");
        jPanel14.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        txtImporteAtrasos.setEditable(false);
        txtImporteAtrasos.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteAtrasos.setText("99");
        txtImporteAtrasos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteAtrasosActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteAtrasos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 75, -1));

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Conexion");
        jPanel14.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        txtImporteConexion.setEditable(false);
        txtImporteConexion.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteConexion.setText("99");
        txtImporteConexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteConexionActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteConexion, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 75, -1));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Medidor");
        jPanel14.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        txtImporteMedidor.setEditable(false);
        txtImporteMedidor.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteMedidor.setText("99");
        txtImporteMedidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteMedidorActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteMedidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 75, -1));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Iva");
        jPanel14.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, -1, -1));

        txtImporteIva.setEditable(false);
        txtImporteIva.setBackground(new java.awt.Color(255, 255, 255));
        txtImporteIva.setText("99");
        txtImporteIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteIvaActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 75, -1));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Total a pagar");
        jPanel14.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 170, -1));

        txtImporteTotal.setEditable(false);
        txtImporteTotal.setBackground(new java.awt.Color(204, 255, 204));
        txtImporteTotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtImporteTotal.setForeground(new java.awt.Color(0, 102, 0));
        txtImporteTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImporteTotal.setText("99");
        txtImporteTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteTotalActionPerformed(evt);
            }
        });
        jPanel14.add(txtImporteTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 230, 170, 25));

        lblFondoInterno1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoInterno1.png"))); // NOI18N
        jPanel14.add(lblFondoInterno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, -1));

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 120, 200, 270));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoFacturas.png"))); // NOI18N
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBoletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBoletaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBoletaActionPerformed

    private void txtInicioMedidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInicioMedidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInicioMedidorActionPerformed

    private void txtCierreMedidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCierreMedidorActionPerformed
        txtCierreMedidor.transferFocus();
        txtConsumoExcedente.requestFocus();
    }//GEN-LAST:event_txtCierreMedidorActionPerformed

    private void txtConsumoMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConsumoMinimoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConsumoMinimoActionPerformed

    private void txtConsumoExcedenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConsumoExcedenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConsumoExcedenteActionPerformed

    private void txtConsumoTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConsumoTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConsumoTotalActionPerformed

    private void txtNumeroUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroUsuarioActionPerformed

    private void txtImporteMedidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteMedidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteMedidorActionPerformed

    private void txtImporteMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteMinimoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteMinimoActionPerformed

    private void txtImporteExcedentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteExcedentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteExcedentesActionPerformed

    private void txtImporteAtrasosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteAtrasosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteAtrasosActionPerformed

    private void txtImporteConexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteConexionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteConexionActionPerformed

    private void txtImporteIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteIvaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteIvaActionPerformed

    private void txtImporteTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteTotalActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (txtIdfacturas.getText().length() == 0) {
            guardar();
        } else {
            editar();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        mostrar("");
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtConsumoExcedenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConsumoExcedenteFocusLost
        if (txtConsumoExcedente.getText().equals("")) {
            txtConsumoExcedente.setText("0");
            consumo();
        } else {
            consumo();
        }
    }//GEN-LAST:event_txtConsumoExcedenteFocusLost

    private void txtConsumoExcedenteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConsumoExcedenteFocusGained
        txtConsumoExcedente.setText("");
    }//GEN-LAST:event_txtConsumoExcedenteFocusGained

    private void btnSeleccionarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarClientesActionPerformed
        llamarCliente();
    }//GEN-LAST:event_btnSeleccionarClientesActionPerformed

    private void tblFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasMouseClicked
        int seleccionar = tblFacturas.rowAtPoint(evt.getPoint());

        registros(Integer.parseInt(tblFacturas.getValueAt(seleccionar, 0).toString()));
        txtIdfacturas.setText(modelo2.getValueAt(0, 0).toString());
        txtBoleta.setText(String.valueOf(modelo2.getValueAt(0, 1)));
        cmbMes.setSelectedItem(String.valueOf(modelo2.getValueAt(0, 2)));

        //Convertir java.sql.date a java.util.date y mostrar en pantalla la fecha de Vencimiento
        SimpleDateFormat vencimiento = new SimpleDateFormat("yyyy-MM-dd");
        //Formato inicial. 
        try {
            String fechaInicio = String.valueOf(modelo2.getValueAt(0, 3));
            d = vencimiento.parse(fechaInicio);
        } catch (ParseException e) {

        }

        //Aplica formato requerido.
        try {
            vencimiento.applyPattern("dd/MM/yyyy");
            String nuevoFormato = vencimiento.format(d);
            Date fecha = vencimiento.parse(nuevoFormato);
            dchVencimiento.setDate(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtImporteAtrasos.setText(modelo2.getValueAt(0, 4).toString());
        txtImporteConexion.setText(modelo2.getValueAt(0, 5).toString());

        //Convertir java.sql.date a java.util.date y mostrar en pantalla la Fecha Inicio
        SimpleDateFormat inicio = new SimpleDateFormat("yyyy-MM-dd");
        //Formato inicial. 
        try {
            String fechaInicio = String.valueOf(modelo2.getValueAt(0, 6));
            d = inicio.parse(fechaInicio);
        } catch (ParseException e) {

        }

        //Aplica formato requerido.
        try {
            inicio.applyPattern("dd/MM/yyyy");
            String nuevoFormato = inicio.format(d);
            Date fecha = inicio.parse(nuevoFormato);
            dchFechaInicio.setDate(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Convertir java.sql.date a java.util.date y mostrar en pantalla la Fecha Cierre
        SimpleDateFormat cierre = new SimpleDateFormat("yyyy-MM-dd");
        //Formato inicial. 
        try {
            String fechaInicio = String.valueOf(modelo2.getValueAt(0, 7));
            d = cierre.parse(fechaInicio);
        } catch (ParseException e) {

        }

        //Aplica formato requerido.
        try {
            cierre.applyPattern("dd/MM/yyyy");
            String nuevoFormato = cierre.format(d);
            Date fecha = cierre.parse(nuevoFormato);
            dchFechaCierre.setDate(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtInicioMedidor.setText(modelo2.getValueAt(0, 8).toString());
        txtCierreMedidor.setText(modelo2.getValueAt(0, 9).toString());
        txtConsumoMinimo.setText(modelo2.getValueAt(0, 10).toString());
        txtConsumoExcedente.setText(modelo2.getValueAt(0, 11).toString());
        txtConsumoTotal.setText(modelo2.getValueAt(0, 12).toString());
        txtIdclientes.setText(modelo2.getValueAt(0, 14).toString());
    }//GEN-LAST:event_tblFacturasMouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() == 2) {
            inhabilitar();
        }
    }//GEN-LAST:event_formMouseClicked

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        Principal.lblProceso.setText("Proceso: OFF");
        this.dispose();
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void txtConsumoExcedenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConsumoExcedenteKeyTyped
        String Caracteres = txtConsumoExcedente.getText();
        if (Caracteres.length() >= 8) {
            evt.consume();
        }
    }//GEN-LAST:event_txtConsumoExcedenteKeyTyped

    private void txtCierreMedidorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCierreMedidorFocusGained
        txtCierreMedidor.setText("");
    }//GEN-LAST:event_txtCierreMedidorFocusGained

    private void txtCierreMedidorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCierreMedidorFocusLost
        if(txtCierreMedidor.getText().length()==0){
            txtCierreMedidor.setText("0");
        }
    }//GEN-LAST:event_txtCierreMedidorFocusLost

    private void txtBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusGained
        txtBuscar.setText("");
    }//GEN-LAST:event_txtBuscarFocusGained

    private void txtBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusLost
        if(txtBuscar.getText().length()==0){
            txtBuscar.setText("Buscar");
        }
    }//GEN-LAST:event_txtBuscarFocusLost

    //Metodos para llamar a los JDialog de Advertencia, Fallo y Realizado
    Frame f = JOptionPane.getFrameForComponent(this);
    String encabezado;
    String mensaje;
    Icon icono;

    public void advertencia() {
        Advertencia dialog = new Advertencia(f, true);
        Advertencia.lblEncabezado.setText(mensaje);
        dialog.setVisible(true);
    }

    public void fallo() {
        Fallo dialog = new Fallo(f, true);
        Fallo.lblEncabezado.setText(mensaje);
        dialog.setVisible(true);
    }

    public void realizado() {
        Realizado dialog = new Realizado(f, true);
        Realizado.lblEncabezado.setText(mensaje);
        dialog.setVisible(true);
    }

    public void aceptarCancelar() {
        Aceptar_Cancelar dialog = new Aceptar_Cancelar(f, true);
        icono = new ImageIcon(getClass().getResource("/Imagenes/FondoCerrarSesion.png"));
        Aceptar_Cancelar.lblFondo.setIcon(icono);
        Aceptar_Cancelar.lblEncabezado.setText(encabezado);
        Aceptar_Cancelar.lblMensaje.setText(mensaje);
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSeleccionarClientes;
    public static javax.swing.JComboBox<String> cmbMes;
    private com.toedter.calendar.JDateChooser dchFechaCierre;
    public static com.toedter.calendar.JDateChooser dchFechaInicio;
    public static com.toedter.calendar.JDateChooser dchVencimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblFondoBuscador;
    private javax.swing.JLabel lblFondoInterno1;
    private javax.swing.JLabel lblFondoInterno2;
    private javax.swing.JLabel lblFondoInterno3;
    private javax.swing.JLabel lblFondoInterno4;
    private javax.swing.JTable tblFacturas;
    private javax.swing.JTextField txtBoleta;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCierreMedidor;
    public static javax.swing.JTextField txtClientes;
    private javax.swing.JTextField txtConsumoExcedente;
    private javax.swing.JTextField txtConsumoMinimo;
    private javax.swing.JTextField txtConsumoTotal;
    public static javax.swing.JTextField txtDireccion;
    public static javax.swing.JTextField txtIdclientes;
    private javax.swing.JTextField txtIdfacturas;
    private javax.swing.JTextField txtImporteAtrasos;
    private javax.swing.JTextField txtImporteConexion;
    private javax.swing.JTextField txtImporteExcedentes;
    private javax.swing.JTextField txtImporteIva;
    private javax.swing.JTextField txtImporteMedidor;
    private javax.swing.JTextField txtImporteMinimo;
    private javax.swing.JTextField txtImporteTotal;
    public static javax.swing.JTextField txtInicioMedidor;
    public static javax.swing.JTextField txtNumeroUsuario;
    // End of variables declaration//GEN-END:variables

    public void guardar() {
        Date vencimiento = dchVencimiento.getDate();
        java.sql.Date vencimiento1 = new java.sql.Date(vencimiento.getTime());

        Date inicio = dchFechaInicio.getDate();
        java.sql.Date inicio1 = new java.sql.Date(inicio.getTime());

        Date cierre = dchFechaCierre.getDate();
        java.sql.Date cierre1 = new java.sql.Date(cierre.getTime());

        int mes = cmbMes.getSelectedIndex();
        String mes2 = ((String) cmbMes.getItemAt(mes));

        datos.setBoleta(txtBoleta.getText());
        datos.setMes(mes2);
        datos.setVencimiento(vencimiento1);
        datos.setAtraso(Integer.parseInt(txtImporteAtrasos.getText()));
        datos.setConexion(Integer.parseInt(txtImporteConexion.getText()));
        datos.setFechaInicio(inicio1);
        datos.setFechaCierre(cierre1);
        datos.setEstadoInicio(Integer.parseInt(txtInicioMedidor.getText()));
        datos.setEstadoCierre(Integer.parseInt(txtCierreMedidor.getText()));
        datos.setConsumoMinimo(Integer.parseInt(txtImporteMinimo.getText()));
        datos.setExcedente(Integer.parseInt(txtImporteExcedentes.getText()));
        datos.setTotal(Integer.parseInt(txtImporteTotal.getText()));

        if (funcion.insertar(datos, funcion.buscarClientes(Integer.parseInt(txtIdclientes.getText())))) {
            mensaje = "Factura guardada correctamente";
            realizado();
            mostrar("");
//            inhabilitar();
        } else {
            mensaje = "Factura no guardada";
            fallo();
            mostrar("");
        }
    }

    public void editar() {
        Date vencimiento = dchVencimiento.getDate();
        java.sql.Date vencimiento1 = new java.sql.Date(vencimiento.getTime());

        Date inicio = dchFechaInicio.getDate();
        java.sql.Date inicio1 = new java.sql.Date(inicio.getTime());

        Date cierre = dchFechaCierre.getDate();
        java.sql.Date cierre1 = new java.sql.Date(cierre.getTime());

        int mes = cmbMes.getSelectedIndex();
        String mes2 = ((String) cmbMes.getItemAt(mes));

        datos.setBoleta(txtBoleta.getText());
        datos.setMes(mes2);
        datos.setVencimiento(vencimiento1);
        datos.setAtraso(Integer.parseInt(txtImporteAtrasos.getText()));
        datos.setConexion(Integer.parseInt(txtImporteConexion.getText()));
        datos.setFechaInicio(inicio1);
        datos.setFechaCierre(cierre1);
        datos.setEstadoInicio(Integer.parseInt(txtInicioMedidor.getText()));
        datos.setEstadoCierre(Integer.parseInt(txtCierreMedidor.getText()));
        datos.setConsumoMinimo(Integer.parseInt(txtImporteMinimo.getText()));
        datos.setExcedente(Integer.parseInt(txtImporteExcedentes.getText()));
        datos.setTotal(Integer.parseInt(txtImporteTotal.getText()));
        datos.setId(Integer.parseInt(txtIdfacturas.getText()));

        if (funcion.editar(datos, Integer.parseInt(txtIdclientes.getText()))) {
            mensaje = "Factura guardada correctamente";
            realizado();
            mostrar("");
//            inhabilitar();
        } else {
            mensaje = "Factura no guardada";
            fallo();
            mostrar("");
        }
    }

    public void eliminar() {
        if (txtIdfacturas.getText().length() == 0) {
            mensaje = "Debes seleccionar primero una factura a eliminar.";
            advertencia();
        } else {
            encabezado = "Eliminar permanentemente";
            mensaje = "Esta seguro de eliminar esta factura?";
            aceptarCancelar();
            String reply = Principal.txtAceptarCancelar.getText();
            if (reply.equals("1")) {

                datos.setId(Integer.parseInt(txtIdfacturas.getText()));

                if (funcion.eliminar(datos)) {
                    mensaje = "Factura eliminada correctamente";
                    realizado();
                    txtIdfacturas.setText("");
                    mostrar("");
//                    inhabilitar();
                } else {
                    mensaje = "Factura no eliminada";
                    fallo();
                    mostrar("");
                }
            }
        }
    }
}
