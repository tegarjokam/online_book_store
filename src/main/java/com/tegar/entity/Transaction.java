package com.tegar.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@Where(clause = "status = 'ACTIVE'")
public class Transaction extends Persistence{
	private static final long serialVersionUID = 9220882551539786922L;

	public enum TransactionStatus {
		PENDING, SETTLED, EXPIRED
	}
	
	public enum PaymentMethod {
		BANK_TRANSFER, CASH_ON_DELIVERY, VIRTUAL_ACCOUNT
	}
	
	@Column(unique = true)
	private String invoiceNumber;
	
	@Column(length = 255)
	private String receiptImageUrl;
	
	@Column 
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date paymentTime;
	
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;
	
	@JoinColumn(name = "user_id")
	@OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	private User user;
	
	@Where(clause = "status = 'ACTIVE'")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "transaction", fetch = FetchType.LAZY)
	private Set<TransactionDetail> transactionDetails;
}
