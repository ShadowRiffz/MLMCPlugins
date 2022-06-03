package me.neoblade298.neoquests.quests;

public class CompletedQuest {
	private Quest q;
	private int stage;
	private boolean success;
	public CompletedQuest(Quest q, int stage, boolean success) {
		this.q = q;
		this.stage = stage;
		this.success = success;
	}
	public Quest getQuest() {
		return q;
	}
	public int getStage() {
		return stage;
	}
	public boolean isSuccess() {
		return success;
	}
	
}
