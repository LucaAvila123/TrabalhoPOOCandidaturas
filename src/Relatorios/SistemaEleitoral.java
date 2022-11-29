package Relatorios;
import java.util.*;

import Candidaturas.*;
import Estaticos.*;

public class SistemaEleitoral {
    
    // usando a estrutura Map pra poder chegar mais rapidamente aos valores pra serem incrementados
    // uma estrutura genérica para partidos e candidatos, adequada à forma genérica de escolha entre estadual e federal
    private Map<Integer, Partido> PartidosParticipantes;
    private Map<Integer, Candidato> CandidatosParticipantes;

    // enquanto a tabela é usada para calcular os valores, a lista para ordenar a quantidade de votos total
    private List<Candidato> CandidatosMaisVotados;

    private List<Partido> PartidosVotados;

    // número de votos (válidos, nominais e de legenda)
    private int totalDeVotosValidos = 0;
    private int totalDeVotosNominais = 0;
    private int totalDeVotosDeLegenda = 0;

    // isso aqui pode ser calculado na hora de ler o arquivo ou percorrendo a lista (tanto faz)
    private int numeroDeVagas;

    public SistemaEleitoral(){
        this.PartidosParticipantes = new HashMap<>();
        this.CandidatosParticipantes = new HashMap<>();
        this.CandidatosMaisVotados = new ArrayList<>();
        this.PartidosVotados = new ArrayList<>();
    }

    //Getters
    public int getTotalDeVotosValidos() {
        return totalDeVotosValidos;
    }
    public int getTotalVotosLegenda(){
        return totalDeVotosDeLegenda;
    }
    public int getTotalVotosNominais(){
        return totalDeVotosNominais;
    }

    // gera cópias de cada lista usada no código
    public ArrayList<Candidato> CopiaCandidatosMaisVotados(){
        return new ArrayList<>(CandidatosMaisVotados);
    }

    public ArrayList<Partido> CopiaPartidosVotados(){
        return new ArrayList<>(PartidosVotados);
    }

    // funções que retornam número de vagas pra estadual e federal (devo colocar isso em alguma classe no futuro)
    public int getNumeroVagas(){
        return numeroDeVagas;
    }

    public Partido getPartido(int busca){
        return PartidosParticipantes.get(busca);
    }

    //serve para inserir um candidato no sistema e nas listas
    public void cadastraCandidato(int numeroPartido, String nomeDeUrna, String dataDeNascimento, int codigoDoCargo,
        int numeroDaFederacao, int numeroDoCandidato, int genero, int situacaoDaTotalizacao, int deferido,
        String destinoVotos, DataEleicao dataEleicao){

        if(CandidatosParticipantes.containsKey(numeroDoCandidato) == false){
            Partido partidoDoCandidato = PartidosParticipantes.get(numeroPartido);
            Candidato candidato = new Candidato(partidoDoCandidato, nomeDeUrna, dataDeNascimento, 
                codigoDoCargo, numeroDaFederacao,
                numeroDoCandidato, genero, situacaoDaTotalizacao, deferido,
                destinoVotos, dataEleicao);
            // essa hash map usa o número do candidato para busca
            CandidatosParticipantes.put(numeroDoCandidato, candidato); 
                                                            
            // essa lista precisa ser reordenada para bom funcionamento posterior
            CandidatosMaisVotados.add(candidato);

            // insere o candidato na lista de candidatos do partido dele
            partidoDoCandidato.adicionaCandidato(candidato);
        }
}

    // serve para cadastrar um partido no sistema
    // o cadastro dos partidos é pra acontecer antes do dos candidatos
    public void cadastraPartido(int numeroPartido, String siglaPartido){
        if(PartidosParticipantes.containsKey(numeroPartido) == false){
            Partido partido = new Partido(numeroPartido, siglaPartido);
            PartidosParticipantes.put(numeroPartido, partido);
            PartidosVotados.add(partido);
        }
    }

    // procura na tabela hash de candidatos o candidato e incrementa o número de votos colocados
    // acredito que a versão anterior do trabalho estava dando problema por usar isso para os votos de legenda
    public void declararVotosNominais(int numeroDoCandidato, int numeroDeVotos){
        if(CandidatosParticipantes.containsKey(numeroDoCandidato) == true){
            Candidato candidato = CandidatosParticipantes.get(numeroDoCandidato);
            if(candidato.foiDeferido()){
                candidato.adicionarVotos(numeroDeVotos);
            }
            // somando os votos de cada candidato assim
            // if(candidato.destinoVotosLegenda() == true){
                // totalDeVotosDeLegenda += numeroDeVotos;
            // }
            // else{
                // totalDeVotosNominais += numeroDeVotos;
            // }
        } 
    }

    // procura na tabela hash de partidos e incrementa o número de votos de legenda
    public void declaraVotosDeLegenda(int numeroPartido, int numeroDeVotos){
        if(PartidosParticipantes.containsKey(numeroPartido) == true){
            PartidosParticipantes.get(numeroPartido).adicionarVotosDeLegenda(numeroDeVotos);

            // totalDeVotosDeLegenda += numeroDeVotos;
        }
        
    }
    
    // escolhe se o voto vai ser de legenda ou não
    public void declaraVotos(int numeroVotavel, int numeroDeVotos){
        // System.out.println(numeroVotavel);
        boolean ehPartido = PartidosParticipantes.containsKey(numeroVotavel);
        boolean ehCandidato = CandidatosParticipantes.containsKey(numeroVotavel);

        // feito nesse formato para ignorar partidos que recebam votos apenas por federação
        if(ehCandidato){
            if(CandidatosParticipantes.get(numeroVotavel).destinoVotosLegenda()){
                declaraVotosDeLegenda(CandidatosParticipantes.get(numeroVotavel).getPartido().getNumeroDoPartido(), numeroDeVotos);
            } else{
                declararVotosNominais(numeroVotavel, numeroDeVotos);
            }

        }

        else if(ehPartido){
            declaraVotosDeLegenda(numeroVotavel, numeroDeVotos);   
        } 
    }
    
    // cada candidato declarado e cada voto de legenda declarado já está sendo contabilizado
    // talvez esteja evitando acessar a estrutura de dados
    public void calculaVotosTotais(){
        totalDeVotosValidos = totalDeVotosDeLegenda + totalDeVotosNominais;
    }

    // reordena a lista de candidatos de acordo com número de votos
    // função estática para poder manipular as listas de candidatos de cada Partido também
    public static void reordenaLista(List<Candidato> listaCandidatos){
        Collections.sort(listaCandidatos, new MaisVotos());
    }

    // reordena cada lista de candidato de partido e a lista de candidatos mais votados
    public void reordenaTodasListas(){
        
        for (Partido partido : PartidosVotados) {
            partido.reordenaListaNoPartido();
            // partido.imprimeCandidatos();
        }
        SistemaEleitoral.reordenaLista(CandidatosMaisVotados);
        
        // não precisa ser criada uma função específica para reordenar a lista de partidos
        Collections.sort(PartidosVotados, new MaisVotosPartido());     
        this.calculaVotosTotais();
    }

    // calcula o número de vagas da eleicao
    public void calculaQuantidadeEleitos(){
        int i = 0;
        for (Candidato candidato : CandidatosMaisVotados) {
            if(candidato.foiEleito() == true)
                i++;
        }

        numeroDeVagas = i;
    }

    public void imprimeCandidatos(){
        for (Candidato candidato : CandidatosMaisVotados) {
            System.out.println(candidato.getNomeDeUrna());
        }
    }

    // deixando os valores internos todos como 0, somando depois de prontas as listas
    public void somaVotos(){
        int votosNominais = 0;
        int votosLegenda = 0;
        for (Partido partido : PartidosVotados) {
            // atualizando dados dos partidos no foreach
            partido.calculaTotalVotosPartido();
            votosNominais += partido.getVotosNominais();
            votosLegenda += partido.getVotosDeLegenda();
        }

        this.totalDeVotosDeLegenda = votosLegenda;
        this.totalDeVotosNominais = votosNominais;
        this.totalDeVotosValidos = votosLegenda + votosNominais;
    }

}
