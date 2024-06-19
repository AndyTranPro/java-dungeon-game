// //The BFS algo is from the following website: https://www.geeksforgeeks.org/bfs-or-dfs-for-shortest-path-problem/ 
// package dungeonmania.movingEntity;
// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Iterator;
// import java.util.LinkedList;
// import java.util.List;

// import dungeonmania.Entity;
// import dungeonmania.util.Direction;
// import dungeonmania.util.Position;

// public class DijkstraAlgoPathFinder implements Serializable{
//     // range would be the like a diameter for the graph that dijstra would run.
//     private int range = 60;
//     private int v = range * range;
//     private int left = range/2;
//     private int top = range/2;

//     public DijkstraAlgoPathFinder() {}

//     public Direction findNextPath(Entity movingEntity, Position playerPos) {
//         ArrayList<ArrayList<Integer>> graph = buildGraph(movingEntity);
//         //early exit if there is no path to player
//         if (graph == null) {
//             return null;
//         }
//         //Position playerPos = movingEntity.getDungeonInfo().getPlayer().getPos();
//         int playerX = playerPos.getX();
//         int playerY = playerPos.getY();
//         int diffPlayerXToEntity = playerX - movingEntity.getPos().getX();
//         int diffPlayerYToEntity = playerY - movingEntity.getPos().getY(); 
//         int start = top*range + left;
//         int dest = (top + diffPlayerYToEntity)*range + left + diffPlayerXToEntity;
//         List<Integer> path = printShortestDistance(graph, start, dest, v);
//         if (path.size() == 0) {
//             return null;
//         }
//         if (path.get(1) == start - range) {
//             // if direction is UP
//             return Direction.UP;
//         } else if (path.get(1) == start + 1) {
//             // if direction is RIGHT
//             return Direction.RIGHT;
//         } else if (path.get(1) == start - 1) {
//             // if direction is LEFT
//             return Direction.LEFT;
//         } else if (path.get(1) == start + range) {
//             // if direction is DOWN
//             return Direction.DOWN;
//         } 
//         return null;
//     }

//     public Direction findNextPathAwayPlayer(Entity movingEntity) {
//         ArrayList<ArrayList<Integer>> graph = buildGraph(movingEntity);
//         //early exit if there is no path to player
//         if (graph == null) {
//             return null;
//         }
//         Position playerPos = movingEntity.getDungeonInfo().getPlayer().getPos();
//         int playerX = playerPos.getX();
//         int playerY = playerPos.getY();
//         int diffEntityToPlayerX = movingEntity.getPos().getX() - playerX;
//         int diffEntityToPlayerY = movingEntity.getPos().getY() - playerY; 
//         int start = top*range + left;
//         int dest = (top + diffEntityToPlayerX)*range + left + diffEntityToPlayerY;
//         List<Integer> path = printShortestDistance(graph, start, dest, v);
//         if (path.size() == 0) {
//             return null;
//         }
//         if (path.get(1) == start - range) {
//             // if direction is UP
//             return Direction.UP;
//         } else if (path.get(1) == start + 1) {
//             // if direction is RIGHT
//             return Direction.RIGHT;
//         } else if (path.get(1) == start - 1) {
//             // if direction is LEFT
//             return Direction.LEFT;
//         } else if (path.get(1) == start + range) {
//             // if direction is DOWN
//             return Direction.DOWN;
//         } 
//         return null;
//     }
 
//     public ArrayList<ArrayList<Integer>> buildGraph(Entity e) {
//         List<String> movingConstrintItemList = Arrays.asList("wall", "boulder");
//         ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>(v);
//         for (int i = 0; i < v; i++) {
//             adj.add(new ArrayList<Integer>());
//         }
        
//         Position topLeftCorner = e.getPos().translateBy(-left,-top); 
//         int x = topLeftCorner.getX();
//         int x1 = 0;
//         //check if player is in range, if he is out of range, then return null.
//         Position playerPos = e.getDungeonInfo().getPlayer().getPos();
//         if(!(playerPos.getX() >= topLeftCorner.getX() && 
//         playerPos.getX() <= topLeftCorner.getX() + range && 
//         playerPos.getY() >= topLeftCorner.getY() && 
//         playerPos.getY() <= topLeftCorner.getY() + range
//         )) {
//             return null;
//         }
//         int y = topLeftCorner.getY();
//         while (y < topLeftCorner.getY() + range -1) {
//             x = topLeftCorner.getX();
//             int y1 = 0;
//             // scan all the vertex around the mercenary and build a graph
//             while (x < topLeftCorner.getX() + range -1) {
//                 // check if the position is a wall or a boulder, and positions on the right and bottom
//                 if (!(e.getDungeonInfo().getEntitiesStringByPosition(new Position(x,y)).stream().anyMatch(element -> movingConstrintItemList.contains(element)))) {
//                     List<String> right_pos_entites = e.getDungeonInfo().getEntitiesStringByPosition(new Position(x+1,y));
//                     // if the right position is not a wall or a boulder, then add the edge to the graph.
//                     if (!right_pos_entites.stream().anyMatch(element -> movingConstrintItemList.contains(element))) {
//                         addEdge(adj, x1*range+y1, x1*range+y1+1);
//                     }
//                     List<String> bottom_pos_entites = e.getDungeonInfo().getEntitiesStringByPosition(new Position(x,y+1));
//                     // if the bottom position is not a wall or a boulder, then add the edge to the graph.
//                     if (!bottom_pos_entites.stream().anyMatch(element -> movingConstrintItemList.contains(element))) {
//                         addEdge(adj, x1*range+y1, x1*range+y1+range);
//                     }
//                 } 
//                 x++;
//                 y1++;
//             }
//             y++;
//             x1++;
//         }
//         return adj;
//     }
 
//     // function to form edge between two vertices
//     // source and dest
//     public void addEdge(ArrayList<ArrayList<Integer>> adj, int i, int j)
//     {
//         adj.get(i).add(j);
//         adj.get(j).add(i);
//     }
 
//     // function to print the shortest distance and path
//     // between source vertex and destination vertex
//     public List<Integer> printShortestDistance(
//                      ArrayList<ArrayList<Integer>> adj,
//                              int s, int dest, int v)
//     {
//         // predecessor[i] array stores predecessor of
//         // i and distance array stores distance of i
//         // from s
//         int pred[] = new int[v];
//         int dist[] = new int[v];

//         List<Integer> pathInt = new ArrayList<Integer>();
 
//         if (BFS(adj, s, dest, v, pred, dist) == false) {
//             // System.out.println("Given source and destination" +
//             //                              "are not connected");
//             return pathInt;
//         }
 
//         // LinkedList to store path
//         LinkedList<Integer> path = new LinkedList<Integer>();
//         int crawl = dest;
//         path.add(crawl);
//         while (pred[crawl] != -1) {
//             path.add(pred[crawl]);
//             crawl = pred[crawl];
//         }
 
 
//         // Print path
//         //System.out.println("Path is ::");
//         for (int i = path.size() - 1; i >= 0; i--) {
//             //System.out.print(path.get(i) + " ");
//             pathInt.add(path.get(i));
//         }
//         return pathInt;
//     }
 
//     // a modified version of BFS that stores predecessor
//     // of each vertex in array pred
//     // and its distance from source in array dist
//     private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src,
//                                   int dest, int v, int pred[], int dist[])
//     {
//         // a queue to maintain queue of vertices whose
//         // adjacency list is to be scanned as per normal
//         // BFS algorithm using LinkedList of Integer type
//         LinkedList<Integer> queue = new LinkedList<Integer>();
 
//         // boolean array visited[] which stores the
//         // information whether ith vertex is reached
//         // at least once in the Breadth first search
//         boolean visited[] = new boolean[v];
 
//         // initially all vertices are unvisited
//         // so v[i] for all i is false
//         // and as no path is yet constructed
//         // dist[i] for all i set to infinity
//         for (int i = 0; i < v; i++) {
//             visited[i] = false;
//             dist[i] = Integer.MAX_VALUE;
//             pred[i] = -1;
//         }
 
//         // now source is first to be visited and
//         // distance from source to itself should be 0
//         visited[src] = true;
//         dist[src] = 0;
//         queue.add(src);
 
//         // bfs Algorithm
//         while (!queue.isEmpty()) {
//             int u = queue.remove();
//             for (int i = 0; i < adj.get(u).size(); i++) {
//                 if (visited[adj.get(u).get(i)] == false) {
//                     visited[adj.get(u).get(i)] = true;

//                     dist[adj.get(u).get(i)] = dist[u] + 1;
//                     pred[adj.get(u).get(i)] = u;
//                     queue.add(adj.get(u).get(i));

//                     // stopping condition (when we find
//                     // our destination)
//                     if (adj.get(u).get(i) == dest)
//                         return true;
//                 }
//             }
//         }
//         return false;
//     }
// }

