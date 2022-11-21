package Estaticos;

public class Federado {
    // -1 indica que o candidato não é federado
    public static boolean ehFederado(int valor){
        if(valor != -1)
            return true;
        return false;
    }
}
