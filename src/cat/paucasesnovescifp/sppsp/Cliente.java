package cat.paucasesnovescifp.sppsp;

import cat.paucasesnovescifp.sppsp.utilidades.Mensaje;
import org.joda.time.DateTimeZone;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.Set;

public class Cliente {

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente();
            cliente.enviar();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Mensaje crearMensaje() {
        Scanner scanner = new Scanner(System.in);
        String texto;
        String textoCorrecto;
        Mensaje mensaje = null;
        int contador = 0;

            System.out.println("Introduce la ciudad: ");
            texto = scanner.nextLine();
            textoCorrecto = texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();

            Set<String> zonas = DateTimeZone.getAvailableIDs();
            for (String s : zonas) {
                if (s.contains(textoCorrecto)) {
                    contador++;
                }
            }
            if (contador == 0) {
                System.out.println("No tenemos una zona horaria para esta ciudad.");
                System.out.println("Introduzca su hora correspondiente: ");
                mensaje = new Mensaje("Cliente", "Servidor", textoCorrecto + " " + scanner.nextLine(), true);
            } else {
                mensaje = new Mensaje("Cliente", "Servidor", textoCorrecto);
            }

        return mensaje;
    }

    private void enviar() throws IOException, ClassNotFoundException {
        // Ip para la conexión
        InetAddress ip = InetAddress.getByName("127.0.0.1");

        // Inicializamos el socket
        DatagramSocket socket = new DatagramSocket();

        // Pedimos al cliente el la ciudad que quiere
        Mensaje mensaje = crearMensaje();

        // Convertimos el objeto a byte
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(mensaje);
        out.close();
        byte[] personaByte = baos.toByteArray();

        // Inicializamos el paquete que queremos enviar
        DatagramPacket paqueteEnviar = new DatagramPacket(personaByte,personaByte.length,ip,3000);

        // Enviamos el paquete
        System.out.println("El cliente envia lo siguiente: \n");
        System.out.println(mensaje);
        socket.send(paqueteEnviar);

        // Cerramos el socket
        socket.close();

        // Escuchar respuesta del servidor
        recibir();
    }

    private void recibir() throws IOException, ClassNotFoundException {
        System.out.println("____________________________________________________________");
        System.out.println("El cliente espera la respuesta del servidor.");
        System.out.println("____________________________________________________________\n");
        DatagramSocket socketRecibir = new DatagramSocket(3001);
        byte[] personaByte = new byte[1024];
        DatagramPacket paqueteRecibir = new DatagramPacket(personaByte, personaByte.length);
        socketRecibir.receive(paqueteRecibir);

        // cal convertir array de bytes a objecte
        ByteArrayInputStream bais = new ByteArrayInputStream(personaByte);
        ObjectInputStream in = new ObjectInputStream(bais);
        Mensaje mensaje = (Mensaje) in.readObject();

        System.out.println("Cliente recibe lo siguiente: \n");
        System.out.println(mensaje);

        in.close();
        socketRecibir.close();
    }

}
