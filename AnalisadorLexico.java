import java.util.ArrayList;

public class AnalisadorLexico {

    private StringBuilder texto;
    private int i;
    private ArrayList<Linha> linhasErradas = new ArrayList<>();

    public AnalisadorLexico(StringBuilder texto) {
        this.texto = texto;
        System.out.println("TEXTO:\n"+texto.toString());
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
            }else{
                novaPalavra = new Palavra(c+"", "identificador invalido");
                i++;
            }
            if(novaPalavra!=null)
                palavras.add(novaPalavra);
        }

        /*/imprimindo:
        System.out.println("------------------------------------------");
        for(Palavra p: palavras){
            System.out.println(p.lexema+": "+p.token);
        }
        System.out.println("------------------------------------------");*/

        return geradorLinhas(palavras);
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
        //procura lexema na lista de palavras reservadas
        novoId = new Palavra(lexema, "identificador");
        i=aux;
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
        
        System.out.println("Achou "+linhasErradas.size()+" erros");
        for(Linha a: linhasErradas){ System.out.println("Erro:\n"+a.getErro());};

        return linhas;
    }

    public ArrayList<Linha> getLinhasErradas() {
        return linhasErradas;
    }
    
}
