package core;

import entities.Entity;

/**
 * Prints every single entity and its location.
 * Intended for to check for equally located entities.
 */


public class Test_XY {
    public static void main(String[] args) {


        Board board = new Board(new BoardConfig());

        System.out.println(board);


        System.out.println(board.getEntitySet());

        /*
        for(Entity entity: board.getEntities())
            if(entity != null)
                System.out.println(entity);
*/
    }


}
