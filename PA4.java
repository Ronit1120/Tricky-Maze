import java.util.*;

public class PA4 { //public variables for # rows & columns, start/end position and the board
    public static int row;
    public static int col;
    public static int startCoord;
    public static int endCoord;
    public static char[][] board;
   
    final public static int[] DX = {-1, 1, 0, 0}; //Maze moveSet x & y 
    final public static int[] DY = {0, 0, -1, 1};

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        row = input.nextInt();
        col = input.nextInt();

        board = new char[row][col];

        for(int i = 0; i < row; i++){ //input board
            board[i] = input.next().toCharArray();
        }

        startCoord = find('*');
        endCoord = find('$');

        int answer = bfs(startCoord, endCoord);

        if(answer == -1){
            System.out.println("Call 911");
        }
        else{
            System.out.println(answer);
        }

    }

    public static int bfs(int start, int end){
        LinkedList<Integer> queue = new LinkedList<>(); 
        int[] visitedcount = new int[row * col]; 

        Arrays.fill(visitedcount, -1); // -1 means not visited

        queue.add(start); //adds start point to queue
        visitedcount[start] = 0; //makes it 0 because it's considered step 0, so not -1 since not visited

        while(queue.size() > 0){
            int cur = queue.poll();//puts start point in queue immediately  

            if(cur == end) // Sees if start is end
                return visitedcount[cur];
            
            for(int i = 0; i < DX.length; i++){ //goes to all other locations

                int nextX = cur / col + DX[i];  //1d to 2d for x (backwards thinking of i * col in find func) with x posibilities
                int nextY = cur % col + DY[i];  //1d to 2d for y (backwards thinking of + j in find func) with y posibilities

                //check if possible pos is in parameters
                if(inbounds(nextX, nextY) == false) 
                    continue;
                if(board[nextX][nextY] == '!')
                    continue;
                if(visitedcount[nextX * col + nextY] != -1)
                    continue;

                //if gets here then pos is possible
                visitedcount[nextX * col + nextY] = visitedcount[cur] + 1; 

                queue.add(nextX * col + nextY);

                if (Character.isLetter(board[nextX][nextY])) { // sees if the char at the next position is a teleport point using next potential spot from earlier one in func (using calculation from lab)

                    char teleportChar = board[nextX][nextY];// if it is a teleport point it takes val of teleportChar and uses it in teleport function

                    teleport(nextX, nextY, teleportChar, visitedcount, queue); // wrapper function
                }

            }

        }
        return -1;
    }

    public static void teleport(int xPos, int yPos, char teleportChar, int[] visitedcount, LinkedList<Integer> queue) { //used for teleport 
        for(int i = 0; i < row; i++){

            for(int j = 0; j < col; j++){ 

                if(board[i][j] == teleportChar && (i != xPos || j != yPos)) { //Sees if board char matches teleportChar and is not the same location as current teleport position

                    int newPos = i * col + j; // new position if there is a possible way to teleport (1d) which is same calculation in find func

                    if(visitedcount[newPos] == -1) {// updates step number/visited array if not visited using same calculation as bfs func

                        visitedcount[newPos] = visitedcount[xPos * col + yPos] + 1;
                        queue.add(newPos); // adds new position to queue
                    }

                }

            }
        }
    }

    public static boolean inbounds(int x, int y) { // makes sure pos is inBounds
        return x >= 0 && x < row && y >= 0 && y < col;
    }

    public static int find(char c) { // finds 1d position from 2d array
        for(int i = 0; i < row; i++){

            for(int j = 0; j < col; j++){

                if(board[i][j] == c){
                    return i * col + j; 
                }
            }
        }
        return -1;
    }
}
