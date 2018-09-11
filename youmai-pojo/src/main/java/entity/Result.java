package entity;

import java.io.Serializable;

/**
 * @ClassName: Result
 * @Description: 返回结果
 * @Author: 泊松
 * @Date: 2018/9/10 22:12
 * @Version: 1.0
 */
public class Result implements Serializable {

    //是否成功
    private boolean success;
    //返回的信息
    private String message;


    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
