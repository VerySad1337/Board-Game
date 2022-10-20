/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arabqn2;

/**
 *
 * @author Eric
 */
public class ArabQn2 {

    static char[][] m_QuestBoard = new char[20][20];
    static int m_iMyHealth = 100;
    static int m_iMyWeapons = 0; // No idea for what it is for yet... He was on drugs after all so...
    static int m_iCurX = 1;
    static int m_iCurY = 1;
    static Boolean m_bSteppedOnPotion = false;
    
    // Tracking variables
    static int m_iNumTotalUncoveredResources = 0;
    static int m_iNumTotalThreatsEncountered = 0;
    static int m_iNumTotalFoodsUncovered = 0;
    static int m_iNumTotalWeaponsUncovered = 0;
    static int m_iNumTotalHealthsUncovered = 0;
    static int m_iNumTotalStepsTakenToFindPotion = 0;
    
    public static void main(String[] args)
    {      
        
        System.out.print("Initializing quest board... ");
        
        initializeGameBoard(m_QuestBoard);
        
        System.out.println("Done");
        
        int iMovesLeft = (int)(Math.random()*75)+1+25;
        
        System.out.println("Starting the game!");
        System.out.println("Total moves available in this game: " + iMovesLeft);
        
        // Lets start from a random location
        m_iCurX = (int)(Math.random()*20)+1;
        m_iCurY = (int)(Math.random()*20)+1;
        
        System.out.println("Wizzard starting at location X: " + m_iCurX + " Y: " + m_iCurY);
        
        do // while loop of the game, moving each step
        {
            m_iNumTotalStepsTakenToFindPotion++;
            
            makeAMove(m_QuestBoard); // Move (Teleport) to the next location
            //makeAMoveByOne(m_QuestBoard); // EXTENSION: Walk to nearby location by 1 box
            
            updateHealth(m_QuestBoard);
            iMovesLeft--;
            
            System.out.println("Step: " + m_iNumTotalStepsTakenToFindPotion);
            System.out.println("Health: " + m_iMyHealth);
            System.out.println("Weapons: " + m_iMyWeapons);
            
            for (int x = 0; x < 20; x++)
            {
                for (int y = 0; y < 20; y++)
                {
                    System.out.print(m_QuestBoard[x][y] + " ");
                }
                
                System.out.println("");
            }
            
            System.out.println("");
            System.out.println("");
            
            m_QuestBoard[m_iCurX-1][m_iCurY-1] = 'E'; // We empty the slot each move
        }
        while (iMovesLeft > 0 && m_iMyHealth > 0 && m_bSteppedOnPotion == false);
        // ^ We continue to do steps, while we have more moves left AND we still have some health AND we are not on The Potion slot
        
        checkForResult(m_QuestBoard);
    }
    
    public static void checkForResult(char[][] aQuestBoard)
    {
        if (m_bSteppedOnPotion)
        {
            System.out.println("You Won!!!");
            System.out.println("Total Steps Taken To Find The Potion: " + m_iNumTotalStepsTakenToFindPotion);
        }
        else
        {
            System.out.println("You Lost");
        }
        
        System.out.println("Total Resources Uncovered: " + m_iNumTotalUncoveredResources);
        System.out.println("Total Threats Enouctered: " + m_iNumTotalThreatsEncountered);
        System.out.println("Total Foods Uncovered: " + m_iNumTotalFoodsUncovered);
        System.out.println("Total Weapons Uncovered: " + m_iNumTotalWeaponsUncovered);
        System.out.println("Total Healths Uncovered: " + m_iNumTotalHealthsUncovered);
    }
    
    public static void makeAMove(char[][] aQuestBoard)
    {
       m_iCurX = (int)(Math.random()*20)+1;
       m_iCurY = (int)(Math.random()*20)+1;
       
       if (aQuestBoard[m_iCurX-1][m_iCurY-1] == 'P') // Lets aknowledge the while loop of the game that stepped on a potion and stop the game
           m_bSteppedOnPotion = true;
    }
    
    public static void updateHealth(char[][] aQuestBoard)
    {
        char cSlotType = aQuestBoard[m_iCurX-1][m_iCurY-1];
        if (cSlotType == 'G') // giant threat, decreases health strength to 0
        {
            m_iMyHealth = 0;
            m_iNumTotalThreatsEncountered++;
        }
        else if (cSlotType == 'S') // sinking sand threat, decreasing health by 3
        {
            m_iMyHealth -= 3;
            m_iNumTotalThreatsEncountered++;
        }
        else if (cSlotType == 'K') // killer tree threat, decreasing health by 5
        {
            m_iMyHealth -= 5;
            m_iNumTotalThreatsEncountered++;
        }
        else if (cSlotType == 'C') // critter threat, decreasing health by 5
        {
            m_iMyHealth -= 5;
            m_iNumTotalThreatsEncountered++;
        }
        else if (cSlotType == 'F') // food resource, increasing health by 5
        {
            Boolean bThreatNearby = false;
            
            // Checking all the 8 boxes around us
            // We need to make sure we dont check out of boundaries, java freaked on me before and took me awhile to find out why...
            if (m_iCurX > 1 && isThreat(aQuestBoard[(m_iCurX-1) - 1][(m_iCurY-1) - 0])) // Left
                bThreatNearby = true;
            if (m_iCurX < 20 && isThreat(aQuestBoard[(m_iCurX-1) + 1][(m_iCurY-1) - 0])) // Right
                bThreatNearby = true;
            if (m_iCurY > 1 && isThreat(aQuestBoard[(m_iCurX-1) - 0][(m_iCurY-1) - 1])) // Up
                bThreatNearby = true;
            if (m_iCurY < 20 && isThreat(aQuestBoard[(m_iCurX-1) - 0][(m_iCurY-1) + 1])) // Down
                bThreatNearby = true;
             if (m_iCurX > 1 && m_iCurY > 1 && isThreat(aQuestBoard[(m_iCurX-1) - 1][(m_iCurY-1) - 1])) // Left/Up
                bThreatNearby = true;
             if (m_iCurX > 1 && m_iCurY < 20 && isThreat(aQuestBoard[(m_iCurX-1) - 1][(m_iCurY-1) + 1])) // Left/Down
                bThreatNearby = true;
             if (m_iCurX < 20 && m_iCurY > 1 && isThreat(aQuestBoard[(m_iCurX-1) + 1][(m_iCurY-1) - 1])) // Right/Up
                bThreatNearby = true;
             if (m_iCurX < 20 && m_iCurY < 20 && isThreat(aQuestBoard[(m_iCurX-1) + 1][(m_iCurY-1) + 1])) // Right/Down
                bThreatNearby = true;
            
             if (bThreatNearby)
                m_iMyHealth -= 5;
             else
                m_iMyHealth += 5;
             
             m_iNumTotalFoodsUncovered++;
             m_iNumTotalUncoveredResources++;
        }
        else if (cSlotType == 'W') // weapons resource, increasing weapons by 1
        {
            m_iMyWeapons += 1;
            
            m_iNumTotalWeaponsUncovered++;
            m_iNumTotalUncoveredResources++;
        }
        else if (cSlotType == 'M') // health resource, increasing health by 10
        {
            m_iMyHealth += 10;
            
            m_iNumTotalHealthsUncovered++;
            m_iNumTotalUncoveredResources++;
        }
    }
    
    public static Boolean isThreat(char cType)
    {
        if (cType == 'G') // giant threat
            return true;
        
        if (cType == 'S') // sinking sand threat
            return true;
        
        if (cType == 'K') // killer tree threat
            return true;
        
        if (cType == 'C') // critter threat
            return true;
        
        return false;
    }
    
    public static void makeAMoveByOne(char[][] aQuestBoard)
    {
       int iDirection = (int)(Math.random()*2); // 0 = Left/Right, 1 = Up/Down
       if (iDirection == 0) // 0 = Left/Right
       {
           if (m_iCurX == 1) // We are on beggining boundary, so lets just move right
           {
               m_iCurX = 2;
           }
           else if (m_iCurX == 20) // We are on ending boundary, so lets just move left
           {
               m_iCurX = 19;
           }
           else // No boundary, so lets move randomly left or right
           {
                int iDirectionX = (int)(Math.random()*2);
                if (iDirectionX == 0)
                    m_iCurX--;
                else
                    m_iCurX++;
           }
       }
       else if (iDirection == 1) // 1 = Up/Down
       {
           if (m_iCurY == 1) // We are on beggining boundary, so lets just move down
           {
               m_iCurY = 2;
           }
           else if (m_iCurY == 20) // We are on ending boundary, so lets just move up
           {
               m_iCurY = 19;
           }
           else // No boundary, so lets move randomly up or down
           {
                int iDirectionY = (int)(Math.random()*2);
                if (iDirectionY == 0)
                    m_iCurY--;
                else
                    m_iCurY++;
           }
       }
       
       if (aQuestBoard[m_iCurX-1][m_iCurY-1] == 'P') // Lets aknowledge the while loop of the game that stepped on a potion and stop the game
           m_bSteppedOnPotion = true;
    }
    
    public static void initializeGameBoard(char[][] aQuestBoard)
    {
        // We make the whole board empty, so we fill it with E
        for (int x = 0; x < 20; x++)
        {
            for (int y = 0; y < 20; y++)
            {
                aQuestBoard[x][y] = 'E'; // make it empty
            }
        }
        
        // The resources
        randomlyFillBoardWith(aQuestBoard, 'F', 10); // Lets randomly fill the board with 10 foods resources
        randomlyFillBoardWith(aQuestBoard, 'W', 10); // Lets randomly fill the board with 10 weapons resources
        randomlyFillBoardWith(aQuestBoard, 'M', 10); // Lets randomly fill the board with 10 health resources
        
        // Now the assholes wanting to kill us...
        randomlyFillBoardWith(aQuestBoard, 'G', 5); // Lets randomly fill the board with 5 giants
        randomlyFillBoardWith(aQuestBoard, 'S', 5); // Lets randomly fill the board with 5 sinking sands
        randomlyFillBoardWith(aQuestBoard, 'K', 10); // Lets randomly fill the board with 10 killer trees
        randomlyFillBoardWith(aQuestBoard, 'C', 10); // Lets randomly fill the board with 10 critters
        
        // And ofcourse... our goal, The Potion!
        randomlyFillBoardWith(aQuestBoard, 'P', 1); // Lets randomly fill the board with 1 potion
    }
    
    public static void randomlyFillBoardWith(char[][] aQuestBoard, char aType, int iTypeCount)
    {
        for (int i = 0; i < iTypeCount; i++)
        {
            Boolean bFound = false;
            while (bFound == false)
            {
                int iPosX = (int)(Math.random()*20); // This will return us a number from 0 to 19
                int iPosY = (int)(Math.random()*20); // This will return us a number from 0 to 19
                if (aQuestBoard[iPosX][iPosY] == 'E') // is empty ?
                {
                    aQuestBoard[iPosX][iPosY] = aType; // put that thing here
                    bFound = true;
                }
            }
        }
    }    
}
