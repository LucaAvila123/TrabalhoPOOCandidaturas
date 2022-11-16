package Relatorios;
import java.util.*;

import Candidaturas.*;

public class SistemaEleitoral {
    
    // usando a estrutura Map pra poder chegar mais rapidamente aos valores pra serem incrementados
    // uma estrutura genérica para partidos e candidatos, adequada à forma genérica de escolha entre estadual e federal
    private Map<Integer, Partido> PartidosParticipantes;
    private Map<Integer, Candidato> CandidatosParticipantes;

    // enquanto a tabela é usada para calcular os valores, a lista para ordenar a quantidade de votos total
    private List<Candidato> CandidatosMaisVotados;

    // acho que essa lista aqui é dispensável, é bom repensar
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
    public void cadastraCandidato(Candidato candidato){
        if(CandidatosParticipantes.containsKey(candidato.getNumeroDoCandidato()) == false){
            // essa hash map usa o número do candidato para busca
            CandidatosParticipantes.put(candidato.getNumeroDoCandidato(), candidato);
            // essa lista precisa ser reordenada para bom funcionamento posterior
            CandidatosMaisVotados.add(candidato);
            
            // se o partido ainda não está na tabela
            if(PartidosParticipantes.containsKey(candidato.getPartido().getNumeroDoPartido()) == false){
                PartidosParticipantes.put(candidato.getPartido().getNumeroDoPartido(), candidato.getPartido());
            }

            candidato.getPartido().adicionaCandidato(candidato);
        }
    }

    // serve para cadastrar um partido no sistema
    // não acredito que seja necessário porque os votos de legenda precisam de alguma candidatura para serem contabilizados
    /*public void cadastraPartido(Partido partido){
        PartidosParticipantes.put(partido.getNumeroDoPartido(), partido);
    }*/

    // procura na tabela hash de candidatos o candidato e incrementa o número de votos colocados
    // acredito que a versão anterior do trabalho estava dando problema por usar isso para os votos de legenda
    public void declararVotosNominais(int numeroDoCandidato, int numeroDeVotos){
        if(CandidatosParticipantes.containsKey(numeroDoCandidato) == true){
            CandidatosParticipantes.get(numeroDoCandidato).adicionarVotos(numeroDeVotos);
            
            // somando os votos de cada candidato assim
            totalDeVotosNominais += numeroDeVotos;
        } 
    }

    // procura na tabela hash de partidos e incrementa o número de votos de legenda
    public void declaraVotosDeLegenda(int numeroPartido, int numeroDeVotos){
        if(PartidosParticipantes.containsKey(numeroPartido) == true){
            PartidosParticipantes.get(numeroPartido).adicionarVotosDeLegenda(numeroDeVotos);

            // somando os votos de legenda assim
            // daria para acessar a lista e assim calcular o total de votos
            totalDeVotosDeLegenda += numeroDeVotos;
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
        }
        SistemaEleitoral.reordenaLista(CandidatosMaisVotados);

        // não precisa ser criada uma função específica para reordenar a lista de partidos
        Collections.sort(PartidosVotados, new MaisVotosPartido());
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

    

}
