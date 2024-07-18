package Vista;

import Controlador.Conexion;
import Funciones.Calculos.Calculo;
import Funciones.FuncionesFacturas;
import Vista.Notificaciones.Aceptar_Cancelar;
import Vista.Notificaciones.Advertencia;
import Vista.Notificaciones.Fallo;
import Vista.Notificaciones.Realizado;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author SwichBlade15
 */
public class ListaFacturas extends javax.swing.JInternalFrame {

    DefaultTableModel modelo;
    DefaultTableModel modelo3 = new DefaultTableModel();
    FuncionesFacturas funcion = new FuncionesFacturas();
    Calculo calculo = new Calculo();
    boolean keyShift = false;
    int seleccionar = -1;
    String idFacturas, fecha = "";
    DecimalFormat formateador14 = new DecimalFormat("#,###.###");
    Date date;
    boolean shiftPressed = false;
    private final Connection cn = Conexion.getConnection();
    ArrayList<String> codigo = new ArrayList<>();
    //Map<String, Object> map = new HashMap<>();
    List<Map<String, Object>> datosFacturas = null;
    Map<String, List<Map<String, Object>>> clienteBoletasMap = new HashMap<>();

    public ListaFacturas() {
        initComponents();
//        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
//        this.setBackground(new Color(0, 0, 0, 0));
//        this.setBorder(null);
//        this.setOpaque(false);
        mostrar("");
        botonesTransparentes();
    }

    public void mostrar(String buscar) {
        try {
            modelo = funcion.mostrarListaFacturas(buscar);
            tblListaFacturas.setModel(modelo);

            // Ajustar la tabla para manejar checkboxes
            tblListaFacturas.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
            tblListaFacturas.getColumnModel().getColumn(0).setCellRenderer(tblListaFacturas.getDefaultRenderer(Boolean.class));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Metodo para pasar las fechas de "yyyy-MM-dd" a "dd/MM/yyyy"
    private String formatearFechas(String fechas) {
        fecha = "";
        //Convertir java.sql.date a java.util.date y mostrar en pantalla la fecha de Vencimiento
        SimpleDateFormat vencimiento = new SimpleDateFormat("yyyy-MM-dd");
        //Formato inicial. 
        try {
            date = vencimiento.parse(fechas);
        } catch (ParseException e) {
        }

        //Aplica formato requerido.
        vencimiento.applyPattern("dd/MM/yyyy");
        fecha = vencimiento.format(date);
        return fecha;
    }

    private String separadorDeMiles(String cantidad) {
        String resultado;
        if (cantidad.length() > 3) {
            resultado = cantidad.replace(".", "");
        } else {
            resultado = cantidad;
        }

        return formateador14.format(Integer.parseInt(resultado));
    }

    private void datosFacturas() {
        datosFacturas = new ArrayList<>();

        // Ruta y configuración del logo
        final String rutaLogo = "src/Imagenes/Icono.png";
        Path rutaLogo2 = Paths.get(rutaLogo);
        String rutaLogo4 = rutaLogo2.toAbsolutePath().toString();

        for (int i = 0; i < modelo3.getRowCount(); i++) {
            Map<String, Object> map = new HashMap<>();

            String sufijo = (i % 2 == 0) ? "" : "2";

            map.put("direccion" + sufijo, "ARAZAPE");
            map.put("mes" + sufijo, modelo3.getValueAt(i, 2));
            map.put("numerousuario" + sufijo, modelo3.getValueAt(i, 17));
            map.put("boleta" + sufijo, modelo3.getValueAt(i, 1));
            map.put("importeminimo" + sufijo, separadorDeMiles("10000"));
            map.put("importeatrasos" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 4).toString()));
            map.put("importeexcedentes" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 12).toString()));
            map.put("importeiva" + sufijo, separadorDeMiles(String.valueOf((Integer.parseInt("10000") + Integer.parseInt(modelo3.getValueAt(i, 12).toString()) / 10))));
            map.put("importetotal" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 13).toString()));
            map.put("vencimiento" + sufijo, formatearFechas(String.valueOf(modelo3.getValueAt(0, 3))));
            map.put("cliente" + sufijo, modelo3.getValueAt(i, 14));
            map.put("fechainicio" + sufijo, formatearFechas(String.valueOf(modelo3.getValueAt(0, 7))));
            map.put("fechacierre" + sufijo, formatearFechas(String.valueOf(modelo3.getValueAt(0, 8))));
            map.put("estadoinicio" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 9).toString()));
            map.put("estadocierre" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 10).toString()));
            map.put("consumominimo" + sufijo, "10");
            map.put("consumoexcedente" + sufijo, String.valueOf(calculo.calculoConsumo(Integer.parseInt(modelo3.getValueAt(i, 9).toString()), Integer.parseInt(modelo3.getValueAt(i, 10).toString()))));
            map.put("consumototal" + sufijo, separadorDeMiles(String.valueOf(Integer.parseInt(modelo3.getValueAt(i, 10).toString()) - Integer.parseInt(modelo3.getValueAt(i, 9).toString()))));
            map.put("importeconexion" + sufijo, separadorDeMiles(modelo3.getValueAt(i, 5).toString()));
            map.put("importemedidor" + sufijo, "COMPLETAR");
            map.put("logo", rutaLogo4);
            System.out.println("logo: " + rutaLogo4);

            datosFacturas.add(map);
        }
        System.out.println("datosFacturas: " + datosFacturas.toString());
    }

    //Obtiene la totalidad de los datos de la factura seleccionada en la tabla
    private void registros(int buscar) {
        System.out.println("entro registros");
        try {
            // Obtener modelo de datos para el código 'buscar'
            DefaultTableModel modelo2 = funcion.registros(buscar);

            // Verificar y configurar columnas de modelo3 si es necesario
            if (modelo3.getColumnCount() == 0) {
                // Configurar modelo3 con las mismas columnas que modelo2
                for (int col = 0; col < modelo2.getColumnCount(); col++) {
                    modelo3.addColumn(modelo2.getColumnName(col));
                }
            }

            // Asegurarse de que modelo2 tiene datos antes de procesar
            if (modelo2 != null && modelo2.getRowCount() > 0) {
                System.out.println("entro if");
                // Recorrer cada fila del modelo2 y agregarla a modelo3
                for (int i = 0; i < modelo2.getRowCount(); i++) {
                    System.out.println("for: " + i);
                    Vector<Object> filaData = new Vector<>();
                    for (int j = 0; j < modelo2.getColumnCount(); j++) {
                        filaData.add(modelo2.getValueAt(i, j));
                    }
                    modelo3.addRow(filaData);
                }
                datosFacturas();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para el código: " + buscar);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los registros: " + e.getMessage());
        }
    }

    //Ingresa lso datos del cliente cuando se selecciona un registro de la tabla
    public void clientes(int buscar) {
        try {
            modelo3 = funcion.clientesFactura(buscar);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void botonesTransparentes() {
        btnImprimir.setOpaque(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setBorderPainted(false);
    }

    private boolean seleccionados(int pos) {
        int contador = 0;
        boolean checked = true;
        for (int i = 0; i < tblListaFacturas.getRowCount(); i++) {
            boolean seleccion = (boolean) tblListaFacturas.getValueAt(i, pos);
            if (seleccion) {
                contador++;
            }
        }
        if (contador == 0) {
            checked = false;
        }
        return checked;
    }

    private void datos() {
        modelo3.setRowCount(0);
        modelo3.setColumnCount(0);

        for (int i = 0; i < codigo.size(); i++) {
            int codigo2 = Integer.parseInt(codigo.get(i));
            System.out.println("códigos: " + codigo2);
            registros(codigo2);
        }

        // Imprimir contenido de modelo3 para depuración
        for (int i = 0; i < modelo3.getRowCount(); i++) {
            for (int j = 0; j < modelo3.getColumnCount(); j++) {
                System.out.print(modelo3.getValueAt(i, j) + " ");
            }
            System.out.println();
        }

        tbl2.setModel(modelo3);
        //generarFacturas();
    }

    public void generarFacturas() {
        try {
            final String rutaReporte = "src/Reportes/MultipleFacturas.jrxml";
            Path rutaRelativaReporte = Paths.get(rutaReporte);
            String rutaAbsolutaReporte = rutaRelativaReporte.toAbsolutePath().toString();

            JasperReport reporte = JasperCompileManager.compileReport(rutaAbsolutaReporte);

            List<JasperPrint> pages = new ArrayList<>();
            for (int i = 0; i < datosFacturas.size(); i += 2) {
                Map<String, Object> datos1 = datosFacturas.get(i);
                Map<String, Object> datos2 = (i + 1 < datosFacturas.size()) ? datosFacturas.get(i + 1) : null;

                Map<String, Object> combinedData = new HashMap<>();
                combinedData.putAll(datos1);
                if (datos2 != null) {
                    combinedData.putAll(datos2);
                } else {
                    // Agregar un valor booleano para controlar la visibilidad del segundo subreporte
                    combinedData.put("showSecondSubreport", Boolean.FALSE);
                }

                JasperPrint jp = JasperFillManager.fillReport(reporte, combinedData, new JREmptyDataSource());
                pages.add(jp);
            }

            JasperPrint finalReport = pages.get(0);
            for (int in = 1; in < pages.size(); in++) {
                finalReport.addPage((JRPrintPage) pages.get(in).getPages().get(0));
            }

            JasperViewer view = new JasperViewer(finalReport, false);
            view.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/Icono.png")).getImage());
            view.setTitle("Facturas Multiples");
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(ListaFacturas.class.getName()).log(Level.SEVERE, null, ex);
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

        internalPanel1 = new Componentes.InternalPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListaFacturas = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        lblFondoBuscador = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl2 = new javax.swing.JTable();
        panelGris1 = new Componentes.panelGris();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        txtHasta = new javax.swing.JTextField();
        btnImprimir = new javax.swing.JButton();
        btnFactura = new javax.swing.JButton();
        btnMostrar = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblListaFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblListaFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListaFacturasMouseClicked(evt);
            }
        });
        tblListaFacturas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblListaFacturasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblListaFacturasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblListaFacturas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 157, 970, 240));

        txtBuscar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(0, 102, 255));
        txtBuscar.setText("Buscar Facturas...");
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(838, 74, 139, 17));

        lblFondoBuscador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/FondoBuscador.png"))); // NOI18N
        getContentPane().add(lblFondoBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, -1, -1));

        tbl2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 417, 1290, 170));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Desde:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Hasta:");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Imprimir32.png"))); // NOI18N
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGris1Layout = new javax.swing.GroupLayout(panelGris1);
        panelGris1.setLayout(panelGris1Layout);
        panelGris1Layout.setHorizontalGroup(
            panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGris1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        panelGris1Layout.setVerticalGroup(
            panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGris1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImprimir)
                    .addGroup(panelGris1Layout.createSequentialGroup()
                        .addGroup(panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGris1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(panelGris1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 250, 70));

        btnFactura.setText("Factura");
        btnFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturaActionPerformed(evt);
            }
        });
        getContentPane().add(btnFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, -1, -1));

        btnMostrar.setText("Datos");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblListaFacturasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblListaFacturasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true; // Marcar que Shift está presionado
        }
    }//GEN-LAST:event_tblListaFacturasKeyPressed

    private void tblListaFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListaFacturasMouseClicked
        if (evt.getClickCount() == 1) {
            int filaActual = tblListaFacturas.rowAtPoint(evt.getPoint());

            if (shiftPressed && seleccionar != -1) {
                // Determinar las filas a seleccionar
                int filaInicial = Math.min(seleccionar, filaActual);
                int filaFinal = Math.max(seleccionar, filaActual);

                // Seleccionar los checkboxes en el rango
                for (int i = filaInicial; i <= filaFinal; i++) {
                    tblListaFacturas.setValueAt(true, i, 0); // Seleccionar el checkbox
                }
            } else {
                // Guardar la fila seleccionada para el siguiente uso
                seleccionar = filaActual;
            }
        }
    }//GEN-LAST:event_tblListaFacturasMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
//        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusGained
        if (txtBuscar.getText().equals("Buscar Facturas...")) {
            txtBuscar.setText("");
        }
    }//GEN-LAST:event_txtBuscarFocusGained

    private void txtBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusLost
        if (txtBuscar.getText().length() == 0) {
            txtBuscar.setText("Buscar Facturas...");
        }
    }//GEN-LAST:event_txtBuscarFocusLost

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        mostrar(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        if (seleccionados(0)) {
            for (int i = 0; i < tblListaFacturas.getRowCount(); i++) {
                boolean sel = (boolean) tblListaFacturas.getValueAt(i, 0);
                if (sel) {
                    codigo.add(tblListaFacturas.getValueAt(i, 1).toString());
                    Collections.sort(codigo, Comparator.reverseOrder());
                }
            }
            System.out.println("boletas: " + codigo);
            datos();

        } else {
            mensaje = "Seleccione al menos 1";
            advertencia();
        }
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void tblListaFacturasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblListaFacturasKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false; // Marcar que Shift ya no está presionado
        }
    }//GEN-LAST:event_tblListaFacturasKeyReleased

    private void btnFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturaActionPerformed
        generarFacturas();
    }//GEN-LAST:event_btnFacturaActionPerformed

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

    private void validarCampos() {
        if (txtDesde.getText().length() == 0) {
            mensaje = "Ingrese un numero de boleta en Desde";
            advertencia();
        }

        if (txtHasta.getText().length() == 0) {
            mensaje = "Ingrese un numero de boleta en Hasta";
            advertencia();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFactura;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnMostrar;
    private Componentes.InternalPanel internalPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblFondoBuscador;
    private Componentes.panelGris panelGris1;
    private javax.swing.JTable tbl2;
    private javax.swing.JTable tblListaFacturas;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
