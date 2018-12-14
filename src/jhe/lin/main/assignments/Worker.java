package jhe.lin.main.assignments;

import java.util.HashMap;

public class Worker {
	private HashMap<WorkType.TYPE, String> Efficiency = new HashMap<WorkType.TYPE, String>();

	public Worker(String A_Efficiency, String B_Efficiency, String C_Efficiency) {
		Efficiency.put(WorkType.TYPE.A, A_Efficiency);
		Efficiency.put(WorkType.TYPE.B, B_Efficiency);
		Efficiency.put(WorkType.TYPE.C, C_Efficiency);
	}

	public HashMap<WorkType.TYPE, String> getEfficiency() {
		return Efficiency;
	}
}
