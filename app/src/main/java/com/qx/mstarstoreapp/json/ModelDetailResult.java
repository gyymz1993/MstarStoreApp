package com.qx.mstarstoreapp.json;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ModelDetailResult {

    /**
     * data : {"stoneColor":[{"id":"1","title":"H+"},{"id":"2","title":"I-J"},{"id":"3","title":"H-I"},{"id":"4","title":"H-K"}],"stoneSpec":[{"id":"1","title":"10"},{"id":"2","title":"20"},{"id":"3","title":"30"},{"id":"4","title":"40"}],"stonePurity":[{"id":"1","title":"SI"},{"id":"2","title":"VS"},{"id":"3","title":"VS-VSS"}],"goldenPrice":[{"price":"355/g","title":"pt"},{"price":"356/g","title":"18k"}],"stoneShape":[{"id":"1","title":"圆形"},{"id":"2","title":"心形"},{"id":"3","title":"马形"},{"id":"4","title":"方形"},{"id":"5","title":"梯形"}],"stoneType":[{"id":"1","title":"钻石"},{"id":"2","title":"碎钻"},{"id":"3","title":"方钻"},{"id":"4","title":"锑钻"},{"id":"5","title":"马眼钻"}],"model":{"price":"5000","categoryTitle":"手镯","stoneA":{"specId":"3","number":null,"shapeId":"2","purityTitle":"SI","shapeTitle":"心形","colorId":"1","typeId":"1","specTitle":"30","colorTitle":"H+","typeTitle":"钻石","purityId":"1"},"stoneB":{"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"1","specTitle":"","colorTitle":"","typeTitle":"钻石","purityId":null},"stoneC":{"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"5","specTitle":"","colorTitle":"","typeTitle":"马眼钻","purityId":null},"title":"心安」 PT950铂金手镯( A36670-40)","pics":[{"id":"2","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/dddd0.jpg"},{"id":"33","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/5721b4f2N39738c83.jpg"}],"categoryId":"11","stone":{"specId":"1","number":null,"shapeId":"3","purityTitle":"SI","shapeTitle":"马形","colorId":"1","typeId":"1","specTitle":"10","colorTitle":"H+","typeTitle":"钻石","purityId":"1"}},"remarks":[{"id":"1","title":"提取信息","content":"不方便拿，请邮寄给我"},{"id":"2","title":"邮寄地址","content":"深圳市水贝工业区52号xx收"},{"id":"3","title":"没有这么办","content":"可以等待货到付款"}]}
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
         * stoneColor : [{"id":"1","title":"H+"},{"id":"2","title":"I-J"},{"id":"3","title":"H-I"},{"id":"4","title":"H-K"}]
         * stoneSpec : [{"id":"1","title":"10"},{"id":"2","title":"20"},{"id":"3","title":"30"},{"id":"4","title":"40"}]
         * stonePurity : [{"id":"1","title":"SI"},{"id":"2","title":"VS"},{"id":"3","title":"VS-VSS"}]
         * goldenPrice : [{"price":"355/g","title":"pt"},{"price":"356/g","title":"18k"}]
         * stoneShape : [{"id":"1","title":"圆形"},{"id":"2","title":"心形"},{"id":"3","title":"马形"},{"id":"4","title":"方形"},{"id":"5","title":"梯形"}]
         * stoneType : [{"id":"1","title":"钻石"},{"id":"2","title":"碎钻"},{"id":"3","title":"方钻"},{"id":"4","title":"锑钻"},{"id":"5","title":"马眼钻"}]
         * model : {"price":"5000","categoryTitle":"手镯","stoneA":{"specId":"3","number":null,"shapeId":"2","purityTitle":"SI","shapeTitle":"心形","colorId":"1","typeId":"1","specTitle":"30","colorTitle":"H+","typeTitle":"钻石","purityId":"1"},"stoneB":{"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"1","specTitle":"","colorTitle":"","typeTitle":"钻石","purityId":null},"stoneC":{"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"5","specTitle":"","colorTitle":"","typeTitle":"马眼钻","purityId":null},"title":"心安」 PT950铂金手镯( A36670-40)","pics":[{"id":"2","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/dddd0.jpg"},{"id":"33","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/5721b4f2N39738c83.jpg"}],"categoryId":"11","stone":{"specId":"1","number":null,"shapeId":"3","purityTitle":"SI","shapeTitle":"马形","colorId":"1","typeId":"1","specTitle":"10","colorTitle":"H+","typeTitle":"钻石","purityId":"1"}}
         * remarks : [{"id":"1","title":"提取信息","content":"不方便拿，请邮寄给我"},{"id":"2","title":"邮寄地址","content":"深圳市水贝工业区52号xx收"},{"id":"3","title":"没有这么办","content":"可以等待货到付款"}]
         */
        private List<StoneColorEntity> stoneColor;
        private List<StoneSpecEntity> stoneSpec;
        private List<StonePurityEntity> stonePurity;
        private List<GoldenPriceEntity> goldenPrice;
        private List<StoneShapeEntity> stoneShape;
        private List<StoneTypeEntity> stoneType;
        private ModelEntity model;
        private List<RemarksEntity> remarks;

        public void setStoneColor(List<StoneColorEntity> stoneColor) {
            this.stoneColor = stoneColor;
        }

        public void setStoneSpec(List<StoneSpecEntity> stoneSpec) {
            this.stoneSpec = stoneSpec;
        }

        public void setStonePurity(List<StonePurityEntity> stonePurity) {
            this.stonePurity = stonePurity;
        }

        public void setGoldenPrice(List<GoldenPriceEntity> goldenPrice) {
            this.goldenPrice = goldenPrice;
        }

        public void setStoneShape(List<StoneShapeEntity> stoneShape) {
            this.stoneShape = stoneShape;
        }

        public void setStoneType(List<StoneTypeEntity> stoneType) {
            this.stoneType = stoneType;
        }

        public void setModel(ModelEntity model) {
            this.model = model;
        }

        public void setRemarks(List<RemarksEntity> remarks) {
            this.remarks = remarks;
        }

        public List<StoneColorEntity> getStoneColor() {
            return stoneColor;
        }

        public List<StoneSpecEntity> getStoneSpec() {
            return stoneSpec;
        }

        public List<StonePurityEntity> getStonePurity() {
            return stonePurity;
        }

        public List<GoldenPriceEntity> getGoldenPrice() {
            return goldenPrice;
        }

        public List<StoneShapeEntity> getStoneShape() {
            return stoneShape;
        }

        public List<StoneTypeEntity> getStoneType() {
            return stoneType;
        }

        public ModelEntity getModel() {
            return model;
        }

        public List<RemarksEntity> getRemarks() {
            return remarks;
        }

        public class StoneColorEntity {
            /**
             * id : 1
             * title : H+
             */
            private String id;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }
        }

        public class StoneSpecEntity {
            /**
             * id : 1
             * title : 10
             */
            private String id;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }
        }

        public class StonePurityEntity {
            /**
             * id : 1
             * title : SI
             */
            private String id;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }
        }

        public class GoldenPriceEntity {
            /**
             * price : 355/g
             * title : pt
             */
            private String price;
            private String title;

            public void setPrice(String price) {
                this.price = price;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPrice() {
                return price;
            }

            public String getTitle() {
                return title;
            }
        }

        public class StoneShapeEntity {
            /**
             * id : 1
             * title : 圆形
             */
            private String id;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }
        }

        public class StoneTypeEntity {
            /**
             * id : 1
             * title : 钻石
             */
            private String id;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }
        }

        public class ModelEntity {
            /**
             * price : 5000
             * categoryTitle : 手镯
             * stoneA : {"specId":"3","number":null,"shapeId":"2","purityTitle":"SI","shapeTitle":"心形","colorId":"1","typeId":"1","specTitle":"30","colorTitle":"H+","typeTitle":"钻石","purityId":"1"}
             * stoneB : {"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"1","specTitle":"","colorTitle":"","typeTitle":"钻石","purityId":null}
             * stoneC : {"specId":null,"number":null,"shapeId":null,"purityTitle":"","shapeTitle":"","colorId":null,"typeId":"5","specTitle":"","colorTitle":"","typeTitle":"马眼钻","purityId":null}
             * title : 心安」 PT950铂金手镯( A36670-40)
             * pics : [{"id":"2","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/dddd0.jpg"},{"id":"33","pic":"http://192.168.1.240:9112/Uploads/Pics/2016-09-21/5721b4f2N39738c83.jpg"}]
             * categoryId : 11
             * stone : {"specId":"1","number":null,"shapeId":"3","purityTitle":"SI","shapeTitle":"马形","colorId":"1","typeId":"1","specTitle":"10","colorTitle":"H+","typeTitle":"钻石","purityId":"1"}
             */
            private String price;
            private String categoryTitle;
            private StoneAEntity stoneA;
            private StoneBEntity stoneB;
            private StoneCEntity stoneC;
            private String title;
            private List<PicsEntity> pics;
            private String categoryId;
            private StoneEntity stone;

            public void setPrice(String price) {
                this.price = price;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }

            public void setStoneA(StoneAEntity stoneA) {
                this.stoneA = stoneA;
            }

            public void setStoneB(StoneBEntity stoneB) {
                this.stoneB = stoneB;
            }

            public void setStoneC(StoneCEntity stoneC) {
                this.stoneC = stoneC;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setPics(List<PicsEntity> pics) {
                this.pics = pics;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public void setStone(StoneEntity stone) {
                this.stone = stone;
            }

            public String getPrice() {
                return price;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public StoneAEntity getStoneA() {
                return stoneA;
            }

            public StoneBEntity getStoneB() {
                return stoneB;
            }

            public StoneCEntity getStoneC() {
                return stoneC;
            }

            public String getTitle() {
                return title;
            }

            public List<PicsEntity> getPics() {
                return pics;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public StoneEntity getStone() {
                return stone;
            }

            public class StoneAEntity extends StoneEntity{

            }

            public class StoneBEntity extends StoneEntity{

            }

            public class StoneCEntity extends StoneEntity{

            }

            public class PicsEntity {
                /**
                 * id : 2
                 * pic : http://192.168.1.240:9112/Uploads/Pics/2016-09-21/dddd0.jpg
                 */
                private String id;
                private String pic;

                public void setId(String id) {
                    this.id = id;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getId() {
                    return id;
                }

                public String getPic() {
                    return pic;
                }
            }

            public class StoneEntity {
                /**
                 * specId : 1
                 * number : null
                 * shapeId : 3
                 * purityTitle : SI
                 * shapeTitle : 马形
                 * colorId : 1
                 * typeId : 1
                 * specTitle : 10
                 * colorTitle : H+
                 * typeTitle : 钻石
                 * purityId : 1
                 */
                private String price;
                private String specId;
                private String number;
                private String shapeId;
                private String purityTitle;
                private String shapeTitle;
                private String colorId;
                private String typeId;
                private String specTitle;
                private String colorTitle;
                private String typeTitle;
                private String purityId;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public void setSpecId(String specId) {
                    this.specId = specId;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public void setShapeId(String shapeId) {
                    this.shapeId = shapeId;
                }

                public void setPurityTitle(String purityTitle) {
                    this.purityTitle = purityTitle;
                }

                public void setShapeTitle(String shapeTitle) {
                    this.shapeTitle = shapeTitle;
                }

                public void setColorId(String colorId) {
                    this.colorId = colorId;
                }

                public void setTypeId(String typeId) {
                    this.typeId = typeId;
                }

                public void setSpecTitle(String specTitle) {
                    this.specTitle = specTitle;
                }

                public void setColorTitle(String colorTitle) {
                    this.colorTitle = colorTitle;
                }

                public void setTypeTitle(String typeTitle) {
                    this.typeTitle = typeTitle;
                }

                public void setPurityId(String purityId) {
                    this.purityId = purityId;
                }

                public String getSpecId() {
                    return specId;
                }

                public String getNumber() {
                    return number;
                }

                public String getShapeId() {
                    return shapeId;
                }

                public String getPurityTitle() {
                    return purityTitle;
                }

                public String getShapeTitle() {
                    return shapeTitle;
                }

                public String getColorId() {
                    return colorId;
                }

                public String getTypeId() {
                    return typeId;
                }

                public String getSpecTitle() {
                    return specTitle;
                }

                public String getColorTitle() {
                    return colorTitle;
                }

                public String getTypeTitle() {
                    return typeTitle;
                }

                public String getPurityId() {
                    return purityId;
                }

                @Override
                public String toString() {
                    return "StoneEntity{" +
                            "price='" + price + '\'' +
                            ", specId='" + specId + '\'' +
                            ", number='" + number + '\'' +
                            ", shapeId='" + shapeId + '\'' +
                            ", purityTitle='" + purityTitle + '\'' +
                            ", shapeTitle='" + shapeTitle + '\'' +
                            ", colorId='" + colorId + '\'' +
                            ", typeId='" + typeId + '\'' +
                            ", specTitle='" + specTitle + '\'' +
                            ", colorTitle='" + colorTitle + '\'' +
                            ", typeTitle='" + typeTitle + '\'' +
                            ", purityId='" + purityId + '\'' +
                            '}';
                }
            }
        }

        public class RemarksEntity {
            /**
             * id : 1
             * title : 提取信息
             * content : 不方便拿，请邮寄给我
             */
            private String id;
            private String title;
            private String content;

            public void setId(String id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
