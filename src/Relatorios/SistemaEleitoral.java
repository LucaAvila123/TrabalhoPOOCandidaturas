package Relatorios;

import java.time.*;
import java.util.*;
import Candidaturas.*;

public enum SistemaEleitoral {
    INSTANCE;

    private int totalDeVotosValidos;
    private int totalDeVotosDeLegenda;
    private int totalDeVotosNominais;

    private LinkedHashMap<Integer, Partido> partidosParticipantes = new LinkedHashMap<>();
    private LinkedHashMap<Integer, Candidato> candidatosParticipantes = new LinkedHashMap<>();

    // gettters
    public int getTotalDeVotosValidos() {
        return totalDeVotosValidos;
    }
    public int getTotalVotosLegenda(){
        return totalDeVotosDeLegenda;
    }
    public int getTotalVotosNominais(){
        return totalDeVotosNominais;
    }

    public Partido getPartido(int busca){
        return partidosParticipantes.get(busca);
    }

    public Candidato getCandidato(int busca){
        return candidatosParticipantes.get(busca);
    }

    //deve retornar uma lista com todos os candidatos
    public List<Candidato> getCandidatos(LocalDate dataEleicao){
        List<Candidato> candidatos = new ArrayList<>(candidatosParticipantes.values());
        Collections.sort(candidatos, (Candidato a, Candidato b)->
        {
            int ordem = b.getTotalDeVotos() - a.getTotalDeVotos();

            if(ordem == 0){
                ordem = b.getIdade(dataEleicao) - a.getIdade(dataEleicao);
                
                if(ordem == 0){
                    return a.getNumeroDoCandidato() - b.getNumeroDoCandidato();
                }
                return ordem;
            }
            return ordem;
        });
        return candidatos;
    }
    
    //deve retornar uma lista com todos os partidos
    public List<Partido> getPartidos(){
        List<Partido> partidos = new ArrayList<>(partidosParticipantes.values());
        Collections.sort(partidos, (Partido a, Partido b)->
        {
            int ordem = b.getVotosValidos() - a.getVotosValidos();

            if(ordem == 0){
                return a.getNumeroDoPartido() - b.getNumeroDoPartido();
            }

            return ordem;
        });
        return partidos;
    }

    //serve para inserir um candidato no sistema e nas listas
    public void cadastraCandidato(int numeroPartido, String nomeDeUrna, String dataDeNascimento, int codigoDoCargo,
    int numeroDaFederacao, int numeroDoCandidato, int genero, int situacaoDaTotalizacao, int deferido,
    String destinoVotos) throws RuntimeException{

        //O partido do candidato deve existir previamente
        Partido partido = partidosParticipantes.get(numeroPartido);
        if(partido == null){
            throw new RuntimeException("Partido de numero " + numeroPartido + " nao encontrado");
        }
        // só vai incluir os deferidos na lista
        if(SituacaoInscricaoCandidato.verificaCodigo(deferido) == SituacaoInscricaoCandidato.DEFERIDO || destinoVotos.equals("Válido (legenda)")){
            Candidato candidato = new Candidato(partido, 
            nomeDeUrna, dataDeNascimento, codigoDoCargo, numeroDaFederacao,
            numeroDoCandidato, genero, situacaoDaTotalizacao, deferido,
            destinoVotos);

            //Assumindo que cada candidato vai ter um numero unico
            candidatosParticipantes.putIfAbsent(numeroDoCandidato, candidato);
            
            partido.adicionaCandidato(candidato);
        }
    }
    
    //Retorna o partido cadastrado mais antigo ou o proprio partido
    public Partido cadastraPartido(int numeroPartido, String siglaPartido) throws RuntimeException {
        //So adiciona o partido se ele nao foi adicionado ainda
        Partido partido = new Partido(numeroPartido, siglaPartido);
        Partido retorno = partidosParticipantes.putIfAbsent(numeroPartido, partido);
        
        if(retorno != null){
            return retorno; 
        }else{
            return partido;
        }
    }

    // procura na tabela hash de candidatos o candidato e incrementa o número de votos colocados
    // acredito que a versão anterior do trabalho estava dando problema por usar isso para os votos de legenda
    public void declaraVotosNominais(int numeroDoCandidato, int numeroDeVotos){

        Candidato candidato = candidatosParticipantes.get(numeroDoCandidato);
        if(candidato != null){
            candidato.adicionarVotos(numeroDeVotos);
            // somando os votos de cada candidato assim
            if(candidato.isCandidatoLegenda()){
                totalDeVotosDeLegenda += numeroDeVotos;
            }
            else{
                totalDeVotosNominais += numeroDeVotos;
            }
        }
        totalDeVotosValidos = totalDeVotosDeLegenda + totalDeVotosNominais;
    }

    // procura na tabela hash de partidos e incrementa o número de votos de legenda
    public void declaraVotosDeLegenda(int numeroPartido, int numeroDeVotos){
        
        Partido partido = partidosParticipantes.get(numeroPartido);
        
        if(partido != null){
        
            // somando os votos de legenda assim
            // daria para acessar a lista e assim calcular o total de votos
            partido.adicionarVotosDeLegenda(numeroDeVotos);
            totalDeVotosDeLegenda += numeroDeVotos;
        }
        totalDeVotosValidos = totalDeVotosDeLegenda + totalDeVotosNominais;
    }

}
