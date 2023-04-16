package it.prova.gestioneordiniarticolicategorie.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public class CategoriaDAOImpl implements CategoriaDAO {

	private EntityManager entityManager;

	@Override
	public List<Categoria> list() throws Exception {
		return entityManager.createQuery("from Categoria", Categoria.class).getResultList();
	}

	@Override
	public Categoria get(Long id) throws Exception {
		return entityManager.find(Categoria.class, id);
	}

	@Override
	public void update(Categoria input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		input = entityManager.merge(input);

	}

	@Override
	public void insert(Categoria input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.persist(input);

	}

	@Override
	public void delete(Categoria input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.remove(entityManager.merge(input));

	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public Categoria caricaCategoriaEager(Long idCategoria) throws Exception {
		TypedQuery<Categoria> query = entityManager
				.createQuery("from Categoria c join fetch c.articoli a where c.id = ?1", Categoria.class);
		query.setParameter(1, idCategoria);
		return query.getResultStream().findFirst().orElse(null);
	}

	@Override
	public void deleteEntireCategoria(Long idCategoria) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria a where a.categoria_id = ?1")
				.setParameter(1, idCategoria).executeUpdate();
		entityManager.createNativeQuery("delete from categoria c where c.id = ?1").setParameter(1, idCategoria)
				.executeUpdate();

	}

	@Override
	public List<Categoria> findAllDistinctByOrdine(Long idOrdine) throws Exception {
		TypedQuery<Categoria> query = entityManager.createQuery(
				"select distinct c from Categoria c join c.articoli a join a.ordine o where o.id = ?1",
				Categoria.class);
		query.setParameter(1, idOrdine);
		return query.getResultList();
	}

	@Override
	public List<String> findDistinctCategoriesByMonth(int year, int month) throws Exception {
		LocalDate startOfMonth = LocalDate.of(year, month, 1);
		LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
		TypedQuery<String> query = entityManager.createQuery(
				"SELECT DISTINCT a.categoria.codice FROM Articolo a WHERE a.ordine.dataOrdine BETWEEN :startOfMonth AND :endOfMonth",
				String.class);
		query.setParameter("startOfMonth", startOfMonth);
		query.setParameter("endOfMonth", endOfMonth);
		return query.getResultList();
	}

	@Override
	public Categoria findByCodice(String codice) throws Exception {
		if (codice == null) {
			throw new Exception("Codice ordine non valido");
		}
		TypedQuery<Categoria> query = entityManager.createQuery("SELECT c FROM Categoria c WHERE c.codice = :codice",
				Categoria.class);
		query.setParameter("codice", codice);
		return query.getSingleResult();
	}

	@Override
	public Categoria findByIdFetchEagher(Long id) throws Exception {
		TypedQuery<Categoria> query = entityManager.createQuery(
				"select u FROM Categoria u left join fetch u.articoli r where u.id = :idCategoria", Categoria.class);
		query.setParameter("idCategoria", id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void deleteByIdPostScollegamento(Long id) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria a where a.categoria_id = ?1")
				.setParameter(1, id).executeUpdate();
		entityManager.createNativeQuery("delete from categoria a where a.id = ?1").setParameter(1, id).executeUpdate();

	}

}
