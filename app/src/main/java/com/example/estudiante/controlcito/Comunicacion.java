package com.example.estudiante.controlcito;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Created by estudiante on 16/02/17.
 */

public class Comunicacion extends Observable implements Runnable {

    private static Comunicacion ref;
    public static final String DEFAULT_ADDRESS = "10.0.2.2";
    public static final String MULTI_ADDRESS = "224.3.3.3";
    public static final int PUERTO = 5000;

    private MulticastSocket multi;
    private boolean running;
    private boolean connecting;
    private boolean reset;

    private Comunicacion(){
        running = true;
        connecting = true;
        reset = false;
    }

    public static Comunicacion getInstance(){
        if (ref == null) {
            ref = new Comunicacion();
            Thread hilo = new Thread(ref);
            hilo.start();
        }
        return ref;
    }

    @Override
    public void run() {
        while (running) {
            if (connecting) {
                if (reset) {
                    if (multi != null) {
                        multi.close();
                    }
                    reset = false;
                }
                connecting = !intento();
            } else {
                if (multi != null) {
                    DatagramPacket p = recibir();

                    // Validate that there are no errors with the data
                    if (p != null) {
                        // Transform packet bytes to understandable data
                        String message = new String(p.getData(), 0, p.getLength());

                        // Notify the observers that new data has arrived and pass the data to them
                        setChanged();
                        notifyObservers(message);
                        clearChanged();
                    }
                }
            }
        }
        multi.close();
    }


    private boolean intento(){
        try {
            multi = new MulticastSocket();
            setChanged();
            notifyObservers("Conexión Iniciada");
            clearChanged();
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public void enviar(final String mensaje, final String destino, final int puerto){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(multi != null){
                    try {
                        InetAddress ia = InetAddress.getByName(destino);
                        multi.joinGroup(ia);
                        byte[] data = mensaje.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, data.length, puerto);
                        multi.send(packet);

                    } catch (UnknownHostException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    setChanged();
                    notifyObservers("Sin Conexión");
                    clearChanged();
                }
            }
        }).start();

    }

    public DatagramPacket recibir(){
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            multi.receive(packet);
            return packet;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
