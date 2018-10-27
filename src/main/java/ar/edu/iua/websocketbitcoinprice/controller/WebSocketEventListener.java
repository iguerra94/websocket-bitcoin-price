package ar.edu.iua.websocketbitcoinprice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// Clase que define escuchadores de eventos para cuando un cliente se conecta o desconecta del servidor websocket, y poder loggear esos eventos y 
// enviarlos a todos los otros clientes conectados.
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    // Escuchador de evento para cuando un nuevo cliente se conecta al servidor websocket
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("A new client has joined the websocket connection");
    }

    // Escuchador de evento para cuando un cliente se desconecta del servidor websocket
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("A client has leaved the websocket connection");
    }
}

