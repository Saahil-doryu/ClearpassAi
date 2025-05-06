import java.util.ArrayList;
import java.util.List;

public class DataModel {
	private List<QuestionData> history;
	
	public DataModel () {
		history = new ArrayList<>(5);
	}
	
	public void setHistory(QuestionData qf ) {
		history.add(qf);
	}
	
	public List<QuestionData> getHistory(){
		return history;
	}
	
}
