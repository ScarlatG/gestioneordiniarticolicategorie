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

	public List<String> findCategoriePerOrdine(Long idOrdine) throws Exception;

	public Articolo findByIdFetchEagher(Long id) throws Exception;

	public void deleteByIdPostScollegamento(Long id) throws Exception;

	public Double getSommaPrezziDiUnaCategoria(Long id) throws Exception;

	public List<Articolo> articoliStrani() throws Exception;

}
