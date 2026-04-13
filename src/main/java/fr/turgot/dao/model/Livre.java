package fr.turgot.dao.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "livre")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Livre")
public class Livre extends Ressource {

    private String genre;
    private String edition;

    public String getGenre() { return genre; }
    public String getEdition() { return edition; }
    public void setGenre(String g) { this.genre = g; }
    public void setEdition(String e) { this.edition = e; }
}