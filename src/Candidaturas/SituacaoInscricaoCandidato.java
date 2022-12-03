
package Candidaturas;
import java.util.Arrays;

public enum SituacaoInscricaoCandidato {
    DEFERIDO, NAO_DEFERIDO;

    //favor inserir em ordem crescente (vai quebrar o codigo caso contrario)
    private static int[] codigosDeferido = {2, 16};

    //Verifica se o codigo assume o candidato como deferido ou nao
    public static SituacaoInscricaoCandidato verificaCodigo(int codigo){
        if(Arrays.binarySearch(codigosDeferido, codigo) >= 0){
            return DEFERIDO;
        }
        else{
            return NAO_DEFERIDO;
        }
    }
}
