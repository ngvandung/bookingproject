/**
 * 
 */
package com.booking.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.booking.business.util.UserBusinessFactoryUtil;
import com.booking.model.User;
import com.booking.util.BeanUtil;
import com.booking.util.UserContext;

/**
 * @author ddung
 *
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Iterable<User> userApi(HttpServletRequest request, HttpSession session,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "phone", required = false) String phone,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName,
			@RequestParam(name = "age", required = false) Integer age,
			@RequestParam(name = "isHost", required = false) Integer isHost,
			@RequestParam(name = "isEnabled", required = false) Integer isEnabled,
			@RequestParam(name = "start", required = false) Integer start,
			@RequestParam(name = "end", required = false) Integer end) {

		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		Iterable<User> users = UserBusinessFactoryUtil.getUsers(username, email, phone, firstName, lastName, age,
				isHost, isEnabled, start, end, userContext);

		return users;
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@RequestBody User user) throws IOException {

		return UserBusinessFactoryUtil.createUser(user.getUsername(), user.getPassword(), user.getEmail(),
				user.getPhone(), user.getFirstName(), user.getLastName(), user.getAge(), user.getAddress(),
				user.getIsHost(), user.getBirthDay(), user.getDescription());

	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	@ResponseBody
	public User updateUser(HttpServletRequest request, HttpSession session, @RequestBody User userModel)
			throws IOException {

		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return UserBusinessFactoryUtil.updateUser(userModel.getUserId(), userModel.getUsername(),
				userModel.getPassword(), userModel.getEmail(), userModel.getPhone(), userModel.getFirstName(),
				userModel.getLastName(), userModel.getAge(), userModel.getAddress(), userModel.getIsHost(),
				userModel.getBirthDay(), userModel.getDescription(), userModel.getIsEnabled(), userContext);

	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
	@ResponseBody
	public User deleteUser(HttpServletRequest request, HttpSession session, @PathVariable("userId") Long userId)
			throws IOException {

		UserContext userContext = BeanUtil.getBean(UserContext.class);
		userContext = (UserContext) session.getAttribute("userContext");

		return UserBusinessFactoryUtil.deleteUser(userId, userContext);
	}
}