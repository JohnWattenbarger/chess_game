package chess_game;

import javax.swing.JFrame;

/*
Created: 1/27/18
Current Date: 3/1/18

Current Goals:
    - Speed up game
        - search through Piece ArrayLists to find pieces (instead of the whole 
            board)
        - when testing for checkmate, only see if there are legal moves for the 
            other player that: move the king, capture the checking piece, block
            the path b/w the checking piece and the king
    - On pawn promotion moves, create a GUI popup with the pieces that the user 
        can choose
        > need to pass 
    - Make "!!! ILLEGAL MOVE !!!" stop printing on the console
    - Add stale mate
        > create Board method isStalemate()
        > possibly do isCheck(), isStalemate(), isCheckmate() in the same 
            function to eliminate getting all legal moves multiple times
        > another solution would be to keep track of legalMoves as an attribute 
            of Board, and set it to null after each turn
    - Add 3 repitition draws
    - Add 50 move draw
    - Make non-piece squares unselectable

Next Edit:
    - Add in stale mate
        > need to check all legal moves (and see if that is null)
        > should probably make getLegalMoves() store in an arrayList on the Board.
            -> do something like: getLegalMoves(){if(legalMoves.isEmpty())legalMoves=calculateLegalMoves(); return legalMoves();
        > if not in check, and legalMoves.isEmpty() then it's stalemate.
        > need to make sure that legalMoves() is set to null after each move
    - Add in 3 repition draws
    - Add in 50 move draw
    - Make the game run faster
        > when testing for check, only test diagonal, horizontal/vertical, and 
            knight moves for pieces that can caputure the king
        > keep a list of all pieces still in play for faster move testing
    - Improve the GUI
        > add something that displays if the user is in check, and if an illegal
            move was attempted
            -> I could also make only legal move locations clickable
            -> When a piece is selected, displaying legal moves in some way
                (changing the background color would be easiest, although 
                adding a dot in legal move locations might look better)
        > center the main menu buttons
        > center the pieces (might require editing their pictures)
    - take out Board.printAllLegalMoves() when a player is in check
    - Clean up the ChessFrame.java code
        > create new methods to be used in setupNewGame
        > change the chess board to a gridLayout
        > change the message, board, and statusMessage to a groupLayout

Future Features:
    - Create a CPU opponent
        > initially just move any piece to a random legal location
        > eventually, add a set of parameters to judge how each player is doing
        > the CPU would then use a decision tree to choose the best move
    - Cleanup chess game (make it faster)
        > make legal move testing only test possible locations (i.e. diagonal 
            moves for bishops)
    - make 2 modes (text and GUI)
*/

/**
 * Creates a default chess game (currently in text form). Allows the user to 
 * move pieces around. Currently checks for basic legal moves, but not special 
 * moves or checkmate.
 * 
 * @author John
 */
class Chess_game {
    public static void main(String[] args) 
    {
//         textGame();
        graphicsGame();
    }
    
    /**
     * Creates a text version of chess.
     */
    public static void textGame(){
        Board b = new Board();
        b.setup();
        
        // prompt the user for moves until checkmate is achieved
        while(!b.isCheckmate() && !b.isStalemate())
        {
            b.printBoard();
            b.displayTurn();
//            
//            // test
//            if(b.enPassantLocation == null)
//                System.out.println("enPassantLocation: null");
//            else
//            {
//                System.out.print("enPassantLocation: ");
//                b.enPassantLocation.printChessNotation();
//            }
            
            if(b.isCheck())
                System.out.println("=========== CHECK =============");
            if(b.isCheckmate())
                System.out.println("~~~~~~~~~~~ Checkmate ~~~~~~~~~~~~");
            b.userMove();
            b.setNextTurn();
        }
        
        b.printBoard();
        System.out.println("Game over");
        if(!b.isStalemate())
            b.printWinner();
        
        if(b.isStalemate())
            System.out.println("Stalemate");
    }
    
    public static void graphicsGame(){
        ChessFrame c = new ChessFrame();
        c.run();
    }
    
}
