package com.murdock.tools.mockito;

/**
 * @author weipeng2k 2020年06月22日 下午13:07:35
 */
public class Hobby {

    private String name;

    private int kind;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", desc='" + desc + '\'' +
                '}';
    }
}
