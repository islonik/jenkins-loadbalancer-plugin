package org.jenkins.plugin.loadbalancer.impl.balancers.executors;

import hudson.model.Computer;
import hudson.model.Node;
import jenkins.model.Jenkins;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * User: Nikita Lipatov
 * Date: 07.03.12
 */
public class ExecutorsManager {
    private int alreadyDistributed = 0;

    private Computer []computers = null;
    private List<ExecutorCounter> counters = null;
    private List<Node> existNodes = null;

    public ExecutorsManager(int alreadyDistributed) {
        this.alreadyDistributed = alreadyDistributed;
        this.computers = Jenkins.getInstance().getComputers();
    }

    public ExecutorsManager(int alreadyDistributed, List existNodes) {
        this.alreadyDistributed = alreadyDistributed;
        this.existNodes = existNodes;
    }

    public Computer getPreferableComputerForRunNewTask() {
        if(counters == null) {
            setCounters();
        }
        // handle for one project in group of project
        Collections.sort(counters);
        
        // handle for lots of projects in group of projects
        for(int i = this.alreadyDistributed; i > 0; i--) {
            if(counters.isEmpty()) {
                return null;
            }
            ExecutorCounter counter = counters.get(0);
            counter.increaseBusyExecutors();
            Collections.sort(counters);
        }
        return (!counters.isEmpty()) ? counters.get(0).getComputer() : null;
    }

    private void setCounters() {
        counters = new LinkedList<ExecutorCounter>();
        if(existNodes == null) {
            for(int i = 0; i < computers.length; i++) {
                Computer computer = computers[i];
                ExecutorCounter executorCounter = new ExecutorCounter(computer);
                counters.add(executorCounter);
            }
        } else {
            setCounters(existNodes);
            existNodes = null;
        }
    }

    private void setCounters(List<Node> existNodes) {
        counters = new LinkedList<ExecutorCounter>();

        for(int i = 0; i < existNodes.size(); i++) {
            Node node = existNodes.get(i);

            Computer computer = node.toComputer();
            ExecutorCounter executorCounter = new ExecutorCounter(computer);
            counters.add(executorCounter);
        }
    }

}
