
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
    private static JScrollPane scrollPane;

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
        JFrame frame = new JFrame("Analisador de Código");
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
        JLabel lblTitulo = new JLabel("Arquivo: nenhum arquivo selecionado");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ScrollPane
        JTextArea areaConteudo = new JTextArea("O conteudo do arquivo aparecera aqui...");
        scrollPane = new JScrollPane(areaConteudo);
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
            chamarTelaPesquisa();
            System.out.println("Caminho lido:" +caminhoAtual);
        });

        btnAnalisar.addActionListener(e->{
            if(caminhoAtual.equals("")){
                areaErro.setText("Nenhum arquivo selecionado!");
            }else{
                String texto = getTexto();
                areaConteudo.setText(texto);
                //analisador = new AnalisadorLexico(texto);
                //ArrayList<Linha> linhas = analisador.classificarTolkens();
                //imprimirArquivo(linhas);
                //mostraErro(linhas, 0);
            }
            
        });
    }
    

    private static void mostraErro(ArrayList<Linha> linhas, int i) {
        if (i >= 0 && i < linhas.size()) {
            areaErro.setText(linhas.get(i).getErro());
            btnErroAnterior.setEnabled(i > 0);
            btnProximoErro.setEnabled(i < linhas.size() - 1);
        }
    }

    public static void chamarTelaPesquisa() {
        JFileChooser fileChooser = new JFileChooser();

        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();

            caminhoAtual = arquivo.getAbsolutePath();
        }
    }

    public static String getTexto(){
        String texto= "";
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoAtual))) {

            String linha;

            while ((linha = br.readLine()) != null) {
                texto += linha +"\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto;
    }

    public static void imprimirArquivo(ArrayList<Linha> linhas){

    }
}