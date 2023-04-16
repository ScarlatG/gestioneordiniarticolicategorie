package it.prova.gestioneordiniarticolicategorie.test;

import java.time.LocalDate;
import java.util.Date;
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

	public static void main(String[] args) throws Exception {

		OrdineService ordineServiceInstance = MyServiceFactory.getOrdineServiceInstance();
		ArticoloService articoloServiceInstance = MyServiceFactory.getArticoloServiceInstance();
		CategoriaService categoriaServiceInstance = MyServiceFactory.getCategoriaServiceInstance();

		try {

			// *****************INIZIO TEST ORDINE******************************

			System.out.println("Nella tabella ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi");
			testInsertOrdine(ordineServiceInstance);
			testUpdateOrdine(ordineServiceInstance);
			testOrdiniDiUnaCategoria(categoriaServiceInstance, articoloServiceInstance, ordineServiceInstance);
			testCaricaOrdinePiuRecenteDiUnaCategoria(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno(categoriaServiceInstance,
					articoloServiceInstance, ordineServiceInstance);
			testCaricaSommaPrezziDiUnDesinatario(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testListaDiIndirizziDiordiniAventiArticoliConSeriale(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);

			System.out.println("Nella tabella ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi");

			// *****************FINE TEST ORDINE********************************

			// *****************INIZIO TEST ARTICOLO****************************

			System.out.println(
					"Nella tabella articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi");
			testInsertArticolo(ordineServiceInstance, articoloServiceInstance);
			testUpdateArticolo(ordineServiceInstance, articoloServiceInstance);
			testRimuoviArticoloPrevioScollegamento(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testAggiungiCategoriaAAritcoloEsistente(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testOttieniSommaPrezziDiArticoliDiUnaCategoria(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testGetArticoliConErroriDOrdine(categoriaServiceInstance, articoloServiceInstance, ordineServiceInstance);
			System.out.println(
					"Nella tabella articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi");

			// *****************FINE TEST ARTICOLO******************************

			// *****************INIZIO TEST CATEGORIA****************************

			System.out.println(
					"Nella tabella categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi");
			testInsertCategoria(categoriaServiceInstance);
			testUpdateCategoria(categoriaServiceInstance);
			testAggiungiArticoloACategoriaEsistente(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			testRimuoviCategoriaPrevioScollegamento(categoriaServiceInstance, articoloServiceInstance,
					ordineServiceInstance);
			System.out.println(
					"Nella tabella categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi");

			// *****************FINE TEST CATEGORIA******************************

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerUtil.shutdown();
		}

	}

	// *****************INIZIO TEST ORDINE****************************

	private static void testInsertOrdine(OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testInsertOrdine************\n");

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);
		if (ordine.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testInsertOrdine: ordine non aggiunto**********\n");

		}

		ordineServiceIstance.rimuovi(ordine.getId());

		System.out.println("\n***********FINE TEST testInsertOrdine: PASSED************\n");

	}

	private static void testUpdateOrdine(OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testUpdateOrdine************\n");

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		String nomeOrdine = ordine.getNomeDestinatario();

		ordineServiceIstance.inserisciNuovo(ordine);
		if (ordine.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateOrdine: ordine non aggiunto**********\n");

		}

		ordine.setNomeDestinatario("Mimmo");
		ordineServiceIstance.aggiorna(ordine);

		Ordine ordineReload = ordineServiceIstance.caricaSingoloElemento(ordine.getId());
		String nomeOrdineNuovo = ordineReload.getNomeDestinatario();

		if (nomeOrdineNuovo.equals(nomeOrdine)) {

			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateOrdine: ordine non aggiornato**********\n");
		}

		ordineServiceIstance.rimuovi(ordine.getId());

		System.out.println("\n***********FINE TEST testUpdateOrdine: PASSED************\n");

	}

	private static void testOrdiniDiUnaCategoria(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testOrdiniDiUnaCategoria************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");
		Categoria categoria2 = new Categoria("Igene personale", "ignprsnl");

		categoriaServiceIstance.inserisciNuovo(categoria);
		categoriaServiceIstance.inserisciNuovo(categoria2);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 120D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);

		if (categoria.getId() == null || categoria2.getId() == null || ordine.getId() == null
				|| articolo.getId() == null || articolo2.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testOrdiniDiUnaCategoria: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria2, articolo2);

		List<Categoria> categorieDerivateDaMetodo = ordineServiceIstance.distinteCategorieDiUnOrdine(ordine);

		if (categorieDerivateDaMetodo.size() < 2) {
			throw new RuntimeException(
					"\n****************TEST FAILED testOrdiniDiUnaCategoria: ritorno inaspettato**********\n");

		}

		System.out.println("\n***********FINE TEST testOrdiniDiUnaCategoria: PASSED************\n");

	}

	private static void testCaricaOrdinePiuRecenteDiUnaCategoria(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testCaricaOrdinePiuRecenteDiUnaCategoria************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));
		Ordine ordine2 = new Ordine("Franco", "Via Roma 12", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);
		ordineServiceIstance.inserisciNuovo(ordine2);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 120D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);

		if (categoria.getId() == null || ordine.getId() == null || ordine2.getId() == null || articolo.getId() == null
				|| articolo2.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaOrdinePiuRecenteDiUnaCategoria: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo2);

		Ordine ordinePiuRecenteDiCategoria = ordineServiceIstance.caricaOrdinePiuRecenteDiUnaCategoria(categoria);

		System.out.println(ordinePiuRecenteDiCategoria.getNomeDestinatario());

		if (!ordinePiuRecenteDiCategoria.getNomeDestinatario().equals("Franco")) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaOrdinePiuRecenteDiUnaCategoria: ritorno inaspettato**********\n");

		}

		System.out.println("\n***********FINE TEST testCaricaOrdinePiuRecenteDiUnaCategoria: PASSED************\n");

	}

	private static void testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno(
			CategoriaService categoriaServiceIstance, ArticoloService articoloServiceIstance,
			OrdineService ordineServiceIstance) throws Exception {

		System.out.println(
				"\n***********INIZIO TEST testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");
		Categoria categoria2 = new Categoria("Giardinaggio", "GRDNGG");

		categoriaServiceIstance.inserisciNuovo(categoria);
		categoriaServiceIstance.inserisciNuovo(categoria2);

		Ordine ordine = new Ordine("Franco", "Via Roma 12", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 120D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);

		if (categoria.getId() == null || categoria2.getId() == null || ordine.getId() == null
				|| articolo.getId() == null || articolo2.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria2, articolo2);

		List<String> codiciTraDueDate = ordineServiceIstance
				.caricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno(5, 2022);

		if (codiciTraDueDate.isEmpty()) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno: ritorno inaspettato**********\n");

		}

		System.out.println(
				"\n***********FINE TEST testCaricaCodiciDiCategorieDiArtcioliDiOrdiniEffettuatiInMeseEAnno: PASSED************\n");

	}

	private static void testCaricaSommaPrezziDiUnDesinatario(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testCaricaSommaPrezziDiUnDesinatario************\n");

		Categoria categoria = new Categoria("Giardinaggio", "GRDNGG");

		categoriaServiceIstance.inserisciNuovo(categoria);

		String nomeDestinatario = "Franco" + new Date().getTime();

		Ordine ordine = new Ordine(nomeDestinatario, "Via Roma 12", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));
		Ordine ordine2 = new Ordine(nomeDestinatario, "Via Roma 12", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);
		ordineServiceIstance.inserisciNuovo(ordine2);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1000D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 100D, LocalDate.now());
		Articolo articolo3 = new Articolo("Sapone", "ABZXC098760", 12.3D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);
		articoloServiceIstance.inserisciNuovo(articolo3);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaSommaPrezziDiUnDesinatario: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo2);

		Double ritornoAspettato = articolo.getPrezzoSingolo() + articolo2.getPrezzoSingolo()
				+ articolo3.getPrezzoSingolo();
		Double ritornoEffetivo = ordineServiceIstance.caricaSommaPrezziDiUnDesinatario(nomeDestinatario);

		if (!ritornoEffetivo.equals(ritornoAspettato)) {
			throw new RuntimeException(
					"\n****************TEST FAILED testCaricaSommaPrezziDiUnDesinatario: ritorno inaspettato**********\n");

		}

		System.out.println("\n***********FINE TEST testCaricaSommaPrezziDiUnDesinatario: PASSED************\n");

	}

	private static void testListaDiIndirizziDiordiniAventiArticoliConSeriale(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out
				.println("\n***********INIZIO TEST testListaDiIndirizziDiordiniAventiArticoliConSeriale************\n");

		Categoria categoria = new Categoria("Giardinaggio", "GRDNGG");

		categoriaServiceIstance.inserisciNuovo(categoria);

		String nomeDestinatario = "Franco" + new Date().getTime();

		Ordine ordine = new Ordine(nomeDestinatario, "Via Roma 123", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));
		Ordine ordine2 = new Ordine(nomeDestinatario, "Via Milano 12", LocalDate.parse("2022-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);
		ordineServiceIstance.inserisciNuovo(ordine2);

		String codiceTipo = "abc" + new Date().getTime();

		Articolo articolo = new Articolo("Pannolini", codiceTipo, 1000D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", codiceTipo, 100D, LocalDate.now());
		Articolo articolo3 = new Articolo("Sapone", codiceTipo, 12.3D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);
		articoloServiceIstance.inserisciNuovo(articolo3);

		if (categoria.getId() == null || ordine.getId() == null || ordine2.getId() == null || articolo.getId() == null
				|| articolo2.getId() == null || articolo3.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testListaDiIndirizziDiordiniAventiArticoliConSeriale: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo2);

		List<String> listaIndirizzi = ordineServiceIstance.listaDiIndirizziDiordiniAventiArticoliConSeriale(codiceTipo);

		if (listaIndirizzi.size() != 2) {
			throw new RuntimeException(
					"\n****************TEST FAILED testListaDiIndirizziDiordiniAventiArticoliConSeriale: ritorno inaspettato**********\n");

		}

		System.out.println(
				"\n***********FINE TEST testListaDiIndirizziDiordiniAventiArticoliConSeriale: PASSED************\n");

	}

	// *****************FINE TEST ORDINE****************************

	// *****************INIZIO TEST ARTICOLO****************************

	private static void testInsertArticolo(OrdineService ordineServiceIstance, ArticoloService articoloServiceIstance)
			throws Exception {

		System.out.println("\n***********INIZIO TEST testInsertArticolo************\n");

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		ordineServiceIstance.inserisciNuovo(ordine);
		articoloServiceIstance.inserisciNuovo(articolo);
		if (ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testInsertArticolo: ordine o articolo non aggiunto**********\n");

		}

		articoloServiceIstance.rimuovi(articolo.getId());

		ordineServiceIstance.rimuovi(ordine.getId());

		System.out.println("\n***********FINE TEST testInsertArticolo: PASSED************\n");

	}

	private static void testUpdateArticolo(OrdineService ordineServiceIstance, ArticoloService articoloServiceIstance)
			throws Exception {

		System.out.println("\n***********INIZIO TEST testUpdateArticolo************\n");

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		ordineServiceIstance.inserisciNuovo(ordine);
		articoloServiceIstance.inserisciNuovo(articolo);
		if (ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateArticolo: ordine o articolo non aggiunto**********\n");

		}

		String descriptionArticolo = articolo.getDescrizione();

		articolo.setDescrizione("Giocattoli");

		articoloServiceIstance.aggiorna(articolo);

		Articolo articoloReload = articoloServiceIstance.caricaSingoloElemento(articolo.getId());

		String descriptionArticoloNew = articoloReload.getDescrizione();

		if (descriptionArticoloNew.equals(descriptionArticolo)) {

			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateArticolo: articolo non aggiornato**********\n");

		}

		articoloServiceIstance.rimuovi(articolo.getId()); // Rimozione di un articolo legato ad un ordine (non a delle
															// categorie)----fatto

		ordineServiceIstance.rimuovi(ordine.getId());

		System.out.println("\n***********FINE TEST testUpdateArticolo: PASSED************\n");

	}

	private static void testAggiungiCategoriaAAritcoloEsistente(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testAggiungiCategoriaAAritcoloEsistente************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testAggiungiCategoriaAAritcoloEsistente: ordine o articolo o categoria non aggiunti**********\n");

		}

		articoloServiceIstance.aggiungiCategoriaAArticoloEsistente(categoria, articolo);

		Articolo articoloReload = articoloServiceIstance.caricaSingoloElementoConCategorie(articolo.getId());

		if (articoloReload.getCategorie().isEmpty()) {
			throw new RuntimeException(
					"\n****************TEST FAILED testAggiungiCategoriaAAritcoloEsistente: ordine o articolo o categoria non aggiunti**********\n");

		}

		System.out.println("\n***********FINE TEST testAggiungiCategoriaAAritcoloEsistente: PASSED************\n");

	}

	private static void testRimuoviArticoloPrevioScollegamento(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testRimuoviArticoloPrevioScollegamento************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testRimuoviArticoloPrevioScollegamento: ordine o articolo o categoria non aggiunti**********\n");

		}

		articoloServiceIstance.aggiungiCategoriaAArticoloEsistente(categoria, articolo);

		articoloServiceIstance.rimuoviArticoloPrevioScollegamento(articolo.getId());

		if (articoloServiceIstance.caricaSingoloElemento(articolo.getId()) != null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testRimuoviArticoloPrevioScollegamento: articolo non eliminato**********\n");

		}

		System.out.println("\n***********FINE TEST testRimuoviArticoloPrevioScollegamento: PASSED************\n");
		ordineServiceIstance.rimuovi(ordine.getId());
		categoriaServiceIstance.rimuovi(categoria.getId());

	}

	private static void testOttieniSommaPrezziDiArticoliDiUnaCategoria(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testOttieniSommaPrezziDiArticoliDiUnaCategoria************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 12000D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 120D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null
				|| articolo2.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testOttieniSommaPrezziDiArticoliDiUnaCategoria: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo2);

		Double ritornoAtteso = articolo.getPrezzoSingolo() + articolo2.getPrezzoSingolo();

		Double totalePrezziDiUnaCategoria = articoloServiceIstance
				.ottieniSommaPrezziDiArticoliDiUnaCategoria(categoria);

		if (!totalePrezziDiUnaCategoria.equals(ritornoAtteso)) {
			throw new RuntimeException(
					"\n****************TEST FAILED testOttieniSommaPrezziDiArticoliDiUnaCategoria: ritorno inaspettato**********\n");

		}

		System.out
				.println("\n***********FINE TEST testOttieniSommaPrezziDiArticoliDiUnaCategoria: PASSED************\n");

	}

	private static void testGetArticoliConErroriDOrdine(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testGetArticoliConErroriDOrdine************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario" + new Date().getTime(), "Via Roma 12", LocalDate.parse("2024-05-20"),
				LocalDate.parse("2020-05-20"));
		Ordine ordine2 = new Ordine("Mario" + new Date().getTime(), "Via Roma 12", LocalDate.parse("2024-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);
		ordineServiceIstance.inserisciNuovo(ordine2);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 12000D, LocalDate.now());
		Articolo articolo2 = new Articolo("Sapone", "ABZXC098760", 120D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);
		articoloServiceIstance.inserisciNuovo(articolo2);

		if (categoria.getId() == null || ordine.getId() == null || ordine2.getId() == null || articolo.getId() == null
				|| articolo2.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testGetArticoliConErroriDOrdine: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);
		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo2);

		List<Articolo> articoliConOrdineStrano = articoloServiceIstance.getArticoliConErroriDOrdine();

		if (articoliConOrdineStrano.size() < 2) {
			throw new RuntimeException(
					"\n****************TEST FAILED testGetArticoliConErroriDOrdine: ritorno inaspettato**********\n");

		}

		System.out.println("\n***********FINE TEST testGetArticoliConErroriDOrdine: PASSED************\n");

	}

	// *****************FINE TEST ARTICOLO****************************

	// *****************INIZIO TEST CATEGORIA****************************

	private static void testInsertCategoria(CategoriaService categoriaServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testInsertCategoria************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);
		if (categoria.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testInsertCategoria: ordine o articolo non aggiunto**********\n");

		}

		categoriaServiceIstance.rimuovi(categoria.getId());

		System.out.println("\n***********FINE TEST testInsertCategoria: PASSED************\n");

	}

	private static void testUpdateCategoria(CategoriaService categoriaServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testUpdateCategoria************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);
		if (categoria.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateCategoria: ordine o articolo non aggiunto**********\n");

		}

		String descrizione = categoria.getDescrizione();

		categoria.setDescrizione("Cura della persona");

		categoriaServiceIstance.aggiorna(categoria);

		Categoria categoriaReload = categoriaServiceIstance.caricaSingoloElemento(categoria.getId());

		String descrizioneNew = categoriaReload.getDescrizione();

		if (descrizioneNew.equals(descrizione)) {
			throw new RuntimeException(
					"\n****************TEST FAILED testUpdateCategoria: categoria non aggiornata**********\n");

		}

		categoriaServiceIstance.rimuovi(categoria.getId());

		System.out.println("\n***********FINE TEST testUpdateCategoria: PASSED************\n");

	}

	private static void testAggiungiArticoloACategoriaEsistente(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testAggiungiArticoloACategoriaEsistente************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testAggiungiArticoloACategoriaEsistente: ordine o articolo o categoria non aggiunti**********\n");

		}

		categoriaServiceIstance.aggiungiArticoloACategoriaEsistente(categoria, articolo);

		Categoria categoriaReload = categoriaServiceIstance.caricaSingoloElemento(categoria.getId());

		if (categoriaReload.getArticoli().isEmpty()) {
			throw new RuntimeException(
					"\n****************TEST FAILED testAggiungiArticoloACategoriaEsistente: ordine o articolo o categoria non aggiunti**********\n");

		}

		System.out.println("\n***********FINE TEST testAggiungiArticoloACategoriaEsistente: PASSED************\n");

	}

	private static void testRimuoviCategoriaPrevioScollegamento(CategoriaService categoriaServiceIstance,
			ArticoloService articoloServiceIstance, OrdineService ordineServiceIstance) throws Exception {

		System.out.println("\n***********INIZIO TEST testRimuoviCategoriaPrevioScollegamento************\n");

		Categoria categoria = new Categoria("Prodotti per la casa", "PRSTPLC");

		categoriaServiceIstance.inserisciNuovo(categoria);

		Ordine ordine = new Ordine("Mario", "Via Roma 12", LocalDate.parse("2020-05-20"),
				LocalDate.parse("2020-05-20"));

		ordineServiceIstance.inserisciNuovo(ordine);

		Articolo articolo = new Articolo("Pannolini", "ABZXC098760", 1200D, LocalDate.now());

		articoloServiceIstance.inserisciNuovo(articolo);

		if (categoria.getId() == null || ordine.getId() == null || articolo.getId() == null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testRimuoviCategoriaPrevioScollegamento: ordine o articolo o categoria non aggiunti**********\n");

		}

		articoloServiceIstance.aggiungiCategoriaAArticoloEsistente(categoria, articolo);

		categoriaServiceIstance.rimuoviCategoriaPrevioScollegamento(categoria.getId());

		if (categoriaServiceIstance.caricaSingoloElemento(categoria.getId()) != null) {
			throw new RuntimeException(
					"\n****************TEST FAILED testRimuoviCategoriaPrevioScollegamento: articolo non eliminato**********\n");

		}

		System.out.println("\n***********FINE TEST testRimuoviCategoriaPrevioScollegamento: PASSED************\n");

		articoloServiceIstance.rimuovi(articolo.getId());
		ordineServiceIstance.rimuovi(ordine.getId());// test rimuovi ordine e creata eccezione nel caso in cui ci siano
														// articoli
	}

	// *****************FINE TEST CATEGORIA****************************
}
