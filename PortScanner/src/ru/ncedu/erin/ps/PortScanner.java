package ru.ncedu.erin.ps;

public class PortScanner {
    /**
     * The main method which calls "CheckPorts".
     * @param args Input arguments.
     */
    public static void main(String args[]){
        String address = args[0];
        int startPort = Integer.parseInt(args[1]);
        int endPort = Integer.parseInt(args[2]);
        new CheckPorts().checkPorts(address, startPort, endPort); 
    }
}