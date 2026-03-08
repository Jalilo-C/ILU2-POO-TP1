package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	
	private static class Marche {
		private Etal[] etals;

		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < etals.length; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur,
				String produit, int nbProduit) {
			if(indiceEtal >= etals.length || indiceEtal<0) {
				return;
			}
			
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (! etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit) {
			int nb = 0;
			for (int i = 0; i < etals.length; i++) {
				if(etals[i].contientProduit(produit)) {
					nb++;
				}
			}
			
			Etal[] etalProduit = new Etal[nb];
			int j = 0;
			
			for (int i = 0; i < etals.length; i++) {
				if(etals[i].contientProduit(produit)) {
					etalProduit[j] = etals[i];
					j++;
				}
			}
			
			return etalProduit;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].getVendeur()==gaulois) {
					return etals[i];
				}
			}
			return null;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int n = 0;
			for (int i = 0; i < etals.length; i++) {
				if(etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
					chaine.append("\n");
					n++;
				}
			}
			
			int nRestant = etals.length - n;
			if (nRestant != 0) {
				chaine.append("il reste " + nRestant + " étals non utilisés dans le marché.\n");
			}
			
			return chaine.toString();
		}
		
	}
	
	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- ");
				chaine.append(villageois[i].getNom());
				chaine.append("\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit,int
			nbProduit) {
		StringBuilder chaine = new StringBuilder();
		int nbLibre = marche.trouverEtalLibre() + 1;
		if (nbLibre == 0) {
			chaine.append("Il n'y a plus d'étals libres dans le marche.\n");
			return chaine.toString();
		}
		marche.utiliserEtal(nbLibre-1, vendeur, produit, nbProduit);
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + nbLibre + ".\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] vendeurProduit = marche.trouverEtals(produit);
		if(vendeurProduit.length==0) {
			chaine.append("Il n'y a pas de vendeur qui propose des " + produit + " au marché.\n");
		} else if (vendeurProduit.length==1) {
			chaine.append("Seul le vendeur " + vendeurProduit[0].getVendeur().getNom() + " propose des " + produit + " au marché.\n");
		} else {
			chaine.append("Les vendeurs qui proposent des " + produit + " sont :\n");
			for (int i = 0; i < vendeurProduit.length; i++) {
				chaine.append("- ");
				chaine.append(vendeurProduit[i].getVendeur().getNom());
				chaine.append("\n");
			}
		}
			
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		StringBuilder chaine = new StringBuilder();
		for (int i = 0; i < marche.etals.length; i++) {
			if(marche.etals[i].getVendeur() == vendeur) {
				try {
					chaine.append(marche.etals[i].libererEtal());
				} catch (IllegalStateException e) {
					System.out.println("Etal non vide.\n");
				}
			}
		}
		return chaine.toString();
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marché du village \"" + getNom() + "\" possède plusieurs étals :\n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
}