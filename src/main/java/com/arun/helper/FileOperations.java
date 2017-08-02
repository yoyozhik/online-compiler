package com.arun.helper;

import java.io.File;
import java.util.List;


public interface FileOperations {

	File createFile(List<String> codeLines, String className, String language);
		
	List<String> syntaxChecker(String file);
	
	List<String> executeCode(File file);
	
	List<String> executePythonCode(File file);
	
}
