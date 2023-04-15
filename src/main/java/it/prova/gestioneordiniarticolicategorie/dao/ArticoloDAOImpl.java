package it.prova.gestioneordiniarticolicategorie.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public class ArticoloDAOImpl implements ArticoloDAO {

	private EntityManager entityManager;

	@Override
	public List<Articolo> list() throws Exception {
		return entityManager.createQuery("from Articolo", Articolo.class).getResultList();
	}

	@Override
	public Articolo get(Long id) throws Exception {
		return entityManager.find(Articolo.class, id);
	}

	@Override
	public void update(Articolo input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		input = entityManager.merge(input);

	}

	@Override
	public void insert(Articolo input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.persist(input);

	}

	@Override
	public void delete(Articolo input) throws Exception {
		if (input == null)
			throw new Exception("Problema valore in input.");
		entityManager.remove(entityManager.merge(input));

	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public Articolo caricaArticoloEager(Long idArticolo) throws Exception {
		TypedQuery<Articolo> query = entityManager
				.createQuery("from Articolo a join fetch a.categorie c where a.id = ?1", Articolo.class);
		query.setParameter(1, idArticolo);
		return query.getResultStream().findFirst().orElse(null);
	}

	@Override
	public void deleteEntireArticolo(Long idArticolo) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria a where a.articolo_id = ?1")
				.setParameter(1, idArticolo).executeUpdate();
		entityManager.createNamedQuery("delete from articolo a where a.id = ?1").setParameter(1, idArticolo)
				.executeUpdate();

	}

	@Override
	public Double sumAllByCategoria(Categoria categoria) throws Exception {
		if (categoria == null) {
			throw new Exception("Categoria non valida");
		}
		TypedQuery<Double> query = entityManager.createQuery(
				"SELECT SUM(a.prezzoSingolo) FROM Articolo a WHERE a.categoria = :categoria", Double.class);
		query.setParameter("categoria", categoria);
		return query.getSingleResult();
	}

	@Override
	public Double findTotalPriceByDestinatario(String destinatario) throws Exception {
		if (destinatario == null || destinatario.isEmpty()) {
			throw new Exception("Destinatario non valido");
		}
		TypedQuery<Double> query = entityManager
				.createQuery("SELECT SUM(a.prezzo) FROM Articolo a WHERE a.destinatario = :destinatario", Double.class);
		query.setParameter("destinatario", destinatario);
		Double result = query.getSingleResult();
		return result != null ? result : 0.0;
	}

	@Override
	public List<Articolo> findArticoliInSituazioniStrane() throws Exception {
		TypedQuery<Articolo> query = entityManager.createQuery(
				"SELECT a FROM Articolo a JOIN a.ordine o WHERE o.dataSpedizione > o.dataScadenza", Articolo.class);
		return query.getResultList();
	}

	@Override
	public Articolo findByCodice(String codice) throws Exception {
		if (codice == null) {
			throw new Exception("Codice ordine non valido");
		}
		TypedQuery<Articolo> query = entityManager.createQuery("SELECT a FROM Articolo a WHERE o.codice = :codice",
				Articolo.class);
		query.setParameter("codice", codice);
		return query.getSingleResult();
	}

	@Override
	public List<Articolo> findArticoliPerCategoria(String codiceCategoria) throws Exception {
		if (codiceCategoria == null) {
			throw new Exception("Codice categoria non valido");
		}
		TypedQuery<Articolo> query = entityManager.createQuery(
				"SELECT a FROM Articolo a JOIN a.categorie c WHERE c.codice = :codiceCategoria", Articolo.class);
		query.setParameter("codiceCategoria", codiceCategoria);
		return query.getResultList();
	}

	@Override
	public List<Articolo> findArticoliPerNumeroSeriale(String numeroSeriale) throws Exception {
		if (numeroSeriale == null) {
			throw new Exception("Numero seriale non valido");
		}

		TypedQuery<Articolo> query = entityManager.createQuery(
				"SELECT a FROM Articolo a WHERE a.numeroSeriale LIKE CONCAT('%',:numeroSeriale,'%')", Articolo.class);
		query.setParameter("numeroSeriale", numeroSeriale);
		return query.getResultList();
	}

}
