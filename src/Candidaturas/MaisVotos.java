package Candidaturas;

// tirado de https://acervolima.com/interface-de-comparacao-em-java-com-exemplos/
import java.util.Comparator;

public class MaisVotos implements Comparator<Candidato> {
    public int compare(Candidato a, Candidato b)
    {
        // a ideia é deixar a ordem decrescente
        int ordem = b.getTotalDeVotos() - a.getTotalDeVotos();

        // TODO: resolver a questão de quem é mais velho (será que precisa ir até os segundos)
        return ordem;
    }
}