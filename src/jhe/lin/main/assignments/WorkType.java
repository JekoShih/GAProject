package jhe.lin.main.assignments;

import java.util.HashMap;

public class WorkType {
	public enum TYPE {
		A, B, C
	}

	public enum LEVEL {
		EASY, NORMAL, COMPLEX, VERY_COMPLEX
	}

	private HashMap<WorkType.LEVEL, String> devStandardDays = new HashMap<WorkType.LEVEL, String>();
	
	public WorkType(String easy, String normal, String complex, String veryComplex) {
		devStandardDays.put(WorkType.LEVEL.EASY, easy);
		devStandardDays.put(WorkType.LEVEL.NORMAL, normal);
		devStandardDays.put(WorkType.LEVEL.COMPLEX, complex);
		devStandardDays.put(WorkType.LEVEL.VERY_COMPLEX, veryComplex);
	}

	public HashMap<WorkType.LEVEL, String> getDevStandardDays() {
		return devStandardDays;
	}
}
