package com.razanPardazesh.supervisor.model.user;

/**
 * Created by Zikey on 10/11/2016.
 */

public class Visitor implements IUser{

    private final String KEY_ID = "id";
    private final String KEY_CODE = "code";
    private final String KEY_NAME = "name";


    private Long id=0l;
    private Long code=0l;
    private  String name="";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKEY_ID() {
        return KEY_ID;
    }

    public String getKEY_CODE() {
        return KEY_CODE;
    }

    public String getKEY_NAME() {
        return KEY_NAME;
    }
}
