package fr.turgot.dao.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "ressource")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)

public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "date_parution")
    private LocalDate dateParution;

    private String auteur;

    private int availableCopies;


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDateParution() { return dateParution; }
    public String getAuteur() { return auteur; }
    public int getAvailableCopies() { return availableCopies; }


    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDateParution(LocalDate dateParution) { this.dateParution = dateParution; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public void borrow() {
        if (availableCopies == 0) {
            throw new IllegalStateException("No copies available");
        }
        availableCopies--;
    }

    public void giveBack() {
        availableCopies++;
    }
}