package jhe.lin.util;

public interface GAflow {
	public void initParameters();
	public void initPopulation();
	public boolean evalFitness();
	public void selection();
	public void crossover();
	public void mutation();
	public void outputResult();

}
