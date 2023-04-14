package it.prova.gestioneordiniarticolicategorie.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "ordine")
public class Ordine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nomedestinatario")
	private String nomeDestinatario;
	@Column(name = "indirizzospedizione")
	private String indirizzoSpedizione;
	@Column(name = "dataspedizione")
	private LocalDate dataSpedizione;
	@Column(name = "datascadenza")
	private LocalDate dataScadenza;

	// campi per le time info del record
	@CreationTimestamp
	@Column(name = "createdatetime")
	private LocalDateTime createDateTime;
	@UpdateTimestamp
	@Column(name = "updatedatetime")
	private LocalDateTime updateDateTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ordine")
	private Set<Articolo> articoli = new HashSet<>();

	// costruttori
	public Ordine() {
		super();
	}

	public Ordine(String nomeDestinatario, String indirizzoSpedizione) {
		super();
		this.nomeDestinatario = nomeDestinatario;
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public Ordine(String nomeDestinatario, String indirizzoSpedizione, LocalDate dataSpedizione) {
		super();
		this.nomeDestinatario = nomeDestinatario;
		this.indirizzoSpedizione = indirizzoSpedizione;
		this.dataSpedizione = dataSpedizione;
	}

	public Ordine(String nomeDestinatario, String indirizzoSpedizione, LocalDate dataSpedizione,
			LocalDate dataScadenza) {
		super();
		this.nomeDestinatario = nomeDestinatario;
		this.indirizzoSpedizione = indirizzoSpedizione;
		this.dataSpedizione = dataSpedizione;
		this.dataScadenza = dataScadenza;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(String indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public LocalDate getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(LocalDate dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public LocalDate getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(LocalDate dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Set<Articolo> getArticoli() {
		return articoli;
	}

	public void setArticoli(Set<Articolo> articoli) {
		this.articoli = articoli;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	@Override
	public String toString() {

		String dataSpedizioneString = dataSpedizione != null
				? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataSpedizione)
				: " N.D.";

		String dataScadenzaString = dataScadenza != null
				? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataScadenza)
				: " N.D.";

		return "Ordine [id = " + id + ", nomeDestinatario = " + nomeDestinatario + ", indirizzoSpedizione = "
				+ indirizzoSpedizione + ", dataSpedizione = " + dataSpedizioneString + ", dataScadenza = "
				+ dataScadenzaString + "]";
	}

}