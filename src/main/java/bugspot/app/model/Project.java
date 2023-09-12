package bugspot.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private AppUser ownerAppUser;

	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@URL
	private String repoURL;
	
	private List<String> productVersions = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "project_members",
	        joinColumns = @JoinColumn(name = "project_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<AppUser> members = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "project_admins",
	        joinColumns = @JoinColumn(name = "project_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<AppUser> adminUsers = new HashSet<>();
	
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();

	
}