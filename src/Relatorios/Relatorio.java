package Relatorios;
import Candidaturas.*;

import java.text.*;
import java.util.*;

public class Relatorio {
    
    private Locale localeBR;
    private NumberFormat nf; 
    private SistemaEleitoral sistema;
    
    // referente aos relatórios para eleição estadual ou federal
    private String modo;

    // colocando as especificidades de local em um objeto específico
    // esse mesmo objeto irá imprimir os relatórios
    public Relatorio(SistemaEleitoral sistema, String modo){
        this.localeBR = new Locale("pt", "BR");
        this.nf = NumberFormat.getInstance(this.localeBR);
        this.sistema = sistema;
        this.modo = modo;
    }
    

    // Relatorio 1: imprime o número de vagas
    public void imprimeNumeroVagas(){
        System.out.println("Número de vagas: " + sistema.getNumeroVagas());
    }

    // Relatorio 2: imprime os candidatos eleitos
    public void imprimeCandidatosEleitos(){
        if(modo.equals("federal")){
            System.out.println("Deputados federais eleitos:");
        }
        if(modo.equals("estadual")){
            System.out.println("Deputados estaduais eleitos");
        }

        int i = 0;
        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
          
        for (Candidato candidato : candidatosMaisVotados) {
            if(candidato.foiEleito() == true){
                i++;
                // verificando se o candidato participa de federação
                if(candidato.ehDeFederacao() == true){
                    System.out.print("*");
                }
                System.out.println(i + " - " + candidato.getNomeDeUrna() + " (" + candidato.getPartido() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)");
            }
        }

        
    }

    // Relatorio 3: imprime candidatos mais votados respeitando número de vagas
    public void imprimeCandidatosMaisVotados(){
        // irei supor aqui que os relatórios só serão feitos depois de a lista de candidatos já ter sido ordenada
        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        
        // esse formato tinha dado problema antes por algum motivo na hora de contabilizar
        for(int i = 0; i < sistema.getNumeroVagas(); i++){
            Candidato candidato = candidatosMaisVotados.get(i);
            if(candidato.ehDeFederacao() == true){
                System.out.print("*");
            }
            System.out.println((i+1) + candidato.getNomeDeUrna() + " (" + candidato.getPartido() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)");
        }
    }

    // Relatorio 4: candidatos prejudicados pelo sistema proporcional
    public void imprimeCandidatosBemVotadosNaoEleitos(){
        
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");

        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        Candidato candidato;

        for(int i = 0; i < sistema.getNumeroVagas(); i++){
            candidato = candidatosMaisVotados.get(i);
            if(candidato.foiEleito() == false){
                if(candidato.ehDeFederacao() == true)
                    System.out.println("*");
                
                System.out.println((i+1) + candidato.getNomeDeUrna() + " (" + candidato.getPartido() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)");
            }
        }
    }
    
    // Relatorio 5: candidatos beneficiados pelo sistema proporcional
    public void imprimeCandidatosMalVotadosEleitos(){
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("com sua posição no ranking de mais votados)");

        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        Candidato candidato;
        
        // vai imprimir pra valores de i acima do número de vagas e até o número de eleitos ter o número de vagas
        for(int i = 0, eleitos = 0; eleitos < sistema.getNumeroVagas(); i++){
            candidato = candidatosMaisVotados.get(i);
            if(candidato.foiEleito() == true){
                eleitos++;
            }
            if(i >= sistema.getNumeroVagas()){
                if(candidato.ehDeFederacao() == true)
                    System.out.println("*");
                
                System.out.println((i+1) + candidato.getNomeDeUrna() + " (" + candidato.getPartido() + ", " + this.nf.format(candidato.getTotalDeVotos()) + " votos)");
            }
        }
        
    }
    // Relatorio 6: 

    // Relatorio 7: IGNORADO, por ordens do professor

    // Relatorio 8
    
    // Relatorio 9
    
    // Relatorio 10
    
    // Relatório 11: Total de votos válidos, total de votos nominais e total de votos de legenda
    public void imprimeTotalDeVotosDeCadaTipo(){
        System.out.println("Total de votos válidos:      " + sistema.getTotalDeVotosValidos());
        System.out.printf("Total de votos nominais:     %d (%.2f%)\n", sistema.getTotalVotosNominais(), (float) 100*sistema.getTotalVotosNominais()/sistema.getTotalDeVotosValidos());
        System.out.printf("Total de votos de legenda:   %d (%.2f%)\n", sistema.getTotalVotosLegenda() , (float) 100*sistema.getTotalVotosLegenda()/sistema.getTotalDeVotosValidos());
    }
    

}
