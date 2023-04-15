package it.prova.gestioneordiniarticolicategorie.service;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.OrdineDAO;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineService {

	public List<Ordine> listAll() throws Exception;

	public void aggiorna(Ordine ordineInstance) throws Exception;

	public void inserisciNuovo(Ordine ordineInstance) throws Exception;

	public void rimuovi(Long idOrdine) throws Exception;

	public Ordine caricaSingoloElemento(Long idOrdine) throws Exception;

	public List<Ordine> trovaOrdiniPerCategoria(String codiceCategoria) throws Exception;

	public Ordine trovaOrdinePiuRecentePerCategoria(LocalDate data, String codiceCategoria) throws Exception;

	public List<String> trovaIndirizziPerNumeroSeriale(String numeroSeriale) throws Exception;

	// per injection
	public void setOrdineDAO(OrdineDAO ordineDAO);

	public void setArticoloDAO(ArticoloDAO articoloDAO);
}
