import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Dijkstra_Unit_Test {
    /*
     * createGraph and buildGraph For Unit Test
     * 
     */
    public static int[][] createGraph(int NodeSize, int AveConnection){
    	int[][] conn = new int[NodeSize][NodeSize];
    	int i,j;
    	for (i = 0 ; i < NodeSize; i++){
    		for ( j = 0; j < NodeSize; j++){
    			conn[i][j] = 0;
    		}
    	}
    	
    	Random rand = new Random();
    	int vibration = (int)(NodeSize / 5.0);
    	for(i = 0 ; i < NodeSize; i++){
    		int conn_num = rand.nextInt(vibration*2) - vibration + AveConnection;
    		int count = 0;
    		while(count < conn_num){
    			int tar = rand.nextInt(NodeSize);
    			if(conn[i][tar]==0){
    				count += 1;
    				conn[i][tar] = rand.nextInt(1000)+1;
    				conn[tar][i] = conn[i][tar];
    			}
    		}
    	}
    	return conn;
    }
    
    public static List<Vertex> buildGraph(int NodeSize, int[][] conn)
    {
    	List<Vertex> vertices = new ArrayList<Vertex>();
    	Random rand = new Random();
    	for(int i = 0; i < NodeSize; i++){
    		Vertex tb = new Vertex("Vertex"+i);
    		tb.adjacencies = new ArrayList<Edge>();
    		tb.location = new double[] {rand.nextDouble()*1000, rand.nextDouble()*1000};
    		vertices.add(tb);
    	}
    	for(int i = 0; i < NodeSize; i++){
    		Vertex tb = vertices.get(i);
    		for(int j = 0; j < NodeSize; j++){
    			if(conn[i][j] != 0){
    				tb.adjacencies.add(new Edge(vertices.get(j), conn[i][j]));
    			}
    		}
    	}
    	return vertices;
    }
    
    public static void validate_path(List<Vertex> path){
    	for(int i = 1; i < path.size(); i++){
    		Vertex v = path.get(i);
    		Vertex vp = v.previous;
    		for(Edge e: vp.adjacencies){
    			if(e.target == v){
    				System.out.println("MinDis: " + v.minDistance+"," + vp.minDistance + " ; Conn: "+e.weight);
    			}
    		}
    	}
    }
}
