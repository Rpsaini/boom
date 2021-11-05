package com.web.bloomex.communication;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketHandlers {
    public Socket socket;
    public void createConnection() {
           try
           {
            socket = IO.socket("https://whatashot.io:2083/");
            socket.connect();
            socket.on(Socket.EVENT_CONNECT,new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        System.out.println("socket connected==>");
                    } catch (Exception e) {

                    }
                }

            }).on("error", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.connect();

                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.connect();
                    System.out.println("event connection error " + args[0]);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            socket.connect();
                            System.out.println("socket disconnected " + args);
                        }
                    });
//                    .on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//
//                    System.out.println("socket reconnecting " + args);
//                }
//            });
            }
        catch(Exception e) {
            System.out.println("Socket connected event===>"+e.getMessage());
        }
    }

 }
