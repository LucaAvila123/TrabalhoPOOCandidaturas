package Estaticos;

import java.util.*;

public class Eleito {
    // numeros que indicam candidato eleito
    private static Integer[] a = new Integer[]{2, 3};
    
    private static List<Integer> lista = Arrays.asList(a);

    public static boolean igualEleito(int valor){
        return lista.contains(valor);
    }
}
