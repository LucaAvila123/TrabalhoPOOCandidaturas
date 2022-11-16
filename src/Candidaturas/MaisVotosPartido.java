package Candidaturas;
// tirado de https://acervolima.com/interface-de-comparacao-em-java-com-exemplos/
import java.util.*;

public class MaisVotosPartido implements Comparator<Partido> {
    public int compare(Partido a, Partido b)
    {
        // a ideia é deixar a ordem decrescente 
        int ordem = b.getVotosValidos() - a.getVotosValidos();
        
        // se há empate nos votos dos partidos, pega-se o que tem menor número na urna
        if(ordem == 0){
            return a.getNumeroDoPartido() - b.getNumeroDoPartido();
        }

        return ordem;
    }
}