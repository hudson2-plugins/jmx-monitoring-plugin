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

import java.util.List;

/**
 *
 * @author henrik
 */
public interface OverviewMBean {
    
    List<String> getJobNames();
    
    List<String> getSlaveNames();
  
    int getExecutors();
    int getExecutorsIdle();
   
    
    int getQueueLength();
    
//    int getDiskUsage();
}
