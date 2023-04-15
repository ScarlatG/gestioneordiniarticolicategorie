package it.prova.gestioneordiniarticolicategorie.dao;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface ArticoloDAO extends IBaseDAO<Articolo> {

	public Articolo caricaArticoloEager(Long idArticolo) throws Exception;

	public void deleteEntireArticolo(Long idArticolo) throws Exception;

	public Double sumAllByCategoria(Categoria categoria) throws Exception;

	public Double findTotalPriceByDestinatario(String destinatario) throws Exception;

	List<Articolo> findArticoliInSituazioniStrane() throws Exception;

	public Articolo findByCodice(String codice) throws Exception;

	public List<Articolo> findArticoliPerCategoria(String codiceCategoria) throws Exception;

	public List<Articolo> findArticoliPerNumeroSeriale(String numeroSeriale) throws Exception;

}
