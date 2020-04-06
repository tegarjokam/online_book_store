package com.tegar.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_detail")
@Where(clause = "status = 'ACTIVE'")
public class TransactionDetail extends Persistence {
	private static final long serialVersionUID = -1695120839903033361L;
	
	@Column(columnDefinition = "double precision default '0'")
	private Double price;
	
	@JoinColumn(name = "book_id")
	@ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
	private Book book;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id_detail", referencedColumnName = "id")
	private CartDetail cartDetail;
	
	@JoinColumn(name = "transaction_id")
	@ManyToOne(targetEntity = Transaction.class, fetch = FetchType.LAZY)
	private Transaction transaction;
}
