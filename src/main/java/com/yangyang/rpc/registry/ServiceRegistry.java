package com.yangyang.rpc.registry;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册
 * @author chenshunyang
 *
 */
public class ServiceRegistry {
	 private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
	 
	 private CountDownLatch latch = new CountDownLatch(1);
	 private String registryAddress;
	 
	 public ServiceRegistry(String registryAddress){
		 this.registryAddress = registryAddress;
	 }
	 
	 public void register(String data){
		 if (data != null) {
			ZooKeeper zk = connectZookeeper();
			 if (zk != null) {
				 addRootNode(zk); // Add root node if not exist
	             createNode(zk, data);
	         }
		}
	 }

	private void addRootNode(ZooKeeper zk) {
		try {
			Stat stat = zk.exists(Constant.ZK_REGISTRY_PATH, false);
			if (stat == null) {
				zk.create(Constant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (KeeperException e) {
			LOGGER.error("",e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.error("",e);
			e.printStackTrace();
		}
	}

	private void createNode(ZooKeeper zk, String data) {
		byte[] bytes = data.getBytes();
		try {
			//会话结束时节点会自动删除，OPEN_ACL_UNSAFE开放所有权限
			String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			LOGGER.debug("create zookeeper node ({} => {})", path, data);
		} catch (KeeperException e) {
			LOGGER.error("",e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.error("",e);
			e.printStackTrace();
		}
	}

	private ZooKeeper connectZookeeper() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher(){
				public void process(WatchedEvent event) {
					  // 判断是否已连接ZK,连接后计数器递减.
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
				}
				
			});
			//若计数器不为0，则等待
			latch.await();
		} catch (Exception e) {
			LOGGER.error("",e);
		}
		return zk;
	}

}

