package cat.paucasesnovescifp.sppsp.utilidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje implements Serializable {

    private String emisor;
    private String receptor;
    private String fecha;
    private String contenido;
    private boolean nuevo;

    public Mensaje(String emisor, String receptor, String contenido) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.setFecha();
        this.contenido = contenido;
        this.nuevo = false;
    }

    public Mensaje(String emisor, String receptor, String contenido, boolean nuevo) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.setFecha();
        this.contenido = contenido;
        this.nuevo = nuevo;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getFecha() {
        return fecha;
    }

    private void setFecha() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.fecha = dtf.format(now);
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    @Override
    public String toString() {
        return "mensaje { \n" +
                "emisor = " + emisor + "\n" +
                "receptor = " + receptor + "\n" +
                "fecha = " + fecha + "\n" +
                "contenido = " + contenido + "\n" +
                "}\n";
    }
}
