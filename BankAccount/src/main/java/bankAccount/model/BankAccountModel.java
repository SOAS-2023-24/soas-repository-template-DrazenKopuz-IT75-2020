package bankAccount.model;

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
@Table(name = "bank_account")
public class BankAccountModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "BANK_ACCOUNT_ID_GENERATOR", sequenceName = "BANK_ACCOUNT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANK_ACCOUNT_ID_GENERATOR")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(name = "rsd_amount", precision = 30, scale = 8)
	private BigDecimal rsd;
	
	@Column(name = "eur_amount", precision = 30, scale = 8)
	private BigDecimal eur;
	
	@Column(name = "usd_amount", precision = 30, scale = 8)
	private BigDecimal usd;
	
	@Column(name = "chf_amount", precision = 30, scale = 8)
	private BigDecimal chf;
	
	@Column(name = "gbp_amount", precision = 30, scale = 8)
	private BigDecimal gbp;
	
	public BankAccountModel() {
		
	}
	
	public BankAccountModel(String email, BigDecimal rsd, BigDecimal eur, BigDecimal usd, BigDecimal chf, BigDecimal gbp) {
		this.email = email;
		this.rsd = rsd;
		this.eur = eur;
		this.usd = usd;
		this.chf = chf;
		this.gbp = gbp;
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

	public BigDecimal getRsd() {
		return rsd;
	}

	public void setRsd(BigDecimal rsd) {
		this.rsd = rsd;
	}

	public BigDecimal getEur() {
		return eur;
	}

	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}

	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public BigDecimal getChf() {
		return chf;
	}

	public void setChf(BigDecimal chf) {
		this.chf = chf;
	}

	public BigDecimal getGbp() {
		return gbp;
	}

	public void setGbp(BigDecimal gbp) {
		this.gbp = gbp;
	}

}
