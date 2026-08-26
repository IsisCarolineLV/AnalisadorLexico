import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Palavra {
    String lexema;
    String token;
    
    public Palavra(String lexema, String token) {
        this.lexema = lexema;
        this.token = token;
    }

    public Palavra(){
        lexema=null;
        token="ESPACO";
    }

    public JLabel criaLabel(){

        if(lexema!=null ){
            JLabel novo = new JLabel(lexema);
            novo.setForeground(Color.decode(corToken()));
            novo.setToolTipText(token);
            //novo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            return novo;
        }else{
            return null;
        }
        
    }

    public String getTolken() throws Exception{
        if(token.equals("identificador invalido")) 
            throw new Exception(token+": "+lexema);
        return token;
    } 

    public String corToken(){
        if(token.equals("identificador")) return "#13a2cd";
        if(token.equals("palavra reservada")) return "#c0bd00";
        if(token.equals("comentario")) return "#035f0a";
        if(token.equals("string")) return "#b06b04";
        if(token.equals("inteiro") || 
        token.equals("flutuante") ||
        token.equals("notacao cientifica") ||
        token.equals("caractere especial")) return "#8400a8";
        return "#1d0505";
    }
}
