package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;

public interface ArticoloService {

	public List<Articolo> listAll() throws Exception;

	public void aggiorna(Articolo articoloInstance) throws Exception;

	public void inserisciNuovo(Articolo articoloInstance) throws Exception;

	public void rimuovi(Long idArticolo) throws Exception;

	public Articolo caricaSingoloElemento(Long idArticolo) throws Exception;

	// Per injection
	public void setArticoloDAO(ArticoloDAO articoloDAO);
}
