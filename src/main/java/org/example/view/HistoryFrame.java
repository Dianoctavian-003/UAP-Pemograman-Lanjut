package org.example.view;

import org.example.model.Tugas;
import org.example.util.FileManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HistoryFrame extends JFrame {

    public HistoryFrame() {

        setTitle("Riwayat Tugas Selesai");
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color SKY = new Color(227, 242, 253);
        getContentPane().setBackground(SKY);

        // ===== HEADER =====
        JLabel title = new JLabel("Riwayat Tugas Selesai", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(title, BorderLayout.NORTH);

        // ===== TABLE =====
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mata Kuliah", "Judul", "Deadline"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false; // HISTORY READ-ONLY
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(SKY);
        table.getTableHeader().setBackground(new Color(144, 202, 249));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(SKY);

        add(scroll, BorderLayout.CENTER);

        // ===== LOAD DATA (SELESAI SAJA) =====
        ArrayList<Tugas> list = FileManager.loadData();

        for (Tugas t : list) {
            if (t.getStatus().equalsIgnoreCase("Selesai")) {
                model.addRow(new Object[]{
                        t.getMataKuliah(),
                        t.getJudul(),
                        t.getDeadline()
                });
            }
        }

        // ===== BUTTON BACK =====
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.setBackground(SKY);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
