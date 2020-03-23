package com.tegar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@MappedSuperclass
public class Persistence {
	
	public enum Status {
		ACTIVE, NOT_ACTIVE
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Column(length = 50)
	@CreatedBy
	private String createdBy;
	
	@Column
	@LastModifiedBy
	private String updateBy;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdTime;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updateTime;
	
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@PrePersist
	public void prePersist() {
		setCreatedTime(new Date());
		setUpdateTime(new Date());
		setStatus(Status.ACTIVE);
		setCreatedBy("system");
	}
	
	@PreUpdate
	public void preUpdate() {
		setUpdateTime(new Date());
		setUpdateBy("system");
	}
}
