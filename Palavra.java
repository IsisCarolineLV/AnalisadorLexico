import java.awt.Color;
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

            novo.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    novo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    novo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            });

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
        if(token.equals("palavra reservada")) return "#e3c502";
        if(token.equals("comentario")) return "#0e8b19";
        if(token.equals("string")) return "#fc8211";
        if(token.equals("char")) return "#f69b47";
        if(token.equals("inteiro") || 
        token.equals("flutuante") ||
        token.equals("notacao cientifica") ||
        token.equals("caractere especial")) return "#8400a8";
        return "#1d0505";
    }
}
