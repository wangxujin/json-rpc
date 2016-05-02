package com.felix.unbiz.json.rpc.exception.rpc;

/**
 * ClassName: ErrorStatus <br>
 * Function: rpc错误状态
 *
 * @author wangxujin
 */
public enum ErrorStatus {

    PARSE_ERROR {
        @Override
        int getCode() {
            return -1;
        }

        @Override
        String getDesc() {
            return "参数转换异常";
        }
    },
    CODEC_ERROR {
        @Override
        int getCode() {
            return -2;
        }

        @Override
        String getDesc() {
            return "序列化异常";
        }
    },
    INVALID_REQUEST {
        @Override
        int getCode() {
            return -3;
        }

        @Override
        String getDesc() {
            return "rpc请求不规范";
        }
    },
    METHOD_NOT_FOUND {
        @Override
        int getCode() {
            return -4;
        }

        @Override
        String getDesc() {
            return "调用的方法找不到";
        }
    },
    INVALID_PARAM {
        @Override
        int getCode() {
            return -5;
        }

        @Override
        String getDesc() {
            return "rpc参数错误";
        }
    },
    INTERNAL_ERROR {
        @Override
        int getCode() {
            return -6;
        }

        @Override
        String getDesc() {
            return "rpc出现其他内部错误";
        }
    },
    SERVER_ERROR {
        @Override
        int getCode() {
            return -7;
        }

        @Override
        String getDesc() {
            return "rpc服务器端被调用函数内部发生了异常";
        }
    };

    /**
     * 错误编码
     *
     * @return
     */
    abstract int getCode();

    /**
     * 错误描述
     *
     * @return
     */
    abstract String getDesc();
}
