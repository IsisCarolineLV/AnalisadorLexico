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
        }
    }

    private void COMANDO_READ() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_READ'");
    }

    private void COMANDO_WRITELN() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_WRITELN'");
    }

    private void COMANDO_WRITE() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_WRITE'");
    }

    private void COMANDO_GOTO() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_GOTO'");
    }

    private void COMANDO_WHILE() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_WHILE'");
    }

    private void COMANDO_FOR() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_FOR'");
    }

    private void COMANDO_IF() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_IF'");
    }

    private void COMANDO_ATRIBUICAO() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'COMANDO_ATRIBUICAO'");
    }

}
