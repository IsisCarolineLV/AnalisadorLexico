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
            }else if(c=='"'){
                novaPalavra = achouString(palavras);
            }else if(c=='\''){
                novaPalavra = achouChar();
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

    private Palavra achouChar() {
        Palavra nova = null;
        i++;
        if(texto.charAt(i+1)=='\''){
            nova = new Palavra("\'"+texto.charAt(i)+"\'", "char");
            i+=2;
        }else if(texto.charAt(i)=='\\'){
            nova = new Palavra("\'"+texto.charAt(i)+texto.charAt(i+1)+"\'", "char");
            i+=3;
        }else{
            nova = new Palavra("\'", "identificador invalido");
        }
        return nova;
    }

    private Palavra achouString(ArrayList<Palavra> palavras) {
        ArrayList<Palavra> stringGrande = new ArrayList<>();
        String lexema = "\"";
        boolean saiuNormal=false;
        int iInicial =i;
        i++;
            
        while (i < texto.length()) {
            char c = texto.charAt(i);
            
            // Verifica se achou o fechamento */
            if (c=='"') {
                lexema += "\"";
                i ++;
                saiuNormal=true;
                break;
            }

            if (c == '\n') {
                if (!lexema.isEmpty()) {
                    stringGrande.add(new Palavra(lexema, "string"));
                }

                stringGrande.add(new Palavra()); 
                
                lexema = ""; // Reseta o lexema para a próxima linha do comentário
                i++;
            } else {
                lexema += c;
                i++;
            }
        }
        if (!lexema.isEmpty() && saiuNormal) {
            if(stringGrande.size()>0) palavras.addAll(stringGrande);
            return new Palavra(lexema, "string");
        }
            i = iInicial +1;
            // Se nao for string, entao ah um identificador invalido
        return new Palavra("\"", "identificador invalido");
    }

    private Palavra achouComentario(ArrayList<Palavra> palavras) {
        String lexema = "";
        
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

                if (c == '\n') {
                    if (!lexema.isEmpty()) {
                        palavras.add(new Palavra(lexema, "comentario"));
                    }

                    palavras.add(new Palavra()); 
                    
                    lexema = ""; // Reseta o lexema para a próxima linha do comentário
                    i++;
                } else {
                    lexema += c;
                    i++;
                }
            }
            if (!lexema.isEmpty()) {
                return new Palavra(lexema, "comentario");
            }
            return null;
        }
        
        // Se nao for '/*', entao ah apenas um operador de divisao '/'
        i++;
        return new Palavra("/", "operador aritmetico");
    }

    private Palavra achouSimbolo() {
        char c = texto.charAt(i);
        //atribuicao
        if (c == ':' && i + 1 < texto.length() && texto.charAt(i + 1) == '=') { 
            i += 2; 
            return new Palavra(":=", "operador de atribuição"); 
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
                while(proximo==' ' || proximo =='\n') proximo = texto.charAt((++i) + 1); 
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
        if (c == '+' || c == '-' || c == '*' || c == '/' || c=='%') { 
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

        if (c=='^' || c=='∨' || c=='¬') { 
            i++; 
            return new Palavra(c+"", "operador lógico"); 
        } 

        //se chegou ate aqui nao eh nenhum caractere resconhecido
        i++; 
        return new Palavra(c + "", "identificador invalido");
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

        //procura lexema na lista de palavras reservadas
        for(String s: palavrasReservadas){
            if(lexema.equalsIgnoreCase(s)){
                novoId = new Palavra(lexema, "palavra reservada");
                return novoId;
            }
        }

        /*for(String o: operacoesLogicas){
            if(lexema.equalsIgnoreCase(o)){
                novoId = new Palavra(lexema, "operador logico");
                return novoId;
            }
        }

        if(lexema.equalsIgnoreCase("mod")){
            novoId = new Palavra(lexema, "operador aritmetico");
            return novoId;
        }*/
        
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

        return linhas;
    }

    public ArrayList<Linha> getLinhasErradas() {
        return linhasErradas;
    }
    
}
