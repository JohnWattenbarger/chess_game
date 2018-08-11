/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author John
 * 
 * Creates a graphical display of this Chess application
 */
public class ChessFrame implements ActionListener
{
    JFrame thisFrame = new JFrame();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    int height, width;
    Color labelTextColor = new Color(50, 30, 25);
    Color buttonBackgroundColor = new Color(220, 170, 125);
    Color backgroundColor = new Color(180, 225, 250);
    Font labelFont = new Font(Font.DIALOG, Font.PLAIN, screenHeight/20);
    Font buttonFont = new Font(Font.DIALOG, Font.PLAIN, screenHeight/25);
// chess board
    Board board = new Board();
    Color whiteSquare = new Color(220, 220, 220);
    Color blackSquare = new Color(150, 120, 50);
    Color selectedSquare = new Color(20, 220, 220);
    int selectedSquareNumber;
    Color selectedSquareColor;
// JPanel to switch between which JPanel is displayed in the frame
    JPanel panelSwitcher;
    CardLayout cardLayout;
// menu screen
    JPanel panel1 = new JPanel();
    GroupLayout groupLayout;
    private JLabel titleScreen;
    private JButton newGame, test;
// new game screen
    JLayeredPane panel2;
    JPanel panelUnder;
    JPanel panelOver;
    JButton[] pieceButtons = new JButton[64]; // each chess piece is a button
    ImageIcon image;    // temp image
    int boardWidth, boardHeight, boardX, boardY, pieceWidth, pieceHeight;
    int boardDepth, pieceDepth, currentPieceDepth;
    JLabel message;
    JLabel statusMessage;
// practice panel
    JPanel testPanel = new JPanel();
    JButton b1, b2;
    JLabel l1, l2;
    
    Location moveFrom = null;
    Location moveTo;
    
    public ChessFrame()
    {
        setupFrame();
        setupMainMenu();
        setupNewGame();
        setupTestPanel();
        setupPanelSwitcher();
     
        thisFrame.add(panelSwitcher, BorderLayout.CENTER);
        thisFrame.setVisible(true);
    }

    // Where button clicks are handled
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
        if (e.getSource() == newGame)
            cardLayout.show(panelSwitcher, "panel2");
        
        // can be removed?
        if (e.getSource() == test)
            panel2.add(test);
            
        // action after a piece button is clicked
        for(int i=0; i<pieceButtons.length; i++)
        {
            if (e.getSource() == pieceButtons[i])
                if (moveFrom == null)
                {
                    moveFrom = new Location(i/8, i%8);
                    
                    if(board.pieceAt(moveFrom).isNull())
                        moveFrom = null;
                    
                    // Change the color of the moveFrom location
                    else{
                        selectedSquareColor = pieceButtons[i].getBackground();
                        pieceButtons[i].setBackground(selectedSquare);
                        selectedSquareNumber = i;
                    }
                }
                else
                {
                    moveTo = new Location(i/8, i%8);
                    
                    // Reset the color of the moveFrom location
                    pieceButtons[selectedSquareNumber].setBackground(
                            selectedSquareColor);
                    
                    board.userMove(moveFrom, moveTo);
                    moveFrom = null;
                    synchBoards();
                    
                    statusMessage.setText(board.errorMessage);
                    board.errorMessage = "";
                    
                    message.setText(board.turnColor + "'s turn");
                    if (board.isCheckmate())
                    {
                        message.setText(board.turnColor + " wins!");
                        statusMessage.setText("Checkmate");
                    }
                }
        }
    }   
    
    /**
     * Sets up the frame that contains all panels. Sets the height, width, font,
     * and allows it to terminate the program when closed.
     */
    private void setupFrame()
    {
        thisFrame.setLayout(new BorderLayout());
        thisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        thisFrame.setSize(screenWidth, screenHeight);
        thisFrame.setLocation(0, 0);
        thisFrame.setTitle("Chess Game");
        thisFrame.setFont(new Font(Font.DIALOG, Font.PLAIN, screenHeight/100));
        
        height = thisFrame.getHeight();
        width = thisFrame.getWidth();
    }
    
    /**
     * Adds all components to the main menu. The main menu is the first panel 
     * that appears. It has a title at the top, followed by buttons that lead 
     * to other game modes.
     */
    private void setupMainMenu()
    {
        groupLayout = new GroupLayout(panel1);
        panel1.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        panel1.setBackground(backgroundColor);
        
        // Main Menu Components
        titleScreen = new JLabel("Chess");
        titleScreen.setForeground(labelTextColor);
        titleScreen.setFont(labelFont);
        panel1.add(titleScreen);
        
        newGame = new JButton("New Game");
        newGame.setFont(buttonFont);
        newGame.addActionListener(this);
        
        // not used (could be replaced later for other buttons
        test = new JButton("Test");
        test.setFont(buttonFont);
        test.addActionListener(this);
        
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleScreen)
                .addComponent(newGame)
                //.addComponent(test)
        );
        
        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addComponent(titleScreen)
                .addComponent(newGame)
                //.addComponent(test)
        );
    }
    
    /**
     * Creates the panel with the chess game. Sets up the board, and pieces.
     */
    private void setupNewGame()
    {
        // set sizes
        boardWidth = screenWidth*2/3;
        boardHeight = screenHeight*2/3;
        boardX = screenWidth*1/8;
        boardY = screenHeight*1/8;
        pieceWidth = boardWidth*1/8;
        pieceHeight = boardHeight*1/8;
        
       // setup panel2
        panel2 = new JLayeredPane();
        panel2.setLayout(null);
        
       // set the pane depths
        boardDepth = 1;
        pieceDepth = 2;
        currentPieceDepth = 3;
        
       // board setup
        // create an image with Board.getImage() picture
        image = new ImageIcon(
            board.getImage().getScaledInstance(boardWidth, 
            boardHeight,Image.SCALE_DEFAULT));
        
       // pieces setup
        // initializes each button and adds it to this panel
        for(int i=0; i<pieceButtons.length; i++)
        {
            pieceButtons[i] = new JButton();
            pieceButtons[i].addActionListener(this);
            panel2.add(pieceButtons[i], pieceDepth);
        }
        
        // converts the piece images to buttons
        for(int i=0; i<pieceButtons.length; i++)
        {
            if(board.board[i/8][i%8].getImage()!=null)
            {
                image = new ImageIcon(
                    board.board[i/8][i%8].getImage().getScaledInstance(
                    pieceWidth, pieceHeight,Image.SCALE_DEFAULT));
                pieceButtons[i].setIcon(image);
            }    
            
            // create a blank icon image
            else
            {
                pieceButtons[i].setText("");
                image = new ImageIcon();
                pieceButtons[i].setIcon(image);
            }
            if(i/8%2 == 0)
                    if(i%2 == 0)
                        pieceButtons[i].setBackground(whiteSquare);
                    else
                        pieceButtons[i].setBackground(blackSquare);
                else
                    if(i%2 == 1)
                        pieceButtons[i].setBackground(whiteSquare);
                    else
                        pieceButtons[i].setBackground(blackSquare);
        }        
        // displays each piece/button
        for(int i=0; i<pieceButtons.length; i++)
        {
            if(pieceButtons[i].getIcon()!=null)
            {
                pieceButtons[i].setSize(pieceWidth, pieceHeight);
                pieceButtons[i].setLocation(boardX + (i%8)*pieceWidth, boardY + 
                        (i/8)*pieceHeight);
            }
        }
        
        // adds a label to display whose turn it is
        message = new JLabel("TEST");
        message.setSize(boardWidth/2, boardHeight/10);
        message.setLocation(boardX + boardWidth/2, boardY - boardHeight/8);
        message.setFont(labelFont);
        panel2.add(message);
        
        // adds a 2nd label for status updates (i.e. check, illegal move, etc.)
        statusMessage = new JLabel(" ");
        statusMessage.setSize(boardWidth/2, boardHeight/10);
        statusMessage.setLocation(boardX + boardWidth/2, boardY + boardHeight + boardHeight/32);
        statusMessage.setFont(labelFont);
        panel2.add(statusMessage);
        
        panel2.setBackground(new Color(180, 240, 250));
        panel2.setOpaque(true);
    }
    
    /**
     * Adds all panels to the cardLayout. Displays the 1st panel in the 
     * cardLayout.
     */
    private void setupPanelSwitcher()
    {
        panelSwitcher = new JPanel(new CardLayout());
        cardLayout = (CardLayout) panelSwitcher.getLayout();
        panelSwitcher.add(panel1, "panel1");
        panelSwitcher.add(panel2, "panel2");
        panelSwitcher.add(testPanel, "testPanel");
        cardLayout.show(panelSwitcher, "panel1"); // displays the 1st panel
    }
    
    private void setupTestPanel()
    {
        testPanel.setLayout(new FlowLayout());
        testPanel.setSize(screenSize);
        testPanel.setLocation(0, 0);
        testPanel.setBackground(Color.green);
        l1 = new JLabel("Label 1");
        b1 = new JButton("Button 1");
        l2 = new JLabel ("Label 2");
        b2 = new JButton ("Button 2");
        testPanel.add(l1);
        testPanel.add(b1);
        testPanel.add(l2);
        testPanel.add(b2);
    }
    
    /**
     * Starts the frame/game
     */
    public void run()
    {
        synchBoards();
        message.setText(board.turnColor + "'s turn");
    }
    
    /**
     * Updates this frame to match the internal board
     */
    public void synchBoards()
    {
        for(int i=0; i<pieceButtons.length; i++)
        {
            if(board.board[i/8][i%8].getImage()!=null)
            {
                image = new ImageIcon(
                    board.board[i/8][i%8].getImage().getScaledInstance(
                    pieceWidth, pieceHeight,Image.SCALE_DEFAULT));
                pieceButtons[i].setIcon(image);
            }    
            
            // create a blank icon image
            else
            {
                pieceButtons[i].setText("");
                image = new ImageIcon();
                pieceButtons[i].setIcon(image);
            }
        }
    }
}