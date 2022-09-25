package fr.umontpellier.iut.graphes;


import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheTest {

    //ajouter controle des exceptions et boucles infinies

    private static Graphe buildGraphe(int n, int[][] aretes){
        Graphe res = new Graphe(n);
        for(int i=0;i<aretes.length;i++){
            res.ajouterArete(aretes[i][0],aretes[i][1],aretes[i][2]);
        }
        return res;
    }

    @Test
    void testCCdeV() {
        Graphe g = buildGraphe(4,new int[][]{{0,1,1},{2,3,1}});
        ArrayList<Integer> res = g.calculerClasseDeConnexite(2);
        //conversion en set pour que le equals ci-dessous soit bien une égalité de set et pas une égalité d'arrayList (qui tiendrait compte de l'ordre)
        HashSet<Integer> resSet = new HashSet<>(res);

        HashSet<Integer> answer = new HashSet<>();
        answer.add(3);
        answer.add(2);



        assertEquals(resSet, answer);
    }

    @Test
    void testCC() {
        Graphe g = buildGraphe(4,new int[][]{{0,1,1},{2,3,1}});
        ArrayList<ArrayList<Integer>> res = g.calculerClassesDeConnexite();
        //conversion en set pour que le equals ci-dessous soit bien une égalité de set et pas une égalité d'arrayList (qui tiendrait compte de l'ordre)
        HashSet<HashSet<Integer>> resSet = new HashSet<>();
        for(ArrayList<Integer> cc : res){
            resSet.add(new HashSet<>(cc));
        }

        HashSet<Integer> cc1 = new HashSet<>();
        cc1.add(3);
        cc1.add(2);

        HashSet<Integer> cc2 = new HashSet<>();
        cc2.add(0);
        cc2.add(1);

        HashSet<HashSet<Integer>> answer = new HashSet<>();
        answer.add(cc1);
        answer.add(cc2);
        //System.out.println(resSet);

        assertEquals(resSet, answer);
    }

    @Test
    void testCCPlusDur() {
        Graphe g = buildGraphe(4,new int[][]{{2,3,1},{2,1,1},{1,3,1}});
        ArrayList<ArrayList<Integer>> res = g.calculerClassesDeConnexite();
        System.out.println(res);
    }

    @Test
    void testEstUnIsthme() {
        Graphe g = buildGraphe(4,new int[][]{{0,1,1},{2,3,1},{2,1,1},{1,3,1}});
        assertTrue(g.estUnIsthme(0,1));
        assertFalse(g.estUnIsthme(1,3));
    }

    @Test
    void testPlusLongChemin() {
        Graphe g = buildGraphe(5,new int[][]{{3,4,1},{4,1,1},{0,1,1},{2,3,1},{2,1,1},{1,3,1}});

        ArrayList<Integer> L = new ArrayList<Integer>();
        L.add(0);
        L.add(1);
        L.add(2);
        L.add(3);
        L.add(1);
        L.add(4);
        L.add(3);
        assertEquals(g.plusLongChemin(),L);
    }


    @Test
    void testExisteParcoursEulerien() {
        Graphe g = buildGraphe(5,new int[][]{{0,1,1},{1,2,1},{2,0,1},{1,3,1},{2,4,1}});

        assertFalse(g.existeParcoursEulerien());
    }

    @Test
    void testEstUnArbre() {
        Graphe g = buildGraphe( 13, new int[][]{{0,1,1},{1,2,1},{2,3,1},{2,4,1},{2,8,1},{4,5,1},{5,6,1},{5,7,1},{8,9,1},{8,10,1},{8,11,1},{11,12,1}});
        assertTrue(g.estUnArbre());
    }


    @Test
    void testExisteArete() {
        Graphe g = new Graphe(5);
        g.ajouterArete(1,2, 2);
        g.ajouterArete(2,3,2);
        g.ajouterArete(2,4,2);
        g.ajouterArete(3,4,2);
        System.out.println(g);
        assertTrue(g.existeArete(2,3));
        assertFalse(g.existeArete(1,3));
    }

    @Test
    void testVoisinsDeC() {
        // C est le sommet 2.
        Graphe g = new Graphe(5);
        g.ajouterArete(1,2, 2);
        g.ajouterArete(2,3,2);
        g.ajouterArete(2,4,2);
        g.ajouterArete(3,4,2);
        ArrayList<Integer> answer = new ArrayList<>();
        answer.add(1);
        answer.add(3);
        answer.add(4);
        assertTrue(answer.containsAll(g.voisins(2)) && g.voisins(2).containsAll(answer));
    }

    @Test
    void testCDCdeB() {
        // B est le sommet 1.
        Graphe g = buildGraphe(10, new int[][]{{0,1,1},{1,2,1},{2,3,1},{3,4,1},{4,5,1},{4,7,1},{4,6,1},{8,9,1}});
        HashSet<Integer> resultat = new HashSet<>();
        for (int i=0; i<8;i++){
            resultat.add(i);
        }
        ArrayList<Integer> res = g.calculerClasseDeConnexite(1);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);
    }

    @Test
    void testCDCdeE() {
        // E est le sommet 4.
        Graphe g = buildGraphe(10, new int[][]{{0,1,1},{1,2,1},{2,3,1},{3,4,1},{4,5,1},{4,7,1},{4,6,1},{8,9,1}});
        HashSet<Integer> resultat = new HashSet<>();
        for (int i=0; i<8;i++){
            resultat.add(i);
        }
        ArrayList<Integer> res = g.calculerClasseDeConnexite(4);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);
    }

    @Test
    void testEulerien() {
        Graphe g = buildGraphe(5, new int[][]{{0,1,1},{0,4,1},{0,3,1},{1,2,1},{2,3,1},{4,2,1}});
        assertTrue(g.existeParcoursEulerien());
    }

    @Test
    void testCycleOrdre10(){
        Graphe g = buildGraphe(10, new int[][]{{0,1,1},{1,2,2},{2,3,3},{3,4,4},{4,5,5},{5,6,6},{6,7,7},{7,8,8},{8,9,9},{9,0,10}});

        //TEST CLASSE DE CONNEXITE
        HashSet<Integer> cdc = new HashSet<>();
        for (int i =0; i<10;i++){
            cdc.add(i);
        }
        HashSet<Integer> calculCDC = new HashSet<>(g.calculerClasseDeConnexite(0));
        assertEquals(calculCDC, cdc );

        // TEST EST VOISIN
        ArrayList<Integer> voisins = new ArrayList<>();
        voisins.add(1);
        voisins.add(9);
        assertEquals(g.voisins(0), voisins);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(3,4));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertTrue(g.existeParcoursEulerien());

        //TEST PLUS LONG PARCOURS
        HashSet<Integer> plusLong = new HashSet<>(g.calculerClasseDeConnexite(3));
        HashSet<Integer> plusLong2 = new HashSet<>(g.plusLongChemin());
        assertEquals(plusLong, plusLong2);
    }

    @Test
    void testGrapheCompletOrdre21() {
        Graphe g = new Graphe(21);
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if (i != j) g.ajouterArete(i, j, 1);
            }
        }

        //TEST CLASSE DE CONNEXITE
        HashSet<Integer> cdc = new HashSet<>();
        for (int i =0; i<21;i++){
            cdc.add(i);
        }
        HashSet<Integer> calculCDC = new HashSet<>(g.calculerClasseDeConnexite(0));
        assertEquals(calculCDC, cdc );

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        for (int i = 1; i < 21; i++) resultat.add(i);
        ArrayList<Integer> res =  g.voisins(0);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(3,4));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertTrue(g.existeParcoursEulerien());


    }

    @Test
    void testGrapheCompletOrdre10(){
        Graphe g = new Graphe(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i != j) g.ajouterArete(i, j, 1);
            }
        }

        //TEST CLASSE DE CONNEXITE
        HashSet<Integer> cdc = new HashSet<>();
        for (int i =0; i<10;i++){
            cdc.add(i);
        }
        HashSet<Integer> calculCDC = new HashSet<>(g.calculerClasseDeConnexite(0));
        assertEquals(calculCDC, cdc );

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        for (int i = 1; i < 10; i++) resultat.add(i);
        ArrayList<Integer> res =  g.voisins(0);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(3,4));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertFalse(g.existeParcoursEulerien());


    }

    @Test
    void testArbreOrdre10avec4feuille(){
        Graphe g = buildGraphe(10, new int[][]{{0,1,1},{1,2,1},{2,3,1},{3,4,1},{2,5,1},{5,7,1},{7,6,1},{7,8,1},{8,9,1}});
        //TEST CLASSE DE CONNEXITE
        HashSet<Integer> cdc = new HashSet<>();
        for (int i =0; i<10;i++){
            cdc.add(i);
        }
        HashSet<Integer> calculCDC = new HashSet<>(g.calculerClasseDeConnexite(0));
        assertEquals(calculCDC, cdc );

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        resultat.add(2);
        resultat.add(7);
        ArrayList<Integer> res =  g.voisins(5);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertTrue(g.estUnIsthme(1,2));

        // TEST EST UN ARBRE
        assertTrue(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertFalse(g.existeParcoursEulerien());

        //TEST PLUS LONG PARCOURS
        ArrayList<Integer> sommets = new ArrayList<>();
        sommets.add(0);
        sommets.add(1);
        sommets.add(2);
        sommets.add(5);
        sommets.add(7);
        sommets.add(8);
        sommets.add(9);
        HashSet<Integer> plusLong = new HashSet<>(sommets);
        HashSet<Integer> plusLong2 = new HashSet<>(g.plusLongChemin());
        assertEquals(plusLong, plusLong2);
    }

    @Test
    void testGrapheNConnexeAvec4SommetsIsolesEt2ComposantesOrdre3(){
        Graphe g = buildGraphe(10, new int[][]{{4,5,1},{5,6,1},{6,4,1},{7,8,1},{8,9,1},{9,7,1}});

        //TEST NOMBRE DE CLASSES DE CONNEXITE
        assertEquals(6, g.nbCC());

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        ArrayList<Integer> res =  g.voisins(0);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(1,2));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertFalse(g.existeParcoursEulerien());

        //TEST TAILLE PLUS LONG CHEMIN
        assertEquals(4, g.plusLongChemin().size());
    }

    @Test
    void testGrapheEulerienOrdre10(){
        Graphe g = buildGraphe(10, new int[][]{{0,1,1},{1,2,1},{2,3,1},{3,4,1},{4,0,1},{4,9,1},{4,5,1},{5,6,1},{6,7,1},{7,8,1},{8,9,1}});

        //TEST NOMBRE DE CLASSES DE CONNEXITE
        assertEquals(1, g.nbCC());

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        resultat.add(0);
        resultat.add(3);
        resultat.add(5);
        resultat.add(9);
        ArrayList<Integer> res =  g.voisins(4);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(1,2));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertTrue(g.existeParcoursEulerien());

        //TEST TAILLE PLUS LONG CHEMIN
        assertEquals(11, g.plusLongChemin().size());
    }

    @Test
    void testGrapheOrdreMini10PasEulerienEtPasArbre(){
        Graphe g = buildGraphe(10,new int[][]{});

        //TEST NOMBRE DE CLASSES DE CONNEXITE
        assertEquals(10, g.nbCC());

        // TEST VOISINS
        HashSet<Integer> resultat = new HashSet<>();
        ArrayList<Integer> res =  g.voisins(4);
        HashSet<Integer> resSet = new HashSet<>(res);
        assertEquals(resultat, resSet);

        // TEST EST UN ISTHME
        assertFalse(g.estUnIsthme(1,2));

        // TEST EST UN ARBRE
        assertFalse(g.estUnArbre());

        //TEST POSSEDE UN PARCOURS EULERIEN
        assertFalse(g.existeParcoursEulerien());

        //TEST TAILLE PLUS LONG CHEMIN
        assertEquals(0 , g.plusLongChemin().size());
    }

    @Test
    void testPlusLongChemin_graphe_ordre9() {
        Graphe g = buildGraphe(9,new int[][]{{0,1,1},{0,4,3},{1,2,2},{1,3,1},{2,3,1},{2,7,5},{3,4,4},{4,5,5},{5,8,1},{5,6,3},{6,7,2},{6,8,7},{7,8,1}});
        //System.out.println(g.plusLongChemin());
        ArrayList<Integer> resultat = new ArrayList<>(Arrays.asList(0,4,3,1,2,7,8,6,5,4));
        assertEquals(resultat, g.plusLongChemin());
        //[0, 4, 3, 1, 2, 7, 8, 6, 5, 4]*/
    }
}
