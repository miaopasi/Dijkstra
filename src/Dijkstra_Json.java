import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;



public class Dijkstra_Json {
	// Redundant variables just for easier writing.
  	public List<String> reference = new ArrayList<String>();
  	public List<Vertex> map_vertices = new ArrayList<Vertex>();
  	public Map<Long,String> poi_vertices = new HashMap<Long,String>();
  	public static double[] map_param = new double[]{4266.0,1556.0,120.0,-674.0,10.0};
	private static int switchmark = 0;
  	
  	
  	public void _load_config(String map_path, String poi_path) throws IOException{
	      String json_str = readinJson(map_path);
//	      System.out.println(json_str);
	      JsonTest(json_str);
	      json_str = readinJson(poi_path);
	      PoiExtract(json_str);
//	      ShowMap();
	  }
  	
  	
  	private void PoiExtract(String json_string){
  		JsonParseUtil json_parser = new JsonParseUtil();
  		Map<String,Object> res = json_parser.parse(json_string);
  		for(String v: res.keySet()){
  			List<String> vert = (List<String>)res.get(v);
  			poi_vertices.put(Long.parseLong(v), vert.get(0));
  		}
  	}
  	
  	//File Read In
    private String readinJson(String file_path) throws IOException{
  	  BufferedReader br = new BufferedReader(new FileReader(file_path));
  	  try {
  	        StringBuilder sb = new StringBuilder();
  	        String line = br.readLine();

  	        while (line != null) {
  	            sb.append(line);
  	            sb.append(System.lineSeparator());
  	            line = br.readLine();
  	        }
  	        String everything = sb.toString();
  	        return everything;
  	    } 
  	  finally {
  	        br.close();
  	    }
    }
    
    
	// Extrac Json Strings Out
	private void JsonTest(String json_string){
		JsonParseUtil json_parser = new JsonParseUtil();
		Map<String, Object> res = json_parser.parse(json_string);
		System.out.println("Num " + res.size());
		for(String v: res.keySet()){

			Vertex tb = new Vertex(v);
			tb.adjacencies = new ArrayList<Edge>();

			Map<String, Object> obj = (Map<String,Object>) res.get(v);
			// Get Vertex Location
			List<String> pos_str = (ArrayList<String>)obj.get("LOC");
			String[] pos = pos_str.get(0).split(",");
			double[] temp = {
								Double.parseDouble((String) pos[0]),
								Double.parseDouble((String) pos[1])
								};
			tb.location = temp;
			if(!reference.contains(v)){
				reference.add(v);
				map_vertices.add(tb);
			}
			Map<String, Object> obj2 = (Map<String,Object>)obj.get("ADJ");
			for(String vn: obj2.keySet()){
				Map<String, Object> adj_obj = (Map<String, Object>) obj2.get(vn);
				if(reference.contains(vn)){
					continue;
				}
				else{

					Vertex tx = new Vertex(vn);
					tx.adjacencies = new ArrayList<Edge>();
					// Get Vertex Location
					List<String> pos_str_n = (ArrayList<String>)adj_obj.get("LOC");
					String[] pos_n = pos_str_n.get(0).split(",");
					double[] temp_n = {
										Double.parseDouble((String) pos_n[0]),
										Double.parseDouble((String) pos_n[1])
										};
					tx.location = temp_n;
					reference.add(vn);
					map_vertices.add(tx);
				}
			}
		}

		for(String v: res.keySet()){

			Map<String, Object> obj = (Map<String,Object>) res.get(v);
			// Get Vertex Location
			int vert_ind = reference.indexOf(v);


			Map<String, Object> obj2 = (Map<String,Object>)obj.get("ADJ");


			//	  		System.out.println("Output: Test: " + v + ";"+map_vertices.get(reference.indexOf(v)).name);

			for(String vn: obj2.keySet()){
				System.out.println(v + "; " + vn);
				Map<String, Object> adj_obj = (Map<String, Object>) obj2.get(vn);
				double dis = Double.parseDouble((String)adj_obj.get("DIS"));
				map_vertices.get(reference.indexOf(v)).adjacencies.add(new Edge(map_vertices.get(reference.indexOf(vn)), dis));
				map_vertices.get(reference.indexOf(vn)).adjacencies.add(new Edge(map_vertices.get(vert_ind),dis));
			}

		}
	}

	//Display the json content
	private void ShowMap(){
	for(Vertex v : map_vertices){
		System.out.println(v.name+" : "+v.location[0]+","+v.location[1]+" ; ");
		System.out.println("\tEDGES");
		for(Edge e: v.adjacencies){
			System.out.println("\t"  + e.target.name + " ; " + e.weight);
		}

	}
	}

	// Unit Test Function


	private static double[] convert_coord(double[] loc){
	  double [] convert = new double[]{0,0};
	  /*
	   * 				pos_x = (int(res[x_key]['post'][0]) - self.PData[2]) / self.PData[4]
			pos_y = (self.PData[1] - int(res[x_key]['post'][1]) - self.PData[3]) / self.PData[4]
	   */
	  convert[0] = (loc[0] - map_param[2]) / map_param[4];
	  convert[1] = (map_param[1] - loc[1] - map_param[3]) / map_param[4];
	  return convert;
	}

	private static String get_block_json(Vertex v, Map<String,String>mark, String last_mark, int count){
		String output_json = "";
		String this_mark = "";
		for(String m : mark.keySet()) {
			if (v.name.contains(m)) {
				this_mark = m;
				break;
			}
		}
//		System.out.println("This mark: "+ this_mark+ "  Last Mark "+ last_mark);
		if(!last_mark.equals(this_mark)){
			//Different block
			output_json += "\n\t],\n";
			output_json += "\t\""+mark.get(this_mark)+switchmark+"\":[\n";
			switchmark++;
			output_json += "\t\t{\n";
			output_json += "\t\t\"Name\":\"" + v.name + "\",\n";
			double[] loc = v.location;
			output_json += "\t\t\"Location\":[\"" + loc[0] + "\",\"" + loc[1] + "\"],\n";
			output_json += "\t\t\"Distance\":\"" + (v.minDistance - v.previous.minDistance) + "\"\n\t}";
		}
		else {
			// Same block
//			output_json += "\t"+mark.get(this_mark)+":[\n";
			if(count == 0){
				output_json += "\t\t{\n";
			}
			else {
				output_json += ",\n\t\t{\n";
			}
			output_json += "\t\t\"Name\":\"" + v.name + "\",\n";
			double[] loc = v.location;
			output_json += "\t\t\"Location\":[\"" + loc[0] + "\",\"" + loc[1] + "\"],\n";
			if (count == 0) {
				output_json += "\t\t\"Distance\":\"" + v.minDistance + "\"\n\t}\n";
			}
			else {

				output_json += "\t\t\"Distance\":\"" + (v.minDistance - v.previous.minDistance) + "\"\n\t}";
			}
		}
		return output_json;
	}




	// Output to Json Format
	public static String _to_json(List<Vertex> path){
		String output_json = "{\n";
		int count = 0;
		String last_mark = "";
		Map<String, String> mark = new HashMap<String, String>();
		mark.put("v","101001170003");
		mark.put("q","101001170002");
		last_mark = path.get(0).name.substring(0,1);
		System.out.println("LastMark:"+last_mark);
		output_json += "\t\""+mark.get(last_mark)+switchmark+"\":[\n";
		switchmark++;
		for(Vertex v: path){
			if(count == 0){
				if(v.minDistance == Double.POSITIVE_INFINITY){
					return "";
				}
			}
			output_json += get_block_json(v, mark, last_mark, count);
			last_mark = v.name.substring(0,1);
			System.out.println("Last_Mark:" + last_mark);
			count += 1;
		}
		output_json += "\t]\n}\n";
		path.clear();
		switchmark = 0;
		return output_json;
		}
	}


/*
	DUMPED CODE:
	From to_json(){
			//		  for(Vertex v: path){
		//			  output_json += "\t\""+count+"\":{\n";
		//			  output_json += "\t\t\"Name\":\""+v.name+"\",\n";
		//			  output_json += "\t\t\"Location\":[\"" + v.location[0]+"\",\""+v.location[1]+"\"],\n";
		//			  if(count == 0){
		//				  output_json += "\t\t\"Distance\":\"" + v.minDistance +"\"\n\t},\n";
		//			  }
		//			  else if(count == path.size()-1){
		//				  output_json += "\t\t\"Distance\":\"" + (v.minDistance - v.previous.minDistance) +"\"\n\t}\n";
		//			  }
		//			  else{
		//
		//				  output_json += "\t\t\"Distance\":\"" + (v.minDistance - v.previous.minDistance) +"\"\n\t},\n";
		//			  }
		//			  count += 1;
		//		  }
		//		  output_json += "\t\""+"array"+"\":[\n";
		}
 */
