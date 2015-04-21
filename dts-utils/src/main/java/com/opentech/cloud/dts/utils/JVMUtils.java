package com.opentech.cloud.dts.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 
 * @author sihai
 *
 */
public class JVMUtils {
	
	public static final long getPid() {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

        //
        // Get name representing the running Java virtual machine.
        // It returns something like 6460@AURORA. Where the value
        // before the @ symbol is the PID.
        //
        String jvmName = bean.getName();
        //
        // Extract the PID by splitting the string returned by the
        // bean.getName() method.
        //
        long pid = Long.valueOf(jvmName.split("@")[0]);
        return pid;
	}
	
	public static void main(String[] args) {
		System.out.println(JVMUtils.getPid());
	}
}
