package org.jenkins.plugin.loadbalancer.impl;

import org.jenkins.plugin.loadbalancer.impl.balancers.ClassicLoadBalancer;
import org.jenkins.plugin.loadbalancer.impl.balancers.SimpleLoadBalancer;

import hudson.model.LoadBalancer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Nikita Lipatov
 * Date: 19.03.12
 */
public class LoadBalancerManager {
    public static final String CLASSIC         = "Classic";
    public static final String FREE_EXECUTORS  = "Free Executors";

    private static final Logger LOGGER
            = Logger.getLogger(LoadBalancerManager.class.getName());
    private static LoadBalancerManager manager  = null;

    private HashMap<String, Class> algorithmsMap = new HashMap<String, Class>();
    private String selectedAlgorithm = null;

    public static LoadBalancerManager getInstance() {
        if(manager == null) {
            manager = new LoadBalancerManager();
        }
        return manager;
    }

    public LoadBalancerManager() {
        this.algorithmsMap.put(CLASSIC,           LoadBalancer.class);
        this.algorithmsMap.put(FREE_EXECUTORS,    SimpleLoadBalancer.class);
        
        this.selectedAlgorithm = CLASSIC;
    }
    
    public Class getAlgorithmClass(String key) {
        if(!this.algorithmsMap.containsKey(key)) {
            LOGGER.log(Level.SEVERE, "No such algorithm - " + key);
            throw new UnsupportedOperationException("No such algorithm - " + key);
        }
        return this.algorithmsMap.get(key);
    }

    public String[] getAlgorithms() {
        int arraySize = this.algorithmsMap.keySet().size();
        String []algorithms = new String[arraySize];
        Iterator<String> algorithmsKey = this.algorithmsMap.keySet().iterator();
        for(int i = 0; i < arraySize; i++) {
            String key = algorithmsKey.next();
            algorithms[i] = key;
        }
        return algorithms;
    }

    public String getSelectedAlgorithm() {
        return this.selectedAlgorithm;
    }

    public static LoadBalancer getLoadBalancerInstance(String key)
            throws InstantiationException, IllegalAccessException {
        Class balancerClass = LoadBalancerManager.getInstance().getAlgorithmClass(key);

        if(balancerClass.getName().equals(LoadBalancer.class.getName())) {
            return ClassicLoadBalancer.getInstance();
        }

        return ((LoadBalancer)balancerClass.newInstance());
    }
}
