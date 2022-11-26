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
        if(!(tipoEleicao.equals("--estadual") || tipoEleicao.equals("--federal"))){
            throw new Exception("Coloque --federal ou --estadual no campo <opção_de_cargo>");
        }
        System.out.println(arquivoCandidaturas);
        // isso daqui é só para inicializar o dia da votação
        // só será usado para setar valoresCandidatos a serem usados no objeto Candidato
        DataEleicao diaVotacao = new DataEleicao(dataEleicao);
        
        // SEGUNDA PARTE: leitura dos arquivos de candidatos
        SistemaEleitoral sistema = new SistemaEleitoral();
        CsvReader arquivoCandidatos = new CsvReader(arquivoCandidaturas, ";", "UTF-8");
        CsvReader arquivoVotacoes = new CsvReader(arquivoVotacao, ";", "UTF-8");
        // vai pegar as colunas específicas do arquivo
        System.out.println(arquivoCandidaturas != null);
        
        //lendo arquivo de candidatos:       0               1                               2                   3                       4                   5                   6                   7                   8                       9               10                              
        String[] valuesCandidatos = {"\"CD_CARGO\"", "\"CD_SITUACAO_CANDIDATO_TOT\"", "\"NR_CANDIDATO\"", "\"NM_URNA_CANDIDATO\"", "\"NR_PARTIDO\"", "\"SG_PARTIDO\"", "\"NR_FEDERACAO\"", "\"DT_NASCIMENTO\"", "\"CD_SIT_TOT_TURNO\"", "\"CD_GENERO\"", "\"NM_TIPO_DESTINACAO_VOTOS\""};
        List<String> valoresCandidatos = arquivoCandidatos.nextValues(valuesCandidatos);
        if(valoresCandidatos == null) throw new Exception("Arquivo de candidaturas tem formato inválido");

        String[] valuesVotacoes = {"\"CD_CARGO\"", "\"NR_VOTAVEL\"", "\"QT_VOTOS\""};
        List<String> valoresVotacoes = arquivoCandidatos.nextValues(valuesVotacoes);
        if(valoresVotacoes == null) throw new Exception("Arquivo de votacoes tem formato inválido");

        // TODO: resolver o formato de leitura aqui
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()));
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()).equals("\"V�lido\""));
        System.out.println(valoresCandidatos.get(IndicesCandidatos.NM_TIPO_DESTINACAO_VOTOS.num()).equals("\"Válido\""));
        
        // TODO: verificar a NoSuchElementException usada
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
    }   
}
