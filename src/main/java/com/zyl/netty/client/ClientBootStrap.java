package com.zyl.netty.client;

public class ClientBootStrap {
    public static void main(String[] args) {
        String deviceNumberPrefix = "90000000000";
        for (int i = 0; i < 1; i++) {
            new SimulatorSocket(deviceNumberPrefix + i).connection();
        }
    }
}
