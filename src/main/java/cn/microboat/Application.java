package cn.microboat;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

/**
 * @author zhouwei
 */
public class Application {

    /**
     * 重试之间等待的初始时间
     */
    private static final int BASE_SLEEP_TIME = 1000;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;


    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                // 要连接的服务器
                .connectString("47.100.131.2:2181")
                // 重试策略
                .retryPolicy(exponentialBackoffRetry)
                .build();
        zkClient.start();

//        zkClient.create().forPath("/node3");
//        zkClient.create().forPath("/node3/0001");
//        zkClient.create().withMode(CreateMode.PERSISTENT).forPath("/node3/0002");

//        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/node4/0001");
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node5/0001");

        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node6/0001", "java".getBytes(StandardCharsets.UTF_8));
        zkClient.getData().forPath("/node6/0001");

        zkClient.checkExists().forPath("/node6/0001");

        zkClient.delete().forPath("/node6/0001");

//        zkClient.delete().deletingChildrenIfNeeded().forPath("/node2");

        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node8/0001", "zookeeper".getBytes(StandardCharsets.UTF_8));
        zkClient.getData().forPath("/node8/0001");
        zkClient.setData().forPath("/node8/0001", "c++".getBytes(StandardCharsets.UTF_8));

//        List<String> strings = zkClient.getChildren().forPath("/node3");


        String path = "/nodenode1";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);
        PathChildrenCacheListener pathChildrenCacheListener = ((curatorFramework, pathChildrenCacheEvent) -> {
            System.out.println("回调");
            System.out.println(pathChildrenCacheEvent.getType());
        });
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();

        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/nodenode1/0001");
    }
}
