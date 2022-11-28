package Relatorios;

import java.util.*;
import java.io.*;


public class CsvReader {
  
    private File source;
    private HashMap<String, Integer> headerIndexes;
    private Scanner fileScanner;
    private String delimiter;
    private String currentLine;

    //construtoress 

    //Versao que assume o uso de headers
    public CsvReader (String filePath, String delimiter,String charsetName) throws FileNotFoundException, NoSuchElementException{
        //setando o delimiter
        this.delimiter = delimiter;
        
        //configurando o diretorio do arquivo e o charset
        source = new File(filePath);
        fileScanner = new Scanner(source, charsetName);

        //especifico caso usando um header
        headerIndexes = new HashMap<>();
        
        //throws de NoSuchElementException caso nao haja um cabecalho no arquivo;
        Scanner lineScanner = new Scanner(fileScanner.nextLine());
        lineScanner.useDelimiter(delimiter);

        //colocando cada indice no hashmap
        int headerIndex = 0;
        while(lineScanner.hasNext()){  
            headerIndexes.put(lineScanner.next(), headerIndex);
            headerIndex++;
        }

        //atualiza a primeira linha do arquivo
        //(feito assim por funcionalidade dos proximos metodos)
        if(fileScanner.hasNext()){
            currentLine = fileScanner.nextLine();
        }else{
            currentLine = null;
        }

    }
    
    //Versao que NAO assume o uso de headers
    public CsvReader (String filePath, String delimiter,String charsetName, boolean usingHeader) throws FileNotFoundException, NoSuchElementException{
        //setando o delimiter
        this.delimiter = delimiter;
        
        //configurando o diretorio do arquivo e o charset
        source = new File(filePath);
        fileScanner = new Scanner(source, charsetName);

        if(usingHeader){
            //especifico caso usando um header
            headerIndexes = new HashMap<>();

            //throws de NoSuchElementException caso nao haja um cabecalho no arquivo;
            Scanner lineScanner = new Scanner(fileScanner.nextLine());
            lineScanner.useDelimiter(delimiter);

            //colocando cada indice no hashmap
            int headerIndex = 0;
            while(lineScanner.hasNext()){  
                headerIndexes.put(lineScanner.next(), headerIndex);
                headerIndex++;
            }
        } else {
            //e null se nao estiver usando o cabecaho
            headerIndexes = null;
        }

        //atualiza a primeira linha do arquivo
        //(feito assim por funcionalidade dos proximos metodos)
        if(fileScanner.hasNext()){
            currentLine = fileScanner.nextLine();
        }else{
            currentLine = null;
        }

    }

    public List<String> nextValues(Collection<String> selectedItems) throws NoSuchElementException{
        
        //retorna uma lista vazia se nao esta usando cabecalho no arquivo
        if(headerIndexes == null){
            return null;
        }

        //Usando ArrayList para uma operacao de tempo constante         
        List<String> lineValues = new ArrayList<>(); //tem todos os valoras da linha
        List<String> selectedValuesList = new ArrayList<String>(); //sera retornada no final

        // ATENCAO: throw de NoSuchElementException se nao houver uma proxima linha;
        if(currentLine == null){
            throw new NoSuchElementException("Next line not found"); 
            //return null;
        }
        
        //inicializando o Scanner da linha
       
        Scanner lineScanner = new Scanner(currentLine);
        lineScanner.useDelimiter(delimiter);

        //adicionando os items da linha do arquivo para a lista
        while(lineScanner.hasNext()){
            lineValues.add(lineScanner.next());
        }
        
        //o lineScanner nao sera usado mais
        lineScanner.close();

        //selecionando os itens relevantes
        for(String item : selectedItems){

            Integer itemIndex = headerIndexes.get(item);

            //caso o item selecionado nao exista no cabecalho
            if(itemIndex == null){
               throw new NoSuchElementException("Item \"" + item + "\" not found on file header"); 
            }

            String selectedValue;

            // caso nao exista um valor para tal cabecalho ele sera identificado com a string "NULL"
            try{
                selectedValue = lineValues.get(itemIndex);
            } catch (IndexOutOfBoundsException E){
                selectedValue = "NULL";
            }

            selectedValuesList.add(selectedValue);
        }

        //salvar a proxima linha para o proximo uso da funcao
        //(Os erros podiam estragar a linha atual do fileScanner)
        
        if(fileScanner.hasNext()){
            currentLine = fileScanner.nextLine();
        }else{
            currentLine = null;
        }
        
        return selectedValuesList;
    }

    public List<String> nextValues() throws NoSuchElementException{

        //Usando ArrayList para uma operacao de tempo constante         
        List<String> lineValues = new ArrayList<>(); //tem todos os valoras da linha

        // ATENCAO: throw de NoSuchElementException se nao houver uma proxima linha;
        if(currentLine == null){
            throw new NoSuchElementException("Next line not found"); 
            //return null;
        }
        
        //inicializando o Scanner da linha
       
        Scanner lineScanner = new Scanner(currentLine);
        lineScanner.useDelimiter(delimiter);

        //adicionando os items da linha do arquivo para a lista
        while(lineScanner.hasNext()){
            lineValues.add(lineScanner.next());
        }
        
        //o lineScanner nao sera usado mais
        lineScanner.close();

        //salvar a proxima linha para o proximo uso da funcao
        //(Os erros podiam estragar a linha atual do fileScanner)
        
        if(fileScanner.hasNext()){
            currentLine = fileScanner.nextLine();
        }else{
            currentLine = null;
        }
        
        return lineValues;
    }

    //Versao que recebe um Array de Strings
    public List<String> nextValues(String[] selectedItems) throws NoSuchElementException{
        
        //retorna uma lista vazia se nao esta usando cabecalho no arquivo
        if(headerIndexes == null){
            return null;
        }
       
        //Usando ArrayList para uma operacao de tempo constante         
        List<String> lineValues = new ArrayList<>(); //tem todos os valoras da linha
        List<String> selectedValuesList = new ArrayList<String>(); //sera retornada no final

        // ATENCAO: throw de NoSuchElementException se nao houver uma proxima linha;
        if(currentLine == null){
            throw new NoSuchElementException("Next line not found"); 
        }

        //inicializando o Scanner da linha
        Scanner lineScanner = new Scanner(currentLine);
        lineScanner.useDelimiter(delimiter);

        //adicionando os items da linha do arquivo para a lista
        while(lineScanner.hasNext()){
            lineValues.add(lineScanner.next());
        }
        
        //o lineScanner nao sera usado mais
        lineScanner.close();

        //selecionando os itens relevantes
        for(String item : selectedItems){

            Integer itemIndex = headerIndexes.get(item);

            //caso o item selecionado nao exista no cabecalho
            if(itemIndex == null){
               throw new NoSuchElementException("Item \"" + item + "\" not found on file header"); 
            }

            String selectedValue;

            // caso nao exista um valor para tal cabecalho ele sera identificado com a string "NULL"
            try{
                selectedValue = lineValues.get(itemIndex);
            } catch (IndexOutOfBoundsException E){
                selectedValue = "NULL";
            }

            selectedValuesList.add(selectedValue);
        }

        //salvar a proxima linha para o proximo uso da funcao
        //(Os erros podiam estragar a linha atual do fileScanner)
        
        if(fileScanner.hasNext()){
            currentLine = fileScanner.nextLine();
        }else{
            currentLine = null;
        }
        
        return selectedValuesList;
    }

    public boolean hasNextValues(){
        if (currentLine == null)
            return false;
        else
            return true;
    }

    public boolean isUsingHeader(){
        if(headerIndexes != null)
            return true;
        else
            return false; 
    }
}
