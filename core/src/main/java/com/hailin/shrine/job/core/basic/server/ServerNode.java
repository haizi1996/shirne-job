package com.hailin.shrine.job.core.basic.server;

import com.hailin.shrine.job.core.basic.JobRegistry;
import com.hailin.shrine.job.core.basic.storage.JobNodePath;

/**
 * shrine服务器节点名称的常量类
 * @author zhanghailin
 */
public class ServerNode {

    /**
     * 作业服务器信息根节点.
     */
    public static final String ROOT = "servers";

    public static final String IP = ROOT + "/%s/ip";

    private static final String SERVERS = ROOT + "/%s";

    public static final String STATUS_APPENDIX = "status";

    public static final String STATUS = ROOT + "/%s/" + STATUS_APPENDIX;

    public static final String PROCESS_SUCCESS_COUNT = ROOT + "/%s/processSuccessCount";

    public static final String PROCESS_FAILURE_COUNT = ROOT + "/%s/processFailureCount";

    /** server 版本 **/
    public static final String VERSION = ROOT + "/%s/version";

    /** server 版本 **/
    public static final String JOB_VERSION = ROOT + "/%s/jobVersion";

    /** 对应的Executor运行节点 */
    public static final String SERVER = ROOT + "/%s";

    public static final String RUNONETIME = ROOT + "/%s/runOneTime";

    public static final String STOPONETIME = ROOT + "/%s/stopOneTime";

    private final String jobName;

    private final JobNodePath jobNodePath;

    public ServerNode(final String jobName) {
        this.jobName = jobName;
        jobNodePath = new JobNodePath(jobName);
    }

    static String getVersionNode(String executorName) {
        return String.format(VERSION, executorName);
    }

    static String getJobVersionNode(String executorName) {
        return String.format(JOB_VERSION, executorName);
    }

    static String getIpNode(String executorName) {
        return String.format(IP, executorName);
    }

    static String getStatusNode(String executorName) {
        return String.format(STATUS, executorName);
    }

    static String getProcessSuccessCountNode(String executorName) {
        return String.format(PROCESS_SUCCESS_COUNT, executorName);
    }

    static String getProcessFailureCountNode(String executorName) {
        return String.format(PROCESS_FAILURE_COUNT, executorName);
    }

    static String getRunOneTimePath(String executorName) {
        return String.format(ServerNode.RUNONETIME, executorName);
    }

    static String getStopOneTimePath(String executorName) {
        return String.format(ServerNode.STOPONETIME, executorName);
    }

    /** 对应的Executor运行节点 */
    static String getServerNodePath(String executorName) {
        return String.format(SERVER, executorName);
    }

    /**
     * 判断给定路径是否为本地作业服务器路径.
     *
     * @param path 待判断的路径
     * @return 是否为本地作业服务器路径
     */
    public boolean isLocalServerPath(final String path) {
        return path.equals(jobNodePath.getFullPath(String.format(SERVERS, JobRegistry.getInstance().getJobInstance(jobName).getIp())));
    }

    /**
     * @return 运行态的server的ZK节点路径
     */
    public static String getServerNode(final String jobName, String executorName) {
        return JobNodePath.getNodeFullPath(jobName, getServerNodePath(executorName));
    }

    public static String getServerRoot(final String jobName) {
        return JobNodePath.getNodeFullPath(jobName, ROOT);
    }

    public static boolean isRunOneTimePath(final String jobName, String path, String executorName) {
        return path
                .startsWith(JobNodePath.getNodeFullPath(jobName, String.format(ServerNode.RUNONETIME, executorName)));
    }

    public static boolean isStopOneTimePath(final String jobName, String path, String executorName) {
        return path
                .startsWith(JobNodePath.getNodeFullPath(jobName, String.format(ServerNode.STOPONETIME, executorName)));
    }
    String getServerNode(final String ip) {
        return String.format(SERVERS, ip);
    }
}
