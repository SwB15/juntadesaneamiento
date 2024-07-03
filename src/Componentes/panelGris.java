
package Componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author SwichBlade15
 */
public class panelGris extends JPanel{
    int borderWidth = 3;
    int borderRadius = 30;
    Color borderColor = new Color(190,190,190);
    Color backgroundColor = new Color(255,255,255);

    public panelGris() {
        this.setOpaque(false);
        this.setBorder(null);
        this.setBackground(null);
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius - borderWidth, borderRadius - borderWidth);

        g2.dispose();
    }
}
