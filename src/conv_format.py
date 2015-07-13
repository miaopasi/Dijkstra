# Convert Format
import os

def convert_format(file_path):
	# file_name = os.path.basename(file_path)
	# file_pre = file_name.split('.')[0];
	# rec_file_path = os.path.join(os.path.dirname(file_path), file_pre+"_conv.json")
	f_src = open(file_path)
	# f_dst = open(rec_file_path, 'w')
	count = 0;
	for l in f_src.readlines():
		
		if "ADJ" in l:
			count += 1;

		# f_dst.write(l);
		# f_dst.flush();
	f_src.close();
	print count
	# f_dst.close();

convert_format("./mutiple_update_2.json")