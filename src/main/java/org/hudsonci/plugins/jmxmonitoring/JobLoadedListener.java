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
public class JobLoadedListener extends ItemListener {

    public JobLoadedListener() {
        System.out.println("*** JobLoaded Listener");
    }

    @Override
    public void onLoaded() {
        System.out.println("Creating MBeans for  jobs");
        for (Project p : Hudson.getInstance().getProjects()) {
            registerJobBean(p.getName());
        }
    }

    @Override
    public void onDeleted(Item item) {
        unregisterJobBean(item.getName());
    }

    @Override
    public void onCreated(Item item) {
        registerJobBean(item.getName());
    }

    @Override
    public void onCopied(Item src, Item item) {
        registerJobBean(item.getName());
    }

    @Override
    public void onRenamed(Item item, String oldName, String newName) {
        unregisterJobBean(oldName);
        registerJobBean(newName);
    }

    private void registerJobBean(String name) {
        try {
            JobStateMBean jobMbean = new JobState(name);
            ManagementFactory.getPlatformMBeanServer().registerMBean(jobMbean, getObjectName(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterJobBean(String name) {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(getObjectName(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObjectName getObjectName(String name) {
        assert (name != null);
        try {
            return new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Job,name=" + name);
        } catch (MalformedObjectNameException ex) {
            Logger.getLogger(JobLoadedListener.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
