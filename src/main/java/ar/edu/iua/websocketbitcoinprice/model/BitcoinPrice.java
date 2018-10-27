package ar.edu.iua.websocketbitcoinprice.model;

// Clase de modelo que representa el payload que va a ser transmitido entre los clientes y el servidor
public class BitcoinPrice {
	
    private float priceActual;
    private float pricePrev;
    
	public float getPriceActual() {
		return priceActual;
	}
	
	public void setPriceActual(float priceActual) {
		this.priceActual = priceActual;
	}
	
	public float getPricePrev() {
		return pricePrev;
	}
	
	public void setPricePrev(float pricePrev) {
		this.pricePrev = pricePrev;
	}

}