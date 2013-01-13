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
import javax.inject.Named;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobState;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobStateMBean;
import org.slf4j.LoggerFactory;

@Named
public class NodeListener extends ComputerListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Plugin.class);
    private static ObjectName QUERY_NAME = getObjectName("*");
    private static ObjectName MASTER_NAME = getObjectName("Master");

    @Override
    public void onConfigurationChange() {
        LOGGER.info("Hudson nodes reconfigured, reconfiguring MBeans");
        Set<ObjectName> queryNames = ManagementFactory.getPlatformMBeanServer().queryNames(QUERY_NAME, null);
        for (ObjectName name : queryNames) {
            unregisterSlaveBean(name);
        }
        for (Node n : Hudson.getInstance().getNodes()) {
            registerSlaveBean(n.getNodeName());
        }
    }

    private void registerSlaveBean(String name) {
        try {
            ObjectName oname = getObjectName(name);
            if (oname != null) {
                LOGGER.debug("Registering bean " + oname);
                JobStateMBean jobMbean = new JobState(name);
                ManagementFactory.getPlatformMBeanServer().registerMBean(jobMbean, oname);
            }
        } catch (JMException ex) {
            LOGGER.error("Failed to reconfigure MBeans for nodes", ex);
        }
    }

    private void unregisterSlaveBean(ObjectName name) {

        try {
            LOGGER.debug("unregistering bean " + name);
            if (!name.equals(MASTER_NAME)) {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(name);
            }
        } catch (JMException ex) {
            LOGGER.error("Failed to reconfigure MBeans for nodes", ex);
        }
    }

    private static ObjectName getObjectName(String name) {
        assert (name != null);
        try {
            return new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Node,name=" + name);
        } catch (MalformedObjectNameException ex) {
            LOGGER.error("Failed to reconfigure MBeans for nodes", ex);
            return null;
        }
    }
}
