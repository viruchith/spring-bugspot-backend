package bugspot.app.model;

import java.util.List;

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
	
	private List<String> productVersions;
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "project_members",
	        joinColumns = @JoinColumn(name = "project_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<AppUser> members;
	
	@ManyToMany
	@JoinTable(name = "project_admins",
	        joinColumns = @JoinColumn(name = "project_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<AppUser> adminUsers;
	
}
