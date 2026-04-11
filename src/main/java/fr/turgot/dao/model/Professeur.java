package fr.turgot.dao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeurs")
@PrimaryKeyJoinColumn(name = "user_id")
public class Professeur extends User {

    @Column(name = "departement", length = 100)
    private String departement;

    public Professeur() {
        super();
        setRole(Role.PROFESSEUR);
    }

    public Professeur(String username, String password, String departement) {
        super(username, password, Role.PROFESSEUR);
        this.departement = departement;
    }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
}
