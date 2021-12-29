package com.document.manager.corenlp.postagger;

import marmot.morph.MorphTagger;
import marmot.morph.Sentence;
import marmot.morph.Word;

import marmot.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PosTagger {
    private static PosTagger posTagger = null;
    private MorphTagger tagger;

    public PosTagger() throws IOException {
        String modelPath = System.getProperty("user.dir") + "/models/postagger/vi-tagger";
        if (!new File(modelPath).exists()) throw new IOException("PosTagger: " + modelPath + " is not found!");
        tagger = FileUtils.loadFromFile(modelPath);

    }

    public static PosTagger initialize() throws IOException {
        if (posTagger == null) {
            posTagger = new PosTagger();
        }
        return posTagger;
    }

    public List<com.document.manager.pipeline.Word> tagSentence(String sentence) throws IOException {
        List<com.document.manager.pipeline.Word> output = new ArrayList<>();
        String line = sentence.trim();
        if (line.length() == 0) {
            return output;
        }
        String[] tokenstrs = line.split(" ");
        LinkedList tokens = new LinkedList();

        for (int i = 0; i < tokenstrs.length; ++i) {
            if (!tokenstrs[i].isEmpty()) {
                Word word = new Word(tokenstrs[i]);
                tokens.add(word);
            }
        }

        Sentence marmotSentence = new Sentence(tokens);
        Object lemma_tags = tagger.tagWithLemma(marmotSentence);
        for (int i = 0; i < marmotSentence.size(); ++i) {
            List<String> token_lemma_tags = (List) ((List) lemma_tags).get(i);
            com.document.manager.pipeline.Word word = new com.document.manager.pipeline.Word((i + 1), marmotSentence.getWord(i).getWordForm(), (String) token_lemma_tags.get(1));
            output.add(word);

        }
        return output;
    }
}

