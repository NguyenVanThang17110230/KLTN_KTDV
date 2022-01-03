package com.document.manager.service.impl;

import com.document.manager.algorithm.Algorithm;
import com.document.manager.domain.DocumentApp;
import com.document.manager.domain.Sentences;
import com.document.manager.domain.UserApp;
import com.document.manager.dto.*;
import com.document.manager.dto.constants.Constants;
import com.document.manager.dto.enums.PlagiarismStatus;
import com.document.manager.dto.mapper.DTOMapper;
import com.document.manager.pipeline.Annotation;
import com.document.manager.pipeline.VnCoreNLP;
import com.document.manager.pipeline.Word;
import com.document.manager.repository.DocumentRepo;
import com.document.manager.service.DocumentService;
import com.document.manager.service.FileService;
import com.document.manager.service.SentencesService;
import com.document.manager.service.UserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private SentencesService sentencesService;

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    private static final String REGEX_DIVISION = "[?!.;]";
    private static final Integer LIMIT_CHARACTER = 30;
    private static final String DIVISION = "\\*";

    @Override
    public List<DocumentDTO> getDocumentOfCurrentUser() throws NotFoundException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Can't get info of user current!");
            throw new IllegalArgumentException("Can't get info of user current!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserApp userApp = userService.findByEmail(email);
        if (userApp == null) {
            log.error("User with email {} not found", email);
            throw new NotFoundException("Current user not found");
        }
        List<DocumentApp> documentApps = this.findByUserId(userApp.getId());
        return dtoMapper.toDocumentDTO(documentApps);
    }

    @Override
    public DocumentApp save(DocumentApp documentApp) {
        if (documentApp == null) {
            throw new IllegalArgumentException("Document app invalid");
        }
        return documentRepo.save(documentApp);
    }

    @Override
    public long count() {
        return documentRepo.count();
    }

    @Transactional
    @Override
    public PlagiarismDocumentDTO uploadDocument(UploadDocumentDTO uploadDTO) throws IOException {
        if (uploadDTO == null) {
            throw new FileNotFoundException("Data upload document invalid!");
        }
        MultipartFile file = uploadDTO.getMultipartFile();
        if (file == null) {
            throw new FileNotFoundException("File document not found!");
        }
        try {
            File documentFile = new File(file.getOriginalFilename());
            FileUtils.writeByteArrayToFile(documentFile, file.getBytes());

            // TODO: Read content of file
            String[] targets = this.divisionToSentences(fileService.readContentDocument(documentFile));

            // TODO: Get tokenizer of targets
            Map<Integer, List<String>> tokenizerOfTarget = getTokenizer(targets);

            // TODO: Check plagiarism
            PlagiarismDocumentDTO plagiarismDocumentDTO = this.getPlagiarism(targets, tokenizerOfTarget);

            if (existPlagiarism(plagiarismDocumentDTO)) {
                plagiarismDocumentDTO.setStatus(false);
                if (plagiarismDocumentDTO.getRate() == 100) {
                    plagiarismDocumentDTO.setMessage(PlagiarismStatus.SAME.name());
                    plagiarismDocumentDTO.setPlagiarism(null);
                } else {
                    plagiarismDocumentDTO.setMessage(PlagiarismStatus.SIMILAR.name());
                }
                return plagiarismDocumentDTO;
            } else {
                plagiarismDocumentDTO.setMessage(PlagiarismStatus.DIFFERENT.name());
                // TODO: Build data and store into DB
                List<Sentences> sentences = new ArrayList<>();
                for (int i = 0; i < targets.length; i = i + 3) {
                    //String rawText = targets[i];
                    Sentences sentence = Sentences.builder()
                            .rawText(getRawTextOfPart(i, targets))
                            .tokenizer(getTokenizerOfPart(i, tokenizerOfTarget))                    // tokenizerOfTarget.get(i).stream().collect(Collectors.joining("|"))
                            .build();
                    sentences.add(sentence);
                }
                // TODO: Save file
                String link = fileService.saveFile(Constants.DIR_UPLOADED_REPORT, file.getOriginalFilename(), file.getBytes());

                DocumentApp documentApp = DocumentApp.builder()
                        .title(uploadDTO.getTitle())
                        .fileName(file.getOriginalFilename())
                        .link(link)
                        .note(uploadDTO.getNote())
                        .createdStamp(new Date())
                        .userApp(userService.getCurrentUser())
                        .sentences(sentences)
                        .build();
                this.save(documentApp);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return null;
    }

    private String getRawTextOfPart(int index, String[] array) {
        StringBuilder s = new StringBuilder();
        for (int i = index; i < index + 3; i++) {
            if (i < array.length) {
                if (StringUtils.isNotBlank(s.toString())) {
                    s.append("*");
                }
                s.append(array[i]);
            }
        }
        return s.toString();
    }

    private String getTokenizerOfPart(int index, Map<Integer, List<String>> tokenizers) {
        StringBuilder s = new StringBuilder();
        for (int i = index; i < index + 3; i++) {
            if (tokenizers.containsKey(i)) {
                if (StringUtils.isNotBlank(s.toString())) {
                    s.append("*");
                }
                s.append(tokenizers.get(i).stream().collect(Collectors.joining("|")));
            }
        }
        return s.toString();
    }

    private String[] getContentToArray(File file) {
        List<String> contents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()), StandardCharsets.UTF_8))) {
            for (String line = null; (line = br.readLine()) != null; ) {
                contents.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arrays = new String[contents.size()];
        return contents.toArray(arrays);
    }

    @Override
    public String[] divisionToSentences(String content) throws Exception {
        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("Content is empty");
        }
        try {
            content = content.replaceAll("\r\n", " ");
            content = content.replaceAll("\\s+", " ");
            List<String> sentences = new LinkedList<>(Arrays.asList(content.split(REGEX_DIVISION)));
            List<Integer> deletes = new ArrayList<>();
            for (int i = 0; i < sentences.size(); i++) {
                if (sentences.get(i).trim().length() < LIMIT_CHARACTER) {
                    String s = sentences.get(i).trim();
                    int index = i;
                    while (s.length() < LIMIT_CHARACTER && i + 1 < sentences.size()) {
                        i++;
                        deletes.add(i);
                        s += sentences.get(i).trim();
                    }
                    sentences.set(index, s);
                }
            }
            List<String> elementDeletes = new ArrayList<>();
            deletes.forEach(d -> elementDeletes.add(sentences.get(d)));
            elementDeletes.forEach(e -> sentences.remove(e));
            sentences.stream().filter(s -> !StringUtils.isBlank(s)).collect(Collectors.toList());
            return sentences.stream().filter(s -> !StringUtils.isBlank(s)).map(String::trim).toArray(size -> new String[size]);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    private void handleSentences(String[] sentences) {
//        if (sentences == null || sentences.length <= 0 ) {
//            return;
//        }
//        for (int i = 0 ; i < sentences.length; i ++ ) {
//            if (sentences[i].trim().length() < 15 && sentences[i+1].trim().length() < 15) {
//                sentences[i] = sentences[i] + " " + sentences[i+1];
//
//            }
//        }
//    }

    private String[] division(String content) {
        content = content.replaceAll("\r\n", " ");
        List<String> sentences = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(content);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            while (end - start < 30) {
                end = iterator.next();
            }
            sentences.add(content.substring(start, end).trim());
        }
        return sentences.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList()).toArray(new String[sentences.size()]);
    }

    @Override
    public List<DocumentApp> findByUserId(Long userId) {
        return documentRepo.findByUserId(userId);
    }

    @Override
    public List<DocumentApp> findAll() {
        return documentRepo.findAll();
    }

    @Override
    public PlagiarismDocumentDTO getPlagiarism(String[] targets, Map<Integer, List<String>> tokenizerOfTarget) {
        if (targets == null || targets.length <= 0) {
            throw new IllegalArgumentException("Array targets want to check plagiarism invalid");
        }
        PlagiarismDocumentDTO plagiarismDocumentDTO = new PlagiarismDocumentDTO();

        // TODO: Get sentences of all documents in database
        List<DocumentApp> documentApps = this.findAll();
        Map<Long, List<Sentences>> allSentences = this.getAllSentences();

        // TODO: If in database haven't document then return empty
        if (allSentences == null || allSentences.isEmpty()) {
            return plagiarismDocumentDTO;
        }

        // TODO: Loop all document to check plagiarism with target
        documentApps.forEach(d -> updatePlagiarism(targets, d.getId(), allSentences, tokenizerOfTarget, plagiarismDocumentDTO));

        return plagiarismDocumentDTO;
    }

    @Override
    public void delete(Long documentId) {
        try {
            UserApp userApp = userService.getCurrentUser();
            if (userApp == null) {
                throw new IllegalArgumentException("Can't get information of user current");
            }
            Optional<DocumentApp> documentOptional = documentRepo.findByDocumentIdAndUserId(documentId, userApp.getId());
            if (!documentOptional.isPresent()) {
                throw new NotFoundException("Document of user not found");
            }
            documentRepo.delete(documentOptional.get());
            log.info("Delete document with id {} successful", documentId);
        } catch (Exception e) {
            log.error("Delete document with id {} failed because: {}", documentId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Long documentId, UpdateDocumentDTO updateDocumentDTO) {
        try {
            UserApp userApp = userService.getCurrentUser();
            if (userApp == null) {
                throw new IllegalArgumentException("Can't get information of user current");
            }
            Optional<DocumentApp> documentOptional = documentRepo.findByDocumentIdAndUserId(documentId, userApp.getId());
            if (!documentOptional.isPresent()) {
                throw new NotFoundException("Document of user not found");
            }
            DocumentApp documentApp = documentOptional.get();
            documentApp.setTitle(updateDocumentDTO.getTitle());
            documentApp.setNote(updateDocumentDTO.getNote());
            documentApp.setModifiedStamp(new Date());
            documentRepo.save(documentOptional.get());
            log.info("Update document with id {} successful", documentId);
        } catch (Exception e) {
            log.error("Update document with id {} failed because: {}", documentId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<Long, List<Sentences>> getAllSentences() {
        List<Sentences> sentences = sentencesService.findAll();
        Map<Long, List<Sentences>> map = new HashMap<>();
        for (Sentences sentence : sentences) {
            if (!map.containsKey(sentence.getDocumentApp().getId())) {
                map.put(sentence.getDocumentApp().getId(), Collections.singletonList(sentence));
            } else {
                List<Sentences> temps = new LinkedList<>(map.get(sentence.getDocumentApp().getId()));
                temps.add(sentence);
                map.put(sentence.getDocumentApp().getId(), temps);
            }
        }
        return map;
    }

    public List<String> getTokenizerFromSentences(Sentences sentences) {
        if (sentences == null || StringUtils.isEmpty(sentences.getTokenizer())) {
            return new ArrayList<>();
        }
        return Arrays.asList(sentences.getTokenizer().split("[|]"));
    }

    public Map<Integer, List<String>> getTokenizer(String[] targets) throws IOException {
        Map<Integer, List<String>> map = new HashMap<>();
        String[] annotators = {"wseg", "pos", "ner", "parse"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);
        for (int i = 0; i < targets.length; i++) {
            Annotation annotation = new Annotation(targets[i]);
            try {
                pipeline.annotate(annotation);
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Word> words = annotation.getWords();
            List<String> tokenizerOfTarget = new ArrayList<>();
            words.stream().forEach(w -> tokenizerOfTarget.add(w.getForm()));
            map.put(i, tokenizerOfTarget);
        }
        return map;
    }

    public List<String> getPartPlagiarism(List<String> tokenizersOfTarget, List<String> tokenizerOfMatching) {
        List<String> tokenizerOfMatchingLower = tokenizerOfMatching.stream().map(t -> t.toLowerCase()).collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        tokenizersOfTarget.stream().forEach(t -> {
            if (tokenizerOfMatchingLower.contains(t.toLowerCase())) {
                result.add(t);
            }
        });
        return result.stream().collect(Collectors.toList());
    }

    private List<IndexDTO> getPlagiarism(String target, String matching, List<String> tokenizers) {
        String targetLower = target.toLowerCase();
        String matchingLower = matching.toLowerCase();
        Map<String, Integer> saveIndexOfTarget = new HashMap<>();
        Map<String, Integer> saveIndexOfMatching = new HashMap<>();
        List<IndexDTO> indexList = new ArrayList<>();
        for (String s : tokenizers) {
            int flagTarget = 1;
            int flagMatching = 1;
            String sLower = s.toLowerCase();
            if (sLower.length() > 3) {
                if (targetLower.contains(sLower) && matchingLower.contains(sLower)) {
                    int startTarget = targetLower.indexOf(" " + sLower + " ");
                    if (startTarget == -1) {
                        flagTarget = 0;
                        startTarget = targetLower.indexOf(s.toLowerCase());
                    }
                    if (saveIndexOfTarget.containsKey(sLower) && saveIndexOfTarget.get(sLower) < targetLower.length()) {
                        startTarget = targetLower.indexOf(sLower, saveIndexOfTarget.get(sLower));
                    }
                    saveIndexOfTarget.put(sLower, startTarget + sLower.length() - 1);
                    int startMatching = matchingLower.indexOf(" " + sLower + " ");
                    if (startMatching == -1) {
                        flagMatching = 0;
                        startMatching = matchingLower.indexOf(s.toLowerCase());
                    }
                    if (saveIndexOfMatching.containsKey(sLower) && saveIndexOfMatching.get(sLower) < matchingLower.length()) {
                        startMatching = matchingLower.indexOf(sLower, saveIndexOfMatching.get(sLower));
                    }
                    saveIndexOfMatching.put(sLower, startMatching + sLower.length() - 1);
                    if (startTarget != -1 && startMatching != -1) {
                        indexList.add(IndexDTO.builder().startTarget(startTarget + flagTarget)
                                .startMatching(startMatching + flagMatching)
                                .length(sLower.length())
                                .build());
                    }
                }
            }
        }
        return indexList;
    }

    private List<IndexDTO> getPlagiarismTest(String target, String matching, List<String> tokenizerOfTarget, List<String> tokenizerOfMatching) {
        Map<String, Integer> saveIndexOfTarget = new HashMap<>();
        Map<String, Integer> saveIndexOfMatching = new HashMap<>();
        List<IndexDTO> indexList = new ArrayList<>();

        List<String> tLower = toLowercase(tokenizerOfTarget);
        List<String> mLower = toLowercase(tokenizerOfMatching);

        String targetLower = target.toLowerCase();
        String matchingLower = matching.toLowerCase();

        for (String s : tokenizerOfMatching) {
            int flagTarget = 1;
            int flagMatching = 1;
            String sLower = s.toLowerCase();
            if (sLower.length() > 3) {

                int rootMatching = -1;

                int startTarget = targetLower.indexOf(" " + sLower + " ");
                if (startTarget == -1) {
                    flagTarget = 0;
                    startTarget = targetLower.indexOf(s.toLowerCase());
                }
                if (saveIndexOfTarget.containsKey(sLower) && saveIndexOfTarget.get(sLower) < targetLower.length()) {
                    startTarget = targetLower.indexOf(sLower, saveIndexOfTarget.get(sLower));
                }
                saveIndexOfTarget.put(sLower, startTarget + sLower.length() - 1);

                int flagIndex = 0;
                int startMatching = -1;
                int position = -1;
                int max = 0;
                do {
                    startMatching = matchingLower.indexOf(sLower, flagIndex);
                    flagIndex = startMatching + sLower.length();
                    int count = count(target.substring(startTarget), matching.substring(startMatching));
                    if (count > max) {
                        max = count;
                        position = startMatching;
                    }
                } while (startMatching != -1);

                if (position == -1) {
                    flagMatching = 0;
                    position = matchingLower.indexOf(s.toLowerCase());
                }
                if (saveIndexOfMatching.containsKey(sLower) && saveIndexOfMatching.get(sLower) < matchingLower.length()) {
                    position = matchingLower.indexOf(sLower, saveIndexOfMatching.get(sLower));
                }
                saveIndexOfMatching.put(sLower, position + sLower.length() - 1);
                if (startTarget != -1 && startMatching != -1) {
                    indexList.add(IndexDTO.builder().startTarget(startTarget + flagTarget)
                            .startMatching(position + flagMatching)
                            .length(sLower.length())
                            .build());
                }
            }
        }
        return indexList;
    }

    private int count(String target, String matching) {
        int count = 0;
        String[] targets = target.trim().split("\\s+");
        String[] matchings = matching.trim().split("\\s+");
        for (int i = 0; i < targets.length; i++) {
            if (i < targets.length && i < matchings.length
                    && targets[i].toLowerCase().equalsIgnoreCase(matchings[i])) {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    private boolean checkCondition(int sTarget, int sMatching, List<String> target, List<String> matching) {
        for (int i = 0; i < 4; i++) {
            if (sTarget + i < target.size() && sMatching + i < matching.size()) {
                if (!target.get(sTarget + i).equalsIgnoreCase(matching.get(sMatching + i))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private List<String> toLowercase(List<String> strings) {
        List<String> result = new ArrayList<>();
        strings.forEach(s -> result.add(s.toLowerCase()));
        return result;
    }

    private Map<Integer, Sentences> buildMapSentences(List<Sentences> parts) {
        parts.sort(Comparator.comparing(Sentences::getId));
        Map<Integer, Sentences> map = new HashMap<>();
        int index = 0;
        for (Sentences part : parts) {
            String rawText = part.getRawText();
            String tokenizer = part.getTokenizer();
            String[] texts = rawText.split(DIVISION);
            String[] tokenizers = tokenizer.split(DIVISION);
            for (int i = 0; i < texts.length; i++) {
                Sentences sentences = new Sentences();
                sentences.setRawText(texts[i]);
                sentences.setTokenizer(tokenizers[i]);
                sentences.setDocumentApp(part.getDocumentApp());
                map.put(index, sentences);
                index++;
            }
        }
        return map;
    }

    private void updatePlagiarism(String[] targets, Long documentMatchingId, Map<Long, List<Sentences>> allSentences,
                                  Map<Integer, List<String>> tokenizerOfTarget, PlagiarismDocumentDTO plagiarismDocumentDTO) {
        // TODO: Get all sentences of document
        Map<Integer, Sentences> mapSentences = buildMapSentences(allSentences.get(documentMatchingId));

        // TODO: Loop all target to check with sentences of this document
        List<PlagiarismSentencesDTO> plagiarismSentences = new ArrayList<>();
        float totalRateMatchingCondition = 0;
        for (int i = 0; i < targets.length; i++) {
            String target = targets[i];
            // TODO: Get tokenizer of target (Key of tokenizerOfTarget equal i)
            List<String> tokenizers = tokenizerOfTarget.get(i);
            float sentenceHighestRate = 0;

            PlagiarismSentencesDTO plagiarismSentencesDTO = new PlagiarismSentencesDTO();

            // TODO: Loop all sentences of document to check plagiarism
            for (Integer position : mapSentences.keySet()) {
                Sentences sentences = mapSentences.get(position);

                // TODO: Calculate percent plagiarism
                float percent = Algorithm.getPercentageSimilarity(Algorithm.getLevenshteinDistance(target, sentences.getRawText()),
                        target.length(), sentences.getRawText().length());

                // TODO: Check with constant number
                if (percent > 80 && percent > sentenceHighestRate) {
                    sentenceHighestRate = percent;

                    // TODO : Convert to list tokenizer from sentences in database
                    List<String> tokenizerOfMatching = this.getTokenizerFromSentences(sentences);

                    // TODO: Get part plagiarism
                    List<String> partPlagiarism = this.getPartPlagiarism(tokenizers, tokenizerOfMatching);
                    List<IndexDTO> indexDTOS = this.getPlagiarism(target, sentences.getRawText(), partPlagiarism);

                    // TODO: Store data plagiarism of sentences
                    plagiarismSentencesDTO.setTarget(target);
                    //plagiarismSentencesDTO.setPositionTarget(i + 1); // Start from 0
                    plagiarismSentencesDTO.setMatching(sentences.getRawText());
                    //plagiarismSentencesDTO.setPositionMatching(position); // Start from 1
                    plagiarismSentencesDTO.setRate(percent);
                    plagiarismSentencesDTO.setTokenizerPlagiarism(indexDTOS);
                }
            }
            // TODO: Store sentence have highest rate
            if (plagiarismSentencesDTO.getRate() > 0) {
                plagiarismSentences.add(plagiarismSentencesDTO);
            }

            // TODO: Store data of target sentences have highest rate
            if (sentenceHighestRate != 0) {
                totalRateMatchingCondition += sentenceHighestRate;
            }
            // TODO: Calculate plagiarism of document
            int totalSentences = targets.length > mapSentences.size() ? targets.length : mapSentences.size();
            float rateOfDocument = totalRateMatchingCondition / totalSentences;
            if (rateOfDocument > 70 && rateOfDocument > plagiarismDocumentDTO.getRate()) {
                // TODO: Build data store plagiarism info of document
                plagiarismDocumentDTO.setDocumentId(documentMatchingId);
                plagiarismDocumentDTO.setRate(rateOfDocument);
                plagiarismDocumentDTO.setPlagiarism(plagiarismSentences);
            }
        }
    }

    private boolean existPlagiarism(PlagiarismDocumentDTO plagiarismDocumentDTO) {
        return plagiarismDocumentDTO != null && plagiarismDocumentDTO.getDocumentId() != null
                && plagiarismDocumentDTO.getRate() > 80 && plagiarismDocumentDTO.getPlagiarism() != null
                && plagiarismDocumentDTO.getPlagiarism().size() > 0;
    }
}
