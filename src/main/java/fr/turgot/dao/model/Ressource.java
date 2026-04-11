package fr.turgot.dao.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ressources")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_parution")
    private LocalDate dateParution;

    @Column(name = "auteur", length = 200)
    private String auteur;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    public Ressource() {}

    public Ressource(String title, String description, LocalDate dateParution, String auteur, int availableCopies) {
        this.title = title;
        this.description = description;
        this.dateParution = dateParution;
        this.auteur = auteur;
        this.availableCopies = availableCopies;
    }

    /**
     * Emprunte un exemplaire. Retourne false si aucun exemplaire disponible.
     */
    public boolean borrow() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    /**
     * Rend un exemplaire.
     */
    public void giveBack() {
        availableCopies++;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateParution() { return dateParution; }
    public void setDateParution(LocalDate dateParution) { this.dateParution = dateParution; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    /**
     * Retourne le type lisible pour l'affichage JSP.
     */
    public abstract String getType();
}
