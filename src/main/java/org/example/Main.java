package org.example;

import javax.swing.SwingUtilities;
import org.example.view.DashboardFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardFrame::new);
    }
}
