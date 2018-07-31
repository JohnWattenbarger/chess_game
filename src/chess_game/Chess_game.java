package chess_game;

import javax.swing.JFrame;

/*
Created: 1/27/18
Current Date: 3/1/18

1st Edit:
    - Went through Board and Piece making code more readable
    - Created a Location object (contains the column and row of a square on the
      board
    - Began Creating classes to get locations that a user wishes to move a piece
      from and to

2nd Edit:
    - Completed all of the move() functions in board
    - Changed Location constructor to only take a char and int in standard chess
      notation form (ex: 'a3')
    - Added documentation to all of my code

3rd Edit:
    - Created a Move object (contains the (moveFrom location, piece), (the 
      moveTo location, piece), and the board
        > used to check if a move is legal
    - Made checking if a move is legal part of the Move object
    - Got legal move working for Pawn_White

4th Edit:
    - Created a board method called getLegalMoveToLocations()
        > This is an array of all possible moves that a piece/location can make
    - Created a Location method called printChessNotation()
        > This outputs the location on the board in chess notation form (ex: 
          'a3')
    - Finished all the basic isLegalMove() checks

5th Edit:
    - Created a board method to get all legal moves
    - Began working on check() method
        > doesn't work right now
        > created other methods for this that might need to be checked

6th Edit:
    - changed isCheck() method
        > now tests if the other player could possibly move to a location
    - added isCheckMate() method

7th Edit:
    - Began working on castling
        > Still need to check if the king is in check or passes through check
    - Fixed a bug in my legal move checking
        > I was doing it through my board class (board.isLegalMove())
        > Now I'm doing it through my move class (move.isLegal())

    - *** Some moves cause a heap space error?
        > ex: e2-e3, f7-f5, g1-h3, e7-e6, f1-b5, g8-h6

8th Edit:
    - Added move.isInCheckAfter() method to see if a player would be in check 
        after a move
    - Separated legal move testing into legal moves, and basic legal moves (not 
        testing if a player is in check after or not).
        > further separated legal move testing so move.isLegalBasic() doesn't 
            include castling anymore.
    - Fixed board.isCheckmate (wasn't working correctly)
    - Finished allowing castling
        > it now test if the king is in check or passes through check
    - Added en passant
    - Added pawn promotion

9th Edit:
    - Began working on my chess frame
    - added blank image for blank pieces
    - Fixed addActionListener() for pieceButtons[]
    - Consider rewriting panel2
        > give it a grid layout
    - Created chessFrame.run() and chessFrame.synchBoard()
        > need to make b.move() come from button clicks, not command typing
    - Add en passant rules
        > The board() should have a Location called enPassantLocation
        > When a move() is a pawn move: 
            -> if it is a double jump up/down, set the enPassantLocation to the
                square in between the original and new location.
            -> if it is a capture move, check if the new location is the 
                enPassantLocation (and that the pieces are different colors). if
                so, move to it.
        > The move() should also keep track of the location where a piece should
            be deleted after an enPassante capture.
        > The board() should reset it's enPassantLocation after each move
            -> set enPassantLocation to whatever move.enPassantLocation is 
                (either null or a location)

Last Edit:
    - Added status update to the GUI
        > created a JLabel that contains status updates
        > changed System.out.println() messages in Board.java to a String
        > passed the String to ChessFrame.java
            -> if there is a new String, display it
    - Make the GUI display clicks
        > when a piece is selected, change the color (or something)
        > when a piece is unselected, change the color back

Current Goals:

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
        > show which piece is currently selected (easiest way would probably be 
            changing the background color, although making the piece image 
            bigger might look better)
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
//        Board b = new Board();
//        b.defaultBoard();
//        
//        // prompt the user for moves until checkmate is achieved
//        while(!b.isCheckmate())
//        {
//            b.printBoard();
//            b.displayTurn();
////            
////            // test
////            if(b.enPassantLocation == null)
////                System.out.println("enPassantLocation: null");
////            else
////            {
////                System.out.print("enPassantLocation: ");
////                b.enPassantLocation.printChessNotation();
////            }
//            
//            if(b.isCheck())
//                System.out.println("=========== CHECK =============");
//            if(b.isCheckmate())
//                System.out.println("~~~~~~~~~~~ Checkmate ~~~~~~~~~~~~");
//            b.userMove();
//            b.setNextTurn();
//        }
//        
//        b.printBoard();
//        System.out.println("Game over");
//        b.printWinner();
        
        ChessFrame c = new ChessFrame();
        c.run();
    }
    
}
