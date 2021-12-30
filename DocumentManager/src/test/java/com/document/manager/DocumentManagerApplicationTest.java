package com.document.manager;


import com.document.manager.pipeline.Annotation;
import com.document.manager.pipeline.VnCoreNLP;

import java.io.IOException;

public class DocumentManagerApplicationTest {

    public static void main(String[] args) throws IOException {
        String[] annotators = {"wseg", "pos", "ner", "parse"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        String str = "Ông Nguyễn Khắc Chúc  đang làm việc tại Đại học Quốc gia Hà Nội. "
                + "Bà Lan, vợ ông Chúc, cũng làm việc tại đây.";
        Annotation annotation = new Annotation(str);
        pipeline.annotate(annotation);

        System.out.println(annotation.toString());
    }
}