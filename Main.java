
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main{
    private static String caminhoAtual="";
    private static AnalisadorLexico analisador;
    private static JLabel areaErro;
    private static JPanel areaConteudo;
    private static JLabel lblTitulo;

    private static StringBuilder texto;
    private static ArrayList<Linha> linhasComErro;
    private static int contErro=0;

    //botoes:
    private static JButton btnPesquisar;
    private static JButton btnAnalisar;
    private static JButton btnErroAnterior;
    private static JButton btnProximoErro;
    
    public static void main(String[] args){
        //criarTela();
        SwingUtilities.invokeLater(Main::criarTela);
    } 

    public static void criarTela() {
        // Tela principal
        JFrame frame = new JFrame("Analisador Lexico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Organizador Vertical
        JPanel organizadorV = new JPanel();
        organizadorV.setLayout(new BoxLayout(organizadorV, BoxLayout.Y_AXIS));
        organizadorV.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Organizador Horizontal (Painel para os botoes)
        JPanel organizadorH = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        organizadorH.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        btnPesquisar = new JButton("Pesquisar");
        btnAnalisar = new JButton("Analisar");
        btnAnalisar.setEnabled(false);

        btnErroAnterior = new JButton("⬆ Erro Anterior");
        btnErroAnterior.setEnabled(false);
            
        btnProximoErro = new JButton("⬇ Proximo Erro");
        btnProximoErro.setEnabled(false);

        organizadorH.add(btnPesquisar);
        organizadorH.add(Box.createRigidArea(new Dimension(5, 0))); 
        organizadorH.add(btnAnalisar);
        organizadorH.add(Box.createRigidArea(new Dimension(5, 0)));
        organizadorH.add(btnErroAnterior);
        organizadorH.add(Box.createRigidArea(new Dimension(5, 0)));
        organizadorH.add(btnProximoErro);
        
        organizadorH.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // titulo do arquivo
        lblTitulo = new JLabel("Arquivo: nenhum arquivo selecionado");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ScrollPane
        areaConteudo = new JPanel();
        areaConteudo.setLayout(new BoxLayout(areaConteudo, BoxLayout.Y_AXIS));
        areaConteudo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(areaConteudo);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Pane normal para aparecer o erro
        JPanel paneErro = new JPanel(new BorderLayout());
        paneErro.setBorder(BorderFactory.createTitledBorder("Painel de Erro"));
        areaErro = new JLabel("Nenhum erro encontrado.");
        areaErro.setForeground(Color.RED);
        paneErro.add(new JScrollPane(areaErro), BorderLayout.CENTER);
        
        paneErro.setPreferredSize(new Dimension(800, 120));
        paneErro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        paneErro.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Montando as peças no Organizador Vertical
        organizadorV.add(organizadorH);
        organizadorV.add(Box.createRigidArea(new Dimension(0, 10)));
        organizadorV.add(lblTitulo);
        organizadorV.add(Box.createRigidArea(new Dimension(0, 10)));
        organizadorV.add(scrollPane);
        organizadorV.add(Box.createRigidArea(new Dimension(0, 10)));
        organizadorV.add(paneErro);

        frame.add(organizadorV);
        frame.setVisible(true);

        //listeners
        btnPesquisar.addActionListener(e ->{
            String t = chamarTelaPesquisa();
            if(!t.equals("nenhum arquivo selecionado")){
                areaConteudo.removeAll();
                lblTitulo.setText("Arquivo: "+t);
                //System.out.println("Caminho lido:" +caminhoAtual);
                texto = getTexto();
            }
            btnAnalisar.setEnabled(true);
        });

        btnAnalisar.addActionListener(e->{
            if(caminhoAtual.equals("")){
                areaErro.setText("Nenhum arquivo selecionado!");
            }else{
                analisador = new AnalisadorLexico(texto);
                ArrayList<Linha> linhas = analisador.classificarTolkens();
                linhasComErro = analisador.getLinhasErradas();
                imprimirArquivo(linhas);
                contErro=0;
                
                if(linhasComErro.size()>=1){
                    btnErroAnterior.setEnabled(true);
                    btnProximoErro.setEnabled(true);
                    mostraErro(linhasComErro, 0);
                }
            }
            
        });

        btnProximoErro.addActionListener(e->{
            contErro++;
            mostraErro(linhasComErro, contErro);
        });

        btnErroAnterior.addActionListener(e->{
            contErro--;
            mostraErro(linhasComErro, contErro);
        });
    }
    

    private static void mostraErro(ArrayList<Linha> linhas, int i) {
        if (i >= 0 && i < linhas.size() && linhas.size()>0) {
            System.out.println("OIIIIIIIIIi");
            areaErro.setText(linhas.get(i).getErro());
            linhas.get(i).destacarLinha();  //destaca linha atual
            if(i>0) linhas.get(i-1).normalizaLinha();   //retorna a anteriormente destacada para a cor normal
            if(i<linhas.size()-1) linhas.get(i+1).normalizaLinha();   //retorna a anteriormente destacada para a cor normal
            btnErroAnterior.setEnabled(i > 0);
            btnProximoErro.setEnabled(i < linhas.size() - 1);
        }
    }

    public static String chamarTelaPesquisa() {
        JFileChooser fileChooser = new JFileChooser();

        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();

            caminhoAtual = arquivo.getAbsolutePath();

            return fileChooser.getSelectedFile().getName();
        }

        return "nenhum arquivo selecionado";
        
    }

    public static StringBuilder getTexto(){
        StringBuilder texto= new StringBuilder("");
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoAtual))) {

            String linha;
            int cont=1;
            while ((linha = br.readLine()) != null) {
                areaConteudo.add(gerarLinha(cont++, linha));
                texto.append( linha +"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        areaConteudo.revalidate();
        areaConteudo.repaint();

        return texto;
    }

    public static void imprimirArquivo(ArrayList<Linha> linhas){
        areaConteudo.removeAll();
        for(Linha l: linhas){
            areaConteudo.add(l.gerarLinha());
        }
    }

    public static JPanel gerarLinha(int num, String conteudo){
        JPanel novaLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel numeroLinha = new JLabel(String.valueOf(num));
        JLabel texto = new JLabel(conteudo);

        novaLinha.add(numeroLinha);
        novaLinha.add(texto);

        novaLinha.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension tamanho = novaLinha.getPreferredSize();
        novaLinha.setMaximumSize(new Dimension(Integer.MAX_VALUE, tamanho.height));
        
        return novaLinha;

    }
}