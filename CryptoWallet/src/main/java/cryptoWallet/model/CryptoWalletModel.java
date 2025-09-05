package cryptoWallet.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "crypto_wallet")
public class CryptoWalletModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "CRYPTO_WALLET_ID_GENERATOR", sequenceName = "CRYPTO_WALLET_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CRYPTO_WALLET_ID_GENERATOR")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(name = "btc_amount", precision = 30, scale = 8)
	private BigDecimal btc;
	
	@Column(name = "eth_amount", precision = 30, scale = 8)
	private BigDecimal eth;
	
	@Column(name = "xrp_amount", precision = 30, scale = 8)
	private BigDecimal xrp;
	
	
	public CryptoWalletModel() {
		
	}
	
	public CryptoWalletModel(String email, BigDecimal btc, BigDecimal eth, BigDecimal xrp) {
		this.email = email;
		this.btc = btc;
		this.eth = eth;
		this.xrp = xrp;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getBtc() {
		return btc;
	}

	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}

	public BigDecimal getEth() {
		return eth;
	}

	public void setEth(BigDecimal eth) {
		this.eth = eth;
	}

	public BigDecimal getXrp() {
		return xrp;
	}

	public void setXrp(BigDecimal xrp) {
		this.xrp = xrp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
