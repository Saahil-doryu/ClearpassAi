
public class QuestionData {
	private String question;
	private String answer;
	private String feedback;

	public  QuestionData() {
		this.question = "";
		this.feedback = "";
		this.answer = "";
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getQuestion(String question) {
		return question;
	}
	
	public String getFeedback(String feedback) {
		return feedback;
	}
	
	public String getAnswer(String answer) {
		return answer;
	}
}

