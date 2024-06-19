package dungeonmania;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class DungeonGenerator {
    Position start;
    Position end;
    int width;
    int height;

    private Boolean[][] DungeonArray;
    DungeonGenerator(Position start, Position end) {
        width = Math.abs(start.getX() - end.getX()) + 1;
        height = Math.abs(start.getY() - end.getY()) + 1;
        this.start = start;
        this.end = end;
        DungeonArray = new Boolean[height][width];
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++)
                DungeonArray[i][j] = false;
        }
    }
    public List<EntityResponse> generate(){
        int start_x;
        int start_y;
        if (start.getY() < end.getY()) {
            if (start.getX() > end.getX()){
                start_x = width - 1;
                start_y = 0;
                DungeonArray[start_y][start_x] = true;
            }
            else {
                start_x = 0;
                start_y = 0;
                DungeonArray[start_y][start_x] = true;
            }
        } 
        else {
            if (start.getX() > end.getX()){
                start_x = width - 1;
                start_y = height - 1;
                DungeonArray[start_y][start_x] = true;
            }
            else {
                start_x = 0;
                start_y = height - 1;
                DungeonArray[start_y][start_x] = true;
            }
        }
        List<Position> options = new ArrayList<>();
        // all cardinal neighbour of distance 2
        // above 2
        if (start_y - 2 >= 0 ) {
            if (DungeonArray[start_y - 2][start_x] == false)
                options.add(new Position(start_x, start_y - 2));
        }
        // right 2
        if (start_x + 2 < width) {
            if (DungeonArray[start_y][start_x + 2] == false)
                options.add(new Position(start_x + 2, start_y));
        }
        // left 2
        if (start_x - 2 >= 0) {
            if (DungeonArray[start_y][start_x - 2] == false)
                options.add(new Position(start_x - 2, start_y));
        }
        // below 2
        if (start_y + 2 < height) {
            if (DungeonArray[start_y + 2][start_x] == false)
                options.add(new Position(start_x, start_y + 2));
        }
        while (options.size() != 0) {
            Random random = new Random();
            int removed_index = random.nextInt(options.size());
            List<Position> neighbours = new ArrayList<>();
            Position next = options.get(removed_index);
            int removed_X = next.getX();
            int removed_Y = next.getY();
            // all cardinal neighbour of distance 2
            // above 2
            if (removed_Y - 2 >= 0 ) {
                if (DungeonArray[removed_Y - 2][removed_X] == true)
                    neighbours.add(new Position(removed_X, removed_Y - 2));
            }
            // right 2
            if (removed_X + 2 < width) {
                if (DungeonArray[removed_Y][removed_X + 2] == true)
                    neighbours.add(new Position(removed_X + 2, removed_Y));
            }
            // left 2
            if (removed_X - 2 >= 0) {
                if (DungeonArray[removed_Y][removed_X - 2] == true)
                    neighbours.add(new Position(removed_X - 2, removed_Y));
            }
            // below 2
            if (removed_Y + 2 < height) {
                if (DungeonArray[removed_Y + 2][removed_X] == true)
                    neighbours.add(new Position(removed_X, removed_Y + 2));
            }
            if (neighbours.size() != 0) {
                removed_index = random.nextInt(neighbours.size());
                Position neighbour = neighbours.get(removed_index);
                DungeonArray[removed_Y][removed_X] = true;
                int x_moved = neighbour.getX() - removed_X;
                int y_moved = neighbour.getY() - removed_Y;
                if (x_moved == 0) {
                    if (y_moved < 0)
                        DungeonArray[removed_Y - 1][removed_X] = true;
                    else
                        DungeonArray[removed_Y + 1][removed_X] = true;
                }
                else {
                    if (x_moved < 0)
                        DungeonArray[removed_Y][removed_X - 1] = true;
                    else
                        DungeonArray[removed_Y][removed_X + 1] = true;
                }
                DungeonArray[neighbour.getY()][neighbour.getX()] = true;
                neighbours.remove(neighbour);
            }
            neighbours = new ArrayList<>();
            // all cardinal neighbour of distance 2
            // above 2
            if (removed_Y - 2 >= 0 ) {
                if (DungeonArray[removed_Y - 2][removed_X] == false)
                    neighbours.add(new Position(removed_X, removed_Y - 2));
            }
            // right 2
            if (removed_X + 2 < width) {
                if (DungeonArray[removed_Y][removed_X + 2] == false)
                    neighbours.add(new Position(removed_X + 2, removed_Y));
            }
            // left 2
            if (removed_X - 2 >= 0) {
                if (DungeonArray[removed_Y][removed_X - 2] == false)
                    neighbours.add(new Position(removed_X - 2, removed_Y));
            }
            // below 2
            if (removed_Y + 2 < height) {
                if (DungeonArray[removed_Y + 2][removed_X] == false)
                    neighbours.add(new Position(removed_X, removed_Y + 2));
            }
            options.addAll(neighbours);
            options.remove(next);
        }
        int end_x;
        int end_y;
        if (start_x == 0)
            end_x = width - 1;
        else
            end_x = 0;
        if (start_y == 0)
            end_y = height - 1;
        else
            end_y = 0;
        if (DungeonArray[end_y][end_x] == false)
            DungeonArray[end_y][end_x] = true;
        List<Position> neighbours = new ArrayList<>();
        // all cardinal neighbour of distance 1
        // above 1
        if (end_y - 1 >= 0 ) {
            if (DungeonArray[end_y - 1][end_x] == false)
                neighbours.add(new Position(end_x, end_y - 1));
        }
        // right 1
        if (end_x + 1 < width) {
            if (DungeonArray[end_y][end_x + 1] == false)
                neighbours.add(new Position(end_x + 1, end_y));
        }
        // left 1
        if (end_x - 1 >= 0) {
            if (DungeonArray[end_y][end_x - 1] == false)
                neighbours.add(new Position(end_x - 1, end_y));
        }
        // below 1
        if (end_y + 1 < height) {
            if (DungeonArray[end_y + 1][end_x] == false)
                neighbours.add(new Position(end_x, end_y + 1));
        }
        if (neighbours.size() != 0) {
            Random random = new Random();
            Position neighbour_to_make_empty = neighbours.get(random.nextInt(neighbours.size()));
            DungeonArray[neighbour_to_make_empty.getY()][neighbour_to_make_empty.getX()] = true;
        }
        List<EntityResponse>  entities = new ArrayList<>();
        int id = 2;
        for (int i = 0; i < height + 2; i++) {
            for (int j = 0; j < width + 2; j++) {
                Position current_pos = new Position(start.getX() + (j - 1), start.getY() + (i - 1), 0);
                if (i == 0 || i == height + 1 || j == 0 || j == width + 1){
                    entities.add(new EntityResponse( String.valueOf(id), "wall", current_pos, false));
                    id ++;
                }
                else {
                    if (DungeonArray[i - 1][j - 1] == false) {
                        entities.add(new EntityResponse( String.valueOf(id), "wall", current_pos, false));
                        id ++;
                    }
                }
                
            }
        }

        entities.add(new EntityResponse("0", "player", start, false));
        entities.add(new EntityResponse("1", "exit", end, false));
        return entities; 
    }
    public Boolean[][] getDungeonArray() {
        return DungeonArray;
    }
    public static void main(String[] args) {
        DungeonGenerator gen = new DungeonGenerator(new Position(0, 0), new Position(7, 7));
        gen.generate();
        Boolean [][] array = gen.getDungeonArray();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (array[i][j] == false)
                    System.out.print("[W]");
                else
                    System.out.print("[ ]");
            }
            System.out.println("");
        }
    }
}
