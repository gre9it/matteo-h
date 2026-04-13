package fr.turgot.dao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "professeur")
@PrimaryKeyJoinColumn(name = "id")
public class Professeur extends User {

    private String departement;

    public String getDepartement() { return departement; }
    public void setDepartement(String d) { this.departement = d; }
}