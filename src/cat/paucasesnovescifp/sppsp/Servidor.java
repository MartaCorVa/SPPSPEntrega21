package cat.paucasesnovescifp.sppsp;

import cat.paucasesnovescifp.sppsp.utilidades.Mensaje;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

public class Servidor {

    private Mensaje mensajeEnviar = null;
    private Mensaje mensajeRecibir = null;

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            servidor.recibir();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void recibir() throws IOException, ClassNotFoundException {
        System.out.println("____________________________________________________________");
        System.out.println("Servidor a la espera de que entren mensajes.");
        System.out.println("____________________________________________________________\n");
        DatagramSocket socket = new DatagramSocket(3000);
        byte[] personaByte = new byte[1024];
        DatagramPacket packet = new DatagramPacket(personaByte, personaByte.length);
        socket.receive(packet);

        // cal convertir array de bytes a objecte
        ByteArrayInputStream bais = new ByteArrayInputStream(personaByte);
        ObjectInputStream in = new ObjectInputStream(bais);
        mensajeRecibir = (Mensaje) in.readObject();

        System.out.println("El servidor recibe lo siguiente: \n");
        System.out.println(mensajeRecibir);

        in.close();
        socket.close();
        enviar(mensajeRecibir.getContenido());
    }

    private void enviar(String ciudad) throws IOException, ClassNotFoundException {
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        DatagramSocket socket = new DatagramSocket();

        if (mensajeRecibir.isNuevo()){
            mensajeEnviar = new Mensaje("Servidor", "Cliente", mensajeRecibir.getContenido());
        } else {
            // Conseguimos la zona horaria de la ciudad
            Set<String> zonas = DateTimeZone.getAvailableIDs();
            for (String s : zonas) {
                if (s.contains(ciudad)){
                    DateTime dateTime = DateTime.now(DateTimeZone.forID(s));
                    String hora = dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour() + ":" + dateTime.getSecondOfMinute();
                    mensajeEnviar = new Mensaje("Sevidor", "Cliente", hora);
                }
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(mensajeEnviar);
        outputStream.close();
        byte[] personaByte = byteArrayOutputStream.toByteArray();

        DatagramPacket datagramPacket = new DatagramPacket(personaByte, personaByte.length, ip, 3001);

        System.out.println("Servidor envia lo siguiente: \n");
        System.out.println(mensajeEnviar);

        socket.send(datagramPacket);
        socket.close();
        recibir();
    }

}
