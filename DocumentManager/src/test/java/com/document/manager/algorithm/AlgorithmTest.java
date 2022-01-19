package com.document.manager.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AlgorithmTest {

    @Test
    public void test() {
        String s1 = "Suppose that X represent a algorithm for the rows of the dynamic programming array and Y";
        String s2 = "Suppose that X represent the rows of the dynamic programming array and Y represent the columns";
        float percent = Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        //percent = (percent * s2.length())/ (s1.length());
        Assert.assertEquals(100.0, percent, 2);
    }

    @Test
    public void testExample() {
        String s1 = "Tôi học ngành công nghệ thông tin";
        String s2 = "Tôi học kế ngành công nghệ thông tin";
        float percent = Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        //percent = (percent * s2.length())/ (s1.length());
        Assert.assertEquals(100.0, percent, 2);
    }

    @Test
    public void testTwoTheSameSentence() {
        String s1 = "Tôi học ngành công nghệ thông tin";
        String s2 = "Tôi học ngành công nghệ thông tin";
        float percent = Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(s1, s2), s1.length(), s2.length());
        //percent = (percent * s2.length())/ (s1.length());
        Assert.assertEquals(100.0, percent, 2);
    }
}