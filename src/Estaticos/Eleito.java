package Estaticos;

import java.util.*;

public class Eleito {
    // numeros que indicam candidato eleito
    private static Integer[] a = new Integer[]{2, 16};

    // -1 indica que o candidato não é federado
    public static boolean ehFederado(int valor){
        if(valor != -1)
            return true;
        return false;
    }
    private static List<Integer> lista = Arrays.asList(a);

    public static boolean igualEleito(int valor){
        return lista.contains(valor);
    }
}
