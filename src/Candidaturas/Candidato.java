package Candidaturas;
import java.text.*;
import java.util.*;

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
        this.codigoDoCargo = codigoDoCargo; // determina se é estadual (7) ou federal (6)
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
    
    // os números 2  e 3 na situação de totalização dizem se o candidato foi eleito
    public boolean foiEleito(){
        return this.situacaoDaTotalizacao == 2 || this.situacaoDaTotalizacao == 3;
    }

    // o número -1 indica que o candidato não participa de federação
    public boolean ehDeFederacao(){
        return this.getNumeroDaFederacao() != -1;
    }

    public int getTotalDeVotos() {
        return totalDeVotos;
    }

    //nao sei classificar essas funcoes


    //deve retornar a idade do candidato na data indicada em sua construção 
    public long getIdade() throws ParseException{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data = formato.parse(dataDeNascimento);

        // só para comparações, não terá calculada a idade de fato
        return data.getTime();
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
