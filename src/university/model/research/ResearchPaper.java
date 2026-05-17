package university.model.research;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private static final long serialVersionUID = 1L;

    private String paperId;
    private String title;
    private List<Researcher> authors;
    private String journal;
    private int pages;
    private Date datePublished;
    private String doi;
    private int citations;
    private String field;

    // Static Comparators
    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDatePublished);

    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

    public ResearchPaper(String paperId, String title, List<Researcher> authors,
                         String journal, int pages, Date datePublished,
                         String doi, int citations, String field) {
        this.paperId = paperId;
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
        this.datePublished = datePublished;
        this.doi = doi;
        this.citations = citations;
        this.field = field;
    }

    // Default natural order: by citations descending
    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    // Getters
    public String getPaperId()         { return paperId; }
    public String getTitle()           { return title; }
    public List<Researcher> getAuthors() { return authors; }
    public String getJournal()         { return journal; }
    public int getPages()              { return pages; }
    public Date getDatePublished()     { return datePublished; }
    public String getDoi()             { return doi; }
    public int getCitations()          { return citations; }
    public String getField()           { return field; }
    public void setCitations(int c)    { this.citations = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(doi, ((ResearchPaper) o).doi);
    }

    @Override
    public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return "\"" + title + "\" | " + journal + " | Citations: " + citations
                + " | Pages: " + pages + " | Date: " + datePublished + " | DOI: " + doi;
    }
}
