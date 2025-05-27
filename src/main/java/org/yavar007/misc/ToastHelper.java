package org.yavar007.misc;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ToastHelper extends JFrame {
    // JWindow
    private JWindow w;

    public ToastHelper(String s, JFrame frame)
    {

        w = new JWindow();

        // make the background transparent
        w.setBackground(new Color(0, 0, 0, 0));
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/yekan.ttf")).deriveFont(12f);
            // Adjust the size as needed
            //
            } catch (Exception e) {
            System.out.println(e.getMessage());
            customFont = new Font("SansSerif", Font.PLAIN, 14);
            // Fallback font
            }
        Font finalCustomFont = customFont;
        // create a panel
        JPanel p = new JPanel() {
            public void paintComponent(Graphics g)
            {
                g.setFont(finalCustomFont);
                int wid = g.getFontMetrics().stringWidth(s);
                int hei = g.getFontMetrics().getHeight();
                // draw the boundary of the toast and fill it

                g.setColor(Color.black);
                g.fillRect(10, 10, wid + 30, hei);
                g.setColor(Color.black);
                g.drawRect(10, 10, wid + 30, hei );

                // set the color of text
                g.setColor(new Color(255, 255, 255, 240));
                g.drawString(s, 25, 27);
                int t = 250;

                // draw the shadow of the toast
                for (int i = 0; i < 4; i++) {
                    t -= 60;
                    g.setColor(new Color(0, 0, 0, t));
                    g.drawRect(10 - i, 10 - i, wid + 30 + i * 2,
                            hei + i * 2);
                }
            }
            @Override public Dimension getPreferredSize() {
                int wid = getFontMetrics(getFont()).stringWidth(s);
                int hei = getFontMetrics(getFont()).getHeight();
                return new Dimension(wid + 40, hei + 20);
            }
        };

        w.add(p);


        int toastWidth = p.getPreferredSize().width;
        int toastHeight = p.getPreferredSize().height;
        int posx=frame.getX();
        int x = frame.getX() + ((frame.getWidth() / 2) - (toastWidth / 2));
        w.setLocation(posx, (frame.getY()+frame.getHeight())-45);
        w.setSize(300, 100);
    }

    // function to pop up the toast
    public void showMessage()
    {
        try {
            w.setOpacity(1);
            w.setVisible(true);

            // wait for some time
            Thread.sleep(2000);

            // make the message disappear  slowly
            for (double d = 1.0; d > 0.2; d -= 0.1) {
                Thread.sleep(100);
                w.setOpacity((float)d);
            }

            // set the visibility to false
            w.setVisible(false);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
