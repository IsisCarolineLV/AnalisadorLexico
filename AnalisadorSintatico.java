import java.util.ArrayList;

public class AnalisadorSintatico {
    ArrayList<Palavra> tokens;
    private int i=0;

    public AnalisadorSintatico (ArrayList<Palavra> tokens){
        this.tokens=tokens;
    }
    
    public void analisaSintaxe(){
        i=0;
        while(i<tokens.size()){
            PROG(tokens.get(i));
        }

    }

    public boolean match (String tokenEsperado, Palavra tokenAnalisado){
        return tokenAnalisado.token.equalsIgnoreCase(tokenEsperado);
    }

    public boolean matchComlexema (String lexemaEsperado, Palavra tokenAnalisado){
        return tokenAnalisado.lexema.equalsIgnoreCase(lexemaEsperado);
    }

    private void PROG(Palavra palavraInicial){
        try {
            if(palavraInicial.getTolken().equalsIgnoreCase("palavra reservada")){
                i++;
                if(palavraInicial.lexema.equalsIgnoreCase("IF")) COMANDO_IF();
                else if(palavraInicial.lexema.equalsIgnoreCase("FOR")) COMANDO_FOR();
                else if(palavraInicial.lexema.equalsIgnoreCase("WHILE")) COMANDO_WHILE();
                else if(palavraInicial.lexema.equalsIgnoreCase("GOTO")) COMANDO_GOTO();
                else if(palavraInicial.lexema.equalsIgnoreCase("WRITE")) COMANDO_WRITE();
                else if(palavraInicial.lexema.equalsIgnoreCase("WRITELN")) COMANDO_WRITELN();
                else if(palavraInicial.lexema.equalsIgnoreCase("READ")) COMANDO_READ();
            } else if(palavraInicial.getTolken().equalsIgnoreCase("identificador")){
                COMANDO_ATRIBUICAO();
            } else{
                palavraInicial.achouErro();
                throw new Exception ("erro de sintaxe");
            }
        } catch (Exception e) {
            //pular comando
            i++;
            while(tokens.get(i).lexema==null) {i++; if(i>=tokens.size()) return;}
            while(!tokens.get(i).lexema.equals(".") && !tokens.get(i).lexema.equals("END")){
                tokens.get(i).achouErro();
                do{
                    i++;
                    if(i>=tokens.size()) return;
                }while(tokens.get(i).lexema==null);
                
            }
        }
    }

    private void COMANDO_READ() {
        i++;
    }

    private void COMANDO_WRITELN() {
        i++;
    }

    private void COMANDO_WRITE() {
        i++;
    }

    private void COMANDO_GOTO() {
        i++;
    }

    private void COMANDO_WHILE() {
        i++;
    }

    private void COMANDO_FOR() {
        i++;
    }

    private void COMANDO_IF() {
        i++;
    }

    private void COMANDO_ATRIBUICAO() {
        i++;
    }

}
