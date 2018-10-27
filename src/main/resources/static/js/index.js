'use strict';

let stompClient = null;
let pusher = null;
let tradesBTCUSDChannel = null;
let pricePrev = null;

const connectingElement = document.querySelector('.connecting');
const priceElement = document.getElementById('btcusd');

const priceValueElement = document.getElementById('price_value');
const arrowElement = document.getElementById('arrow');

// Funcion que se dispara al cargar la ventana
// Utiliza la libreria SockJS y el cliente STOMP 
// para conectarse al endpoint "/ws" que fue configurado en Spring.

// al metodo connect del cliente STOMP se le pasan dos funciones callbacks
// una para cuando la conexion es exitosa y otra cuando se produce un error
// Tambien se utiliza el cliente pusher para establecer una conexion usando la APP_KEY pasada como parametro en el constructor. 
// Pusher expone API's para comunicacion en tiempo real entre clientes y servidores.
function connect(event) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

    pusher = new Pusher('de504dc5763aeef9ff52');
}


// Funcion que se ejecuta si la conexion es exitosa
// El cliente stomp se subscribe al canal /trades/btcusd, y una vez subscripto pasa como callback una funcion (onMessageReceived)
// Tambien el cliente pusher se subscribe al canal "live_trades" que publica mensajes en el mismo referidos a intercambios entre 
// criptomonedas y su cambio a una moneda fiat (dolar, euro) 
function onConnected() {
    // Subscribe to the BTCUSD Trades Channel
    stompClient.subscribe('/trades/btcusd', onMessageReceived);

    tradesBTCUSDChannel = pusher.subscribe('live_trades');

    sendMessage();

    connectingElement.classList.add('hidden');
    priceElement.classList.remove('hidden');
}

// Funcion que se ejecuta si hay un error cuando se establece la conexion con el servidor websocket
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// El cliente STOMP publica un mensaje con el destino "/app/trade.sendMessage", mandando el payload obtenido del canal live_trades 
// de pusher, cuando ocurre un evento de intercambio (trade).
// El payload contiene el valor del precio actual del bitcoin, y el valor del precio previo al actual 
function sendMessage(event) {
    tradesBTCUSDChannel.bind('trade', function (data) {
        let bitcoinPrice = {
            pricePrev,
            priceActual: data.price
        };

        stompClient.send("/app/trade.sendMessage", {}, JSON.stringify(bitcoinPrice));

	pricePrev = bitcoinPrice.priceActual;
    });
}

// Funcion que se ejecuta cuando algun cliente o el servidor publica mensajes en el canal "/trades/btcusd"
// Determina si el valor actual del precio del bitcoin es mayor o menor que el valor previo y 
// de acuerdo a eso, muestra un mensaje con fondo verde, rojo o gris, 
// dependiendo si es mayor, menor o igual el valor actual del precio del bitcoin con respecto al anterior.  
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    console.log(message);

    priceValueElement.textContent = "1 BTC @ USD " + message.priceActual;

    if (message.pricePrev == 0 || message.priceActual == message.pricePrev) {
	priceValueElement.classList.add("equal");
	priceValueElement.classList.remove("up");
	priceValueElement.classList.remove("down");
	
	arrowElement.textContent = "−";
	arrowElement.classList.remove("price-up");
	arrowElement.classList.remove("price-down");
	arrowElement.classList.add("price-equal");
    } else {
        if (message.priceActual > message.pricePrev) {
  	    priceValueElement.classList.add("up");
	    priceValueElement.classList.remove("equal");
	    priceValueElement.classList.remove("down");
            
	    arrowElement.textContent = "▲";
	    arrowElement.classList.remove("price-down");
	    arrowElement.classList.remove("price-equal");
	    arrowElement.classList.add("price-up");
	} else {
  	    priceValueElement.classList.add("down");
	    priceValueElement.classList.remove("up");
	    priceValueElement.classList.remove("equal");

            arrowElement.textContent = "▼";
	    arrowElement.classList.remove("price-up");
	    arrowElement.classList.remove("price-equal");
	    arrowElement.classList.add("price-down");
	} 
    }
}

// Registro el event listener para escuchar el evento de carga de la pagina, y le paso como callback la funcion connect.
window.addEventListener('load', connect);