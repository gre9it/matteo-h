package fr.turgot.dao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "etudiants")
@PrimaryKeyJoinColumn(name = "user_id")
public class Etudiant extends User {

    @Column(name = "numero_etudiant", length = 50)
    private String numeroEtudiant;

    @Column(name = "filiere", length = 100)
    private String filiere;

    public Etudiant() {
        super();
        setRole(Role.ETUDIANT);
    }

    public Etudiant(String username, String password, String numeroEtudiant, String filiere) {
        super(username, password, Role.ETUDIANT);
        this.numeroEtudiant = numeroEtudiant;
        this.filiere = filiere;
    }

    public String getNumeroEtudiant() { return numeroEtudiant; }
    public void setNumeroEtudiant(String numeroEtudiant) { this.numeroEtudiant = numeroEtudiant; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
}
