package com.murdock.tools.mockito;

import java.util.List;

/**
 * @author weipeng2k 2020年06月22日 下午13:07:17
 */
public class Student {

    private String name;

    private int age;

    private List<Hobby> hobbyList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Hobby> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<Hobby> hobbyList) {
        this.hobbyList = hobbyList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hobbyList=" + hobbyList +
                '}';
    }
}
