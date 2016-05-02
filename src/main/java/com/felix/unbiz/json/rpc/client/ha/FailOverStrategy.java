package com.felix.unbiz.json.rpc.client.ha;

/**
 * ClassName: FailOverStrategy <br/>
 * Function: 失败重试策略
 *
 * @author wangxujin
 */
public class FailOverStrategy implements FailStrategy {

    /**
     * 最大重试次数，默认为2
     */
    private int maxRetryTimes = 2;

    /**
     * Creates a new instance of FailOverStrategy.
     */
    public FailOverStrategy() {

    }

    /**
     * Creates a new instance of FailOverStrategy.
     *
     * @param maxRetryTimes
     */
    public FailOverStrategy(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    @Override
    public boolean isQuitImmediately(int currentRetryTime, int clientSize) {
        if (currentRetryTime + 1 == getMaxRetryTimes() || currentRetryTime + 1 == clientSize) {
            return true;
        }
        return false;
    }

    @Override
    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

}
