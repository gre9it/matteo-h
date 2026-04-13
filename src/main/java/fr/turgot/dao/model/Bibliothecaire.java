package fr.turgot.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "bibliothecaire")
@PrimaryKeyJoinColumn(name = "id")
public class Bibliothecaire extends User {

    @Column(name = "bureau")
    private String bureau;

    public Bibliothecaire() {
        this.setRole(Role.BIBLIOTHECAIRE);
    }

    public String getBureau() { return bureau; }
    public void setBureau(String b) { this.bureau = b; }
}