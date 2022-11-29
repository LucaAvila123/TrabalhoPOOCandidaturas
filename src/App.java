import Relatorios.*;
import Estaticos.*;
import java.util.*;


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
        DataEleicao diaVotacao = new DataEleicao(dataEleicao);
        
        // SEGUNDA PARTE: leitura dos arquivos de candidatos

        //instanciando o sistema eleitoral
        SistemaEleitoral sistema = new SistemaEleitoral();

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


        //inicializar os candidatos
        while(arquivoCandidatos.hasNextValues()){

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
            sistema.cadastraCandidato(numeroPartido, nomeDeUrna, dataDeNascimento, codigoDoCargo, numeroDaFederacao, numeroDoCandidato, genero, situacaoDaTotalizacao, deferido, destinoVotos, diaVotacao);
            
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
            if(VotosInvalidos.ignorarNumero(numeroVotavel)) continue;

            //declarando votos
            // System.out.println("Linha " + i);
            sistema.declaraVotos(numeroVotavel, qtd_votos);
        }

        sistema.somaVotos();
        sistema.reordenaTodasListas();
        
        Relatorio relatorioFinal = new Relatorio(sistema, tipoEleicao);
    
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

        //*****************************************************/
        //******MANTIVE O RESTO COMENTADO CASO DE MERDA*******/
        //****************************************************/

        /*
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()));
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()).equals("\"V�lido\""));
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()).equals("\"Válido\""));
        
        
        do{
            if(tipoEleicao.equals("--estadual")){
                // voltando ao começo do loop se não houver valoresCandidatos coincidentes
                if(!(Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_CARGO.num()).replaceAll("\"", "")) == CodigoCargo.ESTADUAL.getCodigoCargo())){
                    valoresCandidatos = arquivoCandidatos.nextValues(valuesCandidatos);
                    continue;
                }
            }
            else if(tipoEleicao.equals("--federal")){
                if(!(Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_CARGO.num()).replaceAll("\"", "")) == CodigoCargo.FEDERAL.getCodigoCargo())){
                    valoresCandidatos = arquivoCandidatos.nextValues(valuesCandidatos);
                    continue;
                }
            }
            
            int numeroPartido = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.NR_PARTIDO.num()).replaceAll("\"", ""));
            String siglaPartido = valoresCandidatos.get(IndicesCandidatos.SG_PARTIDO.num()).replaceAll("\"", "");
            sistema.cadastraPartido(numeroPartido, siglaPartido);
            
            String nomeDeUrna = valoresCandidatos.get(IndicesCandidatos.NM_URNA_CANDIDATO.num()).replaceAll("\"", "");
            String dataDeNascimento = valoresCandidatos.get(IndicesCandidatos.DT_NASCIMENTO.num()).replaceAll("\"", "");
            int codigoDoCargo = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_CARGO.num()).replaceAll("\"", ""));
            int numeroDaFederacao = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.NR_FEDERACAO.num()).replaceAll("\"", ""));
            int numeroDoCandidato = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.NR_CANDIDATO.num()).replaceAll("\"", ""));
            int genero = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_GENERO.num()).replaceAll("\"", ""));
            // verificando se foi eleito
            int situacaoDaTotalizacao = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_SIT_TOT_TURNO.num()).replaceAll("\"", ""));
            int deferido = Integer.parseInt(valoresCandidatos.get(IndicesCandidatos.CD_SITUACAO_CANDIDATO_TOT.num()).replaceAll("\"", ""));
            String destinoVotos = valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()).replaceAll("\"", "");
            sistema.cadastraCandidato(numeroPartido, nomeDeUrna, dataDeNascimento, codigoDoCargo, numeroDaFederacao, numeroDoCandidato, genero, situacaoDaTotalizacao, deferido, destinoVotos);
            
            valoresCandidatos = arquivoCandidatos.nextValues(valuesCandidatos);
            
        }while(valoresCandidatos != null);

        do{
            if(tipoEleicao.equals("--estadual")){
                // voltando ao começo do loop se não houver valoresCandidatos coincidentes
                if(!(Integer.parseInt(valoresVotacoes.get(IndicesVotacoes.CD_CARGO.num()).replaceAll("\"", "")) == CodigoCargo.ESTADUAL.getCodigoCargo())){
                    valoresVotacoes = arquivoVotacoes.nextValues(valuesVotacoes);
                    continue;
                }
            }
            else if(tipoEleicao.equals("--federal")){
                if(!(Integer.parseInt(valoresVotacoes.get(IndicesVotacoes.CD_CARGO.num()).replaceAll("\"", "")) == CodigoCargo.FEDERAL.getCodigoCargo())){
                    valoresVotacoes = arquivoVotacoes.nextValues(valuesVotacoes);
                    continue;
                }
            }

            int numeroVotavel = Integer.parseInt(valoresVotacoes.get(IndicesVotacoes.NR_VOTAVEL.num()).replaceAll("\"", ""));
            if(VotosInvalidos.ignorarNumero(numeroVotavel)){
                valoresVotacoes = arquivoVotacoes.nextValues(valuesVotacoes);
                continue;
            }
            int qtd_votos = Integer.parseInt(valoresVotacoes.get(IndicesVotacoes.QT_VOTOS.num()).replaceAll("\"", ""));
            
            // essa função joga os votos para legenda ou nominais
            sistema.declaraVotos(numeroVotavel, qtd_votos);
            
            valoresVotacoes = arquivoVotacoes.nextValues(valuesVotacoes);
        }while(valoresVotacoes != null);

        sistema.reordenaTodasListas();

        Relatorio relatorioFinal = new Relatorio(sistema, tipoEleicao);
        
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
    */
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
