/*
 * Copyright (c) 2012 Henrik Lynggaard Hansen
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Henrik Lynggaard Hansen- initial API and implementation and/or initial documentation
 */

package org.hudsonci.plugins.jmxmonitoring;

import hudson.model.Hudson;
import hudson.model.Node;
import hudson.slaves.ComputerListener;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobState;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobStateMBean;

/**
 *
 * @author henrik
 */
@Named
public class NodeListener extends ComputerListener {
        
    @Override
    public void onConfigurationChange() {
        try {
            Set<ObjectName> queryNames = ManagementFactory.getPlatformMBeanServer().queryNames(new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Node,name=*"),null);
            for (ObjectName name : queryNames) {
                unregisterSlaveBean(name);
            }
            for (Node n : Hudson.getInstance().getNodes()) {            
                registerSlaveBean(n.getNodeName());        
            }
        } catch (MalformedObjectNameException ex) {
            Logger.getLogger(NodeListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(NodeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    private void registerSlaveBean(String name) {
        try {
            JobStateMBean jobMbean = new JobState(name);
            ManagementFactory.getPlatformMBeanServer().registerMBean(jobMbean, getObjectName(name));
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }    
    
    private void unregisterSlaveBean(ObjectName name) {
        
        try {         
            ObjectName masterName = new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Node,name=Master");
            if (!name.equals(masterName)) {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    private ObjectName getObjectName(String name) {
        assert (name != null);
        try {
            return new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Node,name=" + name);
        } catch (MalformedObjectNameException ex) {
            Logger.getLogger(JobLoadedListener.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }    
}
