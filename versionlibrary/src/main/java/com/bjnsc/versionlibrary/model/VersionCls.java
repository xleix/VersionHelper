package com.bjnsc.versionlibrary.model;

/**
 * Created by lx123 on 2018/2/2/002.
 */

public class VersionCls {

    /**
     * status : OK
     * statusCode : 0
     * data : {"id":"4fb11d00-0a57-11e8-b3b4-0d050ce18e7b","project_id":"1e66db90-0a57-11e8-96dd-21cb26995c19","original_name":"tofuture.apk","file_name":"35ba3b70-0a57-11e8-96dd-21cb26995c19.apk","file_size":7865335,"file_path":"/home/jack/publish_manager/public/upload/35ba3b70-0a57-11e8-96dd-21cb26995c19.apk","file_link":"/public/upload/apk/35ba3b70-0a57-11e8-96dd-21cb26995c19.apk","platform":0,"version_code":8,"version_name":"1.4.2","comment":"更新内容\n1.xxxxx\n2.ssssss","is_deleted":null,"created_by":"52c55ec8-06fd-11e8-91c6-00163e2ef0cb","updated_by":"52c55ec8-06fd-11e8-91c6-00163e2ef0cb","created_at":"2018-02-05T09:31:15.000Z","updated_at":"2018-02-05T09:31:15.000Z"}
     */

    private String status;
    private int statusCode;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4fb11d00-0a57-11e8-b3b4-0d050ce18e7b
         * project_id : 1e66db90-0a57-11e8-96dd-21cb26995c19
         * original_name : tofuture.apk
         * file_name : 35ba3b70-0a57-11e8-96dd-21cb26995c19.apk
         * file_size : 7865335
         * file_path : /home/jack/publish_manager/public/upload/35ba3b70-0a57-11e8-96dd-21cb26995c19.apk
         * file_link : /public/upload/apk/35ba3b70-0a57-11e8-96dd-21cb26995c19.apk
         * platform : 0
         * version_code : 8
         * version_name : 1.4.2
         * comment : 更新内容
         1.xxxxx
         2.ssssss
         * is_deleted : null
         * created_by : 52c55ec8-06fd-11e8-91c6-00163e2ef0cb
         * updated_by : 52c55ec8-06fd-11e8-91c6-00163e2ef0cb
         * created_at : 2018-02-05T09:31:15.000Z
         * updated_at : 2018-02-05T09:31:15.000Z
         */

        private String id;
        private String project_id;
        private String original_name;
        private String file_name;
        private int file_size;
        private String file_path;
        private String file_link;
        private int platform;
        private int version_code;
        private String version_name;
        private String comment;
        private Object is_deleted;
        private String created_by;
        private String updated_by;
        private String created_at;
        private String updated_at;
        private String forced_update;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getOriginal_name() {
            return original_name;
        }

        public void setOriginal_name(String original_name) {
            this.original_name = original_name;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }

        public String getFile_link() {
            return file_link;
        }

        public void setFile_link(String file_link) {
            this.file_link = file_link;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Object getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(Object is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getForced_update() {
            return forced_update == null ? "" : forced_update;
        }

        public void setForced_update(String forced_update) {
            this.forced_update = forced_update;
        }
    }
}
