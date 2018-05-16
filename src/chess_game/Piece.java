package chess_game;

import java.awt.*;
import java.io.*;
import javax.imageio.*;

/**
 * An object that contains the color, type, and more of a chess piece. 
 * 
 * @author John
 */
public class Piece 
{
    public String color;
    public String pieceType;
    public int value;
    public boolean hasMoved;
    public Image picture;
    
    // create a new piece with c as the color (w/b) and p as the piece type
    // ' ', p, n, b, r, k, q
    /**
     * Creates a piece with a white or black color (inputted as 'w' or 'b'). 
     * Each piece must also have a type: pawn, rook, knight, bishop, queen, or 
     * king (inputted as 'p', 'r', 'n', 'b', 'q', or 'k').
     * 
     * @param c The color of the piece ('w' or 'b')
     * @param p The type of the piece ('p', 'r', 'n', 'b', 'q', or 'k')
     */
    public Piece(char c, char p)
    {
        this.setColor(c);
        this.setPieceType(p);
        this.setImage();
        
        hasMoved = false;
    }
    
    /**
     * Setup a piece using the actual color and piece type.
     * 
     * @param color
     * @param pieceType 
     */
    public Piece(String color, String pieceType)
    {
        this.color = color;
        this.pieceType = pieceType;
        this.setImage();
        
        hasMoved = false;
    }
    
    /**
     * Creates a blank piece. has color "none" and piece type " ".
     */
    public Piece()  // empty constructor
    {
        color = "none";
        pieceType = " ";
        value = 0;
        hasMoved = false;
        picture = null;
    }
    
    public void setPiece(Piece otherPiece)
    {
        this.color = otherPiece.color;
        this.pieceType = otherPiece.pieceType;
        this.value = otherPiece.value;
        this.hasMoved = otherPiece.hasMoved;
        this.picture = otherPiece.picture;
    }
    
    /**
     * Sets the color of a piece. Changes 'w' to "white" and 'b' to "black".
     * 
     * @param c color character 'w' or 'b'
     */
    private void setColor(char c)
    {
        if (c=='w' || c=='W')
            color = "white";
        else if (c=='b' || c=='B')
            color = "black";
        else
            color = "none";
    }
    
    /**
     * Sets the type of a piece. Changes from 'p' to "pawn", 'r' to "rook", 'n' 
     * to "knight", 'b' to "bishop", 'q' to "queen", 'k' to "king".
     * 
     * @param p type of a piece 'p', 'r', 'n', 'b', 'q', or 'k'
     */
    private void setPieceType(char p)
    {
        switch(p)
        {
            case 'p': pieceType = "pawn"; value=1; break;
            case 'n': pieceType = "knight"; value=3; break;
            case 'b': pieceType = "bishop"; value=3; break;
            case 'r': pieceType = "rook"; value=5; break;
            case 'k': pieceType = "king"; value=9999; break; 
            case 'q': pieceType = "queen"; value=9; break;
            default: pieceType = " "; value=0;
        }
    }
    
    /**
     * 
     * @return this.color
     */
    public String getColor()
    {
        return this.color;
    }
    
    /**
     * 
     * @return this.pieceType
     */
    public String getPieceType()
    {
        return this.pieceType;
    }
    
    /**
     * Check if a piece is null
     * 
     * @return true if the piece has no type (pieceType is " ")
     */
    public boolean isNull()
    {
        return this.getPieceType().equals(" ");
    }
    
    /**
     * Check if there is a piece (not null).
     * 
     * @return true if the piece has a type (pieceType is not " ")
     */
    public boolean notNull()
    {
        return !(this.getPieceType().equals(" "));
    }
    
    /**
     * Check if this piece is the same color as another piece.
     * 
     * @param other Piece object to check with
     * @return True if this and other have the same color
     */
    public boolean sameColorAs(Piece other)
    {
        return this.getColor().equals(other.getColor());
    }
    
    /**
     * Checks if one piece is white and the other is black. If so, return true. 
     * Else, return false.
     * 
     * @param other The piece to compare to this one
     * @return if true if one piece is white and the other black
     */
    public boolean oppositeColorAs(Piece other)
    {
        if (this.color.equals("white") && other.color.equals("black"))
            return true;
        if (this.color.equals("black") && other.color.equals("white"))
            return true;
        
        return false;
    }
    
    private void setImage()
    {
        try
        {
            if (color.compareTo("white")==0)
                switch(pieceType)
                {
                    case "pawn": picture = 
                            ImageIO.read(new File("Images/PawnWhite.png")); break;
                    case "knight": picture = 
                            ImageIO.read(new File("Images/KnightWhite.png")); break;
                    case "bishop": picture = 
                            ImageIO.read(new File("Images/BishopWhite.png")); break;
                    case "rook": picture = 
                            ImageIO.read(new File("Images/RookWhite.png")); break;
                    case "king": picture = 
                            ImageIO.read(new File("Images/KingWhite.png")); break;
                    case "queen": picture = 
                            ImageIO.read(new File("Images/QueenWhite.png")); break;
                }
            else
                switch(pieceType)
                {
                    case "pawn": picture = 
                            ImageIO.read(new File("Images/PawnBlack.png")); break;
                    case "knight": picture = 
                            ImageIO.read(new File("Images/KnightBlack.png")); break;
                    case "bishop": picture = 
                            ImageIO.read(new File("Images/BishopBlack.png")); break;
                    case "rook": picture = 
                            ImageIO.read(new File("Images/RookBlack.png")); break;
                    case "king": picture = 
                            ImageIO.read(new File("Images/KingBlack.png")); break;
                    case "queen": picture = 
                            ImageIO.read(new File("Images/QueenBlack.png")); break;
                }
            }
            catch (Throwable e)
            {System.out.print(e.getMessage());}
    }
    
    public void printPiece()
    {
        if (color.compareTo("white")==0)
            switch(pieceType)
            {
                case "pawn": System.out.print('P'); break;
                case "knight": System.out.print('N'); break;
                case "bishop": System.out.print('B'); break;
                case "rook": System.out.print('R'); break;
                case "king": System.out.print('K'); break;
                case "queen": System.out.print('Q'); break;
                default: System.out.print(' ');
            }
        else
            switch(pieceType)
            {
                case "pawn": System.out.print('p'); break;
                case "knight": System.out.print('n'); break;
                case "bishop": System.out.print('b'); break;
                case "rook": System.out.print('r'); break;
                case "king": System.out.print('k'); break;
                case "queen": System.out.print('q'); break;
                default: System.out.print(' ');
            }
    }
    
    public Image getImage()
    {
        return picture;
    }
    
    
}   // end class Piece
