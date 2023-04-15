package it.prova.gestioneordiniarticolicategorie.dao;

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

}
