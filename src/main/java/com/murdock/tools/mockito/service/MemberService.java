package com.murdock.tools.mockito.service;

/**
 * @author weipeng2k
 * 
 */
public interface MemberService {
	/**
	 * 插入一个会员
	 * 
	 * 其中 name不能超过32个字符，不能为空 password不能全部是数字，长度不能低于6，不超过16
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	Long insertMember(String name, String password)
			throws IllegalArgumentException;
}
