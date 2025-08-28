package com.mycompany.lab3;

import javax.swing.SwingUtilities;
import view.GUI;

public class Lab3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI();
            app.setVisible(true);
        });
    }
}