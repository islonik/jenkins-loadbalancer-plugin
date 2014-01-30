package org.jenkins.plugin.loadbalancer.impl.balancers.executors;

import hudson.model.Computer;
import hudson.model.Executor;

import java.util.List;
import java.util.logging.Logger;

/**
 * User: Nikita Lipatov
 * Date: 07.03.12
 */
public class ExecutorCounter implements Comparable {
    private static final Logger LOGGER
            = Logger.getLogger(ExecutorCounter.class.getName());

    private Computer computer = null;
    private int totalExecutors = -1;
    private int totalFreeExecutors = -1;

    public ExecutorCounter(Computer computer) {
        this.computer = computer;
        List<Executor> executors = computer.getExecutors();
        totalExecutors = executors.size();
        for(int i = 0; i < totalExecutors; i++) {
            Executor executor = executors.get(i);
            if(!executor.isBusy()) {
                totalFreeExecutors++;
            }
        }
    }

    public void increaseBusyExecutors() {
        if(this.totalFreeExecutors > 0) {
            this.totalFreeExecutors--;
        }
    }

    public Computer getComputer() {
        return computer;
    }

    public int getTotalExecutors() {
        return totalExecutors;
    }

    public int getTotalFreeExecutors() {
        return totalFreeExecutors;
    }

    public int getTotalBusyExecutors() {
        return (totalExecutors - totalFreeExecutors);
    }

    // now working as expected for Collections.sort
    public int compareTo(Object object) {
        ExecutorCounter entry = (ExecutorCounter)object;
        if(entry.getTotalFreeExecutors() > this.totalFreeExecutors) {
            return 1;
        } else if(entry.getTotalFreeExecutors() < this.totalFreeExecutors) {
            return -1;
        } else {
            return 0;
        }
    }

}
