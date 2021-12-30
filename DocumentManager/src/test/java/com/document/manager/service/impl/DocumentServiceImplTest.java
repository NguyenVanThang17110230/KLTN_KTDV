package com.document.manager.service.impl;


import com.document.manager.domain.DocumentApp;
import com.document.manager.domain.Sentences;
import com.document.manager.dto.PlagiarismDocumentDTO;
import com.document.manager.dto.PlagiarismSentencesDTO;
import com.document.manager.repository.DocumentRepo;
import com.document.manager.service.SentencesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceImplTest {

    @InjectMocks
    DocumentServiceImpl documentService;

    @Mock
    DocumentRepo documentRepo;

    @Mock
    SentencesService sentencesService;

    @Test
    public void testSentenceDivision() throws Exception {
        String s = "Hello everyone! This is my message to test function. That right? I want to know.";
        Assert.assertNotNull(documentService.divisionToSentences(s));
        Assert.assertEquals(4, documentService.divisionToSentences(s).length);
    }

    @Test
    public void testGetTokenizerFromSentences() {
        Sentences sentences = new Sentences();
        sentences.setTokenizer("Test|function|tokenizer");
        List<String> actual = documentService.getTokenizerFromSentences(sentences);
        Assert.assertNotNull(actual);
        Assert.assertEquals(3, actual.size());
    }

    @Test
    public void testGetTokenizer() throws IOException {
        String s1 = "Hello everyone";
        String s2 = "This is my message to test function";
        String s3 = "That right";
        String s4 = "I want to know";
        Map<Integer, List<String>> actual = documentService.getTokenizer(new String[]{s1, s2, s3, s4});
        Assert.assertNotNull(actual);
        Assert.assertEquals(4, actual.size());
    }

    @Test
    public void testGetAllSentences() {
        List<Sentences> sentencesList = new ArrayList<>();
        Sentences sentences1 = new Sentences();
        DocumentApp documentApp1 = new DocumentApp();
        documentApp1.setId(1L);
        sentences1.setId(1L);
        sentences1.setDocumentApp(documentApp1);

        Sentences sentences2 = new Sentences();
        DocumentApp documentApp2 = new DocumentApp();
        documentApp2.setId(2L);
        sentences2.setId(2L);
        sentences2.setDocumentApp(documentApp2);

        Sentences sentences3 = new Sentences();
        DocumentApp documentApp3 = new DocumentApp();
        documentApp3.setId(1L);
        sentences3.setId(3L);
        sentences3.setDocumentApp(documentApp3);

        sentencesList.add(sentences1);
        sentencesList.add(sentences2);
        sentencesList.add(sentences3);

        Mockito.when(sentencesService.findAll()).thenReturn(sentencesList);

        Map<Long, List<Sentences>> actual = documentService.getAllSentences();
        Assert.assertNotNull(actual);
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(2, actual.get(1L).size());
    }

    @Test
    public void testGetPartPlagiarism() {
        List<String> targets = new ArrayList<>();
        targets.add("This");
        targets.add("is");
        targets.add("the");
        targets.add("message");
        targets.add("to");
        targets.add("test");
        targets.add("this");
        targets.add("function");

        List<String> matching = new ArrayList<>();
        matching.add("There");
        matching.add("are");
        matching.add("the");
        matching.add("computer");
        matching.add("to");
        matching.add("create");
        matching.add("this");
        matching.add("function");

        List<String> actual = documentService.getPartPlagiarism(targets, matching);
        Assert.assertNotNull(actual);
        Assert.assertEquals(4, actual.size());
    }

    @Test
    public void testGetReportPlagiarism() throws IOException {
        String s1 = "Hello everyone";
        String s2 = "This is my message to test function";
        String s3 = "That right";
        String s4 = "I want to know";
        String[] targets = new String[]{s1, s2, s3, s4};

        List<DocumentApp> documentApps = new ArrayList<>();
        DocumentApp documentApp1 = new DocumentApp();
        documentApp1.setId(1L);

        DocumentApp documentApp2 = new DocumentApp();
        documentApp2.setId(2L);
        documentApps.add(documentApp1);
        documentApps.add(documentApp2);

        String d1 = "Hello everyone";
        String d2 = "I'm from Viet Nam";
        String d3 = "I send to message to test my function";
        String d4 = "And I want to know is working well";
        String d5 = "That right";

        Map<Integer, List<String>> tokenizer1 = documentService.getTokenizer(new String[]{d1, d2, d3, d4, d5});

        Sentences sentences1 = new Sentences();
        sentences1.setId(1L);
        sentences1.setDocumentApp(documentApp1);
        sentences1.setRawText(d1);
        sentences1.setTokenizer(tokenizer1.get(0).stream().collect(Collectors.joining("|")));

        Sentences sentences2 = new Sentences();
        sentences2.setId(2L);
        sentences2.setDocumentApp(documentApp1);
        sentences2.setRawText(d2);
        sentences2.setTokenizer(tokenizer1.get(1).stream().collect(Collectors.joining("|")));

        Sentences sentences3 = new Sentences();
        sentences3.setId(3L);
        sentences3.setDocumentApp(documentApp1);
        sentences3.setRawText(d3);
        sentences3.setTokenizer(tokenizer1.get(2).stream().collect(Collectors.joining("|")));

        Sentences sentences4 = new Sentences();
        sentences4.setId(4L);
        sentences4.setDocumentApp(documentApp1);
        sentences4.setRawText(d4);
        sentences4.setTokenizer(tokenizer1.get(3).stream().collect(Collectors.joining("|")));

        Sentences sentences5 = new Sentences();
        sentences5.setId(5L);
        sentences5.setDocumentApp(documentApp1);
        sentences5.setRawText(d5);
        sentences5.setTokenizer(tokenizer1.get(4).stream().collect(Collectors.joining("|")));

        String t1 = "Hello everybody";
        String t2 = "I'm from Viet Nam";
        String t3 = "I send to message to tell with user create this function";
        String t4 = "And I want to know is working well";

        Map<Integer, List<String>> tokenizer2 = documentService.getTokenizer(new String[]{t1, t2, t3, t4});

        Sentences sentences6 = new Sentences();
        sentences6.setId(6L);
        sentences6.setDocumentApp(documentApp2);
        sentences6.setRawText(t1);
        sentences6.setTokenizer(tokenizer2.get(0).stream().collect(Collectors.joining("|")));

        Sentences sentences7 = new Sentences();
        sentences7.setId(7L);
        sentences7.setDocumentApp(documentApp2);
        sentences7.setRawText(t2);
        sentences7.setTokenizer(tokenizer2.get(1).stream().collect(Collectors.joining("|")));

        Sentences sentences8 = new Sentences();
        sentences8.setId(6L);
        sentences8.setDocumentApp(documentApp2);
        sentences8.setRawText(t3);
        sentences8.setTokenizer(tokenizer2.get(2).stream().collect(Collectors.joining("|")));

        Sentences sentences9 = new Sentences();
        sentences9.setId(6L);
        sentences9.setDocumentApp(documentApp2);
        sentences9.setRawText(t4);
        sentences9.setTokenizer(tokenizer2.get(3).stream().collect(Collectors.joining("|")));

        // Focus
        String r1 = "Hello everyone";
        String r2 = "This is my message to test function";
        String r3 = "That right";
        String r4 = "I am want to know";

        DocumentApp documentApp3 = new DocumentApp();
        documentApp3.setId(3L);
        documentApps.add(documentApp3);

        Map<Integer, List<String>> tokenizer3 = documentService.getTokenizer(new String[]{r1, r2, r3, r4});

        Sentences sentences10 = new Sentences();
        sentences10.setId(10L);
        sentences10.setDocumentApp(documentApp3);
        sentences10.setRawText(r1);
        sentences10.setTokenizer(tokenizer3.get(0).stream().collect(Collectors.joining("|")));

        Sentences sentences11 = new Sentences();
        sentences11.setId(11L);
        sentences11.setDocumentApp(documentApp3);
        sentences11.setRawText(r2);
        sentences11.setTokenizer(tokenizer3.get(1).stream().collect(Collectors.joining("|")));

        Sentences sentences12 = new Sentences();
        sentences12.setId(12L);
        sentences12.setDocumentApp(documentApp3);
        sentences12.setRawText(r3);
        sentences12.setTokenizer(tokenizer3.get(2).stream().collect(Collectors.joining("|")));

        Sentences sentences13 = new Sentences();
        sentences13.setId(13L);
        sentences13.setDocumentApp(documentApp3);
        sentences13.setRawText(r4);
        sentences13.setTokenizer(tokenizer3.get(3).stream().collect(Collectors.joining("|")));
        //

        List<Sentences> sentencesList = new ArrayList<>();
        sentencesList.add(sentences1);
        sentencesList.add(sentences2);
        sentencesList.add(sentences3);
        sentencesList.add(sentences4);
        sentencesList.add(sentences5);
        sentencesList.add(sentences6);
        sentencesList.add(sentences7);
        sentencesList.add(sentences8);
        sentencesList.add(sentences9);
        sentencesList.add(sentences10);
        sentencesList.add(sentences11);
        sentencesList.add(sentences12);
        sentencesList.add(sentences13);

        Mockito.when(documentRepo.findAll()).thenReturn(documentApps);
        Mockito.when(sentencesService.findAll()).thenReturn(sentencesList);

        Map<Integer, List<String>> tokenizerOfTarget = documentService.getTokenizer(targets);

        PlagiarismDocumentDTO actual = documentService.getPlagiarism(targets, tokenizerOfTarget);

        Assert.assertNotNull(actual);
        Assert.assertEquals(95.5, actual.getRate(), 2);
        Assert.assertEquals(3, Integer.parseInt(actual.getDocumentId().toString()));
    }
}