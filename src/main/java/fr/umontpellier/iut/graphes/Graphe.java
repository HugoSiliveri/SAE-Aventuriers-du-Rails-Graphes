package fr.umontpellier.iut.graphes;


import java.util.*;

public class Graphe {
    /**
     * matrice d'adjacence du graphe, un entier supérieur à 0 représentant la distance entre deux sommets
     * mat[i][i] = 0 pour tout i parce que le graphe n'a pas de boucle
     */
    private final int[][] mat;

    /**
     * Construit un graphe à n sommets
     *
     * @param n le nombre de sommets du graphe
     */
    public Graphe(int n) {
        mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = 0;
            }
        }
    }

    public static Graphe copie(Graphe g) {
        Graphe copieG = new Graphe(g.mat.length);
        for(int i = 0; i < g.mat.length; i++) {
            for (int j = 0; j < g.mat.length; j++) {
                if (g.mat[i][j] > 0) copieG.ajouterArete(i, j, g.mat[i][j]);
            }
        }
        return copieG;
    }

    /**
     * @return le nombre de sommets
     */
    public int nbSommets() {
        return mat.length;
    }

    /**
     * Supprime l'arête entre les sommets i et j
     *
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     */
    public void supprimerArete(int i, int j) {
        mat[i][j] = 0;
        mat[j][i] = 0;
    }

    /**
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     * @param k la distance entre i et j (k>0)
     */
    public void ajouterArete(int i, int j, int k) {
        mat[i][j] = k;
        mat[j][i] = k;
    }

    /*** 
     * @return le nombre d'arête du graphe
     */
    public int nbAretes() {
        int nbAretes = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = i + 1; j < mat.length; j++) {
                if (mat[i][j] > 0) {
                    nbAretes++;
                }
            }
        }
        return nbAretes;
    }

    /**
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     * @return vrai s'il existe une arête entre i et j, faux sinon
     */
    public boolean existeArete(int i, int j) {
        return mat[i][j] > 0;
    }

    /**
     * @param v un entier représentant un sommet du graphe
     * @return la liste des sommets voisins de v
     */
    public ArrayList<Integer> voisins(int v) {
        ArrayList<Integer> voisin = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            if (existeArete(v,i)) voisin.add(i); //Pour chaque sommet du graphe, on vérifie s'il existe une arête entre v et ce dernier.
        }
        return voisin;
    }

    /**
     * @return une chaîne de caractères permettant d'afficher la matrice mat
     */
    public String toString() {
        StringBuilder res = new StringBuilder("\n");
        for (int[] ligne : mat) {
            for (int j = 0; j < mat.length; j++) {
                String x = String.valueOf(ligne[j]);
                res.append(x);
                res.append('\t');
            }
            res.append("\n");
        }
        return res.toString();
    }

    /**
     * Calcule la classe de connexité du sommet v
     *
     * @param v un entier représentant un sommet
     * @return une liste d'entiers représentant les sommets de la classe de connexité de v
     */
    public ArrayList<Integer> calculerClasseDeConnexite(int v) {
        ArrayList<Integer> bleus = new ArrayList<>();
        ArrayList<Integer> rouges = new ArrayList<>();
        bleus.add(v);
        while(!bleus.isEmpty()) {
            int sommet = bleus.remove(0);
            ArrayList<Integer> voisinsNonMarques = voisins(sommet);
            voisinsNonMarques.removeAll(rouges);
            voisinsNonMarques.removeAll(bleus);
            bleus.addAll(voisinsNonMarques);
            rouges.add(sommet);
        }
        return rouges;
    }

    /**
     * @return la liste des classes de connexité du graphe
     */
    public ArrayList<ArrayList<Integer>> calculerClassesDeConnexite() {
        ArrayList<ArrayList<Integer>> listeCDC = new ArrayList<>();
        ArrayList<Integer> sommets = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) sommets.add(i);
        while (!sommets.isEmpty()) {
            ArrayList<Integer> cdc = calculerClasseDeConnexite(sommets.remove(0));
            sommets.removeAll(cdc);
            listeCDC.add(cdc);
        }
        return listeCDC;
    }

    /**
     * @return le nombre de classes de connexité
     */
    public int nbCC() {
        return calculerClassesDeConnexite().size();
    }

    /**
     * @param u un entier représentant un sommet
     * @param v un entier représentant un sommet
     * @return vrai si (u,v) est un isthme, faux sinon
     */
    public boolean estUnIsthme(int u, int v) {
        if (!existeArete(u, v)) return false; //Si il n'y a pas d'arête entre les 2 sommets, il ne peut pas y avoir d'isthme.
        Graphe g = copie(this); //Création d'une copie du graphe
        g.supprimerArete(u, v); //Suppression de l'arête dans la copie
        return g.nbCC() > nbCC(); //Vérification du nombre de classes de connexité dans le graphe sans l'arête par rapport au graphe d'origine.
    }


    /**
     * Calcule le plus long chemin présent dans le graphe
     *
     * @return une liste de sommets formant le plus long chemin dans le graphe
     */
    public ArrayList<Integer> plusLongChemin() {
        ArrayList<Integer> plusLongChemin = new ArrayList<>();
        for (int i = 0; i < nbCC(); i++) {
            ArrayList<Integer> CDC = calculerClassesDeConnexite().get(i); //Pour chaque composante connexe, calcul de tous les chemins pour chaque sommet
            for (int j = 0; j < CDC.size(); j++) {
                for (ArrayList<Integer> c : cheminsDuSommet(CDC.get(0))) {
                    if (valuationChemin(plusLongChemin) < valuationChemin(c)) plusLongChemin = c; //Si le chemin c du sommet actuel est plus long
                                                                                                 // que le précédent chemin enregistré alors on met à jour ce dernier.
                }
            }
        }
        return plusLongChemin;
    }

    /**
     * Calcule tous les chemins possibles pour un sommet donné.
     *
     * @param s un sommet du graphe compris entre 0 et mat.length - 1
     * @return La liste de tous les chemins pour le sommet s.
     */
    private ArrayList<ArrayList<Integer>> cheminsDuSommet(int s) {
        HashSet<ArrayList<Integer>> chemins = new HashSet<>(); //Initialisation de la liste des chemins comme un ensemble pour éviter les doublons.
        Graphe g = copie(this); //Création d'une copie du graphe pour ne pas modifier le graphe d'origine.
        ArrayList<Integer> chemin = new ArrayList<>();
        chemin.add(s); //Ajout du sommet s au chemin courant calculé
        for (int v : g.voisins(s)) {
            g.supprimerArete(s, v); //Pour chaque voisin du sommet s : on supprime l'arête en s et ce voisin
                                    //Cela permet d'éviter que cette arête soir retraversée.
            for (ArrayList<Integer> c : g.cheminsDuSommet(v)) { //A partir de la copie du graphe sans l'arête
                                                                //On rappelle le construction de tous les chemins d'un sommet sur le voisin v
                ArrayList<Integer> sousChemin = new ArrayList<>(chemin); //On crée un sousChemin à partir du chemin courant
                sousChemin.addAll(c); //On ajoute au sousChemin un chemin c du sommet v
                chemins.add(sousChemin); //On enregistre notre sousChemin dans notre liste de chemins finale.
            }
        }
        chemins.add(chemin); //On ajoute le 1er chemin créé à notre liste de chemin.
        return new ArrayList<>(chemins);
    }

    /**
     * Calcule la valuation d'un chemin passé en paramètre.
     *
     * @param chemin une ArrayList qui représente un chemin dans le graphe.
     * @return la valuation du chemin
     */
    public int valuationChemin(ArrayList<Integer> chemin) {
        int valeur = 0;
        for (int i = 0; i < chemin.size()-1; i++) {
            valeur += mat[chemin.get(i)][chemin.get(i+1)];
        }
        return valeur;
    }


    /**
     * @return vrai s'il existe un parcours eulérien dans le graphe, faux sinon
     */
    public boolean existeParcoursEulerien() {
        boolean eulerien = nbCC() == 1; //Première vérification : le graphe est connexe.
        int i = 0; //Curseur de sommet
        int impairs = 0; //Compteur de sommets de degré impairs
        while (i < mat.length && eulerien) { //Pour chaque sommet
            if(voisins(i).size() % 2 != 0) impairs++; //Si le degré du sommet est impair on incrémente le compteur.
            if (impairs > 2) eulerien = false; //S'il y'a plus de 2 sommets de degré impairs, le graphe ne possède pas de parcours eulérien
            i++; //On met à jour le curseur de sommet
        }
        return eulerien;
    }

    /**
     * @return vrai si le graphe est un arbre, faux sinon
     */
    public boolean estUnArbre() {
        return nbCC() == 1 && nbAretes() == nbSommets() - 1; //Vérification que l'arbre est connexe et que sa taille est égale à son ordre - 1
    }

}