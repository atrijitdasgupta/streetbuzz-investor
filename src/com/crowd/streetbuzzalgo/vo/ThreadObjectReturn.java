/**
 * 
 */
package com.crowd.streetbuzzalgo.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Atrijit
 *
 */
public class ThreadObjectReturn implements Serializable{
private String type;
private Map map;
private List list;

public List getList() {
	return list;
}
public void setList(List list) {
	this.list = list;
}
public Map getMap() {
	return map;
}
public void setMap(Map map) {
	this.map = map;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
}
