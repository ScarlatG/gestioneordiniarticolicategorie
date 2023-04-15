package it.prova.gestioneordiniarticolicategorie.dao;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;

public interface ArticoloDAO extends IBaseDAO<Articolo> {

	public Articolo caricaArticoloEager(Long idArticolo) throws Exception;

	public void deleteEntireArticolo(Long idArticolo) throws Exception;
}
