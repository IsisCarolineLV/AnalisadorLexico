import java.util.ArrayList;

public class AnalisadorLexico {

    private StringBuilder texto;
    private int i;
    private ArrayList<Linha> linhasErradas = new ArrayList<>();
    private static String[] palavrasReservadas = { "ABSOLUTE", "ARRAY", "BEGIN", "CASE",
        "CHAR", "CONST", "DIV", "DO", "DOWTO", "ELSE", "END", "EXTERNAL",
        "FILE", "FOR", "FORWARD", "FUNC", "FUNCTION", "GOTO", "IF", "IMPLEMENTATION",
        "INTEGER", "INTERFACE", "INTERRUPT", "LABEL", "MAIN", "NIL", "NIT",
        "OF", "PACKED", "PROC", "PROGRAM", "REAL", "RECORD", "REPEAT", "SET",
        "SHL", "SHR", "STRING", "THEN", "TO", "TYPE", "UNIT", "UNTIL", "USES",
        "VAR", "WHILE", "WITH", "XOR", "READ", "WRITE", "WRITELN"};

    private static String[] operacoesLogicas = {"and", "or", "not"};

    public AnalisadorLexico(StringBuilder texto) {
        this.texto = texto;
        //System.out.println("TEXTO:\n"+texto.toString());
        i=0;
    }

    public ArrayList<Linha> classificarTolkens() {
        ArrayList<Palavra> palavras = new ArrayList<>();

        int tamanho = texto.length();
        i=0;
        while(i<tamanho-1){
            
            Palavra novaPalavra = null;
            char c = texto.charAt(i);

            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                novaPalavra=achouIdentificador();
            else if(c>='0' && c<='9')
                novaPalavra = achouNumeral();
            else if(c=='\n'){
                novaPalavra = new Palavra();
                i++;
            }else if(c==' '){
                i++; continue;
            }else if(c=='/'){
                novaPalavra = achouComentario(palavras);
            }else { 
                novaPalavra = achouSimbolo(); 
            }
            if(novaPalavra!=null)
                palavras.add(novaPalavra);
        }

        //imprimindo:
        System.out.println("------------------------------------------");
        for(Palavra p: palavras){
            System.out.println(p.lexema+": "+p.token);
        }
        System.out.println("------------------------------------------");

        return geradorLinhas(palavras);
    }

    private Palavra achouComentario(ArrayList<Palavra> palavras) {
        String lexema = "";
        
        // Verifica se é o início de um comentário de bloco /*
        if (i + 1 < texto.length() && texto.charAt(i + 1) == '*') {
            lexema += "/*";
            i += 2; // Pula o '/*'
            
            while (i < texto.length()) {
                char c = texto.charAt(i);
                
                // Verifica se achou o fechamento */
                if (i + 1 < texto.length() && c == '*' && texto.charAt(i + 1) == '/') {
                    lexema += "*/";
                    i += 2; // Pula o '*/'
                    break;
                }
                
                // O SEGREDO: Se achar quebra de linha, divide o token para a interface gráfica
                if (c == '\n') {
                    if (!lexema.isEmpty()) {
                        // Salva o pedaço lido até aqui como comentário
                        palavras.add(new Palavra(lexema, "comentario"));
                    }
                    // Cria e injeta o token "ESPACO" padrão que quebra a linha na tela
                    palavras.add(new Palavra()); 
                    
                    lexema = ""; // Reseta o lexema para a próxima linha do comentário
                    i++;
                } else {
                    lexema += c;
                    i++;
                }
            }
            
            // Retorna a última parte do comentário para ser adicionada no loop principal
            if (!lexema.isEmpty()) {
                return new Palavra(lexema, "comentario");
            }
            return null;
        }
        
        // Se não for '/*', então é apenas um operador de divisão '/'
        i++;
        return new Palavra("/", "operador aritmetico");
    }

    private Palavra achouSimbolo() {
        char c = texto.charAt(i);
        //atribuicao
        if (c == ':' && i + 1 < texto.length() && texto.charAt(i + 1) == '=') { 
            i += 2; 
            return new Palavra(":=", "atribuição"); 
        }

        //relacionais
        if (c == '>') { 
            if (i + 1 < texto.length() && texto.charAt(i + 1) == '=') { //confere se eh >=
                i += 2; return new Palavra(">=", "operador relacional"); 
            } else { 
                i++; return new Palavra(">", "operador relacional"); 
            }
        } 
        if (c == '<') {
            if (i + 1 < texto.length()) { 
                char proximo = texto.charAt(i + 1); 
                if (proximo == '=') { //confere se eh >=
                    i += 2; 
                    return new Palavra("<=", "operador relacional"); 
                } else if (proximo == '>') { //confere se eh !=
                    i += 2; 
                    return new Palavra("<>", "operador relacional"); 
                }
            } 
            i++; 
            return new Palavra("<", "operador relacional"); 
        } 
        if (c == '=') {
            i++; 
            return new Palavra("=", "operador relacional"); 
        }

        //aritmetico
        if (c == '+' || c == '-' || c == '*' || c == '/') { 
            i++; 
            return new Palavra(c+"", "operador aritmetico"); 
        } 

        //especiais
        if (c == '(' || c == ')' || c == ',' || c == ';' || c == ':') { 
            i++; 
            return new Palavra(c+"", "simbolo especial"); 
        } 

        //fim
        if (c == '.') {
            i++; 
            return new Palavra(".", "fim"); 
        }

        //se chegou ate aqui nao eh nenhum caractere resconhecido
        i++; 
        return new Palavra(c + "", "identificador mal-formado");
    }

    private Palavra achouNumeral() {
        Palavra novoNum = null;
        String lexema = texto.charAt(i)+"";
        int aux=i+1;
        char estado='i';    //finais: i=inteiro n= notacao cientifica acabada f=float
                            //nao finais: c=cientifico nao acabado

        //notacao cientifica inteira
        char c = texto.charAt(aux);

        if(texto.length()-aux>4 && 
            texto.charAt(aux)=='x' && 
            texto.charAt(aux+1)=='1' && 
            texto.charAt(aux+2)=='0' &&
            texto.charAt(aux+3)=='e'){
                lexema+= "x10e";
                estado = 'c';
        }

        //falta logica de float com notacao cientifica

        while(aux<texto.length()){
            c = texto.charAt(aux);
            if(c>='0' && c<='9'){
                lexema+= texto.charAt(aux);
                if(estado=='c') estado = 'n';
            }else if(c=='.' && estado=='i'){
                lexema+= texto.charAt(aux);
                estado = 'f';
            }else{
                break;
            }
            aux++;
        }

        switch (estado) {
            case 'i':
                novoNum = new Palavra(lexema, "inteiro");
                break;
            case 'n':
                novoNum = new Palavra(lexema, "notacao cientifica");
                break;
            case 'f':
                novoNum = new Palavra(lexema, "flutuante"); //esqueci o nome certo
                break;
            default:
                break;
        }
        i=aux;
        return novoNum;
    }

    private Palavra achouIdentificador() {
        Palavra novoId = null;
        String lexema = texto.charAt(i)+"";
        int aux=i+1;
        while(aux<texto.length()){
            char c = texto.charAt(aux);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')|| (c>='0' && c<='9')){
                lexema+=c;
            }else{
                break;
            }
            aux++;
        }
        i=aux;

        for(String s: palavrasReservadas){
            if(lexema.equalsIgnoreCase(s)){
                novoId = new Palavra(lexema, "palavra reservada");
                return novoId;
            }
        }

        for(String o: operacoesLogicas){
            if(lexema.equalsIgnoreCase(o)){
                novoId = new Palavra(lexema, "operador logico");
                return novoId;
            }
        }

        if(lexema.equalsIgnoreCase("mod")){
            novoId = new Palavra(lexema, "operador aritmetico");
            return novoId;
        }

        //procura lexema na lista de palavras reservadas
        novoId = new Palavra(lexema, "identificador");
        return novoId;
    }

    public ArrayList<Linha> geradorLinhas(ArrayList<Palavra> palavras){
        ArrayList<Linha> linhas = new ArrayList<>();
        ArrayList<Palavra> palavrasLinha = new ArrayList<>();
        int cont=1;
        boolean erroNaLinha=false;
        Linha l =null;
        String mensagemErro="";

        for(Palavra p: palavras){
            try {
                if(p.getTolken().equals("ESPACO")){
                    l = new Linha(cont++, palavrasLinha);
                    linhas.add(l);
                    if(erroNaLinha){
                        l.setErro(mensagemErro);
                        mensagemErro = "";
                        linhasErradas.add(l);
                        erroNaLinha=false;
                    }
                    palavrasLinha = new ArrayList<>();

                }else{
                    palavrasLinha.add(p);
                }
            } catch (Exception e) {
                erroNaLinha = true;
                palavrasLinha.add(p);
                mensagemErro+="  ------ "+e.getMessage();
            }
        }

        if(!palavrasLinha.isEmpty()){

            l = new Linha(cont++, palavrasLinha);
            linhas.add(l);

            if(erroNaLinha){
                l.setErro(mensagemErro);
                linhasErradas.add(l);
            }
        }
        
        //System.out.println("Achou "+linhasErradas.size()+" erros");
        //for(Linha a: linhasErradas){ System.out.println("Erro:\n"+a.getErro());};

        return linhas;
    }

    public ArrayList<Linha> getLinhasErradas() {
        return linhasErradas;
    }
    
}
