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
package org.hudsonci.plugins.jmxmonitoring.mbeans;

import hudson.model.Hudson;
import hudson.model.Node;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author henrik
 */
public class Overview implements OverviewMBean {

    public List<String> getJobNames() {
        return new ArrayList<String>(Hudson.getInstance().getJobNames());        
    }

    public List<String> getSlaveNames() {
        List<String> result = new ArrayList<String>(Hudson.getInstance().getNodes().size());
        for (Node n : Hudson.getInstance().getNodes()) {
            result.add(n.getNodeName());
        }
        return result;
    }

    @Override
    public int getExecutorsIdle() {
        return Hudson.getInstance().overallLoad.computeIdleExecutors();
    }

    @Override
    public int getExecutors() {
        return Hudson.getInstance().overallLoad.computeTotalExecutors();        
    }

    public int getQueueLength() {
        return Hudson.getInstance().overallLoad.computeQueueLength();
    }
    
//    public int getDiskUsage() {
//        return -1;
//    }
}
