package org.example.util;
import org.example.model.Tugas;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager {

    private static final String FILE_NAME = "tugas.csv";

    public static ArrayList<Tugas> loadData() {
        ArrayList<Tugas> list = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);

                String checklist = data.length > 5 ? data[5] : "";

                Tugas t = new Tugas(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        LocalDate.parse(data[3]),
                        data[4],
                        checklist
                );

                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void saveData(ArrayList<Tugas> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {

            pw.println("id,mataKuliah,judul,deadline,status,checklist");

            for (Tugas t : list) {
                pw.println(
                        t.getId() + "," +
                                t.getMataKuliah() + "," +
                                t.getJudul() + "," +
                                t.getDeadline() + "," +
                                t.getStatus() + "," +
                                t.getChecklist()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
