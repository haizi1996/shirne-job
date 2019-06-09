package com.hailin.shrine.job.sharding.service;

import com.google.common.collect.Lists;
import com.hailin.shrine.job.sharding.exceptiom.ShardingException;
import com.hailin.shrine.job.sharding.node.ShrineExecutorsNode;
import lombok.AllArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@AllArgsConstructor
public class AddJobListenersService {

    private static final Logger log = LoggerFactory.getLogger(AddJobListenersService.class);

    private String namespace;
    private CuratorFramework curatorFramework;
    private NamespaceShardingService namespaceShardingService;
    private ShardingTreeCacheService shardingTreeCacheService;


    public void addExistJobPathListener() throws Exception {
        if (null != curatorFramework.checkExists().forPath(ShrineExecutorsNode.JOBSNODE_PATH)) {
            List<String> jobs = curatorFramework.getChildren().forPath(ShrineExecutorsNode.JOBSNODE_PATH);
            log.info("addExistJobPathListener, jobs = {}", jobs);
            if (jobs != null) {
                for (String job : jobs) {
                    addJobPathListener(job);
                }
            }
        }
    }
    public void addJobPathListener(String jobName) throws Exception {
        if (addJobConfigPathListener(jobName)) {
            addJobServersPathListener(jobName);
        }
    }
    public void removeJobPathTreeCache(String jobName) throws ShardingException {
        removeJobConfigPathTreeCache(jobName);
        removeJobServersPathTreeCache(jobName);
    }

    private void removeJobConfigPathTreeCache(String jobName) throws ShardingException {
        int depth = 0;
        List<String> configPath2AddTreeCacheList = Lists
                .newArrayList(ShrineExecutorsNode.getJobConfigEnableNodePath(jobName), ShrineExecutorsNode.getJobConfigForceShardNodePath(jobName));
        for (String path2AddTreeCache : configPath2AddTreeCacheList) {
            shardingTreeCacheService.removeTreeCache(path2AddTreeCache, depth);
        }
    }

    private void removeJobServersPathTreeCache(String jobName) throws ShardingException {
        String path = ShrineExecutorsNode.getJobServersNodePath(jobName);
        int depth = 2;
        shardingTreeCacheService.removeTreeCache(path, depth);
    }

    /**
     * Add job config path listener
     * @return false, if the job/config path is not existing
     */
    private boolean addJobConfigPathListener(String jobName) throws Exception {
        String path = ShrineExecutorsNode.JOBSNODE_PATH + "/" + jobName + "/config";
        String fullPath = namespace + path;

        int waitConfigPathCreatedCounts = 50;
        do {
            waitConfigPathCreatedCounts--;
            if (curatorFramework.checkExists().forPath(path) != null) {
                break;
            }
            if (waitConfigPathCreatedCounts == 0) {
                log.warn("the path {} is not existing", fullPath);
                return false;
            }
            Thread.sleep(100L);
        } while (true);

        int depth = 0;
        List<String> configPath2AddTreeCacheList = Lists
                .newArrayList(ShrineExecutorsNode.getJobConfigEnableNodePath(jobName),
                        ShrineExecutorsNode.getJobConfigForceShardNodePath(jobName));
        for (String path2AddTreeCache : configPath2AddTreeCacheList) {
            shardingTreeCacheService.addTreeCacheIfAbsent(path2AddTreeCache, depth);
            shardingTreeCacheService.addTreeCacheListenerIfAbsent(path2AddTreeCache, depth,
                    new JobConfigTriggerShardingListener(jobName, namespaceShardingService));
        }

        return true;
    }

    private void addJobServersPathListener(String jobName) throws Exception {
        String path = ShrineExecutorsNode.getJobServersNodePath(jobName);
        if (curatorFramework.checkExists().forPath(path) == null) {
            try {
                curatorFramework.create().creatingParentsIfNeeded().forPath(path);
            } catch (KeeperException.NodeExistsException e) {// NOSONAR
                log.info("node {} already existed, so skip creation", path);
            }
        }
        int depth = 2;
        shardingTreeCacheService.addTreeCacheIfAbsent(path, depth);
        shardingTreeCacheService.addTreeCacheListenerIfAbsent(path, depth, new JobServersTriggerShardingListener(jobName, namespaceShardingService));
    }
}
