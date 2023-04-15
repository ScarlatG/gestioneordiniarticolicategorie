package it.prova.gestioneordiniarticolicategorie.dao;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineDAO extends IBaseDAO<Ordine> {

	public Ordine findByCodice(String codice) throws Exception;

	public List<Ordine> findByCategoria(Categoria categoria) throws Exception;

	public Ordine findLatestOrderByCategoria(Categoria categoria) throws Exception;

	public List<String> findDistinctIndirizziByNumeroSerialeContaining(String numeroSeriale) throws Exception;

	public List<Ordine> findOrdiniPerCategoria(String codiceCategoria) throws Exception;

	public List<Ordine> findOrdiniPerPeriodo(LocalDate dataInizio, LocalDate dataFine) throws Exception;

}
