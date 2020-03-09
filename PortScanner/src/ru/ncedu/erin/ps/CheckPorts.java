package ru.ncedu.erin.ps;

import java.net.Socket;

public class CheckPorts {
    /**
     * Method checks pointed range of ports on open/close state.
     * @param host Host's name or IP Address.
     * @param fromPort The first port.
     * @param toPort The last port.
     */
    public void checkPorts(String host, int fromPort, int toPort) {
        for(int i = fromPort; i <= toPort; i++) {
            try {
                Socket servSocket = new Socket(host, i);
                System.out.println("Open port: " + i);
                servSocket.close();
            } catch (Exception e)
            {
                System.out.println("Close port: " + i);
            }
        }
    }
}