package Candidaturas;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import Estaticos.*;


public class Candidato {
    //Dados para o Candidato
    private Partido partido;
    
    private String nomeDeUrna; 
    private String dataDeNascimento;

    private int codigoDoCargo;
    private int numeroDaFederacao;
    private int numeroDoCandidato;
    private int genero; 
    private int deferido;
    private int situacaoDaTotalizacao; // define se o candidato foi eleito 
    private String destinoVotos; // fala se o candidato dá votos de legenda
    private LocalDate dataEleicao;
    private int totalDeVotos = 0; //valor default
    
    //Construtor
    public Candidato(Partido partido, String nomeDeUrna, String dataDeNascimento, int codigoDoCargo,
            int numeroDaFederacao, int numeroDoCandidato, int genero, int situacaoDaTotalizacao, int deferido,
            String destinoVotos, LocalDate dataEleicao) {
        this.partido = partido;
        this.nomeDeUrna = nomeDeUrna;
        this.dataDeNascimento = dataDeNascimento; 
        this.codigoDoCargo = codigoDoCargo;
        this.numeroDaFederacao = numeroDaFederacao;
        this.numeroDoCandidato = numeroDoCandidato;
        this.genero = genero; 
        this.situacaoDaTotalizacao = situacaoDaTotalizacao;
        this.deferido = deferido;
        this.destinoVotos = destinoVotos;
        this.dataEleicao = dataEleicao;
    }

    //Getters
    public Partido getPartido() {
        return partido;
    }

    public String getNomeDeUrna() {
        return nomeDeUrna;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public int getCodigoDoCargo() {
        return codigoDoCargo;
    }

    public int getNumeroDaFederacao() {
        return numeroDaFederacao;
    }

    public int getNumeroDoCandidato() {
        return numeroDoCandidato;
    }

    public int getGenero() {
        return genero;
    }
    
    // foi deferido
    public boolean foiDeferido(){
        return Deferido.igualDeferido(deferido);
    }

    // vai pra legenda
    public boolean destinoVotosLegenda(){
        return Legenda.vaiPraLegenda(destinoVotos);
    }
    
    // os números 2  e 16 na situação de totalização dizem se o candidato foi eleito
    public boolean foiEleito(){
        return Eleito.igualEleito(this.situacaoDaTotalizacao);
    }

    // o número -1 indica que o candidato não participa de federação
    public boolean ehDeFederacao(){
        return Federado.ehFederado(numeroDaFederacao);
    }

    public int getTotalDeVotos() {
        return totalDeVotos;
    }

    //deve retornar a idade do candidato na data indicada em sua construção 
    public int getIdade(){
        LocalDate diaNascimento = LocalDate.parse(dataDeNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return (int) diaNascimento.until(dataEleicao, ChronoUnit.YEARS);

    }

    //serve para incrementar os votos do candidato
    public void adicionarVotos(int numeroDeVotos){
        totalDeVotos += numeroDeVotos;
        this.partido.adicionarVotosNominais(numeroDeVotos);
    }

    @Override
    public String toString() {
        return this.nomeDeUrna + " " + this.numeroDoCandidato + " "; 
    }

    
    public int compareTo(Candidato o) {
        if (this.totalDeVotos < o.totalDeVotos) {
            return -1;
        }
        if (this.totalDeVotos > o.totalDeVotos) {
            return 1;
        }
        return 0;
    }
}
