package Candidaturas;

import java.text.*;
// tirado de https://acervolima.com/interface-de-comparacao-em-java-com-exemplos/
import java.util.*;

public class MaisVotos implements Comparator<Candidato> {
    public int compare(Candidato a, Candidato b)
    {
        // a ideia é deixar a ordem decrescente 
        int ordem = b.getTotalDeVotos() - a.getTotalDeVotos();
        
        // se há empate no número de votos de cada candidato
        if(ordem == 0){
            try {
                return (int) (b.getIdade() - a.getIdade());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ordem;
    }
}