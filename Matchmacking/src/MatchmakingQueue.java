import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MatchmakingQueue {
	private static final int PLAYERS_NEEDED_PER_GAME = 4;
	private Semaphore gameSemaphore;
	private CyclicBarrier barrier;
	private int totalPlayers;
	private int maxGames;
	private GameInterface gameInterface;

	public MatchmakingQueue(int totalPlayers, GameInterface gameInterface, int maxGames) {
		this.gameInterface=gameInterface;
		this.totalPlayers = totalPlayers;
		this.maxGames=maxGames;
		gameSemaphore=new Semaphore(PLAYERS_NEEDED_PER_GAME*maxGames);
		barrier = new CyclicBarrier(PLAYERS_NEEDED_PER_GAME);
	}

	public void start() throws InterruptedException {
		for (int i = 1; i <= totalPlayers; i++) {
			Player player = new Player(i, this, gameInterface);
			
			Thread thread = new Thread(player);
			thread.start();

			Thread.sleep(1000);
		}
	}

	public CyclicBarrier getBarrier() {
		return barrier;
	}

	public Semaphore getGameSemaphore() {
		return gameSemaphore;
	}


}
