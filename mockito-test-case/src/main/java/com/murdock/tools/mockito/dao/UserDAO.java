package com.murdock.tools.mockito.dao;

import com.murdock.tools.mockito.dao.dataobject.MemberDO;

public interface UserDAO {
	/**
	 * 查询会员
	 * 
	 * @param memberId name
	 * @return member
	 */
	MemberDO findMember(String memberId);

	/**
	 * 插入会员
	 * 
	 * @param member
	 * @return
	 */
	Long insertMember(MemberDO member);
}
