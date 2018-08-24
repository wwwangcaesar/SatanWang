package com.lovcreate.amap.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by Bright on 2017/8/8 0008
 * 省市bean
 */
public class AreaBean {

    private List<ProvincesBean> provinces;

    public List<ProvincesBean> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvincesBean> provinces) {
        this.provinces = provinces;
    }

    public static class ProvincesBean implements IPickerViewData {
        /**
         * name : 北京市
         * children : [{"name":"东城区","code":"110101"},{"name":"西城区","code":"110102"},{"name":"朝阳区","code":"110105"},{"name":"丰台区","code":"110106"},{"name":"石景山区","code":"110107"},{"name":"海淀区","code":"110108"},{"name":"门头沟区","code":"110109"},{"name":"房山区","code":"110111"},{"name":"通州区","code":"110112"},{"name":"顺义区","code":"110113"},{"name":"昌平区","code":"110114"},{"name":"大兴区","code":"110115"},{"name":"怀柔区","code":"110116"},{"name":"平谷区","code":"110117"},{"name":"密云区","code":"110118"},{"name":"延庆区","code":"110119"}]
         * code : 110000
         */

        private String name;
        private String code;
        private List<ChildrenBean> children;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        @Override
        public String getPickerViewText() {
            return this.name;
        }

        public static class ChildrenBean {
            /**
             * name : 东城区
             * code : 110101
             */

            private String name;
            private String code;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}
