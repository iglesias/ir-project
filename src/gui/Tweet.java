/***********************************************************************
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.text.DecimalFormat;

@SuppressWarnings("rawtypes")
public class Tweet implements Comparable{

	/**
	 * Attributes.
	 */
	private String profile_url = "";
	private String author = "";
	private String text = "";
	private Double tfidfScore = 0.0;
	private Double pagerankMentionScore = null;
	private Double pagerankRetweetScore = null;
	
	private Double finalScore = 0.0;
	
	private DecimalFormat df = new DecimalFormat("#0.00000000");
	
	/**
	 * Constructor.
	 */
	public Tweet(String p, String a, String t, Double tfidf, Double prMention, Double prRetweet){
		this.profile_url = p;
		this.author = a;
		this.text = t;
		this.tfidfScore = tfidf;
		this.pagerankMentionScore = prMention;
		this.pagerankRetweetScore = prRetweet;
	}
	
	/**
	 * Get.
	 */
	public String getProfileUrl() { return this.profile_url; }
	public String getAuthor() { return this.author; }
	public String getText() { return this.text; }
	public Double getTFIDFScore() { return this.tfidfScore; }
	public Double getPRMentionedScore() { return this.pagerankMentionScore; }
	public Double getPRRetweetScore() { return this.pagerankRetweetScore; }
	public Double getFinalScore(){ return this.finalScore; }
	
	/**
	 * Set.
	 */
	public void setProfileUrl(String url) { this.profile_url = url; }
	public void setAuthor(String author) { this.author = author; }
	public void setText(String text) { this.text = text; }
	public void setTFIDFScore(double s) { this.tfidfScore = s; }
	public void setPRMentionedScore(double s) { this.pagerankMentionScore = s; } 
	public void setPRRetweetScore(double s) { this.pagerankRetweetScore = s; }
	public void setFinalScore(double s) { this.finalScore = s; }
	
	/**
	 * Get the value of the total score.
	 */
	public void computeFinalScore(){
		
		// Global values (from percent to norm).
		double mentionPercent = PageRankGUI.actPagerankMentionPercent*0.01;
		double pagerankPercent = PageRankGUI.actPagerankPercent*0.01;
		
		// Calculate the score between Mentioned vs Retweeted.
		double score1 = 0.0;
		if ((this.pagerankMentionScore==null) && (this.pagerankRetweetScore==null)){
			score1 = this.tfidfScore;
		} else {
			if (this.pagerankMentionScore==null)
				score1 = this.pagerankRetweetScore;
			else
				if (this.pagerankRetweetScore==null)
					score1 = this.pagerankMentionScore;
				else
					score1 = (1-mentionPercent)*this.pagerankMentionScore + (mentionPercent*this.pagerankRetweetScore);
		}
		
		// Calculate the score between Pagerank vs TFIDF.
		this.finalScore = (1-pagerankPercent)*score1 + (pagerankPercent*this.tfidfScore*0.001);
	}
	
	/**
	 * Convert tweet to HTML.
	 */
	public String converToHTML(){		
		String content = "<tr>";
		content += "<td<img src='" + this.profile_url + "' width=50 height=50></img></td>";
		content += "<td align=left valign=top>";
		content += "<B>" + this.author + "</B><br>";
		content += "<I>" + this.text + "</I></td>";
		content += "<td valign=top><font size=2>" + df.format(this.finalScore) + "</font></td>";
		content += "</tr>";
		return content;
	}
	
	/**
	 * CompareTo
	 */
	public int compareTo(Object o){
		return (((Tweet) o).getFinalScore()).compareTo(this.finalScore);
	}
}
