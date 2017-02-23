package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ParseEntity {

    public String vers; // 版本
    public String code; // 编号
    public String desc;  // 描述
    public List<CheckEntity> checkEntities;  // 检测项集合

    @Override
    public String toString() {
        return super.toString();
    }

    /*    *//*
     *普通声明的属性默认会被解析和序列化
     *//*
    public String format;

    *//*
     *重命名key还是需要注解来指出的
     *//*
    @JsonField(name = "_id")
    public int imageId;

    *//*
     * 包访问权限的处理是没问题的
     *//*
    List<Image> similarImages;

    *//*
     * 用@JsonIgnore来忽略不想被解析和序列化的属性
     *//*
    @JsonIgnore
    public int nonJsonField;

    *//*
     * 该策略下private属性默认忽略
     *//*
    private int privateInt;

    public int getPrivateInt() {
        return privateInt;
    }

    public void setPrivateInt(int i) {
        privateInt = i;
    }

    @OnJsonParseComplete void onParseComplete() {
        // 解析完成后干点什么
    }

    @OnPreJsonSerialize void onPreSerialize() {
        //序列化前干点什么
    }*/
}
