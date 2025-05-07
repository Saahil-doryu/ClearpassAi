import java.util.ArrayList;
import java.util.List;

public class DataModel {
	private List<QuestionData> history;

	public DataModel() {
		history = new ArrayList<>();
	}

	public void setHistory(QuestionData qd) {
		history.add(qd);
	}

	public List<QuestionData> getHistory() {
		return history;
	}
}
