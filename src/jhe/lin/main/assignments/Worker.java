package jhe.lin.main.assignments;

import java.util.HashMap;

public class Worker {
	private HashMap<WorkType.TYPE, Float> Efficiency = new HashMap<WorkType.TYPE, Float>();

	public Worker(float A_Efficiency, float B_Efficiency, float C_Efficiency) {
		Efficiency.put(WorkType.TYPE.A, A_Efficiency);
		Efficiency.put(WorkType.TYPE.B, B_Efficiency);
		Efficiency.put(WorkType.TYPE.C, C_Efficiency);
	}

	public HashMap<WorkType.TYPE, Float> getEfficiency() {
		return Efficiency;
	}
}
