package cn.minalz.quartz;

/**
 * 定时任务枚举类
 */
public enum JobStatusEnum {


    /**
     * 0=停止
     */
    STOP("0", "停止"),
    /**
     * 1=运行
     */
    RUNNING("1", "运行");

    private String value = null;
    private String code = null;

    JobStatusEnum(String code, String value) {
        this.value = value;
        this.code = code;
    }

    public static JobStatusEnum getEnumByKey(String key) {
        for (JobStatusEnum e : JobStatusEnum.values()) {
            if (e.getCode().equals(key)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取value
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取code
     */
    public String getCode() {
        return code;
    }
}
