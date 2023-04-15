package it.prova.gestioneordiniarticolicategorie.dao;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface CategoriaDAO extends IBaseDAO<Categoria> {

	public Categoria caricaCategoriaEager(Long idCategoria) throws Exception;

	public void deleteEntireCategoria(Long idCategoria) throws Exception;
}
