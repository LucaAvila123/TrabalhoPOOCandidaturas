package Candidaturas;

public enum SituacaoFederacaoCandidato {
    FEDERADO, NAO_FEDERADO;

    private static int codigoNaoFederado = -1;

    public static SituacaoFederacaoCandidato verificaCodigo(int codigo){
        if(codigo == codigoNaoFederado){
            return NAO_FEDERADO;
        }else{
            return FEDERADO;
        }
    }
}
