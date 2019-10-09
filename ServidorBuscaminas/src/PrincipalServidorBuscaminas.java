
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class PrincipalServidorBuscaminas {
    private static Map<String, jugador> jugadores = new HashMap<>();
    private static Map<Integer, sala> salas = new HashMap<>();
    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            System.out.println(listener.getInetAddress());
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }

        }

    }

    public static class Handler implements Runnable {

        private int NumeroJugador;
        private int numerosala;
        private String nombre;
        private Socket socket;
        private PrintWriter Escritor;
        private Scanner Entrada;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {

                Entrada = new Scanner(socket.getInputStream());
                Escritor = new PrintWriter(socket.getOutputStream(), true);
                Escritor.println("NOMBREDEENVIO");
                nombre=Entrada.nextLine();
                while (true) {
                    synchronized (jugadores) {
                        if (jugadores.size()==0) {
                            System.out.println("entro alguien xd");
                            NumeroJugador=1;
                            System.out.println("tu numero de jugador es"+NumeroJugador);
                            numerosala=1;
                            jugador j = new jugador(numerosala, NumeroJugador, Escritor);
                            jugadores.put(nombre, j);
                            ArrayList<String> nuevasala =new ArrayList<>();
                            sala s = new sala(nuevasala);
                            nuevasala.add(nombre);
                            salas.put(1,s);
                            System.out.println(s.mostrarArray());
                            Escritor.println("MENSAJE "+s.mostrarArray());
                            break;
                        }else{
                            if (salas.get(salas.size()).listaJugadores.size()<4) {
                                NumeroJugador=salas.get(salas.size()).listaJugadores.size()+1;
                                System.out.println("tu numero de jugador es: "+NumeroJugador);
                                numerosala=salas.size();
                                System.out.println("numerosala"+salas.size());
                                System.out.println("tu numero de jugador es : "+NumeroJugador);
                                jugador j = new jugador(numerosala, NumeroJugador, Escritor);
                                salas.get(salas.size()).listaJugadores.add(nombre);
                                jugadores.put(nombre, j);
                                Escritor.println("MENSAJE "+salas.get(numerosala).mostrarArray());
                               break;
                            }else{
                                System.out.println("nueva sala");
                                NumeroJugador=1;
                                numerosala=salas.size()+1;
                                System.out.println("numero de sala: "+numerosala);
                                jugador j =new jugador(numerosala, NumeroJugador, Escritor);
                                ArrayList<String> nuevasala =new ArrayList<>();
                                sala s = new sala(nuevasala);
                                salas.put(numerosala, s);
                                nuevasala.add(nombre);
                                jugadores.put(nombre, j);
                                Escritor.println("MENSAJE "+s.mostrarArray());
                                break;
                            }
                        }
                       
                    }
                }
                while (true) {
                    String input = Entrada.nextLine();
                    if (input.startsWith("/") && !input.startsWith("/salir") && !input.startsWith("/bloquear") && !input.startsWith("/desbloquear")) {
                        //MensajePrivado(input);
                    } else {
                        if (input.toLowerCase().startsWith("/salir")) {
                            return;
                        } else {
                            if (input.toLowerCase().startsWith("/bloquear")) {
                                //bloquear(input);
                            } else {
                                if (input.toLowerCase().startsWith("/desbloquear")) {
                                    //desbloquear(input);
                                } else {
                                    EnviarMensaje(input);

                                }

                            }
                        }

                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
               if (Escritor != null || nombre != null) {
                   jugadores.remove(nombre);
                   salas.get(numerosala).listaJugadores.remove(nombre);
                   if (salas.get(numerosala).listaJugadores.isEmpty()) {
                       System.out.println("borro la sala");
                       salas.remove(numerosala);
                   }
                   for (Map.Entry<String, jugador> entry : jugadores.entrySet()) {
                       String key = entry.getKey();
                       jugador value = entry.getValue();
                       if (value.sala==numerosala&&(!nombre.equals(key))) {
                           value.Escritor.println("MESSAGE " + nombre + " ha salido");
                       }
                       
                   }
                   
                }
                try {
                    socket.close();
                } catch (Exception e) {
                }
            }

        }
        
        public void EnviarMensaje(String input) {
            if (!input.equals("")) {
                for (Map.Entry<String, jugador> jugador : jugadores.entrySet()) {
                    String nombrejugador = jugador.getKey();
                    jugador informacionJugador = jugador.getValue();
                    if (informacionJugador.sala==numerosala) {
                        informacionJugador.Escritor.println("MENSAJE "+NumeroJugador+": "+input);
                         System.out.println("este vato esta en sala: "+numerosala);
                    System.out.println("sala del vato a enviar: "+informacionJugador.sala);
                    System.out.println("sala del vato a que envia : "+numerosala);
                    }

                }
            }
        }
    }
}
