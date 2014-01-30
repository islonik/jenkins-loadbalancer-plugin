package org.jenkins.plugin.loadbalancer.impl.balancers.executors;

import hudson.EnvVars;
import hudson.model.Descriptor;
import hudson.model.Node;
import hudson.slaves.ComputerLauncher;
import hudson.slaves.DumbSlave;
import hudson.slaves.NodeProperty;
import hudson.slaves.RetentionStrategy;
import jenkins.model.Jenkins;
import org.jvnet.hudson.test.HudsonTestCase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * User: Nikita Lipatov
 * Date: 15.03.12
 */
public abstract class LoadBalancerTestCase extends HudsonTestCase {
    public static final String SLAVE_NODE_NAME = "slave";

    public Node createNode(String name, String execNumbers)
            throws IOException, Descriptor.FormException, URISyntaxException {
        Node.Mode mode = Jenkins.getInstance().getMode();
        EnvVars vars = new EnvVars();
        ComputerLauncher launcher = this.createComputerLauncher(vars);
        DumbSlave slave =
                new DumbSlave(
                        name, "some description", "wtf", execNumbers, mode, "some_label",
                        launcher,
                        RetentionStrategy.INSTANCE,
                        new ArrayList<NodeProperty<?>>());
        return slave;
    }

    public void removeFourNodes() throws IOException {
        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_1");
        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_2");
        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_3");
        this.removeSlaveFromJenkins(SLAVE_NODE_NAME + "_4");
    }

    public void removeSlaveFromJenkins(String nodeName) throws IOException {
        Node node = jenkins.getNode(nodeName);
        jenkins.removeNode(node);
    }
}
