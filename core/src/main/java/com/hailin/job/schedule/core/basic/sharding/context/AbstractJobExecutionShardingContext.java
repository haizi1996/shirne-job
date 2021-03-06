package com.hailin.job.schedule.core.basic.sharding.context;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 作业运行时分片上下文抽象类
 * @author zhanghailin
 */
@Getter
public abstract class AbstractJobExecutionShardingContext {


    /**
     * 作业任务ID.
     */
    private  String taskId;
    //作业名称
    private String jobName;

    //分片总数
    private int shardingTotalCount;

    //作业自定义参数
    private String jobParameter;

    // 自定义上下文
    private Map<String , String> customContext;

    /**
     * 是否允许可以发送作业事件.
     */
    @Setter
    private boolean allowSendJobEvent = true;

    protected Map<Integer , String> shardingItemParameters = Maps.newHashMap();


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public Map<String, String> getCustomContext() {
        return customContext;
    }

    public void setCustomContext(Map<String, String> customContext) {
        this.customContext = customContext;
    }

    public Map<Integer, String> getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(Map<Integer, String> shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }
}
