/**
 * 
 */
package com.booking.business;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.booking.model.Booking;
import com.booking.util.UserContext;

/**
 * @author ddung
 *
 */
public interface BookingBusiness {
	public Booking findById(long bookingId);

	public Map<String, Object> createBooking(HttpServletRequest request, HttpServletResponse response,
			int numberOfGuest, Date fromDate, Date toDate, long classPK, String className, String fullName,
			String email, String phone, long stateId, String stateName, String vnpOrderInfo, String orderType,
			String bankCode, String language, UserContext userContext) throws UnsupportedEncodingException, IOException;

	public Booking deleteBooking(long bookingId, UserContext userContext);

	public double calculatorPrice(long classPK, String className, String fromDate, String toDate) throws ParseException;

	public List<Booking> findByToDate(String bookingStatus);

	public List<Booking> findBookings(String className, Long classPK, Double totalAmount, Integer numberOfGuest,
			String bookingStatus, Long userId);

	public List<Booking> checkTime(Long classPK, String className, String fromDate, String toDate)
			throws ParseException;

	public List<Map<String, Object>> findMyBookings(long userId, String className, String bookingStatus,
			UserContext userContext);

	public List<Map<String, Object>> findDetailBookings(long ownerId, Long classPK, String className,
			String bookingStatus, UserContext userContext);

	public Booking cancelRequestBooking(long bookingId, UserContext userContext);

	public Map<String, Object> cancelActionBooking(HttpServletRequest request, HttpServletResponse response,
			long bookingId, String bookingStatus, UserContext userContext)
			throws UnsupportedEncodingException, IOException;
	
	public void indexing(UserContext userContext);
}
