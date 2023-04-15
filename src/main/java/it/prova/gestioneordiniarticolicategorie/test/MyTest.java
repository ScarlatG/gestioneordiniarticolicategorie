package it.prova.gestioneordiniarticolicategorie.test;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.EntityManagerUtil;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;
import it.prova.gestioneordiniarticolicategorie.service.ArticoloService;
import it.prova.gestioneordiniarticolicategorie.service.CategoriaService;
import it.prova.gestioneordiniarticolicategorie.service.MyServiceFactory;
import it.prova.gestioneordiniarticolicategorie.service.OrdineService;

public class MyTest {

	public static void main(String[] args) {

		OrdineService ordineServiceInstance = MyServiceFactory.getOrdineServiceInstance();
		ArticoloService articoloServiceInstance = MyServiceFactory.getArticoloServiceInstance();
		CategoriaService categoriaServiceInstance = MyServiceFactory.getCategoriaServiceInstance();

		try {

//			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");
//
//			System.out.println(" -------------------------------------------------------------------------- ");
//
//			System.out
//					.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");
//
//			System.out.println(" -------------------------------------------------------------------------- ");
//
//			System.out.println(
//					"In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");

			// ----------------------------------------------- INIZIO TEST ORDINE
			// -----------------------------------------------

			// Test inserimento nuovo ordine
//			testInserimentoNuovoOrdine(ordineServiceInstance);
//			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");

			// Test aggiornamento ordine esistente
//			testAggiornamentoOrdineEsistente(ordineServiceInstance);
//			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");
			
			// Test rimozione, e nel caso sia collegato ad un articolo lanciare una eccezione
//			testRimuoviOrdine(ordineServiceInstance, articoloServiceInstance);

			// ----------------------------------------------- INIZIO TEST ARTICOLO
			// -----------------------------------------------

			// Test inserimento nuovo articolo
//			testInserimentoNuovoArticolo(ordineServiceInstance, articoloServiceInstance);

			// Test aggiornamento articlo esistente
//			testAggiornamentoArticoloEsistente(articoloServiceInstance);

			// Test rimozione di un articolo legato ad un ordine
//			testRimozioneArticoloDaOrdine(articoloServiceInstance, ordineServiceInstance);

			// Test aggiungi articolo a categoria
//			testAggiungiArticoloACategoria(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);

			// Test rimozione completa di articolo
//			testRimozioneArticoloCompleta(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);

			// ----------------------------------------------- INIZIO TEST CATEGORIA
			// -----------------------------------------------

			// Test inserimento nuova categoria
//			testInserimentoNuovaCategoria(categoriaServiceInstance);

			// Test aggiornamento categoria esistente
//			testAggiornamentoCategoriaEsistente(categoriaServiceInstance);

			// Test aggiungi categoria ad articolo
//			testAggiungiCategoriaAdArticolo(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);
			
			// Test rimozione completa categoria
//			testRimozioneCategoriaCompleta(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerUtil.shutdown();
		}

	}

	// ----------------------------------------------- METODI DEI TEST ORDINE
	// -----------------------------------------------

	// Metodo test inserimento nuovo ordine
	private static void testInserimentoNuovoOrdine(OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testInserimentoNuovoOrdine INIZIO -------------");

		Ordine ordineDaInserire = new Ordine("Mario Rossi", "Via Mosca 52", LocalDate.of(2023, 04, 21),
				LocalDate.of(2023, 06, 30));
		ordineServiceInstance.inserisciNuovo(ordineDaInserire);
		if (ordineDaInserire.getId() == null) {
			throw new RuntimeException("testInserimentoNuovoOrdine FALLITO: Ordine non inserito.");
		}
		System.out.println("------------- testInserimentoNuovoOrdine FINE -------------");
	}

	// Metodo test aggiornamento ordine esistente
	private static void testAggiornamentoOrdineEsistente(OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testAggiornamentoOrdineEsistente INIZIO -------------");

		List<Ordine> listaOrdiniPresenti = ordineServiceInstance.listAll();
		if (listaOrdiniPresenti.size() < 1) {
			throw new RuntimeException("testAggiornamentoOrdineEsistente FALLITO: non sono presenti voci nel DB.");
		}

		Ordine ordineDaAggiornare = listaOrdiniPresenti.get(0);
		System.out.println("Prima dell'aggiornamento: " + ordineDaAggiornare);
		String nuovoDestinatario = "Scarlat Gabriel";
		ordineDaAggiornare.setNomeDestinatario(nuovoDestinatario);
		ordineServiceInstance.aggiorna(ordineDaAggiornare);

		Ordine ordineReloaded = ordineServiceInstance.caricaSingoloElemento(ordineDaAggiornare.getId());
		if (ordineReloaded.getId() != ordineDaAggiornare.getId()) {
			throw new RuntimeException("testAggiornamentoOrdineEsistente FALLITO: update non avvenuto.");
		}
		System.out.println("Dopo l'aggiornamento: " + ordineReloaded);
		System.out.println("------------- testAggiornamentoOrdineEsistente FINE -------------");

	}
	
	
	// Metodo rimozione, e nel caso sia collegato ad un articolo lanciare una eccezione
	private static void testRimuoviOrdine(OrdineService ordineServiceInstance, ArticoloService articoloServiceInstance)
			throws Exception {
		System.out.println("------------- testRimuoviOrdine INIZIO -------------");

		Ordine ordineDaInserire = new Ordine("Mario Rossi", "Via Mosca 52", LocalDate.of(2023, 04, 21),
				LocalDate.of(2023, 06, 30));
		ordineServiceInstance.inserisciNuovo(ordineDaInserire);
		Long idOrdineDaInserire = ordineDaInserire.getId();
		if (idOrdineDaInserire == null) {
			throw new RuntimeException("testRimuoviOrdine FALLITO: Ordine non inserito.");
		}
		Articolo nuovoArticolo = new Articolo("Scarpe Nike", "12345678", 329D, LocalDate.now());
		nuovoArticolo.setOrdine(ordineDaInserire);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testRimuoviOrdine FALLITO: Articolo non inserito");
		}
		if (!nuovoArticolo.getOrdine().getId().equals(idOrdineDaInserire)) {
			throw new RuntimeException("testRimuoviOrdine FALLITO: L'ID dell'ordine non corrisponde");
		}


		Ordine ordineDaEliminare = new Ordine("Mario Rossi", "Via Mosca 52", LocalDate.of(2023, 04, 21),
				LocalDate.of(2023, 06, 30));
		ordineServiceInstance.inserisciNuovo(ordineDaEliminare);
		Long idOrdineDaEliminare = ordineDaEliminare.getId();
		if (idOrdineDaEliminare == null) {
			throw new RuntimeException("testRimuoviOrdine FALLITO: Ordine non inserito.");
		}

		ordineServiceInstance.rimuovi(idOrdineDaEliminare);

		System.out.println("------------- testRimuoviOrdine FINE -------------");

	}


	// ----------------------------------------------- METODI DEI TEST ARTICOLO
	// -----------------------------------------------

	// Metodo test inserimento nuovo articolo
	private static void testInserimentoNuovoArticolo(OrdineService ordineServiceInstance,
			ArticoloService articoloServiceInstance) throws Exception {
		System.out.println("------------- testInserimentoNuovoArticolo INIZIO -------------");

		Ordine ordineDaCollegare = new Ordine("Filippo Belli", "Via Prenestina 52", LocalDate.of(2023, 05, 19),
				LocalDate.of(2023, 07, 30));
		ordineServiceInstance.inserisciNuovo(ordineDaCollegare);
		Long idOrdineDaCollegare = ordineDaCollegare.getId();
		if (idOrdineDaCollegare == null) {
			throw new RuntimeException("testInserimentoNuovoArticolo FALLITO: Ordine non inserito.");
		}
		Articolo nuovoArticolo = new Articolo("Scarpe Nike", "12345678", 329D, LocalDate.now());
		nuovoArticolo.setOrdine(ordineDaCollegare);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testInserimentoNuovoArticolo FALLITO: Articolo non inserito");
		}
		if (!nuovoArticolo.getOrdine().getId().equals(idOrdineDaCollegare)) {
			throw new RuntimeException("testInserimentoNuovoArticolo FALLITO: L'ID dell'ordine non corrisponde");
		}
		System.out.println("------------- testInserimentoNuovoArticolo FINE -------------");

	}

	// Metodo test aggiornamento articolo esistente
	private static void testAggiornamentoArticoloEsistente(ArticoloService articoloServiceInstance) throws Exception {
		System.out.println("------------- testAggiornamentoArticoloEsistente INIZIO -------------");

		List<Articolo> listaArticoliPresenti = articoloServiceInstance.listAll();
		if (listaArticoliPresenti.size() < 1) {
			throw new RuntimeException("testAggiornamentoArticoloEsistente FALLITO: non sono presenti voci nel DB.");
		}

		Articolo articoloDaAggiornare = listaArticoliPresenti.get(0);
		System.out.println("Prima dell'aggiornamento: " + articoloDaAggiornare);
		String nuovaDescrizione = "Portatile MacBook";
		articoloDaAggiornare.setDescrizione(nuovaDescrizione);
		articoloServiceInstance.aggiorna(articoloDaAggiornare);

		Articolo articoloReloaded = articoloServiceInstance.caricaSingoloElemento(articoloDaAggiornare.getId());
		if (articoloReloaded.getId() != articoloDaAggiornare.getId()) {
			throw new RuntimeException("testAggiornamentoArticoloEsistente FALLITO: update non avvenuto.");
		}
		System.out.println("Dopo l'aggiornamento: " + articoloReloaded);
		System.out.println("------------- testAggiornamentoArticoloEsistente FINE -------------");

	}

	// Metodo rimozione di un articolo legato ad un ordine
	public static void testRimozioneArticoloDaOrdine(ArticoloService articoloServiceInstance,
			OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testRimozioneArticoloDaOrdine INIZIO -------------");

		List<Ordine> listaOrdiniPresenti = ordineServiceInstance.listAll();
		if (listaOrdiniPresenti.size() < 1) {
			throw new RuntimeException("testRimozioneArticoloDaOrdine FALLITO: non sono presenti voci nel DB.");
		}

		Ordine ordineDaCollegare = listaOrdiniPresenti.get(0);
		Long idOrdineDaCollegare = ordineDaCollegare.getId();
		Articolo nuovoArticolo = new Articolo("Libro", "12345678", 329D, LocalDate.now());
		nuovoArticolo.setOrdine(ordineDaCollegare);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloDaOrdine FALLITO: Articolo non inserito");
		}
		if (!nuovoArticolo.getOrdine().getId().equals(idOrdineDaCollegare)) {
			throw new RuntimeException("testRimozioneArticoloDaOrdine FALLITO: L'ID dell'ordine non corrisponde");
		}

		Long idArticoloDaEliminare = nuovoArticolo.getId();
		articoloServiceInstance.rimuovi(idArticoloDaEliminare);
		Articolo articoloSupposedToBeRemoved = articoloServiceInstance.caricaSingoloElemento(idArticoloDaEliminare);
		if (articoloSupposedToBeRemoved != null) {
			throw new RuntimeException("testRimozioneArticoloDaOrdine FALLITO: articolo non rimosso");
		}
		System.out.println("------------- testRimozioneArticoloDaOrdine FINE -------------");

	}

	// Metodo aggiungi articolo a categoria
	public static void testAggiungiArticoloACategoria(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testAggiungiArticoloACategoria INIZIO -------------");

		Categoria nuovaCategoria = new Categoria("Elettronica", "101");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		if (nuovaCategoria.getId() == null) {
			throw new RuntimeException("testAggiungiArticoloACategoria FALLITO: Ordine non inserito.");
		}

		Ordine nuovoOrdine = new Ordine("Luca De Luca", "Via Casilina 52", LocalDate.of(2023, 5, 31),
				LocalDate.of(2023, 8, 30));
		ordineServiceInstance.inserisciNuovo(nuovoOrdine);
		if (nuovoOrdine.getId() == null) {
			throw new RuntimeException("testAggiungiArticoloACategoria FALLITO: Ordine non inserito.");
		}

		Articolo nuovoArticolo = new Articolo("Scarpe Nike", "0001", 299D, LocalDate.now());
		nuovoArticolo.setOrdine(nuovoOrdine);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testAggiungiArticoloACategoria FALLITO: Ordine non inserito.");
		}

		categoriaServiceInstance.aggiungiArticolo(nuovaCategoria, nuovoArticolo);

	}

	// Metodo rimozione completa di articolo
	public static void testRimozioneArticoloCompleta(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testRimozioneArticoloCompleta INIZIO -------------");
		Categoria nuovaCategoria1 = new Categoria("Abbigliamento", "201");
		Categoria nuovaCategoria2 = new Categoria("Informatica", "202");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria1);
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria2);
		if (nuovaCategoria1.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: categoria non inserita.");
		}
		if (nuovaCategoria2.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: categoria non inserita.");
		}

		Ordine nuovoOrdine = new Ordine("Luca De Luca", "Via Casilina 52", LocalDate.of(2023, 5, 31),
				LocalDate.of(2023, 8, 30));
		ordineServiceInstance.inserisciNuovo(nuovoOrdine);
		if (nuovoOrdine.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: ordine non inserito");
		}

		Articolo nuovoArticolo = new Articolo("Scarpe Nike", "0001", 299D, LocalDate.now());
		nuovoArticolo.setOrdine(nuovoOrdine);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: articolo non inserito.");
		}

		articoloServiceInstance.aggiungiCategoria(nuovoArticolo, nuovaCategoria1);
		articoloServiceInstance.aggiungiCategoria(nuovoArticolo, nuovaCategoria2);

		Articolo articoloDaRimuovere = articoloServiceInstance.caricaArticoloEager(nuovoArticolo.getId());
		if (articoloDaRimuovere.getId() == null) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: Ordine non inserito.");
		}
		if (articoloDaRimuovere.getCategorie().isEmpty()) {
			throw new RuntimeException("testRimozioneArticoloCompleta FALLITO: categoria non associata ad articolo");
		}
		articoloServiceInstance.rimozioneCompletaArticolo(articoloDaRimuovere.getId());

		System.out.println("------------- testRimozioneArticoloCompleta FINE -------------");

	}

	// ----------------------------------------------- METODI DEI TEST CATEGORIA
	// -----------------------------------------------

	// Metodo inserimento nuova categoria
	private static void testInserimentoNuovaCategoria(CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println("------------- testInserimentoNuovaCategoria INIZIO -------------");

		Categoria categoriaDaInserire = new Categoria("Abbigliamento", "001");
		categoriaServiceInstance.inserisciNuovo(categoriaDaInserire);
		if (categoriaDaInserire.getId() == null) {
			throw new RuntimeException("testInserimentoNuovaCategoria FALLITO: Ordine non inserito.");
		}
		System.out.println("------------- testInserimentoNuovaCategoria FINE -------------");
	}

	// Metodo aggiornamento categoria esistente
	private static void testAggiornamentoCategoriaEsistente(CategoriaService categoriaServiceInstance)
			throws Exception {
		System.out.println("------------- testAggiornamentoCategoriaEsistente INIZIO -------------");

		List<Categoria> listaCategoriePresenti = categoriaServiceInstance.listAll();
		if (listaCategoriePresenti.size() < 1) {
			throw new RuntimeException("testAggiornamentoCategoriaEsistente FALLITO: non sono presenti voci nel DB.");
		}

		Categoria categoriaDaAggiornare = listaCategoriePresenti.get(0);
		System.out.println("Prima dell'aggiornamento: " + categoriaDaAggiornare);
		String nuovaDescrizione = "Informatica";
		categoriaDaAggiornare.setDescrizione(nuovaDescrizione);
		categoriaServiceInstance.aggiorna(categoriaDaAggiornare);

		Categoria categoriaReloaded = categoriaServiceInstance.caricaSingoloElemento(categoriaDaAggiornare.getId());
		if (categoriaReloaded.getId() != categoriaDaAggiornare.getId()) {
			throw new RuntimeException("testAggiornamentoCategoriaEsistente FALLITO: update non avvenuto.");
		}
		System.out.println("Dopo l'aggiornamento: " + categoriaReloaded);
		System.out.println("------------- testAggiornamentoCategoriaEsistente FINE -------------");

	}

	// Metodo aggiunta categoria ad un articolo
	public static void testAggiungiCategoriaAdArticolo(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- aggiungiCategoriaAdArticolo INIZIO -------------");

		Categoria nuovaCategoria = new Categoria("Cultura", "413");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		if (nuovaCategoria.getId() == null) {
			throw new RuntimeException("testAssociaCategoriaAdArticolo FALLITO: Ordine non inserito.");
		}

		Ordine nuovoOrdine = new Ordine("Luca De Luca", "Via Casilina 52", LocalDate.of(2023, 5, 31),
				LocalDate.of(2023, 8, 30));
		ordineServiceInstance.inserisciNuovo(nuovoOrdine);
		if (nuovoOrdine.getId() == null) {
			throw new RuntimeException("testAssociaCategoriaAdArticolo FALLITO: Ordine non inserito.");
		}

		Articolo nuovoArticolo = new Articolo("Scarpe Nike", "0001", 299D, LocalDate.now());
		nuovoArticolo.setOrdine(nuovoOrdine);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null) {
			throw new RuntimeException("testAssociaCategoriaAdArticolo FALLITO: Ordine non inserito.");
		}

		articoloServiceInstance.aggiungiCategoria(nuovoArticolo, nuovaCategoria);
		Articolo articoloReloaded = articoloServiceInstance.caricaArticoloEager(nuovoArticolo.getId());
		if (articoloReloaded.getCategorie().isEmpty()) {
			throw new RuntimeException("testAssociaCategoriaAdArticolo FALLITO: articolo non associato a categoria");
		}

		System.out.println("------------- testAssociaCategoriaAdArticolo FINE -------------");
	}

	// Metodo rimozione completa categoria
	public static void testRimozioneCategoriaCompleta(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {
		System.out.println("------------- testRimozioneCategoriaCompleta INIZIO -------------");
		Categoria nuovaCategoria = new Categoria("Arte", "443");
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		if (nuovaCategoria.getId() == null) {
			throw new RuntimeException("testRimozioneCategoriaCompleta FALLITO: Ordine non inserito.");
		}

		Ordine nuovoOrdine = new Ordine("Alberto Rossi", "Via Appia 52", LocalDate.of(2002, 5, 25),
				LocalDate.of(2023, 8, 30));
		ordineServiceInstance.inserisciNuovo(nuovoOrdine);
		if (nuovoOrdine.getId() == null) {
			throw new RuntimeException("testRimozioneCategoriaCompleta FALLITO: Ordine non inserito.");
		}

		Articolo nuovoArticolo1 = new Articolo("Quadro", "0010", 299D, LocalDate.now());
		Articolo nuovoArticolo2 = new Articolo("Dipinto", "0011", 299D, LocalDate.now());
		nuovoArticolo1.setOrdine(nuovoOrdine);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo1);
		if (nuovoArticolo1.getId() == null) {
			throw new RuntimeException("testRimozioneCategoriaCompleta FALLITO: articolo non inserito.");
		}
		nuovoArticolo2.setOrdine(nuovoOrdine);
		articoloServiceInstance.inserisciNuovo(nuovoArticolo2);
		if (nuovoArticolo2.getId() == null) {
			throw new RuntimeException("testRimozioneCategoriaCompleta FALLITO: articolo non inserito.");
		}

		categoriaServiceInstance.aggiungiArticolo(nuovaCategoria, nuovoArticolo1);
		categoriaServiceInstance.aggiungiArticolo(nuovaCategoria, nuovoArticolo2);
		Categoria categoriaReloaded = categoriaServiceInstance.caricaCategoriaEager(nuovaCategoria.getId());
		if (categoriaReloaded.getArticoli().isEmpty()) {
			throw new RuntimeException("testRimozioneCategoriaCompleta FALLITO: articolo non associato a categoria");
		}

		categoriaServiceInstance.rimozioneCategoriaCompleta(categoriaReloaded.getId());

		System.out.println("------------- testRimozioneCategoriaCompleta FINE -------------");
	}

}
