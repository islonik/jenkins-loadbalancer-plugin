package org.jenkins.plugin.loadbalancer;

import org.jenkins.plugin.loadbalancer.impl.LoadBalancerManager;
import org.jenkins.plugin.loadbalancer.impl.balancers.SimpleLoadBalancer;
import hudson.Plugin;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Initializer of new LoadBalancer should extend Plugin or will appear the errors in output
public class LoadBalancerPlugin extends Plugin {
    private static final Logger LOGGER
            = Logger.getLogger(SimpleLoadBalancer.class.getName());

    /**
     * API values
     **/
    private String[] algorithms = LoadBalancerManager.getInstance().getAlgorithms();
    private String selectedAlgorithm = LoadBalancerManager.getInstance().getSelectedAlgorithm();

    public void start() throws Exception {
        this.load();
        Jenkins.getInstance().getQueue().setLoadBalancer(
                LoadBalancerManager.getLoadBalancerInstance(selectedAlgorithm));
    }

    public void configure(StaplerRequest req, JSONObject formData) throws IOException {
        this.selectedAlgorithm = formData.getString("NewAlgorithm"); // get value from config.jelly

        try {
            Jenkins.getInstance().getQueue().setLoadBalancer(
                    LoadBalancerManager.getLoadBalancerInstance(selectedAlgorithm));
        } catch(InstantiationException ie) {
            LOGGER.log(Level.SEVERE, ie.getMessage());
        } catch(IllegalAccessException iae) {
            LOGGER.log(Level.SEVERE, iae.getMessage());
        }
        this.save();
    }

    /**
     * API for file config.jelly
     **/
    public String[] getAlgorithms() {
        return algorithms;
    }

    /**
     * API for file config.jelly
     */
    public String getSelectedAlgorithm() {
        return this.selectedAlgorithm;
    }

}

