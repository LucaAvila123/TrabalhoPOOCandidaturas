package Estaticos;

public enum IndicesVotacoes {
    CD_CARGO(0), NR_VOTAVEL(1), QT_VOTOS(2);
    
    private int indice;
    IndicesVotacoes(int indice) {
        this.indice = indice;
    }
    
    public int num(){
        return indice;
    }
}
