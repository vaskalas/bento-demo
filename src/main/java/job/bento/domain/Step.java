package job.bento.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Step {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private int distance;
	
	@Column(nullable=false, unique=true)
	private String urlPath;
	
	@ManyToOne(cascade={CascadeType.PERSIST})
	private Step next;
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public Step getNext() {
		return next;
	}
	
	public void setNext(Step next) {
		this.next = next;
	}
	
	public String getUrlPath() {
		return urlPath;
	}
	
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urlPath == null) ? 0 : urlPath.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (urlPath == null) {
			if (other.urlPath != null)
				return false;
		} else if (!urlPath.equals(other.urlPath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Step [urlPath=" + urlPath + "]";
	}
}
