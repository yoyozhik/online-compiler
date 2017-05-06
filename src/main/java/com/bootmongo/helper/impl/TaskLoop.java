package com.bootmongo.helper.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class TaskLoop extends TimerTask {
	private String command;
	private List<String> output;

	public TaskLoop(String cmd, List<String> out){

		this.command = cmd;
		this.output = out;
	}

	public void run() {
		System.out.println("here"); //timer's up, do whatever you need to do

		Process pro = null;

		try {
			pro = Runtime.getRuntime().exec(command);
			if (null != pro.getErrorStream()) {
				output = getOutput(pro.getErrorStream());
			} 
			if (null != pro.getInputStream()) {
				output = getOutput(pro.getInputStream());
			}
			pro.waitFor();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private List<String> getOutput(InputStream ins) throws Exception {
		List<String> results = new ArrayList<String>();
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			results.add(line);
		}
		return results;
	}
}