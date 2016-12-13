package com.jing.jhttp.utils;

/**
 * author: 陈永镜 .
 * date: 2016/12/13 .
 * email: jing20071201@qq.com
 * <p>
 * introduce:
 * <p>
 * 根据错误码获取对应的消息
 */
public class CodeMsgUtils {

    public static class Code {
        public final static int noNetwork = 400;
    }

    /**
     * 根据错误码获取错误信息
     *
     * @param code
     * @return
     */
    public static String matchMsg(int code) {
        String msg = "";
        switch (code) {
            case Code.noNetwork:
                msg = "你的网络不稳定";
                break;
        }
        return msg;
    }

    public static String matchMsg(String code) {
        int codeInt = 0;
        try {
            codeInt = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return code;//不是整数，直接返回
        }

        return matchMsg(codeInt);
    }

}
