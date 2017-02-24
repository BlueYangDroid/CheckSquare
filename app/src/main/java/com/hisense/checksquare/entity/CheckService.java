package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */
@Entity // greenDao annotation
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)   // logan square annotation
public class CheckService {

    @Id // greenDao annotation
    public int serviceId;
    public String serviceName;
    public String format;
    public String serviceUnit;
    public String serviceTarget;
    public String serviceActual;
    public String serviceResult;

    @Generated(hash = 932616929)
    public CheckService(int serviceId, String serviceName, String format, String serviceUnit,
            String serviceTarget, String serviceActual, String serviceResult) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.format = format;
        this.serviceUnit = serviceUnit;
        this.serviceTarget = serviceTarget;
        this.serviceActual = serviceActual;
        this.serviceResult = serviceResult;
    }

    @Generated(hash = 766365664)
    public CheckService() {
    }

    @Override
    public String toString() {
        return new StringBuilder().append("CheckService{")
                .append("serviceId=").append(serviceId)
                .append(", serviceName='").append(serviceName).append("\'")
                .append(", format='").append(format).append("\'")
                .append(", serviceUnit='" ).append(serviceUnit).append("\'")
                .append(", serviceTarget='" ).append( serviceTarget ).append("\'")
                .append(", serviceActual='" ).append(serviceActual).append("\'")
                .append(", serviceResult='" ).append(serviceResult).append("\'")
                .append("}").toString();
    }

    public int getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getServiceUnit() {
        return this.serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public String getServiceTarget() {
        return this.serviceTarget;
    }

    public void setServiceTarget(String serviceTarget) {
        this.serviceTarget = serviceTarget;
    }

    public String getServiceActual() {
        return this.serviceActual;
    }

    public void setServiceActual(String serviceActual) {
        this.serviceActual = serviceActual;
    }

    public String getServiceResult() {
        return this.serviceResult;
    }

    public void setServiceResult(String serviceResult) {
        this.serviceResult = serviceResult;
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
