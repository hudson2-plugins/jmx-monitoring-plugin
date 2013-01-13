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
import hudson.model.Item;
import hudson.model.Project;
import hudson.model.listeners.ItemListener;
import java.lang.management.ManagementFactory;
import javax.inject.Named;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobState;
import org.hudsonci.plugins.jmxmonitoring.mbeans.JobStateMBean;
import org.slf4j.LoggerFactory;

@Named
public class JobLoadedListener extends ItemListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Plugin.class);

    @Override
    public void onLoaded() {
        LOGGER.info("Initial setup of job MBeans");
        for (Project p : Hudson.getInstance().getProjects()) {
            registerJobBean(p.getName());
        }
    }

    @Override
    public void onDeleted(Item item) {
        LOGGER.info("Job deleted, unregistering bean for "+item.getName());
        unregisterJobBean(item.getName());
    }

    @Override
    public void onCreated(Item item) {
        LOGGER.info("Job created, registering bean for "+item.getName());
        registerJobBean(item.getName());
    }

    @Override
    public void onCopied(Item src, Item item) {
        LOGGER.info("Job created, registering bean for "+item.getName());
        registerJobBean(item.getName());
    }

    @Override
    public void onRenamed(Item item, String oldName, String newName) {
        LOGGER.info("Job renamed, unregistering bean for "+oldName+ " and registering for "+newName);
        unregisterJobBean(oldName);
        registerJobBean(newName);
    }

    private void registerJobBean(String name) {
        try {
            JobStateMBean jobMbean = new JobState(name);
            ManagementFactory.getPlatformMBeanServer().registerMBean(jobMbean, getObjectName(name));
        } catch (JMException e) {
            LOGGER.error("Failed to reconfigure MBeans for job", e);
        }
    }

    private void unregisterJobBean(String name) {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(getObjectName(name));
        } catch (JMException e) {
            LOGGER.error("Failed to reconfigure MBeans for job", e);
        }
    }

    private ObjectName getObjectName(String name) {
        assert (name != null);
        try {
            return new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Job,name=" + name);
        } catch (MalformedObjectNameException ex) {
            LOGGER.error("Failed to reconfigure MBeans for job", ex);
            return null;
        }
    }
}
