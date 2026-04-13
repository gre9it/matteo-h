package fr.turgot.dao.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "livres")
@PrimaryKeyJoinColumn(name = "ressource_id")
@DiscriminatorValue("Livre")
public class Livre extends Ressource {

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "edition", length = 100)
    private String edition;

    public Livre() {}

    public Livre(String title, String description, LocalDate dateParution, String auteur,
                 int availableCopies, String genre, String edition) {
        super(title, description, dateParution, auteur, availableCopies);
        this.genre = genre;
        this.edition = edition;
    }

    @Override
    public String getType() { return "Livre"; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }
}
