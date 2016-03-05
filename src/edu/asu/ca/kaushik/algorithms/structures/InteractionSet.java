package edu.asu.ca.kaushik.algorithms.structures;

import java.util.List;

public interface InteractionSet {

	public boolean isEmpty();
	public int deleteInteractions(Integer[] newRandRow, int[] covD);
	public List<ColGroup> getAllColGroups();
	public boolean contains(Interaction interaction);
	public Interaction selectInteraction(Integer[] newRow);
	public int getComp();
	public void deleteFullyDeterminedInteractions(Integer[] newRow);
	public void deletIncompatibleInteractions(Interaction interaction);
	public int getCoverage(Integer[] newRow, int[] covD);
	public int getNumUncovInt(ColGroup colGr);
	public double computeProbCoverage(int[] sCols, int[] entries);
	public int getT();
}
