import java.io.IOException;


public class Call_Main {
    public static void main(String[] args) throws IOException
    {
        String file_path = "/Users/admin/Documents/workspace/Dijkstra/src/sample_json_connect.json";
        String poi_path = "/Users/admin/Documents/workspace/Dijkstra/src/HRWCC_F1_F2.json";
        Dijkstra dj = new Dijkstra();
//        dj.get_path_unit_test(file_path);
        dj.load_config(file_path, poi_path);
        long source_ind = 1010011700030000L;
        long target_ind = 1010011700030008L;
        String res_json = dj.get_path_poi(source_ind, target_ind);
        System.out.println(res_json);
        System.out.println("=====================================================");
//
//
//        source_ind = 1010011700030031L;
//        target_ind = 1010011700030032L;
//        res_json = dj.get_path_poi(source_ind, target_ind);
//        System.out.println(res_json);
//        System.out.println("=====================================================");
//
//        source_ind = 1010011700030008L;
//        target_ind = 1010011700030007L;
//        res_json = dj.get_path_poi(source_ind, target_ind);
//        System.out.println(res_json);
//        System.out.println("=====================================================");
//
//        source_ind = 1010011700030004L;
//        target_ind = 1010011700030009L;
//        res_json = dj.get_path_poi(source_ind, target_ind);
//        System.out.println(res_json);
//        System.out.println("=====================================================");
//
//        source_ind = 1010011700030007L;
//        target_ind = 1010011700030009L;
//        res_json = dj.get_path_poi(source_ind, target_ind);
////        String res_json = dj.get_path(10, 57);
//        System.out.println(res_json);
//        System.out.println("=====================================================");
        
    }
}
