package Candidaturas;
import java.text.*;
import java.time.*;
import java.time.temporal.*;

import Estaticos.DataEleicao;
import Estaticos.Eleito;

public class Candidato {
    //Dados para o Candidato
    private Partido partido;
    
    private String nomeDeUrna; 
    private String dataDeNascimento;

    private int codigoDoCargo;
    private int numeroDaFederacao;
    private int numeroDoCandidato;
    private int genero; 
    private int situacaoDaTotalizacao; // define se o candidato foi eleito 

    private int totalDeVotos = 0; //valor default
    
    //Construtor
    public Candidato(Partido partido, String nomeDeUrna, String dataDeNascimento, int codigoDoCargo,
            int numeroDaFederacao, int numeroDoCandidato, int genero, int situacaoDaTotalizacao) {
        this.partido = partido;
        this.nomeDeUrna = nomeDeUrna;
        this.dataDeNascimento = dataDeNascimento; 
        this.codigoDoCargo = codigoDoCargo;
        this.numeroDaFederacao = numeroDaFederacao;
        this.numeroDoCandidato = numeroDoCandidato;
        this.genero = genero; 
        this.situacaoDaTotalizacao = situacaoDaTotalizacao;
        
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
    
    
    
    // os números 2  e 16 na situação de totalização dizem se o candidato foi eleito
    public boolean foiEleito(){
        return Eleito.igualEleito(this.situacaoDaTotalizacao);
    }

    // o número -1 indica que o candidato não participa de federação
    public boolean ehDeFederacao(){
        return Eleito.ehFederado(numeroDaFederacao);
    }

    public int getTotalDeVotos() {
        return totalDeVotos;
    }

    //deve retornar a idade do candidato na data indicada em sua construção 
    public int getIdade() throws ParseException{

        // talvez conste usar a data da eleição descrita no CSV pra fazer isso aqui
        String manipulandoData[] = dataDeNascimento.split("/");
        int diaNascimento = Integer.parseInt(manipulandoData[0]);
        int mesNascimento = Integer.parseInt(manipulandoData[1]);
        int anoNascimento = Integer.parseInt(manipulandoData[2]);
        LocalDateTime dataCandidato = LocalDateTime.of(anoNascimento, mesNascimento, diaNascimento, 0, 0, 0);
        // a data da eleição de 2022 foi dia 02/10/2022; deixando as constantes em outro arquivo
        LocalDateTime hoje = LocalDateTime.of(DataEleicao.ANO.getNumero(), DataEleicao.MES.getNumero(), DataEleicao.DIA.getNumero(), 0, 0, 0);

        //calcula diferença
        int anos = (int) dataCandidato.until(hoje, ChronoUnit.YEARS);
        return anos;
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
