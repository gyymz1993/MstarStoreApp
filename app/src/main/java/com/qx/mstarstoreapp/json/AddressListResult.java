package com.qx.mstarstoreapp.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class AddressListResult {


    /**
     * data : {"addressList":[{"isDefault":"1","phone":"15155166161","name":"你们","id":"17","addr":"内蒙古自治区 鄂尔多斯市 达拉特旗 内蒙古自治区/鄂尔多斯市/达拉特旗"},{"isDefault":"0","phone":"15158454544","name":"我们","id":"16","addr":"吉林省 松原市 长岭县 吉林省/松原市/长岭县"},{"isDefault":"0","phone":"13177008851","name":"杨明智","id":"15","addr":"北京 北京市 朝阳区 北京/北京市/朝阳区"}]}
     * response :
     * error : 0
     * message :
     */
    private DataEntity data;
    private String response;
    private int error;
    private String message;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public String getResponse() {
        return response;
    }

    public int getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {
        /**
         * addressList : [{"isDefault":"1","phone":"15155166161","name":"你们","id":"17","addr":"内蒙古自治区 鄂尔多斯市 达拉特旗 内蒙古自治区/鄂尔多斯市/达拉特旗"},{"isDefault":"0","phone":"15158454544","name":"我们","id":"16","addr":"吉林省 松原市 长岭县 吉林省/松原市/长岭县"},{"isDefault":"0","phone":"13177008851","name":"杨明智","id":"15","addr":"北京 北京市 朝阳区 北京/北京市/朝阳区"}]
         */
        private List<AddressListEntity> addressList;

        public void setAddressList(List<AddressListEntity> addressList) {
            this.addressList = addressList;
        }

        public List<AddressListEntity> getAddressList() {
            return addressList;
        }

        public class AddressListEntity {

            /**
             * isDefault : 1
             * phone : 15155166161
             * name : 你们
             * id : 17
             * addr : 内蒙古自治区 鄂尔多斯市 达拉特旗 内蒙古自治区/鄂尔多斯市/达拉特旗
             */
            private String isDefault;
            private String phone;
            private String name;
            private String id;
            private String addr;

            public void setIsDefault(String isDefault) {
                this.isDefault = isDefault;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getIsDefault() {
                return isDefault;
            }

            public String getPhone() {
                return phone;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public String getAddr() {
                return addr;
            }
        }
    }
}
