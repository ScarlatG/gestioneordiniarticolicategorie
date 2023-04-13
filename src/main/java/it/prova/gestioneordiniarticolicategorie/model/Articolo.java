package it.prova.gestioneordiniarticolicategorie.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Articolo")
public class Articolo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "descrizione")
	private String descrizione;
	@Column(name = "numeroseriale")
	private String numeroSeriale;
	@Column(name = "prezzosingolo")
	private double prezzoSingolo;
	@Column(name = "datainserimento")
	private LocalDate dataInserimento;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ordine_id", nullable = false)
	private Ordine ordine;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "categorie")
	private Set<Categoria> categorie = new HashSet<Categoria>();

	// Costruttore vuoto
	public Articolo() {

	}

	public Articolo(String descrizione, String numeroSeriale) {
		super();
		this.descrizione = descrizione;
		this.numeroSeriale = numeroSeriale;
	}

	public Articolo(String descrizione, String numeroSeriale, double prezzoSingolo) {
		super();
		this.descrizione = descrizione;
		this.numeroSeriale = numeroSeriale;
		this.prezzoSingolo = prezzoSingolo;
	}

	public Articolo(String descrizione, String numeroSeriale, double prezzoSingolo, LocalDate dataInserimento) {
		super();
		this.descrizione = descrizione;
		this.numeroSeriale = numeroSeriale;
		this.prezzoSingolo = prezzoSingolo;
		this.dataInserimento = dataInserimento;
	}

	public Articolo(String descrizione, String numeroSeriale, double prezzoSingolo, LocalDate dataInserimento,
			Ordine ordine) {
		super();
		this.descrizione = descrizione;
		this.numeroSeriale = numeroSeriale;
		this.prezzoSingolo = prezzoSingolo;
		this.dataInserimento = dataInserimento;
		this.ordine = ordine;
	}

	public Articolo(String descrizione, String numeroSeriale, double prezzoSingolo, LocalDate dataInserimento,
			Ordine ordine, Set<Categoria> categorie) {
		super();
		this.descrizione = descrizione;
		this.numeroSeriale = numeroSeriale;
		this.prezzoSingolo = prezzoSingolo;
		this.dataInserimento = dataInserimento;
		this.ordine = ordine;
		this.categorie = categorie;
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

	public String getNumeroSeriale() {
		return numeroSeriale;
	}

	public void setNumeroSeriale(String numeroSeriale) {
		this.numeroSeriale = numeroSeriale;
	}

	public double getPrezzoSingolo() {
		return prezzoSingolo;
	}

	public void setPrezzoSingolo(double prezzoSingolo) {
		this.prezzoSingolo = prezzoSingolo;
	}

	public LocalDate getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDate dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public Set<Categoria> getCategorie() {
		return categorie;
	}

	public void setCategorie(Set<Categoria> categorie) {
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "Articolo [id=" + id + ", descrizione=" + descrizione + ", numeroSeriale=" + numeroSeriale
				+ ", prezzoSingolo=" + prezzoSingolo + ", dataInserimento=" + dataInserimento + ", ordine=" + ordine
				+ ", categorie=" + categorie + "]";
	}

}
