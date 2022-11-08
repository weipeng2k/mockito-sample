package com.murdock.tools.mockito.dao.impl;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.dao.dataobject.MemberDO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author weipeng2k 2022-11-08 19:57:38
 */
@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public MemberDO findMember(String memberId) {
        return sqlSessionTemplate.selectOne("MemberDAO.queryMember", memberId);
    }

    @Override
    public Long insertMember(MemberDO member) {
        int rows = sqlSessionTemplate.insert("MemberDAO.insertMember", member);
        return member.getId();
    }
}
