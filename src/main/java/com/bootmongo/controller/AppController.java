package com.bootmongo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bootmongo.aop.logging.Loggable;
import com.bootmongo.domain.Problem;
import com.bootmongo.helper.FileOperations;
import com.bootmongo.request.FileSaveRequest;
import com.bootmongo.request.ProblemCreateRequest;
import com.bootmongo.request.UpdateRequest;
import com.bootmongo.response.CodeDetailsWrapper;
import com.bootmongo.response.CompileResponse;
import com.bootmongo.response.ProblemCode;
import com.bootmongo.response.ProblemResponseWrapper;
import com.bootmongo.service.MongoService;


@RestController
@RequestMapping(value = "/file")
public class AppController {
	
	@Autowired
	private MongoService mongoService;
	
	@Autowired
	private FileOperations fileOperations;
	
	@Autowired
	private Mapper dozerMapper;
	
	@Loggable
	@RequestMapping(value="/createProblem", method = RequestMethod.POST)
    public void submitProblem(@RequestBody ProblemCreateRequest problem){   
		FileSaveRequest fileSaveRequest = dozerMapper.map(problem, FileSaveRequest.class);	
		fileSaveRequest.setCode(fileOperations.createFile(problem.getCode(), problem.getClassName(), problem.getLanguage()));
        mongoService.saveCodeFile(fileSaveRequest);		        
    }
	
	@Loggable
	@RequestMapping(value="/compileCode", method = RequestMethod.POST)
	public CompileResponse compileCode(@RequestBody ProblemCreateRequest problem) {
		CompileResponse compiler = new CompileResponse();
		List<String> output = null;
		File codeFile = fileOperations.createFile(problem.getCode(), problem.getClassName(), problem.getLanguage());
		if (StringUtils.isNotBlank(problem.getLanguage()) && problem.getLanguage().equals("Java")) {
			output = fileOperations.syntaxChecker(codeFile.getAbsolutePath());
			if (CollectionUtils.isNotEmpty(output)) {
				compiler.setCompiledOutput(output);
				compiler.setFlag(false);
			} else {
				compiler.setCompiledOutput(fileOperations.executeCode(codeFile));
				compiler.setFlag(true);

			}
		} else if (problem.getLanguage().equals("Python")) {
			output = fileOperations.executePythonCode(codeFile);
			if(CollectionUtils.isNotEmpty(output)) {
				compiler.setCompiledOutput(output);
				compiler.setFlag(true);
			} else {
				compiler.setFlag(false);
			}
			compiler.setCompiledOutput(output);
		}
		
		return compiler;
	}
	
	@Loggable
	@RequestMapping(value="/admin/fetchProblemsByLanguage", method = RequestMethod.GET)
	public ProblemResponseWrapper fetchProblems(@RequestParam(value = "lang") final String lang, @RequestParam(value = "userId", required = false) final Integer userId) {
		ProblemResponseWrapper problems = new ProblemResponseWrapper();
		if(null != userId){
			problems.setProblems(mongoService.fetchProblemsByLanguage(lang, userId, true, null));
		}else{
			problems.setProblems(mongoService.fetchProblemsByLanguage(lang, 0, true, null));
		}
		return problems;
	}
	
	@Loggable
	@RequestMapping(value="/solver/fetchProblemsByLanguage", method = RequestMethod.GET)
	public ProblemResponseWrapper fetchSolverProblems(@RequestParam(value = "lang") final String lang, @RequestParam(value = "userName", required = false) final String userName ) {
		ProblemResponseWrapper problems = new ProblemResponseWrapper();
		if(StringUtils.isNotBlank(userName)){
			problems.setProblems(mongoService.fetchProblemsByLanguage(lang, 0, false, userName));
		} 
		return problems;
		
	}
	
	@Loggable
	@RequestMapping(value="/fetchProblemCode", method = RequestMethod.GET)
	public CodeDetailsWrapper fetchProblemCode(@RequestParam(value = "problemId") final String problemId){
		CodeDetailsWrapper codeWrapper = new CodeDetailsWrapper();
		List<ProblemCode> answersResponse = new ArrayList<ProblemCode>();
		Problem problem = mongoService.fetchFilesById(problemId);
		
		if(null != problem){
			List<String> codeLines = mongoService.fetchCode(problem.getId().toString(),"_id");
			
			if(CollectionUtils.isNotEmpty(codeLines)){
				ProblemCode problemCode = new ProblemCode();
				problemCode.setProblemId(problem.getId().toString());
				problemCode.setProblemDesc(problem.getMetadata().getDescription());
				problemCode.setProblemTitle(problem.getMetadata().getTitle());
				problemCode.setCodeLines(codeLines);
				problemCode.setClassName(problem.getFilename());
				codeWrapper.setProblem(problemCode);				
			}			
		}
		
		List<Problem> answers = mongoService.fetchAnswersforQuestion(problemId);
		if(CollectionUtils.isNotEmpty(answers)){
			for (Problem answer : answers) {
				List<String> codeLines = mongoService.fetchCode(answer.getId().toString(),"_id");
				ProblemCode answersCode = new ProblemCode();
				answersCode.setProblemId(answer.getId().toString());
				answersCode.setProblemDesc(answer.getMetadata().getDescription());
				answersCode.setProblemTitle(answer.getMetadata().getTitle());
				answersCode.setAnsweredBy(answer.getMetadata().getAnsweredBy());
				answersCode.setCodeLines(codeLines);
				answersCode.setClassName(answer.getFilename());
				answersCode.setFeedback(answer.getMetadata().getFeedback());
				answersCode.setRating(answer.getMetadata().getRating());
				answersResponse.add(answersCode);
			}
						
		}	
		codeWrapper.setAnswers(answersResponse);
		return codeWrapper;
		
	}
	
	@Loggable
	@RequestMapping(value="/rateAnswer", method = RequestMethod.POST)
    public void rateAnswer(@RequestBody UpdateRequest update){   
        mongoService.updateAnswer(update);	        
    }
	
}