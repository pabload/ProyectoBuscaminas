
import java.util.ArrayList;

public class sala {

    ArrayList<String> listaJugadores = new ArrayList<>();
    int matriz[][] = new int[5][5];

    public sala(ArrayList<String> listajugadores) {
        this.listaJugadores = listajugadores;
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                matriz[x][y] = (int) (Math.random() * 9 + 1);
            }
        }
    }

    public String mostrarArray() {
        String cadena = "";
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                cadena = cadena + " | " + matriz[x][y] + "|";
                //cadena=cadena+"\n";
                //System.out.print(" | " + matriz[x][y] + " | ");
            }
             cadena=cadena+" \n";
            //return cadena;
        }
        return cadena;
    }

}
