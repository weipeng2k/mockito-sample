package com.murdock.tools.mockito.dao;

import com.murdock.tools.mockito.Member;

public interface UserDAO {
	/**
	 * 查询会员
	 * 
	 * @param memberId
	 * @return
	 */
	Member findMember(String memberId);

	/**
	 * 插入会员
	 * 
	 * @param member
	 * @return
	 */
	Long insertMember(Member member);
}
