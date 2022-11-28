package Estaticos;

public class DataEleicao {
    public int ANO;
    public int MES;
    public int DIA;

    // o formato da data está sempre setado para dd/mm/yyyy
    // essa classe precisa ser inicializada para que Candidato possa receber alguma idade
    public DataEleicao(String data){
        String[] formataData = data.split("/");
        DIA = Integer.parseInt(formataData[0]);
        MES = Integer.parseInt(formataData[1]);
        ANO = Integer.parseInt(formataData[2]);
    }

    
}
