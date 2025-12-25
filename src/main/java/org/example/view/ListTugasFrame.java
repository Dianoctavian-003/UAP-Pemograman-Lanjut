package org.example.view;

import org.example.model.Tugas;
import org.example.util.FileManager;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ListTugasFrame extends JFrame {

    private final ArrayList<Tugas> list;
    private final Color SKY = new Color(227, 242, 253);

    public ListTugasFrame() {

        setTitle("Daftar Tugas");
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(SKY);

        JButton btnBack = new JButton("← Back");
        btnBack.setBackground(new Color(100, 181, 246));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(SKY);
        topPanel.add(btnBack);

        add(topPanel, BorderLayout.NORTH);

        list = FileManager.loadData();

        DefaultTableModel model = new DefaultTableModel(
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
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(SKY);
        table.setSelectionBackground(new Color(179, 229, 252));

        table.getTableHeader().setBackground(new Color(144, 202, 249));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(SKY);

        add(scroll, BorderLayout.CENTER);

        for (Tugas t : list) {
            model.addRow(new Object[]{
                    t.getMataKuliah(),
                    t.getJudul(),
                    t.getStatus().equalsIgnoreCase("Selesai")
            });
        }

        model.addTableModelListener(e -> {
            if (e.getType() != TableModelEvent.UPDATE) return;
            if (e.getColumn() != 2) return;

            int row = e.getFirstRow();
            boolean selesai = (Boolean) model.getValueAt(row, 2);
            list.get(row).setStatus(selesai ? "Selesai" : "Belum");
            FileManager.saveData(list);
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {

                if (e.getClickCount() == 2) {

                    System.out.println("DOUBLE CLICK TERDETEKSI");

                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (col == 2) return;

                    showEditDialog(row);
                }
            }
        });



        setVisible(true);
    }

    private void showEditDialog(int row) {

        Tugas t = list.get(row);

        JTextField tfJudul = new JTextField(t.getJudul());
        JTextField tfDeadline = new JTextField(t.getDeadline().toString());

        UIManager.put("OptionPane.background", SKY);
        UIManager.put("Panel.background", SKY);

        Object[] form = {
                "Judul:", tfJudul,
                "Deadline (yyyy-MM-dd):", tfDeadline
        };

        int option = JOptionPane.showOptionDialog(
                this,
                form,
                "Edit Tugas",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Update", "Delete", "Cancel"},
                "Update"
        );

        try {
            if (option == 0) { // UPDATE
                list.set(row, new Tugas(
                        t.getId(),
                        t.getMataKuliah(),
                        tfJudul.getText(),
                        LocalDate.parse(tfDeadline.getText()),
                        t.getStatus(),
                        t.getChecklist()
                ));
                FileManager.saveData(list);
                dispose();
                new ListTugasFrame();

            } else if (option == 1) { // DELETE
                list.remove(row);
                FileManager.saveData(list);
                dispose();
                new ListTugasFrame();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-MM-dd");
        }
    }
}
