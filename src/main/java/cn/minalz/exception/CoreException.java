package cn.minalz.exception;

public class CoreException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CoreException(String msg) {
        super(msg);
    }

    public static CoreException of(String msg){
        return new CoreException(msg);
    }

}