//package com.qx.mstarstoreapp.bean;
//
///**
// * Created by Administrator on 2016/9/23.
// */
//public class FilterBean {
//
//
//
//    public String id;
//    public String value;
//
//    public  String name;
//
//    public String tag;
//
//    public String getTag() {
//        return tag;
//    }
//
//    public void setTag(String tag) {
//        this.tag = tag;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public boolean isCheck;
//
//    public boolean isCheck() {
//        return isCheck;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setCheck(boolean check) {
//        isCheck = check;
//    }
//
//    public FilterBean( String name,String value,boolean isCheck) {
//        this.value=value;
//        this.isCheck = isCheck;
//        this.name = name;
//    }
//
//    public FilterBean(String name) {
//        this.name = name;
//    }
//
//    public FilterBean() {
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof FilterBean)) return false;
//
//        FilterBean that = (FilterBean) o;
//
//        return getName().equals(that.getName());
//
//    }
//
//    @Override
//    public int hashCode() {
//        return getName().hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return "FilterBean{" +
//                "id='" + id + '\'' +
//                ", value='" + value + '\'' +
//                ", name='" + name + '\'' +
//                ", tag='" + tag + '\'' +
//                ", isCheck=" + isCheck +
//                '}';
//    }
//}
