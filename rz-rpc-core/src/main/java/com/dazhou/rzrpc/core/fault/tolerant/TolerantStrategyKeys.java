package com.dazhou.rzrpc.core.fault.tolerant;

    /**
     * 容错策略键名常量
     *
     * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
     * @create 2024-03-31 20:56
     */
    public interface TolerantStrategyKeys {
        /**
         * 故障恢复
         */
        String FAIL_BACK = "failBack";

        /**
         * 快速失败
         */
        String FAIL_FAST = "failFast";

        /**
         * 故障转移
         */
        String FAIL_OVER = "failOver";

        /**
         * 静默处理
         */
        String FAIL_SAFE = "failSafe";
    }
