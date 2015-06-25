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
 * 
 * CREATE TABLE SB_PROCESS_ID(
ID BIGINT not null auto_increment,
PROCESSID VARCHAR(10),
FORWARDPATH VARCHAR(255),
PRIMARY KEY (ID)) ENGINE=InnoDB;
 */

@Entity
@Table(name="SB_PROCESS_ID", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class ProcessID {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PROCESSID")
	private String processid;
	
	@Column(name = "FORWARDPATH")
	private String forwardpath;

	public String getForwardpath() {
		return forwardpath;
	}

	public void setForwardpath(String forwardpath) {
		this.forwardpath = forwardpath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}
	
	
}
