package org.quantyca.data;

import java.io.Serializable;

public class RigaScontrino implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7388806129977877256L;
	
	private String id_scontrino;
	private String id_riga;
	private String id_prodotto;
	private Float qta;
	private Float prezzo;
		
	public String getId_scontrino() {
		return id_scontrino;
	}
	public void setId_scontrino(String id_scontrino) {
		this.id_scontrino = id_scontrino;
	}
	public String getId_riga() {
		return id_riga;
	}
	public void setId_riga(String id_riga) {
		this.id_riga = id_riga;
	}
	public String getId_prodotto() {
		return id_prodotto;
	}
	public void setId_prodotto(String id_prodotto) {
		this.id_prodotto = id_prodotto;
	}
	public Float getQta() {
		return qta;
	}
	public void setQta(Float qta) {
		this.qta = qta;
	}
	public Float getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "RigaScontrino [id_scontrino=" + id_scontrino + ", id_riga=" + id_riga + ", id_prodotto=" + id_prodotto
				+ ", qta=" + qta + ", prezzo=" + prezzo + "]";
	}
	
	
	

}
