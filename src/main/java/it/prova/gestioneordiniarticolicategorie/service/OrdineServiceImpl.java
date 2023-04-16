package it.prova.gestioneordiniarticolicategorie.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.ArticoloDAOImpl;
import it.prova.gestioneordiniarticolicategorie.dao.CategoriaDAO;
import it.prova.gestioneordiniarticolicategorie.dao.CategoriaDAOImpl;
import it.prova.gestioneordiniarticolicategorie.dao.EntityManagerUtil;
import it.prova.gestioneordiniarticolicategorie.dao.OrdineDAO;
import it.prova.gestioneordiniarticolicategorie.dao.OrdineDAOImpl;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class OrdineServiceImpl implements OrdineService {

	private OrdineDAO ordineDAO;

	private ArticoloDAO articoloDAO;

	@Override
	public void setOrdineDAO(OrdineDAO ordineDAO) {
		this.ordineDAO = ordineDAO;

	}

	@Override
	public void setArticoloDAO(ArticoloDAO articoloDAO) {
		this.articoloDAO = articoloDAO;
	}

	@Override
	public List<Ordine> listAll() throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			// Injection
			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiorna(Ordine ordineInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			ordineDAO.setEntityManager(entityManager);

			ordineDAO.update(ordineInstance);
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void inserisciNuovo(Ordine ordineInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			ordineDAO.setEntityManager(entityManager);

			ordineDAO.insert(ordineInstance);

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
	public void rimuovi(Long idOrdine) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			entityManager.getTransaction().begin();

			// Injection
			ordineDAO.setEntityManager(entityManager);

			ordineDAO.delete(ordineDAO.get(idOrdine));

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
	public Ordine caricaSingoloElemento(Long idOrdine) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		OrdineDAO ordineDAO = new OrdineDAOImpl();
		try {
			entityManager.getTransaction().begin();

			// Injection
			ordineDAO.setEntityManager(entityManager);
			Ordine ordine = ordineDAO.get(idOrdine);

			entityManager.getTransaction().commit();

			return ordine;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public List<Ordine> trovaOrdiniPerCategoria(String codiceCategoria) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
			categoriaDAO.setEntityManager(entityManager);

			// Trova la categoria con il codice specificato
			Categoria categoria = categoriaDAO.findByCodice(codiceCategoria);
			if (categoria == null) {
				throw new Exception("Categoria non trovata");
			}

			// Injection dell'entityManager nel DAO
			ordineDAO.setEntityManager(entityManager);

			// Trova gli ordini associati alla categoria trovata
			List<Ordine> ordini = ordineDAO.findByCategoria(categoria);

			return ordini;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public Ordine trovaOrdinePiuRecentePerCategoria(LocalDate data, String codiceCategoria) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		OrdineDAO ordineDAO = new OrdineDAOImpl();
		try {
			// Injection
			ordineDAO.setEntityManager(entityManager);

			List<Ordine> ordini = ordineDAO.findOrdiniPerCategoria(codiceCategoria);

			if (ordini.isEmpty()) {
				throw new Exception("Non ci sono ordini per la categoria specificata");
			}

			// Filtra gli ordini in base alla data
			List<Ordine> ordiniFiltrati = ordini.stream()
					.filter(ordine -> ordine.getDataSpedizione() != null && ordine.getDataSpedizione().isBefore(data))
					.collect(Collectors.toList());

			if (ordiniFiltrati.isEmpty()) {
				throw new Exception("Non ci sono ordini per la categoria e la data specificate");
			}

			// Ordina gli ordini in base alla data di spedizione
			Collections.sort(ordiniFiltrati, new Comparator<Ordine>() {
				public int compare(Ordine o1, Ordine o2) {
					return o2.getDataSpedizione().compareTo(o1.getDataSpedizione());
				}
			});

			// Ritorna l'ordine più recente
			return ordiniFiltrati.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public List<String> trovaIndirizziPerNumeroSeriale(String numeroSeriale) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();
		try {
			ArticoloDAO articoloDAO = new ArticoloDAOImpl();
			articoloDAO.setEntityManager(entityManager);

			List<Articolo> articoli = articoloDAO.findArticoliPerNumeroSeriale(numeroSeriale);
			Set<String> indirizzi = new HashSet<>();
			for (Articolo articolo : articoli) {
				Ordine ordine = articolo.getOrdine();
				if (ordine != null) {
					indirizzi.add(ordine.getIndirizzoSpedizione());
				}
			}
			return new ArrayList<>(indirizzi);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public List<Categoria> distinteCategorieDiUnOrdine(Ordine ordine) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		if (ordine.getId() == null) {
			throw new RuntimeException("Errore input");
		}

		try {

			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.distinteCategorieDiUnOrdine(ordine.getId());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);

		}
	}

	@Override
	public Ordine caricaOrdinePiuRecenteDiUnaCategoria(Categoria categoria) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		if (categoria.getId() == null) {
			throw new RuntimeException("Errore input");
		}

		try {

			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.getOrdinePiuRecenteByCateoria(categoria.getId());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);

		}
	}

	@Override
	public List<String> caricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno(int mese, int anno)
			throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		if (mese > 12 || mese < 1 || anno < 1000 || anno > 2999) {
			throw new RuntimeException("Errore input");
		}

		try {

			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.getCodiciDiCategorieDiMeseEAnno(mese, anno);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);

		}
	}

	@Override
	public Double caricaSommaPrezziDiUnDesinatario(String nomeDestinatario) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		if (nomeDestinatario == null) {
			throw new RuntimeException("Errore input");
		}

		try {

			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.getSumPrezziDiDesinatario(nomeDestinatario);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);

		}
	}

	@Override
	public List<String> listaDiIndirizziDiordiniAventiArticoliConSeriale(String segmentoSeriale) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		if (segmentoSeriale == null) {
			throw new RuntimeException("Errore input");
		}

		try {

			ordineDAO.setEntityManager(entityManager);

			return ordineDAO.listaDiIndirizziDiordiniAventiArticoliConSeriale(segmentoSeriale);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);

		}
	}

}
