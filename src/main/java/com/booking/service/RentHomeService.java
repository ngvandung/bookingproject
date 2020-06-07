/**
 * 
 */
package com.booking.service;

import java.util.Date;

import com.booking.model.RentHome;

/**
 * @author ddung
 *
 */
public interface RentHomeService {
	public RentHome findById(long rentId);

	public RentHome updateRentHome(long rentId, int rentPeople, Date fromDate, Date toDate, long homeId,
			Long rentUserId, double totalAmount, long userId);

	public RentHome createRentHome(int rentPeople, Date fromDate, Date toDate, long homeId, Long rentUserId,
			double totalAmount, long userId);

	public RentHome deleteRentHome(long rentId);
}
