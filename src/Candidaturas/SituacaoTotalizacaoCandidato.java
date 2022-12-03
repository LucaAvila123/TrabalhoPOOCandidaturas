
package Candidaturas;
import java.util.Arrays;

public enum SituacaoTotalizacaoCandidato {
    ELEITO, NAO_ELEITO;

    //favor inserir em ordem crescente (vai quebrar o codigo caso contrario)
    private static int[] codigosEleito = {2, 3};

    //Verifica se o codigo assume o candidato como eleito ou nao
    public static SituacaoTotalizacaoCandidato verifica(int codigo){
        if(Arrays.binarySearch(codigosEleito, codigo) >= 0){
            return ELEITO;
        }
        else{
            return NAO_ELEITO;
        }
    }
}
