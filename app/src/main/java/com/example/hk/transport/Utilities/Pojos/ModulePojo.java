package com.example.hk.transport.Utilities.Pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModulePojo {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("moduleId")
        @Expose
        private Integer moduleId;
        @SerializedName("moduleName")
        @Expose
        private String moduleName;
        @SerializedName("moduleImage")
        @Expose
        private String moduleImage;
        @SerializedName("isAvailable")
        @Expose
        private Boolean isAvailable;
        @SerializedName("isEnable")
        @Expose
        private Boolean isEnable;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;

        public Integer getModuleId() {
            return moduleId;
        }

        public void setModuleId(Integer moduleId) {
            this.moduleId = moduleId;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModuleImage() {
            return moduleImage;
        }

        public void setModuleImage(String moduleImage) {
            this.moduleImage = moduleImage;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public Boolean getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(Boolean isEnable) {
            this.isEnable = isEnable;
        }

        public Boolean getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

    }
}
