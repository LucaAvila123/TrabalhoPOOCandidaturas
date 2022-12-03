package Candidaturas;

// tirado de https://acervolima.com/interface-de-comparacao-em-java-com-exemplos/
//import java.util.*;


public class MaisVotos /*implements Comparator<Candidato>*/ {
   
   /*  public int compare(Candidato a, Candidato b)
    {
        // a ideia é deixar a ordem decrescente 
        int ordem = b.getTotalDeVotos() - a.getTotalDeVotos();
        
        // se há empate no número de votos de cada candidato, usa a idade
        if(ordem == 0){
            // verificando se b é mais velho que a    
            int diferencaIdade = b.getIdade() - a.getIdade();

                // se os dois tiverem a mesma idade, tem que usar o número do candidato
                if(diferencaIdade == 0){
                    int menorNumero = a.getNumeroDoCandidato() - b.getNumeroDoCandidato();

                    return menorNumero;
                }
                return diferencaIdade;
        }
        return ordem;
    }
    */
}
