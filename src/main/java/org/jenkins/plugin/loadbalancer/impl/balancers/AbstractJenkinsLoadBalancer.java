package org.jenkins.plugin.loadbalancer.impl.balancers;

import hudson.model.LoadBalancer;
import hudson.model.Queue;
import hudson.model.queue.MappingWorksheet;
import hudson.util.ConsistentHash;

import java.util.List;

/**
 * User: Nikita Lipatov
 * This class is needed for the future algorithms.
 * Date: 23.03.12
 */
public abstract class AbstractJenkinsLoadBalancer extends LoadBalancer {

    protected boolean assignGreedily(
            MappingWorksheet.Mapping m,
            Queue.Task task,
              List<ConsistentHash<MappingWorksheet.ExecutorChunk>> hashes, int i) {
        if (i == hashes.size())   {
            return true;    // fully assigned
        }

        String key = task.getFullDisplayName() + (i > 0 ? String.valueOf(i) : "");

        for (MappingWorksheet.ExecutorChunk ec : hashes.get(i).list(key)) {
            // let's attempt this assignment
            m.assign(i, ec);

            if (m.isPartiallyValid() && assignGreedily(m, task, hashes, i+1))   {
                return true;    // successful greedily allocation
            }

            // otherwise 'ec' wasn't a good fit for us. try next.
        }

        // every attempt failed
        m.assign(i, null);
        return false;
    }
}
