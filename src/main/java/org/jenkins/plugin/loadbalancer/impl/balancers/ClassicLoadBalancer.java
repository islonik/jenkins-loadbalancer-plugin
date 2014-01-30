package org.jenkins.plugin.loadbalancer.impl.balancers;

import hudson.model.LoadBalancer;
import hudson.model.Queue;
import hudson.model.queue.MappingWorksheet;

/**
 * User: Nikita Lipatov
 * This LoadBalancer uses standard / classic algorithm of Jenkins.
 * Date: 01.04.12
 */
public class ClassicLoadBalancer {
    private static final LoadBalancer CLASSIC_BASE = LoadBalancer.CONSISTENT_HASH;
    private static LoadBalancer CLASSIC = null;
    
    public static LoadBalancer getInstance() {
        if(CLASSIC == null) {
            CLASSIC = new LoadBalancer() {
                @Override
                public MappingWorksheet.Mapping map(Queue.Task task, MappingWorksheet worksheet) {
                    if (Queue.ifBlockedByHudsonShutdown(task)) {
                        // if we are quieting down, don't start anything new so that
                        // all executors will be eventually free.
                        return null;
                    }
                    return CLASSIC_BASE.map(task, worksheet);
                }
        
                /**
                 * Double-sanitization is pointless.
                 */
                @Override
                protected LoadBalancer sanitize() {
                    return this;
                }
            };   
        }
        return CLASSIC;
    }
}
