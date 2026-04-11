package fr.turgot.dao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bibliothecaires")
@PrimaryKeyJoinColumn(name = "user_id")
public class Bibliothecaire extends User {

    @Column(name = "bureau", length = 50)
    private String bureau;

    public Bibliothecaire() {
        super();
        setRole(Role.BIBLIOTHECAIRE);
    }

    public Bibliothecaire(String username, String password, String bureau) {
        super(username, password, Role.BIBLIOTHECAIRE);
        this.bureau = bureau;
    }

    public String getBureau() { return bureau; }
    public void setBureau(String bureau) { this.bureau = bureau; }
}
