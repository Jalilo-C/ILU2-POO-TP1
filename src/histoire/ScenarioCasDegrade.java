package histoire;

import personnages.Gaulois;
import villagegaulois.Etal;

public class ScenarioCasDegrade {
    public static void main(String[] args) {

        Etal etal = new Etal();
        Gaulois acheteur = null;

        try {
            etal.acheterProduit(-2, acheteur);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur quantité : " + e.getMessage());
        }

        try {
            etal.acheterProduit(2, acheteur);
        } catch (IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        try {
            etal.occuperEtal(acheteur, "apples", 6);   
            etal.acheterProduit(2, acheteur);
        } catch (NullPointerException e) {
            System.out.println("Gaulois null.\n");
        }

        System.out.println("Fin du test");
    }
}
