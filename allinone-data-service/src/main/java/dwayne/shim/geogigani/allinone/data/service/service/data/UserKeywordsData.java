package dwayne.shim.geogigani.allinone.data.service.service.data;

public class UserKeywordsData {

    private final String userId;
    private final StringBuilder oldKeywordSb;
    private volatile long lastAccessTime;

    public UserKeywordsData(String userId) {
        this.userId = userId;
        this.oldKeywordSb = new StringBuilder();
        this.lastAccessTime = System.currentTimeMillis();
    }

    private void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public synchronized boolean notAccessedFor(long duration) {
        return System.currentTimeMillis() - lastAccessTime > duration;
    }

    public synchronized String getKeywords() {
        updateLastAccessTime();
        return oldKeywordSb.toString();
    }

    public synchronized void appendKeywords(String newKeywordStr,
                                              int userKeywordsSize) {

        updateLastAccessTime();

        String[] newKeywords = newKeywordStr.split("\\s+");
        String[] oldKeywords = oldKeywordSb.toString().split("\\s+");

        int sumOldNew = oldKeywords.length + newKeywords.length;
        if(sumOldNew <= userKeywordsSize) {
            oldKeywordSb.append(' ').append(newKeywordStr);
            return;
        }

        oldKeywordSb.setLength(0);
        int start = sumOldNew - userKeywordsSize;
        for(; start < oldKeywords.length; start++) {
            if(oldKeywordSb.length() > 0) oldKeywordSb.append(' ');
            oldKeywordSb.append(oldKeywords[start]);
        }

        for(; start < newKeywords.length; start++) {
            if(oldKeywordSb.length() > 0) oldKeywordSb.append(' ');
            oldKeywordSb.append(newKeywords[start]);
        }
    }
}
