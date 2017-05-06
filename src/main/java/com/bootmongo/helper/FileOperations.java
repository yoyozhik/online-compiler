package com.bootmongo.helper;

import java.io.File;
import java.util.List;


public interface FileOperations {

	public File createFile(List<String> codeLines, String className, String language);
		
	public List<String> syntaxChecker(String file);
	
	public List<String> executeCode(File file);
	
	public List<String> executePythonCode(File file);
	
}
