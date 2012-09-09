jmx-monitoring-plugin
=====================

This plugins exposes several key metrics from Hudson as JMX MBeans so they 
can be queried by monitoring tools.

org.hudsonci.plugin.jmxmonitoring:type=Hudson
----------------------------------------
Executors: total number of executors 
ExecutorsIdle: Number of idle executors
QueueLength: Length of the queue in hudson
JobNames: Array of job names
SlaveNames: array of names of slaves


org.hudsonci.plugin.jmxmonitoring:type=Node,name=<Node Name>
-----------------------------------------------------
Architecture: 
AvailablePhysicalMemory: in bytes
AvailableSwapSpace: in bytes
ClockDifference: in ms
Executors: Total number of executors
ExecutorsBusy: Number of busy executors
ExecutorsIdle: Number of idle executors
FreeDiskspaceHudson: Free disk space where hudson_home is located
FreeDiskspaceTemporary: Free disk space where java.io.tmp points
Name: 
Online: boolean
ResponseTime: in ms
TotalPhysicalMemory: in bytes 
TotalSwapSpace: in bytes


org.hudsonci.plugin.jmxmonitoring:type=Job,name=<Job Name>
-----------------------------------------------------
BuildCount:
Building
InQueue:
Name:
