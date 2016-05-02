package com.felix.unbiz.json.rpc.client.ha;

/**
 * ClassName: FailFastStrategy <br/>
 * Function: 失败立即退出策略
 *
 * @author wangxujin
 */
public class FailFastStrategy implements FailStrategy {

    @Override
    public boolean isQuitImmediately(int currentRetryTime, int clientSize) {
        return true;
    }

    @Override
    public int getMaxRetryTimes() {
        return 1;
    }

}
