/**
 * 
 */
package com.crowd.streetbuzz.model;

import java.util.Date;

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
@Entity
@Table(name = "SB_CRAWLER_MODEL", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class CrawlerModel {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "URL")
	private String url;

	@Column(name = "AUTHOR")
	private String author;

	@Column(name = "FIRSTSENTENCE")
	private String firstsentence;

	@Column(name = "PAGEDATE")
	private Date pagedate;

	@Column(name = "DISQUSSHORTNAME")
	private String disqusshortname;

	@Column(name = "LIVEFYREKEY")
	private String livefyrekey;

	@Column(name = "IMAGEURL")
	private String imageurl;
	
	@Column(name = "META")
	private String meta;
	
	@Column(name = "OGMETA")
	private String ogmeta;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDisqusshortname() {
		return disqusshortname;
	}

	public void setDisqusshortname(String disqusshortname) {
		this.disqusshortname = disqusshortname;
	}

	public String getFirstsentence() {
		return firstsentence;
	}

	public void setFirstsentence(String firstsentence) {
		this.firstsentence = firstsentence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getLivefyrekey() {
		return livefyrekey;
	}

	public void setLivefyrekey(String livefyrekey) {
		this.livefyrekey = livefyrekey;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getOgmeta() {
		return ogmeta;
	}

	public void setOgmeta(String ogmeta) {
		this.ogmeta = ogmeta;
	}

	public Date getPagedate() {
		return pagedate;
	}

	public void setPagedate(Date pagedate) {
		this.pagedate = pagedate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
