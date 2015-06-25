/**
 * 
 */
package com.crowd.streetbuzz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Atrijit
 *
 */
/*
 * CREATE TABLE SB_CARD_MASTER(
ID BIGINT not null auto_increment,
UNIQUEID VARCHAR(50),
TYPE VARCHAR(1),
PRIMARY KEY (ID)) ENGINE=InnoDB;
 */

@Entity
@Table(name="SB_CARD_MASTER", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardMaster {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "UNIQUEID")
	private String uniqueid;
	
	@Column(name = "TYPE")
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	
	
}
