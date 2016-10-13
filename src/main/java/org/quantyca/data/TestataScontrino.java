/**
 * 
 */
package org.quantyca.data;

import java.io.Serializable;
import java.util.Date;

public class TestataScontrino implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5910566318221939817L;
	private String id_scontrino;
	private String store_id;
	private String business_date;
	private Integer pos_id;
	private String transaction_number;
	private String external_reference_id;
	private Float total_amount;
		

	public void createIdScontrino(){
		this.id_scontrino = this.store_id+this.business_date+this.pos_id+this.transaction_number+this.external_reference_id;		
	}
	
	public String getId_scontrino() {
		return id_scontrino;
	}
	public void setId_scontrino(String id_scontrino) {
		this.id_scontrino = id_scontrino;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getBusiness_date() {
		return business_date;
	}
	public void setBusiness_date(String business_date) {
		this.business_date = business_date;
	}
	public Integer getPos_id() {
		return pos_id;
	}
	public void setPos_id(Integer pos_id) {
		this.pos_id = pos_id;
	}
	public String getTransaction_number() {
		return transaction_number;
	}
	public void setTransaction_number(String transaction_number) {
		this.transaction_number = transaction_number;
	}
	public String getExternal_reference_id() {
		return external_reference_id;
	}
	public void setExternal_reference_id(String external_reference_id) {
		this.external_reference_id = external_reference_id;
	}
	public Float getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(Float total_amount) {
		this.total_amount = total_amount;
	}
//	private String xmlParseStartDateTime,xmlParseEndDateTime,hbaseWriteDateTime;
	@Override
	public String toString() {
		return "Testata [id_scontrino=" + id_scontrino + ", store_id=" + store_id + ", business_date=" + business_date
				+ ", pos_id=" + pos_id + ", transaction_number=" + transaction_number + ", external_reference_id="
				+ external_reference_id + ", total_amount=" + total_amount + "]";
	}
	
	
//	public String getXmlParseStartDateTime() {
//		return xmlParseStartDateTime;
//	}
//	public void setXmlParseStartDateTime(String xmlParseStartDateTime) {
//		this.xmlParseStartDateTime = xmlParseStartDateTime;
//	}
//	public String getXmlParseEndDateTime() {
//		return xmlParseEndDateTime;
//	}
//	public void setXmlParseEndDateTime(String xmlParseEndDateTime) {
//		this.xmlParseEndDateTime = xmlParseEndDateTime;
//	}
//	public String getHbaseWriteDateTime() {
//		return hbaseWriteDateTime;
//	}
//	public void setHbaseWriteDateTime(String hbaseWriteDateTime) {
//		this.hbaseWriteDateTime = hbaseWriteDateTime;
//	}
	
	
	
//	@Override
//	public String toString() {
//		return "TestataScontrino [total=" + total + ", storeId=" + storeId + ", startDateTime=" + startDateTime
//				+ ", cardId=" + cardId + ", externalReferenceId=" + externalReferenceId + ", xmlParseStartDateTime="
//				+ xmlParseStartDateTime + ", xmlParseEndDateTime=" + xmlParseEndDateTime + "]";
//	}
	
	
	
}

//StoreID="8017" CashierID="1" TicketTotal="7.00" HomeStore="0" FileSource="1" Locked="0" BusinessDate="2016-08-09" StartDateTime="2016-08-09T11:29:39.108" TransID="7105" PosID="8"
