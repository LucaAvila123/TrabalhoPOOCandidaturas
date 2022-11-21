package Enum;

import java.util.*;

public class Eleito {
    private static Integer[] a = new Integer[]{2, 16};

    private static List<Integer> lista = Arrays.asList(a);

    public static boolean igualEleito(int valor){
        return lista.contains(valor);
    }
}
