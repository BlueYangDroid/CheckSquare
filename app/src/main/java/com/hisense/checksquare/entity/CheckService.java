package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CheckService {

    public String serviceName;
    public String format;
    public String serviceUnit;
    public String serviceTarget;
    public String serviceActual;
    public String serviceResult;

    @Override
    public String toString() {
        return new StringBuilder().append("CheckService{")
                .append("serviceName='").append(serviceName).append("\'")
                .append(", format='").append(format).append("\'")
                .append(", serviceUnit='" ).append(serviceUnit).append("\'")
                .append(", serviceTarget='" ).append( serviceTarget ).append("\'")
                .append(", serviceActual='" ).append(serviceActual).append("\'")
                .append(", serviceResult=" ).append(serviceResult).append("\'")
                .append("}").toString();
    }

    /*public String devId;
    public int ram;
    public int rom;
    public int usbs;
    public boolean ble;
    public boolean rtc;
    public boolean ota;
    public int[] cpuHZs;
    public long[] gpuHZs;*/

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
