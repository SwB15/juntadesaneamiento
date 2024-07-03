
package Componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author SwichBlade15
 */
public class InternalPanel extends JPanel{
    
Panel2 panelSecundario = new Panel2();
    int borderWidth = 7;
    int borderRadius = 30;
    Color borderColor = new Color(0, 102, 255);
    Color fillColor = new Color(0, 102, 255);
    Font titleFont = new Font("Tahoma", Font.BOLD, 30);
    JLabel leftLabel = new JLabel("Imprimir en Lote");
    JLabel rightLabel = new JLabel();
    ImageIcon rightLabelIcon = new ImageIcon(getClass().getResource("/Imagenes/Cerrar32.png"));
    int rightLabelIconWidth = 32;
    int rightLabelIconHeight = 32;

    public InternalPanel() {
        // Configurar el panel principal
        this.setBorder(null);
        this.setOpaque(true);
        this.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.setForeground(Color.black);
        this.setBackground(new Color(0, 0, 0, 0));
        setLayout(null);
        this.setMinimumSize(new Dimension(300, 200));

        // Configurar el panel secundario
        panelSecundario.setOpaque(false);
        add(panelSecundario);

        // Configurar los JLabel
        leftLabel.setForeground(Color.WHITE);
        leftLabel.setFont(titleFont);

        rightLabel.setIcon(resizeIcon(rightLabelIcon, rightLabelIconWidth, rightLabelIconHeight));
        rightLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    JInternalFrame internalFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, InternalPanel.this);
                    if (internalFrame != null) {
                        internalFrame.dispose();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        add(leftLabel);
        add(rightLabel);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustSecondaryPanelSize();
                adjustLabelPositions();
            }
        });

        adjustSecondaryPanelSize();
        adjustLabelPositions();
    }

    // Métodos getters y setters para las propiedades...
    // Incluyendo getters y setters para rightLabelIcon, rightLabelIconWidth, rightLabelIconHeight, y métodos para cambiar el color del icono

    private void adjustSecondaryPanelSize() {
        int mainWidth = getWidth();
        int mainHeight = getHeight();

        int topMargin = 60; 
        int sideBottomMargin = borderWidth;

        int newWidth = mainWidth - (2 * sideBottomMargin);
        int newHeight = mainHeight - topMargin - sideBottomMargin;

        panelSecundario.setBounds(sideBottomMargin, topMargin, newWidth, newHeight);
        panelSecundario.revalidate();
        panelSecundario.repaint();
    }

    private void adjustLabelPositions() {
        int mainWidth = getWidth();
        int topMargin = 30;

        FontMetrics fm = leftLabel.getFontMetrics(leftLabel.getFont());
        int textWidth = fm.stringWidth(leftLabel.getText());

        leftLabel.setBounds(20, (topMargin / 2) - 2, textWidth + 10, 35);

        // Usar el tamaño actual del icono para ajustar la posición del rightLabel
        int rightLabelWidth = rightLabelIconWidth;
        int rightLabelHeight = rightLabelIconHeight;
        rightLabel.setBounds(mainWidth - rightLabelWidth - 10, (topMargin / 2) - 3, rightLabelWidth, rightLabelHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        g2.setColor(fillColor);
        g2.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius - borderWidth, borderRadius - borderWidth);

        g2.dispose();
    }

    // Método para redimensionar el icono
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon == null) return null;
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // Método para cambiar el color del icono
    public void setRightLabelIconColor(Color color) {
        if (rightLabelIcon == null) return;

        ImageIcon icon = resizeIcon(rightLabelIcon, rightLabelIconWidth, rightLabelIconHeight);
        BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                Color c = new Color(rgba, true);
                if (c.getAlpha() > 0) {
                    int newRed = (c.getRed() * color.getRed()) / 255;
                    int newGreen = (c.getGreen() * color.getGreen()) / 255;
                    int newBlue = (c.getBlue() * color.getBlue()) / 255;
                    int newColor = new Color(newRed, newGreen, newBlue, c.getAlpha()).getRGB();
                    img.setRGB(x, y, newColor);
                }
            }
        }

        rightLabel.setIcon(new ImageIcon(img));
        repaint();
    }
}

class Panel2 extends JPanel {

    private int borderWidth = 1;
    private int borderRadius = 23;
    private Color borderColor = Color.WHITE;
    private Color fillColor = Color.WHITE;

    public Panel2() {
        this.setBorder(null);
        this.setOpaque(false);
        this.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.setForeground(Color.black);

        this.setMinimumSize(new Dimension(150, 100));
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        g2.setColor(fillColor);
        g2.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius - borderWidth, borderRadius - borderWidth);
        g2.dispose();
    }
}
