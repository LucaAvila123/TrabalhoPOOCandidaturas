package Estaticos;

public enum DataEleicao {
    ANO(2022), MES(10), DIA(2);

    private int indice;
    DataEleicao(int indice) {
        this.indice = indice;
    }
    
    public int getNumero(){
        return indice;
    }
}
