package it.prova.gestioneordiniarticolicategorie.service;

import it.prova.gestioneordiniarticolicategorie.dao.MyFactoryDAO;

public class MyServiceFactory {

	private static OrdineService ordineServiceInstance;
	private static ArticoloService articoloServiceInstance;
	private static CategoriaService categoriaServiceInstance;

	public static OrdineService getOrdineServiceInstance() {
		if (ordineServiceInstance == null)
			ordineServiceInstance = new OrdineServiceImpl();

		ordineServiceInstance.setOrdineDAO(MyFactoryDAO.getOrdineDAOInstance());
		return ordineServiceInstance;
	}

	public static ArticoloService getArticoloServiceInstance() {
		if (articoloServiceInstance == null)
			articoloServiceInstance = new ArticoloServiceImpl();

		articoloServiceInstance.setArticoloDAO(MyFactoryDAO.getArticoloDAOInstance());
		return articoloServiceInstance;
	}

	public static CategoriaService getCategoriaServiceInstance() {
		if (categoriaServiceInstance == null)
			categoriaServiceInstance = new CategoriaServiceImpl();

		categoriaServiceInstance.setCategoriaDAO(MyFactoryDAO.getCategoriaDAOInstance());
		return categoriaServiceInstance;

	}
}
