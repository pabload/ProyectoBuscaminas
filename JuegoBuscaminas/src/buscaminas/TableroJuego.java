package buscaminas;

/**
 *
 * @author ITLM
 */
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class TableroJuego extends JPanel implements MouseListener { //ActionListener

    private final Color COLOR_MINA = Color.RED;
    private final Color COLOR_BANDERA = Color.YELLOW;
    private final Color COLOR_NO_MINA = Color.ORANGE;
    private final Color COLOR_SI_MINA = Color.GREEN;
    private final String TEXTO_BANDERA = "X";
    private final String TEXTO_MINA = "O";
    private final String TEXTO_SI_MINA = "O";
    private final String TEXTO_NO_MINA = "X";
    private final int SI_BANDERA = 1;
    private final int NO_BANDERA = 0;
    private final int NUMERO_MINAS = 100;
    private int minasRes = NUMERO_MINAS;
    private final int[][] PERIMETRO;
    private String tmp;
    private boolean encontrado = false;
    private int X;
    private int Y;
    private final int ORILLA = 3;
    private int minutos = 0;
    private int segundos = 0;
    private String minutos2 = "";
    private String segundos2 = "";
    private Thread t;
    private boolean estado;
    private final int[][] BANDERAS;
    private final JButton[][] BOTONES;
    private final JLabel texto;
    private final JLabel tiempo;
    private final int TAM_ALTO = 20;
    private final int TAM_ANCHO = 20;
    private final int[][] MINAS;
    private final int FILAS = 30;
    private final int COLUMNAS = 30;
    private final int[] NUM_X = {-1, 0, 1, -1, 1, -1, 0, 1};
    private final int[] NUM_Y = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final double starttime;
    private double endtime;
    private int puntos;
    private boolean j1 = false;

    public TableroJuego(JLabel texto, JLabel tiempo,JButton BotonComenzar) {
        //Nimbus();
        puntos = 0;
        PERIMETRO = new int[FILAS][COLUMNAS];
        BANDERAS = new int[FILAS + 2][COLUMNAS + 2];
        MINAS = new int[FILAS + 2][COLUMNAS + 2];
        BOTONES = new JButton[FILAS][COLUMNAS];
        this.texto = texto;
        this.tiempo = tiempo;
        texto.setText("Banderas Restantes: " + NUMERO_MINAS);
        this.setLayout(new GridLayout(FILAS, COLUMNAS));
        for (int y = 0; y < COLUMNAS + 2; y++) {
            MINAS[0][y] = ORILLA;
            MINAS[FILAS + 1][y] = ORILLA;
            BANDERAS[0][y] = ORILLA;
            BANDERAS[FILAS + 1][y] = ORILLA;
        }
        for (int x = 0; x < FILAS + 2; x++) {
            MINAS[x][0] = ORILLA;
            MINAS[x][COLUMNAS + 1] = ORILLA;
            BANDERAS[x][0] = ORILLA;
            BANDERAS[x][COLUMNAS + 1] = ORILLA;
        }
        for (int y = 1; y < COLUMNAS + 1; y++) {
            for (int x = 1; x < FILAS + 1; x++) {
                MINAS[x][y] = 0;
                BANDERAS[x][y] = 0;
            }
        }
        boolean minasC;
        int filas2;
        int columnas2;
        int contarMinas = 0;
        do {
            minasC = false;
            contarMinas = 0;
            for (int x = 0; x < NUMERO_MINAS; x++) {
                filas2 = (int) (Math.random() * (FILAS) + 1);
                columnas2 = (int) (Math.random() * (COLUMNAS) + 1);
                if (MINAS[filas2][columnas2] == 0) {
                    MINAS[filas2][columnas2] = 1;
                    contarMinas++;
                } else {
                    x--;
                }
            }
            if (contarMinas == NUMERO_MINAS) {
                minasC = true;
            }
        } while (minasC == false);
        System.out.println(contarMinas);
        for (int y = 0; y < COLUMNAS; y++) {
            for (int x = 0; x < FILAS; x++) {
                if ((MINAS[x + 1][y + 1] == 0) || (MINAS[x + 1][y + 1] == 1)) {
                    PERIMETRO[x][y] = checarPerimetro(x, y);
                }
                BOTONES[x][y] = new JButton();
                BOTONES[x][y].setPreferredSize(new Dimension(TAM_ANCHO, TAM_ALTO));
                //BOTONES[x][y].addActionListener(this);
                BOTONES[x][y].addMouseListener(this);
                add(BOTONES[x][y]);
                BOTONES[x][y].setEnabled(true);
            }
        }
        imprimirInfo();
        starttime = System.nanoTime();
        Tiempo();
    }
    
    private void Tiempo() {
        estado = true;
        minutos = 0;
        segundos = 0;
        tiempo.setText("Tiempo: 00:00");
        minutos2 = "";
        segundos2 = "";
        t = new Thread(){
            @Override
            public void run(){
                while (true) {
                    if (estado) {
                        try {
                            sleep(1000);
                            segundos++;
                            if (segundos > 60) {
                                segundos = 0;
                                minutos++;
                            }
                            minutos2 = (minutos < 10) ? ("0" + minutos) : ("" + minutos);
                            segundos2 = (segundos < 10) ? ("0" + segundos) : ("" + segundos);
                            tiempo.setText("Tiempo: " + minutos2 + ":" + segundos2);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        break;
                    }
                }
            }
        };
        t.start();
    }
    
    private void imprimirInfo(){
        for (int y = 1; y < COLUMNAS + 1; y++) {
            for (int x = 1; x < FILAS + 1; x++) {
                System.out.print(MINAS[x][y]);
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int j = 0; j < COLUMNAS; j++) {
            for (int i = 0; i < COLUMNAS; i++) {
                System.out.print(PERIMETRO[i][j]);
            }
            System.out.println("");
        }
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
        encontrado = false;
        JButton boton = (JButton) e.getSource();
        JButton boton2;
        for (int y = 0; y < COLUMNAS; y++) {
            for (int x = 0; x < FILAS; x++) {
                boton2 = BOTONES[x][y];
                if (boton2 == boton) {
                    X = x;
                    Y = y;
                    encontrado = true;
                }
            }
        }
        if (!encontrado) {
            System.out.println("Error");
            System.exit(-1);
        }
        if (BOTONES[X][Y].getBackground() == COLOR_BANDERA) {
            return;
        } else if (MINAS[X + 1][Y + 1] == 1) {
            if (!BOTONES[X][Y].getText().equals("O")) {
                JOptionPane.showMessageDialog(this, "Tiene mina.");
                BOTONES[X][Y].setText("O");
                BOTONES[X][Y].setBackground(COLOR_MINA);
                mostrarMinas();
            }
        } else {
            tmp = "" + PERIMETRO[X][Y];
            if (PERIMETRO[X][Y] == 0) {
                tmp = " ";
            }
            BOTONES[X][Y].setText(tmp);
            BOTONES[X][Y].setEnabled(false);
            checarMinas();
            if (PERIMETRO[X][Y] == 0) {
                buscar(X, Y);
                checarMinas();
            }
        }
    }*/

    public void checarMinas() {
        int checar = 0;
        for (int y = 0; y < COLUMNAS; y++) {
            for (int x = 0; x < FILAS; x++) {
                if (BOTONES[x][y].isEnabled()) {
                    checar++;
                }
            }
        }
        if (checar == NUMERO_MINAS) {
            endtime = System.nanoTime();
            JOptionPane.showMessageDialog(this, "Ganaste en " + (int) ((endtime - starttime) / 1000000000) + " segundos!");
        }
    }

    public void buscar(int x, int y) {
        for (int a = 0; a < 8; a++) {
            if (!(MINAS[x + 1 + NUM_X[a]][y + 1 + NUM_Y[a]] == ORILLA)) {
                if ((PERIMETRO[x + NUM_X[a]][y + NUM_Y[a]] == 0) && (MINAS[x + 1 + NUM_X[a]][y + 1 + NUM_Y[a]] == 0) && (BANDERAS[x + NUM_X[a] + 1][y + NUM_Y[a] + 1] == 0)) {
                    if (BOTONES[x + NUM_X[a]][y + NUM_Y[a]].isEnabled()) {
                        BOTONES[x + NUM_X[a]][y + NUM_Y[a]].setText(" ");
                        BOTONES[x + NUM_X[a]][y + NUM_Y[a]].setEnabled(false);
                        buscar(x + NUM_X[a], y + NUM_Y[a]);
                    }
                } else if ((PERIMETRO[x + NUM_X[a]][y + NUM_Y[a]] != 0) && (MINAS[x + 1 + NUM_X[a]][y + 1 + NUM_Y[a]] == 0) && (BANDERAS[x + NUM_X[a] + 1][y + NUM_Y[a] + 1] == 0)) {
                    tmp = "" + PERIMETRO[x + NUM_X[a]][y + NUM_Y[a]];
                    BOTONES[x + NUM_X[a]][y + NUM_Y[a]].setText("" + PERIMETRO[x + NUM_X[a]][y + NUM_Y[a]]);
                    BOTONES[x + NUM_X[a]][y + NUM_Y[a]].setEnabled(false);
                }
            }
        }
    }
    
    private void mostrarMinas() {
        for (int y = 0; y < COLUMNAS; y++) {
            for (int x = 0; x < FILAS; x++) {
                if (MINAS[x + 1][y + 1] == 0) {
                    if (BOTONES[x][y].getBackground() == COLOR_BANDERA) {
                        BOTONES[x][y].setBackground(COLOR_NO_MINA);
                        BOTONES[x][y].setText(TEXTO_NO_MINA);
                        puntos = puntos - 10;
                    } else {
                        BOTONES[x][y].setText(" ");
                        BOTONES[x][y].setEnabled(false);
                    }
                } else {
                    if (BOTONES[x][y].getBackground() == COLOR_BANDERA) {
                        BOTONES[x][y].setBackground(COLOR_SI_MINA);
                        BOTONES[x][y].setText(TEXTO_SI_MINA);
                        puntos = puntos + 10;
                    } else {
                        BOTONES[x][y].setBackground(COLOR_MINA);
                        BOTONES[x][y].setText(TEXTO_MINA);
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Tus puntos son: " + puntos);
    }

    public int checarPerimetro(int a, int y) {
        int minecount = 0;
        for (int x = 0; x < 8; x++) {
            if (MINAS[a + NUM_X[x] + 1][y + NUM_Y[x] + 1] == 1) {
                minecount++;
            }
        }
        return minecount;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            encontrado = false;
            JButton boton = (JButton) e.getSource();
            if (boton.isEnabled() && !(boton.getBackground() == COLOR_NO_MINA && boton.getText().equalsIgnoreCase(TEXTO_BANDERA)) && !(boton.getBackground() == COLOR_SI_MINA && boton.getText().equalsIgnoreCase(TEXTO_MINA)) ) {
                JButton boton2;
                for (int y = 0; y < COLUMNAS; y++) {
                    for (int x = 0; x < FILAS; x++) {
                        boton2 = BOTONES[x][y];
                        if (boton2 == boton) {
                            X = x;
                            Y = y;
                            encontrado = true;
                        }
                    }
                }
                if(Y == 0){
                    j1 = true;
                }else{
                    if(!j1){
                        JOptionPane.showMessageDialog(null, "Comienza por arriba sisisisisisi");
                    }
                }
                if(j1){
                    if (!encontrado) {
                    System.out.println("Error");
                    System.exit(-1);
                }
                if (BOTONES[X][Y].getBackground() == COLOR_BANDERA) {
                    return;
                } else if (MINAS[X + 1][Y + 1] == 1) {
                    if (!BOTONES[X][Y].getText().equals(TEXTO_MINA)) {
                        JOptionPane.showMessageDialog(this, "Tiene mina.");
                        t.stop();
                        mostrarMinas();
                    }
                } else {
                    tmp = "" + PERIMETRO[X][Y];
                    if (PERIMETRO[X][Y] == 0) {
                        tmp = " ";
                    }
                    BOTONES[X][Y].setText(tmp);
                    BOTONES[X][Y].setEnabled(false);
                    checarMinas();
                    if (PERIMETRO[X][Y] == 0) {
                        buscar(X, Y);
                        checarMinas();
                    }
                }
                }
                
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            encontrado = false;
            JButton boton = (JButton) e.getSource();
            JButton boton2;
            if (boton.isEnabled() && !(boton.getBackground() == COLOR_NO_MINA && boton.getText().equalsIgnoreCase(TEXTO_BANDERA)) && !(boton.getBackground() == COLOR_SI_MINA && boton.getText().equalsIgnoreCase(TEXTO_MINA))) {
                for (int y = 0; y < COLUMNAS; y++) {
                    for (int x = 0; x < FILAS; x++) {
                        boton2 = BOTONES[x][y];
                        if (boton2 == boton) {
                            X = x;
                            Y = y;
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    System.out.println("Error");
                    System.exit(-1);
                }
                if ((BANDERAS[X + 1][Y + 1] == NO_BANDERA) && (BOTONES[X][Y].isEnabled()) && !(BOTONES[X][Y]).getText().equals(TEXTO_MINA)) {
                    BOTONES[X][Y].setText(TEXTO_BANDERA);
                    minasRes--;
                    texto.setText("Banderas Restantes: " + minasRes);
                    BANDERAS[X + 1][Y + 1] = SI_BANDERA;
                    BOTONES[X][Y].setBackground(COLOR_BANDERA);
                } else if (BANDERAS[X + 1][Y + 1] == SI_BANDERA && !(BOTONES[X][Y]).getText().equals(TEXTO_MINA)) {
                    BOTONES[X][Y].setText(" ");
                    minasRes++;
                    texto.setText("Banderas Restantes: " + minasRes);
                    BANDERAS[X + 1][Y + 1] = NO_BANDERA;
                    BOTONES[X][Y].setBackground(null);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }
    
    public void Nimbus(){
        try{
          UIManager.setLookAndFeel(new NimbusLookAndFeel());  
        }catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

}
