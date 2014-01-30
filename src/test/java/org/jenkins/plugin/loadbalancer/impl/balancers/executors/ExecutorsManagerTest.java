package org.jenkins.plugin.loadbalancer.impl.balancers.executors;

import hudson.model.Computer;
import hudson.model.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Nikita Lipatov
 * Date: 11.03.12
 */
//public class ExecutorsManagerTest {
public class ExecutorsManagerTest extends LoadBalancerTestCase {

    @Test
    public void testExecutorsManager_directSorting_testCase01() throws Exception {
        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "10");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "14");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "18");
        jenkins.addNode(node4);

        ExecutorsManager manager = new ExecutorsManager(0);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_4", computer.getName());

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_directSorting_testCase02() throws Exception {
        List existNodes = new LinkedList();

        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "10");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "14");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "18");
        jenkins.addNode(node4);

        existNodes.add(node3);

        ExecutorsManager manager = new ExecutorsManager(0, existNodes);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_3", computer.getName());

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_directSorting_testCase03() throws Exception {
        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "10");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "14");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "18");
        jenkins.addNode(node4);

        ExecutorsManager manager = new ExecutorsManager(9);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_3", computer.getName());

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_directSorting_testCase04() throws Exception {
        List existNodes = new LinkedList();

        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "8");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "10");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "14");
        jenkins.addNode(node4);

        ExecutorsManager manager = new ExecutorsManager(1, existNodes);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertNull(computer);

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_directSorting_testCase05() throws Exception {
        List existNodes = new LinkedList();

        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "6");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "8");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "10");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "14");
        jenkins.addNode(node4);

        ExecutorsManager manager = new ExecutorsManager(0, existNodes);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertNull(computer);

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_reverseSorting_testCase01() throws Exception  {
        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "10");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "8");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "6");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "4");
        jenkins.addNode(node4);
        
        ExecutorsManager manager = new ExecutorsManager(0);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_1", computer.getName());

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_reverseSorting_testCase02() throws Exception  {
        List existNodes = new LinkedList();

        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "10");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "6");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "4");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "2");
        jenkins.addNode(node4);

        existNodes.add(node3);
        existNodes.add(node4);

        ExecutorsManager manager = new ExecutorsManager(0, existNodes);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_3", computer.getName());

        this.removeFourNodes();
    }

    @Test
    public void testExecutorsManager_reverseSorting_testCase03() throws Exception  {
        List existNodes = new LinkedList();

        Node node1 = this.createNode(SLAVE_NODE_NAME + "_1", "14");
        jenkins.addNode(node1);
        Node node2 = this.createNode(SLAVE_NODE_NAME + "_2", "10");
        jenkins.addNode(node2);
        Node node3 = this.createNode(SLAVE_NODE_NAME + "_3", "6");
        jenkins.addNode(node3);
        Node node4 = this.createNode(SLAVE_NODE_NAME + "_4", "2");
        jenkins.addNode(node4);

        existNodes.add(node2);
        existNodes.add(node3);

        ExecutorsManager manager = new ExecutorsManager(5, existNodes);
        Computer computer = manager.getPreferableComputerForRunNewTask();
        Assert.assertEquals("direct sorting", SLAVE_NODE_NAME + "_3", computer.getName());

        this.removeFourNodes();
    }

}
