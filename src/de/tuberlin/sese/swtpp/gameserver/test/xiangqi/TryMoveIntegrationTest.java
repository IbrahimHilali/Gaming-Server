package de.tuberlin.sese.swtpp.gameserver.test.xiangqi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

public class TryMoveIntegrationTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");

	Player redPlayer = null;
	Player blackPlayer = null;
	Game game = null;
	GameController controller;

	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();

		int gameID = controller.startGame(user1, "", "xiangqi");

		game = controller.getGame(gameID);
		redPlayer = game.getPlayer(user1);

	}

	public void startGame() {
		controller.joinGame(user2, "xiangqi");
		blackPlayer = game.getPlayer(user2);
	}

	public void startGame(String initialBoard, boolean redNext) {
		startGame();

		game.setBoard(initialBoard);
		game.setNextPlayer(redNext ? redPlayer : blackPlayer);
	}

	public void assertMove(String move, boolean red, boolean expectedResult) {
		if (red)
			assertEquals(expectedResult, game.tryMove(move, redPlayer));
		else
			assertEquals(expectedResult, game.tryMove(move, blackPlayer));
	}

	public void assertGameState(String expectedBoard, boolean redNext, boolean finished, boolean redWon) {
		assertEquals(expectedBoard, game.getBoard());
		assertEquals(finished, game.isFinished());

		if (!game.isFinished()) {
			assertEquals(redNext, game.getNextPlayer() == redPlayer);
		} else {
			assertEquals(redWon, redPlayer.isWinner());
			assertEquals(!redWon, blackPlayer.isWinner());
		}
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/

	@Test
	public void exampleTest() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR", true);
		assertMove("e3-e4", true, true);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/4S4/S1S3S1S/1C5C1/9/RHEAGAEHR", false, false, false);
	}

	// TODO: implement test cases of same kind as example here
	
	@Test
	public void CannonCheckMateGeneralAndAdvisorProtectTest() {
		startGame("3rga3/9/7R1/4h4/9/4h3S/9/4C4/4a4/4G4", true);
		assertMove("e2-e4", true, true);
		assertGameState("3rga3/9/7R1/4h4/9/4C3S/9/9/4a4/4G4", false, false, false);
	}

	@Test
	public void CannonMovesTest() {
		startGame("9/2a6/9/2r6/9/6E2/s1C1Hr3/9/2H6/9", true);
		assertMove("c3-c6", true, false);
		assertMove("c3-a3", true, false);
		assertMove("c3-f3", true, false);
		assertGameState("9/2a6/9/2r6/9/6E2/s1C1Hr3/9/2H6/9", true, false, false);
		assertMove("c3-c8", true, true);
		assertGameState("9/2C6/9/2r6/9/6E2/s3Hr3/9/2H6/9", false, false, false);
	}


	
	@Test
	public void RookCheckMateGeneralTest() {
		startGame("4g4/2R6/4S2E1/9/9/8S/9/4C4/9/4G4", true);
		assertMove("c8-c9", true, true);
		assertGameState("2R1g4/9/4S2E1/9/9/8S/9/4C4/9/4G4", true, true, true);
	}

	@Test
	public void GeneralCheckMateTest() {
		startGame("4g4/9/4S2E1/1R7/9/8S/9/9/9/4G4", true);
		assertMove("b6-b9", true, true);
		assertGameState("1R2g4/9/4S2E1/9/9/8S/9/9/9/4G4", true, true, true);
	}

	@Test
	public void GeneralWantToJumpTest() {
		startGame("4g4/9/4S2E1/9/9/8S/9/9/9/4G4", false);
		assertMove("e9-d8", false, false);
		assertGameState("4g4/9/4S2E1/9/9/8S/9/9/9/4G4", false, false, false);
	}

	@Test
	public void GeneralAttakEscapingTrapTest() {
		startGame("3Rg4/9/4S2E1/9/9/8S/9/9/9/4G4", false);
		assertMove("e9-d9", false, true);
		assertGameState("3g5/9/4S2E1/9/9/8S/9/9/9/4G4", true, false, false);
	}

	@Test
	public void GeneralLookMeanDeathTest() {
		// if moved Look Death
		startGame("4g4/9/9/9/4S4/9/2C6/9/9/4G4", true);
		assertMove("e5-f2", true, false);
		assertGameState("4g4/9/9/9/4S4/9/2C6/9/9/4G4", true, false, false);
	
		startGame("4g4/4r4/9/9/9/8S/9/9/9/4G4", false);
		assertMove("e8-h8", false, false);
		assertGameState("4g4/4r4/9/9/9/8S/9/9/9/4G4", false, false, false);
		
	
		startGame("4ga3/2e6/3a5/3c3s1/9/3S1sC2/1S3RS2/3A5/9/3G1R3", true);
		assertMove("d0-e0", true, false);
		assertGameState("4ga3/2e6/3a5/3c3s1/9/3S1sC2/1S3RS2/3A5/9/3G1R3", true, false, false);
	}

	@Test
	public void AdvisorTryBreakHisPathTest() {
		startGame("4r4/4s4/9/9/9/8S/4S4/4H4/9/5A3", true);
		assertMove("f0-g0", true, false);
		assertGameState("4r4/4s4/9/9/9/8S/4S4/4H4/9/5A3", true, false, false);

		startGame("5a3/9/5g3/9/9/9/9/9/9/9", false);
		assertMove("f9-e9", false, false);
		assertGameState("5a3/9/5g3/9/9/9/9/9/9/9", false, false, false);
	}

	@Test
	public void AdvisorLookInsidePalaceTest() {
		startGame("4ra3/9/4s4/9/9/8S/4S4/4H4/9/3A5", false);
		assertMove("f9-e8", false, true);
		assertGameState("4r4/4a4/4s4/9/9/8S/4S4/4H4/9/3A5", true, false, false);
		assertMove("d0-e1", true, true);
		assertGameState("4r4/4a4/4s4/9/9/8S/4S4/4H4/4A4/9", false, false, false);
		assertMove("e8-d9", false, true);
		assertGameState("3ar4/9/4s4/9/9/8S/4S4/4H4/4A4/9", true, false, false);
	}

	@Test
	public void generalRightMovementTest() {

		startGame("4g4/9/9/9/9/9/9/9/9/9", false);
		assertMove("e9-e8", false, true);
		assertGameState("9/4g4/9/9/9/9/9/9/9/9", true, false, false);

		startGame("9/9/9/9/9/9/9/9/4G4/9", true);
		assertMove("e1-f1", true, true);
		assertGameState("9/9/9/9/9/9/9/9/5G3/9", false, false, false);

		startGame("9/4g4/9/9/9/9/9/9/9/9", false);
		assertMove("e8-f8", false, true);
		assertGameState("9/5g3/9/9/9/9/9/9/9/9", true, false, false);

		startGame("9/9/9/9/9/9/9/9/4G4/9", true);
		assertMove("e1-d1", true, true);
		assertGameState("9/9/9/9/9/9/9/9/3G5/9", false, false, false);

		startGame("9/4g4/9/9/9/9/9/9/9/9", false);
		assertMove("e8-d8", false, true);
		assertGameState("9/3g5/9/9/9/9/9/9/9/9", true, false, false);
	}

	@Test
	public void generalBackwordMovementTest() {
		startGame("9/9/9/9/9/9/9/5G3/9/9", true);
		assertMove("f2-f1", true, true);
		assertGameState("9/9/9/9/9/9/9/9/5G3/9", false, false, false);

		startGame("9/9/5g3/9/9/9/9/9/9/9", false);
		assertMove("f7-f8", false, true);
		assertGameState("9/5g3/9/9/9/9/9/9/9/9", true, false, false);
	}

	@Test
	public void generalEscapingThePalaceTest() {
		startGame("9/9/9/9/9/9/9/5G3/9/9", true);
		assertMove("f2-g2", true, false);
		assertGameState("9/9/9/9/9/9/9/5G3/9/9", true, false, false);

		startGame("9/9/3g5/9/9/9/9/9/9/9", false);
		assertMove("d7-c7", false, false);
		assertGameState("9/9/3g5/9/9/9/9/9/9/9", false, false, false);
	}

	@Test
	public void cannonMoveVerticalTest() {
		startGame("9/9/4sh3/9/4s4/9/9/4C4/9/9", true);
		assertMove("e2-e7", true, true);
		assertGameState("9/9/4Ch3/9/4s4/9/9/9/9/9", false, false, false);
	}

	@Test
	public void cannonCaptureEmptySquareTest() {
		startGame("9/9/5h3/9/4s4/9/9/4C4/9/9", true);
		assertMove("e2-e7", true, false);
		assertGameState("9/9/5h3/9/4s4/9/9/4C4/9/9", true, false, false);
	}

	@Test
	public void cannonRightMoveTest() {
		startGame("9/9/9/9/9/9/9/4C4/9/9", true);
		assertMove("e2-f5", true, false);
		assertGameState("9/9/9/9/9/9/9/4C4/9/9", true, false, false);
	}

	@Test
	public void cannonWithoutSpaceInBetweenTest() {
		startGame("9/9/4sh3/4s4/9/9/9/4C4/9/9", true);
		assertMove("e2-e7", true, true);
		assertGameState("9/9/4Ch3/4s4/9/9/9/9/9/9", false, false, false);
	}

	@Test
	public void horseNormalMoveTest() {
		startGame("9/9/9/9/9/9/9/4H4/9/9", true);
		assertMove("e2-f4", true, true);
		assertGameState("9/9/9/9/9/5H3/9/9/9/9", false, false, false);

		startGame("9/9/9/9/9/5h3/9/9/9/9", false);
		assertMove("f4-e2", false, true);
		assertGameState("9/9/9/9/9/9/9/4h4/9/9", true, false, false);
	}

	@Test
	public void horseMoveTryToJumpTest() {
		startGame("9/4hg3/4c4/9/9/9/4C4/4H4/9/9", true);
		//assertMove("e2-f5", true, false);
		assertMove("e2-f4", true,false);
		assertGameState("9/4hg3/4c4/9/9/9/4C4/4H4/9/9", true, false, false);
	}

	@Test
	public void elephantNromalMoveTest() {
		startGame("9/9/9/9/9/9/4H4/4EH3/9/9", true);
		assertMove("e2-g4", true, true);
		assertGameState("9/9/9/9/9/6E2/4H4/5H3/9/9", false, false, false);
	}

	@Test
	public void elephantTryToCrossRiverTest() {
		startGame("9/9/9/9/9/6E2/4H4/5H3/9/9", true);
		assertMove("g4-e6", true, false);
		assertGameState("9/9/9/9/9/6E2/4H4/5H3/9/9", true, false, false);
	}

	@Test
	public void rookMoveVerticalTest() {
		startGame("4r4/9/9/9/9/8S/4S4/4EH3/9/9", false);
		assertMove("e9-e4", false, true);
		assertGameState("9/9/9/9/9/4r3S/4S4/4EH3/9/9", true, false, false);

		startGame("4r4/9/9/9/9/6E2/4H4/5H3/9/9", false);
		assertMove("e9-i9", false, true);
		assertGameState("8r/9/9/9/9/6E2/4H4/5H3/9/9", true, false, false);
	}

	@Test
	public void rookMoveHorizantalTest() {
		startGame("4r1h2/9/9/9/9/6E2/4H4/5H3/9/9", false);
		assertMove("e9-i9", false, false);
		assertGameState("4r1h2/9/9/9/9/6E2/4H4/5H3/9/9", false, false, false);
	}

	@Test
	public void soldierMoveBeforeCrossRiverTest() {
		startGame("4r4/4s4/9/9/9/8S/4S4/4EH3/9/9", false);
		assertMove("e8-e7", false, true);
		assertGameState("4r4/9/4s4/9/9/8S/4S4/4EH3/9/9", true, false, false);
		assertMove("i4-i5", true, true);
		assertGameState("4r4/9/4s4/9/8S/9/4S4/4EH3/9/9", false, false, false);
	}

	@Test
	public void soldierMoveAfterCrossRiverTest() {
		startGame("4S4/5s3/9/9/9/8S/9/4EH3/9/9", true);
		assertMove("e9-f9", true, true);
		assertGameState("5S3/5s3/9/9/9/8S/9/4EH3/9/9", false, false, false);

		startGame("4S4/5s3/9/9/9/8S/9/4EH3/9/5s3", false);
		assertMove("f0-e0", false, true);
		assertGameState("4S4/5s3/9/9/9/8S/9/4EH3/9/4s4", true, false, false);
	}

	@Test
	public void soldierMoveBackwardTest() {
		startGame("9/4s4/9/9/9/8S/4S4/4EH3/9/9", false);
		assertMove("e8-e9", false, false);
		assertMove("e8-f8", false, false);
		assertGameState("9/4s4/9/9/9/8S/4S4/4EH3/9/9", false, false, false);
	}
}
