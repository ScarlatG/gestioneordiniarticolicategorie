package it.prova.gestioneordiniarticolicategorie.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAOImpl;
import it.prova.gestioneordiniarticolicategorie.dao.CategoriaDAO;
import it.prova.gestioneordiniarticolicategorie.dao.CategoriaDAOImpl;
import it.prova.gestioneordiniarticolicategorie.dao.EntityManagerUtil;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class ArticoloServiceImpl implements ArticoloService {

	private ArticoloDAO articoloDAO;

	@Override
	public List<Articolo> listAll() throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {

			// Injection
			articoloDAO.setEntityManager(entityManager);

			return articoloDAO.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiorna(Articolo articoloInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			articoloDAO.setEntityManager(entityManager);

			articoloDAO.update(articoloInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void inserisciNuovo(Articolo articoloInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			articoloDAO.setEntityManager(entityManager);

			articoloDAO.insert(articoloInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void rimuovi(Long idArticolo) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			articoloDAO.setEntityManager(entityManager);

			articoloDAO.delete(articoloDAO.get(idArticolo));
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void setArticoloDAO(ArticoloDAO articoloDAO) {
		this.articoloDAO = articoloDAO;

	}

	@Override
	public Articolo caricaSingoloElemento(Long idArticolo) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		ArticoloDAO articoloDAO = new ArticoloDAOImpl();
		try {
			entityManager.getTransaction().begin();

			articoloDAO.setEntityManager(entityManager);
			Articolo articolo = articoloDAO.get(idArticolo);

			entityManager.getTransaction().commit();

			return articolo;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public Articolo caricaArticoloEager(Long idArticolo) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {

			// Injection
			articoloDAO.setEntityManager(entityManager);

			return articoloDAO.caricaArticoloEager(idArticolo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiungiCategoria(Articolo articoloInstance, Categoria categoriaInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			articoloDAO.setEntityManager(entityManager);

			articoloInstance = entityManager.merge(articoloInstance);

			categoriaInstance = entityManager.merge(categoriaInstance);

			articoloInstance.addToCategorie(categoriaInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void rimozioneCompletaArticolo(Long idArticolo) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			articoloDAO.setEntityManager(entityManager);

			articoloDAO.deleteEntireArticolo(idArticolo);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public double sommaPrezziPerCategoria(String codiceCategoria) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
		ArticoloDAO articoloDAO = new ArticoloDAOImpl();
		try {
			// Injection
			categoriaDAO.setEntityManager(entityManager);
			Categoria categoria = categoriaDAO.findByCodice(codiceCategoria);

			if (categoria == null) {
				throw new Exception("Categoria non trovata");
			}

			List<Articolo> articoli = articoloDAO.findArticoliPerCategoria(categoria.getCodice());

			double sommaPrezzi = 0;
			for (Articolo articolo : articoli) {
				sommaPrezzi += articolo.getPrezzoSingolo();
			}

			return sommaPrezzi;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public double calcolaTotalePrezzoPerDestinatario(String destinatario) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			TypedQuery<Double> query = entityManager.createQuery(
					"SELECT SUM(a.prezzo) FROM Articolo a WHERE a.destinatario = :destinatario", Double.class);
			query.setParameter("destinatario", destinatario);
			Double result = query.getSingleResult();
			return result != null ? result : 0.0;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public List<Articolo> trovaArticoliInSituazioniStrane() throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		ArticoloDAO articoloDAO = new ArticoloDAOImpl();
		List<Articolo> articoli = null;
		try {
			// Injection
			articoloDAO.setEntityManager(entityManager);
			articoli = articoloDAO.findArticoliInSituazioniStrane();

			// Filtraggio degli articoli in situazioni strane
			List<Articolo> articoliInSituationeStrana = new ArrayList<>();
			for (Articolo articolo : articoli) {
				Ordine ordine = articolo.getOrdine();
				if (ordine != null && ordine.getDataSpedizione() != null
						&& ordine.getDataSpedizione().isAfter(ordine.getDataScadenza())) {
					articoliInSituationeStrana.add(articolo);
				}
			}

			return articoliInSituationeStrana;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

}
