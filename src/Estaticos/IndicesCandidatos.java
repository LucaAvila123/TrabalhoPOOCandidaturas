package Estaticos;

public enum IndicesCandidatos {
    CD_CARGO(0), CD_SITUACAO_CANDIDATO_TOT(1),
    NR_CANDIDATO(2), NM_URNA_CANDIDATO(3),
    NR_PARTIDO(4), SG_PARTIDO(5), NR_FEDERACAO(6),
    DT_NASCIMENTO(7), CD_SIT_TOT_TURNO(8),
    CD_GENERO(9), NM_TIPO_DESTINACAO_VOTOS(10);
    
    private int indice;
    IndicesCandidatos(int indice) {
        this.indice = indice;
    }
    
    public int num(){
        return indice;
    }
}