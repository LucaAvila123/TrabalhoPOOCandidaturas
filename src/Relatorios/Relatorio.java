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
    // Relatorio 6: votos totalizados por partido e número de candidatos eleitos
    public void imprimeVotacaoDosPartidos(){
        ArrayList <Partido> partidosVotados = sistema.CopiaPartidosVotados();
        int i = 0;
        for (Partido partido : partidosVotados) {
            i++;
            System.out.println(i + " - " + partido.getSigla() + " - " + partido.getNumeroDoPartido() + ", " + this.nf.format(partido.getVotosValidos()) + " votos (" + this.nf.format(partido.getVotosNominais()) + " nominais e " + this.nf.format(partido.getVotosDeLegenda()) + " de legenda), " + partido.StringCandidatosEleitos());
        }
    }

    // Relatorio 7: IGNORADO, por ordens do professor

    // Relatorio 8: primeiro e último colocados de cada partido
    public void imprimePrimeiro_eUltimo(){
        // lógica aqui: a ordem depende do candidato mais votado do partido
        // criando uma nova lista de partidos a partir do candidato mais votado (e só dele)
        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        ArrayList<Partido> partidosTotal = new ArrayList<>();
        
        for (Candidato candidato : candidatosMaisVotados) {
            // vai ignorar partidos que não tenham número positivo de votos
            if(candidato.getTotalDeVotos() > 0 && partidosTotal.contains(candidato.getPartido()) == false){
                partidosTotal.add(candidato.getPartido());
            }
        }

        // depois de toda a lista de candidatos percorrida, fica a lista de partidos para imprimir
        System.out.println("Primeiro e último colocados de cada partido:");
        Partido partido;
        Candidato primeiroColocado;
        Candidato ultimoColocado;

        // candidato posto para colocar o canddidato mais velho em caso de empate
        Candidato intermediarioColocado;
        for(int i = 0; i < partidosTotal.size(); i++){
            partido = partidosTotal.get(i);
            if(partido.getVotosValidos() > 0){
                primeiroColocado = partido.getCandidatosPartido().get(0);
                ultimoColocado = partido.getCandidatosPartido().get(partido.getCandidatosPartido().size() -1);
                intermediarioColocado = ultimoColocado;

                int j = 1;
                // vai subtraindo uma posicao pra cada vez que tiver um valor igual
                while(intermediarioColocado != null && intermediarioColocado.getTotalDeVotos() == ultimoColocado.getTotalDeVotos()){
                    ultimoColocado = intermediarioColocado;
                    j++;
                    intermediarioColocado = partido.getCandidatosPartido().get(partido.getCandidatosPartido().size() - j);
                }
                

                // usando printf para organização (não coloquei nos outros ainda, mas ok)
                System.out.printf("%d - %s - %d, %s (%d, %d votos)", i+1, partido.getSigla(), partido.getNumeroDoPartido(), primeiroColocado.getNomeDeUrna(), primeiroColocado.getNumeroDoCandidato(), this.nf.format(primeiroColocado.getTotalDeVotos()));
                System.out.printf("/ %s (%d, %d votos)\n", ultimoColocado.getNomeDeUrna(), ultimoColocado.getNumeroDoCandidato(), this.nf.format(ultimoColocado.getTotalDeVotos()));
            }
        }
    }
    
    // Relatorio 9: faixas etarias
    public void imprimeFaixasEtarias() throws ParseException{

        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        int menoresDe30 = 0;
        int de30a40 = 0;
        int de40a50 = 0;
        int de50a60 = 0;
        int maioresDe60 = 0;

        for (Candidato candidato : candidatosMaisVotados) {
            if(candidato.getIdade() < 30){
                menoresDe30++;
            }
            else if(candidato.getIdade() < 40){
                de30a40++;
            }
            else if(candidato.getIdade() < 50){
                de40a50++;
            }
            else if(candidato.getIdade() < 60){
                de50a60++;
            }
            else if(candidato.getIdade() >= 60){
                maioresDe60++;
            }
            
        }
        System.out.println("Eleitos por faixa etária (na data da eleição):");
        System.out.println("      Idade < 30: " + menoresDe30 + " (" + this.nf.format((float) 100*menoresDe30/candidatosMaisVotados.size()) + "%)");
        System.out.println("30 <= Idade < 40: " + de30a40 + " (" + this.nf.format((float) 100*de30a40/candidatosMaisVotados.size()) + "%)");
        System.out.println("40 <= Idade < 50: " + de40a50 + " (" + this.nf.format((float) 100*de40a50/candidatosMaisVotados.size()) + "%)");
        System.out.println("50 <= Idade < 60: " + de50a60 + " (" + this.nf.format((float) 100*de50a60/candidatosMaisVotados.size()) + "%)");
        System.out.println("60 <= Idade     : " + maioresDe60 + " (" + this.nf.format((float) 100*maioresDe60/candidatosMaisVotados.size()) + "%)");
     
    }
    
    // Relatorio 10: eleitos por gênero
    public void imprimeGeneros(){
        ArrayList<Candidato> candidatosMaisVotados = sistema.CopiaCandidatosMaisVotados();
        int totalFeminino = 0;
        int totalMasculino = 0;

        for (Candidato candidato : candidatosMaisVotados) {
            // codigo Masculino
            if(candidato.getGenero() == 2){
                totalMasculino++;
            }
            // codigo Feminino
            if(candidato.getGenero() == 4){
                totalFeminino++;
            }
        }
        System.out.println("Eleitos, por gênero:");
        System.out.println("Feminino:  " + totalFeminino + " (" + this.nf.format((float) 100*totalFeminino/candidatosMaisVotados.size()));
        System.out.println("Masculino: " + totalMasculino + " (" + this.nf.format((float) 100*totalMasculino/candidatosMaisVotados.size()));

    }
    
    // Relatório 11: Total de votos válidos, total de votos nominais e total de votos de legenda
    public void imprimeTotalDeVotosDeCadaTipo(){
        System.out.println("Total de votos válidos:      " + sistema.getTotalDeVotosValidos());
        System.out.printf("Total de votos nominais:     %d (%.2f%)\n", sistema.getTotalVotosNominais(), (float) 100*sistema.getTotalVotosNominais()/sistema.getTotalDeVotosValidos());
        System.out.printf("Total de votos de legenda:   %d (%.2f%)\n", sistema.getTotalVotosLegenda() , (float) 100*sistema.getTotalVotosLegenda()/sistema.getTotalDeVotosValidos());
    }
    

}
