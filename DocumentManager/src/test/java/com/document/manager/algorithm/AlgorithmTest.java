package com.document.manager.algorithm;


import com.document.manager.pipeline.Annotation;
import com.document.manager.pipeline.VnCoreNLP;
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
}