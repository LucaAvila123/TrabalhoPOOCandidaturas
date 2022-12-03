import Relatorios.*;
import Estaticos.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Candidaturas.Candidato;


public class App {
    public static void main(String[] args) throws Exception {
        // PRIMEIRA PARTE: lidando com as entradas no terminal
        // unzip <arquivo>.zip
        // ant compile
        // ant jar
        // java -jar deputados.jar <opção_de_cargo> <caminho_arquivo_candidatos> <caminho_arquivo_votacao> <data>
        
        // java -jar deputados.jar --estadual consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022
        // java -jar deputados.jar --federal consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022

        // o termo 0 é referente ao tipo da eleição
        // o termo 1 é o arquivo de candidaturas
        // o termo 2 é o arquivo da votação 
        // o termo 3 é a data da votação (é o parâmetro usado)

        // decidindo se serão lidos apenas federal ou estadual
        // a string será ou "--federal" ou "--estadual"
        
        String tipoEleicao = null;
        String arquivoCandidaturas = null;
        String arquivoVotacao = null;
        String dataEleicao = null;

        //MODIFICACAO coloquei um enum aqui
        CodigoCargo cargoAtual;

        try{
            tipoEleicao = args[0];
            arquivoCandidaturas = args[1];
            arquivoVotacao = args[2];
            dataEleicao = args[3];
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("O formato da linha de comando deve ser o feito a seguir");
            System.out.println("java -jar deputados.jar <opção_de_cargo> <caminho_arquivo_candidatos> <caminho_arquivo_votacao> <data>");
        }

        //salvando o cargo atual do programa
        if(tipoEleicao.equals("--estadual")){
            cargoAtual = CodigoCargo.ESTADUAL;
        }else if (tipoEleicao.equals("--federal")){
            cargoAtual = CodigoCargo.FEDERAL;
        }else{
            //MODIFICACAO: RUNTIME EXCEPTION AQUI....
            throw new RuntimeException("Coloque --federal ou --estadual no campo <opção_de_cargo>");
        }
        
        // isso daqui é só para inicializar o dia da votação
        // só será usado para setar valoresCandidatos a serem usados no objeto Candidato
        LocalDate diaVotacao = LocalDate.parse(dataEleicao, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // SEGUNDA PARTE: leitura dos arquivos de candidatos

        //instanciando o sistema eleitoral
        SistemaEleitoral sistema = SistemaEleitoral.INSTANCE;

        //Gerenciadoes das linhas do arquivo
        CsvReader arquivoCandidatos = new CsvReader(arquivoCandidaturas, ";", "ISO-8859-1", true);
        CsvReader arquivoVotacoes = new CsvReader(arquivoVotacao, ";", "ISO-8859-1", true);
        
        //valores que serao selecionados do arquivo
        String[] valuesCandidatos = {
            /*0*/"CD_CARGO", 
            /*1*/"CD_SITUACAO_CANDIDATO_TOT", 
            /*2*/"NR_CANDIDATO", 
            /*3*/"NM_URNA_CANDIDATO", 
            /*4*/"NR_PARTIDO", 
            /*5*/"SG_PARTIDO", 
            /*6*/"NR_FEDERACAO", 
            /*7*/"DT_NASCIMENTO", 
            /*8*/"CD_SIT_TOT_TURNO", 
            /*9*/"CD_GENERO", 
            /*10*/"NM_TIPO_DESTINACAO_VOTOS"
        };
        //adiciona aspas para cada elemento
        quote(valuesCandidatos);

        String[] valuesVotacoes = {
            /*0*/"CD_CARGO", 
            /*1*/"NR_VOTAVEL", 
            /*2*/"QT_VOTOS"
        };
        //adiciona aspas para cada elemento
        quote(valuesVotacoes);

        // int linha = 0;
        //inicializar os candidatos
        while(arquivoCandidatos.hasNextValues()){
            // linha++;
            //passando para array pra ficar mais facil e tirando as aspas dos valores
            List<String> valoresCandidatos = arquivoCandidatos.nextValues(valuesCandidatos);
            //dados do partido
            int numeroPartido = Integer.parseInt(unquote(valoresCandidatos.get(4)));
            String siglaPartido = unquote(valoresCandidatos.get(5));
            
            //cadastrando no sistema    
            // essa função precisa ficar aqui pro caso de partidos com 0 votos
            if(sistema.getPartido(numeroPartido) == null){
                sistema.cadastraPartido(numeroPartido, siglaPartido);
            }
            //dados do candidato
            String nomeDeUrna = unquote(valoresCandidatos.get(3));
            String dataDeNascimento = unquote(valoresCandidatos.get(7));
            // if(dataDeNascimento.equals("")){
                // System.out.println("linha " + linha);
                // System.out.println(valoresCandidatos);
            // }
            int codigoDoCargo = Integer.parseInt(unquote(valoresCandidatos.get(0)));
            int numeroDaFederacao = Integer.parseInt(unquote(valoresCandidatos.get(6)));
            int numeroDoCandidato = Integer.parseInt(unquote(valoresCandidatos.get(2)));
            int genero = Integer.parseInt(unquote(valoresCandidatos.get(9)));

            // verificando se foi eleito
            int situacaoDaTotalizacao = Integer.parseInt(unquote(valoresCandidatos.get(8)));
            int deferido = Integer.parseInt(unquote(valoresCandidatos.get(1)));
            String destinoVotos = unquote(valoresCandidatos.get(10));
            
            // voltando ao começo do loop se não houver valoresCandidatos coincidentes
            if(codigoDoCargo != cargoAtual.getCodigoCargo()) continue;

            
            // System.out.println(valoresCandidatos);
            sistema.cadastraCandidato(numeroPartido, nomeDeUrna, dataDeNascimento, codigoDoCargo, numeroDaFederacao, numeroDoCandidato, genero, situacaoDaTotalizacao, deferido, destinoVotos);
            
        }

        // int i = 0;
        //contabilisar os votos
        while(arquivoVotacoes.hasNextValues()){
            List<String> valoresVotacoes = arquivoVotacoes.nextValues(valuesVotacoes);
            // i++;
            int numeroVotavel = Integer.parseInt(unquote(valoresVotacoes.get(1)));
            int qtd_votos = Integer.parseInt(unquote(valoresVotacoes.get(2)));
            int codigoDoCargo = Integer.parseInt(unquote(valoresVotacoes.get(0)));

            // voltando ao começo do loop se não houver valoresCandidatos coincidentes
            if(codigoDoCargo != cargoAtual.getCodigoCargo()) continue;

            //validando votos
            int[] codigosDeVotosInvalidos = {95, 96, 97, 98};
            if(Arrays.binarySearch(codigosDeVotosInvalidos, numeroVotavel) >= 0) continue;

            //declarando votos
            // System.out.println("Linha " + i);
            
            Candidato candidatoVotado = sistema.getCandidato(numeroVotavel);
            if(candidatoVotado != null){
                if(candidatoVotado.isCandidatoLegenda()){
                    sistema.declaraVotosDeLegenda(candidatoVotado.getPartido().getNumeroDoPartido(), qtd_votos);
                }else{
                    sistema.declaraVotosNominais(numeroVotavel, qtd_votos);
                }
            }else{
                sistema.declaraVotosDeLegenda(numeroVotavel, qtd_votos);
            }

        }
        
        Relatorio relatorioFinal = new Relatorio(sistema, tipoEleicao, diaVotacao);
    
        relatorioFinal.primeiro();
        relatorioFinal.segundo();
        relatorioFinal.terceiro();
        relatorioFinal.quarto();
        relatorioFinal.quinto();
        relatorioFinal.sexto();
        relatorioFinal.oitavo();
        relatorioFinal.nono();
        relatorioFinal.decimo();
        relatorioFinal.decimoPrimeiro();
	    
    }

    //--------FUNCOES QUE SAO BEM UTEIS PARA O PROGRAMA-----------//
	//Coloca a string entre aspas duplas
	private static String quote(String stringToBeQuoted){
		return "\"" + stringToBeQuoted + "\"";
	}


	//Retira aspas duplas da string
	//se nao existem aspas em um dos lados retorna a propria string 
	private static String unquote(String stringToBeUnquoted){
		if(stringToBeUnquoted.endsWith("\"") && stringToBeUnquoted.startsWith("\"")){
			return stringToBeUnquoted.substring(1, stringToBeUnquoted.length() - 1);
		}else
		return stringToBeUnquoted;
	}
	
	private static void quote(String[] stringsToBeQuoted){
		for(int i = 0; i < stringsToBeQuoted.length; i++){
			stringsToBeQuoted[i] = quote(stringsToBeQuoted[i]);
		}
	}

    private static void unquote(String[] stringsToBeQuoted){
		for(int i = 0; i < stringsToBeQuoted.length; i++){
			stringsToBeQuoted[i] = unquote(stringsToBeQuoted[i]);
		}
	}
}
