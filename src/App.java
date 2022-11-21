import Relatorios.*;

import java.io.*;

import Candidaturas.*;
import Estaticos.*;

public class App {
    public static void main(String[] args) throws Exception {
        // PRIMEIRA PARTE: leitura dos arquivos de candidatos
        SistemaEleitoral sistema = new SistemaEleitoral();
        int CD_CARGO_index = -1;
        int CD_SITUACAO_CANDIDADO_TOT_index = -1;
        int NR_CANDIDATO_index = -1;
        int NM_URNA_CANDIDATO_index = -1;
        int NR_PARTIDO_index = -1;
        int SG_PARTIDO_index = -1;
        int NR_FEDERACAO_index = -1;
        int DT_NASCIMENTO_index = -1;
        int CD_SIT_TOT_TURNO_index = -1;
        int CD_GENERO_index = -1;
        int NM_TIPO_DESTINACAO_VOTOS_index = -1;

        try {
            BufferedReader buffRead = new BufferedReader(new FileReader("consulta_cand_2-122/consulta_cand_2-122_ES.csv"));
            String linha = "";
            String separando[];

            linha = buffRead.readLine();
            // o csv é dividido por ; e tem aspas em volta de todos os elementos
            separando = linha.split(";");
            
            for(int i = 0; i < separando.length; i++){
                if(separando[i].equals("CD_CARGO")) CD_CARGO_index = i;
                if(separando[i].equals("CD_SITUACAO_CANDIDADO_TOT")) CD_SITUACAO_CANDIDADO_TOT_index = i;
                if(separando[i].equals("NR_CANDIDATO")) NR_CANDIDATO_index = i;
                if(separando[i].equals("NM_URNA_CANDIDATO")) NM_URNA_CANDIDATO_index = i;
                if(separando[i].equals("NR_PARTIDO")) NR_PARTIDO_index = i;
                if(separando[i].equals("SG_PARTIDO")) SG_PARTIDO_index = i;
                if(separando[i].equals("NR_FEDERACAO")) NR_FEDERACAO_index = i;
                if(separando[i].equals("DT_NASCIMENTO")) DT_NASCIMENTO_index = i;
                if(separando[i].equals("CD_SIT_TOT_TURNO")) CD_SIT_TOT_TURNO_index = i;
                if(separando[i].equals("CD_GENERO")) CD_GENERO_index = i;
                if(separando[i].equals("NM_TIPO_DESTINACAO_VOTOS")) NM_TIPO_DESTINACAO_VOTOS_index = i;
            }
            
            buffRead.close();
        } catch (Exception e) {
            System.out.println("Arquivo de candidatos não encontrado");
        }
        
    }   
}
