package com.arun.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.arun.logging.Loggable;
import com.arun.domain.Problem;
import com.arun.request.FileSaveRequest;
import com.arun.request.UpdateRequest;
import com.arun.response.ProblemResponse;
import com.arun.service.MongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class MongoServiceImpl implements MongoService {

    @Autowired
    private GridFsOperations gridOperations;

    @Autowired
    private Mapper dozerMapper;

    /**
     * Save the source file and metadata in Mongo using GridFsOperations
     *
     * @param fileSaveRequest
     */
    @Loggable
    @Override
    public void saveCodeFile(FileSaveRequest fileSaveRequest) {
        DBObject metaData = new BasicDBObject();
        metaData.put("questionId", fileSaveRequest.getQuestionId());
        metaData.put("description", fileSaveRequest.getProblemDescription());
        metaData.put("language", fileSaveRequest.getLanguage());
        metaData.put("title", fileSaveRequest.getProblemTitle());
        metaData.put("adminId", fileSaveRequest.getAdminId());
        metaData.put("adminUserName", fileSaveRequest.getAdminUserName());
        metaData.put("participants", fileSaveRequest.getParticipants());
        metaData.put("answeredBy", fileSaveRequest.getAnsweredBy());


        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileSaveRequest.getCode());
            gridOperations.store(inputStream, fileSaveRequest.getCode().getName(), "application/octet-stream", metaData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Query the source file snippet associated with admin user or candidate.
     * GridFSDBFile file is converted to List of Strings
     *
     * @param id
     * @param fieldName
     * @return
     */
    @Loggable
    @Override
    public List<String> fetchCode(String id, String fieldName) {
        List<GridFSDBFile> result = null;
        ObjectId objectId = new ObjectId(id);
        List<String> codeLines = new ArrayList<String>();
        if (fieldName.equals("_id")) {
            result = gridOperations.find(new Query().addCriteria(Criteria.where(fieldName).is(objectId)));
        } else {
            result = gridOperations.find(new Query().addCriteria(Criteria.where(fieldName).is(id)));
        }

        for (GridFSDBFile file : result) {
            try {
                InputStream content = file.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(content));
                String line = null;
                while ((line = br.readLine()) != null) {
                    codeLines.add(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return codeLines;

    }

    /**
     * Fetch problem code snippets by id
     *
     * @param id
     * @return
     */
    @Loggable
    @Override
    public Problem fetchFilesById(String id) {
        ObjectId objectId = new ObjectId(id);
        GridFSDBFile gridFile =	gridOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(objectId)));
        if(null != gridFile){
            Problem problem	 =	dozerMapper.map(gridFile, Problem.class);
            return problem;
        }
        return null;
    }

    /**
     * Save feed back and rating on a submitted answer
     *
     * @param updateRequest
     */
    @Loggable
    @Override
    public void updateAnswer(UpdateRequest updateRequest) {
        ObjectId objectId = new ObjectId(updateRequest.getQuestionId());
        GridFSDBFile gridFile =	gridOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(objectId)));
        if(null != gridFile) {
            gridFile.getMetaData().put("feedback", updateRequest.getFeedback());
            gridFile.getMetaData().put("rating", updateRequest.getRating());
            gridFile.save();
        }
    }

    /**
     * Fetch problem snippets for users
     *
     * @param language
     * @param userId
     * @param isAdmin
     * @param userName
     * @return
     */
    @Loggable
    @Override
    public List<ProblemResponse> fetchProblemsByLanguage(String language, Integer userId, boolean isAdmin, String userName) {
        List<GridFSDBFile> results = null;
        if(isAdmin){
            if(userId > 0 ){
                results	 = gridOperations.find(new Query().addCriteria(Criteria.where("metadata.language").is(language)).addCriteria(Criteria.where("metadata.adminId").is(userId)).addCriteria(Criteria.where("metadata.questionId").is("")));
            }else{
                results	 = gridOperations.find(new Query().addCriteria(Criteria.where("metadata.language").is(language)).addCriteria(Criteria.where("metadata.questionId").is("")));
            }
        } else {
            if(StringUtils.isNotBlank(userName) ){
                results	 = gridOperations.find(new Query().addCriteria(Criteria.where("metadata.language").is(language)).addCriteria(Criteria.where("metadata.participants").in(userName)).addCriteria(Criteria.where("metadata.questionId").is("")));
            }
        }
        List<Problem> files = new ArrayList<Problem>();

        if(CollectionUtils.isNotEmpty(results)){
            for(GridFSDBFile result : results){
                Problem problem	 =	dozerMapper.map(result, Problem.class);
                files.add(problem);
            }

        }
        if(CollectionUtils.isNotEmpty(files)){
            List<ProblemResponse>  problemResponses = new ArrayList<ProblemResponse>();
            for(Problem problem : files){
                ProblemResponse  problemResponse = dozerMapper.map(problem, ProblemResponse.class);
                problemResponse.setProblemId(problem.getId().toString());
                problemResponses.add(problemResponse);
            }
            return problemResponses;

        }else{
            return new ArrayList<ProblemResponse>();
        }
    }

    /**
     * Fetch all submitted answers for a question id
     *
     * @param id
     * @return
     */
    @Loggable
    @Override
    public List<Problem> fetchAnswersforQuestion(String id) {
        List<Problem> answers = new ArrayList<Problem>();
        List<GridFSDBFile> gridFile = gridOperations.find(new Query().addCriteria(Criteria.where("metadata.questionId").is(id)));
        if(CollectionUtils.isNotEmpty(gridFile)){
            for(GridFSDBFile result : gridFile){
                Problem problem	 =	dozerMapper.map(result, Problem.class);
                answers.add(problem);
            }

        }
        return answers;
    }


}
