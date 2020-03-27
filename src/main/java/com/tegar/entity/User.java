package com.tegar.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
@Where(clause = "status = 'ACTIVE'")
public class User extends Persistence implements Serializable{
	
	private static final long serialVersionUID = 4457669404205697511L;
	
	@NotNull
	@Size(min = 5, max = 50) 
	@Column(length = 50, unique = true) 
	private String username;
	
	@JsonIgnore
	@NotNull
	@Size(min = 8)
	@Column
	private String password;
	
	@NotNull
	private String fullName;
	
	@Column(length = 100) 
	private String email;
	
	@NotNull
	@Column(length = 25) 
	private String phoneNumber;
	
	@NotNull
	@Column(columnDefinition = "text") 
	private String address;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_role", 
				joinColumns = {@JoinColumn(name = "username", referencedColumnName = "username") }, 
				inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "role_name") })
	private Set<Role> roles = new HashSet<>();
}
