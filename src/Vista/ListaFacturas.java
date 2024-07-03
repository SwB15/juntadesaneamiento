package Vista;

import Controlador.Conexion;
import Funciones.FuncionesFacturas;
import Vista.Notificaciones.Aceptar_Cancelar;
import Vista.Notificaciones.Advertencia;
import Vista.Notificaciones.Fallo;
import Vista.Notificaciones.Realizado;
import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
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
    boolean keyShift = false;
    int seleccionar = -1;
    String idFacturas;
    boolean shiftPressed = false;
    private final Connection cn = Conexion.getConnection();
    ArrayList<String> codigo = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    Map<String, List<Map<String, Object>>> clienteBoletasMap = new HashMap<>();

    public ListaFacturas() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setBorder(null);
        this.setOpaque(false);
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

    //Obtiene la totalidad de los datos de la factura seleccionada en la tabla
    private void registros(int buscar) {
        try {
            // Obtener modelo de datos para el código 'buscar'
            DefaultTableModel modelo2 = funcion.registros(buscar);

            // Verificar y configurar columnas de modelo3 si es necesario
            if (modelo3.getColumnCount() == 0) {
                // Configurar modelo3 con las mismas columnas que modelo2
                for (int col = 0; col < modelo2.getColumnCount(); col++) {
                    modelo3.addColumn(modelo2.getColumnName(col));
                }
                // Añadir columnas adicionales para "Consumo Total (C.T.)" y "Cierre"
                modelo3.addColumn("Consumo Total (C.T.)");
                modelo3.addColumn("Cierre");
            }

            // Asegurarse de que modelo2 tiene datos antes de procesar
            if (modelo2 != null && modelo2.getRowCount() > 0) {
                // Recorrer cada fila del modelo2 y agregarla a modelo3
                for (int i = 0; i < modelo2.getRowCount(); i++) {
                    Vector<Object> filaData = new Vector<>();
                    for (int j = 0; j < modelo2.getColumnCount(); j++) {
                        filaData.add(modelo2.getValueAt(i, j));
                    }

                    // Obtener los valores necesarios para los cálculos
                    int consumoMinimo = Integer.parseInt(modelo2.getValueAt(i, 10).toString()); // Ajusta el índice según tu tabla
                    int consumoExcedente = Integer.parseInt(modelo2.getValueAt(i, 11).toString()); // Ajusta el índice según tu tabla
                    int estadoInicio = Integer.parseInt(modelo2.getValueAt(i, 9).toString()); // Ajusta el índice según tu tabla
                    int estadoCierre = Integer.parseInt(modelo2.getValueAt(i, 8).toString()); // Ajusta el índice según tu tabla

                    // Calcular el Consumo Total
                    int consumoTotal = consumoMinimo + consumoExcedente;

                    // Calcular el Cierre
                    int cierre = estadoCierre - estadoInicio;
                    if (cierre > 10) {
                        cierre = cierre - 10; // Ajuste basado en tu lógica
                    } else {
                        cierre = 0;
                    }

                    // Añadir los valores calculados al vector de datos de la fila
                    filaData.add(consumoTotal); // Añadir "Consumo Total (C.T.)"
                    filaData.add(cierre); // Añadir "Cierre"

                    // Añadir la fila completa (con datos calculados) a modelo3
                    modelo3.addRow(filaData);
                }
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

    private void consumo(int minimo, int excedente, int cierreMedidor, int inicioMedidor) {
        int consumoTotal = minimo + excedente;

        int cierre = cierreMedidor - inicioMedidor;
        if (cierre > 10) {
            int cierre2 = cierre - 10;
        }
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
        generarFacturas();
    }

    public void generarFacturas() {
        // Obtener el número de filas en tbl2
        int rowCount = tbl2.getRowCount();

        // Verificar si tbl2 tiene datos
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para generar facturas.");
            return;
        }

        // Ruta y configuración del logo
        final String rutaLogo = "src/Imagenes/Icono.png";
        Path rutaLogo2 = Paths.get(rutaLogo);
        String rutaLogo4 = rutaLogo2.toAbsolutePath().toString();

        // Ruta del reporte JRXML
        final String rutaReporte = "src/Reportes/Facturas.jrxml";
        Path rutaRelativaReporte = Paths.get(rutaReporte);
        String rutaAbsolutaReporte = rutaRelativaReporte.toAbsolutePath().toString();

        // Iterar sobre cada fila en tbl2
        for (int i = 0; i < rowCount; i++) {
            try {
                // Obtener los datos de la fila
                String direccion = tbl2.getValueAt(i, 0).toString();
                String mes = tbl2.getValueAt(i, 1).toString();
                String numeroUsuario = tbl2.getValueAt(i, 2).toString();
                String boleta = tbl2.getValueAt(i, 3).toString();
                String importeMinimo = tbl2.getValueAt(i, 4).toString();
                String importeAtrasos = tbl2.getValueAt(i, 5).toString();
                String importeExcedentes = tbl2.getValueAt(i, 6).toString();
                String importeIva = tbl2.getValueAt(i, 7).toString();
                String importeTotal = tbl2.getValueAt(i, 8).toString();
                String vencimiento = tbl2.getValueAt(i, 9).toString();
                String cliente = tbl2.getValueAt(i, 10).toString();
                String fechaInicio = tbl2.getValueAt(i, 11).toString();
                String fechaCierre = tbl2.getValueAt(i, 12).toString();
                String estadoInicio = tbl2.getValueAt(i, 13).toString();
                String estadoCierre = tbl2.getValueAt(i, 14).toString();
                String consumoMinimo = tbl2.getValueAt(i, 15).toString();
                String consumoExcedente = tbl2.getValueAt(i, 16).toString();
                String consumoTotal = tbl2.getValueAt(i, 17).toString();
                String importeConexion = tbl2.getValueAt(i, 18).toString();
                String importeMedidor = tbl2.getValueAt(i, 19).toString();

                // Configurar los parámetros del reporte
                Map<String, Object> map = new HashMap<>();
                map.put("direccion", direccion);
                map.put("mes", mes);
                map.put("numerousuario", numeroUsuario);
                map.put("boleta", boleta);
                map.put("importeminimo", importeMinimo);
                map.put("importeatrasos", importeAtrasos);
                map.put("importeexcedentes", importeExcedentes);
                map.put("importeiva", importeIva);
                map.put("importetotal", importeTotal);
                map.put("vencimiento", vencimiento);
                map.put("cliente", cliente);
                map.put("fechainicio", fechaInicio);
                map.put("fechacierre", fechaCierre);
                map.put("estadoinicio", estadoInicio);
                map.put("estadocierre", estadoCierre);
                map.put("consumominimo", consumoMinimo);
                map.put("consumoexcedente", consumoExcedente);
                map.put("consumototal", consumoTotal);
                map.put("importeconexion", importeConexion);
                map.put("importemedidor", importeMedidor);
                map.put("logo", rutaLogo4);

                // Compilar y llenar el reporte
                JasperReport reporte = JasperCompileManager.compileReport(rutaAbsolutaReporte);
                JasperPrint jp = JasperFillManager.fillReport(reporte, map, cn);

                // Opcional: Guardar cada reporte en un archivo PDF
//            String nombreArchivo = "Factura_" + numeroUsuario + ".pdf";
//            JasperExportManager.exportReportToPdfFile(jp, "salidas/" + nombreArchivo);
//            System.out.println("Reporte generado: " + nombreArchivo);
                // Opcional: Mostrar el reporte en pantalla
                JasperViewer view = new JasperViewer(jp, false);
                view.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/Icono.png")).getImage());
                view.setTitle("Facturas");
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);

            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Error al generar reporte para el cliente " + i + ": " + e.getMessage());
            }
        }

        // Mensaje final de éxito
        JOptionPane.showMessageDialog(null, "Se han generado todas las facturas exitosamente.");
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
        btnMostrar = new javax.swing.JButton();
        internalPanel2 = new Componentes.InternalPanel();

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 417, 970, 170));

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

        btnMostrar.setText("jButton1");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        internalPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                internalPanel2MouseClicked(evt);
            }
        });
        getContentPane().add(internalPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 610));

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

    private void internalPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_internalPanel2MouseClicked
        if (evt.getClickCount() == 2) {
            shiftPressed = false;
            for (int i = 0; i < tblListaFacturas.getRowCount(); i++) {
                // Deseleccionar todos los checkboxes
                tblListaFacturas.setValueAt(false, i, 0); // Deseleccionar el checkbox
            }
        }
    }//GEN-LAST:event_internalPanel2MouseClicked

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
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnMostrar;
    private Componentes.InternalPanel internalPanel1;
    private Componentes.InternalPanel internalPanel2;
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
