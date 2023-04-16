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

	@Override
	public List<Categoria> distinteCategorieDiUnOrdine(Long idOrdine) throws Exception {
		return entityManager
				.createQuery("select distinct c from Ordine o join o.articoli a join a.categorie c where o.id= ?1 ",
						Categoria.class)
				.setParameter(1, idOrdine).getResultList();
	}

	@Override
	public Ordine getOrdinePiuRecenteByCateoria(Long idCategoria) throws Exception {
		String queryString = "SELECT o FROM Ordine o JOIN o.articoli a JOIN a.categorie c WHERE c.id = :idCategoria AND o.dataSpedizione = (SELECT MAX(o2.dataSpedizione) FROM Ordine o2 JOIN o2.articoli a2 JOIN a2.categorie c2 WHERE c2.id = :idCategoria)";
		TypedQuery<Ordine> query = entityManager.createQuery(queryString, Ordine.class);
		query.setParameter("idCategoria", idCategoria);
		return query.getSingleResult();
	}

	@Override
	public List<String> getCodiciDiCategorieDiMeseEAnno(int mese, int anno) throws Exception {
		String queryString = "SELECT distinct a.descrizione FROM Ordine o JOIN o.articoli a JOIN a.categorie c WHERE year(o.dataSpedizione) = ?1 and month(o.dataSpedizione) = ?2 ";
		TypedQuery<String> query = entityManager.createQuery(queryString, String.class);
		query.setParameter(1, anno);
		query.setParameter(2, mese);
		return query.getResultList();
	}

	@Override
	public Double getSumPrezziDiDesinatario(String nomeDestinatario) throws Exception {
		String queryString = "SELECT SUM(a.prezzoSingolo) FROM Articolo a JOIN a.ordine o WHERE o.nomeDestinatario like ?1";
		;
		TypedQuery<Double> query = entityManager.createQuery(queryString, Double.class);
		query.setParameter(1, nomeDestinatario);

		return query.getSingleResult();
	}

	@Override
	public List<String> listaDiIndirizziDiordiniAventiArticoliConSeriale(String segmentoSeriale) throws Exception {
		String queryString = "SELECT distinct o.indirizzoSpedizione FROM Ordine o JOIN o.articoli a WHERE a.numeroSeriale like ?1";
		TypedQuery<String> query = entityManager.createQuery(queryString, String.class);
		query.setParameter(1, "%" + segmentoSeriale + "%");
		return query.getResultList();
	}
}
