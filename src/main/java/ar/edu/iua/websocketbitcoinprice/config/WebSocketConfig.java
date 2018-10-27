package ar.edu.iua.websocketbitcoinprice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // Archivo de configuracion de WS
@EnableWebSocketMessageBroker // Habilita el servidor de websocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	// Metodos para configurar la conexion websocket
	
	// Se registra el endpoint (/ws) donde los clientes se conectaran al servidor websocket
	// La opcion withSockJS() habilita opciones alternativas en caso de que el navegador no soporte websockets 
	// STOMP => Simple Text Oriented Messaging Protocol, define el formato y las reglas para el intercambio de datos o mensajes
	// Mandar mensajes solo a los clientes suscriptos a un determinado canal o tema, o mandar un mensaje a un cliente en particular
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    // Se configura el Broker de mensajeria (Intermediario) que sera usado para enrutar los mensajes de un cliente a otro
    // Los mensajes cuyo destino empiecen con /app van a ser manejados por determinados metodos manejadores (que se definen en el controlador).
    // Los mensajes cuyo destino empiecen con /trades van a ser enrutados al Broker de mensajeria. Este ultimo envia los mensajes a todos los clientes 
    // conectados que estan subscriptos a un canal particular (/trades)
    // Se usa un broker de mensajeria simple (Otras opciones son RabbitMQ o ActiveMQ)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/trades");
    }
}

