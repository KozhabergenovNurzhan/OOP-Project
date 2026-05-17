package university.model.research;

import university.enums.ProjectStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectId;
    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<Researcher> participants;
    private Date startDate;
    private ProjectStatus status;

    public ResearchProject(String projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.startDate = new Date();
        this.status = ProjectStatus.ACTIVE;
    }

    public void addParticipant(Researcher r) {
        if (!participants.contains(r)) {
            participants.add(r);
            r.joinProject(this);
            System.out.println("Researcher added to project: " + topic);
        }
    }

    public void removeParticipant(Researcher r) {
        participants.remove(r);
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    public void printDetails() {
        System.out.println("Project: " + topic + " | Status: " + status);
        System.out.println("Participants: " + participants.size());
        System.out.println("Papers published: " + publishedPapers.size());
    }

    // Getters & Setters
    public String getProjectId()              { return projectId; }
    public String getTopic()                  { return topic; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }
    public List<Researcher> getParticipants() { return participants; }
    public ProjectStatus getStatus()          { return status; }
    public void setStatus(ProjectStatus s)    { this.status = s; }
    public Date getStartDate()                 {return startDate;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchProject)) return false;
        return Objects.equals(projectId, ((ResearchProject) o).projectId);
    }

    @Override
    public int hashCode() { return Objects.hash(projectId); }

    @Override
    public String toString() {
        return "ResearchProject{topic='" + topic + "', status=" + status + "}";
    }
}
