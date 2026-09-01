import java.awt.Component;
import java.awt.Dimension;
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

    private String erro;

    public Linha (int num, ArrayList<Palavra> palavras){
        this.num=num;
        this.palavras = palavras;
        certo=true;
        erro="";
    }
    
    public void setErro(String erro){
        certo=false;
        this.erro += erro;
    }
    public JPanel gerarLinha(){
        JPanel novaLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)); 
        novaLinha.setAlignmentX(Component.LEFT_ALIGNMENT); 
        JLabel numeroLinha = new JLabel(num+"- ");
        novaLinha.add(numeroLinha);
        novaLinha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for(Palavra p: palavras){
            novaLinha.add(p.criaLabel());
            
        }
        if(!certo){
            novaLinha.setBackground(Color.decode("#eb7c7c"));
        }

        Dimension tamanho = novaLinha.getPreferredSize();
        novaLinha.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, tamanho.height)
        );

        refLinha = novaLinha;
        return novaLinha;
    }

    public boolean taCerto() {
        return certo;
    }

    public String getErro() {
        return erro;
    }

    public int getNum(){
        return num;
    }

    public void destacarLinha(){
        refLinha.setBackground(Color.decode("#ee4141"));
        refLinha.scrollRectToVisible(new java.awt.Rectangle(0, 0, 10, refLinha.getHeight()));
    }

    public void normalizaLinha(){
        refLinha.setBackground(Color.decode("#eb7c7c"));
    }

}