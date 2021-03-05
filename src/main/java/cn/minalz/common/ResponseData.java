package cn.minalz.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(value = "接口返回封装对象")
public class ResponseData implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "返回状态：00 请求成功，01 请求失败，05 登录超时")
    public String code = "00";// 返回状态：00 请求成功，01 请求失败，E 请求失败，05 登录超时
    @ApiModelProperty(value = "返回信息")
    public String msg = "OK";
    @ApiModelProperty(value = "数据集合")
    public List list = new ArrayList();
    @ApiModelProperty(value = "数据对象")
    public Map<String, Object> data = new HashMap<>();
    @ApiModelProperty(value = "分页，总页数")
    public int totalPage;// 分页时，总页数
    @ApiModelProperty(value = "分页，总记录数")
    public int count;
    @ApiModelProperty(value = "当前页码")
    public int currentPage;
    @ApiModelProperty(value = "token")
    public String token;
    @ApiModelProperty(value = "错误类型")
    public String errtype;
    @ApiModelProperty(value = "操作ID")
    public String opid;

    public static ResponseData of(String type, String message) {
        return new ResponseData(type, message);
    }

    public static ResponseData oferror(String message) {
        return new ResponseData("01", message);
    }

    public static ResponseData ofok() {
        return new ResponseData("00", "OK");
    }

    public static ResponseData ofok(Map<String, Object> data) {
        return new ResponseData("00", "OK", data);
    }

    public static ResponseData of() {
        return new ResponseData();
    }

    public static ResponseData ofPage(Page page, int pageNumber) {
        return new ResponseData("00", "OK", page.getContent(), page.getTotalPages(), (int) page.getTotalElements(), pageNumber);
    }

    public ResponseData SetOKMsg(String message) {
        this.code = "00";
        this.msg = message;
        return this;
    }

    public ResponseData SetErrMsg(String message) {
        this.code = "01";
        this.msg = message;
        return this;
    }

    public ResponseData(String type, String message) {
        this.code = type;
        this.msg = message;
    }

    public ResponseData(String type, String message, Map<String, Object> data) {
        this.code = type;
        this.msg = message;
        this.data = data;
    }

    public ResponseData() {
    }

    public ResponseData(String code, String msg, List list, int totalPage, int count, int currentPage) {
        this.code = code;
        this.msg = msg;
        this.list = list;
        this.totalPage = totalPage;
        this.count = count;
        this.currentPage = currentPage;
    }

}
