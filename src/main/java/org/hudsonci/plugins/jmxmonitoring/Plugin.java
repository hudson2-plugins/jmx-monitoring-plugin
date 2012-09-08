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

import java.lang.management.ManagementFactory;
import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.hudsonci.plugins.jmxmonitoring.mbeans.Overview;
import org.hudsonci.plugins.jmxmonitoring.mbeans.SlaveState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author henrik
 */
@Named
public class Plugin extends hudson.Plugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(Plugin.class);                
    
    @Override
    public void postInitialize() throws Exception {
        LOGGER.info("Registering Hudson MBeans");
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Hudson");
        Overview mbean = new Overview();
        mbs.registerMBean(mbean, name);
        
        ObjectName masterName = new ObjectName("org.hudsonci.plugin.jmxmonitoring:type=Node,name=Master");
        SlaveState masterState = new SlaveState(null);
        mbs.registerMBean(masterState, masterName);
    }
}
