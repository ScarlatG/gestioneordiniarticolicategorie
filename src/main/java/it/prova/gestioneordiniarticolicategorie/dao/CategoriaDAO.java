package it.prova.gestioneordiniarticolicategorie.dao;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface CategoriaDAO extends IBaseDAO<Categoria> {

	public Categoria caricaCategoriaEager(Long idCategoria) throws Exception;

	public void deleteEntireCategoria(Long idCategoria) throws Exception;

	List<Categoria> findAllDistinctByOrdine(Long idOrdine) throws Exception;

	List<String> findDistinctCategoriesByMonth(int year, int month) throws Exception;

	public Categoria findByCodice(String codice) throws Exception;

	public Categoria findByIdFetchEagher(Long id) throws Exception;

	public void deleteByIdPostScollegamento(Long id) throws Exception;

}
