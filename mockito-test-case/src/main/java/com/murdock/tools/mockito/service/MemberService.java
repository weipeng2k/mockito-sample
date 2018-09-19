package com.murdock.tools.mockito.service;

/**
 * @author weipeng2k
 */
public interface MemberService {
    /**
     * <pre>
     * 插入一个会员，返回会员的主键
     * 如果有重复，则会抛出异常
     * </pre>
     *
     * @param name     name不能超过32个字符，不能为空
     * @param password password不能全部是数字，长度不能低于6，不超过16
     * @return PK
     */
    Long insertMember(String name, String password) throws IllegalArgumentException;
}
