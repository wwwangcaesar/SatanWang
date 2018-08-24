package com.lovcreate.amap.bean;

import java.util.List;

/**
 * Created by Bright on 2017/8/14 0014
 */

public class TaskTrackBean {

    private List<List<ListBean>> list;

    public List<List<ListBean>> getList() {
        return list;
    }

    public void setList(List<List<ListBean>> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * lng : 125.30954
         * lat : 43.823106
         * userId : 26
         * taskId : 1
         * stepCount : null
         * date : 1502673723430
         */

        private String lng;
        private String lat;
        private String userId;
        private String taskId;
        private String stepCount;
        private Long date;

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getStepCount() {
            return stepCount;
        }

        public void setStepCount(String stepCount) {
            this.stepCount = stepCount;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }
}
