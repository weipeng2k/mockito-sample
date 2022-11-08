/**
 *
 */
package com.murdock.tools.mockito.service;

import com.murdock.tools.mockito.dao.dataobject.MemberDO;
import com.murdock.tools.mockito.dao.UserDAO;

/**
 * @author weipeng2k
 *
 */
public class MemberServiceImpl implements MemberService {

	private UserDAO userDAO;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * MemberService#insertMember(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public Long insertMember(String name, String password)
			throws IllegalArgumentException {
		if (name == null || password == null) {
			throw new IllegalArgumentException();
		}

		if (name.length() > 32 || password.length() < 6
				|| password.length() > 16) {
			throw new IllegalArgumentException();
		}

		boolean pass = false;
		for (Character c : password.toCharArray()) {
			if (!Character.isDigit(c)) {
				pass = true;
				break;
			}
		}
		if (!pass) {
			throw new IllegalArgumentException();
		}

		MemberDO member = userDAO.findMember(name);
		if (member != null) {
			throw new IllegalArgumentException("duplicate member.");
		}

		member = new MemberDO();
		member.setName(name);
		member.setPassword(password);
		Long id = userDAO.insertMember(member);

		return id;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
