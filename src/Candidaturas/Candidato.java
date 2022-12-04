
package Candidaturas;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

public class Candidato {
    //Dados para o Candidato

    //Um candidato tem apenas um partido
    private Partido partido;

    private String nomeDeUrna; 
    private LocalDate dataDeNascimento;
    private int codigoDoCargo;
    private int numeroDaFederacao;
    private int numeroDoCandidato;
    private int genero;
    private boolean candidatoLegenda;

    private SituacaoInscricaoCandidato situacaoInsc;

    private SituacaoTotalizacaoCandidato situacaoTot; // define se o candidato foi eleito

    private SituacaoFederacaoCandidato situacaoFed;

    private int totalDeVotos;

    public Candidato(Partido partido, String nomeDeUrna, String dataDeNascimento, int codigoDoCargo,
    int numeroDaFederacao, int numeroDoCandidato, int genero, int situacaoDaTotalizacao, int deferido,
    String destinoVotos) {
        
        partido.adicionaCandidato(this);
        this.partido = partido;

        this.nomeDeUrna = nomeDeUrna;
        this.dataDeNascimento = LocalDate.parse(dataDeNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")); 
        this.codigoDoCargo = codigoDoCargo;
        this.numeroDaFederacao = numeroDaFederacao;
        this.numeroDoCandidato = numeroDoCandidato;
        this.genero = genero;
        this.candidatoLegenda = destinoVotos.equals("Válido (legenda)");
        
        this.situacaoTot = SituacaoTotalizacaoCandidato.verifica(situacaoDaTotalizacao);
        this.situacaoInsc = SituacaoInscricaoCandidato.verificaCodigo(deferido);
        this.situacaoFed = SituacaoFederacaoCandidato.verificaCodigo(numeroDaFederacao);
    }

    //Getters
    public Partido getPartido() {
        return partido;
    }

    public String getNomeDeUrna() {
        return nomeDeUrna;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento.toString();
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

    public SituacaoTotalizacaoCandidato getSituacaoTot() {
        return situacaoTot;
    }

    public SituacaoInscricaoCandidato getSituacaoInsc() {
        return situacaoInsc;
    }

    public SituacaoFederacaoCandidato getSituacaoFed() {
        return situacaoFed;
    }

    public int getTotalDeVotos() {
        return totalDeVotos;
    }

    public boolean isCandidatoLegenda(){
        return candidatoLegenda;
    }

    //deve retornar a idade do candidato na data indicada em sua construção 
    public int getIdade(LocalDate dataEleicao){
        return (int) dataDeNascimento.until(dataEleicao, ChronoUnit.YEARS);
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

}
