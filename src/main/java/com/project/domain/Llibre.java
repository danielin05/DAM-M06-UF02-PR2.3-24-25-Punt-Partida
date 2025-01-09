package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "llibres")
public class Llibre implements Serializable {
    
    private long llibreId;
    private String isbn;
    private String titol;
    private String editorial;
    private Integer anyPublicacio;
    private Set<Autor> autors = new HashSet<>();
    private Set<Exemplar> exemplars = new HashSet<>();
    
    public long getLlibreId() {
        return llibreId;
    }
    public void setLlibreId(long llibreId) {
        this.llibreId = llibreId;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitol() {
        return titol;
    }
    public void setTitol(String titol) {
        this.titol = titol;
    }
    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    public Integer getAnyPublicacio() {
        return anyPublicacio;
    }
    public void setAnyPublicacio(Integer anyPublicacio) {
        this.anyPublicacio = anyPublicacio;
    }
    public Set<Autor> getAutors() {
        return autors;
    }
    public void setAutors(Set<Autor> autors) {
        this.autors = autors;
    }
    public Set<Exemplar> getExemplars() {
        return exemplars;
    }
    public void setExemplars(Set<Exemplar> exemplars) {
        this.exemplars = exemplars;
    }

    public Llibre(String isbn, String titol, String editorial, Integer anyPublicacio) {
        this.isbn = isbn;
        this.titol = titol;
        this.editorial = editorial;
        this.anyPublicacio = anyPublicacio;
    }

    public Llibre(long llibreId, String isbn, String titol, String editorial, Integer anyPublicacio, Set<Autor> autors,
            Set<Exemplar> exemplars) {
        this.llibreId = llibreId;
        this.isbn = isbn;
        this.titol = titol;
        this.editorial = editorial;
        this.anyPublicacio = anyPublicacio;
        this.autors = autors;
        this.exemplars = exemplars;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Llibre[id=%d, isbn='%s', titol='%s'", 
            llibreId, isbn, titol));
        
        if (editorial != null) {
            sb.append(String.format(", editorial='%s'", editorial));
        }
        if (anyPublicacio != null) {
            sb.append(String.format(", any=%d", anyPublicacio));
        }
        
        if (!autors.isEmpty()) {
            sb.append(", autors={");
            boolean first = true;
            for (Autor a : autors) {
                if (!first) sb.append(", ");
                sb.append(a.getNom());
                first = false;
            }
            sb.append("}");
        }
        
        if (!exemplars.isEmpty()) {
            sb.append(", exemplars={");
            boolean first = true;
            for (Exemplar e : exemplars) {
                if (!first) sb.append(", ");
                sb.append(e.getCodiBarres());
                first = false;
            }
            sb.append("}");
        }
        
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Llibre llibre = (Llibre) o;
        return llibreId == llibre.llibreId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(llibreId);
    }
}