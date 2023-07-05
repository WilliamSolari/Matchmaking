public class Player implements Runnable {

	private int id;
	private MatchmakingQueue matchmakingQueue;
	private GameInterface gameInterface;


	public Player(int id, MatchmakingQueue matchmakingQueue, GameInterface gameInterface) {
		this.id = id;
		this.matchmakingQueue = matchmakingQueue;
		this.gameInterface=gameInterface;
	}

	public int getId() {
		return id;
	}

	@Override
	public void run() {
		System.out.println("Player " + id + " joined the matchmaking queue.");
		gameInterface.addWaitingPlayer(this);
		try {
			//Thread.sleep(1000);
			matchmakingQueue.getBarrier().await();
			matchmakingQueue.getGameSemaphore().acquire();

			System.out.println("Player " + id + " is performing game activities.");
			gameInterface.movePlayerToPlaying(this);
			Thread.sleep(5000);
			matchmakingQueue.getGameSemaphore().release();
			System.out.println("Player " + id + " has finished the game.");
			gameInterface.movePlayerToFinished(this);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public void play() {
		System.out.println("Player " + id + "is playing");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
	    return "Player"+id;
	}

}