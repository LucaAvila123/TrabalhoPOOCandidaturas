import Relatorios.*;
import java.io.*;
import Candidaturas.*;
import Estaticos.*;

public class App {
    public static void main(String[] args) throws Exception {
        // PRIMEIRA PARTE: lidando com as entradas no terminal
        // unzip <arquivo>.zip
        // ant compile
        // ant jar
        // java -jar deputados.jar <opção_de_cargo> <caminho_arquivo_candidatos> <caminho_arquivo_votacao> <data>
        
        // java -jar deputados.jar --estadual consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022
        // java -jar deputados.jar --federal consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022
        // int i = 0;
        // for (String string : args) {
        //     System.out.println(i + " " + string);
        //     i++;
        // }

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
        
        // isso daqui é só para inicializar o dia da votação
        // só será usado para setar valores a serem usados no objeto Candidato
        DataEleicao diaVotacao = new DataEleicao(args[3]);
        
        // SEGUNDA PARTE: leitura dos arquivos de candidatos
        // SistemaEleitoral sistema = new SistemaEleitoral();
        // int CD_CARGO_index = -1;
        // int CD_SITUACAO_CANDIDADO_TOT_index = -1;
        // int NR_CANDIDATO_index = -1;
        // int NM_URNA_CANDIDATO_index = -1;
        // int NR_PARTIDO_index = -1;
        // int SG_PARTIDO_index = -1;
        // int NR_FEDERACAO_index = -1;
        // int DT_NASCIMENTO_index = -1;
        // int CD_SIT_TOT_TURNO_index = -1;
        // int CD_GENERO_index = -1;
        // int NM_TIPO_DESTINACAO_VOTOS_index = -1;

        // try {
        //     BufferedReader buffRead = new BufferedReader(new FileReader("consulta_cand_2-122/consulta_cand_2-122_ES.csv"));
        //     String linha = "";
        //     String separando[];

        //     linha = buffRead.readLine();
        //     // o csv é dividido por ; e tem aspas em volta de todos os elementos
        //     separando = linha.split(";");
            
        //     for(int i = 0; i < separando.length; i++){
        //         if(separando[i].equals("CD_CARGO")) CD_CARGO_index = i;
        //         if(separando[i].equals("CD_SITUACAO_CANDIDADO_TOT")) CD_SITUACAO_CANDIDADO_TOT_index = i;
        //         if(separando[i].equals("NR_CANDIDATO")) NR_CANDIDATO_index = i;
        //         if(separando[i].equals("NM_URNA_CANDIDATO")) NM_URNA_CANDIDATO_index = i;
        //         if(separando[i].equals("NR_PARTIDO")) NR_PARTIDO_index = i;
        //         if(separando[i].equals("SG_PARTIDO")) SG_PARTIDO_index = i;
        //         if(separando[i].equals("NR_FEDERACAO")) NR_FEDERACAO_index = i;
        //         if(separando[i].equals("DT_NASCIMENTO")) DT_NASCIMENTO_index = i;
        //         if(separando[i].equals("CD_SIT_TOT_TURNO")) CD_SIT_TOT_TURNO_index = i;
        //         if(separando[i].equals("CD_GENERO")) CD_GENERO_index = i;
        //         if(separando[i].equals("NM_TIPO_DESTINACAO_VOTOS")) NM_TIPO_DESTINACAO_VOTOS_index = i;
        //     }
            
        //     linha = buffRead.readLine();
        //     while(linha != null){
        //         separando = linha.split(";");

        //         linha = buffRead.readLine();
        //     }
        //     buffRead.close();

        // } catch (Exception e) {
        //     System.out.println("Arquivo de candidatos não encontrado");
        // }
        
    }   
}
