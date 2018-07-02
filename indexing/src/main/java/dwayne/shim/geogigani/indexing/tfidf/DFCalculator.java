package dwayne.shim.geogigani.indexing.tfidf;

import dwayne.shim.geogigani.core.keyword.KeywordExtractor;

import java.util.*;

public class DFCalculator {

    private final KeywordExtractor ke;
    public DFCalculator(KeywordExtractor ke) {
        this.ke = ke;
    }

    public void df(String text) {
        if(text == null || text.length() == 0) return;

        Set<String> keywordSet = new HashSet<>(ke.extract(text.toLowerCase()));
        df(keywordSet, true);
    }

    private int nDocument = 0;
    private final Map<String, Integer> df = new HashMap<>();
    private void df(Set<String> keywordSet, boolean smooth) {
        int d = smooth ? 1 : 0;
        ++nDocument;

        // 1. DF
        for(String keyword : keywordSet) {
            Integer value = df.get(keyword);
            if(value == null) value = d;
            df.put(keyword, value + 1);
        }
    }

    private final Map<String, Double> idf = new HashMap<>();
    public void idf(boolean addOne) {
        int a = addOne ? 1 : 0;

        for(String keyword : df.keySet()) {
            Integer count = df.get(keyword);
            double score = Math.log(nDocument / (double)count) + a;
            idf.put(keyword, score);
        }
    }

    public void printSortedIDF() {
        List<IDF> idfList = new ArrayList<>();
        for(String keyword : idf.keySet())
            idfList.add(new IDF(keyword, idf.get(keyword)));

        Collections.sort(idfList);

        for(IDF idf : idfList)
            System.out.println(idf);
    }

    private static class IDF implements Comparable<IDF> {
        String keyword;
        double score;

        IDF(String keyword, double score) {
            this.keyword = keyword;
            this.score = score;
        }

        @Override
        public int compareTo(IDF o) {
            return score > o.score ? -1 : score == o.score ? 0 : 1;
        }

        @Override
        public String toString() {
            return keyword + '\t' + score;
        }
    }
}
