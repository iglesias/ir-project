import java.io.Serializable;

public class LinksEntry implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String screenName;
	private int numberOfPaths;
	
	LinksEntry (String n){
		this.screenName = n;
		this.numberOfPaths = 1;
	}
	
	public String getScreenName(){
		return this.screenName;
	}
	
	public int getNumberOfPaths(){
		return this.numberOfPaths;
	}
	
	public void addOne(){
		this.numberOfPaths++;
	}
	
}
