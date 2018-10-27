package ar.edu.iua.websocketbitcoinprice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import ar.edu.iua.websocketbitcoinprice.model.BitcoinPrice;

// Se definen los metodos manejadores de mensajes
// Son responsables de recibir los mensajes desde un cliente y mandarselos a todos los otros conectados a ese canal.
@Controller
public class BitcoinPriceController {
	
	// Un mensaje con el destino /app/trade.sendMessage va a ser manejado por el metodo sendMessage()
	// El resultado de este metodo va a ser enviado de vuelta al canal /trades/btcusd
    @MessageMapping("/trade.sendMessage")
    @SendTo("/trades/btcusd")
    public BitcoinPrice sendMessage(@Payload BitcoinPrice bitcoinPrice) {
        return bitcoinPrice;
    }

}