package fr.turgot.dao.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprunt")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ressource_id", nullable = false)
    private Ressource ressource;

    @Column(name = "date_emprunt", nullable = false)
    private LocalDate dateEmprunt;

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_effective")
    private LocalDate dateRetourEffective;

    public Emprunt(User user, Ressource ressource, int dureeJours) {
        this.user            = user;
        this.ressource       = ressource;
        this.dateEmprunt     = LocalDate.now();
        this.dateRetourPrevue = LocalDate.now().plusDays(dureeJours);
    }

    protected Emprunt() {}

    public int getId() { return id; }
    public User getUser() { return user; }
    public Ressource getRessource() { return ressource; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }

    public void setDateRetourEffective(LocalDate d) { this.dateRetourEffective = d; }

    public boolean isOngoing() { return dateRetourEffective == null; }
    public boolean isLate() {
        return isOngoing() && LocalDate.now().isAfter(dateRetourPrevue);
    }
}