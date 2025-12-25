package org.example.view;

import org.example.model.Tugas;
import org.example.util.FileManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DashboardFrame extends JFrame {

    private JLabel lblHariIni;
    private JLabel lblTotal;
    private JLabel lblSelesai;
    private JLabel lblBelum;

    public DashboardFrame() {

        Color PRIMARY = new Color(79, 195, 247);
        Color BG = new Color(227, 242, 253);

        setTitle("Reminder Tugas");
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Reminder Tugas", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        cardPanel.setBackground(BG);

        lblHariIni = new JLabel();
        lblTotal = new JLabel();
        lblSelesai = new JLabel();
        lblBelum = new JLabel();

        cardPanel.add(createCard("Deadline Hari Ini", lblHariIni, "HARI_INI"));
        cardPanel.add(createCard("Semua Tugas", lblTotal, "ALL"));
        cardPanel.add(createCard("Selesai", lblSelesai, "SELESAI"));
        cardPanel.add(createCard("Belum Selesai", lblBelum, "BELUM"));

        mainPanel.add(cardPanel);
        mainPanel.add(Box.createVerticalStrut(40));

        JButton btnAdd = new JButton("+ Tambah Tugas");
        btnAdd.setBackground(PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setMaximumSize(new Dimension(220, 45));
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.addActionListener(e -> new FormTugasFrame());

        JButton btnExit = new JButton("Keluar");
        btnExit.setBackground(Color.GRAY);
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setMaximumSize(new Dimension(220, 45));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.addActionListener(e -> dispose());

        JButton btnHistory = new JButton("📜 History Tugas");
        btnHistory.setBackground(new Color(79, 195, 247));
        btnHistory.setForeground(Color.WHITE);
        btnHistory.setFocusPainted(false);
        btnHistory.setMaximumSize(new Dimension(220, 45));
        btnHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHistory.addActionListener(e -> new HistoryFrame());

        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnHistory);
        mainPanel.add(btnAdd);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnExit);

        add(mainPanel);

        refreshDashboard();

        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                refreshDashboard();
            }
        });

        setVisible(true);
    }

    private JPanel createCard(String title, JLabel valueLabel, String filter) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openTable(filter);
            }
        });

        return card;
    }

    private void refreshDashboard() {

        ArrayList<Tugas> list = FileManager.loadData();
        LocalDate today = LocalDate.now();

        int total = list.size();
        int selesai = 0;
        int hariIni = 0;

        for (Tugas t : list) {
            if (t.getStatus().equalsIgnoreCase("Selesai")) selesai++;
            if (t.getDeadline().equals(today)) hariIni++;
        }

        lblTotal.setText(String.valueOf(total));
        lblSelesai.setText(String.valueOf(selesai));
        lblBelum.setText(String.valueOf(total - selesai));
        lblHariIni.setText(String.valueOf(hariIni));
    }

    private void openTable(String filter) {
        new ListTugasFrame();

        JFrame frame = new JFrame("Daftar Tugas");
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(this);

        javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(
                        new Object[]{"Mata Kuliah", "Judul", "Selesai"}, 0
                ) {
                    public Class<?> getColumnClass(int c) {
                        return c == 2 ? Boolean.class : String.class;
                    }
                    public boolean isCellEditable(int r, int c) {
                        return c == 2;
                    }
                };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateRowSorter(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        ArrayList<Tugas> list = FileManager.loadData();
        LocalDate today = LocalDate.now();

        for (Tugas t : list) {
            boolean show =
                    filter.equals("ALL") ||
                            (filter.equals("SELESAI") && t.getStatus().equalsIgnoreCase("Selesai")) ||
                            (filter.equals("BELUM") && t.getStatus().equalsIgnoreCase("Belum")) ||
                            (filter.equals("HARI_INI") && t.getDeadline().equals(today));

            if (show) {
                model.addRow(new Object[]{
                        t.getMataKuliah(),
                        t.getJudul(),
                        t.getStatus().equalsIgnoreCase("Selesai")
                });
            }
        }

        model.addTableModelListener(e -> {
            if (e.getColumn() != 2) return;

            int row = e.getFirstRow();
            if (row < 0 || row >= list.size()) return;

            boolean selesai = (Boolean) model.getValueAt(row, 2);
            list.get(row).setStatus(selesai ? "Selesai" : "Belum");
            FileManager.saveData(list);

            refreshDashboard();
        });

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}
