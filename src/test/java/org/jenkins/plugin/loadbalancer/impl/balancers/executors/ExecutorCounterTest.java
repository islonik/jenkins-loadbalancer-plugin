package org.jenkins.plugin.loadbalancer.impl.balancers.executors;

import hudson.model.Computer;
import hudson.model.Node;
import jenkins.model.Jenkins;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Nikita Lipatov
 * Date: 15.03.12
 */
public class ExecutorCounterTest extends LoadBalancerTestCase  {

    @Test
    public void testExecutorCounter_testCase1() throws Exception {
        Node node01 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node01);

        Computer[]computers = Jenkins.getInstance().getComputers();
        ExecutorCounter counter = new ExecutorCounter(computers[1]);
        Assert.assertEquals("Total free executors", 5, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 1, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_1");
    }

    @Test
    public void testExecutorCounter_testCase2() throws Exception {
        Node node01 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node01);

        Computer[]computers = Jenkins.getInstance().getComputers();
        ExecutorCounter counter = new ExecutorCounter(computers[1]);
        Assert.assertEquals("Total free executors", 5, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 1, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors();

        Assert.assertEquals("Total free executors", 4, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 2, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors();

        Assert.assertEquals("Total free executors", 3, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 3, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors();

        Assert.assertEquals("Total free executors", 2, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 4, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors();

        Assert.assertEquals("Total free executors", 1, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 5, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors();

        Assert.assertEquals("Total free executors", 0, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 6, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        counter.increaseBusyExecutors(); // defense from negative value of free executors

        Assert.assertEquals("Total free executors", 0, counter.getTotalFreeExecutors());
        Assert.assertEquals("Total busy executors", 6, counter.getTotalBusyExecutors());
        Assert.assertEquals("Total executors", 6, counter.getTotalExecutors());

        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_1");
    }
}
