package university.model.research;

import university.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Wraps any User to add Researcher behavior for standalone employees
 * who are neither Teachers nor Students.
 */
public class ResearcherDecorator implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    private final User user;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    public ResearcherDecorator(User user) {
        this.user = user;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    @Override
    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        Researcher.printPapersSorted(user.getLogin(), papers, comparator);
    }

    @Override
    public List<ResearchProject> getProjects() {
        return projects;
    }

    @Override
    public void joinProject(ResearchProject project) {
        if (!projects.contains(project)) {
            projects.add(project);
        }
    }

    public User getUser() { return user; }

    @Override
    public String toString() {
        return "Researcher{user=" + user.getLogin() + ", h-index=" + getHIndex() + "}";
    }
}
