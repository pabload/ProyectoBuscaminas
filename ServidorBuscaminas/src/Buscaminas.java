
public class Buscaminas {

    int matriz[][] = new int[5][5];

    public Buscaminas() {
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                matriz[x][y] = (int) (Math.random() * 9 + 1);

            }
        }
    }

    public String mostrarArray() {
        String cadena="";
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                cadena=cadena+" | " + matriz[x][y] + " | ";
                //System.out.print(" | " + matriz[x][y] + " | ");
            }
            cadena=cadena+"\n----------------------------------------";

        }
        return cadena;
    }
}
