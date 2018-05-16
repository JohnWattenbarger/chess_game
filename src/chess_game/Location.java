/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_game;

/**
 * Contains the column and row location of a piece.
 * 
 * @author John
 */
public class Location 
{
    public int column;
    public int row;
    
    /**
     * Creates an empty location
     */
    public Location() {}
    
    /**
     * Get the column and row of a location on a chess board. The chess board 
     * is an 8x8 array. The row and column should each be an int from 0 to 7. 
     * Example: column = 5, row = 7 would be: board[7][5]
     * 
     * @param row an int from 0 to 7
     * @param column an int from 0 to 7
     */
    public Location(int row, int column)
    {
        this.column = column;
        this.row = row;
    }
    
    /**
     * Takes a location in the chess notation form (ex: 'a3'), and converts it 
     * to array form (ex: 03). columnChar is a char from 'a' to 'h'. row is an 
     * int from 1 to 8.
     * 
     * @param columnChar a char from 'a' to 'h'
     * @param row a row from 1 to 8
     */
    public Location(char columnChar, int row)
    {
        column = convertColumnToArrayForm(columnChar);
        this.column = column;
        
        this.row = convertRowToArrayForm(row);
    }
    
    /**
     * Converts the user inputted row to its row in an array. The user inputs 
     * locations in the form 'a3', where the 2nd digit is the row. The user row 
     * will be from 1 to 8, where 1 is the bottom of the board and 8 is the top 
     * of the board. The converted form will be from 0 to 7, where 7 is the 
     * bottom of the board and 0 is the top of the board. For example: if the 
     * user inputted 'a3', the row would be converted from 3 to 5 (8-3).
     * 
     * @param row The row inputted from the user (8 to 1)
     * @return The location's row in an array (0 to 7)
     */
    public int convertRowToArrayForm(int row)
    {
        return 8-row;
    }
    
    /**
     * Converts the user inputted column to its column in an array. The user 
     * inputs locations in the form 'a3', where the 1st digit is the column. The
     * user column will be from 'a' to 'h', where 'a' is the left of the board 
     * and 'h' is the right of the board. The converted form will be from 0 to 
     * 7, where 0 is the left of the board and 7 is the right of the board. For 
     * example: if the user inputted 'a3', the column would be converted from 
     * 'a' to 0.
     * 
     * @param column The column inputted from the user ('a' to 'h')
     * @return The location's column in an array (0 to 7)
     */
    public int convertColumnToArrayForm(char column)
    {
        column = Character.toLowerCase(column);
        return column-'a';
    }
    
    /**
     * Set location equal to a different location. Changes the column and row 
     * of this location to equal the column and row of the inputted location.
     * 
     * @param newLocation The location this one will equal.
     */
    public void setLocation(Location newLocation)
    {
        this.column = newLocation.getColumn();
        this.row = newLocation.getRow();
    }
    
    /**
     * Sets this location to the designated row and column.
     * 
     * @param row an int from 0 to 7
     * @param column an int from 0 to 7
     */
    public void setLocation(int row, int column)
    {
        this.column = column;
        this.row = row;
    }
    
    /**
     * 
     * @return this.column
     */
    private int getColumn()
    {
        return this.column;
    }
    
    /**
     * 
     * @return this.row
     */
    private int getRow()
    {
        return this.row;
    }
    
    /**
     * Prints this location's column and row to the console.
     */
    public void print()
    {
        System.out.println("    Location Column: " + this.column);
        System.out.println("    Location row: " + this.row);
    }
    
    /**
     * ! Incomplete
     * Convert from 07 to a2
     */
    public void printChessNotation()
    {
        char columnCharacter;
        int row;
        
        row = 8-this.row;
        columnCharacter = Character.valueOf((char)(this.column+65));
        
        System.out.print(columnCharacter);
        System.out.println(Integer.toString(row));
    }
    
    public boolean sameLocation(Location otherLocation)
    {
        if (this.column == otherLocation.column)
            if (this.row == otherLocation.row)
                return true;
        return false;
    }
    
    public boolean differentLocation(Location otherLocation)
    {
        return !this.sameLocation(otherLocation);
    }
    
    /**
     * Check if: 0<=row<=7 and 0<=column<=7
     * 
     * @return true if the rows and columns are between 0 and 7 
     */
    public boolean isOnBoard()
    {
        if(this.column <8 && this.column >= 0)
            if(this.row <8 && this.row >= 0)
                return true;
        return false;
    }
    
    /**
     * Checks if this location has the same row and column as another location.
     * 
     * @param otherLocation The location to compare to
     * @return True if both locations have the same row and column.
     */
    public boolean equals(Location otherLocation)
    {
        if(this.column == otherLocation.column)
            if(this.row == otherLocation.row)
                return true;
        return false;
    }
}
