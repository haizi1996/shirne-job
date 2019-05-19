package com.hailin.shrine.job.core.basic.control;


import com.hailin.shrine.job.core.basic.storage.JobNodePath;

/**
 * @author chembo.huang
 *
 */
public final class ControlNode {

	public static final String CONTROL_NODE = "control";
	public static final String REPORT_NODE = CONTROL_NODE + "/report";

	public static boolean isReportPath(final String jobName, final String path) {
		return JobNodePath.getNodeFullPath(jobName, REPORT_NODE).equals(path);
	}
}
