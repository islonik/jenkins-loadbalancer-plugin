package org.jenkins.plugin.loadbalancer.impl;

import org.jenkins.plugin.loadbalancer.impl.balancers.SimpleLoadBalancer;

import hudson.model.LoadBalancer;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Nikita Lipatov
 * Date: 22.03.12
 */
public class LoadBalancerManagerTest {

    @Test
    public void testLoadBalancerManager_getDefaultSelectedAlgorithm() {
        Assert.assertEquals(
                LoadBalancerManager.CLASSIC,
                LoadBalancerManager.getInstance().getSelectedAlgorithm());
    }

    @Test
    public void testLoadBalancerManager_getClassicAlgorithmClass() {
        Assert.assertEquals(
                LoadBalancer.class,
                LoadBalancerManager.getInstance().getAlgorithmClass(LoadBalancerManager.CLASSIC));
    }

    @Test
    public void testLoadBalancerManager_getFreeExecutorsAlgorithmClass() {
        Assert.assertEquals(
                SimpleLoadBalancer.class,
                LoadBalancerManager.getInstance().getAlgorithmClass(LoadBalancerManager.FREE_EXECUTORS));
    }

    @Test
    public void testLoadBalancerManager_getAlgorithmsSize() {
        Assert.assertEquals(
                2,
                LoadBalancerManager.getInstance().getAlgorithms().length);
    }

    @Test
    public void testLoadBalancerManager_getLoadBalancerInstance()
            throws InstantiationException, IllegalAccessException {
        LoadBalancer balancer = LoadBalancerManager.getLoadBalancerInstance(LoadBalancerManager.CLASSIC);
        Assert.assertTrue(balancer instanceof LoadBalancer);
    }

    @Test
    public void testLoadBalancerManager_getSimpleLoadBalancerInstance()
            throws InstantiationException, IllegalAccessException {
        LoadBalancer balancer = LoadBalancerManager.getLoadBalancerInstance(LoadBalancerManager.FREE_EXECUTORS);
        Assert.assertTrue(balancer instanceof SimpleLoadBalancer);
    }
}
