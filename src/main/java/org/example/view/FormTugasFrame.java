package org.example.view;

import org.example.model.Tugas;
import org.example.util.FileManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FormTugasFrame extends JFrame {

    public FormTugasFrame() {

        Color PRIMARY = new Color(79, 195, 247);
        Color BG = new Color(227, 242, 253);

        setTitle("Pengingat Baru");
        setSize(360, 640);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Pengingat baru");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField tfMK = field();
        tfMK.setBorder(BorderFactory.createTitledBorder("Mata Kuliah"));

        JTextField tfJudul = field();
        tfJudul.setBorder(BorderFactory.createTitledBorder("Judul"));

        JTextField tfDeadline = field();
        tfDeadline.setBorder(BorderFactory.createTitledBorder("Waktu (yyyy-MM-dd)"));

        JButton btnSave = new JButton("Simpan");
        btnSave.setBackground(PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnCancel = new JButton("Batalkan");
        btnCancel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            if (tfMK.getText().isEmpty() || tfJudul.getText().isEmpty() || tfDeadline.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mata kuliah, judul, dan waktu wajib diisi");
                return;
            }

            try {
                ArrayList<Tugas> list = FileManager.loadData();
                int id = list.isEmpty() ? 1 : list.get(list.size() - 1).getId() + 1;

                list.add(new Tugas(
                        id,
                        tfMK.getText(),
                        tfJudul.getText(),
                        LocalDate.parse(tfDeadline.getText()),
                        "Belum",
                        ""
                ));

                FileManager.saveData(list);
                JOptionPane.showMessageDialog(this, "Tugas berhasil disimpan");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-MM-dd");
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tfMK);
        panel.add(Box.createVerticalStrut(15));
        panel.add(tfJudul);
        panel.add(Box.createVerticalStrut(15));
        panel.add(tfDeadline);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnCancel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSave);

        add(panel);
        setVisible(true);
    }

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return tf;
    }
}
