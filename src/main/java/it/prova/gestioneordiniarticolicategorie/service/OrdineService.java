package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.OrdineDAO;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineService {

	public List<Ordine> listAll() throws Exception;

	public void aggiorna(Ordine ordineInstance) throws Exception;

	public void inserisciNuovo(Ordine ordineInstance) throws Exception;

	public void rimuovi(Long idOrdine) throws Exception;

	// Per injection
	public void setOrdineDAO(OrdineDAO ordineDAO);
}