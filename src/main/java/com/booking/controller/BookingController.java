/**
 * 
 */
package com.booking.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.booking.business.util.BookingBusinessFactoryUtil;
import com.booking.business.util.HouseBusinessFactoryUtil;
import com.booking.business.util.UserBusinessFactoryUtil;
import com.booking.business.util.VnPayPaymentBusinessFactoryUtil;
import com.booking.model.Booking;
import com.booking.model.House;
import com.booking.model.User;
import com.booking.util.BeanUtil;
import com.booking.util.DateFormat;
import com.booking.util.UserContext;

/**
 * @author ddung
 *
 */
@RestController
@RequestMapping("/api/v1")
public class BookingController {
	@RequestMapping(value = "/bookings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Booking> bookingHouse(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(name = "className", required = false) String className,
			@RequestParam(name = "classPK", required = false) Long classPK,
			@RequestParam(name = "numberOfGuest", required = false) Integer numberOfGuest,
			@RequestParam(name = "totalAmount", required = false) Double totalAmount,
			@RequestParam(name = "userId", required = false) Long userId,
			@RequestParam(name = "bookingStatus", required = false) String bookingStatus) {

		return BookingBusinessFactoryUtil.findBookings(className, classPK, totalAmount, numberOfGuest, bookingStatus,
				userId);
	}

	@RequestMapping(value = "/booking/indexing", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> indexing(HttpServletRequest request, HttpSession session) {
		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		BookingBusinessFactoryUtil.indexing(userContext);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", 200);
		return result;
	}

	@RequestMapping(value = "/payment/house/{bookingId}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> payment(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable("bookingId") long bookingId) throws UnsupportedEncodingException, IOException {
		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		Booking booking = BookingBusinessFactoryUtil.findById(bookingId);
		House house = HouseBusinessFactoryUtil.findById(booking.getClassPK());
		User user = UserBusinessFactoryUtil.findById(house.getOwnerHouseId(), userContext);

		Map<String, Object> result = VnPayPaymentBusinessFactoryUtil.payment(request, response,
				"Thanh toan don hang thoi gian: " + DateFormat.formatDateToString_ddMMyyyy_HHmmss(new Date()),
				"billpayment", "NCB", "vn", user.getTmnCode(), user.getHashSecret(), booking.getTotalAmount(),
				bookingId);
		return result;
	}

	@RequestMapping(value = "/bookings/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> findMyBookings(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam("className") String className, @RequestParam("userId") Long userId,
			@RequestParam(name = "bookingStatus", required = false) String bookingStatus) {

		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return BookingBusinessFactoryUtil.findMyBookings(userId, className, bookingStatus, userContext);
	}

	@RequestMapping(value = "/validate/house", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Booking> checkTime(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(name = "classPK", required = false) Long classPK,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) throws ParseException {

		return BookingBusinessFactoryUtil.checkTime(classPK, House.class.getName(), fromDate, toDate);
	}

	@RequestMapping(value = "/booking/house", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> bookingHouse(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody Booking booking) throws UnsupportedEncodingException, IOException {
		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return BookingBusinessFactoryUtil.createBooking(request, response, booking.getNumberOfGuest(),
				booking.getFromDate(), booking.getToDate(), booking.getClassPK(), House.class.getName(),
				booking.getFullName(), booking.getEmail(), booking.getPhone(), booking.getStateId(),
				booking.getStateName(),
				"Thanh toan don hang thoi gian: " + DateFormat.formatDateToString_ddMMyyyy_HHmmss(new Date()),
				"billpayment", "NCB", "vn", userContext);
	}

	@RequestMapping(value = "/booking/cancelrequest/{bookingId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Booking cancelRequestBooking(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable("bookingId") long bookingId) {
		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return BookingBusinessFactoryUtil.cancelRequestBooking(bookingId, userContext);
	}

	@RequestMapping(value = "/booking/cancelaction/{bookingId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> bookingHouse(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("bookingId") long bookingId,
			@RequestParam("bookingStatus") String bookingStatus) throws UnsupportedEncodingException, IOException {
		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return BookingBusinessFactoryUtil.cancelActionBooking(request, response, bookingId, bookingStatus, userContext);
	}

	@RequestMapping(value = "/vnpay/confirm/{bookingId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> confirmPayment(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("bookingId") long bookingId) throws UnsupportedEncodingException {

		return VnPayPaymentBusinessFactoryUtil.confirm(request, bookingId);
	}

	@RequestMapping(value = "/booking/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> findDetailBookings(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(name = "classPK", required = false) Long classPK,
			@RequestParam("className") String className, @RequestParam("ownerId") Long ownerId,
			@RequestParam(name = "bookingStatus", required = false) String bookingStatus) throws ParseException {

		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return BookingBusinessFactoryUtil.findDetailBookings(ownerId, classPK, className, bookingStatus, userContext);
	}

}
