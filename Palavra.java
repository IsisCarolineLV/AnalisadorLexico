import javax.swing.JLabel;

public class Palavra {
    String lexema;
    JLabel label;
    
    public JLabel criaLabel(){
        JLabel novo = new JLabel(lexema);
        return novo;
    } 
}
