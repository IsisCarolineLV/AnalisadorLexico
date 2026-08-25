import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;

public class Linha{

    private int num;
    private ArrayList<Palavra> palavras;
    private boolean certo;
    private JPanel refLinha;

    private String erro="";

    public Linha (int num, ArrayList<Palavra> palavras){
        this.num=num;
        this.palavras = palavras;
        certo=true;
        for(Palavra p: palavras){
            try{
                p.getTolken();
            }catch (Exception e){
                certo = false;
                erro += e.toString()+ "\n";
            }
        }
    }

    public JPanel gerarLinha(){
        JPanel novaLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        novaLinha.setAlignmentX(Component.LEFT_ALIGNMENT); 
        JLabel numeroLinha = new JLabel(num+"");
        novaLinha.add(numeroLinha);
        novaLinha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for(Palavra p: palavras){
            novaLinha.add(p.criaLabel());
        }
        if(!certo){
            novaLinha.setBackground(Color.decode("#eb7c7c"));
        }
        refLinha = novaLinha;
        return novaLinha;
    }

    public boolean taCerto() {
        return certo;
    }

    public String getErro() {
        return erro;
    }

    public void destacarLinha(){
        refLinha.setBackground(Color.decode("#ee4141"));
    }

    public void normalizaLinha(){
        refLinha.setBackground(Color.decode("#eb7c7c"));
    }

}