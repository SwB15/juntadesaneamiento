package Vista;

import Vista.Caja.AperturaCaja;
import Vista.Caja.CierreCaja;
import Controlador.Conexion;
import Controlador.ControlCantidad;
import Funciones.FuncionesCaja;
import Reportes.Reportes;
import Vista.Caja.MovimientosCaja;
import Vista.CompraVenta.Compras;
import Vista.CompraVenta.ComprasOnline;
import Vista.CompraVenta.ListaCompras;
import Vista.CompraVenta.ListaComprasOnline;
import Vista.CompraVenta.ListaVentas;
import Vista.CompraVenta.Ventas;
import Vista.FichaServicios.FichaServicios;
import Vista.FichaServicios.ListaServicios;
import Vista.Notificaciones.Aceptar_Cancelar;
import Vista.Notificaciones.Advertencia;
import Vista.Notificaciones.Fallo;
import Vista.Notificaciones.Notificaciones;
import Vista.Notificaciones.Realizado;
import Vista.Otros.AcercaDe;
import Vista.Otros.Auditoria;
import Vista.Otros.Ciudades;
import Vista.Otros.Departamentos;
import Vista.Personas.Clientes;
import Vista.Personas.Usuarios;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Desktop;
import java.net.URI;
import javax.swing.JOptionPane;
import Vista.Otros.Backup;
import Vista.Personas.Funcionarios;
import Vista.Productos.Baja;
import Vista.Productos.ListaProductos;
import Vista.Productos.Productos;
import Vista.Proveedores.Proveedores;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JFrame;

/**
 *
 * @author SwichBlade15
 */
public final class Principal extends javax.swing.JFrame {

    private Conexion mysql = new Conexion();
    private Connection cn = mysql.getConnection();
    FuncionesCaja funcion = new FuncionesCaja();
    ControlCantidad control = new ControlCantidad();
    private String sSQL = "";
    public Integer totalRegistros;
    int idcaja, iddetallecaja;
    Date date = new Date();
    DateFormat hora, fecha;
    String fechaActual;

    public Principal() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);

        if (null == funcion.estadoCaja()) {
            mnuVentas.setEnabled(false);
            mnuCompras.setEnabled(false);
            smnuAbrirCaja.setEnabled(true);
            smnuCerrarCaja.setEnabled(false);
            mnuServicios.setEnabled(false);

            txtCaja.setText("Caja Cerrada");
            encabezado = "JSystems";
            mensaje = "Gracias por adquirir el sistema";
            realizado();
        } else {
            switch (funcion.estadoCaja()) {
                case "ABIERTO":
                    funcion.idCaja();
                    funcion.idDetalleCaja();
                    txtIdcaja.setText(String.valueOf(funcion.idCaja()));
                    txtIddetallecaja.setText(String.valueOf(funcion.idDetalleCaja()));
                    mnuVentas.setEnabled(true);
                    mnuCompras.setEnabled(true);
                    smnuAbrirCaja.setEnabled(false);
                    smnuCerrarCaja.setEnabled(true);
                    mnuServicios.setEnabled(true);
                    smnuMovimientos.setEnabled(true);
                    txtCaja.setText("Caja Abierta");
                    encabezado = "Estado de caja";
                    mensaje = "Se cerró el sistema sin cerrar Caja";
                    advertencia();
                    break;
                default:
                    
                    mnuVentas.setEnabled(false);
                    mnuCompras.setEnabled(false);
                    smnuAbrirCaja.setEnabled(true);
                    smnuCerrarCaja.setEnabled(false);
                    smnuMovimientos.setEnabled(false);
                    mnuServicios.setEnabled(false);
                    txtCaja.setText("Caja Cerrada");
                    encabezado = "Estado de caja";
                    mensaje = "La caja se encuentra cerrada";
                    realizado();
                    break;
            }
        }

        botonesTransparentes();
        cerrar();
        fecha();
        eliminarArchivos();

        lblCodigo.setVisible(false);
        txtCaja.setVisible(false);
        txtAceptarCancelar.setVisible(false);
        txtIdcaja.setVisible(false);
        txtIddetallecaja.setVisible(false);
        txtFecha.setVisible(false);
        pnlNotificaciones.setBackground(new Color(0, 0, 0, 0));
        lblProceso.setVisible(false);
        lblContadorNotificaciones.setText("0");
        control.cantidadMinima();
        jScrollPane1.setVisible(false);
        control.cont();
    }

    public void fecha() {
        fecha = new SimpleDateFormat("dd/MM/yyyy");
        txtFecha.setText(fecha.format(date));
    }

    public void ocultar_columnas() {
        tblCantidadMinima.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCantidadMinima.getColumnModel().getColumn(0).setMinWidth(0);
        tblCantidadMinima.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void botonesTransparentes() {
        btnWeb.setOpaque(false);
        btnWeb.setContentAreaFilled(false);
        btnWeb.setBorderPainted(false);

        btnSesion.setOpaque(false);
        btnSesion.setContentAreaFilled(false);
        btnSesion.setBorderPainted(false);
    }

    public void cerrar() {
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    encabezado = "Cerrar Sesión";
                    mensaje = "Está seguro que desea salir del Sistema?";
                    aceptarCancelar();
                    confirmarSalida();
                }
            });
            this.setVisible(true);
        } catch (Exception e) {
        }
    }

    public void confirmarSalida() {
        String valor = txtAceptarCancelar.getText();
        if (valor.equals("1")) {
            txtAceptarCancelar.setText("");
            System.exit(0);
        }
    }

    //Metodo que sirve para eliminar los archivos de la carpeta Boletas cuando pasen 24 horas de su creación
    public void eliminarArchivos() {
        String direccion = "C:\\Users\\User\\Documents\\NetBeansProjects\\ventainformatica\\src\\Temp";

        File directorio = new File(direccion);
        File file;
        if (directorio.isDirectory()) {
            String[] files = directorio.list();
            if (files.length > 0) {
                System.out.println(" Directorio vacio: " + direccion);
                for (String archivo : files) {
                    System.out.println(archivo);
                    file = new File(direccion + File.separator + archivo);

                    System.out.println("Ultima modificación: " + new Date(file.lastModified()));
                    long Time;
                    Time = (System.currentTimeMillis() - file.lastModified());
                    long cantidadDia = (Time / 86400000);
                    System.out.println("Age of the file is: " + cantidadDia + " days");
                    // Attempt to delete it
                    //86400000 ms is equivalent to one day
                    if (Time > (86400000 * 1) && archivo.contains(".pdf")) {
                        System.out.println("Borrado:" + archivo);
                        file.delete();
                        file.deleteOnExit();
                    }

                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Principal.png"));
        Image image = icon.getImage();
        jDesktopPane1 = new javax.swing.JDesktopPane(){
            public void paintComponent(Graphics g){
                g.drawImage(image,0,0,getWidth(),getHeight(),this);
            }
        };
        lblCodigo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblPermisos = new javax.swing.JLabel();
        btnWeb = new javax.swing.JButton();
        btnSesion = new javax.swing.JButton();
        lblProceso = new javax.swing.JLabel();
        pnlNotificaciones = new javax.swing.JPanel();
        lblContadorNotificaciones = new javax.swing.JLabel();
        lblNotificaciones = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCantidadMinima = new javax.swing.JTable();
        txtAceptarCancelar = new javax.swing.JTextField();
        txtCaja = new javax.swing.JTextField();
        txtIdcaja = new javax.swing.JTextField();
        txtIddetallecaja = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuProductos = new javax.swing.JMenu();
        smnuCategorias = new javax.swing.JMenuItem();
        smnuProductos = new javax.swing.JMenuItem();
        smnuListaProductos = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnuProveedores = new javax.swing.JMenu();
        smnuProveedores = new javax.swing.JMenuItem();
        mnuPersonas = new javax.swing.JMenu();
        smnuUsuarios = new javax.swing.JMenuItem();
        smnuClientes = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnuCompras = new javax.swing.JMenu();
        smnuCompras = new javax.swing.JMenuItem();
        smnuListaCompras = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuCaja = new javax.swing.JMenu();
        smnuAbrirCaja = new javax.swing.JMenuItem();
        smnuCerrarCaja = new javax.swing.JMenuItem();
        smnuMovimientos = new javax.swing.JMenuItem();
        mnuVentas = new javax.swing.JMenu();
        smnuVentas = new javax.swing.JMenuItem();
        smnuListaVentas = new javax.swing.JMenuItem();
        mnuServicios = new javax.swing.JMenu();
        smnuServicios = new javax.swing.JMenuItem();
        smnuReparaciones = new javax.swing.JMenuItem();
        mnuHerramientas = new javax.swing.JMenu();
        smnuOtros = new javax.swing.JMenu();
        smnuDepartamentos = new javax.swing.JMenuItem();
        smnuCiudades = new javax.swing.JMenuItem();
        smnuAcercaDe = new javax.swing.JMenuItem();
        smnuBackup = new javax.swing.JMenuItem();
        mnuAuditoria = new javax.swing.JMenuItem();
        smnuReportes = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDesktopPane1.setBackground(new java.awt.Color(224, 224, 224));

        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblPermisos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Web30.png"))); // NOI18N
        btnWeb.setToolTipText("Web del Sistema");
        btnWeb.setBorder(null);
        btnWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebActionPerformed(evt);
            }
        });

        btnSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logout30.png"))); // NOI18N
        btnSesion.setToolTipText("Cerrar Sesion");
        btnSesion.setBorder(null);
        btnSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSesionMouseClicked(evt);
            }
        });
        btnSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSesionActionPerformed(evt);
            }
        });
        btnSesion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSesionKeyPressed(evt);
            }
        });

        pnlNotificaciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblContadorNotificaciones.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        lblContadorNotificaciones.setForeground(new java.awt.Color(255, 255, 255));
        lblContadorNotificaciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContadorNotificaciones.setText("+99");
        pnlNotificaciones.add(lblContadorNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 4, 24, 24));

        lblNotificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pruebanotificaciones.png"))); // NOI18N
        lblNotificaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNotificacionesMouseClicked(evt);
            }
        });
        pnlNotificaciones.add(lblNotificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        tblCantidadMinima.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblCantidadMinima);

        jDesktopPane1.setLayer(lblCodigo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(lblUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(lblPermisos, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnWeb, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnSesion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(lblProceso, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(pnlNotificaciones, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtAceptarCancelar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtCaja, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtIdcaja, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtIddetallecaja, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtFecha, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPermisos, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlNotificaciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtIdcaja)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(lblProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(txtAceptarCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnWeb)
                                    .addComponent(btnSesion))
                                .addGap(25, 25, 25))
                            .addComponent(txtCaja)
                            .addComponent(txtIddetallecaja, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFecha))
                        .addGap(20, 20, 20))))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSesion)
                        .addGap(18, 18, 18)
                        .addComponent(btnWeb)
                        .addGap(33, 33, 33)
                        .addComponent(txtCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAceptarCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdcaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIddetallecaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(pnlNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addComponent(lblPermisos, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

        mnuProductos.setForeground(new java.awt.Color(0, 102, 255));
        mnuProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Productos32.png"))); // NOI18N
        mnuProductos.setText("Productos");
        mnuProductos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        mnuProductos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mnuProductosMouseMoved(evt);
            }
        });

        smnuCategorias.setBackground(new java.awt.Color(255, 255, 255));
        smnuCategorias.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuCategorias.setForeground(new java.awt.Color(0, 102, 255));
        smnuCategorias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Categoria20.png"))); // NOI18N
        smnuCategorias.setText("Categorias");
        smnuCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuCategoriasActionPerformed(evt);
            }
        });
        mnuProductos.add(smnuCategorias);

        smnuProductos.setBackground(new java.awt.Color(255, 255, 255));
        smnuProductos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuProductos.setForeground(new java.awt.Color(0, 102, 255));
        smnuProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Productos20.png"))); // NOI18N
        smnuProductos.setText("Productos");
        smnuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuProductosActionPerformed(evt);
            }
        });
        mnuProductos.add(smnuProductos);

        smnuListaProductos.setBackground(new java.awt.Color(255, 255, 255));
        smnuListaProductos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuListaProductos.setForeground(new java.awt.Color(0, 102, 255));
        smnuListaProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Lista20.png"))); // NOI18N
        smnuListaProductos.setText("Lista Productos");
        smnuListaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuListaProductosActionPerformed(evt);
            }
        });
        mnuProductos.add(smnuListaProductos);

        jMenuItem3.setText("Baja de Productos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mnuProductos.add(jMenuItem3);

        jMenuBar1.add(mnuProductos);

        mnuProveedores.setForeground(new java.awt.Color(0, 102, 255));
        mnuProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Proveedores32.png"))); // NOI18N
        mnuProveedores.setText("Proveedores");
        mnuProveedores.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        smnuProveedores.setBackground(new java.awt.Color(255, 255, 255));
        smnuProveedores.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuProveedores.setForeground(new java.awt.Color(0, 102, 255));
        smnuProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Camion2-20.png"))); // NOI18N
        smnuProveedores.setText("Proveedores");
        smnuProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuProveedoresActionPerformed(evt);
            }
        });
        mnuProveedores.add(smnuProveedores);

        jMenuBar1.add(mnuProveedores);

        mnuPersonas.setForeground(new java.awt.Color(0, 102, 255));
        mnuPersonas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Usuario32.png"))); // NOI18N
        mnuPersonas.setText("Personas");
        mnuPersonas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuUsuarios.setBackground(new java.awt.Color(255, 255, 255));
        smnuUsuarios.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuUsuarios.setForeground(new java.awt.Color(0, 102, 255));
        smnuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Usuario2-20.png"))); // NOI18N
        smnuUsuarios.setText("Usuarios");
        smnuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuUsuariosActionPerformed(evt);
            }
        });
        mnuPersonas.add(smnuUsuarios);

        smnuClientes.setBackground(new java.awt.Color(255, 255, 255));
        smnuClientes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuClientes.setForeground(new java.awt.Color(0, 102, 255));
        smnuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Cliente20.png"))); // NOI18N
        smnuClientes.setText("Clientes");
        smnuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuClientesActionPerformed(evt);
            }
        });
        mnuPersonas.add(smnuClientes);

        jMenuItem4.setText("Funcionarios");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuPersonas.add(jMenuItem4);

        jMenuBar1.add(mnuPersonas);

        mnuCompras.setForeground(new java.awt.Color(0, 102, 255));
        mnuCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Compras32.png"))); // NOI18N
        mnuCompras.setText("Compras");
        mnuCompras.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuCompras.setBackground(new java.awt.Color(255, 255, 255));
        smnuCompras.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuCompras.setForeground(new java.awt.Color(0, 102, 255));
        smnuCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/NuevaVenta20.png"))); // NOI18N
        smnuCompras.setText("Nueva Compra");
        smnuCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuComprasActionPerformed(evt);
            }
        });
        mnuCompras.add(smnuCompras);

        smnuListaCompras.setBackground(new java.awt.Color(255, 255, 255));
        smnuListaCompras.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuListaCompras.setForeground(new java.awt.Color(0, 102, 255));
        smnuListaCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Lista20.png"))); // NOI18N
        smnuListaCompras.setText("Lista Compras");
        smnuListaCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuListaComprasActionPerformed(evt);
            }
        });
        mnuCompras.add(smnuListaCompras);

        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jMenuItem1.setForeground(new java.awt.Color(0, 102, 255));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ComprasOnline20.png"))); // NOI18N
        jMenuItem1.setText("Compras Online");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuCompras.add(jMenuItem1);

        jMenuItem2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jMenuItem2.setForeground(new java.awt.Color(0, 102, 255));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Lista20.png"))); // NOI18N
        jMenuItem2.setText("Lista Compras Online");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuCompras.add(jMenuItem2);

        jMenuBar1.add(mnuCompras);

        mnuCaja.setForeground(new java.awt.Color(0, 102, 255));
        mnuCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Caja Registradora2-32.png"))); // NOI18N
        mnuCaja.setText("Caja");
        mnuCaja.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuAbrirCaja.setBackground(new java.awt.Color(255, 255, 255));
        smnuAbrirCaja.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuAbrirCaja.setForeground(new java.awt.Color(0, 102, 255));
        smnuAbrirCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Caja Registradora2-20.png"))); // NOI18N
        smnuAbrirCaja.setText("Abrir Caja");
        smnuAbrirCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuAbrirCajaActionPerformed(evt);
            }
        });
        mnuCaja.add(smnuAbrirCaja);

        smnuCerrarCaja.setBackground(new java.awt.Color(255, 255, 255));
        smnuCerrarCaja.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuCerrarCaja.setForeground(new java.awt.Color(0, 102, 255));
        smnuCerrarCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Caja Registradora2-20.png"))); // NOI18N
        smnuCerrarCaja.setText("Cerrar Caja");
        smnuCerrarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuCerrarCajaActionPerformed(evt);
            }
        });
        mnuCaja.add(smnuCerrarCaja);

        smnuMovimientos.setBackground(new java.awt.Color(255, 255, 255));
        smnuMovimientos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuMovimientos.setForeground(new java.awt.Color(0, 102, 255));
        smnuMovimientos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Caja Registradora20.png"))); // NOI18N
        smnuMovimientos.setText("Movimientos");
        smnuMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuMovimientosActionPerformed(evt);
            }
        });
        mnuCaja.add(smnuMovimientos);

        jMenuBar1.add(mnuCaja);

        mnuVentas.setForeground(new java.awt.Color(0, 102, 255));
        mnuVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Ventas32.png"))); // NOI18N
        mnuVentas.setText("Ventas");
        mnuVentas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuVentas.setBackground(new java.awt.Color(255, 255, 255));
        smnuVentas.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuVentas.setForeground(new java.awt.Color(0, 102, 255));
        smnuVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Ventas2-20.png"))); // NOI18N
        smnuVentas.setText("Nueva Venta");
        smnuVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuVentasActionPerformed(evt);
            }
        });
        mnuVentas.add(smnuVentas);

        smnuListaVentas.setBackground(new java.awt.Color(255, 255, 255));
        smnuListaVentas.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuListaVentas.setForeground(new java.awt.Color(0, 102, 255));
        smnuListaVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Lista20.png"))); // NOI18N
        smnuListaVentas.setText("Lista Ventas");
        smnuListaVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuListaVentasActionPerformed(evt);
            }
        });
        mnuVentas.add(smnuListaVentas);

        jMenuBar1.add(mnuVentas);

        mnuServicios.setBackground(new java.awt.Color(255, 255, 255));
        mnuServicios.setForeground(new java.awt.Color(0, 102, 255));
        mnuServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Servicios32Izquierda.png"))); // NOI18N
        mnuServicios.setText("Servicios");
        mnuServicios.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuServicios.setBackground(new java.awt.Color(255, 255, 255));
        smnuServicios.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuServicios.setForeground(new java.awt.Color(0, 102, 255));
        smnuServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Ficha20.png"))); // NOI18N
        smnuServicios.setText("Nueva Ficha");
        smnuServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuServiciosActionPerformed(evt);
            }
        });
        mnuServicios.add(smnuServicios);

        smnuReparaciones.setBackground(new java.awt.Color(255, 255, 255));
        smnuReparaciones.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuReparaciones.setForeground(new java.awt.Color(0, 102, 255));
        smnuReparaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Lista20.png"))); // NOI18N
        smnuReparaciones.setText("Lista Fichas");
        smnuReparaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuReparacionesActionPerformed(evt);
            }
        });
        mnuServicios.add(smnuReparaciones);

        jMenuBar1.add(mnuServicios);

        mnuHerramientas.setForeground(new java.awt.Color(0, 102, 255));
        mnuHerramientas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Herramientas32.png"))); // NOI18N
        mnuHerramientas.setText("Herramientas");
        mnuHerramientas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        smnuOtros.setBackground(new java.awt.Color(255, 255, 255));
        smnuOtros.setForeground(new java.awt.Color(0, 102, 255));
        smnuOtros.setText("Otros");
        smnuOtros.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        smnuDepartamentos.setBackground(new java.awt.Color(255, 255, 255));
        smnuDepartamentos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuDepartamentos.setForeground(new java.awt.Color(0, 102, 255));
        smnuDepartamentos.setText("Departamentos");
        smnuDepartamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuDepartamentosActionPerformed(evt);
            }
        });
        smnuOtros.add(smnuDepartamentos);

        smnuCiudades.setBackground(new java.awt.Color(255, 255, 255));
        smnuCiudades.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuCiudades.setForeground(new java.awt.Color(0, 102, 255));
        smnuCiudades.setText("Ciudades");
        smnuCiudades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuCiudadesActionPerformed(evt);
            }
        });
        smnuOtros.add(smnuCiudades);

        mnuHerramientas.add(smnuOtros);

        smnuAcercaDe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        smnuAcercaDe.setBackground(new java.awt.Color(255, 255, 255));
        smnuAcercaDe.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuAcercaDe.setForeground(new java.awt.Color(0, 102, 255));
        smnuAcercaDe.setText("Acerca De");
        smnuAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuAcercaDeActionPerformed(evt);
            }
        });
        mnuHerramientas.add(smnuAcercaDe);

        smnuBackup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        smnuBackup.setBackground(new java.awt.Color(255, 255, 255));
        smnuBackup.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuBackup.setForeground(new java.awt.Color(0, 102, 255));
        smnuBackup.setText("Backup");
        smnuBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuBackupActionPerformed(evt);
            }
        });
        mnuHerramientas.add(smnuBackup);

        mnuAuditoria.setBackground(new java.awt.Color(255, 255, 255));
        mnuAuditoria.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        mnuAuditoria.setForeground(new java.awt.Color(0, 102, 255));
        mnuAuditoria.setText("Auditoria");
        mnuAuditoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAuditoriaActionPerformed(evt);
            }
        });
        mnuHerramientas.add(mnuAuditoria);

        smnuReportes.setBackground(new java.awt.Color(255, 255, 255));
        smnuReportes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        smnuReportes.setForeground(new java.awt.Color(0, 102, 255));
        smnuReportes.setText("Reportes");
        smnuReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnuReportesActionPerformed(evt);
            }
        });
        mnuHerramientas.add(smnuReportes);

        jMenuBar1.add(mnuHerramientas);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void smnuCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuCategoriasActionPerformed
        Categorias form = new Categorias();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuCategoriasActionPerformed

    private void smnuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuClientesActionPerformed
        Clientes form = new Clientes();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuClientesActionPerformed

    private void smnuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuProductosActionPerformed
        Productos form = new Productos();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuProductosActionPerformed

    private void smnuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuUsuariosActionPerformed
        Usuarios form = new Usuarios();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuUsuariosActionPerformed

    private void smnuAbrirCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuAbrirCajaActionPerformed
        AperturaCaja form = new AperturaCaja();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuAbrirCajaActionPerformed

    private void smnuCerrarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuCerrarCajaActionPerformed
        CierreCaja form = new CierreCaja();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuCerrarCajaActionPerformed

    private void smnuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuVentasActionPerformed
        Ventas form = new Ventas();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuVentasActionPerformed

    private void smnuDepartamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuDepartamentosActionPerformed
        Departamentos form = new Departamentos();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuDepartamentosActionPerformed

    private void smnuCiudadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuCiudadesActionPerformed
        Ciudades form = new Ciudades();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuCiudadesActionPerformed

    private void smnuAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuAcercaDeActionPerformed
        AcercaDe form = new AcercaDe();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuAcercaDeActionPerformed

    private void smnuListaVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuListaVentasActionPerformed
        ListaVentas form = new ListaVentas();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuListaVentasActionPerformed

    private void mnuAuditoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAuditoriaActionPerformed
        Auditoria form = new Auditoria();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_mnuAuditoriaActionPerformed

    private void btnWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWebActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("http://localhost/sentra/index.html"));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al ingresar a la web");
        }
    }//GEN-LAST:event_btnWebActionPerformed

    private void smnuMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuMovimientosActionPerformed
        MovimientosCaja form = new MovimientosCaja();
        jDesktopPane1.add(form);

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuMovimientosActionPerformed

    private void smnuBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuBackupActionPerformed
        Backup form = new Backup();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuBackupActionPerformed

    private void btnSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSesionActionPerformed

    }//GEN-LAST:event_btnSesionActionPerformed

    private void btnSesionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSesionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSesionKeyPressed

    private void btnSesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSesionMouseClicked
        encabezado = "Cerrar Sesión";
        mensaje = "Está seguro que desea cerrar sesión?";
        aceptarCancelar();

        if (txtAceptarCancelar.getText().equals("1")) {
            if (txtCaja.getText().equals("Caja Cerrada")) {
                this.dispose();
                Login form = new Login();
                form.toFront();
                form.setVisible(true);
            } else {
                mensaje = "Cierre la caja antes de Cerrar Sesión";
                advertencia();
            }
        }
    }//GEN-LAST:event_btnSesionMouseClicked

    private void smnuComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuComprasActionPerformed
        Compras form = new Compras();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuComprasActionPerformed

    private void smnuListaComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuListaComprasActionPerformed
        ListaCompras form = new ListaCompras();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuListaComprasActionPerformed

    private void smnuProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuProveedoresActionPerformed
        Proveedores form = new Proveedores();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuProveedoresActionPerformed

    private void mnuProductosMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuProductosMouseMoved

    }//GEN-LAST:event_mnuProductosMouseMoved

    private void smnuListaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuListaProductosActionPerformed
        ListaProductos form = new ListaProductos();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuListaProductosActionPerformed

    private void lblNotificacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNotificacionesMouseClicked
//        Notificacion form = new Notificacion();
//        form.setLocation(10000, 10000);
//        form.toFront();
//        form.setVisible(true);

        if (lblProceso.getText().equals("Proceso: ON")) {

        } else {

            Notificaciones form = new Notificaciones();
            jDesktopPane1.add(form);
            Notificaciones.btnNotificacionesOculto.doClick();
            lblProceso.setText("Proceso: ON");

            form.setClosable(true);
            form.setIconifiable(true);
            try {
                Dimension desktopSize = jDesktopPane1.getSize();
                Dimension FrameSize = form.getSize();
                form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
                form.show();
            } catch (Exception e) {
            }

            form.toFront();
            form.setVisible(true);
            this.setLocationRelativeTo(null);
            this.setExtendedState(MAXIMIZED_BOTH);
        }

    }//GEN-LAST:event_lblNotificacionesMouseClicked

    private void smnuServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuServiciosActionPerformed
        FichaServicios form = new FichaServicios();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuServiciosActionPerformed

    private void smnuReparacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuReparacionesActionPerformed
        ListaServicios form = new ListaServicios();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuReparacionesActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ComprasOnline form = new ComprasOnline();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ListaComprasOnline form = new ListaComprasOnline();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void smnuReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnuReportesActionPerformed
        Reportes form = new Reportes();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_smnuReportesActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Baja form = new Baja();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Funcionarios form = new Funcionarios();
        jDesktopPane1.add(form);
        lblProceso.setText("Proceso: ON");

        form.setClosable(true);
        form.setIconifiable(true);
        try {
            Dimension desktopSize = jDesktopPane1.getSize();
            Dimension FrameSize = form.getSize();
            form.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            form.show();
        } catch (Exception e) {
        }

        form.toFront();
        form.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    //Metodos para llamar al JDialog Aceptar_Cancelar
    //Se le añade un Fondo diferente al de Eliminar (Se diferencian en el sombreado)
    Frame f = JOptionPane.getFrameForComponent(this);
    String encabezado;
    String mensaje;
    Icon icono;

    public void advertencia() {
        Advertencia dialog = new Advertencia(f, true);
        Advertencia.lblEncabezado.setText(mensaje);
        Advertencia.lblAdvertencia.setText(encabezado);
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
        Realizado.lblRealizado.setText(encabezado);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSesion;
    private javax.swing.JButton btnWeb;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel lblCodigo;
    public static javax.swing.JLabel lblContadorNotificaciones;
    public static javax.swing.JLabel lblNotificaciones;
    public static javax.swing.JLabel lblPermisos;
    public static javax.swing.JLabel lblProceso;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JMenuItem mnuAuditoria;
    public static javax.swing.JMenu mnuCaja;
    public static javax.swing.JMenu mnuCompras;
    public static javax.swing.JMenu mnuHerramientas;
    public static javax.swing.JMenu mnuPersonas;
    public static javax.swing.JMenu mnuProductos;
    public static javax.swing.JMenu mnuProveedores;
    public static javax.swing.JMenu mnuServicios;
    public static javax.swing.JMenu mnuVentas;
    public static javax.swing.JPanel pnlNotificaciones;
    public static javax.swing.JMenuItem smnuAbrirCaja;
    private javax.swing.JMenuItem smnuAcercaDe;
    private javax.swing.JMenuItem smnuBackup;
    private javax.swing.JMenuItem smnuCategorias;
    public static javax.swing.JMenuItem smnuCerrarCaja;
    private javax.swing.JMenuItem smnuCiudades;
    private javax.swing.JMenuItem smnuClientes;
    private javax.swing.JMenuItem smnuCompras;
    private javax.swing.JMenuItem smnuDepartamentos;
    private javax.swing.JMenuItem smnuListaCompras;
    private javax.swing.JMenuItem smnuListaProductos;
    private javax.swing.JMenuItem smnuListaVentas;
    public static javax.swing.JMenuItem smnuMovimientos;
    private javax.swing.JMenu smnuOtros;
    private javax.swing.JMenuItem smnuProductos;
    private javax.swing.JMenuItem smnuProveedores;
    private javax.swing.JMenuItem smnuReparaciones;
    private javax.swing.JMenuItem smnuReportes;
    private javax.swing.JMenuItem smnuServicios;
    public static javax.swing.JMenuItem smnuUsuarios;
    private javax.swing.JMenuItem smnuVentas;
    public static javax.swing.JTable tblCantidadMinima;
    public static javax.swing.JTextField txtAceptarCancelar;
    public static javax.swing.JTextField txtCaja;
    public static javax.swing.JTextField txtFecha;
    public static javax.swing.JTextField txtIdcaja;
    public static javax.swing.JTextField txtIddetallecaja;
    // End of variables declaration//GEN-END:variables
}
