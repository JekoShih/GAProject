package jhe.lin.main.assignments;

import java.util.HashMap;

public class WorkType {
	public enum TYPE {
		A, B, C
	}

	public enum LEVEL {
		EASY, NORMAL, COMPLEX, VERY_COMPLEX
	}

	private HashMap<WorkType.LEVEL, Float> devStandardDays = new HashMap<WorkType.LEVEL, Float>();
	
	public WorkType(float easy, float normal, float complex, float veryComplex) {
		devStandardDays.put(WorkType.LEVEL.EASY, easy);
		devStandardDays.put(WorkType.LEVEL.NORMAL, normal);
		devStandardDays.put(WorkType.LEVEL.COMPLEX, complex);
		devStandardDays.put(WorkType.LEVEL.VERY_COMPLEX, veryComplex);
	}

	public HashMap<WorkType.LEVEL, Float> getDevStandardDays() {
		return devStandardDays;
	}
}
