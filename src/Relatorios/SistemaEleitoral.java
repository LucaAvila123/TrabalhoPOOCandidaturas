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
        String destinoVotos){
        if(CandidatosParticipantes.containsKey(numeroDoCandidato) == false){
            // essa hash map usa o número do candidato para busca
            CandidatosParticipantes.put(numeroDoCandidato, new Candidato(PartidosParticipantes.get(numeroPartido), 
                                                            nomeDeUrna, dataDeNascimento, codigoDoCargo, numeroDaFederacao,
                                                            numeroDoCandidato, genero, situacaoDaTotalizacao, deferido,
                                                            destinoVotos));
            // essa lista precisa ser reordenada para bom funcionamento posterior
            CandidatosMaisVotados.add(CandidatosParticipantes.get(numeroDoCandidato));
            
            PartidosParticipantes.get(numeroPartido).adicionaCandidato(CandidatosParticipantes.get(numeroDoCandidato));
        }
    }

    // serve para cadastrar um partido no sistema
    // o cadastro dos partidos é pra acontecer antes do dos candidatos
    public void cadastraPartido(int numeroPartido, String siglaPartido){
        if(PartidosParticipantes.containsKey(numeroPartido) == false){
            PartidosParticipantes.put(numeroPartido, new Partido(numeroPartido, siglaPartido));
            PartidosVotados.add(PartidosParticipantes.get(numeroPartido));
        }
    }

    // procura na tabela hash de candidatos o candidato e incrementa o número de votos colocados
    // acredito que a versão anterior do trabalho estava dando problema por usar isso para os votos de legenda
    public void declararVotosNominais(int numeroDoCandidato, int numeroDeVotos){
        if(CandidatosParticipantes.containsKey(numeroDoCandidato) == true){
            Candidato candidato = CandidatosParticipantes.get(numeroDoCandidato);
            candidato.adicionarVotos(numeroDeVotos);
            // somando os votos de cada candidato assim
            if(candidato.destinoVotosLegenda() == true){
                totalDeVotosDeLegenda += numeroDeVotos;
            }
            else{
                totalDeVotosNominais += numeroDeVotos;
            }
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
    
    // escolhe se o voto vai ser de legenda ou não
    public void declaraVotos(int numeroVotavel, int numeroDeVotos){
        if(PartidosParticipantes.containsKey(numeroVotavel) == true){
            declaraVotosDeLegenda(numeroVotavel, numeroDeVotos);
        }
        else{
            declararVotosNominais(numeroVotavel, numeroDeVotos);
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
