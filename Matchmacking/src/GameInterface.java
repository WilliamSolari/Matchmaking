import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GameInterface extends JFrame {

	private MatchmakingQueue matchmakingSystem;
	private HashMap<Integer, Player> waitingPlayers;
	private HashMap<Integer, Player> playingPlayers;
	private HashMap<Integer, Player> finishedPlayers;
	private JPanel mainPanel;
	private JPanel waitingPanel;
	private JPanel playingPanel;
	private JPanel finishedPanel;

	public GameInterface() {
		waitingPlayers = new HashMap<>();
		playingPlayers = new HashMap<>();
		finishedPlayers = new HashMap<>();
		initComponents();
	}

	private void initComponents() {
		setTitle("Game Interface");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 3));

		waitingPanel = new JPanel(new GridLayout(0, 1));
		waitingPanel.setBorder(BorderFactory.createTitledBorder("Waiting Players"));
		mainPanel.add(waitingPanel);

		playingPanel = new JPanel(new GridLayout(0, 1));
		playingPanel.setBorder(BorderFactory.createTitledBorder("Playing Players"));
		mainPanel.add(playingPanel);

		finishedPanel = new JPanel(new GridLayout(0, 1));
		finishedPanel.setBorder(BorderFactory.createTitledBorder("Finished Players"));
		mainPanel.add(finishedPanel);

		JPanel buttonPanel = new JPanel();
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startMatchmaking();
			}
		});
		buttonPanel.add(startButton);

		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private synchronized  void startMatchmaking() {
		int totalPlayers = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the number of players that want to play:"));
		int maxGames = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the maximum number of games allowed simultaniously:"));
			
		matchmakingSystem = new MatchmakingQueue(totalPlayers, this, maxGames);
		Thread matchmakingThread = new Thread(new MatchmakingRunnable(matchmakingSystem));
		matchmakingThread.start();
	}

	public synchronized void addWaitingPlayer(Player player) {
		waitingPanel.removeAll();
		waitingPlayers.put(player.getId(), player);
		JList<Player> list = new JList(waitingPlayers.values().toArray());
		waitingPanel.add(list);
		waitingPanel.revalidate();
		waitingPanel.repaint();
	}

	public synchronized void movePlayerToPlaying(Player player) {
		waitingPanel.removeAll();
		waitingPanel.revalidate();
		waitingPanel.repaint();
		waitingPlayers.remove(player.getId());
		playingPanel.removeAll();
		playingPanel.revalidate();
		playingPanel.repaint();
		playingPlayers.put(player.getId(), player);
		JList<Player> list = new JList(waitingPlayers.values().toArray());
		JList<Player> listPlaying = new JList(playingPlayers.values().toArray());
		waitingPanel.add(list);
		playingPanel.add(listPlaying);
		waitingPanel.revalidate();
		waitingPanel.repaint();
		playingPanel.revalidate();
		playingPanel.repaint();
	}

	public synchronized void movePlayerToFinished(Player player) {
		playingPanel.removeAll();
		playingPanel.revalidate();
		playingPanel.repaint();
		playingPlayers.remove(player.getId());
		finishedPanel.removeAll();
		finishedPanel.revalidate();
		finishedPanel.repaint();
		finishedPlayers.put(player.getId(), player);
		JList<Player> list = new JList(playingPlayers.values().toArray());
		JList<Player> listPlaying = new JList(finishedPlayers.values().toArray());
		playingPanel.add(list);
		finishedPanel.add(listPlaying);
		playingPanel.revalidate();
		playingPanel.repaint();
		finishedPanel.revalidate();
		finishedPanel.repaint();
	}

	private class MatchmakingRunnable implements Runnable {
		private MatchmakingQueue matchmakingQueue;

		public MatchmakingRunnable(MatchmakingQueue matchmakingQueue) {
			this.matchmakingQueue = matchmakingQueue;
		}

		@Override
		public void run() {
			try {
				matchmakingQueue.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
