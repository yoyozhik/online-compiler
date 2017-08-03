package com.arun.tests;

import com.arun.request.FileSaveRequest;
import com.arun.service.MongoService;
import com.arun.service.impl.MongoServiceImpl;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;

import static org.mockito.Matchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class MongoServiceImplTest {

    @InjectMocks
    MongoService mongoService = new MongoServiceImpl();

    @Mock
    GridFsOperations gridOperations = Mockito.mock(GridFsOperations.class);

    @Mock
    Mapper mapper = new DozerBeanMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveCode() {
        GridFSFile gridFSFile = Mockito.mock(GridFSFile.class);
        File file = new File("Test.txt");
        FileSaveRequest fileSaveRequest = new FileSaveRequest();
        fileSaveRequest.setQuestionId("123");
        fileSaveRequest.setCode(file);
        Mockito.when(gridOperations.store(any(InputStream.class), any(String.class), any(String.class), any(DBObject.class))).thenReturn(gridFSFile);
        mongoService.saveCodeFile(fileSaveRequest);
    }
}
