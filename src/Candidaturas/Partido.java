package Candidaturas;

import java.util.*;

import Relatorios.*;

// import Relatorios.SistemaEleitoral;

public class Partido {
    private int numeroDoPartido;
    private String sigla;

    private int votosValidos = 0;
    private int votosNominais = 0;
    private int votosDeLegenda = 0; 

    // a lista aqui é a estrutura de dados escolhida porque não será necessário acessar diretamente os valores
    private List<Candidato> candidatosPartido;

    //Constructor
    public Partido(int numeroDoPartido, String sigla) {
        this.numeroDoPartido = numeroDoPartido;
        this.sigla = sigla;
        this.candidatosPartido = new ArrayList<>();
    }

    //Getters
    public int getNumeroDoPartido() {
        return numeroDoPartido;
    }
    
    public String getSigla() {
        return sigla;
    }

    public int getVotosValidos() {
        return votosValidos;
    }

    public int getVotosDeLegenda() {
        return votosDeLegenda;
    }

    public int getVotosNominais() {
        return votosNominais;
    }

    public List<Candidato> getCandidatosPartido(){
        return new ArrayList<Candidato>(candidatosPartido);
    }

    public void adicionaCandidato(Candidato candidato){
        // talvez precise verificar se o candidato já está inserido no partido
        
        candidatosPartido.add(candidato);
    }

    //votos nominais so podem ser realizados ao votar em um candidato
    protected void adicionarVotosNominais(int numeroDeVotosNominais){
        votosNominais += numeroDeVotosNominais;
    }

    //essa funcao pode funcionar fora do pacote pois o voto de legenda nao depende de um candidato
    public void adicionarVotosDeLegenda(int numeroDeVotosDeLegenda){
        votosDeLegenda += numeroDeVotosDeLegenda;
    }

    public void calculaTotalVotosPartido(){
        votosValidos = votosDeLegenda + votosNominais;
    }

    public void reordenaListaNoPartido(){
        SistemaEleitoral.reordenaLista(candidatosPartido);
    }

    // calculando quantidade de candidatos eleitos
    public int getCandidatosEleitos(){
        int i = 0;
        for (Candidato candidato : candidatosPartido) {
            if(candidato.foiEleito() == true){
                i++;
            }
        }

        return i;
    }
    
    public String StringCandidatosEleitos(){
        int totalCandidatosEleitos = this.getCandidatosEleitos();
        String quantosEleitos = totalCandidatosEleitos + " ";
        if(totalCandidatosEleitos > 1){
            quantosEleitos += "candidatos eleitos";
        }
        else{
            quantosEleitos += "candidato eleito";
        }

        return quantosEleitos;
    }

    public void imprimeCandidatos(){
        System.out.println(this.getSigla() + ":");
        for (Candidato candidato : candidatosPartido) {
            System.out.println(candidato.getNomeDeUrna());
        }
    }
}
