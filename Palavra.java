import java.awt.Color;

import javax.swing.JLabel;

public class Palavra {
    String lexema;
    String token;
    
    public Palavra(String lexema, String token) {
        this.lexema = lexema;
        this.token = token;
    }

    public JLabel criaLabel(){
        JLabel novo = new JLabel(lexema);
        novo.setBackground(Color.decode(corToken()));
        return novo;
    }

    public void getTolken() throws Exception{
        if(token.equals("identificador invalido")) 
            throw new Exception(token);
        if(token.equals("caractere desconhecido"))
            throw new Exception(token);
    } 

    public String corToken(){
        if(token.equals("identificador")) return "#13a2cd";
        if(token.equals("palavra reservada")) return "#c0bd00";
        if(token.equals("comentario")) return "#035f0a";
        if(token.equals("string")) return "#b06b04";
        if(token.equals("numero") || token.equals("caractere especial")) return "#8400a8";
        return "#1d0505";
    }
}
