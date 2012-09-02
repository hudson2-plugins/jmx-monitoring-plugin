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

import hudson.model.Computer;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.node_monitors.ResponseTimeMonitor;
import hudson.node_monitors.SwapSpaceMonitor;
import hudson.util.ClockDifference;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author henrik
 */
public class SlaveState implements SlaveStateMBean {

    private String name;
    private Node node;

    public SlaveState(String name) {
        this.name = name;
        node = name != null ? Hudson.getInstance().getNode(name) : Hudson.getInstance();
    }

    @Override
    public String getName() {
        return name != null ? name : "master";
    }

    @Override
    public boolean isOnline() {
        return getComputer() != null;
    }

    @Override
    public long getExecutors() {
        return node.getNumExecutors();
    }

    @Override
    public long getExecutorsIdle() {
        Computer comp = getComputer();
        return comp != null ? comp.getIdleCount() : 0;
    }

    @Override
    public long getExecutorsBusy() {
        Computer comp = getComputer();
        return comp != null ? comp.getBusyCount() : 0;
    }

    @Override
    public String getArchitecture() {
        return (String) getMonitorData("hudson.node_monitors.ArchitectureMonitor");

    }

    @Override
    public long getClockDifference() {
        ClockDifference diff = (ClockDifference) getMonitorData("hudson.node_monitors.ClockMonitor");
        return diff != null ? diff.abs() : -1;
    }

    @Override
    public long getTotalPhysicalMemory() {
        SwapSpaceMonitor.MemoryUsage memory = (SwapSpaceMonitor.MemoryUsage) getMonitorData("hudson.node_monitors.SwapSpaceMonitor");
        return memory != null ? memory.getTotalPhysicalMemory() : -1;
    }

    @Override
    public long getAvailablePhysicalMemory() {
        SwapSpaceMonitor.MemoryUsage memory = (SwapSpaceMonitor.MemoryUsage) getMonitorData("hudson.node_monitors.SwapSpaceMonitor");
        return memory != null ? memory.getAvailablePhysicalMemory() : -1;
    }

    @Override
    public long getTotalSwapSpace() {
        SwapSpaceMonitor.MemoryUsage memory = (SwapSpaceMonitor.MemoryUsage) getMonitorData("hudson.node_monitors.SwapSpaceMonitor");
        return memory != null ? memory.getTotalSwapSpace() : -1;
    }

    @Override
    public long getAvailableSwapSpace() {
        SwapSpaceMonitor.MemoryUsage memory = (SwapSpaceMonitor.MemoryUsage) getMonitorData("hudson.node_monitors.SwapSpaceMonitor");
        return memory != null ? memory.getAvailableSwapSpace() : -1;
    }

    @Override
    public long getResponseTime() {
        ResponseTimeMonitor.Data monitor = (ResponseTimeMonitor.Data) getMonitorData("hudson.node_monitors.ResponseTimeMonitor");
        return monitor.getAverage();
    }

    @Override
    public long getFreeDiskspaceTemporary() {
        Object data = getMonitorData("hudson.node_monitors.TemporarySpaceMonitor");
        return data != null ? Long.parseLong(data.toString()) : -1;
    }

    @Override
    public long getFreeDiskspaceHudson() {
        Object data = getMonitorData("hudson.node_monitors.DiskSpaceMonitor");
        return data != null ? Long.parseLong(data.toString()) : -1;
    }

    private Computer getComputer() {
        Computer comp = node.toComputer();

        // offline
        if (comp == null) {
            return null;
        }
        return comp.isOnline() ? comp : null;
    }

    private Object getMonitorData(String name) {
        Computer comp = getComputer();
        Map<String, Object> data = comp != null ? comp.getMonitorData() : Collections.EMPTY_MAP;
        return data.get(name);
    }
}
