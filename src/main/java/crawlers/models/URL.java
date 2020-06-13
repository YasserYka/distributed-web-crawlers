package crawlers.models;

import java.util.Date;

import lombok.Data;

@Data
public class URL {

	private String domainName;
	private String url;
	private Date createdAt;

}
