package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface ArticoloService {

	public List<Articolo> listAll() throws Exception;

	public void aggiorna(Articolo articoloInstance) throws Exception;

	public void inserisciNuovo(Articolo articoloInstance) throws Exception;

	public void rimuovi(Long idArticolo) throws Exception;

	public Articolo caricaSingoloElemento(Long idArticolo) throws Exception;

	public Articolo caricaArticoloEager(Long idArticolo) throws Exception;

	public void aggiungiCategoria(Articolo articoloInstance, Categoria categoriaInstance) throws Exception;

	public void rimozioneCompletaArticolo(Long idArticolo) throws Exception;

	public double sommaPrezziPerCategoria(String codiceCategoria) throws Exception;

	public double calcolaTotalePrezzoPerDestinatario(String destinatario) throws Exception;

	public List<Articolo> trovaArticoliInSituazioniStrane() throws Exception;

	public void aggiungiCategoriaAArticoloEsistente(Categoria categoria, Articolo articolo) throws Exception;

	public Articolo caricaSingoloElementoConCategorie(Long id) throws Exception;

	public void rimuoviArticoloPrevioScollegamento(Long id) throws Exception;

	public Double ottieniSommaPrezziDiArticoliDiUnaCategoria(Categoria categoria) throws Exception;

	public List<Articolo> getArticoliConErroriDOrdine() throws Exception;

	// Per injection
	public void setArticoloDAO(ArticoloDAO articoloDAO);

}
