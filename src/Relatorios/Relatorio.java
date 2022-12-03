package Relatorios;
import Candidaturas.*;
import Estaticos.*;

import java.text.*;
import java.time.*;
import java.util.*;

public class Relatorio {
    
    private Locale localeBR;
    private NumberFormat nf; 
    private DecimalFormat df;
    private SistemaEleitoral sistema;
    private LocalDate dataEleicao;
    private int numeroDeVagas;

    private List<Candidato> candidatos;
    private List<Partido> partidos;
    // referente aos relatórios para eleição estadual ou federal
    private String modo;

    // colocando as especificidades de local em um objeto específico
    // esse mesmo objeto irá imprimir os relatórios
    public Relatorio(SistemaEleitoral sistema, String modo, LocalDate dataEleicao){
        this.localeBR = new Locale("pt", "BR");
        this.nf = NumberFormat.getInstance(this.localeBR);
        this.df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(this.localeBR));
        this.df.setMinimumFractionDigits(2);
        this.df.setMaximumFractionDigits(2);
        this.sistema = sistema;
        this.modo = modo;
        this.dataEleicao = dataEleicao;
        this.candidatos = sistema.getCandidatos(dataEleicao);
        this.partidos = sistema.getPartidos();
        
    }
    
    public void imprimeCadastrados(SistemaEleitoral sistema){
        for (Candidato candidato : candidatos) {
            System.out.format(localeBR, candidato.toString() + "\n");
            
        }
    }
    
    // Relatorio 1: imprime o número de vagas
    public void primeiro(){
        this.numeroDeVagas = 0;
        for (Candidato candidato : candidatos) {
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.ELEITO){
                this.numeroDeVagas++;
            }
        }
        System.out.println("Número de vagas: " + nf.format(this.numeroDeVagas));
        System.out.print("\n");
    }

    // Relatorio 2: imprime os candidatos eleitos
    public void segundo(){
        if(modo.equals("--federal")){
            System.out.println("Deputados federais eleitos:");
        }
        if(modo.equals("--estadual")){
            System.out.println("Deputados estaduais eleitos:");
        }

        int i = 0;
        String impressao;
        for (Candidato candidato : candidatos) {
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.ELEITO){
                i++;
                // verificando se o candidato participa de federação
                impressao = i + " - ";

                if(candidato.getSituacaoFed() == SituacaoFederacaoCandidato.FEDERADO){
                    impressao += "*";
                }

                impressao += candidato.getNomeDeUrna() + " (" + candidato.getPartido().getSigla() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)";
                System.out.println(impressao);
            }
        }

        System.out.print("\n");
        
    }

    // Relatorio 3: imprime candidatos mais votados respeitando número de vagas
    public void terceiro(){
        // irei supor aqui que os relatórios só serão feitos depois de a lista de candidatos já ter sido ordenada
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        String impressao;
        // esse formato tinha dado problema antes por algum motivo na hora de contabilizar
        for(int i = 0; i < this.numeroDeVagas; i++){
            Candidato candidato = candidatos.get(i);
            impressao = (i+1) + " - ";
            
            if(candidato.getSituacaoFed() == SituacaoFederacaoCandidato.FEDERADO){
                impressao += "*";
            }
            impressao += candidato.getNomeDeUrna() + " (" + candidato.getPartido().getSigla() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)";
            System.out.println(impressao);
        }

        System.out.print("\n");
    }

    // Relatorio 4: candidatos prejudicados pelo sistema proporcional
    public void quarto(){
        
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");

        String impressao;

        List<Candidato> candidatosMaisVotados = sistema.getCandidatos(dataEleicao);
        Candidato candidato;

        for(int i = 0; i < this.numeroDeVagas; i++){
            candidato = candidatosMaisVotados.get(i);
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.NAO_ELEITO){
                impressao = (i+1) + " - ";
                if(candidato.getSituacaoFed() == SituacaoFederacaoCandidato.FEDERADO)
                    impressao += "*";
                
                impressao += candidato.getNomeDeUrna() + " (" + candidato.getPartido().getSigla() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)";
                System.out.println(impressao);
            }
        }

        System.out.print("\n");
    }
    
    // Relatorio 5: candidatos beneficiados pelo sistema proporcional
    public void quinto(){
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");

        String impressao;
        Candidato candidato;
        
        // vai imprimir pra valores de i acima do número de vagas e até o número de eleitos ter o número de vagas
        for(int i = 0, eleitos = 0; eleitos < this.numeroDeVagas; i++){
            candidato = candidatos.get(i);
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.ELEITO){
                eleitos++;
                if(i >= this.numeroDeVagas){
                    impressao = (i+1) + " - ";
                    if(candidato.getSituacaoFed() == SituacaoFederacaoCandidato.FEDERADO)
                        impressao += "*";
                    
                    impressao += candidato.getNomeDeUrna() + " (" + candidato.getPartido().getSigla() + ", " + this.nf.format(candidato.getTotalDeVotos())+ " votos)";
                    System.out.println(impressao);
                }
            }
        }

        System.out.print("\n");
        
    }
    // Relatorio 6: votos totalizados por partido e número de candidatos eleitos
    public void sexto(){
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        String impressao;
        int i = 0;
        for (Partido partido : partidos) {
            // partido.calculaTotalVotosPartido();
            i++;
            impressao = i + " - " + partido.getSigla() + " - " + partido.getNumeroDoPartido() + ", " + this.nf.format(partido.getVotosValidos());
            if(partido.getVotosValidos() > 1)
                impressao += " votos";
            else
                impressao += " voto";
            
            impressao += " (" + this.nf.format(partido.getVotosNominais());
            
            if(partido.getVotosNominais() > 1)
                impressao += " nominais";
            else
                impressao += " nominal";

            impressao += " e " + this.nf.format(partido.getVotosDeLegenda()) + " de legenda), ";
            
            int totalEleitos = partido.getQtdEleitos();
            
            if(totalEleitos > 1){
                impressao += nf.format(totalEleitos) + " candidatos eleitos";
            } else{
                impressao += nf.format(totalEleitos) + " candidato eleito";
            }
            System.out.println(impressao);
        }

        System.out.print("\n");
    }

    // Relatorio 7: IGNORADO, por ordens do professor

    // Relatorio 8: primeiro e último colocados de cada partido
    public void oitavo(){
        // lógica aqui: a ordem depende do candidato mais votado do partido
        // criando uma nova lista de partidos a partir do candidato mais votado (e só dele)
        List<Partido> partidosTotal = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            // vai ignorar partidos que não tenham número positivo de votos
            if(candidato.getTotalDeVotos() > 0 && partidosTotal.contains(candidato.getPartido()) == false){
                partidosTotal.add(candidato.getPartido());
            }
        }
       
        System.out.println("Primeiro e último colocados de cada partido:");
        Partido partido;
        Candidato primeiroColocado;
        Candidato ultimoColocado;
        
        // candidato posto para colocar o candidato mais velho em caso de empate
        Candidato intermediarioColocado;
        String impressao;
        for(int i = 0; i < partidosTotal.size(); i++){
            partido = partidosTotal.get(i);
            if(partido.getVotosValidos() > 0){ 
                List<Candidato> candidatos = partido.getCandidatosPartido(dataEleicao);
                primeiroColocado = candidatos.get(0);
                ultimoColocado = candidatos.get(candidatos.size() -1);
                
                // tratando o caso de só haver uma candidatura do partido 
               /* if(primeiroColocado != ultimoColocado){

                    // não incluir candidatos com 0 votos na contagem
                    int j = 1;
                    while(ultimoColocado.getTotalDeVotos() == 0){
                        ultimoColocado = candidatos.get(candidatos.size() - j);
                        j++;
                    }
                    intermediarioColocado = ultimoColocado;

                    // quantos candidatos ainda serão analisados
                    int tamanhoRestante = candidatos.size() - j;

                    while(tamanhoRestante > j && intermediarioColocado.getTotalDeVotos() == ultimoColocado.getTotalDeVotos()){
                        ultimoColocado = intermediarioColocado;
                        j++;
                        intermediarioColocado = candidatos.get(candidatos.size() - j);
                    }
                }
                */
                impressao = (i+1) + " - " + partido.getSigla() +
                    " - " + partido.getNumeroDoPartido()
                    + ", " + primeiroColocado.getNomeDeUrna() 
                    + " (" + primeiroColocado.getNumeroDoCandidato() + ", " 
                    + this.nf.format(primeiroColocado.getTotalDeVotos());

                if(primeiroColocado.getTotalDeVotos() > 1){
                    impressao += " votos)"; 
                } else{
                    impressao += " voto)";
                }

                impressao += " / " + ultimoColocado.getNomeDeUrna() 
                    + " (" + ultimoColocado.getNumeroDoCandidato() + ", " 
                    + this.nf.format(ultimoColocado.getTotalDeVotos());
                if(ultimoColocado.getTotalDeVotos() > 1){
                    impressao += " votos)";
                } else{
                    impressao += " voto)";
                }
                
                System.out.println(impressao);
            }
        }

        System.out.print("\n");
    }
    
    // Relatorio 9: faixas etarias
    public void nono(){

        List<Candidato> candidatosMaisVotados = sistema.getCandidatos(dataEleicao);
        int menoresDe30 = 0;
        int de30a40 = 0;
        int de40a50 = 0;
        int de50a60 = 0;
        int maioresDe60 = 0;

        for (Candidato candidato : candidatosMaisVotados) {
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.ELEITO){
                if(candidato.getIdade(this.dataEleicao) < 30){
                    menoresDe30++;
                }
                else if(candidato.getIdade(this.dataEleicao) < 40){
                    de30a40++;
                }
                else if(candidato.getIdade(this.dataEleicao) < 50){
                    de40a50++;
                }
                else if(candidato.getIdade(this.dataEleicao) < 60){
                    de50a60++;
                }
                else if(candidato.getIdade(this.dataEleicao) >= 60){
                    maioresDe60++;
                }
            }
            
        }
        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        System.out.println("      Idade < 30: " + this.nf.format(menoresDe30) + " (" + this.df.format((float) 100*menoresDe30/this.numeroDeVagas) + "%)");
        System.out.println("30 <= Idade < 40: " + this.nf.format(de30a40) + " (" + this.df.format((float) 100*de30a40/this.numeroDeVagas) + "%)");
        System.out.println("40 <= Idade < 50: " + this.nf.format(de40a50) + " (" + this.df.format((float) 100*de40a50/this.numeroDeVagas) + "%)");
        System.out.println("50 <= Idade < 60: " + this.nf.format(de50a60) + " (" + this.df.format((float) 100*de50a60/this.numeroDeVagas) + "%)");
        System.out.println("60 <= Idade     : " + this.nf.format(maioresDe60) + " (" + this.df.format((float) 100*maioresDe60/this.numeroDeVagas) + "%)");
        
        System.out.print("\n");
    }
    
    // Relatorio 10: eleitos por gênero
    public void decimo(){
        List<Candidato> candidatosMaisVotados = sistema.getCandidatos(dataEleicao);
        int totalFeminino = 0;
        int totalMasculino = 0;

        for (Candidato candidato : candidatosMaisVotados) {
            if(candidato.getSituacaoTot() == SituacaoTotalizacaoCandidato.ELEITO){
                // codigo Masculino
                if(candidato.getGenero() == Genero.MASCULINO.getGenero()){
                    totalMasculino++;
                }
                // codigo Feminino
                if(candidato.getGenero() == Genero.FEMININO.getGenero()){
                    totalFeminino++;
                }
            }
        }
        System.out.println("Eleitos, por gênero:");
        System.out.println("Feminino:  " + this.nf.format(totalFeminino) + " (" + this.df.format((float) 100*totalFeminino/this.numeroDeVagas) + "%)");
        System.out.println("Masculino: " + this.nf.format(totalMasculino) + " (" + this.df.format((float) 100*totalMasculino/this.numeroDeVagas) + "%)");

        System.out.print("\n");
    }
    
    // Relatório 11: Total de votos válidos, total de votos nominais e total de votos de legenda
    public void decimoPrimeiro(){
        System.out.println("Total de votos válidos:    " + this.nf.format(sistema.getTotalDeVotosValidos()));
        System.out.println("Total de votos nominais:   " + this.nf.format(sistema.getTotalVotosNominais()) + " (" + this.df.format((float) 100*sistema.getTotalVotosNominais()/sistema.getTotalDeVotosValidos()) + "%)");
        System.out.println("Total de votos de legenda: " + this.nf.format(sistema.getTotalVotosLegenda()) + " (" + this.df.format((float) 100*sistema.getTotalVotosLegenda()/sistema.getTotalDeVotosValidos()) + "%)");
        System.out.print("\n");
    }
    

}
