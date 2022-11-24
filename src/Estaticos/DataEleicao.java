package Estaticos;

public class DataEleicao {
    static private int ano;
    static private int mes;
    static private int dia;

    // o formato da data está sempre setado para dd/mm/yyyy
    // essa classe precisa ser inicializada para que Candidato possa receber alguma idade
    public DataEleicao(String data){
        String[] formataData = data.split("/");
        dia = Integer.parseInt(formataData[0]);
        mes = Integer.parseInt(formataData[1]);
        ano = Integer.parseInt(formataData[2]);
    }

    public static final int ANO = ano;
    public static final int MES = mes;
    public static final int DIA = dia;
}
