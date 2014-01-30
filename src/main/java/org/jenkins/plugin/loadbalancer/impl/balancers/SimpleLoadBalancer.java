package org.jenkins.plugin.loadbalancer.impl.balancers;

import org.jenkins.plugin.loadbalancer.impl.balancers.executors.ExecutorsManager;

import hudson.model.Computer;
import hudson.model.Node;
import hudson.model.Queue;
import hudson.model.Queue.Task;
import hudson.model.queue.MappingWorksheet.Mapping;
import hudson.model.queue.MappingWorksheet;
import hudson.util.ConsistentHash;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Nikita Lipatov
 * The first algorithm which was created and now is used in Jenkins.
 * Date: 06.03.12
 */
public class SimpleLoadBalancer extends AbstractJenkinsLoadBalancer {
    private static final Logger LOGGER
            = Logger.getLogger(SimpleLoadBalancer.class.getName());
    private int alreadyDistributed = 0;

    public SimpleLoadBalancer() {
        super();
        LOGGER.log(Level.INFO, "Simple Load Balancer is set!");
    }

    public Mapping map(Task task, MappingWorksheet ws) {

        if (Queue.ifBlockedByHudsonShutdown(task)) {
            return null;
        }

        this.alreadyDistributed = Queue.getInstance().getPendingItems().size();

        List existNodes = new LinkedList();

        List<ConsistentHash<MappingWorksheet.ExecutorChunk>> hashes =
                this.getFirstHashes(ws, existNodes);

        if(hashes == null) {
            hashes = this.getSecondHashes(ws, existNodes);
        }

        // do a greedy assignment
        Mapping m = ws.new Mapping();
        assert m.size() == ws.works.size();   // just so that you the reader of the source code don't get confused with the for loop index

        if (this.assignGreedily(m, task, hashes, 0)) {
            assert m.isCompletelyValid();
            return m;
        } else {
            return null;
        }
    }

    private List<ConsistentHash<MappingWorksheet.ExecutorChunk>> getFirstHashes(
            MappingWorksheet ws, List existNodes) {
        boolean isPrefComputerFound = false;
        List<ConsistentHash<MappingWorksheet.ExecutorChunk>> hashes =
                new ArrayList<ConsistentHash<MappingWorksheet.ExecutorChunk>>(ws.works.size());

        for (int i = 0; i < ws.works.size(); i++) {
            ConsistentHash<MappingWorksheet.ExecutorChunk> hash = createHash();

            ExecutorsManager manager = new ExecutorsManager(this.alreadyDistributed);
            Computer computer = manager.getPreferableComputerForRunNewTask();
            if(computer != null) {
                for (MappingWorksheet.ExecutorChunk ec : ws.works(i).applicableExecutorChunks()) {
                    Node node = ec.node;
                    if(node.equals(computer.getNode())) {
                        hash.add(ec, ec.size() * 100);
                        isPrefComputerFound = true;
                    } else {
                        existNodes.add(node);
                    }
                }
            }
            hashes.add(hash);
        }

        if(!isPrefComputerFound) {
            return null;
        }
        return hashes;
    }

    private List<ConsistentHash<MappingWorksheet.ExecutorChunk>> getSecondHashes(
            MappingWorksheet ws, List existNodes) {
        List<ConsistentHash<MappingWorksheet.ExecutorChunk>> hashes =
                new ArrayList<ConsistentHash<MappingWorksheet.ExecutorChunk>>(ws.works.size());
        for (int i = 0; i < ws.works.size(); i++) {
            ConsistentHash<MappingWorksheet.ExecutorChunk> hash = createHash();

            ExecutorsManager manager = new ExecutorsManager(this.alreadyDistributed, existNodes);
            Computer computer = manager.getPreferableComputerForRunNewTask();

            if(computer != null) {
                for (MappingWorksheet.ExecutorChunk ec : ws.works(i).applicableExecutorChunks()) {
                    if(ec.node.equals(computer.getNode())) {
                        hash.add(ec, ec.size() * 100);
                    }
                }
            }

            hashes.add(hash);
        }
        return hashes;
    }
    
    private ConsistentHash<MappingWorksheet.ExecutorChunk> createHash() {
        ConsistentHash<MappingWorksheet.ExecutorChunk> hash =
                new ConsistentHash<MappingWorksheet.ExecutorChunk>(new ConsistentHash.Hash<MappingWorksheet.ExecutorChunk>() {
                    public String hash(MappingWorksheet.ExecutorChunk node) {
                        return node.getName();
                    }
                });
        return hash;
    }



}
