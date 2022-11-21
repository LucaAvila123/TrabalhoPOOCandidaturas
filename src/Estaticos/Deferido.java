package Estaticos;

import java.util.*;

public class Deferido {
    // numeros que indicam candidato eleito
    private static Integer[] a = new Integer[]{2, 16};

    private static List<Integer> lista = Arrays.asList(a);

    public static boolean igualDeferido(int valor){
        return lista.contains(valor);
    }
}
