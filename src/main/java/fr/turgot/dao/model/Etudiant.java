package fr.turgot.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiant")
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends User {

    @Column(name = "numero_etudiant")
    private String numeroEtudiant;
    private String filiere;

    public String getNumeroEtudiant() { return numeroEtudiant; }
    public String getFiliere() { return filiere; }
    public void setNumeroEtudiant(String n) { this.numeroEtudiant = n; }
    public void setFiliere(String f) { this.filiere = f; }
}