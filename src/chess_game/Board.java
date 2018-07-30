package chess_game;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
/** An array of Piece objects.
 * 
 * @author John
 */
public class Board 
{
    public int turn;
    Piece[][] board;
    BufferedImage picture;
    public String turnColor;
    int boardSize = 8;
    public String winner;
    Location enPassantLocation = null;
    public String errorMessage;
    
    public Board()
    {
        board = new Piece [8][8];
        defaultBoard();
        setImage();
        winner = "";
        errorMessage = "";
    }
    
    /**
     * Sets each Piece in the board to blank (' ', ' ').
     */
    public void blankBoard()
    {
        // set all pieces to blank
        for(int i=0; i<8; i++)
            for(int j=0; j<8; j++)
                board[i][j] = new Piece(' ', ' '); 
    }
    
    /**
     * Creates a starting chess board.
     */
    public void defaultBoard()
    {
        blankBoard();
        setupPieces();
        this.turnColor = "white";
    }
    
    /**
     * Sets this board to be the same as another board.
     * 
     * @param otherBoard the board you want to copy
     */
    public void setBoard(Board otherBoard)
    {
        this.turn = otherBoard.turn;
        this.turnColor = otherBoard.turnColor;
        this.enPassantLocation = otherBoard.enPassantLocation;
        
        for(int i=0; i<this.boardSize; i++)
            for(int j=0; j<this.boardSize; j++)
                this.board[i][j].setPiece(otherBoard.board[i][j]);
    }
    
    /**
     * Changes the pieces on the board from blank to the correct starting piece.
     */
    public void setupPieces()
    {
        setupPawns();
        setupRooks();
        setupKnights();
        setupBishops();
        setupQueens();
        setupKings();
    }
    
    /**
     * Adds pawns to the the chess board.
     */
    private void setupPawns()
    {
        for (int i=0; i<8; i++)
        {
            board[1][i] = new Piece('b', 'p');
            board[6][i] = new Piece('w', 'p');
        }
    }
    
    /**
     * Adds rooks to the chess board.
     */
    private void setupRooks()
    {
        board[0][0] = new Piece('b', 'r');
        board[0][7] = new Piece('b', 'r');
        board[7][0] = new Piece('w', 'r');
        board[7][7] = new Piece('w', 'r');
    }
    
    /**
     * Adds Knights to the chess board.
     */
    private void setupKnights()
    {
        board[0][1] = new Piece('b', 'n');
        board[0][6] = new Piece('b', 'n');
        board[7][1] = new Piece('w', 'n');
        board[7][6] = new Piece('w', 'n');
    }
    
    /**
     * Adds bishops to the chess board.
     */
    private void setupBishops()
    {
        board[0][2] = new Piece('b', 'b');
        board[0][5] = new Piece('b', 'b');
        board[7][2] = new Piece('w', 'b');
        board[7][5] = new Piece('w', 'b');
    }
    
    /**
     * Adds queens to the chess board.
     */
    private void setupQueens()
    {
        board[0][3] = new Piece('b', 'q');
        board[7][3] = new Piece('w', 'q');
    }
    
    /**
     * Adds kings to the chess board.
     */
    private void setupKings()
    {
        board[0][4] = new Piece('b', 'k');
        board[7][4] = new Piece('w', 'k');
    }
    
    /**
     * Prints the board in text format to the console.
     */
    public void printBoard()
    {
        int row = 8;
        char column = 'a';
        
        System.out.println("  _________________");
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                if(j==0)
                {
                    System.out.print(Integer.toString(row) + " ");
                    row--;
                }
                System.out.print("|");
                board[i][j].printPiece();
            }
            System.out.println("|");
            System.out.println("  _________________");
        }
        System.out.print("  ");
        while(column <= 'h')
        {
            System.out.print(" " + column);
            column++;
        }
        System.out.println();
    }
    
    /**
     * Displays whose turn it is to the console.
     */
    public void displayTurn()
    {
        if (turn%2 == 0)
            System.out.println("White's Turn");
        else
            System.out.println("Black's Turn");
    }
    
    /**
     * Increments the turn variable. Switches between even numbers (white) and 
     * odd numbers (black).
     */
    public void setNextTurn()
    {
        turn++;
        if (this.turnColor.equals("white"))
            this.turnColor = "black";
        else
            this.turnColor = "white";
    }
    
    /**
     * Decrements the turn variable. Switches between even numbers (white) and 
     * odd numbers (black).
     */
    public void setPreviousTurn()
    {
        turn--;
        if (this.turnColor.equals("white"))
            this.turnColor = "black";
        else
            this.turnColor = "white";
    }
    
    /**
     * Prompts the user for the location of a piece to move, and a location to 
     * move to. If the move is legal, the piece is moved to the new location.
     * If the move is illegal, this method is called again.
     */
    public void userMove()
    {
        String moveFrom;
        String moveTo;
        Location originalLocation;
        Location newLocation;
        Move attemptedMove;
        // Test?
        Location[] legalMovesArray = new Location[64];
        
        moveFrom = this.getUserMoveFrom();
        originalLocation = convertStringToLocation(moveFrom);
        
        // Test
        // check all legal moves
        legalMovesArray = getLegalMoveToLocations(originalLocation);
        System.out.println("----All Legal Moves");
        for(int i=0; i<legalMovesArray.length; i++)
            if(legalMovesArray[i] != null)
                legalMovesArray[i].printChessNotation();
        
        
        moveTo = this.getUserMoveTo();
        newLocation = convertStringToLocation(moveTo);
        
        attemptedMove = new Move(originalLocation, newLocation);
        attemptedMove.setup(this);
        
        if(attemptedMove.isLegal())
        {
            // test (never occurs)
            if(attemptedMove.isInCheckAfter()){
                errorMessage = "Still in check";
                System.out.println("Still in check");
            }
            
            // if this is a castle move, move the rook
            if(attemptedMove.isCastleMove)
                move(attemptedMove.rookStart, attemptedMove.rookEnd);
            move(attemptedMove);
            
            // if this is a castle move, delete/capture the other player's pawn
            if(attemptedMove.isEnPassantMove)
                this.board[attemptedMove.enPassantDeleteLocation.row][attemptedMove.enPassantDeleteLocation.column] = new Piece();
            
            // check if a pawn made it to row 0 or 7 and promote it
            if(attemptedMove.originalPieceType.equals("pawn"))
                if(attemptedMove.newLocation.row == 0 || attemptedMove.newLocation.row == 7)
                    pawnPromotion(newLocation);
                    // System.out.println("~~~~ Pawn Promotion ~~~~");
            
            this.enPassantLocation = attemptedMove.enPassantLocation;
        }
        else
        {
            errorMessage = "ILLEGAL MOVE";
            System.out.println("!!! ILLEGAL MOVE !!!");
            userMove();
        }
    }
    
    /**
     * Prompts the user for the location of a piece to move, and a location to 
     * move to. If the move is legal, the piece is moved to the new location.
     * If the move is illegal, this method is called again.
     */
    public void userMove(Location moveFrom, Location moveTo)
    {
        Move attemptedMove;
        
        attemptedMove = new Move(moveFrom, moveTo);
        attemptedMove.setup(this);
        
        if(attemptedMove.isLegal())
        {
            // if this is a castle move, move the rook
            if(attemptedMove.isCastleMove)
                move(attemptedMove.rookStart, attemptedMove.rookEnd);
            
            move(attemptedMove);
            
            // if this is a castle move, delete/capture the other player's pawn
            if(attemptedMove.isEnPassantMove)
                this.board[attemptedMove.enPassantDeleteLocation.row][attemptedMove.enPassantDeleteLocation.column] = new Piece();
            
            // check if a pawn made it to row 0 or 7 and promote it
            if(attemptedMove.originalPieceType.equals("pawn"))
                if(attemptedMove.newLocation.row == 0 || attemptedMove.newLocation.row == 7)
                    pawnPromotion(moveTo);
                    // System.out.println("~~~~ Pawn Promotion ~~~~");
            
            this.enPassantLocation = attemptedMove.enPassantLocation;
            
            this.setNextTurn();
        }
        else
        {
            errorMessage = "ILLEGAL MOVE";
            System.out.println("!!! ILLEGAL MOVE !!!");
        }
    }
            
    /**
     * Prompts the user for a location to move a piece to. Returns a String 
     * of the location that the user wants to move a piece to. The user should
     * input a location in the form "a3" where the 1st digit is a character 
     * ranging from 'a' to 'h', and the 2nd digit is an integer ranging from 
     * 1 to 8.
     * 
     * @return The location the user wants to move to as a String
     */ 
    private String getUserMoveFrom()
    {
        String moveFrom;
        Scanner cin = new Scanner(System.in);
        // prompt the user for where to move from
        System.out.print("Select a piece to move (ex: 'a3'): ");
        moveFrom = cin.nextLine();
        
        return moveFrom;
    }
    
    /**
     * Prompts the user for a location to move a piece from. Returns a String 
     * of the location that the user wants to move a piece from. The user should
     * input a location in the form "a3" where the 1st digit is a character 
     * ranging from 'a' to 'h', and the 2nd digit is an integer ranging from 
     * 1 to 8.
     * 
     * @return The location the user wants to move from as a String
     */
    private String getUserMoveTo()
    {
        String moveTo;
        Scanner cin = new Scanner(System.in);
        
        System.out.print("Select a square to move to (ex: 'a3'): ");
        moveTo = cin.nextLine();
        
        return moveTo;
    }
    
    /**
     * Returns a Location object of a piece on the chess board. The String 
     * input should be in the form "a3" where the 1st digit is a 
     * character ranging from 'a' to 'h', and the 2nd digit is an integer 
     * ranging from 1 to 8.
     * 
     * @param str a location of a piece as a char and int.
     * @return Returns the location on the chess board.
     */
    private Location convertStringToLocation(String str)
    {
        int row = 0;
        char firstCharacter;
        char secondCharacter;
        Location pieceLocation;
        
        // Location should be entered in the form of: "a5"
        firstCharacter = str.charAt(0);
        secondCharacter = str.charAt(1);
        
        // convert from char to int
        row = Character.getNumericValue(secondCharacter);
        
        pieceLocation = new Location(firstCharacter, row);
        
        return pieceLocation;
    }
    
    /**
     * <p>Move a piece from one location to a new location. The original location 
     * is set to a blank piece.<p>
     * <p>Side effect: the piece that moved has it's hasMoved boolean set to 
     * true. </p>
     * 
     * @param originalLocation The location of the piece you want to move
     * @param newLocation The location the piece will move to
     */
    public void move(Location originalLocation, Location newLocation)
    {
        // move the piece
        board[newLocation.row][newLocation.column] = 
                board[originalLocation.row][originalLocation.column];
        // make the original location blank
        board[originalLocation.row][originalLocation.column] = new Piece();
        this.pieceAt(newLocation).hasMoved = true;
    }
    
    /**
     * Move a piece from one location to a new location. The original location 
     * is set to a blank piece.
     * 
     * @param move The move you wish to make
     */
    public void move(Move move)
    {
        move(move.originalLocation, move.newLocation);
    }
    
    /**
     * Checks if a move from originalLocation to newLocation is a legal chess 
     * move. If the move is legal return true, if the move is illegal return 
     * false.
     * 
     * @param originalLocation Location to move from
     * @param newLocation Location to move to
     * @return True if the move is legal, false if the move is illegal
     */
    public boolean isLegalMove(Location originalLocation, Location newLocation)
    {
        // Setup the move object
        Move move = new Move(originalLocation, newLocation);
        move.setOriginalPiece(this.pieceAt(originalLocation));
        move.setNewPiece(this.pieceAt(newLocation));
        move.setBoard(this);
        
        return move.isLegal();
    }
    
    public boolean isLegalMove(Move move)
    {
        return isLegalMove(move.originalLocation, move.newLocation);
    }
    
    public boolean isLegalMoveBasic(Location originalLocation, Location newLocation)
    {
        // Setup the move object
        Move move = new Move(originalLocation, newLocation);
        move.setOriginalPiece(this.pieceAt(originalLocation));
        move.setNewPiece(this.pieceAt(newLocation));
        move.setBoard(this);
        
        return move.isLegalBasic();
    }
    
    public boolean isLegalMoveBasic(Move move)
    {
        return isLegalMoveBasic(move.originalLocation, move.newLocation);
    }
    
    /**
     * Returns a set with all legal move-to locations.
     * Note: this function is incomplete. It correctly detects legal moves, but
     * for some reason, it stores the new location as Location(7, 7)
     * 
     * @param originalLocation The piece you want to move
     * @return A set of legal-move locations
     */
    public Location[] getLegalMoveToLocations(Location originalLocation)
    {
        int row;
        int column;
        int i=0;
        Location newLocation = new Location();
        Location[] legalMoves = new Location[64];
//        Move tempMove =  new Move();
//        
//        // setup the board and original location of the move
//        tempMove.setBoard(this);
//        tempMove.originalLocation = originalLocation;
//        tempMove.setOriginalPiece(this.pieceAt(originalLocation));
        
        for(int j=0; j<legalMoves.length; j++)
        {
            legalMoves[j] = null;
        }
        
        for(row=0; row<8; row++)
        {
            for(column=0; column<8; column++)
            {
                newLocation = new Location(row, column);
                if(isLegalMove(originalLocation, newLocation))
                {
                    legalMoves[i] = newLocation;
                    i++;
                }
            }
        }
        
        return legalMoves;
    }
    
    public Location[] getLegalMoveToLocationsBasic(Location originalLocation)
    {
        int row;
        int column;
        int i=0;
        Location newLocation = new Location();
        Location[] legalMoves = new Location[64];
        
        for(int j=0; j<legalMoves.length; j++)
        {
            legalMoves[j] = null;
        }
        
        for(row=0; row<8; row++)
        {
            for(column=0; column<8; column++)
            {
                newLocation = new Location(row, column);
                if(isLegalMoveBasic(originalLocation, newLocation))
                {
                    legalMoves[i] = newLocation;
                    i++;
                }
            }
        }
        
        return legalMoves;
    }
    
    /**
     * Gives the piece on the board at the given location.
     * 
     * @param location Location to get the piece type from
     * @return The piece at the specified location
     */
    public Piece pieceAt(Location location)
    {
        return board[location.row][location.column];
    }
    
    /**
     * Gets all locations of pieces for whichever turn it is. Then for each 
     * location, add any legal moves to an ArrayList. return the ArrayList.
     * 
     * @return an ArrayList with all legal moves
     */
    public ArrayList<Move> getLegalMoves()
    {
        ArrayList<Location> thisTurnsPieceLocations = new ArrayList<Location>();
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        Location[] currentLocationLegalMoves = new Location[64];
        
        if(turnColor.equals("white"))
            thisTurnsPieceLocations = getWhitePieceLocations();
        if(turnColor.equals("black"))
            thisTurnsPieceLocations = getBlackPieceLocations();
        
        // for each piece, get legal moves
        for(int i = 0; i < thisTurnsPieceLocations.size(); i++)
        {
            currentLocationLegalMoves = getLegalMoveToLocations(thisTurnsPieceLocations.get(i));
            int j = 0;
            while(currentLocationLegalMoves[j] != null)
            {
                allLegalMoves.add(new Move(thisTurnsPieceLocations.get(i), currentLocationLegalMoves[j]));
                j++;
            }
        }
        
        return allLegalMoves;
    }
    
    public ArrayList<Move> getLegalMovesBasic()
    {
        ArrayList<Location> thisTurnsPieceLocations = new ArrayList<Location>();
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        Location[] currentLocationLegalMoves = new Location[64];
        
        if(turnColor.equals("white"))
            thisTurnsPieceLocations = getWhitePieceLocations();
        if(turnColor.equals("black"))
            thisTurnsPieceLocations = getBlackPieceLocations();
        
        // for each piece, get legal moves
        for(int i = 0; i < thisTurnsPieceLocations.size(); i++)
        {
            currentLocationLegalMoves = getLegalMoveToLocationsBasic(thisTurnsPieceLocations.get(i));
            int j = 0;
            while(currentLocationLegalMoves[j] != null)
            {
                allLegalMoves.add(new Move(thisTurnsPieceLocations.get(i), currentLocationLegalMoves[j]));
                j++;
            }
        }
        
        return allLegalMoves;
    }
    
    /**
     * Uses Board.getLegalMoves() and Move.print() to display all legal moves.
     */
    public void printAllLegalMoves()
    {
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        allLegalMoves = getLegalMoves();
        
        for(int i=0; i<allLegalMoves.size(); i++)
            allLegalMoves.get(i).print();
    }
    
    public void printAllLegalMovesBasic()
    {
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        allLegalMoves = getLegalMovesBasic();
        
        for(int i=0; i<allLegalMoves.size(); i++)
            allLegalMoves.get(i).print();
    }
    
    /**
     * Gives an ArrayList with all locations that have a white piece on them.
     * 
     * @return An ArrayList with all white piece locations
     */
    public ArrayList<Location> getWhitePieceLocations()
    {
        ArrayList<Location> whitePieceLocations = new ArrayList<Location>();
        Location tempLocation;
        
        for(int row = 0; row < boardSize; row++)
            for(int column = 0; column < boardSize; column++)
            {
                tempLocation = new Location(row, column);
                if(pieceAt(tempLocation).color.equals("white"))
                    whitePieceLocations.add(tempLocation);
            }
        
        return whitePieceLocations;
    }
    
    /**
     * Gives an ArrayList with all locations that have a black piece on them.
     * 
     * @return An ArrayList with all black piece locations
     */
    public ArrayList<Location> getBlackPieceLocations()
    {
        ArrayList<Location> blackPieceLocations = new ArrayList<Location>();
        Location tempLocation;
        
        for(int row = 0; row < boardSize; row++)
            for(int column = 0; column < boardSize; column++)
            {
                tempLocation = new Location(row, column);
                if(pieceAt(tempLocation).color.equals("black"))
                    blackPieceLocations.add(tempLocation);
            }
        
        return blackPieceLocations;
    }
    
    /**
     * Returns the location of the current player's (white/black) king.
     * 
     * @return The location of the current player's king.
     */
    public Location currentPlayersKingLocation()
    {
        Location currentPlayersKing = new Location();
        Location tempLocation = new Location();
        
        for(int row = 0; row < boardSize; row++)
            for(int column = 0; column < boardSize; column++)
            {
                tempLocation = new Location(row, column);
                if(pieceAt(tempLocation).pieceType.equals("king") && 
                        pieceAt(tempLocation).color.equals(this.turnColor))
                    currentPlayersKing = tempLocation;
            }
        
        return currentPlayersKing;
    }
    
    /**
     * Determines if the current player is in check or not. This is determined 
     * by: creating a temporary board that copies this board; incrementing to 
     * the next turn; seeing if there are any legal moves that capture the king.
     * 
     * @return True if the current player is in check.
     */
    public boolean isCheck()
    {
        Board tempBoard = new Board();
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        Location currentKingLocation = new Location();
        
        currentKingLocation.setLocation(this.currentPlayersKingLocation());
        
        // set tempBoard to be a copy of this board
        tempBoard.setBoard(this);
        
        // get all legal moves for the other player
        tempBoard.setNextTurn();
        legalMoves = tempBoard.getLegalMovesBasic();
        
        // check if any move can capture the king
        for(int i=0; i<legalMoves.size(); i++)
//            
            if(legalMoves.get(i).newLocation.sameLocation(currentKingLocation))
            {
                return true;
            }
        
        return false;
    }
    
    /**
     * <p>Test if the current player has been checkmated. A player has been 
     * checkmated if there are no legal moves where the current player is not 
     * in check after. Note: also sets the winner to the other player.</p>
     * 
     * <p>Side effects: decreases the turn by 1. Sets the winner.</p>
     * 
     * @return true if the current player is in checkmate.
     */
    public boolean isCheckmate()
    {
        // if the current player is not in check, then it cannot be checkmate
        if(!isCheck())
            return false;
        
        Board tempBoard = new Board();
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        
        legalMoves = this.getLegalMoves();
        
        for(int i=0; i<legalMoves.size(); i++)
        {
            // test
            legalMoves.get(i).print();
            
            tempBoard.setBoard(this);
            tempBoard.move(legalMoves.get(i));
            if(!tempBoard.isCheck())
                return false;
        }
        this.setPreviousTurn();
        this.setWinner(this.turnColor);
        
        return true;
    }
    
    /**
     * Determines if the next/other player could move to a location (regardless 
     * of whether they would be in check or not). Tests all 
     * possible moves that the other player can make. If the ending location 
     * has the same row and column as the one you're testing for, returns true.
     * 
     * @return True if the other player could move to the location you give.
     */
    public boolean opponentCanMoveTo(Location location)
    {
        Board tempBoard = new Board();
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        
        // set tempBoard to be a copy of this board
        tempBoard.setBoard(this);
        
        // get all legal moves for the other player
        tempBoard.setNextTurn();
        legalMoves = tempBoard.getLegalMovesBasic();
        
        // check if any move can move to some location
        for(int i=0; i<legalMoves.size(); i++)
            if(legalMoves.get(i).newLocation.equals(location))
                return true;
        
        return false;
    }
    
    public void pawnPromotion(Location pawnLocation)
    {
        String newPieceType = "";
        Piece promotedPawn;
        
        newPieceType = getUserPawnPromotionType();
        
        switch(newPieceType)
        {
            case "q":
            case "Q": newPieceType = "queen"; break;
            case "r":
            case "R": newPieceType = "rook"; break;
            case "b":
            case "B": newPieceType = "bishop"; break;
            case "n":
            case "N": newPieceType = "knight"; break;
        }
        
        if(newPieceType.equals("queen") || newPieceType.equals("rook") || 
                newPieceType.equals("bishop") || newPieceType.equals("knight"))
        {
            promotedPawn = new Piece(this.pieceAt(pawnLocation).color, newPieceType);
            this.board[pawnLocation.row][pawnLocation.column] = promotedPawn;
        }
        
        else
        {
            errorMessage = "ILLEGAL MOVE";
            System.out.println("!!! ILLEGAL SELECTION !!!");
            pawnPromotion(pawnLocation);
        }
    }
    
    public String getUserPawnPromotionType()
    {
        String newPieceType = "";
        Scanner cin = new Scanner(System.in);
        
        errorMessage = "Select a new piece (q, r, b, n): ";
        System.out.print("Select a new piece (q, r, b, n): ");
        newPieceType = cin.nextLine();
        
        return newPieceType;
    }
    
    /**
     * Sets the winner.
     * 
     * @param winner the color that won.
     */
    public void setWinner(String winner)
    {
        this.winner = winner;
    }
    
    /**
     * Display the winner to the console. Ex: "black wins!"
     */
    public void printWinner()
    {
        System.out.println(this.winner + " wins!");
    }
    
    /**
     * Description: !!!Incomplete!!!
     *      Will determine who won (white, 
     *
     * @return 
     */
    public String displayWinner()
    {
        return "White";
    }
    
    private void setImage()
    {
        try
            {
                picture = ImageIO.read(new File("Images/chessboard.png"));
            }
            catch (Throwable e)
            {System.out.println(e.getMessage());}
    }
    
    public BufferedImage getImage()
    {
        return picture;
    }
    
}
