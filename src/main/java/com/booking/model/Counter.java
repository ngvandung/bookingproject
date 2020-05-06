/**
 * 
 */
package com.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ddung
 *
 */
@Entity
@Table(name = "counter")
public class Counter {
	@Id
	private String clazz;
	@Column(name = "counter", nullable = false)
	private long counter;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

}