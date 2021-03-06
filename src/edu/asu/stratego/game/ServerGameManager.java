package edu.asu.stratego.game;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import edu.asu.stratego.game.board.ServerBoard;

/**
 * Task to manage a Stratego game between two clients.
 */
public class ServerGameManager implements Runnable {

	private final String session;

	private ServerBoard board = new ServerBoard();

	private ObjectOutputStream toPlayerOne;
	private ObjectOutputStream toPlayerTwo;
	private ObjectInputStream  fromPlayerOne;
	private ObjectInputStream  fromPlayerTwo;

	private Player playerOne = new Player();
	private Player playerTwo = new Player();

	private Point playerOneFlag;
	private Point playerTwoFlag;

	private PieceColor turn;
	private Move move;

	private Socket socketOne;
	private Socket socketTwo;

	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	/**
	 * Creates a new instance of ServerGameManager.
	 *
	 * @param sockOne socket connected to Player 1's client
	 * @param sockTwo socket connected to Player 2's client
	 * @param sessionNum the nth game session created by Server.
	 *
	 * @see edu.asu.stratego.Server
	 */
	public ServerGameManager(Socket sockOne, Socket sockTwo, int sessionNum) {
		this.session = "Session " + sessionNum + ": ";
		this.socketOne = sockOne;
		this.socketTwo = sockTwo;

		if (Math.random() < 0.5)
			this.turn = PieceColor.RED;
		else
			this.turn = PieceColor.BLUE;
	}

	/**
	 * See ClientGameManager's run() method to understand how the server
	 * interacts with the client.
	 *
	 * @see edu.asu.stratego.game.ClientGameManager
	 */
	@Override
	public void run() {
		createIOStreams();
		exchangePlayers();
		exchangeSetup();

		playGame();
	}

	/**
	 * Establish IO object streams to facilitate communication between the
	 * client and server.
	 */
	private void createIOStreams() {
		try {
			// NOTE: ObjectOutputStreams must be constructed before the
			//       ObjectInputStreams so as to prevent a remote deadlock.
			toPlayerOne   = new ObjectOutputStream(socketOne.getOutputStream());
			fromPlayerOne = new ObjectInputStream(socketOne.getInputStream());
			toPlayerTwo   = new ObjectOutputStream(socketTwo.getOutputStream());
			fromPlayerTwo = new ObjectInputStream(socketTwo.getInputStream());
		}
		catch(IOException e) {
			// TODO Handle this exception somehow...
			e.printStackTrace();
		}
	}

	/**
	 * Receive player information from the clients. Determines the players'
	 * colors, and sends the player information of the opponents back to the
	 * clients.
	 */
	private void exchangePlayers() {
		try {
			playerOne = (Player) fromPlayerOne.readObject();
			playerTwo = (Player) fromPlayerTwo.readObject();


			if (Math.random() < 0.5) {
				playerOne.setColor(PieceColor.RED);
				playerTwo.setColor(PieceColor.BLUE);
			}
			else {
				playerOne.setColor(PieceColor.BLUE);
				playerTwo.setColor(PieceColor.RED);
			}

			toPlayerOne.writeObject(playerTwo);
			toPlayerTwo.writeObject(playerOne);
		}
		catch(ClassNotFoundException | IOException e) {
			// TODO Handle this exception somehow...
			e.printStackTrace();
		}
	}

	private void exchangeSetup() {
		try {
			SetupBoard setupBoardOne = (SetupBoard) fromPlayerOne.readObject();
			SetupBoard setupBoardTwo = (SetupBoard) fromPlayerTwo.readObject();

			// Register pieces on the server board.
			for (int row = 0; row < 4; ++row) {
				for (int col = 0; col < 10; ++col) {
					board.getSquare(row, col).setPiece(setupBoardOne.getPiece(3 - row, 9 - col));
					board.getSquare(row + 6, col).setPiece(setupBoardTwo.getPiece(row, col));

					if(setupBoardOne.getPiece(3 - row, 9 - col).getPieceType() == PieceType.FLAG)
						playerOneFlag = new Point(row, col);
					if(setupBoardTwo.getPiece(row, col).getPieceType() == PieceType.FLAG)
						playerTwoFlag = new Point(row + 6, col);
				}
			}

			// Rotate pieces by 180 degrees.
			for (int row = 0; row < 2; ++row) {
				for (int col = 0; col < 10; ++col) {
					// Player One
					Piece temp = setupBoardOne.getPiece(row, col);
					setupBoardOne.setPiece(setupBoardOne.getPiece(3 - row, 9 - col), row, col);
					setupBoardOne.setPiece(temp, 3 - row, 9 - col);

					// Player Two
					temp = setupBoardTwo.getPiece(row, col);
					setupBoardTwo.setPiece(setupBoardTwo.getPiece(3 - row, 9 - col), row, col);
					setupBoardTwo.setPiece(temp, 3 - row, 9 - col);
				}
			}

			GameStatus winCondition = checkWinCondition(board);

			toPlayerOne.writeObject(setupBoardTwo);
			toPlayerTwo.writeObject(setupBoardOne);

			toPlayerOne.writeObject(winCondition);
			toPlayerTwo.writeObject(winCondition);
		}
		catch (ClassNotFoundException | IOException e) {
			// TODO Handle this exception somehow...
			e.printStackTrace();
		}

	}

	private void playGame() {
		while (true) {
			try {
				// Send player turn color to clients.
				toPlayerOne.writeObject(turn);
				toPlayerTwo.writeObject(turn);

				// Get turn from client.
				if (playerOne.getColor() == turn) {
					move = (Move) fromPlayerOne.readObject();
					move.setStart(9-move.getStart().x, 9-move.getStart().y);
					move.setEnd(9-move.getEnd().x, 9-move.getEnd().y);
				}
				else{
					move = (Move) fromPlayerTwo.readObject();
				}

				Move moveToPlayerOne = new Move(), moveToPlayerTwo = new Move();

				// Register move on the board.
				// If there is no piece at the end (normal move, no attack)
				if(board.getSquare(move.getEnd().x, move.getEnd().y).getPiece() == null) {
					Piece piece = board.getSquare(move.getStart().x, move.getStart().y).getPiece();

					board.getSquare(move.getStart().x, move.getStart().y).setPiece(null);
					board.getSquare(move.getEnd().x, move.getEnd().y).setPiece(piece);

					// Rotate the move 180 degrees before sending.
					moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
					moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
					moveToPlayerOne.setMoveColor(move.getMoveColor());
					moveToPlayerOne.setStartPiece(null);
					moveToPlayerOne.setEndPiece(piece);

					moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
					moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
					moveToPlayerTwo.setMoveColor(move.getMoveColor());
					moveToPlayerTwo.setStartPiece(null);
					moveToPlayerTwo.setEndPiece(piece);
				}
				else {
					Piece attackingPiece = board.getSquare(move.getStart().x, move.getStart().y).getPiece();
					Piece defendingPiece = board.getSquare(move.getEnd().x, move.getEnd().y).getPiece();

					BattleOutcome outcome = attackingPiece.getPieceType().attack(defendingPiece.getPieceType());

					moveToPlayerOne.setAttackMove(true);
					moveToPlayerTwo.setAttackMove(true);

					if(outcome == BattleOutcome.WIN) {
						board.getSquare(move.getEnd().x, move.getEnd().y).setPiece(board.getSquare(move.getStart().x, move.getStart().y).getPiece());
						board.getSquare(move.getStart().x, move.getStart().y).setPiece(null);

						// Rotate the move 180 degrees before sending.
						moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
						moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
						moveToPlayerOne.setMoveColor(move.getMoveColor());
						moveToPlayerOne.setStartPiece(null);
						moveToPlayerOne.setEndPiece(attackingPiece);
						moveToPlayerOne.setAttackWin(true);
						moveToPlayerOne.setDefendWin(false);

						moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
						moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
						moveToPlayerTwo.setMoveColor(move.getMoveColor());
						moveToPlayerTwo.setStartPiece(null);
						moveToPlayerTwo.setEndPiece(attackingPiece);
						moveToPlayerTwo.setAttackWin(true);
						moveToPlayerTwo.setDefendWin(false);
					}
					else if(outcome == BattleOutcome.LOSE) {
						board.getSquare(move.getStart().x, move.getStart().y).setPiece(null);

						// Rotate the move 180 degrees before sending.
						moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
						moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
						moveToPlayerOne.setMoveColor(move.getMoveColor());
						moveToPlayerOne.setStartPiece(null);
						moveToPlayerOne.setEndPiece(defendingPiece);
						moveToPlayerOne.setAttackWin(false);
						moveToPlayerOne.setDefendWin(true);

						moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
						moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
						moveToPlayerTwo.setMoveColor(move.getMoveColor());
						moveToPlayerTwo.setStartPiece(null);
						moveToPlayerTwo.setEndPiece(defendingPiece);
						moveToPlayerTwo.setAttackWin(false);
						moveToPlayerTwo.setDefendWin(true);
					}
					else if(outcome == BattleOutcome.DRAW) {
						board.getSquare(move.getStart().x, move.getStart().y).setPiece(null);
						board.getSquare(move.getEnd().x, move.getEnd().y).setPiece(null);

						// Rotate the move 180 degrees before sending.
						moveToPlayerOne.setStart(new Point(9 - move.getStart().x, 9 - move.getStart().y));
						moveToPlayerOne.setEnd(new Point(9 - move.getEnd().x, 9 - move.getEnd().y));
						moveToPlayerOne.setMoveColor(move.getMoveColor());
						moveToPlayerOne.setStartPiece(null);
						moveToPlayerOne.setEndPiece(null);
						moveToPlayerOne.setAttackWin(false);
						moveToPlayerOne.setDefendWin(false);

						moveToPlayerTwo.setStart(new Point(move.getStart().x, move.getStart().y));
						moveToPlayerTwo.setEnd(new Point(move.getEnd().x, move.getEnd().y));
						moveToPlayerTwo.setMoveColor(move.getMoveColor());
						moveToPlayerTwo.setStartPiece(null);
						moveToPlayerTwo.setEndPiece(null);
						moveToPlayerTwo.setAttackWin(false);
						moveToPlayerTwo.setDefendWin(false);
					}
				}

				GameStatus winCondition = checkWinCondition(board);

				toPlayerOne.writeObject(moveToPlayerOne);
				toPlayerTwo.writeObject(moveToPlayerTwo);

				toPlayerOne.writeObject(winCondition);
				toPlayerTwo.writeObject(winCondition);


				// Change turn color.
				if (turn == PieceColor.RED)
					turn = PieceColor.BLUE;
				else
					turn = PieceColor.RED;

				// Check win conditions.
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println(session + "Error occured during network I/O");
				return;
			}
		}
	}

	private GameStatus checkWinCondition(ServerBoard board) {
		 if(!hasAvailableMoves(PieceColor.RED, board)) {
		 	if (!hasAvailableMoves(PieceColor.BLUE, board)) {
		 		return GameStatus.TIE_GAME;
		 	}
		 	return GameStatus.RED_NO_MOVES;
		 }

		 if(!hasAvailableMoves(PieceColor.BLUE, board)) {
		 	return GameStatus.BLUE_NO_MOVES;
		 }

		 if(isCaptured(PieceColor.RED, board)) {
		 	return GameStatus.RED_CAPTURED;
		 }

		 if(isCaptured(PieceColor.BLUE, board)) {
		 	return GameStatus.BLUE_CAPTURED;
		 }

		return GameStatus.IN_PROGRESS;
	}

	private boolean isCaptured(PieceColor inColor, ServerBoard board) {
		if(playerOne.getColor() == inColor) {
			if(board.getSquare(playerOneFlag.x, playerOneFlag.y).getPiece().getPieceType() != PieceType.FLAG)
				return true;
		}
		if(playerTwo.getColor() == inColor) {
			if(board.getSquare(playerTwoFlag.x, playerTwoFlag.y).getPiece().getPieceType() != PieceType.FLAG)
				return true;
		}
		return false;
	}

	private boolean hasAvailableMoves(PieceColor inColor, ServerBoard board) {
		for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				Piece current = board.getSquare(row, col).getPiece();
				if(current != null && current.getPieceColor() == inColor) {
					if(computeValidMoves(row, col, current, board)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean checkRowForMoves(int row, int col, Piece piece, ServerBoard board, Direction dir) {
		int max = 1;
		PieceColor inColor = piece.getPieceColor();
		if (piece.getPieceType() == PieceType.SCOUT) {
			max = 8;
		}
		int modifier = 1, colMod = 0, rowMod = 0;
		switch (dir) {
			case UP:
				modifier = -1;
				rowMod = modifier;
				break;
			case DOWN:
				modifier = 1;
				rowMod = modifier;
				break;
			case LEFT:
				modifier = -1;
				colMod = modifier;
				break;
			case RIGHT:
				modifier = 1;
				colMod = modifier;
				break;
			default:
				break;
		}
		//Negative Row (UP)
		for(int i = 0; i < max; i++) {
			if (rowMod != 0) {
				row = row + rowMod;
				rowMod += modifier;
			}
			if (colMod != 0) {
				col = col + colMod;
				colMod += modifier;
			}
			if (isInBounds(row, col) && (!isLake(row, col) || (!isNullPiece(row, col, board) && !isOpponentPiece(row, col, inColor, board)))) {
				if (isNullPiece(row, col, board) || isOpponentPiece(row, col, inColor, board)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean computeValidMoves(int row, int col, Piece piece, ServerBoard board) {
		int max = 1;
		PieceType pieceType = piece.getPieceType();
		PieceColor inColor = piece.getPieceColor();
		if(pieceType == PieceType.SCOUT)
			max = 8;

		ArrayList<Point> validMoves = new ArrayList<Point>();
//
		if(pieceType != PieceType.BOMB && pieceType != PieceType.FLAG) {
			// Negative Row (UP)
			if (checkRowForMoves(row, col, piece, board, Direction.UP)) {
				return true;
			}
			if (checkRowForMoves(row, col, piece, board, Direction.DOWN)) {
				return true;
			}
			if (checkRowForMoves(row, col, piece, board, Direction.LEFT)) {
				return true;
			}
			if (checkRowForMoves(row, col, piece, board, Direction.RIGHT)) {
				return true;
			}
			for(int i = -1; i >= -max; i--) {
				if(isInBounds(row+i,col) && (!isLake(row+i, col) || (!isNullPiece(row+i, col, board) && !isOpponentPiece(row+i, col, inColor, board)))) {
					if(isNullPiece(row+i, col, board) || isOpponentPiece(row+i, col, inColor, board)) {
						validMoves.add(new Point(row+i, col));

						if(!isNullPiece(row+i, col, board) && isOpponentPiece(row+i, col, inColor, board))
							break;
					}
					else
						break;
				}
				else
					break;
			}
			// Positive Col (RIGHT)
			for(int i = 1; i <= max; ++i) {
				if(isInBounds(row,col+i) && (!isLake(row, col+i) || (!isNullPiece(row, col+i, board) && !isOpponentPiece(row, col+i, inColor, board)))) {
					if(isNullPiece(row, col+i, board) || isOpponentPiece(row, col+i, inColor, board)) {
						validMoves.add(new Point(row, col+i));

						if(!isNullPiece(row, col+i, board) && isOpponentPiece(row, col+i, inColor, board))
							break;
					}
					else
						break;
				}
				else
					break;
			}
			// Positive Row (DOWN)
			for(int i = 1; i <= max; ++i) {
				if(isInBounds(row+i,col) && (!isLake(row+i, col) || (!isNullPiece(row+i, col, board) && !isOpponentPiece(row+i, col, inColor, board)))) {
					if(isNullPiece(row+i, col, board) || isOpponentPiece(row+i, col, inColor, board)) {
						validMoves.add(new Point(row+i, col));

						if(!isNullPiece(row+i, col, board) && isOpponentPiece(row+i, col, inColor, board))
							break;
					}
					else
						break;
				}
				else
					break;
			}
			// Negative Col (LEFT)
			for(int i = -1; i >= -max; --i) {
				if(isInBounds(row,col+i) && (!isLake(row, col+i) || (!isNullPiece(row, col+i, board) && !isOpponentPiece(row, col+i, inColor, board)))) {
					if(isNullPiece(row, col+i, board) || isOpponentPiece(row, col+i, inColor, board)) {
						validMoves.add(new Point(row, col+i));

						if(!isNullPiece(row, col+i, board) && isOpponentPiece(row, col+i, inColor, board))
							break;
					}
					else
						break;
				}
				else
					break;
			}
		}

		return false;
	}

	private static boolean isLake(int row, int col) {
		if (col == 2 || col == 3 || col == 6 || col == 7) {
			if (row == 4 || row == 5)
				return true;
		}
		return false;
	}

	private static boolean isInBounds(int row, int col) {
		if(row < 0 || row > 9)
			return false;
		if(col < 0 || col > 9)
			return false;

		return true;
	}
	private boolean isOpponentPiece(int row, int col, PieceColor inColor, ServerBoard board) {
		return board.getSquare(row, col).getPiece().getPieceColor() != inColor;
	}
	private boolean isNullPiece(int row, int col, ServerBoard board) {
		return board.getSquare(row, col).getPiece() == null;
	}
}
