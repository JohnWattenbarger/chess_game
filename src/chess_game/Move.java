/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_game;

/**
 *
 * @author John
 */

public class Move 
{
    Location originalLocation;
    Location newLocation;
    public Piece originalPiece;
    public Piece newPiece;
    public String originalPieceType;
    public String newPieceType;
    public Board board;
    int rowChange;
    int columnChange;
    // only used for castling
    boolean isCastleMove = false;
    Location rookStart = new Location();
    Location rookEnd = new Location();
    // only used for en passant
    boolean isEnPassantMove = false;
    Location enPassantLocation = null;
    Location enPassantDeleteLocation = null;
    
    /**
     * Create a blank Move
     */
    public Move(){}
    
    /**
     * Create a Move from originalLocation to a newLocation. Note: a board can 
     * also be added using Move.setBoard().
     * 
     * @param originalLocation the starting location
     * @param newLocation the ending location
     */
    public Move(Location originalLocation, Location newLocation)
    {
        this.originalLocation = originalLocation;
        this.newLocation = newLocation;
        rowChange = newLocation.row - originalLocation.row;
        columnChange = newLocation.column - originalLocation.column;
    }
    
    /**
     * Set's this move's board. Then sets the original piece and new piece based
     * on the board.
     * 
     * @param board this move's board.
     */
    public void setup(Board board)
    {
        setBoard(board);
        setOriginalPiece(board.pieceAt(originalLocation));
        setNewPiece(board.pieceAt(newLocation));
    }
    
    /**
     * Sets the piece and piece type of the original location.
     * 
     * @param originalPiece thie move's original piece
     */
    public void setOriginalPiece(Piece originalPiece)
    {
        this.originalPiece = originalPiece;
        this.originalPieceType = originalPiece.getPieceType();
    }
    
    /**
     * Sets the piece and piece type of the new location.
     * 
     * @param newPiece this move's new piece
     */
    public void setNewPiece(Piece newPiece)
    {
        this.newPiece = newPiece;
        this.newPieceType = newPiece.getPieceType();
    }
    
    /**
     * Sets the board that this move takes place on.
     * 
     * @param board this move's board.
     */
    public void setBoard(Board board)
    {
        this.board = board;
    }
    
    /**
     * Sets this move to be exactly equal to another move. Copies all properties
     * of one move object to this one.
     * 
     * @param otherMove the move you wish to copy.
     */
    public void setMove(Move otherMove)
    {
        this.board = otherMove.board;
        this.columnChange = otherMove.columnChange;
        this.isCastleMove = otherMove.isCastleMove;
        this.newLocation = otherMove.newLocation;
        this.newPiece = otherMove.newPiece;
        this.newPieceType = otherMove.newPieceType;
        this.originalLocation = otherMove.originalLocation;
        this.originalPiece = otherMove.originalPiece;
        this.originalPieceType = otherMove.originalPieceType;
        this.rowChange = otherMove.rowChange;
    }
    
    /**
     * Will be used to combine isLegalBasic() with testing if someone is in check.
     * 
     * @return if a move is legal
     */
    public boolean isLegal()
    {
        boolean legalMove = true;
        
        if (!this.originalPiece.color.equals(this.board.turnColor))
            return legalMove = false;
        
        if (this.originalPiece.sameColorAs(this.newPiece))
            return legalMove = false;
        
        // Check different piece types
        if (this.originalPieceType.equals("pawn"))
            legalMove = isLegalPawnMove();
        if (this.originalPieceType.equals("knight"))
            legalMove = isLegalKnightMove();
        if (this.originalPieceType.equals("bishop"))
            legalMove = isLegalBishopMove();
        if (this.originalPieceType.equals("rook"))
            legalMove = isLegalRookMove();
        if (this.originalPieceType.equals("queen"))
            legalMove = isLegalQueenMove();
        if (this.originalPieceType.equals("king"))
            legalMove = isLegalKingMove();
        
        if(!legalMove)
            return false;
        
        if(isInCheckAfter())
            return false;
        
        return true;
    }
    
    /**
     * Checks if the move is legal based on whose turn it is, and where the 
     * piece moves to. Does not account for check.
     * 
     * @return true if this is a legal move
     */
    public boolean isLegalBasic()
    {
        boolean legalMove = true;
        
        if (!this.originalPiece.color.equals(this.board.turnColor))
            return legalMove = false;
        
        if (this.originalPiece.sameColorAs(this.newPiece))
            return legalMove = false;
        
        // Check different piece types
        if (this.originalPieceType.equals("pawn"))
            legalMove = isLegalPawnMove();
        if (this.originalPieceType.equals("knight"))
            legalMove = isLegalKnightMove();
        if (this.originalPieceType.equals("bishop"))
            legalMove = isLegalBishopMove();
        if (this.originalPieceType.equals("rook"))
            legalMove = isLegalRookMove();
        if (this.originalPieceType.equals("queen"))
            legalMove = isLegalQueenMove();
        if (this.originalPieceType.equals("king"))
            legalMove = isLegalKingMoveBasic();
        
        return legalMove;
    }
    
    /**
     * <p>Checks if the move is a legal pawn move. A legal pawn move can be either:
     * moving up/down 1 space to an empty square; moving up/down 2 spaces to an 
     * empty square (with an empty square in between) if this pawn hasn't moved 
     * yet; moving diagonally 1 square to capture an opponent's piece.</p>
     * 
     * <p>Black pawns can only move down (positive row change). White pawns can 
     * only move up (negative row change). </p>
     * 
     * @return if this is a legal pawn move
     */
    public boolean isLegalPawnMove()
    {
        boolean legalPawnMove = false;
        
        if (this.originalPiece.color.equals("white"))
        {
            switch (this.rowChange)
            {
                // the pawn moved up 1
                case -1:
                {
                    switch (this.columnChange)
                    {
                        // the pawn moved left or right 1
                        case 1: case -1:
                        {
                            if (this.originalPiece.oppositeColorAs(this.newPiece))
                                return true;
                            
                            // test
                            // Note: need to add something to delete the captured piece
                            if (this.board.enPassantLocation != null)
                                if (this.newLocation.sameLocation(this.board.enPassantLocation))
                                {
                                    this.isEnPassantMove = true;
                                    this.enPassantDeleteLocation = new Location(this.originalLocation.row, this.newLocation.column);
                                    return true;
                                }
                            break;
                        }
                        // the pawn didn't move left or right
                        case 0:
                        {
                            if (this.newPiece.isNull())
                                return true;
                        }
                    }
                    break;
                }
                // the pawn moved up 2
                case -2:
                {
                    // if a pawn moves up 2, it can't move left or right
                    if (this.columnChange != 0)
                        return false;
                    
                    // if the pawn is not in its starting spot
                    if (this.originalLocation.row != 6)
                        return false;
                    
                    if (this.newPiece.isNull())
                    {
                        if (this.noPieceBetweenLinear())
                        {
                            this.enPassantLocation = new Location(this.originalLocation.row - 1, this.originalLocation.column);
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        
        if (this.originalPiece.color.equals("black"))
        {
            switch (this.rowChange)
            {
                // the pawn moved down 1
                case 1:
                {
                    switch (this.columnChange)
                    {
                        // the pawn moved left or right 1
                        case 1: case -1:
                        {
                            if (this.originalPiece.oppositeColorAs(this.newPiece))
                                return true;
                            
                            // test
                            // Note: need to add something to delete the captured piece
                            if (this.board.enPassantLocation != null)
                                if (this.newLocation.sameLocation(this.board.enPassantLocation))
                                {
                                    this.isEnPassantMove = true;
                                    this.enPassantDeleteLocation = new Location(this.originalLocation.row, this.newLocation.column);
                                    return true;
                                }
                            break;
                        }
                        // the pawn didn't move left or right
                        case 0:
                        {
                            if (this.newPiece.isNull())
                                return true;
                        }
                    }
                    break;
                }
                // the pawn moved down 2
                case 2:
                {
                    // if a pawn moves down 2, it can't move left or right
                    if (this.columnChange != 0)
                        return false;
                    
                    // if the pawn is not in its starting location
                    if (this.originalLocation.row != 1)
                        return false;
                    
                    if (this.newPiece.isNull())
                    {
                        if (this.noPieceBetweenLinear())
                        {
                            this.enPassantLocation = new Location(this.originalLocation.row + 1, this.originalLocation.column);
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        
        return legalPawnMove;
    }
    
    /**
     * Check if the knight moves 2 squares in a direction, and 1 square in the 
     * other direction.
     * 
     * @return true if this is a legal knight move
     */
    public boolean isLegalKnightMove()
    {
        boolean legalKnightMove = false;
        
        if(this.columnChange == 2 || this.columnChange == -2)
            if(this.rowChange == 1 || this.rowChange == -1)
                return true;
        if(this.columnChange == 1 || this.columnChange == -1)
            if(this.rowChange == 2 || this.rowChange == -2)
                return true;
        
        return legalKnightMove;
    }
    
    /**
     * Checks if the bishop moves the same amount of squares up/down as 
     * right/left, and that there are no pieces diagonally between the starting
     * location and the new location. If these 2 conditions are met, return 
     * true.
     * 
     * @return true if this is a legal bishop move
     */
    public boolean isLegalBishopMove()
    {
        boolean legalBishopMove = false;
        
        if(isDiagonalMove() && noPieceBetweenDiagonal())
            return true;
        
        return legalBishopMove;
    }
    
    /**
     * Checks if the rook makes a linear move (only changes columns or rows, but
     * not both), and that there are no pieces between its starting location and
     * ending location.
     * 
     * @return true if this is a legal rook move
     */
    public boolean isLegalRookMove()
    {
        boolean legalRookMove = false;
        
        if(isLinearMove() && noPieceBetweenLinear())
            return true;
        
        return legalRookMove;
    }
    
    /**
     * Checks if the queen makes a linear or diagonal move, and that there are 
     * no pieces between its starting location and ending location. A linear 
     * move is a move where the column or row changes, but not both. A diagonal 
     * move is a move where the piece moves the same amount of squares up/down 
     * as it does right/left.
     * 
     * @return true if this is a legal queen move
     */
    public boolean isLegalQueenMove()
    {
        boolean legalQueenMove = false;
        
        if(isLegalBishopMove() || isLegalRookMove())
            return true;
        
        return legalQueenMove;
    }
    
    /**
     * Check if the king moved 1 or 0 squares in any direction (including 
     * diagonally).
     * 
     * @return true if this is a legal king move
     */
    public boolean isLegalKingMove()
    {
        boolean legalKingMove = false;
        
        if(Math.abs(rowChange)<2 && Math.abs(columnChange)<2)
            return true;
        
        // if the king moves twice to the right it is a castle move
        if(legalCastleMove())
            return true;
        
        return legalKingMove;
    }
    
    /**
     * Test if this is a legal king move (not including castle moves testing for
     * check). A legal king move is one where the king moves 1 square in any 
     * direction (up, down, left, right, or diagonally).
     * 
     * @return If this is a legal king move not including castling or testing 
     * for check.
     */
    public boolean isLegalKingMoveBasic()
    {
        boolean legalKingMove = false;
        
        if(Math.abs(rowChange)<2 && Math.abs(columnChange)<2)
            return true;
        
        return legalKingMove;
    }
    
    /**
     * Test if a move is a legal castle move. A legal castle move is where a 
     * king (that has not moved all game) moves 2 spaces left or right towards a
     * rook (that has not moved all game). There can be no pieces between the 
     * king and the rook. Also, the king cannot be in check and cannot pass 
     * through check.
     * 
     * @return if this move is a legal castle move.
     */
    public boolean legalCastleMove()
    {
        String moveDirection = "";
        Location checkForPieces = new Location();
        int increment = 0;
        //Board tempBoard = new Board();
        
        // check if the king moved left or right 2 squares
        if(columnChange == 2)
        {
            moveDirection = "right";
            increment = 1;
        }
        if(columnChange == -2)
        {
            moveDirection = "left";
            increment = -1;
        }
        if(!(columnChange == 2 || columnChange == -2))
            return false;
        
        // make sure the king stayed on the same row
        if(rowChange != 0)
            return false;
        
        // check if the king has moved
        if(this.originalPiece.hasMoved)
            return false;
        
        // check if the king is in check or would pass through check
        if(this.board.isCheck())    // currently in check
            return false;
        if(this.isInCheckAfter())   // would be in check
            return false;
        if(this.board.opponentCanMoveTo(new Location(originalLocation.row, originalLocation.column + increment)))   // passes through check
            return false;
        
        // find where the rook should be
        if(moveDirection.equals("left"))
        {
            rookStart.row = originalLocation.row;
            rookStart.column = originalLocation.column - 4;
        }
        if(moveDirection.equals("right"))
        {
            rookStart.row = originalLocation.row;
            rookStart.column = originalLocation.column + 3;
        }
        
        // check if there is a rook where it should be, and if it has moved
        if(this.board.pieceAt(rookStart).pieceType.equals("rook"))
        {
            if(this.board.pieceAt(rookStart).hasMoved)
                return false;
        }
        else 
            return false;
        
        // check if there are any pieces between the king and rook
        checkForPieces.setLocation(originalLocation);
        checkForPieces.column += increment;
        while(!checkForPieces.sameLocation(rookStart))
        {
            if(this.board.pieceAt(checkForPieces).notNull())
                return false;
            
            checkForPieces.column += increment;
        }
        
        // ! PASSED ALL TESTS !
        
        // set rook end location
        rookEnd.row = originalLocation.row;
        rookEnd.column = originalLocation.column + increment;
        isCastleMove = true;
        return true;
    }
    
    
    /**
     * Check if a king made a legal castle move. A legal castle move is one 
     * where the king (and rook) attempting to move have not moved before. There
     * must also be no pieces in between the king and rook. The king must also 
     * not be in check. The king also cannot move through/to any square that 
     * would put it in check.
     * 
     * @return If the player made a legal castle move.
     */
//    public boolean legalCastleMove()
//    {
//        Location rookLocation = new Location();
//        Move kingToRook;
//        
//            //this.rookEnd = new Location();
//            //this.rookStart = new Location();
//        
//            // check that the king isn't in check, doesn't switch rows, and hasn't moved
//            /*if(this.board.opponentCanMoveTo(originalLocation))
//                return false;*/
//        
//        if(rowChange != 0)
//            return false;
//        if(this.originalPiece.hasMoved)
//            return false;
//        
//        // used to determine where the rook is
//        rookLocation.row = newLocation.row;
//        
//        // the king moved twice to the right
//        if(columnChange==2)
//        {
//            // check if the king moves to/passes through check
//            Location tempLocation = new Location(originalLocation.row, originalLocation.column+1);
//            if(this.board.opponentCanMoveTo(tempLocation))
//            {
//                    // Test
//                    System.out.print("Error, opponent can move to: ");
//                    tempLocation.printChessNotation();
//                
//                return false;
//            }
//            tempLocation.column++;
//            if(this.board.opponentCanMoveTo(tempLocation))
//                return false;
//            
//            // locate the rook. This is stored so the board knows where to move the rook from
//            rookLocation.column = newLocation.column + 1;
//            this.rookEnd.column = originalLocation.column + 1;
//            
//        }
//        // the king moved twice to the left
//        if(columnChange==-2)
//        {
//            // check if the king moves to/passes through check
//            Location tempLocation = new Location(originalLocation.row, originalLocation.column-1);
//            if(this.board.opponentCanMoveTo(tempLocation))
//            {
//                    // Test
//                    System.out.print("Error, opponent can move to: ");
//                    tempLocation.printChessNotation();
//                    
//                return false;
//            }
//            tempLocation.column--;
//            if(this.board.opponentCanMoveTo(tempLocation))
//                return false;
//            
//            // locate the rook. This is stored so the board knows where to move the rook from
//            rookLocation.column = newLocation.column - 2;
//            this.rookEnd.column = originalLocation.column -1;
//        }
//        
//        // Check the rook
//        if(!board.pieceAt(rookLocation).pieceType.equals("rook"))
//            return false;
//        if(board.pieceAt(rookLocation).hasMoved)
//            return false;
//        
//        // Check if there are any pieces between the king and rook
//        kingToRook = new Move(originalLocation, rookLocation);
//        kingToRook.board = this.board;
//        if(kingToRook.anyPieceBetweenLinear())
//            return false;
//        
//        // Let's the board know that this is a castle move.
//        // Also states where the rook should move from and to
//        this.isCastleMove = true;
//        this.rookStart = rookLocation;
//        this.rookEnd.row = originalLocation.row;
//        
//        // test
//        System.out.println("made it to end");
//        
//        return true;
//    }
    
    /**
     * Check if any piece is between the starting or ending location either 
     * vertically or horizontally. This method should only be used if the row 
     * change or column change is 0.
     * 
     * @return true if any piece is between the start and end locations
     */
    public boolean anyPieceBetweenLinear()
    {
        boolean anyPiece = false;
        
        Location tempLocation = new Location();
        
        tempLocation.setLocation(this.originalLocation);
        
        // the piece moved right
        if (this.columnChange > 0)
        {
            tempLocation.column++;
            while (tempLocation.differentLocation(this.newLocation))
            {
                if (this.board.pieceAt(tempLocation).notNull())
                    return anyPiece = true;
                tempLocation.column++;
            }
        }
        // the piece moved left
        if (this.columnChange < 0)
        {
            tempLocation.column--;
            while (tempLocation.differentLocation(this.newLocation))
            {
                if (this.board.pieceAt(tempLocation).notNull())
                    return anyPiece = true;
                tempLocation.column--;
            }
        }
        
        // the piece moved up
        if (this.rowChange > 0)
        {
            tempLocation.row++;
            while (tempLocation.differentLocation(this.newLocation))
            {
                if (this.board.pieceAt(tempLocation).notNull())
                    return anyPiece = true;
                tempLocation.row++;
            }
        }
        // the piece moved down
        if (this.rowChange < 0)
        {
            tempLocation.row--;
            while (tempLocation.differentLocation(this.newLocation))
            {
                if (this.board.pieceAt(tempLocation).notNull())
                    return anyPiece = true;
                tempLocation.row--;
            }
        }
        
        return anyPiece;
    }
    
    public boolean noPieceBetweenLinear()
    {
        return !this.anyPieceBetweenLinear();
    }
    
    /**
     * Check if a move is diagonal or not. A diagonal move is a move where the 
     * (absolute) change in columns is equal to the (absolute) change in rows.
     * 
     * @return true if this is a diagonal move
     */
    public boolean isDiagonalMove()
    {
        if(rowChange == 0 && columnChange == 0)
            return false;
        if(Math.abs(rowChange) == Math.abs(columnChange))
            return true;
        return false;
    }
    
    /**
     * Check if a move is linear or not. A linear move is a move where the 
     * piece changes columns (columnChange>0), or rows (rowChange>0), but not 
     * both.
     * 
     * @return true if this is a linear move
     */
    public boolean isLinearMove()
    {
        if(Math.abs(rowChange) >0 && columnChange==0)
            return true;
        if(Math.abs(columnChange) >0 && rowChange==0)
            return true;
        return false;
    }
    
    /**
     * Checks if there are any pieces diagonally between the original piece 
     * location, and the new location
     * 
     * @return true if there is a piece diagonally between the move-from 
     * location and the move-to location
     */
    public boolean anyPieceBetweenDiagonal()
    {
        if(!isDiagonalMove())
            return true;
        
        boolean anyPiece = false;
        
        Location tempLocation = new Location();
        tempLocation.setLocation(this.originalLocation);
        
        int columnIteration = 0;
        int rowIteration = 0;
        
        columnIteration = (columnChange>0)? 1 : -1;
        rowIteration = (rowChange>0)? 1 : -1;
        
        tempLocation.column += columnIteration;
        tempLocation.row += rowIteration;
        
        while(tempLocation.differentLocation(this.newLocation))
        {
            if (this.board.pieceAt(tempLocation).notNull())
                return anyPiece = true;
            tempLocation.column += columnIteration;
            tempLocation.row += rowIteration;
        }
        
        return anyPiece;  
    }
    
    /**
     * Returns true if there are no pieces diagonally between the starting and 
     * ending location.
     * 
     * @return true if no pieces are diagonally between the starting and ending 
     * locations
     */
    public boolean noPieceBetweenDiagonal()
    {
        return (!anyPieceBetweenDiagonal());
    }
    
    /**
     * Determines if a player would be in check after a certain move is made.
     * 
     * @return if after this move the player is in check.
     */
    public boolean isInCheckAfter()
    {
        Board newState = new Board();
        newState.setBoard(this.board);
        newState.move(this);
        if(newState.isCheck())
            return true;
        return false;
    }
    
    // test
        // Note: Should go unused. Likely could delete
    public void checkIfCastleMove()
    {
        if(originalPiece.pieceType.equals("king"))
            if(legalCastleMove())
            {
                isCastleMove = true;
                rookStart.row = originalLocation.row;
                rookEnd.row = originalLocation.row;
                if(columnChange > 0)
                {
                    rookStart.column = originalLocation.column + 3;
                    rookEnd.column = originalLocation.column + 1;
                }
                else
                {
                    rookStart.column = originalLocation.column - 4;
                    rookEnd.column = originalLocation.column - 1;
                }
            }
    }
    
    /**
     * Prints the starting and ending location to the console (using chess 
     * notation). Example: "Move from: E2 to: E4" 
     */
    public void print()
    {
        System.out.print("Move from: ");
        this.originalLocation.printChessNotation();
        System.out.print(" to: ");
        this.newLocation.printChessNotation();
    }
    
}
