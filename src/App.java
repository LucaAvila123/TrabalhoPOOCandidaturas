import Relatorios.*;

import java.io.*;

import Candidaturas.*;
import Estaticos.*;
import Exceptions.*;

public class App {
    public static void main(String[] args) throws Exception {
        // PRIMEIRA PARTE: leitura dos arquivos de candidatos
        SistemaEleitoral sistema = new SistemaEleitoral();
        try {
            BufferedReader buffRead = new BufferedReader(new FileReader("consulta_cand_2022/consulta_cand_2022_ES.csv"));
            buffRead.close();
        } catch (Exception e) {
            System.out.println("Arquivo de candidatos não encontrado");
        }
        
    }   
}
