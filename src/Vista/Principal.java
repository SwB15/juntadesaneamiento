
package Vista;

import Vista.Notificaciones.Aceptar_Cancelar;
import Vista.Notificaciones.Advertencia;
import Vista.Notificaciones.Fallo;
import Vista.Notificaciones.Realizado;
import java.awt.Dimension;
import java.awt.Frame;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author SwichBlade15
 */
public final class Principal extends javax.swing.JFrame {

    public Principal() {
        initComponents();
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setIconImage(new ImageIcon(getClass().getResource("/Imagenes/Icono.png")).getImage());
        cerrar();
        txtAceptarCancelar.setVisible(false);
        lblProceso.setVisible(false);
    }
    
    public void cerrar() {
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    encabezado = "Salir";
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        lblProceso = new javax.swing.JLabel();
        txtAceptarCancelar = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFacturas = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        mnuReportes = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Principal.png"));
        Image image = icon.getImage();
        jDesktopPane1 = new javax.swing.JDesktopPane(){
            public void paintComponent(Graphics g){
                g.drawImage(image,0,0,getWidth(),getHeight(),this);
            }
        };
        jDesktopPane1.setBackground(new java.awt.Color(204, 204, 204));

        lblProceso.setBackground(new java.awt.Color(255, 255, 255));
        lblProceso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProceso.setText("lblProceso");

        jDesktopPane1.setLayer(lblProceso, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(txtAceptarCancelar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(810, Short.MAX_VALUE)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblProceso, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(txtAceptarCancelar))
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAceptarCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(397, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        mnuFacturas.setForeground(new java.awt.Color(0, 102, 255));
        mnuFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/factura32.png"))); // NOI18N
        mnuFacturas.setText("Facturas");
        mnuFacturas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        mnuFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuFacturasMouseClicked(evt);
            }
        });

        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setText("Facturas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuFacturas.add(jMenuItem1);

        jMenuItem2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setText("Imprimir en Lote");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuFacturas.add(jMenuItem2);

        jMenuBar1.add(mnuFacturas);

        mnuClientes.setForeground(new java.awt.Color(0, 102, 255));
        mnuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Usuario32.png"))); // NOI18N
        mnuClientes.setText("Clientes");
        mnuClientes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        mnuClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuClientesMouseClicked(evt);
            }
        });
        jMenuBar1.add(mnuClientes);

        mnuReportes.setForeground(new java.awt.Color(0, 102, 255));
        mnuReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte32.png"))); // NOI18N
        mnuReportes.setText("Reportes");
        mnuReportes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        mnuReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuReportesMouseClicked(evt);
            }
        });
        jMenuBar1.add(mnuReportes);

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

    private void mnuFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuFacturasMouseClicked
        
    }//GEN-LAST:event_mnuFacturasMouseClicked

    private void mnuClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuClientesMouseClicked
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
    }//GEN-LAST:event_mnuClientesMouseClicked

    private void mnuReportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuReportesMouseClicked
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
    }//GEN-LAST:event_mnuReportesMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Facturas form = new Facturas();
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
        ListaFacturas form = new ListaFacturas();
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
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    public static javax.swing.JLabel lblProceso;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenu mnuFacturas;
    private javax.swing.JMenu mnuReportes;
    public static javax.swing.JTextField txtAceptarCancelar;
    // End of variables declaration//GEN-END:variables

}
