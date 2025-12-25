package org.example.model;

import java.time.LocalDate;

public class Tugas {

    private int id;
    private String mataKuliah;
    private String judul;
    private LocalDate deadline;
    private String status;
    private String checklist;

    public Tugas(int id, String mataKuliah, String judul,
                 LocalDate deadline, String status, String checklist) {
        this.id = id;
        this.mataKuliah = mataKuliah;
        this.judul = judul;
        this.deadline = deadline;
        this.status = status;
        this.checklist = checklist;
    }

    public int getId() {
        return id;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public String getJudul() {
        return judul;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public String getChecklist() {
        return checklist;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setChecklist(String checklist) {
        this.checklist = checklist;
    }
}
