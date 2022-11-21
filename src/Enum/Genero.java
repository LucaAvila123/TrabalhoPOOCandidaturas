package Enum;

public enum Genero {
    MASCULINO(2), FEMININO(4);
    
    private int indice;
    Genero(int indice) {
        this.indice = indice;
    }
    
    public int getGenero(){
        return indice;
    }
}