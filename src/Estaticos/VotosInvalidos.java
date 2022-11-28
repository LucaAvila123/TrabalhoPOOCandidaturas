package Estaticos;

import java.util.*;

public class VotosInvalidos {
     // numeros que indicam votos inválidos (branco, nulos ou anulados)
     private static Integer[] a = new Integer[]{95, 96, 97, 98};

     private static List<Integer> lista = Arrays.asList(a);
 
     public static boolean ignorarNumero(int valor){
        return lista.contains(valor);
    }
}