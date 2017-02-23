package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CheckEntity  implements MultiItemEntity {
    public static final int TYPE_ITEM_VIEW_HW = 1;
    public static final int TYPE_ITEM_VIEW_FUNC = 2;

    public static final String CHECK_STATUS_TOCHECK = "100";
    public static final String CHECK_STATUS_CHECKING = "101";
    public static final String CHECK_STATUS_FAIL = "110";
    public static final String CHECK_STATUS_OK = "111";

    public String checkId;
    public String checkName;
    public String checkDesc;
    public String checkStatus;  // 100 开启待测，101测试中，110失败，111成功；000关闭待测
    public String checkResult;
    public List<CheckService> checkServices;
    public int type;    // 1 硬件类型，2 接口类型

    public CheckEntity() {
    }

    public CheckEntity(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("CheckEntity{")
                .append("checkId='").append(checkId).append("\'")
                .append(", checkName='").append(checkName).append("\'")
                .append(", checkDesc='" ).append(checkDesc).append("\'")
                .append(", checkStatus='" ).append( checkStatus ).append("\'")
                .append(", checkResult='" ).append( checkResult ).append("\'")
                .append(", checkServices='" ).append( checkServices ).append("\'")
                .append(", type=" ).append( type)
                .append("}").toString();
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
