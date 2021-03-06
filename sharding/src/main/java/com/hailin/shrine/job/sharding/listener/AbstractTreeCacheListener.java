package com.hailin.shrine.job.sharding.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

public abstract class AbstractTreeCacheListener implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
        ChildData data = event.getData();
        if (data != null) {
            TreeCacheEvent.Type type = event.getType();
            String path = data.getPath();
            String nodeData = null;
            byte[] dataData = data.getData();
            if (dataData != null) {
                nodeData = new String(dataData, "UTF-8");
            }
            if (path != null) {
                childEvent(type, path, nodeData);
            }
        }
    }

    protected abstract void childEvent(TreeCacheEvent.Type type, String path, String nodeData) throws Exception;
}
