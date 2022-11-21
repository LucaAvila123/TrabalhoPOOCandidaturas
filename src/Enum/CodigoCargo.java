package Enum;

// acredito que este enum será usado mais na main
public enum CodigoCargo {
    FEDERAL(6), ESTADUAL(7);
    private int indice;
    CodigoCargo(int indice) {
        this.indice = indice;
    }
    
    public int getCodigoCargo(){
        return indice;
    }
}
