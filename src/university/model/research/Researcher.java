package university.model.research;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface Researcher {

    void printPapers(Comparator<ResearchPaper> comparator);

    default int getHIndex() {
        List<Integer> citations = getPapers().stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    void addPaper(ResearchPaper paper);

    List<ResearchPaper> getPapers();

    List<ResearchProject> getProjects();

    void joinProject(ResearchProject project);

    static void printPapersSorted(String name, List<ResearchPaper> papers, Comparator<ResearchPaper> comparator) {
        if (papers.isEmpty()) {
            System.out.println(name + " has no papers.");
            return;
        }
        System.out.println("=== Papers by " + name + " ===");
        papers.stream().sorted(comparator).forEach(System.out::println);
    }
}
