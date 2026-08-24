import java.util.ArrayList;

public class AnalisadorLexico {

    String texto;

    public AnalisadorLexico(String texto) {
        this.texto = texto;
    }

    public ArrayList<Linha> classificarTolkens() {
        ArrayList<Palavra> palavras = new ArrayList<>();
        return geradorLinhas(palavras);
    }

    public ArrayList<Linha> geradorLinhas(ArrayList<Palavra> palavras){
        return null;
    }
    
}
