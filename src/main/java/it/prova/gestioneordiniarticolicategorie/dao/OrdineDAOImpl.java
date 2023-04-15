package it.prova.gestioneordiniarticolicategorie.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class OrdineDAOImpl implements OrdineDAO {

	private EntityManager entityManager;

	@Override
	public List<Ordine> list() throws Exception {
		return entityManager.createQuery("from Ordine", Ordine.class).getResultList();
	}

	@Override
	public Ordine get(Long id) throws Exception {
		return entityManager.find(Ordine.class, id);
	}

	@Override
	public void update(Ordine input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		input = entityManager.merge(input);

	}

	@Override
	public void insert(Ordine input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.persist(input);

	}

	@Override
	public void delete(Ordine input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.remove(entityManager.merge(input));

	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Ordine> findByCategoria(Categoria categoria) throws Exception {
		try {
			TypedQuery<Ordine> query = entityManager.createQuery(
					"SELECT DISTINCT o FROM Ordine o JOIN o.articoli a WHERE a.categoria = :categoria", Ordine.class);
			query.setParameter("categoria", categoria);
			return query.getResultList();
		} catch (Exception e) {
			throw new Exception("Impossibile trovare gli ordini per la categoria: " + categoria.getId(), e);
		}
	}

	@Override
	public Ordine findLatestOrderByCategoria(Categoria categoria) throws Exception {
		if (categoria == null) {
			throw new Exception("Categoria non valida");
		}
		TypedQuery<Ordine> query = entityManager.createQuery(
				"SELECT o FROM Ordine o WHERE o.categoria = :categoria ORDER BY o.dataSpedizione DESC", Ordine.class);
		query.setParameter("categoria", categoria);
		query.setMaxResults(1);
		List<Ordine> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	@Override
	public List<String> findDistinctIndirizziByNumeroSerialeContaining(String numeroSeriale) throws Exception {
		if (numeroSeriale == null || numeroSeriale.trim().isEmpty()) {
			throw new Exception("Numero seriale non valido");
		}
		TypedQuery<String> query = entityManager.createQuery(
				"SELECT DISTINCT o.indirizzoSpedizione FROM Ordine o JOIN o.articoli a WHERE a.numeroSeriale LIKE :numeroSeriale",
				String.class);
		query.setParameter("numeroSeriale", "%" + numeroSeriale + "%");
		return query.getResultList();
	}

	@Override
	public Ordine findByCodice(String codice) throws Exception {
		if (codice == null) {
			throw new Exception("Codice ordine non valido");
		}
		TypedQuery<Ordine> query = entityManager.createQuery("SELECT o FROM Ordine o WHERE o.codice = :codice",
				Ordine.class);
		query.setParameter("codice", codice);
		return query.getSingleResult();
	}

	@Override
	public List<Ordine> findOrdiniPerCategoria(String codiceCategoria) throws Exception {
		TypedQuery<Ordine> query = entityManager.createQuery(
				"SELECT o FROM Ordine o JOIN o.articoli a JOIN a.categorie c WHERE c.codice = :codiceCategoria ORDER BY o.dataSpedizione DESC",
				Ordine.class);
		query.setParameter("codiceCategoria", codiceCategoria);
		return query.getResultList();
	}

	@Override
	public List<Ordine> findOrdiniPerPeriodo(LocalDate inizioPeriodo, LocalDate finePeriodo) throws Exception {
		if (inizioPeriodo == null || finePeriodo == null) {
			throw new Exception("Data non valida");
		}
		TypedQuery<Ordine> query = entityManager.createQuery(
				"SELECT o FROM Ordine o WHERE o.dataSpedizione BETWEEN :inizioPeriodo AND :finePeriodo", Ordine.class);
		query.setParameter("inizioPeriodo", inizioPeriodo);
		query.setParameter("finePeriodo", finePeriodo);
		return query.getResultList();
	}

}
