package bugspot.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Issue {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	private AppUser creatorAppUser;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Category category;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Reproducibility reproducibility;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    @NotBlank
    private String projectVersion;
    
    @NotBlank
    private String summary;
    
    @NotBlank
    private String description;
    
    private String stepsToReproduce;
    
    private String additionalInformation;
    
    private String sourceCode;
    
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}
