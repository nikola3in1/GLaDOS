package com.aperturescience.model.response;


import com.aperturescience.model.response.helper.MyError;
import com.aperturescience.model.response.helper.UploadResult;

//Response model class
public class Upload {
    private UploadResult result;
    private String success = "";
    private MyError error;

    public UploadResult getResult() {
        return result;
    }

    public void setResult(UploadResult result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public MyError getError() {
        return error;
    }

    public void setError(MyError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "result=" + result +
                '}';
    }


}
