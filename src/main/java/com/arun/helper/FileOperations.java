package com.arun.helper;

import java.io.File;
import java.util.List;


public interface FileOperations {

	/**
	 * Create a Java source file from problem code
	 *
	 * @param codeLines
	 * @param className
	 * @param language
	 * @return
	 */
	File createFile(List<String> codeLines, String className, String language);

	/**
	 * Compile and check for errors
	 *
	 * @param file
	 * @return
	 */
	List<String> syntaxChecker(String file);

	/**
	 * Execute Java code file
	 *
	 * @param file
	 * @return
	 */
	List<String> executeCode(File file);

	/**
	 * Execute python source file
	 *
	 * @param file
	 * @return
	 */
	List<String> executePythonCode(File file);
	
}
