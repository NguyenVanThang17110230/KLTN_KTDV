package com.document.manager.algorithm;


import com.document.manager.pipeline.Annotation;
import com.document.manager.pipeline.VnCoreNLP;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

public class AlgorithmTest {

    @Test
    public void TwoSentences() throws IOException {
        String s1 = "Ông Nguyễn Khắc Chúc  đang làm việc tại Đại học Quốc gia Hà Nội";
        String s2 = "Bà Lan, vợ ông Chúc, cũng làm việc tại Đại học Quốc gia Hà Nội";

        String[] annotators = {"wseg", "pos", "ner", "parse"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        Annotation annotation = new Annotation(s1);
        pipeline.annotate(annotation);

        System.out.println(annotation.toString());


        int distance = Algorithm.getLevenshteinDistance(s1, s2);

        System.out.println(Algorithm.getPercentageSimilarity(distance, s1.length(), s2.length()));
    }

    @Test
    public void twoSentenceEnglish() throws IOException {
        String s1 = "Part III Models of Synaptic Plasticity 359 Chapter 10 Hebbian Models In the neuron models discussed so far each synapse is characterized by " +
                "a sin- gle constant parameter wij that determines the amplitude of the postsynaptic response to an incoming action potential.";

        String s2 = "Spike timing-dependent plasticity (STDP) In the neuron models discussed so far each synapse is characterized by a sin gle fixed real parameter w " +
                "that determines the amplitude of the postsynaptic response to an incoming spike. However, a great deal";

        String[] annotators = {"wseg", "pos", "ner", "parse"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        Annotation annotation1 = new Annotation(s1);
        pipeline.annotate(annotation1);

        HashMap<Integer, String> map1 = new HashMap<>();
        HashMap<Integer, String> map2 = new HashMap<>();

        annotation1.getSentences().get(0).getWords().stream().forEach(w -> {
            map1.put(w.getIndex(), w.getForm());
        });

        Annotation annotation2 = new Annotation(s2);
        pipeline.annotate(annotation2);

        annotation1.getSentences().get(0).getWords().stream().forEach(w -> {
            map2.put(w.getIndex(), w.getForm());
        });

        System.out.println("============ Sentences 1 ============");
        for (Integer key : map1.keySet()) {
            System.out.println(key + " - " + map1.get(key));
        }

        System.out.println("============ Sentences 2 ============");
        for (Integer key : map2.keySet()) {
            System.out.println(key + " - " + map1.get(key));
        }
    }

    /* Document 1
        String s1 = "Hello everyone";                       100
        String s2 = "This is my message to test function";  70
        String s3 = "That right";                           100
        String s4 = "I want to know";                       41
    */

    /* Document 2
        String d1 = "Hello everyone";
        String d2 = "I'm from Viet Nam";
        String d3 = "I send to message to test my function";
        String d4 = "And I want to know is working well";
        String d5 = "That right";
    */

    @Test
    public void testSentencesZero() {
        String s1 = "I want to know";
        String s2 = "I am want to know";
        float actual =  Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        Assert.assertEquals(82.0, actual, 2);
    }

    @Test
    public void testSentencesOne() {
        String s1 = "Hello everyone";
        String s2 = "Hello everyone";
        float actual =  Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        Assert.assertEquals(100.0, actual, 2);
    }

    @Test
    public void testSentencesTwo() {
        String s1 = "This is my message to test function";
        String s2 = "I send to message to test my function";
        float actual =  Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        Assert.assertEquals(70.0, actual, 2);
    }
    @Test
    public void testSentencesThree() {
        String s1 = "I want to know";
        String s2 = "And I want to know is working well";
        float actual =  Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        Assert.assertEquals(41.0, actual, 2);
    }

}