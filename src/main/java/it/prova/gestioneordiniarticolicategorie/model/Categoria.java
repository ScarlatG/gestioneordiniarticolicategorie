package it.prova.gestioneordiniarticolicategorie.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "descrizione")
	private String descrizione;
	@Column(name = "codice")
	private String codice;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "categoria_articolo", joinColumns = @JoinColumn(name = "categoria_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "articolo_id", referencedColumnName = "ID"))
	private Set<Articolo> articoli = new HashSet<Articolo>();

	// Costruttore vuoto
	public Categoria() {

	}

	public Categoria(String descrizione, String codice, Set<Articolo> articoli) {
		super();
		this.descrizione = descrizione;
		this.codice = codice;
		this.articoli = articoli;
	}

	public Categoria(String descrizione, String codice) {
		super();
		this.descrizione = descrizione;
		this.codice = codice;
	}

	public Categoria(Set<Articolo> articoli) {
		super();
		this.articoli = articoli;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Set<Articolo> getArticoli() {
		return articoli;
	}

	public void setArticoli(Set<Articolo> articoli) {
		this.articoli = articoli;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", descrizione=" + descrizione + ", codice=" + codice + ", articoli=" + articoli
				+ "]";
	}

}
